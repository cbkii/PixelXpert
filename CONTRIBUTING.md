Contributions are always welcome here!

## Quick workflow for contributors

- Fork this repository to your own account.
- Create a branch in your fork for your changes.
- Push that branch and open a Pull Request back to this repository.

## Running test-build workflows from a fork

You can run both test-build workflows directly in your fork from the **Actions** tab using **Run workflow**:

- `Make Test Package` (`.github/workflows/fullPackageTestBuild.yml`)
- `Make Xposed Test Package` (`.github/workflows/xposedPackageTestBuild.yml`)

These workflows build the branch/commit that triggered the run in the current repository, so they work for forks and branch-specific testing.

## Signed vs unsigned artifacts

If these repository secrets are set, the workflows produce signed artifacts:

- `SIGNING_KEY`
- `ALIAS`
- `KEY_STORE_PASSWORD`
- `KEY_PASSWORD`

If one or more signing secrets are missing (common in forks), signing is skipped and unsigned debug/test artifacts are uploaded instead.

## Expected artifacts and where to get them

After a workflow run finishes, open the run page in **Actions** and download artifacts from the **Artifacts** section.

- Full test package workflow:
  - Signed path: `PixelXpert_signed.apk`, `PixelXpert_signed.zip`
  - Unsigned path: `PixelXpert_unsigned_debug.apk`, `PixelXpert_unsigned_test.zip`
- Xposed test package workflow:
  - Signed path: `PixelXpert_Xposed_signed.apk`, `PixelXpert_Xposed_signed.zip`
  - Unsigned path: `PixelXpert_Xposed_unsigned_debug.apk`, `PixelXpert_Xposed_unsigned_test.zip`

## Recommended PR testing flow

Before opening a PR, run the relevant test-build workflow(s) on your fork branch to validate build output for your exact changes.
