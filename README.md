# LEARN YOUR SMASH - Une application de Raphaël et Aleksandar

## Présentation de l'application

Dans le cadre du module IDV-WEB4 à l'ETNA, nous avons développé un application de consultation de personnage du jeu-vidéo Super Smash Bros Ultimate communautaire.

Le front de l'application est développé en ReactJS. Le back est en JAVA (SpringBoot).

<br>

## Comment lancer l'application ?

**npm start** dans le dossier front_web4 => l'application devrait s'ouvrir dans le navigateur, sinon entrez le lien de la homepage sur le navigateur : 'localhost:3000'

NB : il faut pour cela avoir installé npm et Node JS au préalable :

- npm - https://docs.npmjs.com/downloading-and-installing-node-js-and-npm (version : 9.5.1)

- NodeJS - https://nodejs.org/en/download (version : v18.16.0) 

<br>

Lancer également le back-end via SpringBoot : **./mvnw spring-bott:run** dans le dossier quest_web_java.  

Pour cela, il faut avoir Java 11 d'installé sur sa machine.


## Fonctionnalitées :

### Home page : *localhost:3000/* <br>

### CRUD

L'application gère un système de CRUD, il est possible de créer un compte, se connecter, supprimer des users, de modifier le role si on est admin, et supprimer des users.

<br>

### Consulting de personnages

L'utilisateur a la possibilité de consulter les statistiques des personnages de smash bros, voir des vidéos guides et également poster des commentaires.

<br>

### Les commentaires

L'utilisateur à la possibilité de poster des commentaires sur les pages des personnages pour donner son avis (qui intéressera enormement de gens j'en suis sur). Il est également possible de supprimer ses propres commentaires.
