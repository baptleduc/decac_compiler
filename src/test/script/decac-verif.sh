#!/bin/sh
: '
Test script using the `decac -v` option to validate the contextual verification phase of the Deca compiler.
The test fails if the output of `decac -v` is not empty.
Author: Baptiste LE DUC
'

INPUT_DIRS="./src/test/deca/context/valid/ ./src/test/deca/context/valid/provided/ ./src/test/deca/codegen/valid/ ./src/test/deca/codegen/valid/provided/"

echo "[BEGIN] Testing contextual verification phase of the Deca compiler with : decac -v "
for dir in $INPUT_DIRS; do
    for file in "$dir"*.deca; do
        echo "Testing $file"
        if ! ./src/main/bin/decac -v "$file" > /dev/null; then
            echo "Error: $file failed the contextual verification phase."
            exit 1
        fi
    done
done

echo "[SUCESS] All tests passed."