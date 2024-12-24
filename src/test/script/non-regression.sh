#!/bin/sh

TMP_DIR="./src/test/results/tmp/"

# Répertoires contenant les fichiers de tests
INPUT_DIRS="./src/test/deca/syntax/valid/ ./src/test/deca/syntax/valid/provided"
OUTPUT_DIR="./src/test/results/deca/syntax/"

TEST_LEX="./src/test/script/launchers/test_lex"
TEST_SYNT="./src/test/script/launchers/test_synt"
EXECUTABLES="$TEST_LEX $TEST_SYNT"

# Fonction pour exécuter les tests
# Arguments :
#   $1 : Répertoire d'entrée (contenant les fichiers .deca)
#   $2 : Répertoire de sortie (où les résultats seront sauvegardés)
#   $3 : Exécutable (test_lex ou test_synt)
run_tests() {
    input_dir=$1
    output_dir=$2
    executable=$3

    if [ ! -d "$output_dir" ]; then
        echo "Erreur : le répertoire de sortie $output_dir n'existe pas."
        exit 1
    fi

    if [ ! -d "$input_dir" ]; then
        echo "Erreur : le répertoire d'entré $input_dir n'existe pas."
        exit 1
    fi
    # Détermine l'extension des fichiers de sortie (lex ou synt)
    extension=$(echo "$executable" | cut -d'_' -f2)

    
    for file in "$input_dir"*.deca
    do
        if [ -f "$file" ]; then
            echo "Traitement du fichier : $file"
            output_file="$output_dir$extension/$(basename "${file%.deca}").$extension"
            tmp_file=$TMP_DIR$(basename "${file%.deca}").$extension
            $executable "$file" > "$tmp_file"
            
            if [ -f "$output_file" ]; then
                # Compare les résultats avec le fichier existant
                if ! diff "$output_file" "$tmp_file" > /dev/null; then
                    diff "$output_file" "$tmp_file"
                    echo "Erreur : $output_file et $tmp_file non identique."
                    diff "$output_file" "$tmp_file"
                    rm "$tmp_file"
                    exit 1
                fi
                rm "$tmp_file"
            else
                # Si le fichier de sortie n'existe pas, sauvegarde la sortie temporaire
                mv "$tmp_file" "$output_file"
            fi
        fi
    done
    exit 0
}


# Fonction principale
main() {
    
    # Parcours les exécutables (test_lex et test_synt)
    for executable in $EXECUTABLES; do
        echo "[BEGIN] : $executable"
        for input_dir in $INPUT_DIRS; do
            run_tests "$input_dir" "$OUTPUT_DIR" "$executable"
        done
        echo "[SUCESS] : $executable"
    done
}
main