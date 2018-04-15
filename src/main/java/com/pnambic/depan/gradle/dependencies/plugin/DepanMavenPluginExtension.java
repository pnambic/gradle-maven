package com.pnambic.depan.gradle.dependencies.plugin;

import org.gradle.api.Project;
import org.gradle.api.provider.Property;

public class DepanMavenPluginExtension {

  private Property<String> graphFile;

  private Property<String> configurationMap;

  public DepanMavenPluginExtension(Project project) {
    graphFile = project.getObjects().property(String.class);
    configurationMap = project.getObjects().property(String.class);
  }

  public Property<String> getGraphFile() {
    return graphFile;
  }

  public void setGraphFile(String graphFile) {
    this.graphFile.set(graphFile);
  }

  public Property<String> getConfigurationMap() {
    return configurationMap;
  }

  public void setConfigurationMap(String configurationMap) {
    this.configurationMap.set(configurationMap);
  }
}
