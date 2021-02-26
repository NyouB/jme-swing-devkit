package fr.exratio.devkit.plugin.configuration;

public class PluginConfiguration {

  private String id = null;
  private String prefix = null;
  private String version = null;
  private String[] dependencies = new String[0];
  private String[] softDependencies = new String[0];

  public PluginConfiguration() {

  }

  /**
   * Returns the unique identifier for the plugin.
   *
   * @return the unique identifier for the plugin.
   */
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getPrefix() {
    return prefix;
  }

  public void setPrefix(String prefix) {
    this.prefix = prefix;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  /**
   * Returns the dependencies that the plugin requires to function.
   *
   * @return the dependencies that the plugin requires to function.
   */
  public String[] getDependencies() {
    return dependencies;
  }

  public void setDependencies(String[] dependencies) {
    if (dependencies != null) {
      this.dependencies = dependencies;
    } else {
      // avoid null-pointers on iteration.
      this.dependencies = new String[0];
    }
  }

  /**
   * Returns the dependencies that the plugin can use to increase functionality.
   *
   * @return the dependencies that the plugin can use to increase functionality.
   */
  public String[] getSoftDependencies() {
    return softDependencies;
  }

  public void setSoftDependencies(String[] softDependencies) {
    if (softDependencies != null) {
      this.softDependencies = softDependencies;
    } else {
      // avoid null-pointers on iteration.
      this.softDependencies = new String[0];
    }
  }

}
