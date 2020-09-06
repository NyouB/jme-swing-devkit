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