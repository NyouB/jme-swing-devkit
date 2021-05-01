package fr.exratio.jme.devkit.registration.control;

import com.jme3.app.SimpleApplication;
import com.jme3.scene.control.Control;
import fr.exratio.jme.devkit.tree.control.ControlTreeNode;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import javax.swing.tree.TreeNode;

public class NoArgsControlRegistrar extends ControlRegistrar {

  public NoArgsControlRegistrar() {
  }

  @Override
  public Control createInstance(SimpleApplication application) {

    try {
      Constructor<? extends Control> constructor = getRegisteredClass().getConstructor();
      return constructor.newInstance();
    } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
      e.printStackTrace();
    }

    return null;
  }

  @Override
  public TreeNode createSceneTreeNode(Control control, SimpleApplication application) {
    return new ControlTreeNode(control);
  }

}
