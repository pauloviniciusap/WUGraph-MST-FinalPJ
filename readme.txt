Work Division / Contributions
By: Jake H., Juliana W., Paulo P.
Date: 12/12/25

Juliana Wong:
I completed part one of the project which included:
Vertex Hash Table Integration --> 
- Creating and maintaining a hash table mapping application level vertex objects to internal Vertex nodes
- Ensuring constant-time lookup for all vertex-related operations
Internal Vertex Structure -->
- Completing the internal Vertex class(linked-list node + adjacency list entry)
- Distinguished between the application vertex object(stored as appVertex) and the internal Vertex wrapper node used by WUGraph
Core Graph Operations -->
- Implemented all required public vertex-related methods in WUGraph.java which were: addVertex(Object vertex), removeVertex(Object vertex), isVertex(Object vertex), vertexCount(), and getVertices()
- With correct linked-list updates and hash table synchronization



Jake Hernandez: I was the middle contributor in our group, and my responsibility was implementing the core edge functionality of the WUGraph. This meant building the EdgeNode structure, managing adjacency lists, and making sure the graph correctly handled undirected edges.

Edge + Adjacency List Structure
I built the internal EdgeNode structure, including the following, previous, and partner pointers, and ensured that vertices correctly track their adjacency lists.

addEdge()
I implemented edge insertion using two linked half-edges. This included supporting self-edges, updating degrees, inserting nodes at the front of adjacency lists, and storing edges in the edgeTable for O(1) lookups.

getNeighbors()
I completed the neighbor-retrieval method by creating new arrays of neighbors and weights based on a vertex’s adjacency list.

removeEdge()
I handled edge removal by unlinking both half-edges cleanly, adjusting adjacency pointers, updating degrees, and removing the entry from the edge hash table.

Utility Methods
I finished constant-time implementations of isEdge() and weight() using VertexPair keys to efficiently look up edges.



Paulo Pereira

- Implemented Kruskal’s algorithm in `graphalg.Kruskal`:
  - Wrote `public static WUGraph minSpanTree(WUGraph g)`.
  - Built the MST graph `T` by copying all vertices from `g` without modifying the original graph.
  - Constructed the edge list using `getVertices()` and `getNeighbors()`, avoiding double-counting by only including edges where the source vertex id is less than or equal to the destination id.
  - Built a mapping from vertex objects to integer ids for use with `DisjointSets`.
  - Implemented a custom merge sort on the edge array (no Java built-in sort).
  - Ran standard Kruskal:
    - For each edge `(u, v)` in nondecreasing weight order, added the edge to `T` only if `find(uId) != find(vId)`.
    - Called `union(ru, rv)` only on the roots returned by `find` to keep `DisjointSets` safe.
  - Ensured that `minSpanTree` leaves the original graph unchanged, returns a tree that passes `KruskalTest`, and runs in `O(|V| + |E| log |E|)`.

- Owned the GRADER and final integration:
  - Wrote the `GRADER` file describing the internal data structures, runtime guarantees, and Kruskal/DisjointSets design.
  - Helped run and debug `WUGTest` and `KruskalTest` until both tests passed.

- WUGraph fixes and debugging:
  - Helped refine `graph.WUGraph` so that `addEdge`, `removeEdge`, `removeVertex`, `degree`, `edgeCount`, and `getNeighbors` all behave correctly, including self-edges.
  - Verified that the final `WUGraph` implementation satisfies the required asymptotic bounds and passes `WUGTest`.
