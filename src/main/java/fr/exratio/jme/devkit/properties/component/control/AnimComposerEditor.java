package fr.exratio.jme.devkit.properties.component.control;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.jme3.anim.AnimClip;
import com.jme3.anim.AnimComposer;
import com.jme3.anim.tween.action.Action;
import fr.exratio.jme.devkit.properties.component.AbstractPropertyEditor;
import fr.exratio.jme.devkit.service.EditorJmeApplication;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class AnimComposerEditor extends AbstractPropertyEditor<AnimComposer> {

  private final DefaultBoundedRangeModel animTimelineModel = new DefaultBoundedRangeModel(0, 1, 0,
      1000);
  private Timer timer;
  private JPanel contentPanel;
  private AnimClip animClip;
  private Action action;
  private final EditorJmeApplication editorJmeApplication;

  // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
  private JSlider timeSlider;


  @Override
  protected AnimComposer computeValue() {
    return null;
  }

  private void createUIComponents() {
    contentPanel = this;

    timeSlider = new JSlider();
    timeSlider.setModel(animTimelineModel);

    editorJmeApplication.enqueue(() -> {
      // fill our list
      SwingUtilities.invokeLater(() -> {
        DefaultListModel<AnimClip> listModel = new DefaultListModel<>();
        listModel.addAll(value.getAnimClips());
        animationsList.setModel(listModel);
      });
    });

    animationsList = new JList();
    animationsList.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {

        final AnimClip selectedClip = animationsList.getSelectedValue();

        if (selectedClip != null) {
          editorJmeApplication.enqueue(() -> {
            animClip = selectedClip;
            SwingUtilities.invokeLater(
                () -> animTimelineModel.setMaximum((int) (animClip.getLength() * 1000)));
          });
        }
      }
    });

    speedSlider = new JSlider();
    speedSlider.setModel(new DefaultBoundedRangeModel(1000, 250, 0, 2000));
    speedSlider.addChangeListener(
        e -> editorJmeApplication.enqueue(() -> action.setSpeed(speedSlider.getValue() / 1000f)));

    timeSlider = new JSlider();
    timeSlider.addChangeListener(e -> {
      if (action != null) {
        editorJmeApplication.enqueue(
            () -> value.setTime(AnimComposer.DEFAULT_LAYER, timeSlider.getValue() / 1000f));
      }
    });

    playButton = new JButton();
    playButton.addActionListener(e -> editorJmeApplication.enqueue(() -> {
      action = value.setCurrentAction(animClip.getName());
      action.setSpeed(speedSlider.getValue() / 1000f);
    }));

    stopButton = new JButton();
    stopButton.addActionListener(e -> editorJmeApplication.enqueue(() -> action.setSpeed(0)));

    // create a timer that queries the animation channel time so we can update the time slider position.
    timer = new Timer(10, e -> editorJmeApplication.enqueue(() -> {
      if (animClip != null) {
        SwingUtilities.invokeLater(() -> {
          timeSlider.setValue((int) (value.getTime(AnimComposer.DEFAULT_LAYER) * 1000));
        });
      }
    }));

    timer.setRepeats(true);
    timer.start();

  }

  private JSlider speedSlider;
  private JButton playButton;
  private JButton stopButton;
  private JList<AnimClip> animationsList;

  public AnimComposerEditor(AnimComposer animComposer,
      EditorJmeApplication editorJmeApplication) {
    super(animComposer);
    initComponents();
    this.editorJmeApplication = editorJmeApplication;
  }

  private void initComponents() {
    // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
    createUIComponents();

    var label1 = new JLabel();
    var label2 = new JLabel();
    var panel1 = new JPanel();
    var hSpacer1 = new Spacer();
    var scrollPane1 = new JScrollPane();

    //======== this ========
    setLayout(new GridLayoutManager(4, 2, new Insets(0, 0, 0, 0), -1, -1));
    add(timeSlider, new GridConstraints(2, 1, 1, 1,
        GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL,
        GridConstraints.SIZEPOLICY_CAN_GROW | GridConstraints.SIZEPOLICY_WANT_GROW,
        GridConstraints.SIZEPOLICY_FIXED,
        null, null, null));
    add(speedSlider, new GridConstraints(3, 1, 1, 1,
        GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL,
        GridConstraints.SIZEPOLICY_CAN_GROW | GridConstraints.SIZEPOLICY_WANT_GROW,
        GridConstraints.SIZEPOLICY_FIXED,
        null, null, null));

    //---- label1 ----
    label1.setText("Time");
    add(label1, new GridConstraints(2, 0, 1, 1,
        GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE,
        GridConstraints.SIZEPOLICY_FIXED,
        GridConstraints.SIZEPOLICY_FIXED,
        null, null, null));

    //---- label2 ----
    label2.setText("Speed");
    add(label2, new GridConstraints(3, 0, 1, 1,
        GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE,
        GridConstraints.SIZEPOLICY_FIXED,
        GridConstraints.SIZEPOLICY_FIXED,
        null, null, null));

    //======== panel1 ========
    {
      panel1.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));

      //---- playButton ----
      playButton.setText("Play");
      panel1.add(playButton, new GridConstraints(0, 0, 1, 1,
          GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE,
          GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
          GridConstraints.SIZEPOLICY_FIXED,
          null, null, null));

      //---- stopButton ----
      stopButton.setText("Stop");
      panel1.add(stopButton, new GridConstraints(0, 1, 1, 1,
          GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE,
          GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
          GridConstraints.SIZEPOLICY_FIXED,
          null, null, null));
      panel1.add(hSpacer1, new GridConstraints(0, 2, 1, 1,
          GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL,
          GridConstraints.SIZEPOLICY_CAN_GROW | GridConstraints.SIZEPOLICY_WANT_GROW,
          GridConstraints.SIZEPOLICY_CAN_SHRINK,
          null, null, null));
    }
    add(panel1, new GridConstraints(1, 0, 1, 2,
        GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
        null, null, null));

    //======== scrollPane1 ========
    {
      scrollPane1.setViewportView(animationsList);
    }
    add(scrollPane1, new GridConstraints(0, 0, 1, 2,
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
