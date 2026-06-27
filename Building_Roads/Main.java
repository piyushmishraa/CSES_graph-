import java.util.*;
import java.io.*;

public class Main {
    public static void dfs(HashMap<Integer, List<Integer>> adj, int source, int[] visited) {
        visited[source] = 1;

        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(source);
        while (!stack.isEmpty()) {
            int cur = stack.pop();
            List<Integer> neighbours = adj.getOrDefault(cur, new ArrayList<>());
            for (int neigh : neighbours) {
                if (visited[neigh] != 1) {
                    visited[neigh] = 1;
                    stack.push(neigh);
                }

            }
        }

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
        // now we have our connections [][] in which connection[i] represent [ai,bi]
        // which means there is a connections between a and b
        // we should now build or adj list
        HashMap<Integer, List<Integer>> adj = new HashMap<>();
        for (int i = 0; i < connections.length; i++) {
            int u = connections[i][0];
            int v = connections[i][1];
            adj.putIfAbsent(u, new ArrayList<>());
            adj.putIfAbsent(v, new ArrayList<>());
            adj.get(u).add(v);
            adj.get(v).add(u);
        }

        int[] visited = new int[n + 1];
        ArrayList<Integer> reps = new ArrayList<>();
        for (int city = 1; city <= n; city++) {
            if (visited[city] != 1) {
                dfs(adj, city, visited);
                reps.add(city);
            }
        }
        int firstOutput = reps.size() - 1;

        sb.append(firstOutput).append("\n");
        for (int i = 0; i < reps.size() - 1; i++) {
            sb.append(reps.get(i)).append(" ").append(reps.get(i + 1)).append("\n");
        }
        System.out.print(sb);
    }
}
