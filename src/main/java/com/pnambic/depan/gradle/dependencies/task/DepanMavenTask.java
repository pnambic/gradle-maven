package com.pnambic.depan.gradle.dependencies.task;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.Map;

import org.gradle.api.DefaultTask;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;

import com.google.common.base.Splitter;
import com.pnambic.depan.gradle.dependencies.Dependencies;

public class DepanMavenTask extends DefaultTask {

  private static final String DESCRIPTION = "Creates a DepAn .dgi file with Maven dependencies.";

  private static final char COLON = ':';
  private static final char COMMA = ',';

  private Property<String> graphFile;

  private Property<String> configurationMap;

  public DepanMavenTask() {
    graphFile = getProject().getObjects().property(String.class);
    configurationMap = getProject().getObjects().property(String.class);
  }

  @Input
  public Property<String> getGraphFile() {
    return graphFile;
  }

  @Input
  public Property<String> getConfigurationMap() {
    return configurationMap;
  }

  @Override
  public String getDescription() {
    return DESCRIPTION;
  }

  @TaskAction
  public void generateGraph() {
    Map<String, String> cnfgMap = buildConfigurationMap();
    GraphModelBuilder model = new ConfigurationAnalyzer(getProject(), cnfgMap).analyze();

    // With no designated output, write to standard out
    if (!graphFile.isPresent()) {
      OutputStreamWriter dst = new OutputStreamWriter(System.out);
      writeGraphData(dst, model);
      return;
    }

    // Handle open/close for named file
    try (Writer graphWriter = openGraphWriter()) {
      writeGraphData(graphWriter, model);
    } catch (IOException errIO) {
      Dependencies.LOG.warn("Unable to write output file {}", graphFile.getOrNull());
    }
  }

  private Writer openGraphWriter() {
    try {
      FileOutputStream stream = new FileOutputStream(graphFile.get());
      return new OutputStreamWriter(stream);
    } catch (IOException errIo) {
      throw new RuntimeException(errIo);
    }
  }

  private Map<String, String> buildConfigurationMap() {
    if (!configurationMap.isPresent()) {
      Dependencies.LOG.warn("Empty configuration map - Graph info will be empty");
      return Collections.emptyMap();
    }

    String cnfgData = configurationMap.get();
    return Splitter.on(COMMA).trimResults().withKeyValueSeparator(COLON).split(cnfgData);
  }

  private void writeGraphData(Writer dst, GraphModelBuilder builder) {
    try {
      GraphModelWriter writer = new GraphModelWriter(dst);
      writer.out(builder);
    } catch (IOException errIo) {
      Dependencies.LOG.error("Unable to write .dgi output", errIo);
    }
  }
}
