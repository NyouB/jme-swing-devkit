package fr.exratio.jme.devkit.properties.component.control;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.LoopMode;
import fr.exratio.jme.devkit.properties.component.AbstractPropertyEditor;
import fr.exratio.jme.devkit.service.EditorJmeApplication;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;

public class AnimControlEditor extends AbstractPropertyEditor<AnimControl> {

  private final DefaultBoundedRangeModel animTimelineModel = new DefaultBoundedRangeModel(0, 1, 0,
      1000);
  private Timer timer;
  private JPanel contentPanel;
  // JME objects should be touched on the JME thread ONLY.
  private AnimChannel animChannel;
  private final EditorJmeApplication editorJmeApplication;

  // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
  private JButton playButton;

  public void cleanup() {
    editorJmeApplication.enqueue(value::clearChannels);
    timer.stop();
  }

  @Override
  protected AnimControl computeValue() {
    return value;
  }


  private void createUIComponents() {

    contentPanel = this;

    timeSlider = new JSlider();
    timeSlider.setModel(animTimelineModel);
    timeSlider.addChangeListener(e -> {
      final float animTime = timeSlider.getValue() / 1000f;
      editorJmeApplication.enqueue(() -> animChannel.setTime(animTime));
    });

    loopModeComboBox = new JComboBox();
    DefaultComboBoxModel<LoopMode> loopModeModel = new DefaultComboBoxModel<>(LoopMode.values());
    loopModeComboBox.setModel(loopModeModel);
    loopModeComboBox.setSelectedItem(LoopMode.Loop);

    animationsList = new JList();

    editorJmeApplication.enqueue(() -> {
      animChannel = value.createChannel();
      // fill our list
      SwingUtilities.invokeLater(() -> {
        DefaultListModel<String> listModel = new DefaultListModel<>();
        listModel.addAll(value.getAnimationNames());
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
          editorJmeApplication.enqueue(() -> {
            animChannel.setAnim(animName);
            animChannel.setLoopMode(loopMode);
            // update the slider on the AWT thread.
            SwingUtilities.invokeLater(
                () -> animTimelineModel.setMaximum((int) (animChannel.getAnimMaxTime() * 1000)));
          });
        }
      }
    });

    // the extent is the step, basically. How much it moves when you click either side
    // of the slider knob.
    // the speed is multiplied by 1000, so 1000 = 1.0f speed. We do this because a JSlider is an INT slider.
    speedSlider = new JSlider();
    speedSlider.setModel(new DefaultBoundedRangeModel(1000, 250, 0, 2000));
    speedSlider.addChangeListener(
        e -> editorJmeApplication
            .enqueue(() -> animChannel.setSpeed(speedSlider.getValue() / 1000f)));

    playButton = new JButton();
    playButton.addActionListener(e -> {
      // pressing stop sets the animSpeed to zero, so we need to set it to the value of the speed slider
      // when we press play.
      editorJmeApplication.enqueue(() -> {
        animChannel.setSpeed(speedSlider.getValue() / 1000f);
        animChannel.setLoopMode((LoopMode) loopModeComboBox.getSelectedItem());
        animChannel.setTime(0);
      });
    });

    stopButton = new JButton();
    // set the speed on the animChannel, but not the slider.
    stopButton.addActionListener(e -> editorJmeApplication.enqueue(() -> animChannel.setSpeed(0)));

    // create a timer that queries the animation channel time so we can update the time slider position.
    timer = new Timer(10, e -> {
      editorJmeApplication.enqueue(() -> {
        if (animChannel != null) {
          SwingUtilities
              .invokeLater(() -> timeSlider.setValue((int) (animChannel.getTime() * 1000)));
        }
      });
    });
    timer.setRepeats(true);
    timer.start();
  }

  private JButton stopButton;
  private JSlider timeSlider;
  private JSlider speedSlider;
  private JComboBox loopModeComboBox;
  private JList<String> animationsList;

  public AnimControlEditor(AnimControl animControl,
      EditorJmeApplication editorJmeApplication) {
    super(animControl);
    initComponents();
    this.editorJmeApplication = editorJmeApplication;
  }

  private void initComponents() {
    // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
    createUIComponents();

    var panel1 = new JPanel();
    var hSpacer1 = new Spacer();
    var label1 = new JLabel();
    var label2 = new JLabel();
    var scrollPane1 = new JScrollPane();

    //======== this ========
    setLayout(new GridLayoutManager(4, 3, new Insets(0, 0, 0, 0), -1, -1));

    //======== panel1 ========
    {
      panel1.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));

      //---- playButton ----
      playButton.setText("Play");
      panel1.add(playButton, new GridConstraints(0, 0, 1, 1,
          GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL,
          GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
          GridConstraints.SIZEPOLICY_FIXED,
          null, null, null));
      panel1.add(hSpacer1, new GridConstraints(0, 2, 1, 1,
          GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL,
          GridConstraints.SIZEPOLICY_CAN_GROW | GridConstraints.SIZEPOLICY_WANT_GROW,
          GridConstraints.SIZEPOLICY_CAN_SHRINK,
          null, null, null));

      //---- stopButton ----
      stopButton.setText("Stop");
      panel1.add(stopButton, new GridConstraints(0, 1, 1, 1,
          GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL,
          GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
          GridConstraints.SIZEPOLICY_FIXED,
          null, null, null));
    }
    add(panel1, new GridConstraints(1, 0, 1, 3,
        GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
        null, null, null));

    //---- label1 ----
    label1.setText("Time");
    add(label1, new GridConstraints(2, 0, 1, 1,
        GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE,
        GridConstraints.SIZEPOLICY_FIXED,
        GridConstraints.SIZEPOLICY_FIXED,
        null, null, null));
    add(timeSlider, new GridConstraints(2, 1, 1, 2,
        GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL,
        GridConstraints.SIZEPOLICY_CAN_GROW | GridConstraints.SIZEPOLICY_WANT_GROW,
        GridConstraints.SIZEPOLICY_FIXED,
        null, null, null));

    //---- label2 ----
    label2.setText("Speed");
    add(label2, new GridConstraints(3, 0, 1, 1,
        GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE,
        GridConstraints.SIZEPOLICY_FIXED,
        GridConstraints.SIZEPOLICY_FIXED,
        null, null, null));
    add(speedSlider, new GridConstraints(3, 1, 1, 1,
        GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL,
        GridConstraints.SIZEPOLICY_CAN_GROW | GridConstraints.SIZEPOLICY_WANT_GROW,
        GridConstraints.SIZEPOLICY_FIXED,
        null, null, null));
    add(loopModeComboBox, new GridConstraints(3, 2, 1, 1,
        GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL,
        GridConstraints.SIZEPOLICY_CAN_GROW,
        GridConstraints.SIZEPOLICY_FIXED,
        null, null, null));

    //======== scrollPane1 ========
    {
      scrollPane1.setBorder(new TitledBorder(new BevelBorder(BevelBorder.LOWERED), ""));
      scrollPane1.setViewportView(animationsList);
    }
    add(scrollPane1, new GridConstraints(0, 0, 1, 3,
        GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW
            | GridConstraints.SIZEPOLICY_WANT_GROW,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW
            | GridConstraints.SIZEPOLICY_WANT_GROW,
        null, null, null));
    // JFormDesigner - End of component initialization  //GEN-END:initComponents
  }
  // JFormDesigner - End of variables declaration  //GEN-END:variables
}
