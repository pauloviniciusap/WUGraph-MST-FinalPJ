









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
