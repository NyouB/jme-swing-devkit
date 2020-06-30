package com.jayfella.importer.properties.builder;

import com.jayfella.importer.properties.PropertySection;
import com.jayfella.importer.properties.component.*;
import com.jme3.material.Material;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class SpatialComponentSetBuilder<T> extends AbstractComponentSetBuilder<Spatial> {

    private Spatial object;
    private String[] ignoredProperties = new String[0];

    @Override
    public void setObject(Spatial object, String... ignoredProperties) {
        this.object = object;
        this.ignoredProperties = ignoredProperties;
    }

    @Override
    public List<PropertySection> build() {

        List<PropertySection> propertySections = new ArrayList<>();

        Method getter, setter;

        try {

            // Transform : location, rotation, scale

            getter = object.getClass().getMethod("getLocalTranslation");
            setter = object.getClass().getMethod("setLocalTranslation", Vector3f.class);

            Vector3fComponent localTranslation = new Vector3fComponent(object, getter, setter);
            localTranslation.setPropertyName("localTranslation");

            getter = object.getClass().getMethod("getLocalRotation");
            setter = object.getClass().getMethod("setLocalRotation", Quaternion.class);

            QuaternionComponent localRotation = new QuaternionComponent(object, getter, setter);
            localRotation.setPropertyName("localRotation");

            getter = object.getClass().getMethod("getLocalScale");
            setter = object.getClass().getMethod("setLocalScale", Vector3f.class);

            Vector3fComponent localScale = new Vector3fComponent(object, getter, setter);
            localScale.setPropertyName("localScale");

            PropertySection transformSection = new PropertySection("Transform", localTranslation, localRotation, localScale);
            propertySections.add(transformSection);


            // Spatial: name, cullHint, lastFrustumIntersection, shadowMode, QueueBucket, BatchHint

            getter = object.getClass().getMethod("getName");
            setter = object.getClass().getMethod("setName", String.class);

            StringComponent name = new StringComponent(object, getter, setter);
            name.setPropertyName("name");

            getter = object.getClass().getMethod("getCullHint");
            setter = object.getClass().getMethod("setCullHint", com.jme3.scene.Spatial.CullHint.class);

            EnumComponent cullHint = new EnumComponent(object, getter, setter);
            cullHint.setEnumValues(com.jme3.scene.Spatial.CullHint.class);
            cullHint.setPropertyName("cullHint");

            getter = object.getClass().getMethod("getShadowMode");
            setter = object.getClass().getMethod("setShadowMode", RenderQueue.ShadowMode.class);

            EnumComponent shadowMode = new EnumComponent(object, getter, setter);
            shadowMode.setEnumValues(RenderQueue.ShadowMode.class);
            shadowMode.setPropertyName("shadowMode");

            getter = object.getClass().getMethod("getQueueBucket");
            setter = object.getClass().getMethod("setQueueBucket", RenderQueue.Bucket.class);

            EnumComponent queueBucket = new EnumComponent(object, getter, setter);
            queueBucket.setEnumValues(RenderQueue.Bucket.class);
            queueBucket.setPropertyName("queueBucket");

            getter = object.getClass().getMethod("getBatchHint");
            setter = object.getClass().getMethod("setBatchHint", com.jme3.scene.Spatial.BatchHint.class);

            EnumComponent batchHint = new EnumComponent(object, getter, setter);
            batchHint.setEnumValues(com.jme3.scene.Spatial.BatchHint.class);
            batchHint.setPropertyName("batchHint");

            PropertySection spatialSection = new PropertySection("Spatial", name, cullHint, shadowMode, queueBucket, batchHint);
            propertySections.add(spatialSection);


        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        if (object instanceof Node) {
            // Node doesn't have any properties we want.
            // Leave the comment here so we're aware that we know.
        }
        else if (object instanceof Geometry) {

            try {

                // Geometry-specific data
                getter = object.getClass().getMethod("isIgnoreTransform");
                setter = object.getClass().getMethod("setIgnoreTransform", boolean.class);

                BooleanComponent ignoreTranform = new BooleanComponent(object, getter, setter);
                ignoreTranform.setPropertyName("ignoreTransform");

                getter = object.getClass().getMethod("getLodLevel");
                setter = object.getClass().getMethod("setLodLevel", int.class);

                IntegerComponent lodLevel = new IntegerComponent(object, getter, setter);
                lodLevel.setPropertyName("lodLevel");

                PropertySection geometrySection = new PropertySection("Geometry", ignoreTranform, lodLevel);
                propertySections.add(geometrySection);


                // Material
                getter = object.getClass().getMethod("getMaterial");
                setter = object.getClass().getMethod("setMaterial", Material.class);

                MaterialComponent materialComponent = new MaterialComponent(object, getter, setter);

                PropertySection materialSection = new PropertySection("Material", materialComponent);
                propertySections.add(materialSection);

                // Additional Render State
                // move it from here to the material titledpane.
                // that way when the material gets updated, the renderstate does too.
                /*
                Geometry geometry = (Geometry) object;
                Material material = geometry.getMaterial();

                ReflectedComponentBuilder<RenderState> renderStateBuilder = new ReflectedComponentBuilder<>();
                renderStateBuilder.setObject(material.getAdditionalRenderState());

                List<Component> renderStateComponents = renderStateBuilder.build();

                TitledPane renderStatePane = createTitledPane("AdditionalRenderState", renderStateComponents);
                titledPanes.add(renderStatePane);

                 */

            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }

        }


        return propertySections;
    }


}
