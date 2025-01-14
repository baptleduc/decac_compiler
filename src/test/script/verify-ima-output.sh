#!/bin/bash

: '
Test script for validating code generation of the Deca compiler.
Author: Baptiste LE DUC

Description:
This script runs tests to validate the code generation (`decac`) and execution `ima` phases 
for all `.deca` files located in the specified directory. It compares the actual outputs 
produced by the compiler to the expected results specified in the `Results:` section 
of each `.deca` file.

Directories:
- Test files: ./src/test/deca/codegen/valid/
- Generated `.ass` files: same directory as the `.deca` files.

Expected Results:
- The expected output is defined in the line following the `Results:` section in each `.deca` file.
- If the expected result is `None`, the program is expected to produce no output.

Usage:
1. Compiles each `.deca` file using `decac`.
2. Executes the resulting `.ass` file using `ima`.
3. Compares the output of `ima` with the expected result.
4. Displays differences and logs the results for each file.

The script continues execution for all files, even if some tests fail.
'



process_deca_file() {
    local DECA_FILE="$1"
    echo "Processing file: $DECA_FILE"

    # Extract the expected output from the line below "Results:"
    local EXPECTED_OUTPUT
    EXPECTED_OUTPUT=$(grep -A 1 -i "Results:" "$DECA_FILE" | tail -n 1 | sed 's|^[[:space:]]*//||' | sed 's|^[[:space:]]*||;s|[[:space:]]*$||')


    # If no "Results:" line is found, skip this file
    if [ -z "$EXPECTED_OUTPUT" ]; then
        echo "Warning: No 'Results:' line found in $DECA_FILE. Skipping..."
        return
    fi

    # Determine the corresponding .ass file path
    local ASS_FILE="${DECA_FILE%.deca}.ass"

    # Step 1: Compile the .deca file with decac
    echo "Compiling $DECA_FILE..."
    if ! "$DECAC_EXEC" "$DECA_FILE"; then
        echo "Error: Compilation failed for $DECA_FILE. Skipping..."
        return
    fi
    echo "Compilation successful."

    # Step 2: Execute the .ass file with $IMA_EXEC
    echo "Executing $ASS_FILE..."
    local OUTPUT
    local ERROR_OUTPUT
    OUTPUT=$($IMA_EXEC "$ASS_FILE" 2> >(ERROR_OUTPUT=$(cat)))
    local EXIT_CODE=$?

    # Step 3: Verify the program output
    # Convert the expected output to lowercase for case-insensitive comparison
    EXPECTED_OUTPUT_LOWER=$(echo "$EXPECTED_OUTPUT" | tr '[:upper:]' '[:lower:]')
    echo "Verifying output..."
    if [ "$EXPECTED_OUTPUT_LOWER" = "none" ]; then
        # Expected output is "None", so we expect no output
        if [ -z "$OUTPUT" ]; then
            echo "Test passed for $DECA_FILE. Output is correctly empty."
        else
            echo "Test failed for $DECA_FILE. Expected no output, but got: $OUTPUT"
            rm "$ASS_FILE"
            exit 1
        fi
    else
        # Expected output is not "None", verify the output matches
        if [ "$OUTPUT" = "$EXPECTED_OUTPUT" ]; then
            echo "Test passed for $DECA_FILE. Output is correct: $OUTPUT"
        else
            echo "Test failed for $DECA_FILE. Expected: $EXPECTED_OUTPUT, Got: $OUTPUT"
            rm "$ASS_FILE"
            exit 1
        fi
    fi
    rm "$ASS_FILE"
    echo "-----------------------------------"
}

# Directory containing the .deca files, dir can be add as needed.
DECA_DIR="./src/test/deca/codegen/valid ./src/test/deca/codegen/valid/test_if"
IMA_EXEC="./env/ima_sources/bin/ima"
DECAC_EXEC="./src/main/bin/decac"

main() {
    for dir in $DECA_DIR; do
        if [ ! -d "$dir" ]; then
            echo "Error: Directory $dir does not exist."
            exit 1
        fi

        for DECA_FILE in "$dir"/*.deca; do
            process_deca_file "$DECA_FILE"
        done
    done
    
}

main
