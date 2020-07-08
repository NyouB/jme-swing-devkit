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
import java.util.logging.Level;
import java.util.logging.Logger;

public class MaterialComponentSetBuilder extends AbstractComponentSetBuilder<Material> {

    private static final Logger log = Logger.getLogger(MaterialComponentSetBuilder.class.getName());

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

        List<ReflectedSdkComponent<?>> components = new ArrayList<>();

        // a list of all possible params
        Collection<MatParam> params = object.getMaterialDef().getMaterialParams();
        List<MatParam> allParams = new ArrayList<>(params);

        // sort by type then name
        allParams.sort((o1, o2) -> {
            int value1 =  o1.getVarType().compareTo(o2.getVarType());
            if (value1 == 0) {
                int value2 = o1.getName().compareTo(o2.getName());
                if (value2 == 0) {
                    return value1;
                } else {
                    return value2;
                }
            }

            return value1;
        });

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

                ReflectedSdkComponent<?> vector4fComponent;

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

                    vector4fComponent.setValue(null);
                }

                vector4fComponent.setPropertyChangedEvent(value -> {

                    if (value == null) {
                        object.clearParam(matParam.getName());
                    }
                    else {

                        if (matParam.getValue() instanceof Vector4f) {
                            object.setVector4(matParam.getName(), (Vector4f) value);
                        } else {
                            object.setColor(matParam.getName(), (ColorRGBA) value);
                        }

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

                Texture2DComponent texture2DComponent = new Texture2DComponent();
                texture2DComponent.setPropertyName(matParam.getName());

                Map.Entry<String, MatParam> setParam = setParams.entrySet().stream()
                        .filter(entry -> entry.getKey().equalsIgnoreCase(matParam.getName()))
                        .findFirst()
                        .orElse(null);

                if (setParam != null) {
                    Texture2D fVal = (Texture2D) setParam.getValue().getValue();
                    texture2DComponent.setValue(fVal);
                }
                else {
                    texture2DComponent.setValue(null);
                }

                texture2DComponent.setPropertyChangedEvent(value -> {

                    String val = (String) value;

                    try {

                        if (val != null) {
                            Texture2D texture2D = (Texture2D) ServiceManager.getService(JmeEngineService.class).getAssetManager().loadTexture(val);
                            object.setTexture(matParam.getName(), texture2D);
                        }
                        else {
                            object.clearParam(matParam.getName());
                        }

                    } catch (AssetNotFoundException ex) {
                        log.warning("Texture2D Not Found: " + val);
                        log.log(Level.FINER, "Texture2D Not Found: " + val, ex);
                    }

                });

                components.add(texture2DComponent);

            }

        }

        return new PropertySection("Material", components.toArray(new ReflectedSdkComponent[0]));
    }

    private List<PropertySection> createAdditionalRenderStateSection() {

        ReflectedComponentSetBuilder renderStateBuilder = new ReflectedComponentSetBuilder(
                "AdditionalRenderState",
                object.getAdditionalRenderState());

        return renderStateBuilder.build();

    }

}
