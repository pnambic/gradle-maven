package com.pnambic.depan.gradle.dependencies;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Dependencies {

  // Common logger for this package
  public static final Logger LOG =
      LoggerFactory.getLogger(Dependencies.class.getPackage().getName());

  private Dependencies() {
    // Prevent instantiation.
  }
}
