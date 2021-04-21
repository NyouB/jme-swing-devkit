package fr.exratio.jme.devkit.tree.light;

import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.Light;
import com.jme3.light.LightProbe;
import com.jme3.light.PointLight;
import javax.swing.tree.DefaultMutableTreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class LightTreeNode extends DefaultMutableTreeNode {

  private static final Logger LOGGER = LoggerFactory.getLogger(LightTreeNode.class);

  public LightTreeNode(Light light) {
    super(light);
  }

  public static LightTreeNode fromLight(Light light) {
    if (light instanceof AmbientLight) {
      return new AmbientLightTreeNode((AmbientLight) light);
    } else if (light instanceof DirectionalLight) {
      return new DirectionalLightTreeNode((DirectionalLight) light);
    } else if (light instanceof LightProbe) {
      return new LightProbeTreeNode((LightProbe) light);
    } else if (light instanceof PointLight) {
      return new PointLightTreeNode((PointLight) light);
    }
    LOGGER.warn("Unable to create LightTreeNode from object: " + light.getClass());
    return null;
  }

  @Override
  public Light getUserObject() {
    return (Light) super.getUserObject();
  }

}
