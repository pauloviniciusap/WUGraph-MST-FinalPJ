/* WUGraph.java */

package graph;
import java.util.HashMap;

/**
 * The WUGraph class represents a weighted, undirected graph.  Self-edges are
 * permitted.
 */

public class WUGraph {
  
  private int edgeCount; //Count (USE FOR LATER!)
  private int vertexCount; // Amount of vertices
  
  private HashMap<Object, Vertex> vertexTable; //maps Object
  
  private Vertex vertexHead; // Head of List
  private Vertex vertexTail; // Tail of List

  // Vertex Representation
  private class Vertex {
    int degree; //number of edges
    Vertex previous;
    Vertex next;
    Object appVertex; //vertex object
    EdgeNode nextList; // adjacent list (USE FOR LATER!)

    Vertex(Object v){
      appVertex = v;
      degree = 0;
      nextList = null;
      next = null;
      previous = null;
    }
  }

  private class EdgeNode{
    Object neighbor; //the neighbor the edge is next to
    int weight; //The weight
    EdgeNode previous; //links adjacent to the list
    EdgeNode next;
    EdgeNode partner; // edge in the vertex's list

    EdgeNode(Object n, int w){
      neighbor = n;
      weight = w;
    }
  }
  private HashMap<VertexPair, EdgeNode> edgeTable; //stores the pairs (u,v)/(v,u)

  // The WUGraph is initialized and has a runtime of O(1)
  public WUGraph(){ //Makes an empty graph
      vertexTable = new HashMap<>();
      edgeTable = new HashMap<>();
      vertexHead = null;
      vertexTail = null;
      vertexCount = 0;
      edgeCount = 0;
  }
  public int vertexCount(){
    return vertexCount; // returns the number of vertices in the graph
  }


   //edgeCount() returns the total number of edges in the graph.
  public int edgeCount(){
    return edgeCount;
  }

  /**
   * getVertices() returns an array containing all the objects that serve
   * as vertices of the graph.  The array's length is exactly equal to the
   * number of vertices.  If the graph has no vertices, the array has length
   * zero.
   *
   * (NOTE:  Do not return any internal data structure you use to represent
   * vertices!  Return only the same objects that were provided by the
   * calling application in calls to addVertex().)
   *
   * Running time:  O(|V|).
   */
  public Object[] getVertices(){
    Object[] array = new Object[vertexCount];
    int i = 0;
    Vertex current = vertexHead;

    while(current != null){
      array[i] = current.appVertex;
      current = current.next;
      i++;
    }
    return array;
  }
    
  /**
   * addVertex() adds a vertex (with no incident edges) to the graph.
   * The vertex's "name" is the object provided as the parameter "vertex".
   * If this object is already a vertex of the graph, the graph is unchanged.
   *
   * Running time:  O(1).
   */
  public void addVertex(Object vertex) {
    if (isVertex(vertex)) {
		  return;
	  }
	  
	  Vertex newV = new Vertex(vertex);
	  
	  newV.next = vertexHead;
	  if (vertexHead != null) {
		  vertexHead.previous = newV;
	  }
	  vertexHead = newV;
	  
	  vertexTable.put(vertex, newV); 
	  
	  vertexCount++;
  }

  /**
   * removeVertex() removes a vertex from the graph.  All edges incident on the
   * deleted vertex are removed as well.  If the parameter "vertex" does not
   * represent a vertex of the graph, the graph is unchanged.
   *
   * Running time:  O(d), where d is the degree of "vertex".
   */
  public void removeVertex(Object vertex) {
    Vertex e = vertexTable.get(vertex);
	  if (e == null) {
		  return;
	  }
	  
	  //Remove edge incidents here
	  
	  //Removal from linked list
	  if (e.previous != null) {
		  e.previous.next = e.next;
	  }
	  else {
		  vertexHead = e.next;
	  }
	  
	  if (e.next != null) {
		  e.next.previous = e.previous;
	  }
	  
	  //Removal from hash table
	  vertexTable.remove(vertex);
	  
	  vertexCount--;
  }

  /**
   * isVertex() returns true if the parameter "vertex" represents a vertex of
   * the graph.
   *
   * Running time:  O(1).
   */
  public boolean isVertex(Object vertex) {
    return vertexTable.containsKey(vertex);
  }

  /**
   * degree() returns the degree of a vertex.  Self-edges add only one to the
   * degree of a vertex.  If the parameter "vertex" doesn't represent a vertex
   * of the graph, zero is returned.
   *
   * Running time:  O(1).
   */
  public int degree(Object vertex) {
    Vertex e = vertexTable.get(vertex);
	  if (e == null) {
		  return 0;
	  }
	  return e.degree;
  }

  /**
   * getNeighbors() returns a new Neighbors object referencing two arrays.  The
   * Neighbors.neighborList array contains each object that is connected to the
   * input object by an edge.  The Neighbors.weightList array contains the
   * weights of the corresponding edges.  The length of both arrays is equal to
   * the number of edges incident on the input vertex.  If the vertex has
   * degree zero, or if the parameter "vertex" does not represent a vertex of
   * the graph, null is returned (instead of a Neighbors object).
   *
   * The returned Neighbors object, and the two arrays, are both newly created.
   * No previously existing Neighbors object or array is changed.
   *
   * (NOTE:  In the neighborList array, do not return any internal data
   * structure you use to represent vertices!  Return only the same objects
   * that were provided by the calling application in calls to addVertex().)
   *
   * Running time:  O(d), where d is the degree of "vertex".
   */
  public Neighbors getNeighbors(Object vertex){
	  return null;
  }

  /**
   * addEdge() adds an edge (u, v) to the graph.  If either of the parameters
   * u and v does not represent a vertex of the graph, the graph is unchanged.
   * The edge is assigned a weight of "weight".  If the graph already contains
   * edge (u, v), the weight is updated to reflect the new value.  Self-edges
   * (where u.equals(v)) are allowed.
   *
   * Running time:  O(1).
   */
  public void addEdge(Object u, Object v, int weight){

  }

  /**
   * removeEdge() removes an edge (u, v) from the graph.  If either of the
   * parameters u and v does not represent a vertex of the graph, the graph
   * is unchanged.  If (u, v) is not an edge of the graph, the graph is
   * unchanged.
   *
   * Running time:  O(1).
   */
  public void removeEdge(Object u, Object v){

  }

  /**
   * isEdge() returns true if (u, v) is an edge of the graph.  Returns false
   * if (u, v) is not an edge (including the case where either of the
   * parameters u and v does not represent a vertex of the graph).
   *
   * Running time:  O(1).
   */
  public boolean isEdge(Object u, Object v){
	  if(!isVertex(u) || !isVertex(v)){
		return false;
	  }
	  //Because VertexPair is unordered, (u, v) and (v, u) are identical keys.
	  VertexPair uv = new VertexPair(u, v);
	  return edgeTable.containsKey(uv);
  }
  /**
   * weight() returns the weight of (u, v).  Returns zero if (u, v) is not
   * an edge (including the case where either of the parameters u and v does
   * not represent a vertex of the graph).
   *
   * (NOTE:  A well-behaved application should try to avoid calling this
   * method for an edge that is not in the graph, and should certainly not
   * treat the result as if it actually represents an edge with weight zero.
   * However, some sort of default response is necessary for missing edges,
   * so we return zero.  An exception would be more appropriate, but also more
   * annoying.)
   *
   * Running time:  O(1).
   */
	// If no edge exists, then it will return 0
  public int weight(Object u, Object v){
	  if(!isVertex(u) || !isVertex(v)){
	  	return 0;
		}
	  VertexPair uv = new VertexPair(u, v);
	  EdgeNode edge = edgeTable.get(uv);

	  if (edge == null){
		  return 0;
	  }
	  return edge.weight;
}
