package fr.exratio.jme.devkit.properties;

import com.jme3.material.MatParam;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector4f;
import com.jme3.shader.VarType;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;
import fr.exratio.jme.devkit.jme.IgnoredProperties;
import fr.exratio.jme.devkit.properties.builder.ReflectedPropertySectionBuilder;
import fr.exratio.jme.devkit.properties.component.AbstractPropertyEditor;
import fr.exratio.jme.devkit.properties.component.bool.BooleanEditor;
import fr.exratio.jme.devkit.properties.component.colorgba.ColorRGBAEditor;
import fr.exratio.jme.devkit.properties.component.floatc.FloatEditor;
import fr.exratio.jme.devkit.properties.component.texture2d.Texture2DEditor;
import fr.exratio.jme.devkit.properties.component.vector2f.Vector2fEditor;
import fr.exratio.jme.devkit.properties.component.vector3f.Vector3fEditor;
import fr.exratio.jme.devkit.properties.component.vector4f.Vector4fEditor;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class MaterialSection extends UpdatablePropertySection<Material> {

  public static final List<String> IGNORED_PROPERTIES = Arrays
      .asList(IgnoredProperties.material.clone()).stream()
      .map(String::toLowerCase)
      .collect(Collectors.toList());

  private final ReflectedPropertySectionBuilder reflectedPropertySectionBuilder;

  public MaterialSection(Material object,
      ReflectedPropertySectionBuilder reflectedPropertySectionBuilder) {
    super("Material", object);
    this.reflectedPropertySectionBuilder = reflectedPropertySectionBuilder;

  }

  private AbstractPropertyEditor componentFromVarType(VarType varType, String name, Object value) {
    switch (varType) {
      case Float:
        FloatEditor floatComponent = new FloatEditor((Float) value);

        floatComponent.addPropertyChangeListener(event -> {
          Float newValue = (Float) event.getNewValue();
          if (newValue != null) {
            object.setFloat(name, newValue);
          } else {
            object.clearParam(name);
          }
        });

        return floatComponent;
      case Vector2:
        Vector2fEditor vector2fComponent = new Vector2fEditor((Vector2f) value);
        // set the value of the component if one is found.

        vector2fComponent.addPropertyChangeListener(event -> {
          Vector2f newVal = (Vector2f) event.getNewValue();

          if (newVal != null) {
            object.setVector2(name, newVal);
          } else {
            object.clearParam(name);
          }
        });
        return vector2fComponent;
      case Vector3:
        Vector3fEditor vector3fComponent = new Vector3fEditor((Vector3f) value);
        vector3fComponent.addPropertyChangeListener(event -> {
          Vector3f newValue = (Vector3f) event.getNewValue();
          if (newValue != null) {
            object.setVector3(name, newValue);
          } else {
            object.clearParam(name);
          }
        });

        return vector3fComponent;
      case Vector4:
        // a vector4 could also be a ColorRGBA.

        AbstractPropertyEditor vector4fComponent;

        if (value instanceof Vector4f) {
          vector4fComponent = new Vector4fEditor((Vector4f) value);
          vector4fComponent.addPropertyChangeListener(event -> {
            if (event.getNewValue() == null) {
              object.clearParam(name);
            } else {
              object.setVector4(name, (Vector4f) event.getNewValue());
            }
          });

        } else {
          vector4fComponent = new ColorRGBAEditor((ColorRGBA) value);
          vector4fComponent.addPropertyChangeListener(event -> {

            if (event.getNewValue() == null) {
              object.clearParam(name);
            } else {
              object.setColor(name, (ColorRGBA) event.getNewValue());
            }

          });
        }

        return vector4fComponent;
      case Boolean:
        BooleanEditor booleanComponent = new BooleanEditor((Boolean) value);

        booleanComponent.addPropertyChangeListener(event -> {
          if (event.getNewValue() != null) {
            object.setBoolean(name, (Boolean) event.getNewValue());
          } else {
            object.clearParam(name);
          }
        });
        return booleanComponent;

      case Texture2D:
        Texture2DEditor texture2DComponent = new Texture2DEditor((Texture2D) value);

        texture2DComponent.addPropertyChangeListener(event -> {

          if (event.getNewValue() != null) {
            object.setTexture(name, (Texture) event.getNewValue());
          } else {
            object.clearParam(name);
          }
        });
        return texture2DComponent;
      default:
        return null;
    }
  }

  @Override
  public void buildView() {

    // a list of all possible params
    Collection<MatParam> params = object.getMaterialDef().getMaterialParams();
    List<MatParam> allParams = new ArrayList<>(params);

    // sort by type then name
    allParams.sort((o1, o2) -> {
      int value1 = o1.getVarType().compareTo(o2.getVarType());
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

    for (MatParam matParam : allParams) {

      if (IGNORED_PROPERTIES.contains(matParam.getName().toLowerCase())) {
        continue;
      }

      AbstractPropertyEditor propertyEditor = componentFromVarType(matParam.getVarType(),
          matParam.getName(), object.getParamValue(matParam.getName()));
      if (propertyEditor != null) {
        addProperty(matParam.getName(), propertyEditor.getCustomEditor());
      }

    }

    List<PropertySection> renderStateSections = reflectedPropertySectionBuilder
        .withObject(object.getAdditionalRenderState()).build();

    for (PropertySection propertySection : renderStateSections) {
      for (Entry<String, Component> entry : propertySection.getComponents().entrySet()) {
        addProperty(entry.getKey(), entry.getValue());
      }
    }
  }
}
