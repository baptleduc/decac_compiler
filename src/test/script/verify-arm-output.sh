#!/bin/bash

: '
Test script for validating ARM code generation of the Deca compiler.
Author: [Votre Nom]

Description:
This script runs tests to validate the ARM code generation (`deca --arm`) phase 
for all `.deca` files located in the specified directories. It assembles, links, and 
executes the resulting binaries using QEMU, and compares their outputs to the 
expected results specified in the `Results:` section of each `.deca` file.

Directories:
- Test files: ./src/test/deca/codegen/valid/
- Generated `.s` files: same directory as the `.deca` files.

Expected Results:
- The expected output is defined in the line following the `Results:` section in each `.deca` file.
- If the expected result is `None`, the program is expected to produce no output.

Usage:
1. Compiles each `.deca` file using `deca --arm`.
2. Assembles and links the resulting `.s` file using `aarch64-linux-gnu-gcc`.
3. Executes the binary using QEMU.
4. Compares the output of the binary with the expected result.
5. Displays differences and logs the results for each file.
6. Allows skipping failed tests by pressing 's'.
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

    # Determine the corresponding .s file path
    local ASM_FILE="${DECA_FILE%.deca}.s"
    local BINARY_FILE="${DECA_FILE%.deca}"

    # Step 1: Compile the .deca file with deca --arm
    echo "Compiling $DECA_FILE to $ASM_FILE..."
    if ! "$DECAC_EXEC" --arm "$DECA_FILE"; then
        echo "Error: Compilation failed for $DECA_FILE."
        read -n 1 -p "Press 's' to skip or any other key to exit: " choice
        echo ""
        if [ "$choice" = "s" ]; then
            return
        else
            exit 1
        fi
    fi
    echo "Compilation successful."

    # Step 2: Assemble and link the .s file
    echo "Assembling and linking $ASM_FILE..."
    if ! aarch64-linux-gnu-gcc -o "$BINARY_FILE" "$ASM_FILE"; then
        echo "Error: Assembly or linkage failed for $ASM_FILE."
        read -n 1 -p "Press 's' to skip or any other key to exit: " choice
        echo ""
        if [ "$choice" = "s" ]; then
            rm "$ASM_FILE"
            return
        else
            rm "$ASM_FILE"
            exit 1
        fi
    fi
    echo "Assembly and linkage successful."

    # Step 3: Execute the binary with QEMU
    echo "Executing $BINARY_FILE with QEMU..."
    local OUTPUT
    OUTPUT=$(qemu-aarch64 -L /usr/aarch64-linux-gnu/ -cpu cortex-a53 "$BINARY_FILE" 2>&1)

    # Step 4: Verify the program output
    # Convert the expected output to lowercase for case-insensitive comparison
    EXPECTED_OUTPUT_LOWER=$(echo "$EXPECTED_OUTPUT" | tr '[:upper:]' '[:lower:]')
    echo "Verifying output..."
    if [ "$EXPECTED_OUTPUT_LOWER" = "none" ]; then
        # Expected output is "None", so we expect no output
        if [ -z "$OUTPUT" ]; then
            echo "Test passed for $DECA_FILE. Output is correctly empty."
        else
            echo "Test failed for $DECA_FILE. Expected no output, but got: $OUTPUT"
            read -n 1 -p "Press 's' to skip or any other key to exit: " choice
            echo ""
            if [ "$choice" = "s" ]; then
                return
            else
                exit 1
            fi
        fi
    else
        # Expected output is not "None", verify the output matches
        if [ "$OUTPUT" = "$EXPECTED_OUTPUT" ]; then
            echo "Test passed for $DECA_FILE. Output is correct: $OUTPUT"
        else
            echo "Test failed for $DECA_FILE. Expected: $EXPECTED_OUTPUT, Got: $OUTPUT"
            read -n 1 -p "Press 's' to skip or any other key to exit: " choice
            echo ""
            if [ "$choice" = "s" ]; then
                rm "$ASM_FILE" "$BINARY_FILE"
                return
            else
                rm "$ASM_FILE" "$BINARY_FILE"
                exit 1
            fi
        fi
    fi

    # Cleanup
    rm "$ASM_FILE" "$BINARY_FILE"
    echo "-----------------------------------"
}

# Directory containing the .deca files
DECA_DIR="./src/test/deca/codegen/valid/test_arithmetic ./src/test/deca/codegen/valid ./src/test/deca/codegen/valid/test_if ./src/test/deca/codegen/valid/test_while"
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
