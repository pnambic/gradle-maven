package com.pnambic.depan.gradle.dependencies.task;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ConfigurationContainer;
import org.gradle.api.artifacts.result.DependencyResult;
import org.gradle.api.artifacts.result.ResolutionResult;

import com.pnambic.depan.gradle.dependencies.Dependencies;

public class DepanMavenExec {

  private final Writer graphFile;

  private final Project project;

  private final Map<String, String> configurationMap;

  public DepanMavenExec(Writer graphFile, Project project, Map<String, String> configurationMap) {
    this.graphFile = graphFile;
    this.project = project;
    this.configurationMap = configurationMap;
  }

  public void generate() {
    GraphModelBuilder builder = new GraphModelBuilder();

    ConfigurationContainer cnfgs = project.getConfigurations();

    for (Configuration cnfg : cnfgs) {
      analyzeConfiguration(builder, cnfg);
    }

    writeDgi(builder);
  }

  private void analyzeConfiguration(GraphModelBuilder builder, Configuration cnfg) {
    if (!cnfg.isCanBeResolved()) {
      return;
    }

    String relation = getRelationName(cnfg.getName());
    if (null == relation) {
      return;
    }

    ResolutionResult data = cnfg.getIncoming().getResolutionResult();
    analyzeResolution(builder, relation, data);
  }

  private void analyzeResolution(
      GraphModelBuilder graphBuilder, String relation, ResolutionResult data) {
    for (DependencyResult dep : data.getAllDependencies()) {
      GraphNode head = graphBuilder.mapNode(MavenArtifact.from(dep.getFrom(), project));
      GraphNode tail = graphBuilder.mapNode(MavenArtifact.from(dep.getRequested()));
      GraphEdge edge = new GraphEdge(relation, head, tail);
      graphBuilder.addEdge(edge);
    }
  }

  private void writeDgi(GraphModelBuilder builder) {
    try {
      GraphModelWriter writer = new GraphModelWriter(graphFile);
      writer.out(builder);
    } catch (IOException errIo) {
      Dependencies.LOG.error("Unable to write .dgi output", errIo);
    }
  }

  private String getRelationName(String label) {
    return configurationMap.get(label);
  }

/*

  COMPILE_SCOPE("build", "uses build"),
  IMPORT_SCOPE("imports", "uses import"),
  PROVIDED_SCOPE("provides", "uses provider"),
  RUNTIME_SCOPE("runtime use", "uses runtime"),
  SYSTEM_SCOPE("system use", "uses system"),
  TEST_SCOPE("test use", "uses test"),

  TOOL_DEPEND("tool", "uses"),
  PARENT_DEPEND("parent", "child"),
  MODULE_DEPEND("module", "master"),
  PROPERTY_DEPEND("property", "uses property")

 */
}
