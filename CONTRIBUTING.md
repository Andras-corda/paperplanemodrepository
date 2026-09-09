# Contribuer

## Prérequis
- JDK 17 (Temurin conseillé)
- IntelliJ IDEA ou VS Code (voir le guide)

## Workflow
1. `git switch -c feat/ma-fonctionnalite`
2. Coder + écrire la datagen associée
3. `./gradlew runData` puis `./gradlew build`
4. `./gradlew spotlessApply`
5. Commits en Conventional Commits
6. Pousser, ouvrir une PR, attendre la CI verte + 1 revue

## Fichiers générés
On committe `src/generated/resources/` (hors `.cache/`).
Toujours relancer `runData` avant de committer.