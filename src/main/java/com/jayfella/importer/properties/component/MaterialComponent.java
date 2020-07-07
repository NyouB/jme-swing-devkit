package com.jayfella.importer.properties.component;

import com.intellij.uiDesigner.core.GridLayoutManager;
import com.jayfella.importer.jme.IgnoredProperties;
import com.jayfella.importer.service.JmeEngineService;
import com.jayfella.importer.service.ServiceManager;
import com.jme3.asset.AssetNotFoundException;
import com.jme3.material.MatParam;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector4f;
import com.jme3.scene.Geometry;
import com.jme3.shader.VarType;
import com.jme3.texture.Texture2D;
import com.jme3.util.ListMap;
import org.jdesktop.swingx.VerticalLayout;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Method;
import java.util.List;
import java.util.*;

public class MaterialComponent extends SdkComponent<Material> {

    // private static final Logger log = Logger.getInstance(MaterialComponent.class);

    private JPanel contentPanel;

    public MaterialComponent(Object parent, Method getter, Method setter) {
        super(parent, getter, setter);

        contentPanel.setLayout(new VerticalLayout());

        // Material material = (Material) parent;
        Geometry geometry = (Geometry) parent;
        Material material = geometry.getMaterial();

        // a list of all possible params
        Collection<MatParam> params = material.getMaterialDef().getMaterialParams();
        List<MatParam> allParams = new ArrayList<>(params);

        allParams.sort(Comparator.comparing(MatParam::getName));

        // a list of params that have been set (either default or by the user).
        ListMap<String, MatParam> setParams = material.getParamsMap();

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
                    material.setFloat(matParam.getName(), val);
                });

                contentPanel.add(floatComponent.getJComponent());

            } else if (varyType == VarType.Vector2) {

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
                    material.setVector3(matParam.getName(), val);
                });

                contentPanel.add(vector3fComponent.getJComponent());

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
                        material.setVector4(matParam.getName(), val);
                    } else {
                        ColorRGBA val = (ColorRGBA) value;
                        material.setColor(matParam.getName(), val);
                    }

                });

                contentPanel.add(vector4fComponent.getJComponent());


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
                    material.setBoolean(matParam.getName(), val);
                });

                contentPanel.add(booleanComponent.getJComponent());

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
                                    material.setTexture(matParam.getName(), texture2D);
                                } catch (AssetNotFoundException ex) {
                                    // do nothing.
                                    System.out.println("Texture2D Not Found: " + val);
                                    // log.info("Texture2D Not Found: " + val);
                                }

                            });

                            contentPanel.add(texture2DComponent.getJComponent());
                        });

            }

        }

    }

    @Override
    public JComponent getJComponent() {
        return contentPanel;
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     */
    private void $$$setupUI$$$() {
        contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
    }

    /**
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPanel;
    }

}
