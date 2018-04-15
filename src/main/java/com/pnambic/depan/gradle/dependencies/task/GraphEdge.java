package com.pnambic.depan.gradle.dependencies.task;

public class GraphEdge {
  private final String relation;
  private final GraphNode head;
  private final GraphNode tail;

  public GraphEdge(String relation, GraphNode head, GraphNode tail) {
    this.relation = relation;
    this.head = head;
    this.tail = tail;
  }

  public String getRelation() {
    return relation;
  }

  public GraphNode getHead() {
    return head;
  }

  public GraphNode getTail() {
    return tail;
  }
}
