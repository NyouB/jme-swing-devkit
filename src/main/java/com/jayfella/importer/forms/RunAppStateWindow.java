package com.jayfella.importer.forms;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.jayfella.importer.appstate.annotations.*;
import com.jayfella.importer.core.ColorConverter;
import com.jayfella.importer.core.DevkitPackages;
import com.jayfella.importer.service.JmeEngineService;
import com.jayfella.importer.service.ServiceManager;
import com.jme3.app.state.AppState;
import com.jme3.math.ColorRGBA;
import net.miginfocom.swing.MigLayout;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ConfigurationBuilder;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RunAppStateWindow {

    public static final String RUN_APPSTATE_WINDOW_TITLE = "Run AppState";

    private JPanel rootPane;
    private JList<Class<? extends AppState>> appstatesList;
    private JPanel appstatePropertiesPanel;
    private JButton runButton;
    private JButton stopButton;

    public RunAppStateWindow() {

        $$$setupUI$$$();

        Set<Class<? extends AppState>> classes = getAllAnnotatedAppStates();
        DefaultListModel<Class<? extends AppState>> dlm = new DefaultListModel<>();

        for (Class<? extends AppState> c : classes) {
            dlm.addElement(c);
        }

        appstatesList.setModel(dlm);

        runButton.addActionListener(e -> {

            Class<? extends AppState> selectedClass = appstatesList.getSelectedValue();

            if (selectedClass != null) {

                boolean exists = ServiceManager.getService(JmeEngineService.class)
                        .getStateManager()
                        .getState(selectedClass) != null;

                if (!exists) {

                    try {

                        Constructor<? extends AppState> constructor = selectedClass.getConstructor();
                        AppState appState = constructor.newInstance();

                        ServiceManager.getService(JmeEngineService.class)
                                .getStateManager()
                                .attach(appState);

                        populateStateProperties(appState);

                        runButton.setEnabled(false);
                        stopButton.setEnabled(true);

                    } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException noSuchMethodException) {
                        noSuchMethodException.printStackTrace();
                    }
                }
            } else {
                populateStateProperties(null);
            }

        });
        stopButton.addActionListener(e -> {

            Class<? extends AppState> selectedClass = appstatesList.getSelectedValue();

            if (selectedClass != null) {

                AppState appState = ServiceManager.getService(JmeEngineService.class)
                        .getStateManager()
                        .getState(selectedClass);

                if (appState != null) {
                    ServiceManager.getService(JmeEngineService.class)
                            .getStateManager()
                            .detach(appState);
                }

                stopButton.setEnabled(false);
                runButton.setEnabled(true);


            }

            populateStateProperties(null);

        });

        appstatesList.addListSelectionListener(e -> {

            Class<? extends AppState> selectedClass = appstatesList.getSelectedValue();

            AppState appState = ServiceManager.getService(JmeEngineService.class)
                    .getStateManager()
                    .getState(selectedClass);

            if (appState != null) {
                runButton.setEnabled(false);
                stopButton.setEnabled(true);
            } else {
                runButton.setEnabled(true);
                stopButton.setEnabled(false);
            }

            populateStateProperties(appState);
        });
    }

    private void populateStateProperties(AppState appState) {

        appstatePropertiesPanel.removeAll();

        if (appState != null) {

            Map<Class<? extends Annotation>, Set<Method>> allAnnotatedMethods = new HashMap<>();

            Reflections reflections = new Reflections(appState.getClass(), new MethodAnnotationsScanner());

            Set<Method> annotatedMethods;

            annotatedMethods = reflections.getMethodsAnnotatedWith(FloatProperty.class);
            allAnnotatedMethods.put(FloatProperty.class, annotatedMethods);

            annotatedMethods = reflections.getMethodsAnnotatedWith(IntegerProperty.class);
            allAnnotatedMethods.put(IntegerProperty.class, annotatedMethods);

            annotatedMethods = reflections.getMethodsAnnotatedWith(ButtonProperty.class);
            allAnnotatedMethods.put(ButtonProperty.class, annotatedMethods);

            annotatedMethods = reflections.getMethodsAnnotatedWith(ColorProperty.class);
            allAnnotatedMethods.put(ColorProperty.class, annotatedMethods);

            appstatePropertiesPanel.setLayout(new MigLayout("fillx"));

            annotatedMethods = allAnnotatedMethods.get(FloatProperty.class);
            for (Method method : annotatedMethods) {

                final String methodPartial = method.getName().substring(3);

                try {

                    // We can get the method from the swing thread.
                    final Method setter = appState.getClass().getDeclaredMethod("set" + methodPartial, float.class);

                    // Get the value from the JME thread.
                    ServiceManager.getService(JmeEngineService.class).enqueue(() -> {

                        Float methodValue = null;

                        try {
                            methodValue = (float) method.invoke(appState);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }

                        // while we're here, technically the annotation belongs to the JME thread.
                        FloatProperty floatProperty = method.getAnnotation(FloatProperty.class);

                        final float finalValue = methodValue == null ? 0 : methodValue;
                        final float minValue = floatProperty.min();
                        final float maxValue = floatProperty.max();
                        final float stepValue = floatProperty.step();

                        // Back to the AWT thread...
                        SwingUtilities.invokeLater(() -> {

                            int value = (int) (finalValue * 1000);
                            int min = (int) (minValue * 1000);
                            int max = (int) (maxValue * 1000);
                            int step = (int) (stepValue * 1000);

                            JSlider slider = new JSlider(new DefaultBoundedRangeModel(value, step, min, max));

                            slider.addChangeListener(e1 -> {

                                final float sliderVal = slider.getValue() / 1000f;

                                // invoke the method on the JME thread (it's an appstate, belongs to JME).
                                ServiceManager.getService(JmeEngineService.class).enqueue(() -> {
                                    try {
                                        setter.invoke(appState, sliderVal);
                                    } catch (IllegalAccessException | InvocationTargetException e) {
                                        e.printStackTrace();
                                    }
                                });
                            });

                            appstatePropertiesPanel.add(new Label(methodPartial), "align right");
                            appstatePropertiesPanel.add(slider, "wrap, pushx, growx");

                            rootPane.revalidate();
                            rootPane.repaint();
                        });

                    });

                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }

            }

            annotatedMethods = allAnnotatedMethods.get(IntegerProperty.class);
            for (Method method : annotatedMethods) {

                final String methodPartial = method.getName().substring(3);

                try {

                    // We can get the method from the swing thread.
                    final Method setter = appState.getClass().getDeclaredMethod("set" + methodPartial, int.class);

                    // Get the value from the JME thread.
                    ServiceManager.getService(JmeEngineService.class).enqueue(() -> {

                        Integer methodValue = null;

                        try {
                            methodValue = (int) method.invoke(appState);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }

                        // while we're here, technically the annotation belongs to the JME thread.
                        IntegerProperty integerProperty = method.getAnnotation(IntegerProperty.class);

                        final int finalValue = methodValue == null ? 0 : methodValue;
                        final int minValue = integerProperty.min();
                        final int maxValue = integerProperty.max();
                        final int stepValue = integerProperty.step();

                        // Back to the AWT thread...
                        SwingUtilities.invokeLater(() -> {

                            JSlider slider = new JSlider(new DefaultBoundedRangeModel(finalValue, stepValue, minValue, maxValue));

                            slider.addChangeListener(e1 -> {

                                final int sliderVal = slider.getValue();

                                // invoke the method on the JME thread (it's an appstate, belongs to JME).
                                ServiceManager.getService(JmeEngineService.class).enqueue(() -> {
                                    try {
                                        setter.invoke(appState, sliderVal);
                                    } catch (IllegalAccessException | InvocationTargetException e) {
                                        e.printStackTrace();
                                    }
                                });
                            });

                            appstatePropertiesPanel.add(new Label(methodPartial), "align right");
                            appstatePropertiesPanel.add(slider, "wrap, pushx, growx");

                            rootPane.revalidate();
                            rootPane.repaint();
                        });

                    });

                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }

            }

            annotatedMethods = allAnnotatedMethods.get(ButtonProperty.class);
            for (Method method : annotatedMethods) {

                JButton button = new JButton(method.getName());
                button.addActionListener(e -> ServiceManager.getService(JmeEngineService.class).enqueue(() -> {
                    try {
                        method.invoke(appState);
                    } catch (IllegalAccessException | InvocationTargetException illegalAccessException) {
                        illegalAccessException.printStackTrace();
                    }
                }));

                appstatePropertiesPanel.add(new Label(method.getName()), "align right");
                appstatePropertiesPanel.add(button, "wrap, pushx, growx");

                rootPane.revalidate();
                rootPane.repaint();
            }

            annotatedMethods = allAnnotatedMethods.get(ColorProperty.class);
            for (Method method : annotatedMethods) {

                final String methodPartial = method.getName().substring(3);

                try {

                    // We can get the method from the swing thread.
                    Method setter = appState.getClass().getDeclaredMethod("set" + methodPartial, ColorRGBA.class);

                    // Get the value from the JME thread.
                    ServiceManager.getService(JmeEngineService.class).enqueue(() -> {

                        ColorRGBA methodValue = null;

                        try {
                            methodValue = (ColorRGBA) method.invoke(appState);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }

                        final ColorRGBA finalValue = methodValue;

                        // Back to the AWT thread...
                        SwingUtilities.invokeLater(() -> {

                            JColorChooser jColorChooser = new JColorChooser(ColorConverter.toColor(finalValue));
                            jColorChooser.getSelectionModel().addChangeListener(e -> {

                                // get the color in the AWT thread.
                                final ColorRGBA newColor = ColorConverter.toColorRGBA(jColorChooser.getColor());

                                // set the color in the JME thread.
                                ServiceManager.getService(JmeEngineService.class).enqueue(() -> {
                                    try {
                                        setter.invoke(appState, newColor);
                                    } catch (IllegalAccessException | InvocationTargetException illegalAccessException) {
                                        illegalAccessException.printStackTrace();
                                    }
                                });
                            });

                            Window parent = SwingUtilities.getWindowAncestor(rootPane);
                            JDialog colorDialog = new JDialog(parent, "Color Chooser: " + methodPartial);
                            colorDialog.setContentPane(jColorChooser);
                            colorDialog.pack();

                            JButton button = new JButton("Choose...");
                            button.addActionListener(e -> colorDialog.setVisible(true));

                            colorDialog.setLocationRelativeTo(button);

                            appstatePropertiesPanel.add(new Label(methodPartial), "align right");
                            appstatePropertiesPanel.add(button, "wrap, pushx, growx");

                            rootPane.revalidate();
                            rootPane.repaint();

                        });

                    });

                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }

            }
        }

        // finally
        rootPane.revalidate();
        rootPane.repaint();
    }

    @SuppressWarnings("unchecked")
    private Set<Class<? extends AppState>> getAllAnnotatedAppStates() {

        // Reflections reflections = new Reflections(new ConfigurationBuilder().setUrls(ClasspathHelper.forJavaClassPath()));
        Reflections reflections = new Reflections(new ConfigurationBuilder().forPackages(DevkitPackages.AppState));

        Set<Class<? extends AppState>> classes = new HashSet<>();

        for (Class<?> aClass : reflections.getTypesAnnotatedWith(DevKitAppState.class)) {
            if (isAppStateClass(aClass)) {
                classes.add((Class<? extends AppState>) aClass);
            }
        }

        return classes;
    }

    private boolean isAppStateClass(Class<?> clazz) {

        while (clazz != null) {

            if (clazz.isAssignableFrom(AppState.class)) {
                return true;
            }

            Class<?>[] interfaces = clazz.getInterfaces();

            for (Class<?> interfaceClass : interfaces) {

                if (interfaceClass.isAssignableFrom(AppState.class)) {
                    return true;
                }
            }

            clazz = clazz.getSuperclass();

            // everything is assignable from object.
            if (clazz == Object.class) {
                break;
            }
        }

        return false;
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        rootPane = new JPanel();
        rootPane.setLayout(new GridLayoutManager(1, 1, new Insets(5, 5, 5, 5), -1, -1));
        rootPane.setMinimumSize(new Dimension(-1, -1));
        final JSplitPane splitPane1 = new JSplitPane();
        splitPane1.setDividerLocation(150);
        splitPane1.setOrientation(0);
        rootPane.add(splitPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(250, 300), new Dimension(200, 200), null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        splitPane1.setRightComponent(scrollPane1);
        appstatePropertiesPanel.setMinimumSize(new Dimension(350, 150));
        scrollPane1.setViewportView(appstatePropertiesPanel);
        appstatePropertiesPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Properties", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 5, 0), -1, -1));
        splitPane1.setLeftComponent(panel1);
        panel1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 3, new Insets(0, 5, 0, 5), -1, -1));
        panel1.add(panel2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        runButton = new JButton();
        runButton.setText("Run");
        panel2.add(runButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel2.add(spacer1, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        stopButton = new JButton();
        stopButton.setText("Stop");
        panel2.add(stopButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JScrollPane scrollPane2 = new JScrollPane();
        panel3.add(scrollPane2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 75), null, null, 0, false));
        appstatesList = new JList();
        scrollPane2.setViewportView(appstatesList);
    }

    /**
     */
    public JComponent $$$getRootComponent$$$() {
        return rootPane;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        appstatePropertiesPanel = new JPanel();
        // appstatePropertiesPanel.setLayout(new GridLayoutManager(1, 2, new Insets(5, 5, 5, 5), 5, 5));
        appstatePropertiesPanel.setLayout(new MigLayout());

    }
}
