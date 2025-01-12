# Critique et Résumé de l’Organisation Adoptée pour le Projet de Compilateur Deca

## Historique du Projet et Ordre des Activités

Le projet de développement du compilateur Deca a été organisé en suivant une approche méthodique et structurée, adaptée à la complexité des tâches à réaliser. Dès le départ, l’équipe a décidé d’adopter une organisation collaborative basée sur l’utilisation des issues et des merge requests dans GitLab. Ces outils ont permis de diviser les tâches, d’assurer un suivi rigoureux de l’avancement, et de maintenir la qualité du code tout au long du projet.

La première phase, correspondant au sprint "Hello World", a été principalement consacrée à la préparation et à l'analyse des documents de référence. Pendant les trois premiers jours, nous avons étudié les vidéos explicatives, les polycopiés, et les ressources de cours TD. Cette étape, essentielle pour comprendre les objectifs et le fonctionnement du compilateur, s’est avérée plus longue que prévu en raison de la densité des documents. Ce retard initial a décalé légèrement la phase de développement.

Après cette phase d'analyse, l'équipe s'est divisée en trois groupes pour travailler simultanément sur plusieurs aspects du projet :
- Un groupe dédié à la partie A du compilateur.
- Un second groupe focalisé sur la partie B.
- Un dernier groupe chargé de configurer GitLab (intégration des pipelines pour des tests automatiques) et de travailler sur la partie C.

Cependant, cette répartition stricte n’a pas été totalement respectée en pratique. Les interdépendances techniques entre les parties A, B et C, ainsi que les disparités de compétences au sein de l’équipe, ont nécessité des ajustements fréquents. Certains membres ont dû contribuer à d'autres parties que celles qui leur étaient initialement attribuées.

Durant les vacances scolaires, malgré la distance, nous avons maintenu un rythme de travail régulier grâce à différents outils de communication comme Discord et l’ouverture d’un GitLab secondaire. Ces outils ont permis de partager les avancées, d’organiser des discussions techniques, et de résoudre les problèmes rapidement. Ces efforts ont permis d’achever la majorité des fonctionnalités du décompilateur "Hello World" avant la fin des vacances.

Après les vacances, l’équipe a entamé le sprint suivant, intitulé "Sans Objet", et a défini l’architecture de l’extension ARM. Cependant, cette phase a été marquée par des contretemps, notamment liés au changement de GitLab (passage du GitLab classique à celui de l’Ensimag). Une anticipation de ce changement aurait pu réduire le temps perdu dans la reconfiguration des outils.

## Description Critique de l’Organisation Adoptée

### Points Forts

L’organisation adoptée a reposé sur des principes de gestion de projet modernes, inspirés des méthodologies agiles. L’utilisation des issues dans GitLab a joué un rôle central dans le suivi des tâches et la coordination des efforts. Cela a permis à l’équipe d’avoir une vue d’ensemble claire sur l’état d’avancement, de prioriser les tâches en retard, et de réorienter les efforts en fonction des besoins.

Les merge requests ont été un autre pilier essentiel de notre organisation. Cette méthode a permis de maintenir une qualité de code élevée en instaurant un processus de validation rigoureux avant l’intégration des modifications. Grâce à ce mécanisme, nous avons évité de nombreux bugs potentiels qui auraient pu compromettre la stabilité du projet.

Enfin, l’intégration de pipelines dans GitLab pour effectuer des tests automatiques a été une excellente initiative. Chaque push dans le dépôt principal était soumis à des tests de vérification, garantissant le bon fonctionnement du code. Cette automatisation a considérablement réduit le temps nécessaire pour détecter les erreurs, augmentant ainsi la fiabilité du code produit.

### Points Faibles

Malgré ces réussites, plusieurs aspects de l’organisation auraient pu être améliorés. La division initiale des tâches en trois équipes a montré ses limites. La nature interdépendante des parties A, B, et C a rendu difficile le travail en silo. Par exemple, certaines connaissances acquises dans une partie étaient nécessaires pour avancer sur une autre. Cela a conduit à une redirection imprévue des efforts, ralentissant parfois la progression.

De plus, le principe consistant à attribuer les tests à une personne différente de celle qui avait écrit le code a entraîné des problèmes de coordination. Bien que cette approche ait permis de repérer des erreurs de manière indépendante, elle a également causé des retards. La personne en charge des tests n’était pas toujours au fait des derniers changements dans le code, ce qui compliquait la création des tests appropriés.

Un autre défi majeur a été le changement de GitLab en milieu de projet. La transition entre le GitLab classique et celui de l’Ensimag a nécessité du temps pour reconfigurer les outils et ajuster les pipelines. Une meilleure anticipation de ce changement aurait permis de minimiser ces perturbations.

Enfin, la documentation du code, bien qu’elle ait été intégrée à notre flux de travail, a souffert de la pression des délais. En travaillant simultanément sur le codage et la documentation, nous avons parfois négligé la qualité et la précision des descriptions techniques.

### Enjeux Communicationnels

La distance géographique pendant les vacances scolaires a constitué un défi supplémentaire. Bien que les outils de communication aient aidé à maintenir la collaboration, certaines discussions techniques complexes étaient plus difficiles à mener à distance. Cela a parfois conduit à des malentendus ou à des retards dans la prise de décisions importantes.

## Conclusion et Recommandations

En résumé, l’organisation adoptée pour le projet de compilateur Deca a permis d’atteindre des résultats satisfaisants tout en limitant les bugs grâce à des pratiques solides comme l’utilisation des merge requests et des pipelines. Cependant, plusieurs lacunes, telles que les difficultés de coordination et le manque d’anticipation pour certains obstacles, ont ralenti notre progression.

Pour de futurs projets, il serait bénéfique de :
- Anticiper les changements d’outils pour éviter des perturbations inutiles.
- Instaurer des points de synchronisation réguliers pour améliorer la coordination entre les différentes équipes.
- Simplifier la documentation en la centralisant et en dédiant des périodes spécifiques à cette tâche.