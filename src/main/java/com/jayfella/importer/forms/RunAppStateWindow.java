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
import java.util.List;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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

    private Map<Method, Object> getMethodValues(Set<Method> annotatedMethods, AppState appState) {

        Map<Method, Object> values = new HashMap<>();
        for (Method getter : annotatedMethods) {

            Object getterValue;

            try {

                // don't invoke methods that return void. They are methods for buttons, and it will "click" the button.
                getterValue = getter.getReturnType() != void.class
                        ? getter.invoke(appState)
                        : null;

                values.put(getter, getterValue);

            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        return values;
    }

    private void addComponentToGui(JComponent component, String labelText, String tabName, String[] tabs, JPanel[] tabPanels) {

        if (!tabName.isEmpty()) {

            int tabIndex = -1;

            for (int i = 0; i < tabs.length; i++) {
                if (tabs[i].equalsIgnoreCase(tabName)) {
                    tabIndex = i;
                    break;
                }
            }

            if (tabIndex > -1) {
                tabPanels[tabIndex].add(new JLabel(labelText), "align right");
                tabPanels[tabIndex].add(component, "wrap, pushx, growx");
            } else {
                appstatePropertiesPanel.add(new JLabel(labelText), "align right");
                appstatePropertiesPanel.add(component, "wrap, pushx, growx");
            }
        } else {
            appstatePropertiesPanel.add(new JLabel(labelText), "align right");
            appstatePropertiesPanel.add(component, "wrap, pushx, growx");
        }

    }

    private void populateStateProperties(AppState appState) {

        // we need to collect each JComponent before we add them because they get added in a random order, which isn't
        // the end of the world, but when we add a JTabbedPane it gets added at some random position, and that's not cool.
        // If we collect them, we can at least put them in alphabetical order or something.

        // The idea is to visit the JME thread only once.

        appstatePropertiesPanel.removeAll();

        if (appState != null) {

            appstatePropertiesPanel.setLayout(new MigLayout("fillx"));

            DevKitAppState devKitAppState = appState.getClass().getAnnotation(DevKitAppState.class);

            String[] tabs = devKitAppState.tabs();
            final JTabbedPane tabbedPane = tabs.length == 0 ? null : new JTabbedPane();
            final JPanel[] tabPanels = tabs.length == 0 ? null : new JPanel[tabs.length];

            if (tabs.length > 0) {
                for (int i = 0; i < tabs.length; i++) {

                    tabPanels[i] = new JPanel(new MigLayout());
                    tabbedPane.addTab(tabs[i].trim(), tabPanels[i]);
                }
            }

            ConcurrentHashMap<Class<? extends Annotation>, Map<Method, Object>> allAnnotatedMethods = new ConcurrentHashMap<>();

            // we need to get the values from the JME thread.
            ServiceManager.getService(JmeEngineService.class).enqueue(() -> {

                List<Class<? extends Annotation>> annotations = new ArrayList<>();
                Collections.addAll(annotations,
                        FloatProperty.class,
                        IntegerProperty.class,
                        ButtonProperty.class,
                        ColorProperty.class,
                        EnumProperty.class
                );

                Reflections reflections = new Reflections(appState.getClass(), new MethodAnnotationsScanner());

                Set<Method> annotatedMethods;
                Map<Method, Object> getterValues;

                for (Class<? extends Annotation> annotation : annotations) {
                    annotatedMethods = reflections.getMethodsAnnotatedWith(annotation);
                    getterValues = getMethodValues(annotatedMethods, appState);
                    allAnnotatedMethods.put(annotation, getterValues);
                }

                // now we have all the values. We need to create the components on the AWT thread.
                SwingUtilities.invokeLater(() -> {

                    for (Map.Entry<Class<? extends Annotation>, Map<Method, Object>> entry : allAnnotatedMethods.entrySet()) {

                        Class<? extends Annotation> annotation = entry.getKey();
                        Map<Method, Object> methodsAndValues = entry.getValue();

                        for (Map.Entry<Method, Object> methodEntries : methodsAndValues.entrySet()) {

                            Method getter = methodEntries.getKey();
                            String methodPartial = getter.getName().substring(3);

                            // We can get the method from the swing thread.
                            Method setter;

                            if (annotation == FloatProperty.class) {

                                try {
                                    setter = appState.getClass().getDeclaredMethod("set" + methodPartial, float.class);
                                } catch (NoSuchMethodException e) {
                                    e.printStackTrace();
                                    continue;
                                }

                                // this is dictating how many decimal places we're accurate to.
                                final float multiplier = 1000;

                                float methodValue = (float) methodEntries.getValue();
                                FloatProperty floatAnnotation = getter.getAnnotation(FloatProperty.class);

                                int value = (int) (methodValue * multiplier);
                                int min = (int) (floatAnnotation.min() * multiplier);
                                int max = (int) (floatAnnotation.max() * multiplier);
                                int step = (int) (floatAnnotation.step() * multiplier);

                                JSlider slider = new JSlider(new DefaultBoundedRangeModel(value, step, min, max));

                                slider.addChangeListener(event -> {

                                    final float sliderVal = slider.getValue() / multiplier;

                                    // invoke the method on the JME thread (it's an appstate, belongs to JME).
                                    ServiceManager.getService(JmeEngineService.class).enqueue(() -> {
                                        try {
                                            setter.invoke(appState, sliderVal);
                                        } catch (IllegalAccessException | InvocationTargetException e) {
                                            e.printStackTrace();
                                        }
                                    });
                                });

                                final String tab = floatAnnotation.tab().trim();

                                addComponentToGui(slider, methodPartial, tab, tabs, tabPanels);

//                                if (!tab.isEmpty()) {
//                                    int tabIndex = -1;
//                                    for (int i = 0; i < tabs.length; i++) {
//                                        if (tabs[i].equalsIgnoreCase(tab)) {
//                                            tabIndex = i;
//                                            break;
//                                        }
//                                    }
//
//                                    if (tabIndex > -1) {
//                                        contentPanels[tabIndex].add(new JLabel(methodPartial), "align right");
//                                        contentPanels[tabIndex].add(slider, "wrap, pushx, growx");
//                                    } else {
//                                        appstatePropertiesPanel.add(new JLabel(methodPartial), "align right");
//                                        appstatePropertiesPanel.add(slider, "wrap, pushx, growx");
//                                    }
//                                } else {
//                                    appstatePropertiesPanel.add(new JLabel(methodPartial), "align right");
//                                    appstatePropertiesPanel.add(slider, "wrap, pushx, growx");
//                                }

                            }

                            else if (annotation == IntegerProperty.class) {

                                try {
                                    setter = appState.getClass().getDeclaredMethod("set" + methodPartial, int.class);
                                } catch (NoSuchMethodException e) {
                                    e.printStackTrace();
                                    continue;
                                }

                                int methodValue = (int) methodEntries.getValue();
                                IntegerProperty integerAnnotation = getter.getAnnotation(IntegerProperty.class);

                                JSlider slider = new JSlider(new DefaultBoundedRangeModel(methodValue,
                                        integerAnnotation.step(),
                                        integerAnnotation.min(),
                                        integerAnnotation.max()));

                                slider.addChangeListener(event -> {

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

                                final String tab = integerAnnotation.tab().trim();
                                addComponentToGui(slider, methodPartial, tab, tabs, tabPanels);

//                                if (!tab.isEmpty()) {
//                                    int tabIndex = -1;
//                                    for (int i = 0; i < tabs.length; i++) {
//                                        if (tabs[i].equalsIgnoreCase(tab)) {
//                                            tabIndex = i;
//                                            break;
//                                        }
//                                    }
//
//                                    if (tabIndex > -1) {
//                                        contentPanels[tabIndex].add(new JLabel(methodPartial), "align right");
//                                        contentPanels[tabIndex].add(slider, "wrap, pushx, growx");
//                                    } else {
//                                        appstatePropertiesPanel.add(new JLabel(methodPartial), "align right");
//                                        appstatePropertiesPanel.add(slider, "wrap, pushx, growx");
//                                    }
//                                } else {
//                                    appstatePropertiesPanel.add(new JLabel(methodPartial), "align right");
//                                    appstatePropertiesPanel.add(slider, "wrap, pushx, growx");
//                                }

                            }

                            else if (annotation == ButtonProperty.class) {

                                JButton button = new JButton(getter.getName());
                                button.addActionListener(e -> ServiceManager.getService(JmeEngineService.class).enqueue(() -> {
                                    try {
                                        getter.invoke(appState);
                                    } catch (IllegalAccessException | InvocationTargetException illegalAccessException) {
                                        illegalAccessException.printStackTrace();
                                    }
                                }));

                                ButtonProperty buttonAnnotation = getter.getAnnotation(ButtonProperty.class);

                                final String tab = buttonAnnotation.tab().trim();
                                addComponentToGui(button, "", tab, tabs, tabPanels);
//                                if (!tab.isEmpty()) {
//                                    int tabIndex = -1;
//                                    for (int i = 0; i < tabs.length; i++) {
//                                        if (tabs[i].equalsIgnoreCase(tab)) {
//                                            tabIndex = i;
//                                            break;
//                                        }
//                                    }
//
//                                    if (tabIndex > -1) {
//                                        contentPanels[tabIndex].add(new JLabel(""), "align right");
//                                        contentPanels[tabIndex].add(button, "wrap, pushx, growx");
//                                    } else {
//                                        appstatePropertiesPanel.add(new JLabel(""), "align right");
//                                        appstatePropertiesPanel.add(button, "wrap, pushx, growx");
//                                    }
//                                } else {
//                                    appstatePropertiesPanel.add(new JLabel(""), "align right");
//                                    appstatePropertiesPanel.add(button, "wrap, pushx, growx");
//                                }

                            }

                            else if (annotation == EnumProperty.class) {

                                try {
                                    setter = appState.getClass().getDeclaredMethod("set" + methodPartial, getter.getReturnType());
                                } catch (NoSuchMethodException e) {
                                    e.printStackTrace();
                                    continue;
                                }


                                Enum<?> methodValue = (Enum<?>) methodEntries.getValue();
                                EnumProperty enumAnnotation = getter.getAnnotation(EnumProperty.class);

                                Enum<?>[] values = methodValue.getDeclaringClass().getEnumConstants();

                                JComboBox<Enum<?>> comboBox = new JComboBox<>(new DefaultComboBoxModel<>(values));
                                comboBox.setSelectedItem(methodValue);

                                comboBox.addActionListener(event -> {

                                    final Enum<?> comboVal = (Enum<?>) comboBox.getSelectedItem();
                                    ServiceManager.getService(JmeEngineService.class).enqueue(() -> {

                                        try {
                                            setter.invoke(appState, comboVal);
                                        } catch (IllegalAccessException | InvocationTargetException e) {
                                            e.printStackTrace();
                                        }

                                    });
                                });

                                final String tab = enumAnnotation.tab().trim();
                                addComponentToGui(comboBox, methodPartial, tab, tabs, tabPanels);
//                                if (!tab.isEmpty()) {
//                                    int tabIndex = -1;
//                                    for (int i = 0; i < tabs.length; i++) {
//                                        if (tabs[i].equalsIgnoreCase(tab)) {
//                                            tabIndex = i;
//                                            break;
//                                        }
//                                    }
//
//                                    if (tabIndex > -1) {
//                                        contentPanels[tabIndex].add(new JLabel(methodPartial), "align right");
//                                        contentPanels[tabIndex].add(comboBox, "wrap, pushx, growx");
//                                    } else {
//                                        appstatePropertiesPanel.add(new JLabel(methodPartial), "align right");
//                                        appstatePropertiesPanel.add(comboBox, "wrap, pushx, growx");
//                                    }
//                                } else {
//                                    appstatePropertiesPanel.add(new JLabel(methodPartial), "align right");
//                                    appstatePropertiesPanel.add(comboBox, "wrap, pushx, growx");
//                                }

                            }

                            else if (annotation == ColorProperty.class) {

                                try {
                                    setter = appState.getClass().getDeclaredMethod("set" + methodPartial, float.class);
                                } catch (NoSuchMethodException e) {
                                    e.printStackTrace();
                                    continue;
                                }


                                ColorRGBA methodValue = (ColorRGBA) methodEntries.getValue();
                                ColorProperty colorAnnotation = getter.getAnnotation(ColorProperty.class);

                                JColorChooser jColorChooser = new JColorChooser(ColorConverter.toColor(methodValue));
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

                                final String tab = colorAnnotation.tab().trim();
                                addComponentToGui(button, methodPartial, tab, tabs, tabPanels);
//                                if (!tab.isEmpty()) {
//                                    int tabIndex = -1;
//                                    for (int i = 0; i < tabs.length; i++) {
//                                        if (tabs[i].equalsIgnoreCase(tab)) {
//                                            tabIndex = i;
//                                            break;
//                                        }
//                                    }
//
//                                    if (tabIndex > -1) {
//                                        contentPanels[tabIndex].add(new JLabel(methodPartial), "align right");
//                                        contentPanels[tabIndex].add(button, "wrap, pushx, growx");
//                                    } else {
//                                        appstatePropertiesPanel.add(new JLabel(methodPartial), "align right");
//                                        appstatePropertiesPanel.add(button, "wrap, pushx, growx");
//                                    }
//                                } else {
//                                    appstatePropertiesPanel.add(new JLabel(methodPartial), "align right");
//                                    appstatePropertiesPanel.add(button, "wrap, pushx, growx");
//                                }

                            }

                        }

                    }

                    // Add the tabbedPane (if it exists) last.
                    if (tabbedPane != null) {
                        appstatePropertiesPanel.add(new JSeparator(), "span, wrap");
                        appstatePropertiesPanel.add(tabbedPane, "span, wrap");
                    }

                    // we've added all the controls, so we can finally repaint the surface.
                    rootPane.revalidate();
                    rootPane.repaint();

                });


            });

        }

    }

    private void populateStateProperties2(AppState appState) {

        // we need to collect each JComponent before we add them because they get added in a random order, which isn't
        // the end of the world, but when we add a JTabbedPane it gets added at some random position, and that's not cool.
        // If we collect them, we can at least put them in alphabetical order or something.

        appstatePropertiesPanel.removeAll();

        if (appState != null) {

            DevKitAppState devKitAppState = appState.getClass().getAnnotation(DevKitAppState.class);

            String[] tabs = devKitAppState.tabs();
            final JTabbedPane tabbedPane = tabs.length == 0 ? null : new JTabbedPane();
            final JPanel[] contentPanels = tabs.length == 0 ? null : new JPanel[tabs.length];

            if (tabs.length > 0) {
                for (int i = 0; i < tabs.length; i++) {

                    contentPanels[i] = new JPanel(new MigLayout());
                    tabbedPane.addTab(tabs[i].trim(), contentPanels[i]);
                }
            }

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

            annotatedMethods = reflections.getMethodsAnnotatedWith(EnumProperty.class);
            allAnnotatedMethods.put(EnumProperty.class, annotatedMethods);

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
                        final String tab = floatProperty.tab().trim();

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

                            if (!tab.isEmpty()) {
                                int tabIndex = -1;
                                for (int i = 0; i < tabs.length; i++) {
                                    if (tabs[i].equalsIgnoreCase(tab)) {
                                        tabIndex = i;
                                        break;
                                    }
                                }

                                if (tabIndex > -1) {
                                    contentPanels[tabIndex].add(new JLabel(methodPartial), "align right");
                                    contentPanels[tabIndex].add(slider, "wrap, pushx, growx");
                                } else {
                                    appstatePropertiesPanel.add(new JLabel(methodPartial), "align right");
                                    appstatePropertiesPanel.add(slider, "wrap, pushx, growx");
                                }
                            } else {
                                appstatePropertiesPanel.add(new JLabel(methodPartial), "align right");
                                appstatePropertiesPanel.add(slider, "wrap, pushx, growx");
                            }

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

                            appstatePropertiesPanel.add(new JLabel(methodPartial), "align right");
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

                appstatePropertiesPanel.add(new JLabel(method.getName()), "align right");
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

                            appstatePropertiesPanel.add(new JLabel(methodPartial), "align right");
                            appstatePropertiesPanel.add(button, "wrap, pushx, growx");

                            rootPane.revalidate();
                            rootPane.repaint();

                        });

                    });

                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }

            }

            annotatedMethods = allAnnotatedMethods.get(EnumProperty.class);
            for (Method method : annotatedMethods) {

                final String methodPartial = method.getName().substring(3);

                try {

                    // We can get the method from the swing thread.
                    final Method setter = appState.getClass().getDeclaredMethod("set" + methodPartial, method.getReturnType());

                    // Get the value from the JME thread.
                    ServiceManager.getService(JmeEngineService.class).enqueue(() -> {

                        Enum<?> methodValue = null;

                        try {
                            methodValue = (Enum<?>) method.invoke(appState);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }

                        final Enum<?> finalValue = methodValue;

                        // Back to the AWT thread...
                        SwingUtilities.invokeLater(() -> {

                            Enum<?>[] values = finalValue.getDeclaringClass().getEnumConstants();
                            JComboBox<Enum<?>> comboBox = new JComboBox<>(new DefaultComboBoxModel<>(values));

                            comboBox.setSelectedItem(finalValue);

                            comboBox.addActionListener(source -> {

                                final Enum<?> comboVal = (Enum<?>) comboBox.getSelectedItem();
                                ServiceManager.getService(JmeEngineService.class).enqueue(() -> {

                                    try {
                                        setter.invoke(appState, comboVal);
                                    } catch (IllegalAccessException | InvocationTargetException e) {
                                        e.printStackTrace();
                                    }

                                });
                            });

                            appstatePropertiesPanel.add(new JLabel(methodPartial), "align right");
                            appstatePropertiesPanel.add(comboBox, "wrap, pushx, growx");

                            rootPane.revalidate();
                            rootPane.repaint();
                        });

                    });

                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }

            }


            if (tabbedPane != null) {
                // invoke it later (i.e. after everything else)
                SwingUtilities.invokeLater(() -> {
                    appstatePropertiesPanel.add(tabbedPane, "span, wrap");
                });

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
        panel2.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JScrollPane scrollPane2 = new JScrollPane();
        panel2.add(scrollPane2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 75), null, null, 0, false));
        appstatesList = new JList();
        scrollPane2.setViewportView(appstatesList);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 1, new Insets(5, 5, 5, 5), -1, -1));
        panel1.add(panel3, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel3.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 3, new Insets(0, 5, 0, 5), -1, -1));
        panel3.add(panel4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        runButton = new JButton();
        runButton.setEnabled(false);
        runButton.setText("Run");
        panel4.add(runButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel4.add(spacer1, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        stopButton = new JButton();
        stopButton.setEnabled(false);
        stopButton.setText("Stop");
        panel4.add(stopButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     */
    public JComponent $$$getRootComponent$$$() {
        return rootPane;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        appstatePropertiesPanel = new JPanel();
        appstatePropertiesPanel.setLayout(new MigLayout());

    }
}
