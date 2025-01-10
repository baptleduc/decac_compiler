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
- les résultats attendus pour les tests de vérification contextuelle sont situés dans : ./src/test/results/deca/contex/
- Les fichiers temporaires sont stockés dans : ./src/test/results/tmp/

Ajout de fichiers de référence pour la comparaison :
- Pour `test_lex`, ajoutez un fichier `.lex` dans ./src/test/results/deca/syntax/lex/.
- Pour `test_synt`, ajoutez un fichier `.synt` dans ./src/test/results/deca/syntax/synt/.
- Pour `decac -p`, ajoutez un fichier `.synt` dans ./src/test/results/deca/decompile/.
- Pour `decac`, ajoutez un fichier `.deca` dans ./src/test/results/deca/codegen.

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
INPUT_DIR_CODEGEN="./src/test/deca/codegen/valid/"

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


NAME_TEST_DECOMPILE="decompile"
EXEC_DECOMPILE="./src/main/bin/decac"
EXTENSION_TEST_DECOMPILE="synt"
OUTPUT_DIR_TEST_DECOMPILE="./src/test/results/deca/decompile/"
OPTIONS_DECOMPILE="-p"

NAME_TEST_CODEGEN="codegen"
EXEC_CODEGEN="./src/main/bin/decac"
EXTENSION_TEST_CODEGEN="ass"
OUTPUT_DIR_TEST_CODEGEN="./src/test/results/deca/codegen/"
OPTIONS_TEST_CODEGEN=""

ALL_TESTS="$NAME_TEST_LEX $NAME_TEST_SYNT $NAME_TEST_DECOMPILE $NAME_TEST_CODEGEN"


get_output_dir() {
    case $1 in
        $NAME_TEST_LEX)
            echo $OUTPUT_DIR_TEST_LEX
            ;;
        $NAME_TEST_SYNT)
            echo $OUTPUT_DIR_TEST_SYNT
            ;;
        $NAME_TEST_DECOMPILE)
            echo $OUTPUT_DIR_TEST_DECOMPILE
            ;;
        $NAME_TEST_CODEGEN)
            echo $OUTPUT_DIR_TEST_CODEGEN
            ;;
        *)
            echo "Erreur : répertoire de sortie non trouvé pour $1."
            exit 1
            ;;
    esac
}

get_input_dir() {
    case $1 in
        $NAME_TEST_LEX)
            echo $INPUT_DIRS_SYNTAX
            ;;
        $NAME_TEST_SYNT)
            echo $INPUT_DIRS_SYNTAX
            ;;
        $NAME_TEST_DECOMPILE)
            echo $INPUT_DIR_DECOMPILE
            ;;
        $NAME_TEST_CODEGEN)
            echo $INPUT_DIR_CODEGEN
            ;;
        *)
            echo "Erreur : répertoire d'entré non trouvé pour $1."
            exit 1
            ;;
    esac
}

get_exec() {
    case $1 in
        $NAME_TEST_LEX)
            echo $EXEC_LEX
            ;;
        $NAME_TEST_SYNT)
            echo $EXEC_SYNT
            ;;
        $NAME_TEST_DECOMPILE)
            echo $EXEC_DECOMPILE
            ;;
        $NAME_TEST_CODEGEN)
            echo $EXEC_CODEGEN
            ;;
        *)
            echo "Erreur : exécutable non trouvé pour $1."
            exit 1
            ;;
    esac
}
get_extension() {
    case $1 in
        $NAME_TEST_LEX)
            echo $EXTENSION_TEST_LEX
            ;;
        $NAME_TEST_SYNT)
            echo $EXTENSION_TEST_SYNT
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
        $TEST_CODEGEN)
            echo $OPTIONS_TEST_CODEGEN
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
run_non_regression_tests() {
    
    input_dir=$1
    output_dir=$2
    options=$3
    executable=$4
    name_test=$5

    echo "input_dir : $input_dir"
    echo "output_dir : $output_dir"
    echo "executable : $executable"

    if [ ! -d "$output_dir" ]; then
        echo "Erreur : le répertoire de sortie $output_dir n'existe pas."
        exit 1
    fi

    if [ ! -d "$input_dir" ]; then
        echo "Erreur : le répertoire d'entrée $input_dir n'existe pas."
        exit 1
    fi
    # Détermine l'extension des fichiers de sortie (lex ou synt)
    extension=$(get_extension $name_test)

    for file in "$input_dir"*.deca
    do
        if [ -f "$file" ]; then
            echo "Traitement du fichier : $file"
            output_file="$output_dir$(basename "${file%.deca}").$extension"
            tmp_file=$TMP_DIR$(basename "${file%.deca}").$extension

            if [ "$name_test" = "$NAME_TEST_CODEGEN" ]; then
                $executable $option "$file"
                mv "$input_dir$(basename "${file%.deca}").$extension" "$tmp_file"
            else
                $executable $option "$file" > "$tmp_file"
            fi

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
    for test_name in $ALL_TESTS; do
        echo "[BEGIN] : $test_name"
            for input_dir in $(get_input_dir $test_name); do
                run_non_regression_tests "$input_dir" "$(get_output_dir $test_name)" "$(get_options $test_name)" "$(get_exec $test_name)" "$test_name"
            done
        echo "[SUCCESS] : $executable"
    done
}

main
