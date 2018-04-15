package com.pnambic.depan.gradle.dependencies.task;

import java.io.IOException;
import java.io.Writer;

public class GraphModelWriter {

  private static final String GRAPH_EDGE_TAG = "graph-edge";

  private static final String HEAD_TAG = "head";
  private static final String TAIL_TAG = "tail";

  private static final String RELATION_TAG = "relation";
  private static final String CLASS_ATTR = "class";
  private static final String MAVEN_RELATION = "com.google.devtools.depan.maven.graph.MavenRelation";

  private static final String MAVEN_ARTIFACT_TAG = "mvn-artifact";

  private static final String GROUP_ID_TAG = "groupId";

  private static final String ARTIFACT_ID_TAG = "artifactId";

  private static final String VERSION_TAG = "version";

  private static final String PACKAGING_TAG = "packaging";

  private static final String CLASSIFIER_TAG = "classifier";

  private final GenerateDgi out;

  public GraphModelWriter(Writer dst) throws IOException {
    out = new GenerateDgi(dst);
  }

  public void out(GraphModelBuilder builder) throws IOException {
    out.openDgi();
    outGraph(builder);
    out.closeDgi();
  }

  private void outGraph(GraphModelBuilder builder) throws IOException {
    out.openGraph();
    for (GraphNode node : builder.getNodes()) {
      outNode(node);
    }
    for (GraphEdge edge : builder.getEdges()) {
      outEdge(edge);
    }
    out.closeGraph();
  }

  private void outNode(GraphNode node) throws IOException {
    if (node instanceof MavenArtifact) {
      outNode((MavenArtifact) node);
      return;
    }
    throw new IllegalArgumentException("Unknown node type " + node.getClass().getCanonicalName());
  }

  private void outNode(MavenArtifact node) throws IOException {
    out.outText(out.asOpenTag(MAVEN_ARTIFACT_TAG));
    if (null != node.getGroupId()) {
      out.outTagValue(GROUP_ID_TAG, node.getGroupId());
    }
    if (null != node.getArtifactId()) {
      out.outTagValue(ARTIFACT_ID_TAG, node.getArtifactId());
    }
    if (null != node.getVersion()) {
      out.outTagValue(VERSION_TAG, node.getVersion());
    }
    if (null != node.getPackaging()) {
      out.outTagValue(PACKAGING_TAG, node.getPackaging());
    }
    if (null != node.getClassifier()) {
      out.outTagValue(CLASSIFIER_TAG, node.getClassifier());
    }
    out.outText(out.asCloseTag(MAVEN_ARTIFACT_TAG));
   }

  private void outNodeRef(String labelTag, GraphNode node) throws IOException {
    out.outText(out.asOpenTag(labelTag));
    out.outText(node.getId());
    out.outText(out.asCloseTag(labelTag));
  }

  private void outEdge(GraphEdge edge) throws IOException {
    out.outText(out.asOpenTag(GRAPH_EDGE_TAG));
    outRelation(edge.getRelation());
    outNodeRef(HEAD_TAG, edge.getHead());
    outNodeRef(TAIL_TAG, edge.getTail());
    out.outText(out.asCloseTag(GRAPH_EDGE_TAG));
  }

  private void outRelation(String relation) throws IOException {
    out.outText(out.asOpenTagAttr(RELATION_TAG, CLASS_ATTR, MAVEN_RELATION));
    out.outText(relation);
    out.outText(out.asCloseTag(RELATION_TAG));
  }
}
