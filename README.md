jMonkeyEngine Devkit
===

A Software Development Kit created using a Java Swing UI with support for plugins.

### Use the SDK
To use the SDK add the gradle plugin to your project and run the gradle task `runSdk`

```groovy
plugins {
    id "com.jayfella.jme-devkit" version "0.0.15"
}
```

![screenshot](https://i.imgur.com/t7HFH50.png)

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
- Much more!

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
