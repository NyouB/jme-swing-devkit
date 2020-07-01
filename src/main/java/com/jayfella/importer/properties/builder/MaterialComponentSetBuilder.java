package com.jayfella.importer.properties.builder;

import com.jayfella.importer.jme.IgnoredProperties;
import com.jayfella.importer.properties.PropertySection;
import com.jayfella.importer.properties.component.*;
import com.jayfella.importer.service.JmeEngineService;
import com.jayfella.importer.service.ServiceManager;
import com.jme3.asset.AssetNotFoundException;
import com.jme3.material.MatParam;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector4f;
import com.jme3.shader.VarType;
import com.jme3.texture.Texture2D;
import com.jme3.util.ListMap;

import java.util.*;

public class MaterialComponentSetBuilder extends AbstractComponentSetBuilder<Material> {

    public MaterialComponentSetBuilder(Material object, String... ignoredProperties) {
        super(object, ignoredProperties);
    }

    @Override
    public List<PropertySection> build() {

        List<PropertySection> propertySections = new ArrayList<>();
        propertySections.add(createMaterialPropertySection());
        propertySections.addAll(createAdditionalRenderStateSection());

        return  propertySections;
    }

    private PropertySection createMaterialPropertySection() {

        List<SdkComponent> components = new ArrayList<>();

        // a list of all possible params
        Collection<MatParam> params = object.getMaterialDef().getMaterialParams();
        List<MatParam> allParams = new ArrayList<>(params);

        allParams.sort(Comparator.comparing(MatParam::getName));

        // a list of params that have been set (either default or by the user).
        ListMap<String, MatParam> setParams = object.getParamsMap();

        for (MatParam matParam : allParams) {

            boolean ignoreProperty = false;

            for (String ignoredProperty : IgnoredProperties.material) {
                if (matParam.getName().equalsIgnoreCase(ignoredProperty)) {
                    ignoreProperty = true;
                    break;
                }
            }

            if (ignoreProperty) {
                continue;
            }

            VarType varyType = matParam.getVarType();

            if (varyType == VarType.Float) {

                FloatComponent floatComponent = new FloatComponent();
                floatComponent.setPropertyName(matParam.getName());

                // set the value of the component if one is found.
                Map.Entry<String, MatParam> setParam = setParams.entrySet().stream()
                        .filter(entry -> entry.getKey().equalsIgnoreCase(matParam.getName()))
                        .findFirst()
                        .orElse(null);

                if (setParam != null) {
                    float fVal = (float) setParam.getValue().getValue();
                    floatComponent.setValue(fVal);
                } else {
                    floatComponent.setValue(0.0f);
                }

                floatComponent.setPropertyChangedEvent(value -> {
                    float val = (float) value;
                    object.setFloat(matParam.getName(), val);
                });

                components.add(floatComponent);

            } else if (varyType == VarType.Vector2) {

                Vector2fComponent vector2fComponent = new Vector2fComponent();
                vector2fComponent.setPropertyName(matParam.getName());

                // set the value of the component if one is found.
                Map.Entry<String, MatParam> setParam = setParams.entrySet().stream()
                        .filter(entry -> entry.getKey().equalsIgnoreCase(matParam.getName()))
                        .findFirst()
                        .orElse(null);

                if (setParam != null) {
                    Vector2f fVal = (Vector2f) setParam.getValue().getValue();
                    vector2fComponent.setValue(fVal);
                } else {
                    vector2fComponent.setValue(new Vector2f(0, 0));
                }

                vector2fComponent.setPropertyChangedEvent(value -> {
                    Vector2f val = (Vector2f) value;
                    object.setVector2(matParam.getName(), val);
                });

                components.add(vector2fComponent);

            } else if (varyType == VarType.Vector3) {

                Vector3fComponent vector3fComponent = new Vector3fComponent();
                vector3fComponent.setPropertyName(matParam.getName());

                // set the value of the component if one is found.
                Map.Entry<String, MatParam> setParam = setParams.entrySet().stream()
                        .filter(entry -> entry.getKey().equalsIgnoreCase(matParam.getName()))
                        .findFirst()
                        .orElse(null);

                if (setParam != null) {
                    Vector3f fVal = (Vector3f) setParam.getValue().getValue();
                    vector3fComponent.setValue(fVal);
                } else {
                    vector3fComponent.setValue(new Vector3f(0, 0, 0));
                }

                vector3fComponent.setPropertyChangedEvent(value -> {
                    Vector3f val = (Vector3f) value;
                    object.setVector3(matParam.getName(), val);
                });

                components.add(vector3fComponent);

            } else if (varyType == VarType.Vector4) {

                // a vector4 could also be a ColorRGBA.

                SdkComponent vector4fComponent;

                if (matParam.getValue() instanceof Vector4f) {
                    vector4fComponent = new Vector4fComponent();
                } else {
                    vector4fComponent = new ColorRGBAComponent();
                }

                vector4fComponent.setPropertyName(matParam.getName());

                // set the value of the component if one is found.
                Map.Entry<String, MatParam> setParam = setParams.entrySet().stream()
                        .filter(entry -> entry.getKey().equalsIgnoreCase(matParam.getName()))
                        .findFirst()
                        .orElse(null);

                if (setParam != null) {

                    if (matParam.getValue() instanceof Vector4f) {
                        Vector4f fVal = (Vector4f) setParam.getValue().getValue();
                        vector4fComponent.setValue(fVal);
                    } else {
                        ColorRGBA fVal = (ColorRGBA) setParam.getValue().getValue();
                        vector4fComponent.setValue(fVal);
                    }
                } else {
                    if (matParam.getValue() instanceof Vector4f) {
                        vector4fComponent.setValue(new Vector4f());
                    } else if (matParam.getValue() instanceof ColorRGBA) {
                        vector4fComponent.setValue(new ColorRGBA());
                    }
                }

                vector4fComponent.setPropertyChangedEvent(value -> {

                    if (matParam.getValue() instanceof Vector4f) {
                        Vector4f val = (Vector4f) value;
                        object.setVector4(matParam.getName(), val);
                    } else {
                        ColorRGBA val = (ColorRGBA) value;
                        object.setColor(matParam.getName(), val);
                    }

                });

                components.add(vector4fComponent);


            } else if (varyType == VarType.Boolean) {

                BooleanComponent booleanComponent = new BooleanComponent();
                booleanComponent.setPropertyName(matParam.getName());

                // set the value of the component if one is found.
                Map.Entry<String, MatParam> setParam = setParams.entrySet().stream()
                        .filter(entry -> entry.getKey().equalsIgnoreCase(matParam.getName()))
                        .findFirst()
                        .orElse(null);

                if (setParam != null) {
                    boolean fVal = (boolean) setParam.getValue().getValue();
                    booleanComponent.setValue(fVal);
                } else {
                    booleanComponent.setValue(false);
                }

                booleanComponent.setPropertyChangedEvent(value -> {
                    boolean val = (boolean) value;
                    object.setBoolean(matParam.getName(), val);
                });

                components.add(booleanComponent);

            } else if (varyType == VarType.Texture2D) {

                setParams.entrySet().stream()
                        .filter(entry -> entry.getKey().equalsIgnoreCase(matParam.getName()))
                        .findFirst()
                        .ifPresent(setParam -> {

                            Texture2DComponent texture2DComponent = new Texture2DComponent();
                            texture2DComponent.setPropertyName(matParam.getName());

                            Texture2D fVal = (Texture2D) setParam.getValue().getValue();
                            texture2DComponent.setValue(fVal);

                            texture2DComponent.setPropertyChangedEvent(value -> {
                                // Texture2D val = (Texture2D)value;

                                String val = (String) value;

                                // Texture2D texture2D = ServiceManager.getService(JmeEngineService.class).getExternalAssetLoader().load(val, Texture2D.class);
                                try {
                                    Texture2D texture2D = (Texture2D) ServiceManager.getService(JmeEngineService.class).getAssetManager().loadTexture(val);
                                    object.setTexture(matParam.getName(), texture2D);
                                } catch (AssetNotFoundException ex) {
                                    // do nothing.
                                    System.out.println("Texture2D Not Found: " + val);
                                    // log.info("Texture2D Not Found: " + val);
                                }

                            });

                            components.add(texture2DComponent);
                        });

            }

        }

        return new PropertySection("Material", components.toArray(new SdkComponent[0]));
    }

    private List<PropertySection> createAdditionalRenderStateSection() {

        ReflectedComponentSetBuilder renderStateBuilder = new ReflectedComponentSetBuilder(
                "AdditionalRenderState",
                object.getAdditionalRenderState());

        return renderStateBuilder.build();

    }

}
