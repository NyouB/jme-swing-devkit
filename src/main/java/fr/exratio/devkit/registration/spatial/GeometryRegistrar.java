package fr.exratio.devkit.registration.spatial;

import com.jme3.app.SimpleApplication;
import com.jme3.scene.Geometry;
import fr.exratio.devkit.registration.AbstractClassRegistrar;
import javax.swing.tree.TreeNode;

public class GeometryRegistrar extends AbstractClassRegistrar<Geometry> {


  @Override
  public Geometry createInstance(SimpleApplication application) {
    return null;
  }

  @Override
  public TreeNode createSceneTreeNode(Geometry instance, SimpleApplication application) {
    return null;
  }

}
