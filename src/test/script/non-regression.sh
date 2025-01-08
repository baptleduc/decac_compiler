#!/bin/sh
: '
Script de tests pour la phase de lexique, de syntaxe, et de décompilation du compilateur Deca.
Auteur : Baptiste LE DUC

Description :
Ce script exécute des tests pour vérifier la conformité des phases de lexique (`test_lex`), de syntaxe (`test_synt`), 
et de décompilation (`decac -p`) en comparant les résultats générés avec des fichiers de référence. 

Répertoires :
- Les fichiers de test pour lexique et syntaxe sont recherchés dans : ./src/test/deca/syntax/valid/ et ./src/test/deca/syntax/valid/provided/
- Les fichiers de test pour décompilation sont recherchés dans : ./src/test/deca/decompile/
- Les résultats attendus pour lexique et syntaxe sont situés dans : ./src/test/results/deca/syntax/
- Les résultats attendus pour décompilation sont situés dans : ./src/test/results/deca/decompile/
- Les fichiers temporaires sont stockés dans : ./src/test/results/tmp/

Ajout de fichiers de référence pour la comparaison :
- Pour `test_lex`, ajoutez un fichier `.lex` dans ./src/test/results/deca/syntax/lex/.
- Pour `test_synt`, ajoutez un fichier `.synt` dans ./src/test/results/deca/syntax/synt/.
- Pour `decac -p`, ajoutez un fichier `.synt` dans ./src/test/results/deca/decompile/.

Usage :
Le script :
1. Exécute les tests avec les exécutables `test_lex`, `test_synt`, et `decac -p`.
2. Compare les résultats générés avec les fichiers de référence.
3. Affiche les différences en cas d'incohérence et arrête l'exécution.

Le script s’arrête automatiquement si une erreur survient.
'
# Arrête le script en cas d'echec d'une commande
set -e 

TMP_DIR="./src/test/results/tmp/"

# Répertoires contenant les fichiers de tests
INPUT_DIRS_SYNTAX="./src/test/deca/syntax/valid/ ./src/test/deca/syntax/valid/provided/"
INPUT_DIR_DECOMPILE="./src/test/deca/decompile/"
OUTPUT_DIR_SYNTAX="./src/test/results/deca/syntax/"
OUTPUT_DIR_DECOMPILE="./src/test/results/deca/decompile/"

TEST_LEX="./src/test/script/launchers/test_lex"
TEST_SYNT="./src/test/script/launchers/test_synt"
TEST_DECOMPILE="decac -p"
EXECUTABLES="$TEST_LEX $TEST_SYNT"

# Fonction pour exécuter les tests de lexique et de syntaxe
# Arguments :
#   $1 : Répertoire d'entrée (contenant les fichiers .deca)
#   $2 : Répertoire de sortie (où les résultats seront sauvegardés)
#   $3 : Exécutable (test_lex ou test_synt)
run_tests_syntax() {
    input_dir=$1
    output_dir=$2
    executable=$3

    if [ ! -d "$output_dir" ]; then
        echo "Erreur : le répertoire de sortie $output_dir n'existe pas."
        exit 1
    fi

    if [ ! -d "$input_dir" ]; then
        echo "Erreur : le répertoire d'entrée $input_dir n'existe pas."
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
                    echo "Erreur : $output_file et $tmp_file non identiques."
                    diff "$output_file" "$tmp_file"
                    rm "$tmp_file"
                    exit 1
                fi
                rm "$tmp_file"
            else
                # Si le fichier de sortie n'existe pas, supprime la sortie temporaire
                rm "$tmp_file"
            fi
        fi
    done
}

# Fonction pour exécuter les tests de décompilation
# Arguments :
#   $1 : Répertoire d'entrée (contenant les fichiers .deca)
#   $2 : Répertoire de sortie (où les résultats seront sauvegardés)
#   $3 : Commande de décompilation (decac -p)
run_tests_decompile() {
    input_dir=$1
    output_dir=$2
    command=$3

    if [ ! -d "$output_dir" ]; then
        echo "Erreur : le répertoire de sortie $output_dir n'existe pas."
        exit 1
    fi

    if [ ! -d "$input_dir" ]; then
        echo "Erreur : le répertoire d'entrée $input_dir n'existe pas."
        exit 1
    fi

    for file in "$input_dir"*.deca
    do
        if [ -f "$file" ]; then
            echo "Traitement du fichier : $file"
            output_file="$output_dir$(basename "${file%.deca}").synt"
            tmp_file=$TMP_DIR$(basename "${file%.deca}").synt
            $command "$file" 2> "$tmp_file"

            if [ -f "$output_file" ]; then
                # Compare les résultats avec le fichier existant
                if ! diff "$output_file" "$tmp_file" > /dev/null; then
                    echo "Erreur : $output_file et $tmp_file non identiques."
                    diff "$output_file" "$tmp_file"
                    rm "$tmp_file"
                    exit 1
                fi
                rm "$tmp_file"
            else
                # Si le fichier de sortie n'existe pas, supprime la sortie temporaire
                rm "$tmp_file"
            fi
        fi
    done
}

# Fonction principale
main() {
    # Tests de lexique et de syntaxe
    for executable in $EXECUTABLES; do
        echo "[BEGIN] : $executable"
        for input_dir in $INPUT_DIRS_SYNTAX; do
            run_tests_syntax "$input_dir" "$OUTPUT_DIR_SYNTAX" "$executable"
        done
        echo "[SUCCESS] : $executable"
    done

    # Tests de décompilation
    echo "[BEGIN] : Décompilation"
    run_tests_decompile "$INPUT_DIR_DECOMPILE" "$OUTPUT_DIR_DECOMPILE" "$TEST_DECOMPILE"
    echo "[SUCCESS] : Décompilation"
}
main
