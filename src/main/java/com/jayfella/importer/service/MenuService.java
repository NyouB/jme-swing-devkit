package com.jayfella.importer.service;

import javax.swing.*;

public class MenuService implements Service {

    private final JMenuBar primaryMenu;

    public MenuService(JMenuBar primaryMenu) {
        this.primaryMenu = primaryMenu;
    }

    /**
     * Adds the given menu to the primary horizontal JMenuBar.
     * @param menu the menu to add to the menu.
     * @return the given menu.
     */
    public JMenu addPrimaryMenu(JMenu menu) {
        primaryMenu.add(menu);
        return menu;
    }

    /**
     * Removes the given menu from the primary horizontal JMenuBar.
     * @param menu the menu to remove.
     */
    public void removePrimaryMenu(JMenu menu) {
        primaryMenu.remove(menu);
    }

    /**
     * Returns the first menu from the primary horizontal JMenuBar that matches the given text exactly.
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

    @Override
    public void stop() {

    }

}
