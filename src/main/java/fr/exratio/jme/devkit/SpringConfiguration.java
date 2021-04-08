package fr.exratio.jme.devkit;

import com.google.common.eventbus.EventBus;
import javax.swing.JFrame;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "devkit.appstate.tool")
public class SpringConfiguration {

  @Bean
  public JFrame getJFrame(){
    return new JFrame();
  }

  @Bean
  public EventBus getEventBus(){
    return new EventBus();
  }

}
