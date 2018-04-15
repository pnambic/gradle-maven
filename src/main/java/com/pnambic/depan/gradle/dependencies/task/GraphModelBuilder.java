package com.pnambic.depan.gradle.dependencies.task;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class GraphModelBuilder {

  private final Map<String, GraphNode> nodes = Maps.newHashMap();

  private final Set<GraphEdge> edges = Sets.newHashSet();

  @SuppressWarnings("serial")
  public static class DuplicateNodeException
      extends IllegalArgumentException {

    public DuplicateNodeException(String nodeId) {
      super("duplicate node id " + nodeId);
    }
  }

  public GraphEdge addEdge(GraphEdge edge) {
    edges.add(edge);
    return edge;
  }

  public GraphNode findNode(String id) {
    return nodes.get(id);
  }

  public GraphNode newNode(GraphNode node) {
    if (null != findNode(node.getId())) {
      throw new DuplicateNodeException(node.getId().toString());
    }

    nodes.put(node.getId(), node);
    return node;
  }

  public GraphNode mapNode(GraphNode mapNode) {
    GraphNode result = findNode(mapNode.getId());
    if (null != result) {
      return result;
    }

    // Yes, findNode() is called twice if this path is taken.
    // A missing node should be a cheap lookup, and it ensures no
    // duplicates.
    return newNode(mapNode);
  }

  public Collection<GraphNode> getNodes() {
    return nodes.values();
  }

  public Collection<GraphEdge> getEdges() {
    return edges;
  }
}
