#!/bin/bash
# usage: run inside a problem folder after compiling, e.g. ../run_tests.sh
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
