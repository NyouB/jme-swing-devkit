package com.jayfella.importer.properties.component;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.jayfella.importer.config.DevKitConfig;
import com.jayfella.importer.jme.TextureImage;
import com.jayfella.importer.service.JmeEngineService;
import com.jayfella.importer.service.ServiceManager;
import com.jme3.texture.Texture2D;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class Texture2DComponent extends ReflectedSdkComponent<Texture2D> {

    private JPanel contentPanel;
    private JLabel propertyNameLabel;
    private JButton clearTextureButton;
    private Texture2DPanel imagePanel;
    private JList<String> texturesList;


    public Texture2DComponent() {
        super(null, null, null);

        initCustomLayout();
    }

    public Texture2DComponent(Object parent, Method getter, Method setter) {
        super(parent, getter, setter);

        initCustomLayout();

        try {
            setValue(getter.invoke(parent));
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    private void initCustomLayout() {


        // get a list of all textures in the asset root.
        List<Path> textureFiles = null;

        try {
            textureFiles = Files.walk(new File(DevKitConfig.getInstance().getProjectConfig().getAssetRootDir()).toPath())
                    .filter(p -> {
                        for (String ext : TextureImage.imageExtensions) {
                            if (p.toString().endsWith(ext)) {
                                return true;
                            }
                        }
                        return false;
                    })
                    .collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (textureFiles != null) {
            // DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
            DefaultListModel<String> model = new DefaultListModel<>();

            for (Path path : textureFiles) {

                String relativePath = path.toString().replace(DevKitConfig.getInstance().getProjectConfig().getAssetRootDir(), "");

                // remove any trailing slashes.
                if (relativePath.startsWith("/")) {
                    relativePath = relativePath.substring(1);
                }

                model.addElement(relativePath);
            }

            texturesList.setModel(model);

        }

        // contentPanel.setLayout(new VerticalLayout());

        // this.imagePanel = new Texture2DPanel();
        // this.contentPanel.add(imagePanel);

        clearTextureButton.addActionListener(e -> {

            texturesList.clearSelection();

            imagePanel.setTexture(null);
            imagePanel.revalidate();
            imagePanel.repaint();

            setValue(null);
        });

    }

    @Override
    public JComponent getJComponent() {
        return contentPanel;
    }

    @Override
    public void setValue(Object value) {
        super.setValue(value);

        if (!isBinded()) {

            Texture2D texture2D = (Texture2D) value;

            SwingUtilities.invokeLater(() -> {

                if (texture2D != null) {
                    // if the texture is embedded it won't have a key.
                    if (texture2D.getKey() != null) {
                        texturesList.setSelectedValue(texture2D.getKey().getName(), true);

                    } else {
                        texturesList.setSelectedValue(texture2D.getName(), true);
                    }

                    this.imagePanel.setTexture(texture2D);
                    this.imagePanel.revalidate();
                } else {
                    texturesList.setSelectedIndex(-1);
                }

                bind();
            });
        }

    }


    @Override
    public void bind() {
        super.bind();

        texturesList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                String newValue = texturesList.getSelectedValue();

                Texture2D texture2D = null;

                if (newValue != null) {
                    texture2D = (Texture2D) ServiceManager.getService(JmeEngineService.class)
                            .getAssetManager().loadTexture(newValue);
                }

                imagePanel.setTexture(texture2D);
                imagePanel.revalidate();
                imagePanel.repaint();

                setValue(newValue);
            }
        });

    }

    @Override
    public void setPropertyName(String propertyName) {
        super.setPropertyName(propertyName);
        propertyNameLabel.setText("Texture2D: " + propertyName);
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
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayoutManager(5, 1, new Insets(0, 0, 0, 0), -1, -1));
        propertyNameLabel = new JLabel();
        propertyNameLabel.setText("Label");
        contentPanel.add(propertyNameLabel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        imagePanel = new Texture2DPanel();
        imagePanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPanel.add(imagePanel, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        imagePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JScrollPane scrollPane1 = new JScrollPane();
        contentPanel.add(scrollPane1, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        scrollPane1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        texturesList = new JList();
        scrollPane1.setViewportView(texturesList);
        clearTextureButton = new JButton();
        clearTextureButton.setText("No Texture");
        contentPanel.add(clearTextureButton, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JSeparator separator1 = new JSeparator();
        contentPanel.add(separator1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPanel;
    }

}
