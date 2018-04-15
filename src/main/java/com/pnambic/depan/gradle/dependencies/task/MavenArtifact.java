package com.pnambic.depan.gradle.dependencies.task;

import org.gradle.api.Project;
import org.gradle.api.artifacts.component.ComponentIdentifier;
import org.gradle.api.artifacts.component.ComponentSelector;
import org.gradle.api.artifacts.component.ModuleComponentIdentifier;
import org.gradle.api.artifacts.component.ModuleComponentSelector;
import org.gradle.api.artifacts.component.ProjectComponentIdentifier;
import org.gradle.api.artifacts.component.ProjectComponentSelector;
import org.gradle.api.artifacts.result.ResolvedComponentResult;
import org.gradle.internal.component.local.model.DefaultProjectComponentIdentifier;

import com.google.common.base.Strings;

public class MavenArtifact implements GraphNode {

  private static final String MAVEN_PREFIX = "mvn:";

  private static final String COLON = ":";

  private static final Object JAR_PACKAGING = "jar";

  /**
   * Maven artifact coordinates.
   */
  private final String groupId;
  private final String artifactId;
  private final String version;
  private final String packaging;
  private final String classifier;

  public MavenArtifact(String groupId, String artifactId, String version, String packaging, String classifier) {
    this.groupId = groupId;
    this.artifactId = artifactId;
    this.version = version;
    this.packaging = packaging;
    this.classifier = classifier;
  }

  public static MavenArtifact from(ResolvedComponentResult component, Project project) {
    ComponentIdentifier componentId = component.getId();
    if (componentId instanceof DefaultProjectComponentIdentifier) {
      return new MavenArtifact(
          project.getGroup().toString(), project.getName(), project.getVersion().toString(), null, null);
    }
    return from(componentId);
  }

  public static MavenArtifact from(ComponentIdentifier component) {
    if (component instanceof ModuleComponentIdentifier) {
      ModuleComponentIdentifier module = (ModuleComponentIdentifier) component;
      return new MavenArtifact(module.getGroup(), module.getModule(), module.getVersion(), null, null);
    }
    if (component instanceof ProjectComponentIdentifier) {
      ProjectComponentIdentifier project = (ProjectComponentIdentifier) component;
      return new MavenArtifact(project.getProjectPath(), project.getBuild().getName(), null, null, null);
    }
    throw new IllegalArgumentException("Unknown component identifier type " + component.getClass().getCanonicalName());
  }

  public static GraphNode from(ComponentSelector component) {
    if (component instanceof ModuleComponentSelector) {
      ModuleComponentSelector module = (ModuleComponentSelector) component;
      return new MavenArtifact(module.getGroup(), module.getModule(), module.getVersion(), null, null);
    }
    if (component instanceof ProjectComponentSelector) {
      ProjectComponentSelector project = (ProjectComponentSelector) component;
      return new MavenArtifact(project.getProjectPath(), project.getBuildName(), null, null, null);
    }
    throw new IllegalArgumentException("Unknown component selector type " + component.getClass().getCanonicalName());
  }

  @Override
  public String getId() {
    return MAVEN_PREFIX + getCoordinate();
  }

  @Override
  public String toString() {
    return getId();
  }

  public String getGroupId() {
    return groupId;
  }

  public String getArtifactId() {
    return artifactId;
  }

  public String getVersion() {
    return version;
  }

  public String getPackaging() {
    return packaging;
  }

  public String getClassifier() {
    return classifier;
  }

  private String getCoordinate() {
    StringBuilder result = new StringBuilder();
    result.append(groupId);

    result.append(COLON);
    result.append(artifactId);

    if (null != classifier) {
      result.append(COLON);
      if (!Strings.isNullOrEmpty(packaging)) {
        result.append(packaging);
      }
      result.append(COLON);
      result.append(classifier);
    } else if (hasPackaging()) {
      result.append(COLON);
      if (!Strings.isNullOrEmpty(packaging)) {
        result.append(packaging);
      }
    }

    // Version might be null for "as is" POMs with provided versions.
    if (null != version) {
      result.append(COLON);
      result.append(version);
    }
    return result.toString();
  }

  private boolean hasPackaging() {
    if (Strings.isNullOrEmpty(packaging)) {
      return false;
    }
    if (JAR_PACKAGING.equals(packaging)) {
      return false;
    }
    return true;
  }
}
