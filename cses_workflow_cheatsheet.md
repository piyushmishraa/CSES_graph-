# CSES + Java + Neovim — Workflow Cheatsheet

## Folder structure
```
~/cses/
  templates/Template.java   <- blank Java template
  new.sh                     <- creates a new problem folder
  run_tests.sh                <- compiles + runs all test cases
  1068/                       <- one folder per problem
    Main.java
    tests.zip
    1.in, 1.out, 2.in, 2.out, ...
```

---

## 1. Starting a new problem

```bash
cd ~/cses
./new.sh 1068
```
- Creates folder `1068/`
- Copies `templates/Template.java` into `1068/Main.java`
- Opens `Main.java` in nvim automatically

Use the **CSES problem number** as the argument (no spaces, no problem names).

---

## 2. Writing your solution

Inside nvim, fill in the logic inside `main()`. Save and quit:
```
:wq
```

---

## 3. Getting test cases

Download "tests.zip" (or similar) from the CSES problem page directly into the problem folder, then:

```bash
cd ~/cses/1068
unzip "tests.zip"
```
This extracts files like `1.in`, `1.out`, `2.in`, `2.out`, etc. — one input/output pair per official test case.

---

## 4. Compiling and testing — ALL test cases at once

From inside the problem folder:
```bash
../run_tests.sh
```

What it does:
- Compiles `Main.java` (stops with an error message if compilation fails)
- Runs your program against every `.in` file
- Compares your output to the matching `.out` file
- Prints `PASS` or `FAIL` per test
- **If a test fails, it automatically shows the diff** (expected vs. yours) right there
- Prints a final summary: `Summary: X passed, Y failed`

This is the one command you'll run constantly. No need to write the loop manually anymore.

---

## 5. Manually inspecting a specific test (optional)

If you want to look closer at one test case manually:

```bash
diff 14.out 14.myout
```
- `14.out` = expected output
- `14.myout` = your program's actual output (created automatically when you run the test script)
- No output from `diff` = files are identical
- `<` = expected line, `>` = your line

To see everything side by side:
```bash
cat 14.in
echo "---expected---"
cat 14.out
echo "---yours---"
cat 14.myout
```

---

## 6. Once everything passes

Copy the contents of `Main.java`, paste into CSES's submit box on the website, and submit.

---

## Common gotchas

- **`~` always means home directory**, regardless of which folder you were in when you typed a command. `cd ~/cses` always goes to the same place.
- **Use `long` instead of `int`** for problems where intermediate values might overflow (like Collatz-style sequences), even if the final constraints fit in `int`.
- **Trailing space / newline mismatches** are a common silent failure — your output and expected output can *look* identical but differ by one invisible character. Use `xxd file | tail -3` to inspect raw bytes if `diff` shows no visible difference but tests still fail.
- **Filenames with spaces** (like `tests (1).zip`) need to be wrapped in quotes when used in commands: `unzip "tests (1).zip"`.
- macOS's `cat` doesn't support `-A` (that's GNU/Linux only). Use `cat -e` or `xxd`/`od -c` instead for inspecting raw file bytes on Mac.

---

## One-time setup files (already created, for reference)

**`templates/Template.java`**
```java
import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();



        System.out.print(sb);
    }
}
```

**`new.sh`**
```bash
#!/bin/bash
# usage: ./new.sh 1068
mkdir -p "$1"
cp templates/Template.java "$1/Main.java"
cd "$1" && nvim Main.java
```

**`run_tests.sh`**
```bash
#!/bin/bash
# usage: run from inside a problem folder, e.g. ../run_tests.sh
javac Main.java
if [ $? -ne 0 ]; then
    echo "Compilation failed."
    exit 1
fi

pass=0
fail=0

for f in *.in; do
    base="${f%.in}"
    java Main < "$f" > "$base.myout"
    if diff -q "$base.myout" "$base.out" > /dev/null; then
        echo "Test $base: PASS"
        pass=$((pass+1))
    else
        echo "Test $base: FAIL"
        fail=$((fail+1))
        echo "  --- diff (expected vs got) ---"
        diff "$base.out" "$base.myout" | sed 's/^/  /'
    fi
done

echo ""
echo "Summary: $pass passed, $fail failed"
```
