import java.util.*;
import java.io.*;

public class Main {

    static class Pair {
        int r, c;

        Pair(int r, int c) {
            this.r = r;
            this.c = c;
        }
    }

    static int[][] directions = {
            { 0, 1 }, // R
            { 0, -1 }, // L
            { -1, 0 }, // U
            { 1, 0 } // D
    };

    static char[] dirChar = { 'R', 'L', 'U', 'D' };

    public static void main(String[] args) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String[] first = br.readLine().split(" ");
        int n = Integer.parseInt(first[0]);
        int m = Integer.parseInt(first[1]);

        char[][] labyrinth = new char[n][m];

        int Arow = -1, Acol = -1;
        int Brow = -1, Bcol = -1;

        for (int i = 0; i < n; i++) {
            labyrinth[i] = br.readLine().toCharArray();

            for (int j = 0; j < m; j++) {
                if (labyrinth[i][j] == 'A') {
                    Arow = i;
                    Acol = j;
                } else if (labyrinth[i][j] == 'B') {
                    Brow = i;
                    Bcol = j;
                }
            }
        }

        Queue<Pair> q = new LinkedList<>();

        List<List<Integer>> arrive = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            arrive.add(new ArrayList<>());
            for (int j = 0; j < m; j++) {
                arrive.get(i).add(-1);
            }
        }

        q.add(new Pair(Arow, Acol));
        arrive.get(Arow).set(Acol, 4); // mark source visited

        while (!q.isEmpty()) {

            Pair curr = q.poll();

            for (int i = 0; i < 4; i++) {

                int newR = curr.r + directions[i][0];
                int newC = curr.c + directions[i][1];

                if (newR >= 0 && newR < n &&
                        newC >= 0 && newC < m &&
                        labyrinth[newR][newC] != '#' &&
                        arrive.get(newR).get(newC) == -1) {

                    arrive.get(newR).set(newC, i);
                    q.add(new Pair(newR, newC));
                }
            }
        }

        if (arrive.get(Brow).get(Bcol) == -1) {
            System.out.println("NO");
            return;
        }

        StringBuilder ans = new StringBuilder();

        int r = Brow;
        int c = Bcol;

        while (!(r == Arow && c == Acol)) {

            int d = arrive.get(r).get(c);

            ans.append(dirChar[d]);

            r -= directions[d][0];
            c -= directions[d][1];
        }

        ans.reverse();

        System.out.println("YES");
        System.out.println(ans.length());
        System.out.println(ans);
    }
}
