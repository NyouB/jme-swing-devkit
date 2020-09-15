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

            if (!e.getValueIsAdjusting()) {
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
            }
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
                        EnumProperty.class,
                        ListProperty.class
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

                            }

                            else if (annotation == ListProperty.class) {

                                // for this list we need a method that gets all list values, a getter and a setter.
                                // the given method returns all values
                                // the `listAnnotation.accessorName()` value specifies the getter/setter.

                                ListProperty listAnnotation = getter.getAnnotation(ListProperty.class);

                                Method listGetter;
                                Method listSetter;

                                try {
                                    listGetter = appState.getClass().getDeclaredMethod("get" + listAnnotation.accessorName());
                                    listSetter = appState.getClass().getDeclaredMethod("set" + listAnnotation.accessorName(), int.class);
                                } catch (NoSuchMethodException e) {
                                    e.printStackTrace();
                                    continue;
                                }

                                JComponent component = null;
                                Object[] methodValue = (Object[]) methodEntries.getValue();

                                if (listAnnotation.listType() == ListType.List) {

                                    DefaultListModel<Object> model = new DefaultListModel<>();

                                    for (Object object : methodValue) {
                                        model.addElement(object);
                                    }

                                    JList<Object> list = new JList<>(model);

                                    try {
                                        int getterValue = (int) listGetter.invoke(appState);
                                        list.setSelectedIndex(getterValue);
                                    } catch (IllegalAccessException | InvocationTargetException e) {
                                        e.printStackTrace();
                                    }

                                    list.addListSelectionListener(listener -> {

                                        if (!listener.getValueIsAdjusting()) {

                                            final int selectedValue = list.getSelectedIndex();

                                            ServiceManager.getService(JmeEngineService.class).enqueue(() -> {

                                                try {
                                                    listSetter.invoke(appState, selectedValue);
                                                } catch (IllegalAccessException | InvocationTargetException e) {
                                                    e.printStackTrace();
                                                }

                                            });

                                        }

                                    });

                                    component = list;

                                }

                                else if (listAnnotation.listType() == ListType.ComboBox) {

                                    DefaultComboBoxModel<Object> model = new DefaultComboBoxModel<>();

                                    for (Object object : methodValue) {
                                        model.addElement(object);
                                    }

                                    JComboBox<Object> comboBox = new JComboBox<>(model);

                                    try {
                                        int getterValue = (int) listGetter.invoke(appState);
                                        comboBox.setSelectedIndex(getterValue);
                                    } catch (IllegalAccessException | InvocationTargetException e) {
                                        e.printStackTrace();
                                    }

                                    comboBox.addActionListener(listener -> {

                                        final int selectedValue = comboBox.getSelectedIndex();

                                        ServiceManager.getService(JmeEngineService.class).enqueue(() -> {

                                            try {
                                                listSetter.invoke(appState, selectedValue);
                                            } catch (IllegalAccessException | InvocationTargetException e) {
                                                e.printStackTrace();
                                            }

                                        });


                                    });

                                    component = comboBox;

                                }

                                String tab = listAnnotation.tab();
                                addComponentToGui(component, methodPartial, tab, tabs, tabPanels);

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
