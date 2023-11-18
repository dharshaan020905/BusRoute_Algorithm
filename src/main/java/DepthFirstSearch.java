import java.util.*;

// Declare the DFS algorithm
public class DepthFirstSearch {
    private int[] num;
    private List<Integer> path;
    private boolean found;

    public DepthFirstSearch(int numVertices) {
        num = new int[numVertices];
        path = new ArrayList<>();
        found = false;
    }

    public List<Integer> findPathDFS(Graph graph, int source, int destination) {
        for (int v = 0; v < graph.getNumVertices(); v++) {
            num[v] = 0;
        }
        path.clear();
        found = false;

        if (source == destination) {
            return path; // Return an empty path, as source and destination are the same
        }

        DFS(graph, source, destination);

        if (found) {
            return path; // Return the path from source to destination
        } else {
            return null; // No path found from source to destination
        }
    }

    private void DFS(Graph graph, int v, int d) {
        num[v] = 1;
        path.add(v);

        if (v == d) {
            found = true;
            return;
        }

        for (int u : graph.getAdjacentVertices(v)) {
            if (num[u] == 0) {
                DFS(graph, u, d);
                if (found) {
                    return;
                }
            }
        }
        path.remove(path.size() - 1);
    }
}