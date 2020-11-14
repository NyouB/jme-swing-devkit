package com.jayfella.devkit.properties.builder;

import com.jayfella.devkit.jme.IgnoredProperties;
import com.jayfella.devkit.properties.PropertySection;
import com.jayfella.devkit.properties.component.AbstractSDKComponent;
import com.jayfella.devkit.properties.component.bool.BooleanComponent;
import com.jayfella.devkit.properties.component.colorgba.ColorRGBAComponent;
import com.jayfella.devkit.properties.component.floatc.FloatComponent;
import com.jayfella.devkit.properties.component.texture2d.Texture2DComponent;
import com.jayfella.devkit.properties.component.vector2f.Vector2fComponent;
import com.jayfella.devkit.properties.component.vector3f.Vector3fComponent;
import com.jayfella.devkit.properties.component.vector4f.Vector4fComponent;
import com.jme3.material.MatParam;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector4f;
import com.jme3.shader.VarType;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MaterialPropertySectionBuilder extends AbstractPropertySectionBuilder<Material> {

  //Build a lower case list of ignored properties
  public static final List<String> IGNORED_PROPERTIES = Arrays
      .asList(IgnoredProperties.material.clone()).stream()
      .map(String::toLowerCase)
      .collect(Collectors.toList());
  private static final Logger LOGGER = LoggerFactory
      .getLogger(MaterialPropertySectionBuilder.class);

  public MaterialPropertySectionBuilder(Material object, String... ignoredProperties) {
    super(object);
  }

  @Override
  public List<PropertySection> build() {

    List<PropertySection> propertySections = new ArrayList<>();
    propertySections.add(createMaterialPropertySection());
    propertySections.addAll(createAdditionalRenderStateSection());
    return propertySections;
  }

  private PropertySection createMaterialPropertySection() {

    List<AbstractSDKComponent<?>> components = new ArrayList<>();

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

      AbstractSDKComponent sdkComponent = componentFromVarType(matParam.getVarType(),
          matParam.getName(), object.getParamValue(matParam.getName()));
      if (sdkComponent == null) {
        continue;
      }
      components.add(sdkComponent);
    }

    return new PropertySection("Material", components.toArray(new AbstractSDKComponent[0]));
  }


  private List<PropertySection> createAdditionalRenderStateSection() {

    ReflectedPropertySectionBuilder renderStateBuilder = new ReflectedPropertySectionBuilder(
        "AdditionalRenderState",
        object.getAdditionalRenderState());

    return renderStateBuilder.build();

  }

  private AbstractSDKComponent componentFromVarType(VarType varType, String name, Object value) {
    switch (varType) {
      case Float:
        FloatComponent floatComponent = new FloatComponent((Float) value, name);

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
        Vector2fComponent vector2fComponent = new Vector2fComponent((Vector2f) value, name);
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
        Vector3fComponent vector3fComponent = new Vector3fComponent((Vector3f) value, name);
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

        AbstractSDKComponent vector4fComponent;

        if (value instanceof Vector4f) {
          vector4fComponent = new Vector4fComponent((Vector4f) value, name);
          vector4fComponent.addPropertyChangeListener(event -> {
            if (event.getNewValue() == null) {
              object.clearParam(name);
            } else {
              object.setVector4(name, (Vector4f) event.getNewValue());
            }
          });

        } else {
          vector4fComponent = new ColorRGBAComponent((ColorRGBA) value, name);
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
        BooleanComponent booleanComponent = new BooleanComponent((Boolean) value, name);

        booleanComponent.addPropertyChangeListener(event -> {
          if (event.getNewValue() != null) {
            object.setBoolean(name, (Boolean) event.getNewValue());
          } else {
            object.clearParam(name);
          }
        });
        return booleanComponent;

      case Texture2D:
        Texture2DComponent texture2DComponent = new Texture2DComponent((Texture2D) value, name);

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


}
