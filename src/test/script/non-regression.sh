#!/bin/sh
: '
Test script for the lexicon, syntax, and decompilation phases of the Deca compiler.
Author: Baptiste LE DUC

Description:
This script runs tests to validate the lexical (`test_lex`), syntax (`test_synt`), decompilation (`decac -p`)  and codegen (`decac`) phases 
by comparing the generated outputs to reference files.

Directories:
- Test files for lexicon and syntax: ./src/test/deca/syntax/valid/ and ./src/test/deca/syntax/valid/provided/
- Test files for decompilation: ./src/test/deca/decompile/
- Expected results for lexicon and syntax: ./src/test/results/deca/syntax/
- Expected results for decompilation: ./src/test/results/deca/decompile/
- Expected results for contextual verification tests: ./src/test/results/deca/contex/
- Temporary files: ./src/test/results/tmp/

Adding reference files for comparison:
- For `test_lex`, add `.lex` files to ./src/test/results/deca/syntax/lex/.
- For `test_synt`, add `.synt` files to ./src/test/results/deca/syntax/synt/.
- For `decac -p`, add `.synt` files to ./src/test/results/deca/decompile/.
- For `decac`, add `.ass` files to ./src/test/results/deca/codegen/.

Usage:
1. Executes tests using `test_lex`, `test_synt`, and `decac -p`.
2. Compares generated outputs with reference files.
3. Displays differences and stops execution on failure.

The script stops immediately if an error occurs.
'
# Stop script on any command failure
set -e 

# Temporary directory for intermediate files
TMP_DIR="./src/test/results/tmp/"

# Directories containing test files
INPUT_DIRS_SYNTAX="./src/test/deca/syntax/valid/ ./src/test/deca/syntax/valid/provided/"
INPUT_DIR_DECOMPILE="./src/test/deca/decompile/"
INPUT_DIR_CODEGEN="./src/test/deca/codegen/valid/"
INPUT_DIR_CONTEXT="./src/test/deca/context/valid/ ./src/test/deca/context/valid/provided/"
INPUT_DIR_INVALID_CONTEXT="./src/test/deca/context/invalid/ ./src/test/deca/context/invalid/provided/"

# SYNTAX TESTS CONFIGURATION
NAME_TEST_LEX="testlex"
EXEC_LEX="./src/test/script/launchers/test_lex"
EXTENSION_TEST_LEX="lex"
OUTPUT_DIR_TEST_LEX="./src/test/results/deca/syntax/lex/"
OPTIONS_TEST_LEX=""

NAME_TEST_SYNT="testsynt"
EXEC_SYNT="./src/test/script/launchers/test_synt"
EXTENSION_TEST_SYNT="synt"
OUTPUT_DIR_TEST_SYNT="./src/test/results/deca/syntax/synt/"
OPTIONS_TEST_SYNT=""

# CONTEXT TESTS CONFIGURATION
NAME_TEST_CONTEXT="testcontext"
EXEC_CONTEXT="./src/test/script/launchers/test_context"
EXTENSION_TEST_CONTEXT="synt"
OUTPUT_DIR_TEST_CONTEXT="./src/test/results/deca/context/"
OPTIONS_TEST_CONTEXT=""

# CONTEXT INVALID TESTS CONFIGURATION
NAME_TEST_INVALID_CONTEXT="invalid"
EXEC_INVALID_CONTEXT="./src/test/script/launchers/test_context"
EXTENSION_TEST_INVALID_CONTEXT="err"
OUTPUT_DIR_TEST_INVALID_CONTEXT="./src/test/results/deca/context/invalid/"
OPTIONS_TEST_INVALID_CONTEXT=""

# DECOMPILE TESTS CONFIGURATION
NAME_TEST_DECOMPILE="decompile"
EXEC_DECOMPILE="./src/main/bin/decac"
EXTENSION_TEST_DECOMPILE="deca"
OUTPUT_DIR_TEST_DECOMPILE="./src/test/results/deca/decompile/"
OPTIONS_DECOMPILE="-p"

# CODEGEN TESTS CONFIGURATION
NAME_TEST_CODEGEN="codegen"
EXEC_CODEGEN="./src/main/bin/decac"
EXTENSION_TEST_CODEGEN="ass"
OUTPUT_DIR_TEST_CODEGEN="./src/test/results/deca/codegen/"
OPTIONS_TEST_CODEGEN=""



ALL_TESTS="$NAME_TEST_LEX $NAME_TEST_SYNT $NAME_TEST_CONTEXT $NAME_TEST_INVALID_CONTEXT $NAME_TEST_DECOMPILE $NAME_TEST_CODEGEN"


# Retrieve output directory based on the test name
get_output_dir() {
    case $1 in
        $NAME_TEST_LEX)
            echo $OUTPUT_DIR_TEST_LEX
            ;;
        $NAME_TEST_SYNT)
            echo $OUTPUT_DIR_TEST_SYNT
            ;;
	$NAME_TEST_CONTEXT)
            echo $OUTPUT_DIR_TEST_CONTEXT
            ;;
	$NAME_TEST_INVALID_CONTEXT)
            echo $OUTPUT_DIR_TEST_INVALID_CONTEXT
            ;;
        $NAME_TEST_DECOMPILE)
            echo $OUTPUT_DIR_TEST_DECOMPILE
            ;;
        $NAME_TEST_CODEGEN)
            echo $OUTPUT_DIR_TEST_CODEGEN
            ;;
        *)
            echo "Error: output directory not found for $1."
            exit 1
            ;;
    esac
}

# Retrieve input directory based on the test name
get_input_dir() {
    case $1 in
        $NAME_TEST_LEX)
            echo $INPUT_DIRS_SYNTAX
            ;;
        $NAME_TEST_SYNT)
            echo $INPUT_DIRS_SYNTAX
            ;;
	$NAME_TEST_CONTEXT)
            echo $INPUT_DIR_CONTEXT
            ;;
	$NAME_TEST_INVALID_CONTEXT)
            echo $INPUT_DIR_INVALID_CONTEXT
            ;;
        $NAME_TEST_DECOMPILE)
            echo $INPUT_DIR_DECOMPILE
            ;;
        $NAME_TEST_CODEGEN)
            echo $INPUT_DIR_CODEGEN
            ;;
        *)
            echo "Error: input directory not found for $1."
            exit 1
            ;;
    esac
}

# Retrieve executable command based on the test name
get_exec() {
    case $1 in
        $NAME_TEST_LEX)
            echo $EXEC_LEX
            ;;
        $NAME_TEST_SYNT)
            echo $EXEC_SYNT
            ;;
	$NAME_TEST_CONTEXT)
            echo $EXEC_CONTEXT
            ;;
	$NAME_TEST_INVALID_CONTEXT)
            echo $EXEC_INVALID_CONTEXT
            ;;
        $NAME_TEST_DECOMPILE)
            echo $EXEC_DECOMPILE
            ;;
        $NAME_TEST_CODEGEN)
            echo $EXEC_CODEGEN
            ;;
        *)
            echo "Error: executable not found for $1."
            exit 1
            ;;
    esac
}

# Retrieve output file extension based on the test name
get_extension() {
    case $1 in
        $NAME_TEST_LEX)
            echo $EXTENSION_TEST_LEX
            ;;
        $NAME_TEST_SYNT)
            echo $EXTENSION_TEST_SYNT
            ;;
	$NAME_TEST_CONTEXT)
            echo $EXTENSION_TEST_CONTEXT
            ;;
	$NAME_TEST_INVALID_CONTEXT)
            echo $EXTENSION_TEST_INVALID_CONTEXT
            ;;
        $NAME_TEST_DECOMPILE)
            echo $EXTENSION_TEST_DECOMPILE
            ;;
        $NAME_TEST_CODEGEN)
            echo $EXTENSION_TEST_CODEGEN
            ;;
        *)
            echo "Erreur : extension non trouvée pour $1."
            exit 1
            ;;
    esac
}

# Retrieve options based on the test name
get_options() {
    case $1 in
        $NAME_TEST_LEX)
            echo $OPTIONS_TEST_LEX
            ;;
        $NAME_TEST_SYNT)
            echo $OPTIONS_TEST_SYNT
            ;;
	$NAME_TEST_CONTEXT)
            echo $OPTIONS_TEST_CONTEXT
            ;;
	$NAME_TEST_INVALID_CONTEXT)
            echo $OPTIONS_TEST_INVALID_CONTEXT
            ;;
        $NAME_TEST_DECOMPILE)
            echo $OPTIONS_DECOMPILE
            ;;
        $NAME_TEST_CODEGEN)
            echo $OPTIONS_TEST_CODEGEN
            ;;
        *)
            echo "Erreur : options non trouvées pour $1."
            exit 1
            ;;
    esac
}

# Execute syntax-related tests (test_lex, test_synt) and save output to a temporary file for comparison (ex : test_lex file.deca > tmp_file.lex) 
exec_test_syntax(){
    executable=$1
    options=$2
    file=$3
    tmp_file=$4

    $executable $options "$file" > "$tmp_file"
}

# Execute context-related tests and save output to a temporary file for comparison (ex : test_context file.deca > tmp_file.synt) 
exec_test_context(){
    executable=$1
    options=$2
    file=$3
    tmp_file=$4

    $executable $options "$file" > "$tmp_file"
}

# Execute invalid-context-related tests and save output to a temporary file for comparison (ex : test_context file.deca > tmp_file.synt 2>&1) 
exec_test_invalid_context(){
    executable=$1
    options=$2
    file=$3
    tmp_file=$4

    $executable $options "$file" > "$tmp_file" 2>&1 || true
}
# Execute decompilation tests and save output to a temporary file for comparison (ex : decac -p file.deca > tmp_file.synt)
exec_test_decompile(){
    executable=$1
    options=$2
    file=$3
    tmp_file=$4

    $executable $options "$file" > "$tmp_file" 2>&1
}

# Execute codegen tests and save output to a temporary file for comparison (ex : decac file.deca > tmp_file.ass)
exec_test_codegen(){
    executable=$1
    options=$2
    file=$3
    tmp_file=$4

    $executable $options "$file"
    mv "$input_dir$(basename "${file%.deca}").$EXTENSION_TEST_CODEGEN" "$tmp_file"
}

# Generic execution function for tests
exec_test(){
    name_test=$1
    executable=$2
    options=$3
    file=$4
    tmp_file=$5

    case $1 in
        $NAME_TEST_LEX)
            exec_test_syntax "$executable" "$options" "$file" "$tmp_file"
            ;;
        $NAME_TEST_SYNT)
            exec_test_syntax "$executable" "$options" "$file" "$tmp_file"
            ;;
	$NAME_TEST_CONTEXT)
            exec_test_context "$executable" "$options" "$file" "$tmp_file"
            ;;
	$NAME_TEST_INVALID_CONTEXT)
            exec_test_invalid_context "$executable" "$options" "$file" "$tmp_file"
            ;;
        $NAME_TEST_DECOMPILE)
            exec_test_decompile "$executable" "$options" "$file" "$tmp_file"
            ;;
        $NAME_TEST_CODEGEN)
            exec_test_codegen "$executable" "$options" "$file" "$tmp_file"
            ;;
        *)
            echo "Erreur : test exec non trouvé pour $1."
            exit 1
            ;;
    esac

}


# Function to execute a non-regression test.
#
# Parameters:
#   $1 : Input directory containing `.deca` test files.
#   $2 : Output directory for saving and comparing results.
#   #3 : Options for the executable (e.g., `-p` for `decac -p`).
#   $4 : Executable (e.g., `test_lex` or `test_synt` or `decac -p`).
#   $5 : Name of the test (e.g., `test_lex` or `test_synt` or `decompile` or `codegen`).
#
# Description:
#   Runs tests on `.deca` files, saves outputs temporarily, and compares them to expected results.
#   Displays differences if mismatched and stops on errors. Cleans up temporary files after comparison
run_non_regression_tests() {
    
    input_dir=$1
    output_dir=$2
    options=$3
    executable=$4
    name_test=$5

    if [ ! -d "$output_dir" ]; then
        echo "Error: output directory $output_dir does not exist."
        exit 1
    fi

    if [ ! -d "$input_dir" ]; then
        echo "Error: input directory $input_dir does not exist."
        exit 1
    fi
    # Détermine l'extension des fichiers de sortie (lex ou synt)
    extension=$(get_extension $name_test)

    for file in "$input_dir"*.deca
    do
        if [ -f "$file" ]; then
            echo "Processing file: $file"
            output_file="$output_dir$(basename "${file%.deca}").$extension"
            tmp_file=$TMP_DIR$(basename "${file%.deca}").$extension

            exec_test $name_test "$executable" "$options" "$file" "$tmp_file"

            if [ -f "$output_file" ]; then
                # Compare les résultats avec le fichier existant
                if ! diff "$output_file" "$tmp_file" > /dev/null; then
                    echo "Error: $output_file and $tmp_file differ."
                    diff "$output_file" "$tmp_file"
                    exit 1
                fi
                rm "$tmp_file"
            else
                # If the output file does not exist, remove the temporary output
                echo "Warning: $output_file does not exist."
                rm "$tmp_file"
            fi
        fi
    done
}




# Main function to execute all tests
main() {
    for test_name in $ALL_TESTS; do
        echo "[BEGIN] : $test_name"
            for input_dir in $(get_input_dir $test_name); do
                run_non_regression_tests "$input_dir" "$(get_output_dir $test_name)" "$(get_options $test_name)" "$(get_exec $test_name)" "$test_name"
            done
        echo "[SUCCESS] : $executable"
    done
}

main
