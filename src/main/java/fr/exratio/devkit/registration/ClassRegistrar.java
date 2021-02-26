package fr.exratio.devkit.registration;

import com.jme3.app.SimpleApplication;
import javax.swing.tree.TreeNode;

public interface ClassRegistrar<T> {

  /**
   * Gets the registered class.
   *
   * @return the class that is registered.
   */
  Class<? extends T> getRegisteredClass();

  /**
   * Sets the registered class.
   *
   * @param classToRegister the class to register.
   */
  void setRegisteredClass(Class<? extends T> classToRegister);

  /**
   * Creates an instance of the registered item.
   *
   * @param application an instance of the running JME application.
   * @return an instance of the registered item.
   */
  T createInstance(SimpleApplication application);

  /**
   * Creates a TreeNode for the SceneTree whenever the registered class is added to the scene. Also
   * allows you to create a context menu to create additional actions for the registered item. For
   * example a BatchNode has a "batch" menu item in its context menu.
   *
   * If this method returns null the default TreeNode will be used instead. For example if the
   * registered item is a Geometry, a GeometryTreeNode will be created.
   *
   * @param instance the item being selected.
   * @param application an instance of the running JME application.
   * @return a TreeNode associated with the registered item.
   */
  TreeNode createSceneTreeNode(T instance, SimpleApplication application);

}
