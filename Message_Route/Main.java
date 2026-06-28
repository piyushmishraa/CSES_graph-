import java.util.*;
import java.io.*;

public class Main {

    public static List<Integer> bfs(int source, int destination, int n, List<List<Integer>> adj) {
        int[] visited = new int[n + 1];
        int[] parent = new int[n + 1];
        Arrays.fill(parent, -1);

        Queue<Integer> queue = new LinkedList<>();
        queue.add(source);
        visited[source] = 1;

        while (!queue.isEmpty()) {
            int curr = queue.poll();

            if (curr == destination) {
                break;
            }

            for (int neighbor : adj.get(curr)) {
                if (visited[neighbor] != 1) {
                    visited[neighbor] = 1;
                    parent[neighbor] = curr;
                    queue.add(neighbor);
                }
            }
        }

        if (visited[destination] != 1) {
            return null; // unreachable -> IMPOSSIBLE
        }

        List<Integer> path = new ArrayList<>();
        int node = destination;
        while (node != -1) {
            path.add(node);
            node = parent[node];
        }
        Collections.reverse(path);
        return path;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();

        String[] firstInputs = br.readLine().trim().split(" ");
        int n = Integer.parseInt(firstInputs[0]);
        int m = Integer.parseInt(firstInputs[1]);

        int[][] connections = new int[m][2];
        for (int i = 0; i < m; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            connections[i][0] = Integer.parseInt(st.nextToken());
            connections[i][1] = Integer.parseInt(st.nextToken());
        }

        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            adj.add(new ArrayList<>());
        }
        for (int i = 0; i < m; i++) {
            int u = connections[i][0];
            int v = connections[i][1];
            adj.get(u).add(v);
            adj.get(v).add(u);
        }

        List<Integer> result = bfs(1, n, n, adj);

        if (result == null) {
            sb.append("IMPOSSIBLE");
        } else {
            sb.append(result.size()).append("\n");
            for (int node : result) {
                sb.append(node).append(" ");
            }
        }

        System.out.print(sb);
    }
}
