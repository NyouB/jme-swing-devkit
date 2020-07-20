package com.jayfella.importer.properties.component.control;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.jayfella.importer.properties.ControlSdkComponent;
import com.jayfella.importer.service.JmeEngineService;
import com.jayfella.importer.service.ServiceManager;
import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.LoopMode;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;

public class AnimControlComponent extends ControlSdkComponent<AnimControl> {

    private final Timer timer;

    private JList<String> animationsList;
    private JPanel contentPanel;
    private JButton playButton;
    private JButton stopButton;
    private JSlider timeSlider;
    private JSlider speedSlider;
    private JComboBox<LoopMode> loopModeComboBox;

    private final DefaultBoundedRangeModel animTimelineModel = new DefaultBoundedRangeModel(0, 1, 0, 1000);

    // JME objects should touched on the JME thread ONLY.
    private AnimChannel animChannel;

    public AnimControlComponent(AnimControl animControl) {
        super(animControl);

        final JmeEngineService engineService = ServiceManager.getService(JmeEngineService.class);

        timeSlider.setModel(animTimelineModel);

        DefaultComboBoxModel<LoopMode> loopModeModel = new DefaultComboBoxModel<>(LoopMode.values());
        loopModeComboBox.setModel(loopModeModel);
        loopModeComboBox.setSelectedItem(LoopMode.Loop);

        engineService.enqueue(() -> {

            final Collection<String> animNames = animControl.getAnimationNames();

            animChannel = animControl.createChannel();

            // fill our list
            SwingUtilities.invokeLater(() -> {

                DefaultListModel<String> listModel = new DefaultListModel<>();
                for (String animName : animNames) {
                    listModel.addElement(animName);
                }

                animationsList.setModel(listModel);

            });

        });

        animationsList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                final String animName = animationsList.getSelectedValue();
                final LoopMode loopMode = (LoopMode) loopModeComboBox.getSelectedItem();

                if (animName != null) {

                    // set the animation on the JME thread.
                    engineService.enqueue(() -> {

                        animChannel.setAnim(animName);
                        animChannel.setLoopMode(loopMode);

                        final float maxTime = animChannel.getAnimMaxTime();

                        // update the slider on the AWT thread.
                        SwingUtilities.invokeLater(() -> {

                            int max = (int) (maxTime * 1000);
                            animTimelineModel.setMaximum(max);

                        });
                    });
                }
            }
        });

        // the extent is the step, basically. How much it moves when you click either side
        // of the slider knob.
        // the speed is multiplied by 1000, so 1000 = 1.0f speed. We do this because a JSlider is an INT slider.
        speedSlider.setModel(new DefaultBoundedRangeModel(1000, 250, 0, 2000));
        speedSlider.addChangeListener(e -> {

            final float animSpeed = speedSlider.getValue() / 1000f;

            engineService.enqueue(() -> {
                animChannel.setSpeed(animSpeed);
            });

        });

        timeSlider.addChangeListener(e -> {

            final float animTime = timeSlider.getValue() / 1000f;

            engineService.enqueue(() -> {
                animChannel.setTime(animTime);
            });

        });


        playButton.addActionListener(e -> {

            // pressing stop sets the animSpeed to zero, so we need to set it to the value of the speed slider
            // when we press play.

            final LoopMode loopMode = (LoopMode) loopModeComboBox.getSelectedItem();
            final float animSpeed = speedSlider.getValue() / 1000f;

            engineService.enqueue(() -> {

                animChannel.setSpeed(animSpeed);
                animChannel.setLoopMode(loopMode);

                animChannel.setTime(0);
            });

        });

        stopButton.addActionListener(e -> {
            engineService.enqueue(() -> {
                // set the speed on the animChannel, but not the slider.
                animChannel.setSpeed(0);
            });

        });

        // create a timer that queries the animation channel time so we can update the time slider position.
        timer = new Timer(10, e -> {

            engineService.enqueue(() -> {

                if (animChannel != null) {
                    final float time = animChannel.getTime();

                    SwingUtilities.invokeLater(() -> {
                        timeSlider.setValue((int) (time * 1000));
                    });

                }

            });

        });

        timer.setRepeats(true);
        timer.start();

    }

    @Override
    public JComponent getJComponent() {
        return contentPanel;
    }

    @Override
    public boolean isNullable() {
        return false;
    }

    @Override
    public void setNullable(boolean value) {

    }

    @Override
    public void cleanup() {
        ServiceManager.getService(JmeEngineService.class).enqueue(object::clearChannels);
        timer.stop();
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
        contentPanel.setLayout(new GridLayoutManager(4, 3, new Insets(0, 0, 0, 0), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        contentPanel.add(panel1, new GridConstraints(1, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        playButton = new JButton();
        playButton.setText("Play");
        panel1.add(playButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        stopButton = new JButton();
        stopButton.setText("Stop");
        panel1.add(stopButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Time");
        contentPanel.add(label1, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        timeSlider = new JSlider();
        contentPanel.add(timeSlider, new GridConstraints(2, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Speed");
        contentPanel.add(label2, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        speedSlider = new JSlider();
        contentPanel.add(speedSlider, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        loopModeComboBox = new JComboBox();
        contentPanel.add(loopModeComboBox, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        contentPanel.add(scrollPane1, new GridConstraints(0, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        scrollPane1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        animationsList = new JList();
        scrollPane1.setViewportView(animationsList);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPanel;
    }

}
