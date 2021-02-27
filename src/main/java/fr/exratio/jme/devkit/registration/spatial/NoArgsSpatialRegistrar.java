package fr.exratio.jme.devkit.registration.spatial;

import com.jme3.app.SimpleApplication;
import com.jme3.scene.Node;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import javax.swing.tree.TreeNode;

public class NoArgsSpatialRegistrar extends NodeRegistrar {

  public NoArgsSpatialRegistrar(Class<? extends Node> clazz) {
    super(clazz);
  }

  @Override
  public Node createInstance(SimpleApplication application) {

    try {
      Constructor<? extends Node> constructor = getRegisteredClass().getConstructor();
      return constructor.newInstance();
    } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
      e.printStackTrace();
    }

    return null;
  }

  @Override
  public TreeNode createSceneTreeNode(Node node, SimpleApplication application) {
    return null;
  }

}
