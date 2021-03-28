Changelog
---
### 1.3.0
- View can now be swap between dock and window
- Tools inherit Tool class

### 1.2.0
- The editors are now JPanel
- Simplify Texture2dEditor
- Remove javadoc no inspection

### 1.1.7
- The main page use zone instead of differents methods

#### 1.1.6
- Fix the project version misconfigured on bintray

#### 1.1.5

- Update gradle configuration for publishing on bintray
- Refactor package name
- Add color and inline for vector editor
- Refactor main windows loading and add Canvas test
- Fix propagation of different value to editor
- Move plugin to be displayed in tab and splitpane instead of dialog

#### 1.1.4

- Use Logback as SLF4J implementation
- Improve PropertyEditor gui

#### 1.1.3

- Refactor component to use canonical java PropertyEditor
- Replace reflection to find property editor by implementation using chain of responsibilities and
  strategy design pattern.

#### 1.1.2

- Fix international formatting

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
- Rename `DevkitPlugin` to `DevKitPlugin`.
- Switch plugin package requirement to `devkit.plugin`.
