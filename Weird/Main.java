import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        long n = Long.parseLong(br.readLine().trim());

        while (true) {
            sb.append(n).append(" ");
            if (n == 1)
                break;
            if (n % 2 == 0) {
                n = n / 2;
            } else {
                n = n * 3 + 1;
            }
        }
        sb.append("\n");

        System.out.print(sb);
    }
}
