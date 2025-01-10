# Documents à rendre

## 1. Le manuel utilisateur
**Description** : pour un utilisateur du compilateur.
- Les limitations spécifiques de l’implémentation (par exemple, les portions du langages non ou mal implémentées).
- Les messages d’erreur retourné à l'utilisateur et leurs causes possibles.
- Les modes opératoires et limitations des extensions.
- Les limitations des extentions

**Format attendu** : PDF à déposer sous `docs/Manuel-Utilisateur.pdf`.

---

## 2. Une documentation sur l’implémentation : la documentation de conception
**Description** : pour des développeurs.
- Liste des classes et leurs dépendances (architecture).
- Spécifications sur le code du compilateur autres que celles fournies et leurs justifications.
- Algorithmes et structures de données non fournis dans les documents initiaux.

---

## 3. Une documentation sur la validation
**Description** : Comment la validation a été réalisée.
- Descriptif des tests
    - types de tests pour chaque étape/passe (tests unitaires, tests système, . . .)
    - organisation des tests
    - objectifs des tests, comment ces objectifs ont été atteints.
- Les scripts de tests
    - comment faire passer tous les tests
- Gestion des risques et gestion des rendus (cf. section 2)
- Résultats de Jacoco
- Méthodes de validation utilisées autres que le test

---

## 4. Une documentation sur les extensions
**Description** : 20 à 30 pages couvrant :
- Spécification de l’extension.
- Analyse bibliographique.
- Choix d’architecture et algorithmes.
- Méthode de validation
- Résultats obtenues 

---

## 5. Un document présentant votre analyse de l’impact énergétique de votre projet
**Description** : Analyse de 4 à 10 pages abordant deux axes :
1. **Efficience du code produit** : Évaluer le coût énergétique des instructions générées (top ou powertop).
2. **Efficience du procédé de fabrication** : Réduire l’impact des processus de compilation et tests.

**Éléments attendus** :
- moyens mis en œuvre pour évaluer la consommation énergétique de votre projet;
- discussion de l’impact de vos choix de compilation de Deca vers ima ;
- discussion de votre processus de validation, et stratégie mise en œuvre pour en diminuer l’impact énergétique sans nuire à l’effort de validation et à la qualité du compilateur ;
- prise en compte de l’impact énergétique dans votre extension

---

## 6. Un bilan sur la gestion d’équipe et de projet (fait)
**Description** : Document critiquant et résumant l’organisation adoptée, avec :
- Historique du projet ordre choisi pour la conception et le développement des
étapes B et C, temps passé sur les différentes activités (analyse, conception, codage, validation,
documentation)
- Description critique de l'organisation adopté par l'équipe

**Format attendu** : PDF sous `docs/Bilan_Projet.pdf`.

---

## Liste des produits à rendre
- Les sources Java du compilateur
- Votre base de tests et vos scripts de tests 
- planning/Planning.pdf : planning prévisionnel
- planning/Realisation.pdf : temps passé effectivement
- docs/Bilan_Equipe.pdf : bilan de gestion d’équipe et de projet (cf. section 1.6)
- docs/Manuel-Utilisateur.pdf : manuel utilisateu

**ATTENTION** : il est impératif que votre compilateur passe tous les tests du script common-tests.sh fourni.