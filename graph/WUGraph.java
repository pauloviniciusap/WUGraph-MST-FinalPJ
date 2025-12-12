/* WUGraph.java */

package graph;

import java.util.HashMap;

/**
 * The WUGraph class represents a weighted, undirected graph.  Self-edges are
 * permitted.
 *
 * This implementation uses:
 *  - a HashMap<Object,Vertex> to map vertex objects to internal records
 *  - a doubly-linked list of all vertices for getVertices()
 *  - for each vertex, a doubly-linked adjacency list of EdgeNode objects
 *  - a HashMap<VertexPair,EdgeNode> to find edges in O(1)
 */
public class WUGraph {

  /** Number of vertices. */
  private int vertexCount;

  /** Number of undirected edges (self-edges count once). */
  private int edgeCount;

  /** Map from application vertex object to internal Vertex. */
  private HashMap<Object, Vertex> vertexTable;

  /** Head and tail of global vertex list. */
  private Vertex vertexHead;
  private Vertex vertexTail;

  /** Map from undirected pair (u,v) to one EdgeNode of that edge. */
  private HashMap<VertexPair, EdgeNode> edgeTable;

  /** Internal vertex record. */
  private class Vertex {
    Object appVertex;   // user vertex object
    int degree;         // number of incident edges; self-edge adds 1
    Vertex prev;        // previous in global list
    Vertex next;        // next in global list
    EdgeNode adj;       // head of adjacency list

    Vertex(Object v) {
      appVertex = v;
      degree = 0;
      prev = null;
      next = null;
      adj = null;
    }
  }

  /**
   * Internal adjacency node.
   *
   * For a non-self edge (u,v) there are TWO EdgeNode objects:
   *  - one in u's adjacency list with neighbor=v
   *  - one in v's adjacency list with neighbor=u
   * They point at each other via partner.
   *
   * For a self-edge (u,u) there is ONE EdgeNode in u's adjacency list and
   * partner points to itself.
   */
  private class EdgeNode {
    Object neighbor;
    int weight;
    EdgeNode prev;      // previous in this vertex's adjacency list
    EdgeNode next;      // next in this vertex's adjacency list
    EdgeNode partner;   // the corresponding half-edge, or self for self-edge

    EdgeNode(Object n, int w) {
      neighbor = n;
      weight = w;
      prev = null;
      next = null;
      partner = null;
    }
  }

  /**
   * Construct an empty graph.
   */
  public WUGraph() {
    vertexTable = new HashMap<Object, Vertex>();
    edgeTable = new HashMap<VertexPair, EdgeNode>();
    vertexHead = null;
    vertexTail = null;
    vertexCount = 0;
    edgeCount = 0;
  }

  /**
   * Returns the number of vertices.
   */
  public int vertexCount() {
    return vertexCount;
  }

  /**
   * Returns the number of edges (self-edges count once).
   */
  public int edgeCount() {
    return edgeCount;
  }

  /**
   * getVertices() returns all vertex objects as an array.
   *
   * Running time: O(|V|).
   */
  public Object[] getVertices() {
    Object[] verts = new Object[vertexCount];
    int i = 0;
    Vertex cur = vertexHead;
    while (cur != null) {
      verts[i++] = cur.appVertex;
      cur = cur.next;
    }
    return verts;
  }

  /**
   * addVertex() adds a vertex (with no incident edges).  If already present,
   * do nothing.
   *
   * Running time: O(1).
   */
  public void addVertex(Object vertex) {
    if (isVertex(vertex)) {
      return;
    }
    Vertex v = new Vertex(vertex);

    // Insert at head of global vertex list.
    v.next = vertexHead;
    if (vertexHead != null) {
      vertexHead.prev = v;
    } else {
      vertexTail = v;
    }
    vertexHead = v;

    vertexTable.put(vertex, v);
    vertexCount++;
  }

  /**
   * removeVertex() deletes a vertex and all incident edges.
   *
   * Running time: O(d) where d is the degree.
   */
  public void removeVertex(Object vertex) {
    Vertex v = vertexTable.get(vertex);
    if (v == null) {
      return;
    }

    // Remove all incident edges.
    EdgeNode e = v.adj;
    while (e != null) {
      EdgeNode nextEdge = e.next;  // save before we unlink

      Object neighObj = e.neighbor;
      VertexPair key = new VertexPair(vertex, neighObj);
      EdgeNode partner = e.partner;

      if (neighObj.equals(vertex)) {
        // Self-edge: only v involved.
        unlinkEdgeFromAdjacency(v, e);
        v.degree--;
        edgeCount--;
      } else {
        // Regular edge: v and some neighbor w.
        Vertex w = vertexTable.get(neighObj);
        if (w != null && partner != null) {
          unlinkEdgeFromAdjacency(w, partner);
          w.degree--;
        }
        unlinkEdgeFromAdjacency(v, e);
        v.degree--;
        edgeCount--;
      }

      edgeTable.remove(key);

      e = nextEdge;
    }

    // Remove v itself from global vertex list.
    if (v.prev != null) {
      v.prev.next = v.next;
    } else {
      vertexHead = v.next;
    }
    if (v.next != null) {
      v.next.prev = v.prev;
    } else {
      vertexTail = v.prev;
    }

    vertexTable.remove(vertex);
    vertexCount--;
  }

  /**
   * Helper to unlink one EdgeNode from its owner's adjacency list.
   * Does not touch degree, edgeCount, or edgeTable.
   */
  private void unlinkEdgeFromAdjacency(Vertex owner, EdgeNode e) {
    if (e.prev != null) {
      e.prev.next = e.next;
    } else {
      owner.adj = e.next;
    }
    if (e.next != null) {
      e.next.prev = e.prev;
    }
  }

  /**
   * isVertex() returns true if vertex is in the graph.
   *
   * Running time: O(1).
   */
  public boolean isVertex(Object vertex) {
    return vertexTable.containsKey(vertex);
  }

  /**
   * degree() returns degree of vertex, self-edge counts as 1.
   * Returns 0 if not a vertex.
   *
   * Running time: O(1).
   */
  public int degree(Object vertex) {
    Vertex v = vertexTable.get(vertex);
    if (v == null) {
      return 0;
    }
    return v.degree;
  }

  /**
   * getNeighbors() returns a Neighbors object for this vertex, or null
   * if the vertex does not exist or has degree 0.
   *
   * Running time: O(d).
   */
  public Neighbors getNeighbors(Object vertex) {
    Vertex v = vertexTable.get(vertex);
    if (v == null || v.degree == 0) {
      return null;
    }

    Neighbors neigh = new Neighbors();
    neigh.neighborList = new Object[v.degree];
    neigh.weightList = new int[v.degree];

    EdgeNode cur = v.adj;
    int i = 0;
    while (cur != null && i < v.degree) {
      neigh.neighborList[i] = cur.neighbor;
      neigh.weightList[i] = cur.weight;
      cur = cur.next;
      i++;
    }
    return neigh;
  }

  /**
   * addEdge() adds or updates an edge (u,v) with given weight.
   * Self-edges (u,u) are allowed.
   *
   * Running time: O(1).
   */
  public void addEdge(Object u, Object v, int weight) {
    if (!isVertex(u) || !isVertex(v)) {
      return;
    }

    VertexPair key = new VertexPair(u, v);
    EdgeNode existing = edgeTable.get(key);

    if (existing != null) {
      // Edge already exists - just update weight on both halves.
      existing.weight = weight;
      if (existing.partner != null && existing.partner != existing) {
        existing.partner.weight = weight;
      }
      return;
    }

    Vertex U = vertexTable.get(u);
    Vertex V = vertexTable.get(v);

    if (u.equals(v)) {
      // Self-edge: single EdgeNode in U's list.
      EdgeNode e = new EdgeNode(u, weight);
      e.partner = e;

      // Insert at head of U.adj.
      e.next = U.adj;
      if (U.adj != null) {
        U.adj.prev = e;
      }
      U.adj = e;

      U.degree++;
      edgeCount++;
      edgeTable.put(key, e);
    } else {
      // Regular edge: create two half-edges.
      EdgeNode eUV = new EdgeNode(v, weight);
      EdgeNode eVU = new EdgeNode(u, weight);

      eUV.partner = eVU;
      eVU.partner = eUV;

      // Insert at head of U.adj.
      eUV.next = U.adj;
      if (U.adj != null) {
        U.adj.prev = eUV;
      }
      U.adj = eUV;

      // Insert at head of V.adj.
      eVU.next = V.adj;
      if (V.adj != null) {
        V.adj.prev = eVU;
      }
      V.adj = eVU;

      U.degree++;
      V.degree++;
      edgeCount++;
      edgeTable.put(key, eUV);
    }
  }

  /**
   * removeEdge() removes edge (u,v) if it exists.
   *
   * Running time: O(1).
   */
  public void removeEdge(Object u, Object v) {
    if (!isVertex(u) || !isVertex(v)) {
      return;
    }

    VertexPair key = new VertexPair(u, v);
    EdgeNode e = edgeTable.get(key);
    if (e == null) {
      return;
    }

    Vertex U = vertexTable.get(u);
    Vertex V = vertexTable.get(v);
    EdgeNode partner = e.partner;

    edgeTable.remove(key);

    if (u.equals(v)) {
      // Self-edge.
      unlinkEdgeFromAdjacency(U, e);
      U.degree--;
      edgeCount--;
    } else {
      // Regular edge.
      unlinkEdgeFromAdjacency(U, e);
      if (V != null && partner != null) {
        unlinkEdgeFromAdjacency(V, partner);
      }
      U.degree--;
      if (V != null) {
        V.degree--;
      }
      edgeCount--;
    }
  }

  /**
   * isEdge() returns true if (u,v) is an edge.
   * Returns false if either is not a vertex or edge does not exist.
   *
   * Running time: O(1).
   */
  public boolean isEdge(Object u, Object v) {
    if (!isVertex(u) || !isVertex(v)) {
      return false;
    }
    VertexPair key = new VertexPair(u, v);
    return edgeTable.containsKey(key);
  }

  /**
   * weight() returns weight of (u,v) or 0 if no such edge.
   *
   * Running time: O(1).
   */
  public int weight(Object u, Object v) {
    if (!isVertex(u) || !isVertex(v)) {
      return 0;
    }
    VertexPair key = new VertexPair(u, v);
    EdgeNode e = edgeTable.get(key);
    if (e == null) {
      return 0;
    }
    return e.weight;
  }
}
