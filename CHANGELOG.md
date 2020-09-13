Changelog
---

#### 1.0.5
- Added threading security to ServiceManager
    - Currently only warns the user in the logs if they are accessing a service from the wrong thread.
- Added a method in SceneTreeService to access currently selected node.

#### 1.0.6
- Provide the selected `TreeNode` from the `SceneTreeService`.
- Provide the main `Frame` from `WindowService`

#### 1.0.7
- Add the ability to run an `AppState`.
- Add annotations to an `AppState` to display a GUI to modify variables.