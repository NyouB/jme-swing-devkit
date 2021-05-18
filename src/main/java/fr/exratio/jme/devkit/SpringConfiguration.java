package fr.exratio.jme.devkit;

import com.google.common.eventbus.EventBus;
import fr.exratio.jme.devkit.jme.SceneObjectHighlighterState;
import javax.swing.JFrame;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"devkit.appstate.tool", "fr.exratio.jme.devkit"})
public class SpringConfiguration {

  @Bean
  public JFrame getJFrame() {
    return new JFrame();
  }

  @Bean
  public EventBus getEventBus() {
    return new EventBus();
  }

  @Bean
  public SceneObjectHighlighterState getSceneHighlighterState() {
    return new SceneObjectHighlighterState();
  }

}
