package com.pnambic.depan.gradle.dependencies.plugin;

import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

import com.pnambic.depan.gradle.dependencies.task.DepanMavenTask;

public class DepanMavenPlugin implements Plugin<Project> {

/* Maven scopes defined by DepAn [Apr-2018]

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

  public static final String DEFAULT_CONFIG_MAP =
      "compile:COMPILE_SCOPE"
      + ",runtime:RUNTIME_SCOPE"
      + ",testRuntime:TEST_SCOPE";

  public static final String GRAPH_INFO_SUFFIX = ".dgi";

  public static final String DEPAN_MAVEN_TASK = "depanMaven";

  @Override
  public void apply(Project project) {

    // Create the configuration extension with good default values.
    DepanMavenPluginExtension extension =
        project.getExtensions().create(DEPAN_MAVEN_TASK, DepanMavenPluginExtension.class, project);
    extension.setConfigurationMap(DEFAULT_CONFIG_MAP);
    extension.setGraphFile(project.getName() + GRAPH_INFO_SUFFIX);

    // Wire the extension values to the task attributes.
    project.getTasks().create(DEPAN_MAVEN_TASK, DepanMavenTask.class,
        new Action<DepanMavenTask>() {

          @Override
          public void execute(DepanMavenTask task) {
            task.getGraphFile().set(extension.getGraphFile());
            task.getConfigurationMap().set(extension.getConfigurationMap());
          }
        }
    );
  }
}
