package fr.exratio.jme.devkit.config;

import fr.exratio.jme.devkit.config.DevKitConfig.ToolConfiguration;
import fr.exratio.jme.devkit.config.DevKitConfig.ToolConfigurationMapper;
import fr.exratio.jme.devkit.tool.Tool;
import fr.exratio.jme.devkit.tool.Tool.ToolBuilder;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-04-05T18:02:16+0200",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 13.0.1 (Oracle Corporation)"
)
public class DevKitConfig$ToolConfigurationMapperImpl implements ToolConfigurationMapper {

    @Override
    public ToolConfiguration toolToToolConfiguration(Tool tool) {
        if ( tool == null ) {
            return null;
        }

        ToolConfiguration toolConfiguration = new ToolConfiguration();

        return toolConfiguration;
    }

    @Override
    public Tool toolConfigurationToTool(ToolConfiguration toolConfiguration) {
        if ( toolConfiguration == null ) {
            return null;
        }

        ToolBuilder tool = Tool.toolBuilder();

        return tool.build();
    }
}
