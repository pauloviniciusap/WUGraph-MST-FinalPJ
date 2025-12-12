/* Kruskal.java */

package graphalg;

import graph.*;
import set.*;
import java.util.HashMap;

/**
 * The Kruskal class contains the method minSpanTree(), which implements
 * Kruskal's algorithm for computing a minimum spanning tree of a graph.
 */
public class Kruskal {

  /**
   * Simple edge representation for Kruskal. This is separate from any
   * internal edge representation inside WUGraph to preserve encapsulation.
   */
  private static class Edge {
    Object u;
    Object v;
    int weight;

    Edge(Object u, Object v, int weight) {
      this.u = u;
      this.v = v;
      this.weight = weight;
    }
  }

  /**
   * minSpanTree() returns a WUGraph that represents the minimum spanning tree
   * of the WUGraph g. The original WUGraph g is NOT changed.
   *
   * Running time: O(|V| + |E| log |E|).
   *
   * @param g The weighted, undirected graph whose MST we want to compute.
   * @return A newly constructed WUGraph representing the MST of g.
   */
  public static WUGraph minSpanTree(WUGraph g) {
    // 1. Get all vertices from g and create an empty MST graph T with the
    //    same vertex set and no edges.
    Object[] vertices = g.getVertices();
    int n = vertices.length;

    WUGraph T = new WUGraph();
    for (int i = 0; i < n; i++) {
      T.addVertex(vertices[i]);
    }

    // Edge case - no vertices or no edges, return T as is.
    if (n == 0 || g.edgeCount() == 0) {
      return T;
    }

    // 2. Build a mapping from vertex Object to integer id (0..n-1)
    HashMap<Object, Integer> vertexToId = new HashMap<Object, Integer>();
    for (int i = 0; i < n; i++) {
      vertexToId.put(vertices[i], Integer.valueOf(i));
    }

    // 3. Build a list of all edges in g without double counting.
    //    We rely on g.edgeCount() to know how many undirected edges exist.
    int m = g.edgeCount();
    Edge[] edges = new Edge[m];
    int edgeIndex = 0;

    for (int i = 0; i < n; i++) {
      Object u = vertices[i];
      Neighbors neigh = g.getNeighbors(u);
      if (neigh == null) {
        continue;  // isolated vertex
      }

      Object[] neighborList = neigh.neighborList;
      int[] weightList = neigh.weightList;

      for (int k = 0; k < neighborList.length; k++) {
        Object v = neighborList[k];
        int j = vertexToId.get(v).intValue();

        // Avoid double counting: only record edges for which id(u) <= id(v).
        if (i <= j) {
          edges[edgeIndex++] = new Edge(u, v, weightList[k]);
        }
      }
    }

    // In case something is off and we collected fewer edges than g.edgeCount(),
    // shrink the array to the actual number collected.
    if (edgeIndex < m) {
      Edge[] trimmed = new Edge[edgeIndex];
      for (int i = 0; i < edgeIndex; i++) {
        trimmed[i] = edges[i];
      }
      edges = trimmed;
      m = edgeIndex;
    }

    // 4. Sort edges by weight using our own sort - no Java built in sort.
    mergeSort(edges, 0, m - 1);

    // 5. Run Kruskal using DisjointSets.
    DisjointSets sets = new DisjointSets(n);

    for (int i = 0; i < m; i++) {
      Edge e = edges[i];
      int uId = vertexToId.get(e.u).intValue();
      int vId = vertexToId.get(e.v).intValue();

      int rootU = sets.find(uId);
      int rootV = sets.find(vId);

      // Only add edge if it connects two different components.
      if (rootU != rootV) {
        T.addEdge(e.u, e.v, e.weight);
        // Always union by roots to keep DisjointSets happy.
        sets.union(rootU, rootV);
      }
    }

    return T;
  }

  /**
   * mergeSort() sorts the edges array in place by nondecreasing weight.
   *
   * Running time: O(m log m) where m is edges.length.
   */
  private static void mergeSort(Edge[] edges, int left, int right) {
    if (left >= right) {
      return;
    }

    int mid = (left + right) / 2;
    mergeSort(edges, left, mid);
    mergeSort(edges, mid + 1, right);
    merge(edges, left, mid, right);
  }

  /**
   * merge() is the helper for mergeSort().
   */
  private static void merge(Edge[] edges, int left, int mid, int right) {
    int n1 = mid - left + 1;
    int n2 = right - mid;

    Edge[] leftArr = new Edge[n1];
    Edge[] rightArr = new Edge[n2];

    for (int i = 0; i < n1; i++) {
      leftArr[i] = edges[left + i];
    }
    for (int j = 0; j < n2; j++) {
      rightArr[j] = edges[mid + 1 + j];
    }

    int i = 0;
    int j = 0;
    int k = left;

    while (i < n1 && j < n2) {
      if (leftArr[i].weight <= rightArr[j].weight) {
        edges[k] = leftArr[i];
        i++;
      } else {
        edges[k] = rightArr[j];
        j++;
      }
      k++;
    }

    while (i < n1) {
      edges[k] = leftArr[i];
      i++;
      k++;
    }

    while (j < n2) {
      edges[k] = rightArr[j];
      j++;
      k++;
    }
  }
}
