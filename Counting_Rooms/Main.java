import java.util.*;
import java.io.*;

public class Main {
    static int[][] direction = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } };

    static int n, m;

    public static void dfs(char[][] grid, int x, int y) {

        grid[x][y] = '*';
        Deque<int[]> stack = new ArrayDeque<>();
        stack.push(new int[] { x, y });

        while (!stack.isEmpty()) {
            int[] temp = stack.pop();
            int sx = temp[0];
            int sy = temp[1];
            for (int i = 0; i < direction.length; i++) {
                int newx = sx + direction[i][0];
                int newy = sy + direction[i][1];
                if (newx >= 0 && newx < n && newy >= 0 && newy < m && grid[newx][newy] == '.') {
                    grid[newx][newy] = '*';
                    stack.push(new int[] { newx, newy });
                }
            }
        }

    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();

        // reading inputs
        String[] inputs1 = br.readLine().trim().split(" ");

        n = Integer.parseInt(inputs1[0]);
        m = Integer.parseInt(inputs1[1]);
        char[][] grid = new char[n][m];
        for (int i = 0; i < n; i++) {
            String row = br.readLine();
            for (int j = 0; j < m; j++) {
                grid[i][j] = row.charAt(j);
            }
        }
        int count = 0;
        // so now we have our grid and we just need to apply dfs
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == '.') {
                    dfs(grid, i, j);
                    count++;

                }
            }
        }
        sb.append(count).append("\n");
        System.out.print(sb);
    }
}
