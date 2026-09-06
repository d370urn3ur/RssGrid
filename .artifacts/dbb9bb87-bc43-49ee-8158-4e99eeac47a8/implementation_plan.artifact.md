# Persist TabIndex across Navigation

The `tabIndex` in `AppContainer` is currently managed using a local `remember { mutableStateOf(...) }`. When navigating away from `AppContainer` and returning, the component is removed and re-added to the composition, causing the state to be reset to its initial value.

To persist this state, we will hoist the `tabIndex` state to `AppRouter`. Since `AppRouter` hosts the `NavDisplay` and remains in the composition during navigation between its children, the hoisted state will be preserved.

## Proposed Changes

### [Component Name] navigation

#### [MODIFY] [AppRouter.kt](file:///Users/jpierce/Projects/kmp/newsgrid/composeApp/src/commonMain/kotlin/the/autarch/newsgrid/navigation/AppRouter.kt)
- Hoist `tabIndex` state using `rememberSaveable`.
- Pass `tabIndex` and an update lambda to `AppContainer`.

#### [MODIFY] [AppContainer.kt](file:///Users/jpierce/Projects/kmp/newsgrid/composeApp/src/commonMain/kotlin/the/autarch/newsgrid/AppContainer.kt)
- Add `tabIndex` and `onTabIndexChanged` parameters to the `AppContainer` function.
- Remove the local `tabIndex` state.
- Use the passed-in `tabIndex` and call `onTabIndexChanged` in the `Tab`'s `onClick`.

## Verification Plan

### Manual Verification
- Deploy the app.
- Switch to the "Bookmarks" tab.
- Navigate to an entry details screen.
- Go back.
- Verify that the "Bookmarks" tab is still selected.
