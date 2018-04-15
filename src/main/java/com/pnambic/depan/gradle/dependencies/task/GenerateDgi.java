package com.pnambic.depan.gradle.dependencies.task;

import java.io.IOException;
import java.io.Writer;

public class GenerateDgi {

  // XML characters.
  private static final String TAG_OPEN = "<";
  private static final String TAG_CLOSE = ">";
  private static final String TAG_FINAL = "/";

  private static final Object ATTR_SEPARATOR = " ";
  private static final Object ATTR_ASSIGN = "=";
  private static final Object VALUE_QUOTE = "\"";

  private static final String XML_HEADER = "<?xml version=\"1.0\"?>";

  private static final String GRAPH_INFO_TAG = "graph-info";

  private static final String GRAPH_TAG = "graph";

  private static final String GRAPH_MODEL_ELEMENT =
      "<graphModel>"
      + "<nodeContribIds></nodeContribIds>"
      + "<relationContribIds>"
      + "<string>com.google.devtools.depan.maven.MavenRelationContributor</string>"
      + "<string>com.google.devtools.depan.filesystem.FileSystemRelationContributor</string>"
      + "</relationContribIds>"
      +"</graphModel>";

  private final Writer dst;

  public GenerateDgi(Writer dst) {
    this.dst = dst;
  }

  public void openDgi() throws IOException {
    dst.write(XML_HEADER);
    dst.write(asOpenTag(GRAPH_INFO_TAG));
    dst.write(GRAPH_MODEL_ELEMENT);
  }

  public void closeDgi() throws IOException {
    dst.write(asCloseTag(GRAPH_INFO_TAG));
  }

  public void openGraph() throws IOException {
    dst.write(asOpenTag(GRAPH_TAG));
  }

  public void closeGraph() throws IOException {
    dst.write(asCloseTag(GRAPH_TAG));
  }

  public void outText(String text) throws IOException {
    dst.write(text);
  }

  public void outTagValue(String tag, String value) throws IOException {
    outText(asTagValue(tag, value));
  }

  public String asOpenTag(String tagName) {
    StringBuilder result = new StringBuilder(TAG_OPEN);
    result.append(tagName);
    result.append(TAG_CLOSE);
    return result.toString();
  }

  public String asClosedTag(String tagName) {
    StringBuilder result = new StringBuilder(TAG_OPEN);
    result.append(tagName);
    result.append(TAG_FINAL);
    result.append(TAG_CLOSE);
    return result.toString();
  }

  public String asCloseTag(String tagName) {
    StringBuilder result = new StringBuilder(TAG_OPEN);
    result.append(TAG_FINAL);
    result.append(tagName);
    result.append(TAG_CLOSE);
    return result.toString();
  }

  public String asOpenTagAttr(String tagName, String attrName, String attrValue) {
    StringBuilder result = new StringBuilder(TAG_OPEN);
    result.append(tagName);
    result.append(ATTR_SEPARATOR);
    result.append(attrName);
    result.append(ATTR_ASSIGN);
    result.append(VALUE_QUOTE);
    result.append(attrValue);
    result.append(VALUE_QUOTE);
    result.append(TAG_CLOSE);
    return result.toString();
  }

  public String asTagValue(String tag, String value) {
    StringBuilder result = new StringBuilder();
    result.append(asOpenTag(tag));
    result.append(value);
    result.append(asCloseTag(tag));
    return result.toString();
  }
}
