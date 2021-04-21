package fr.exratio.jme.devkit.properties.component.control;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.jme3.anim.AnimClip;
import com.jme3.anim.AnimComposer;
import com.jme3.anim.tween.action.Action;
import fr.exratio.jme.devkit.properties.component.AbstractPropertyEditor;
import fr.exratio.jme.devkit.service.EditorJmeApplication;
import fr.exratio.jme.devkit.service.ServiceManager;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
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
  private JList<AnimClip> animationsList;
  private JButton playButton;
  private JButton stopButton;
  private JSlider timeSlider;
  private JPanel contentPanel;
  private JSlider speedSlider;
  private AnimClip animClip;
  private Action action;

  public AnimComposerEditor(AnimComposer animComposer) {
    super(animComposer);
  }

  {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
    $$$setupUI$$$();
  }

  /**
   * Method generated by IntelliJ IDEA GUI Designer >>> IMPORTANT!! <<< DO NOT edit this method OR
   * call it in your code!
   *
   * @noinspection ALL
   */
  private void $$$setupUI$$$() {
    createUIComponents();
    contentPanel.setLayout(new GridLayoutManager(4, 2, new Insets(0, 0, 0, 0), -1, -1));
    contentPanel.add(timeSlider, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST,
        GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW,
        GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    contentPanel.add(speedSlider, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST,
        GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW,
        GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final JLabel label1 = new JLabel();
    label1.setText("Time");
    contentPanel.add(label1,
        new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE,
            GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0,
            false));
    final JLabel label2 = new JLabel();
    label2.setText("Speed");
    contentPanel.add(label2,
        new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE,
            GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0,
            false));
    final JPanel panel1 = new JPanel();
    panel1.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
    contentPanel.add(panel1,
        new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null,
            null, 0, false));
    playButton.setText("Play");
    panel1.add(playButton,
        new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    stopButton.setText("Stop");
    panel1.add(stopButton,
        new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final Spacer spacer1 = new Spacer();
    panel1.add(spacer1, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER,
        GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null,
        0, false));
    final JScrollPane scrollPane1 = new JScrollPane();
    contentPanel.add(scrollPane1,
        new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null,
            null, null, 0, false));
    scrollPane1.setViewportView(animationsList);
  }

  /**
   * @noinspection ALL
   */
  public JComponent $$$getRootComponent$$$() {
    return contentPanel;
  }


  @Override
  protected AnimComposer computeValue() {
    return null;
  }

  private void createUIComponents() {
    contentPanel = this;
    final EditorJmeApplication engineService = ServiceManager
        .getService(EditorJmeApplication.class);
    timeSlider = new JSlider();
    timeSlider.setModel(animTimelineModel);

    engineService.enqueue(() -> {
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
          engineService.enqueue(() -> {
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
        e -> engineService.enqueue(() -> action.setSpeed(speedSlider.getValue() / 1000f)));

    timeSlider = new JSlider();
    timeSlider.addChangeListener(e -> {
      if (action != null) {
        engineService.enqueue(
            () -> value.setTime(AnimComposer.DEFAULT_LAYER, timeSlider.getValue() / 1000f));
      }
    });

    playButton = new JButton();
    playButton.addActionListener(e -> engineService.enqueue(() -> {
      action = value.setCurrentAction(animClip.getName());
      action.setSpeed(speedSlider.getValue() / 1000f);
    }));

    stopButton = new JButton();
    stopButton.addActionListener(e -> engineService.enqueue(() -> action.setSpeed(0)));

    // create a timer that queries the animation channel time so we can update the time slider position.
    timer = new Timer(10, e -> engineService.enqueue(() -> {
      if (animClip != null) {
        SwingUtilities.invokeLater(() -> {
          timeSlider.setValue((int) (value.getTime(AnimComposer.DEFAULT_LAYER) * 1000));
        });
      }
    }));

    timer.setRepeats(true);
    timer.start();

  }

}
