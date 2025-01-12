# Projet Génie Logiciel, Ensimag.
gl12, 01/01/2025.

## 🚀 Lancer le Compilateur Deca

Pour lancer le compilateur **Deca**, utilisez la commande suivante dans votre terminal :  

```bash
./src/main/bin/decac [fichier-à-compiler]
```
**ℹ️ Note :**  
Pour afficher l'usage du compilateur, exécutez la commande suivante sans argument :  
```bash
./src/main/bin/decac
```

---

## 🧪 Tester les différentes étapes

### **Test du Lexer (Analyse Lexicale) :**  
Pour tester l'analyse lexicale :  
```bash
./src/test/script/launchers/test_lex [fichier-à-tester]
```
### **Test de la Syntaxe (Analyse Syntaxique) :**  
Pour tester l'analyse syntaxique :
```bash
./src/test/script/launchers/test_synt [fichier-à-tester]
```
### **Test du Contexte (Analyse Sémantique) :**
Pour tester l'analyse du contexte :
```bash
./src/test/script/launchers/test_context [fichier-à-tester]
```

---

## 🧪 Tester avec les scripts

Les scripts de tests sont disponibles dans le dossier `src/test/script`.
Les fichiers utilisés par les scripts sont dans le dossier `src/test/deca` et les résultats attendus sont dans le dossier `src/test/results`.

---

## 🎨 Formattage

Pour formater le code et passer la CI, utilisez la commande suivante :  
```bash
mvn spotless:apply 
```