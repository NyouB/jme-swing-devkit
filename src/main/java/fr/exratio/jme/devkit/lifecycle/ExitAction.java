package fr.exratio.jme.devkit.lifecycle;

import fr.exratio.jme.devkit.service.EditorJmeApplication;
import fr.exratio.jme.devkit.service.MainPageController;
import fr.exratio.jme.devkit.service.ServiceManager;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.SwingUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExitAction extends AbstractAction {

  private final EditorJmeApplication editorJmeApplication;
  private final MainPageController mainPageController;

  @Autowired
  public ExitAction(EditorJmeApplication editorJmeApplication,
      MainPageController mainPageController) {
    this.editorJmeApplication = editorJmeApplication;
    this.mainPageController = mainPageController;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    editorJmeApplication.stop();
    SwingUtilities.getWindowAncestor(mainPageController.getMainPage()).dispose();
  }

  @Override
  public boolean accept(Object sender) {
    return false;
  }
}
