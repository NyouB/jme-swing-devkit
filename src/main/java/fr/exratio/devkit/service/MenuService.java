package fr.exratio.devkit.service;

import fr.exratio.devkit.tree.JmeTreeNode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MenuService implements Service {

  private final JMenuBar primaryMenu;
  private final Map<Class<? extends JmeTreeNode>, List<JMenuItem>> customMenuItems = new HashMap<>();

  private final long threadId;

  public MenuService(JMenuBar primaryMenu) {
    this.threadId = Thread.currentThread().getId();
    this.primaryMenu = primaryMenu;
  }

  /**
   * Adds the given menu to the primary horizontal JMenuBar.
   *
   * @param menu the menu to add to the menu.
   * @return the given menu.
   */
  public JMenu addPrimaryMenu(JMenu menu) {
    return primaryMenu.add(menu);
  }

  /**
   * Removes the given menu from the primary horizontal JMenuBar.
   *
   * @param menu the menu to remove.
   */
  public void removePrimaryMenu(JMenu menu) {
    primaryMenu.remove(menu);
  }

  /**
   * Returns the first menu from the primary horizontal JMenuBar that matches the given text
   * exactly.
   *
   * @param text the text of the menuItem.
   * @return the menu with exact matching text.
   */
  public JMenu getPrimaryMenu(String text) {

    for (int i = 0; i < primaryMenu.getMenuCount(); i++) {
      JMenu menu = primaryMenu.getMenu(i);
      if (menu.getText().equals(text)) {
        return menu;
      }
    }

    return null;
  }

  /**
   * Adds a menu item to a context (right-click) menu in the Scene Tree window.
   *
   * @param treeNodeClass the TreeNode to add the menu item.
   * @param menuItem the menuItem to add.
   * @return whether or not the addition was accepted.
   */
  public boolean addItemToContextMenu(Class<? extends JmeTreeNode> treeNodeClass,
      JMenuItem menuItem) {
    List<JMenuItem> itemsList = customMenuItems
        .computeIfAbsent(treeNodeClass, k -> new ArrayList<>());
    return itemsList.add(menuItem);
  }

  /**
   * Removes a menu item from a context (right-click) menu in the Scene Tree Window.
   *
   * @param treeNodeClass the TreeNode to remove the menu item.
   * @param menuItem the menuItem to remove.
   * @return whether or not the removal was successful.
   */
  public boolean removeItemFromContextMenu(Class<? extends JmeTreeNode> treeNodeClass,
      JMenuItem menuItem) {
    List<JMenuItem> itemsList = customMenuItems.get(treeNodeClass);

    if (itemsList != null) {
      return itemsList.remove(menuItem);
    }

    return false;
  }

  /**
   * Returns all custom menu items for the given TreeNode.
   *
   * @param treeNodeClass the TreeNode in question.
   * @return a list of custom MenuItems for the given TreeNode.
   */
  public List<JMenuItem> getCustomMenuItems(Class<? extends JmeTreeNode> treeNodeClass) {
    return customMenuItems.get(treeNodeClass);
  }

  @Override
  public long getThreadId() {
    return threadId;
  }

  @Override
  public void stop() {

  }

}
