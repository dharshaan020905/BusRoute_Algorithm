import java.util.ArrayList;
import java.util.List;

// Declare the graph class
class Graph {
    private int numVertices;
    private List<List<Integer>> adjList;

    public Graph(int numVertices) {
        this.numVertices = numVertices;
        adjList = new ArrayList<>(numVertices);
        for (int i = 0; i < numVertices; i++) {
            adjList.add(new ArrayList<>());
        }
    }

    // Method to add an edge
    public void addEdge(int source, int destination) {
        adjList.get(source).add(destination);
        adjList.get(destination).add(source); // For an undirected graph, add both directions
    }

    // Method to return the adjacent vertices of a vertex
    public List<Integer> getAdjacentVertices(int vertex) {
        return adjList.get(vertex);
    }

    // Method to return the number of vertices
    public int getNumVertices() {
        return numVertices;
    }
}