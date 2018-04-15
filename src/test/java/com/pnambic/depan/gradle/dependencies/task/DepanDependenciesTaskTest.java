package com.pnambic.depan.gradle.dependencies.task;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.testng.annotations.Test;

import com.google.common.io.Files;

public class DepanDependenciesTaskTest {

  @Test(enabled = false)
  public void testSimple() throws IOException {
    File testProject = buildTestProject();
    GradleRunner testRunner = GradleRunner.create()
        .withProjectDir(testProject)
        .withArguments("depan-deps")
        .withPluginClasspath();

    BuildResult result = testRunner.build();
  }

  @Test(enabled = false)
  public void testCmsLegacyData() throws IOException {
    File testProject = new File("../cms-legacy-data");
    GradleRunner testRunner = GradleRunner.create()
        .withProjectDir(testProject)
        .withArguments("depanGraph")
        .withPluginClasspath();

    BuildResult result = testRunner.build();
  }

  private File buildTestProject() throws IOException {
    File testProjectDir = Files.createTempDir();
    File testBuildFile = new File(testProjectDir, "build.gradle");
    writeBuildFile(testBuildFile);
    return testProjectDir;
  }

  private void writeBuildFile(File dst) throws IOException {
    try (Writer write = new BufferedWriter(new FileWriter(dst))) {
      write.write(
          "plugins {"
          + "\n\tid \"com.pnambic.depan.gradle.dependencies.plugin.DepanGradlePlugin\" version \"0.0.1\" apply true"
          + "\n}");
    }
  }
}
