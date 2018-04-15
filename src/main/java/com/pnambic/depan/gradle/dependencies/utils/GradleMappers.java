package com.pnambic.depan.gradle.dependencies.utils;

import org.gradle.api.Project;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.artifacts.component.ComponentIdentifier;
import org.gradle.api.artifacts.component.ComponentSelector;
import org.gradle.api.artifacts.component.ModuleComponentIdentifier;
import org.gradle.api.artifacts.component.ModuleComponentSelector;
import org.gradle.api.artifacts.component.ProjectComponentIdentifier;
import org.gradle.api.artifacts.component.ProjectComponentSelector;
import org.gradle.api.artifacts.result.ResolvedComponentResult;
import org.gradle.internal.component.local.model.DefaultProjectComponentIdentifier;

public class GradleMappers {

  public final static char COORDINATE_SEPARATOR = ':';

  private GradleMappers() {
    // Prevent instantiation.
  }

  public static String toCoordinate(ResolvedComponentResult component, Project project) {
    ComponentIdentifier componentId = component.getId();
    if (componentId instanceof DefaultProjectComponentIdentifier) {
      StringBuilder result = new StringBuilder((String) project.getGroup());
      result.append(COORDINATE_SEPARATOR);
      result.append(project.getName());
      result.append(COORDINATE_SEPARATOR);
      result.append(project.getVersion());
      return result.toString();
    }
    return toCoordinate(componentId);
  }

  public static String toCoordinate(ComponentIdentifier component) {
    if (component instanceof ModuleComponentIdentifier) {
      ModuleComponentIdentifier module = (ModuleComponentIdentifier) component;
      StringBuilder result = new StringBuilder(module.getGroup());
      result.append(COORDINATE_SEPARATOR);
      result.append(module.getModule());
      result.append(COORDINATE_SEPARATOR);
      result.append(module.getVersion());
      return result.toString();
    }
    if (component instanceof ProjectComponentIdentifier) {
      ProjectComponentIdentifier project = (ProjectComponentIdentifier) component;
      StringBuilder result = new StringBuilder(project.getProjectPath());
      result.append(COORDINATE_SEPARATOR);
      result.append(project.getBuild().getName());
      result.append(COORDINATE_SEPARATOR);
      result.append(project.getBuild().getName());
      return result.toString();
    }
    throw new IllegalArgumentException("Unknown component identifier type " + component.getClass().getCanonicalName());
  }

  public static String toCoordinate(ComponentSelector component) {
    if (component instanceof ModuleComponentSelector) {
      ModuleComponentSelector module = (ModuleComponentSelector) component;
      StringBuilder result = new StringBuilder(module.getGroup());
      result.append(COORDINATE_SEPARATOR);
      result.append(module.getModule());
      result.append(COORDINATE_SEPARATOR);
      result.append(module.getVersion());
      return result.toString();
    }
    if (component instanceof ProjectComponentSelector) {
      ProjectComponentSelector project = (ProjectComponentSelector) component;
      StringBuilder result = new StringBuilder(project.getProjectPath());
      result.append(COORDINATE_SEPARATOR);
      result.append(project.getBuildName());
      return result.toString();
    }
    throw new IllegalArgumentException("Unknown component selector type " + component.getClass().getCanonicalName());
  }

  public static String toCoordinate(Dependency dep) {
    StringBuilder result = new StringBuilder(dep.getGroup());
    result.append(COORDINATE_SEPARATOR);
    result.append(dep.getName());
    result.append(COORDINATE_SEPARATOR);
    result.append(dep.getVersion());
    return result.toString();
  }
}
