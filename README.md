jMonkeyEngine Devkit Fork
===

A Software Development Kit created using a Java Swing UI with support for plugins and running AppStates from within
the DevKit.

### MAVEN/GRADLE

Add this repository : https://dl.bintray.com/exratio/jme-tool

Maven:
```xml
<dependency>
  <groupId>fr.exratio</groupId>
  <artifactId>jme-swing-devkit</artifactId>
  <version>1.1.6</version>
  <type>pom</type>
</dependency>
```
Gradle
> implementation 'fr.exratio:jme-swing-devkit:1.1.6'

### Latest Versions
- Latest Gradle Plugin Version: `0.0.21`
- Latest DevKit Version: `1.1.6`

### Features
- Import Models (glTF using JMEC)
- Create, Edit, and Save scenes to .j3o format.
- Change Materials and material properties on Geometries.
- Create SkyBoxes from images.
- Generate LightProbes.
- InstancedNode Support.
- BatchNode Support.
- AssetLinkNode Support.
- Plugin Support.
- Run AppStates from your project in the SDK with custom property editing.

### Use the SDK
To use the SDK add the gradle plugin to your project and run the gradle task `runSdk`

```groovy
plugins {
    id "com.jayfella.jme-devkit" version "0.0.21"
}
```

Documentation
---

![screenshot](https://i.imgur.com/t7HFH50.png)

#### Importing Models
```
File -> Import Model
```

The model importer uses the [JmeConvert](https://github.com/Simsilica/JmeConvert) library created by Paul Speed and
supports the `.glTF` or `.glb` extension. For more information on importing models see the
 [JME Documentation](https://wiki.jmonkeyengine.org/docs/3.3/tutorials/how-to/modeling/blender/blender_gltf.html#import-structure).
 
 #### Scene Editing
 Scene objects are added, removed and selected in the `Scene Tree` window. The properties of the selected item are shown
 in the `Property Inspector` window. Most objects in the `Scene Tree` window have a context menu associated with them,
 which is made available by right-clicking the element in the scene tree.
 
 Modifying any properties in the `Property Inspector` has an immediate effect on the selected item. The properties
 available will depend on the item that is selected in the `Scene Tree` window. For example, selecting a `Geometry`
 in the `Scene Tree` window will allow you to change the material and material properties of that Geometry.
 
 ### Scene Saving
 ```
Right-Click a Spatial in Scene Tree Window -> Save...
```
 You can save any `Spatial` in the `Scene Tree` window by right-clicking the Spatial in the `Scene Tree` window and
 selecting `Save...`. You will be prompted with a dialog allowing you to select the directory and choose a name.
 The selected directory **must** be inside the `Asset Root` directory. After clicking `OK` the Spatial will be saved
 in the specified directory with the specified name, with the extension `.j3o`.
 
 #### Create a SkyBox
 ```
Right-Click a Node in Scene Tree Window -> Add -> SkyBox...
```
Skyboxes can be added to a scene by right-clicking any `Node` in the `Scene Tree` window, clicking `Add...` and
selecting `SkyBox...`. SkyBoxes can be created using a CubeMap, SphereMap or EquirectMap. For some great examples of
 skybox textures, visit [HDRI Haven](https://hdrihaven.com/hdris/).

#### Generating LightProbes
```
Right-Click a Spatial in Scene Tree Window -> Add -> Light -> Generate LightProbe...
```
Lights (including light probes) can be added to any `Spatial` by right-clicking any `Spatial` in the `Scene Tree`
window and clicking `Add -> Light -> Generate LightProbe...`. You will be presented with a dialog window allowing you
to choose which node in your scene the lightprobe will use to create an `Environment Map`, the shape (cube or sphere)
and the radius of the probe.

#### Creating Plugins
The [TestDevKitPlugin](https://github.com/jayfella/TestDevkitPlugin) repository contains a working example and
documentation on how to create plugins.

#### Run AppStates in the DevKit
```
Window -> Run AppState
```
Running an AppState from the DevKit gives the user more functionality, as well as allowing the user to create scene
objects and provide separation of Game Objects. There are several rules in allowing an AppState to be run from the
DevKit:

- The AppState must be inside the `devkit.appstate` package.
- The AppState must have a class Annotation `DevKitAppState`.
- The AppState must have a noArgs constructor.

You may also specify a tabs `String[]` property in the annotation to specify tabs. For example
- `@DevKitAppState(tabs = { "Tab 1", "Tab 2", "Tab 3" } )`

In addition to being able to run the AppState you can also specify annotations on methods to change values. These
annotations will automatically generate GUI components for you when you run the `DevKitAppState` in the DevKit.

- `@ButtonProperty`
    - Creates a button. Must be Placed on a method returning `void` with no arguments. Executes the method when the
    button is pressed.
    For example:
        - `public void doSomething()` 
- `@ColorProperty`
    - Creates a ColorPicker. Placed on a getter method returning `ColorRGBA`. You must also have a setter method.
    For example:
        - `public ColorRGBA getMyColor()`
        - `public void setMyColor(ColorRGBA color)`
- `@EnumProperty`
    - Creates a ComboBox. Placed on a getter method returning an Enum. You must also have a setter method.
    For example:
        - `public MyEnum getMyEnum()`
        - `public void setMyEnum(MyEnum myEnum)`
- `@FloatProperty`
    - Creates a Slider. Placed on a getter method returning `float`. You must also have a setter method.
    For example:
        - `public float getMyFloatValue()`
        - `public void setMyFloatValue(float value)`
    - Optional parameters:
        - min (optional, default Integer.MIN_VALUE)
            - Specifies the minimum value the slider will allow.
        - max (optional, default Integer.MAX_VALUE)
            - Specifies the maximum value the slider will allow
        - step (optional, default 1.0f)
            - Specifies the amount of change the slider moves per movement.
- `@IntegerProperty`
    - Creates a slider. Placed on a getter method returning `int`. You must also have a setter method.
    For example:
        - `public int getMyIntValue()`
        - `public void setMyIntValue(int value)`
    - Optional Parameters:
        - min (optional, default Integer.MIN_VALUE)
            - Specifies the minimum value the slider will allow.
        - max (optional, default Integer.MAX_VALUE)
            - Specifies the maximum value the slider will allow
        - step (optional, default 1.0f)
            - Specifies the amount of change the slider moves per movement.
- `@ListProperty`
    - Creates a List or ComboBox. Placed on a method returning an Array of any object type with no arguments.
    In addition there must also be a getter and setter for the index chosen in the list.
    For example:
        - `public String[] myStrings()`
        - `public int getMySelectedIndex()`
        - `public void setMySelectedIndex(int value)`
    - accessorName (required)
        - Specifies the common method name of the getter and setter. For example `getMySelectedIndex` and
        `setMySelectedIndex` would have an accessorName of `MySelectedIndex`
    - listType (optional, default ListType.List)
        - Determines whether or not to create a List or ComboBox.

All of These annotations have a `tab` property. The GUI control will be placed in the tab of the matching name in the
`DevKitAppState` annotation. If no tab name is specified the GUI component will not be placed in a tab.

For a complete example, see the [TestDevKitAppState](https://github.com/jayfella/jme-swing-devkit/blob/master/src/test/java/TestDevKitAppState.java) class.

### Register Component
For a basic component/class
> PropertyEditorManager.registerEditor(Boolean.class, BooleanEditor.class);

For a complex class with many field

> ServiceManager.getService(RegistrationService.class).registerPropertySectionBuilder(classToMap, MyPropertySectionBuilder)


### POSITIONNING PLUGIN
The fork provides different area in which you can add your plugin in a tab; LEFT_TOP, LEFT_BOTTOM, TOP_LEFT, TOP_RIGHT, RIGHT_TOP, RIGHT_BOTTOM, BOTTOM_LEFT, BOTTOM_RIGHT.

> ServiceManager.getService(CoreService.class).addTab("Tab Title", myComponent, myIcon, Zone.LEFT_TOP);