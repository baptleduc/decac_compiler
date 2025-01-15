# Documentation de l'extension : ARM
## Spécification de l'extension ARM

### Objectifs
1. **Support de l'architecture ARM** : Permettre la génération de code efficace pour le processeur ARM, architecture la plus utilisée aujourd'hui.
2. **Double-cible de génération** :
   - **Machine abstraite (IMA)** : Utilisée comme étape intermédiaire pour validation et débogage.
   - **Processeur ARM Cortex-A7** : Cible finale pour tester l'exécution native.

### Pourquoi le Cortex-A7 ?
Le **Cortex-A7** est un microprocesseur 32 bits lancé en 2011 par ARM. Initialement utilisé dans les smartphones d'entrée de gamme, il a ensuite été adopté pour les plateformes IoT. Nous l'avons choisi comme cible matérielle pour exécuter nos programmes pour plusieurs raisons :
1. **Puissance et efficience énérgétique** : Le Cortex-A7 est un processeur ARM conforme à l'architecture **ARMv7-A**, utilisé dans des systèmes embarqués et des plateformes avec OS (Linux embarqué). Il offre un bon équilibre entre performances et est réputé pour son efficacité énergétique.
2. **Compatibilité avec des outils** : Contrairement au Cortex profile M, le Cortex-A7 peut exécuter des systèmes d'exploitation complets comme Linux, permettant une utilisation des syscall directement via `qemu-arm`
4. **Usage dans des applications modernes** : Le Cortex-A7 est utilisé dans des processeurs multi-cœurs modernes, tels que ceux intégrés aux plateformes Raspberry Pi (Raspberry Pi 2 par exemple) et aux SoC destinés à l'IoT.
5. **Support des optimisations avancées** : Son architecture prend en charge des optimisations spécifiques comme les instructions SIMD (NEON) pour les calculs parallèles optimisés et une gestion efficace de la mémoire grâce à une MMU (Memory Management Unit).

### Fonctionnalités attendues
1. Traduction des instructions de Deca en langage assembleur ARM.
2. Gestion des modes d'adressage ARM (registre direct, immédiat, déplacement...).
3. Support des opérations arithmétiques et logiques spécifiques à ARM.
4. Optimisation du code généré pour minimiser les instructions et la consommation énergétique.

### Défis techniques
1. **Double back-end** :
   - Adapter le générateur de code pour qu'il puisse produire du code pour deux cibles distinctes : la Machine Abstraite (IMA) et l'ARM Cortex-M0.
   - Concevoir l'architecture de manière à maximiser la réutilisation du code entre les deux cibles tout en permettant les optimisations spécifiques à ARM, afin de garantir un développement maintenable.
2. **Environnement d'exécution limité** :
   - Absence de librairies standard ARM pour les entrées/sorties et le débogage.
   - Conception et intégration d'un environnement minimaliste pour exécuter les programmes Deca sur ARM.

### Étapes de mise en œuvre
1. **Mise en place de l'environnement** :
   - Configurer un simulateur ou un émulateur ARM, comme **QEMU**, pour permettre l'exécution et le débogage des programmes Deca générés.
   - Finir la chaine de compilation via une étape d'assemblage et de linkage.
   - Automatiser les tests ARM à l'aide d'un pipeline CI/CD :
     - Configurer des scripts pour assembler, lier et exécuter automatiquement les programmes générés.
     - Intégrer la validation des programmes via le simulateur.
2. **Analyse préliminaire** :
   - Étudier la sémantique des instructions ARM et leur correspondance avec les instructions de Deca.
3. **Conception du back-end** :
   - Implémentation des classes nécessaires dans le package `fr.ensimag.deca.codegen` pour ARM.
   - Facteur commun avec le back-end IMA pour maximiser la réutilisation.
4. **Validation intermédiaire** :
   - Tests sur des programmes simples (arithmétiques, boucles, conditions) traduits vers IMA et ARM pour garantir la cohérence.
5. **Optimisation** :
   - Ajout d'optimisations spécifiques à ARM (ex. utilisation efficace des registres, instruction "multiply-accumulate").

### Méthode de validation
1. **Tests unitaires** :
   - Génération de code ARM pour une batterie de programmes Deca représentatifs.
   - Vérification de la cohérence entre les résultats obtenus sur ARM et sur IMA.
2. **Évaluation de la performance** :
   - Mesure de la consommation en cycles pour des programmes réels exécutés sur ARM.
3. **Comparaison avec des compilateurs existants** :
   - Comparer les performances du code généré par Deca à celles obtenues par GCC ou LLVM sur des tâches similaires.

### Documentation associée
- **Manuel utilisateur** : Instructions pour utiliser l'option de génération ARM dans la commande `decac`.
- **Analyse des performances** : Étude des impacts énergétiques et des optimisations possibles pour ARM.



## Environnement 

Pour développer et tester l'extension ARM, nous utiliserons les outils suivants :

1. **Cross-compilation avec arm-linux-gnueabihf-gcc** :
   - Le compilateur `arm-linux-gnueabihf-gcc` permet, grâce à son outil d'assemblage et de linkage, de générer du code binaire pour l'architecture ARM .
   - Installation sur une distribution Linux (ex. Ubuntu) :
     ```sh
     sudo apt-get install gcc-arm-linux-gnueabihf
     ```
   - Assemblage et linkage pour produire un exécutable à partir d'un fichier assembleur (.s) en entrée : 
     ```sh
     arm-linux-gnueabihf-gcc -S arm/hello_world arm/hello_world.s
     ```


2. **Installation de l'environnement d'execution QEMU** :
   - QEMU est un émulateur et virtualiseur qui permet d'exécuter des programmes compilés pour ARM sur une machine x86 munie de notre processeur cible : **cortex-a7**.
   - Installation de QEMU :
     ```sh
     sudo apt-get install qemu-user
     ```
   - Exécution d'un programme ARM avec QEMU :
     ```sh
     qemu-arm  -L /usr/arm-linux-gnueabihf -cpu cortex-a7 ./programme_arm
     ```