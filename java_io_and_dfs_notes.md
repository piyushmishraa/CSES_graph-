# Java I/O & DFS Notes (CSES / Competitive Programming)

## 1. Why BufferedReader + StringBuilder

- `System.in` = raw byte input stream, clunky to use directly.
- `InputStreamReader` wraps it to convert bytes → characters.
- `BufferedReader` wraps that to add **buffering** (reads big chunks at once) and gives you `.readLine()` to read a full line as a `String`.
- `Scanner` is simpler but much slower — avoid for CP, especially with large inputs (10^5+).
- `StringBuilder` is for **output**: append everything, then print once at the end with `System.out.print(sb)`. Avoids the overhead of calling `print()` repeatedly in a loop.

```java
BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
StringBuilder sb = new StringBuilder();
// ... build sb ...
System.out.print(sb);
```

---

## 2. Common Input Patterns

### Single integer on one line
```
3
```
```java
int n = Integer.parseInt(br.readLine().trim());
```

### Two integers, space-separated
```
3 5
```
```java
String[] parts = br.readLine().trim().split(" ");
int a = Integer.parseInt(parts[0]);
int b = Integer.parseInt(parts[1]);
```

### N integers (array), count on first line
```
5
1 2 3 4 5
```
```java
int n = Integer.parseInt(br.readLine().trim());
String[] parts = br.readLine().trim().split(" ");
int[] arr = new int[n];
for (int i = 0; i < n; i++) {
    arr[i] = Integer.parseInt(parts[i]);
}
```

### Faster alternative: StringTokenizer
`split()` uses regex internally — slow for huge inputs (10^5+). `StringTokenizer` is the standard fast alternative in CP:
```java
import java.util.StringTokenizer;

int n = Integer.parseInt(br.readLine().trim());
StringTokenizer st = new StringTokenizer(br.readLine());
int[] arr = new int[n];
for (int i = 0; i < n; i++) {
    arr[i] = Integer.parseInt(st.nextToken());
}
```

### Multiple lines, each with different values
```
3
1 2
3 4
5 6
```
```java
int n = Integer.parseInt(br.readLine().trim());
for (int i = 0; i < n; i++) {
    StringTokenizer st = new StringTokenizer(br.readLine());
    int x = Integer.parseInt(st.nextToken());
    int y = Integer.parseInt(st.nextToken());
}
```

### Unknown number of lines — read until EOF
```java
String line;
while ((line = br.readLine()) != null) {
    int val = Integer.parseInt(line.trim());
}
```

### A line of text (don't split as numbers)
```java
String sentence = br.readLine();
```

### Big numbers that might overflow int
```java
long n = Long.parseLong(br.readLine().trim());
```

### Reading a grid (rows of characters, no spaces between chars)
```
5 8
########
#..#...#
####.#.#
#..#...#
########
```
```java
String[] inputs1 = br.readLine().trim().split(" ");
int n = Integer.parseInt(inputs1[0]);
int m = Integer.parseInt(inputs1[1]);

char[][] grid = new char[n][m];
for (int i = 0; i < n; i++) {
    String row = br.readLine();   // do NOT trim grid rows — only trim the dimension line
    for (int j = 0; j < m; j++) {
        grid[i][j] = row.charAt(j);
    }
}
```

### Decision guide
| Situation | Use |
|---|---|
| One number | `Integer.parseInt(br.readLine().trim())` |
| Few numbers, same line | `split(" ")` or `StringTokenizer` |
| Many numbers (10^5+) | `StringTokenizer` (never `split` at scale) |
| Unknown number of lines | `while ((line = br.readLine()) != null)` |
| Line of text/words | Just `br.readLine()`, don't parse as numbers |
| Grid of characters | Read each row as a raw `String`, index with `.charAt(j)` |

---

## 3. Output formatting gotchas

- Many CSES problems expect a trailing newline at the end of output. If `diff` shows identical content but says `\ No newline at end of file`, just add `\n`:
  ```java
  sb.append(answer).append("\n");
  System.out.print(sb);
  ```
- Some problems expect a trailing space after every number, including the last one (check the expected `.out` file byte-for-byte with `xxd` or `od -c` if `diff` shows a mismatch you can't see visually).

---

## 4. Recursive DFS → Iterative DFS (avoiding StackOverflowError)

### Why it happens
Recursive calls use Java's **call stack**, which has a small, fixed size (~512KB–1MB by default). On large grids (e.g. 1000x1000 with a big connected region), recursion can go thousands of levels deep and overflow the stack. CSES often uses large grids specifically to test this.

### The fix: manage your own stack on the heap
Instead of letting Java implicitly manage recursive calls, we use an explicit `Deque` as our own stack. The heap has much more room than the call stack, so this avoids the overflow entirely.

**Recursive (breaks on large input):**
```java
static void dfs(char[][] grid, int x, int y) {
    grid[x][y] = '*';
    for (int i = 0; i < direction.length; i++) {
        int newx = x + direction[i][0];
        int newy = y + direction[i][1];
        if (valid && grid[newx][newy] == '.') {
            dfs(grid, newx, newy);  // recursive call = stack frame
        }
    }
}
```

**Iterative (safe for large input):**
```java
static void dfs(char[][] grid, int sx, int sy) {
    Deque<int[]> stack = new ArrayDeque<>();
    stack.push(new int[]{sx, sy});
    grid[sx][sy] = '*';

    while (!stack.isEmpty()) {
        int[] cur = stack.pop();
        int x = cur[0], y = cur[1];
        for (int i = 0; i < direction.length; i++) {
            int newx = x + direction[i][0];
            int newy = y + direction[i][1];
            if (newx >= 0 && newx < n && newy >= 0 && newy < m && grid[newx][newy] == '.') {
                grid[newx][newy] = '*';           // mark visited on push, not on pop
                stack.push(new int[]{newx, newy});
            }
        }
    }
}
```

### Key mapping between the two versions
| Recursive | Iterative |
|---|---|
| Function call `dfs(...)` | `stack.push(...)` |
| Function returns / call ends | `stack.pop()` happens naturally in the while loop |
| Call stack (managed by JVM, small) | `Deque` (managed by you, lives on heap, large) |

### Important subtlety
Mark a cell visited **when pushing**, not when popping. Otherwise the same cell can be pushed multiple times by different neighbors before being processed, wasting work and potentially causing bugs.

### Alternative: just use BFS
BFS uses a `Queue` instead of a stack and is iterative by nature — so it never hits this problem at all. For connected-components / flood-fill problems, BFS and iterative DFS give identical results; pick whichever is easier for you to reason about.

```java
static void bfs(char[][] grid, int sx, int sy) {
    Queue<int[]> queue = new LinkedList<>();
    queue.add(new int[]{sx, sy});
    grid[sx][sy] = '*';

    while (!queue.isEmpty()) {
        int[] cur = queue.poll();
        int x = cur[0], y = cur[1];
        for (int i = 0; i < direction.length; i++) {
            int newx = x + direction[i][0];
            int newy = y + direction[i][1];
            if (newx >= 0 && newx < n && newy >= 0 && newy < m && grid[newx][newy] == '.') {
                grid[newx][newy] = '*';
                queue.add(new int[]{newx, newy});
            }
        }
    }
}
```

---

## 5. Why `Deque` instead of `Stack`

Java's `java.util.Stack` is legacy (Java 1.0 era):
- It's synchronized → locking overhead even though CP code is single-threaded.
- It extends `Vector`, another legacy synchronized class.
- Official Java docs recommend `Deque` instead.

`ArrayDeque` is faster (no synchronization), and supports both stack and queue operations from the same interface:

```java
// Stack behavior (LIFO)
Deque<int[]> stack = new ArrayDeque<>();
stack.push(x);
stack.pop();

// Queue behavior (FIFO)
Deque<int[]> queue = new ArrayDeque<>();
queue.offer(x);
queue.poll();
```

**Rule of thumb:** always prefer `ArrayDeque` over `Stack`/`Vector` in modern Java and especially in CP.

---

## 6. When to worry about recursion depth

| Input size | Recursive DFS safe? |
|---|---|
| ~10^3 or less | Usually fine |
| ~10^4+ | Convert to iterative DFS or use BFS |

This pattern (StackOverflowError on deep recursion) recurs constantly in CP — connected components, flood fill, maze/labyrinth pathfinding, graph connectivity, tree traversal on skewed trees. Recognize the symptom (`StackOverflowError`, repeating stack trace at the same line) and know the fix (iterative stack or BFS) by default.
