package sh.siava.pixelxpert.xposed.modpacks.systemui;

import static de.robv.android.xposed.XposedHelpers.callMethod;
import static sh.siava.pixelxpert.xposed.XPrefs.Xprefs;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;

import de.robv.android.xposed.callbacks.XC_LoadPackage;
import sh.siava.pixelxpert.xposed.XposedModPack;
import sh.siava.pixelxpert.xposed.annotations.SystemUIModPack;
import sh.siava.pixelxpert.xposed.utils.toolkit.ReflectedClass;

@SuppressWarnings("RedundantThrows")
@SystemUIModPack
public class SlimSilentNotifications extends XposedModPack {

	private static boolean slimSilentNotifications = false;

	private int mHeightReduction;
	private int mMinHeight;

	public SlimSilentNotifications(Context context) {
		super(context);
	}

	@Override
	public void onPreferenceUpdated(String... Key) {
		slimSilentNotifications = Xprefs.getBoolean("slimSilentNotifications", false);
	}

	@Override
	public void onPackageLoaded(XC_LoadPackage.LoadPackageParam lpParam) throws Throwable {
		mHeightReduction = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 16f,
				mContext.getResources().getDisplayMetrics());
		mMinHeight = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 40f,
				mContext.getResources().getDisplayMetrics());

		ReflectedClass NotificationContentViewClass = ReflectedClass.of(
				"com.android.systemui.statusbar.notification.row.NotificationContentView");

		// Intercept the contracted-height assignment and reduce it for silent, collapsed rows.
		// The hook fires every time the notification stack recalculates row heights, so it
		// naturally re-applies on update and restores the full height when the toggle is off
		// or when the row is no longer silent / collapsed.
		NotificationContentViewClass
				.before("setSmallHeight")
				.run(param -> {
					if (!slimSilentNotifications) return;
					try {
						View contentView = (View) param.thisObject;
						Object row = contentView.getParent();
						if (row == null) return;

						// Only target silent (low-priority) section rows
						boolean isLowPriority = (Boolean) callMethod(row, "isLowPriority");
						if (!isLowPriority) return;

						// Leave expanded rows and heads-up rows unchanged
						boolean isExpanded = (Boolean) callMethod(row, "isExpanded");
						if (isExpanded) return;

						boolean isHeadsUp = (Boolean) callMethod(row, "isHeadsUp");
						if (isHeadsUp) return;

						int originalHeight = (int) param.args[0];
						param.args[0] = Math.max(originalHeight - mHeightReduction, mMinHeight);
					} catch (Throwable ignored) {}
				});
	}
}
