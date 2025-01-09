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
EXTENSION_TEST_LEX="lex"
OUTPUT_DIR_TEST_LEX="./src/test/results/deca/syntax/lex/"
OPTIONS_TEST_LEX=""

TEST_SYNT="./src/test/script/launchers/test_synt"
EXTENSION_TEST_SYNT="synt"
OUTPUT_DIR_TEST_SYNT="./src/test/results/deca/syntax/synt/"
OPTIONS_TEST_SYNT=""

TEST_DECOMPILE="./src/main/bin/decac"
EXTENSION_TEST_DECOMPILE="synt"
OUTPUT_DIR_TEST_DECOMPILE="./src/test/results/deca/decompile/"
OPTIONS_DECOMPILE="-p"

EXECUTABLES="$TEST_LEX $TEST_SYNT $TEST_DECOMPILE"


get_output_dir() {
    case $1 in
        $TEST_LEX)
            echo $OUTPUT_DIR_TEST_LEX
            ;;
        $TEST_SYNT)
            echo $OUTPUT_DIR_TEST_SYNT
            ;;
        $TEST_DECOMPILE)
            echo $OUTPUT_DIR_TEST_DECOMPILE
            ;;
        *)
            echo "Erreur : répertoire de sortie non trouvé pour $1."
            exit 1
            ;;
    esac
}

get_extension() {
    case $1 in
        $TEST_LEX)
            echo $EXTENSION_TEST_LEX
            ;;
        $TEST_SYNT)
            echo $EXTENSION_TEST_SYNT
            ;;
        $TEST_DECOMPILE)
            echo $EXTENSION_TEST_DECOMPILE
            ;;
        *)
            echo "Erreur : extension non trouvée pour $1."
            exit 1
            ;;
    esac
}

get_options() {
    case $1 in
        $TEST_LEX)
            echo $OPTIONS_TEST_LEX
            ;;
        $TEST_SYNT)
            echo $OPTIONS_TEST_SYNT
            ;;
        $TEST_DECOMPILE)
            echo $OPTIONS_DECOMPILE
            ;;
        *)
            echo "Erreur : options non trouvées pour $1."
            exit 1
            ;;
    esac
}
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
    extension=$(get_extension $executable)

    for file in "$input_dir"*.deca
    do
        if [ -f "$file" ]; then
            echo "Traitement du fichier : $file"
            output_file="$(get_output_dir $executable)/$(basename "${file%.deca}").$extension"
            tmp_file=$TMP_DIR$(basename "${file%.deca}").$extension
            $executable $(get_options $executable) "$file" > "$tmp_file"

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
}
main
