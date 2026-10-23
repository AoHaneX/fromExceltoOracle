# fromExceltoOracle

Module Java d'import de données réalisé en 2022 pendant mon stage de BTS SIO au **Département de Seine-Maritime** (service innovations, études et développements).

Il alimente et met à jour la base Oracle de l'application mobile **Sites et Musées 76** (Visite76) à partir des fichiers Excel remplis par les équipes des sites culturels du département.

## Contexte

- Stage du 23 mai au 25 juin 2022, délai cible de quatre semaines.
- Première expérience avec Java et Oracle, apprise en autonomie pendant le stage.
- Le module a permis d'alimenter l'application avant sa mise en ligne.

## Fonctionnement

```
Fichier .xlsx  →  module Java  →  base Oracle  →  application Sites et Musées 76
```

Pour chaque ligne du fichier, le module :

1. lit le point d'intérêt (POI) : titre, textes, parcours, tags et coordonnées ;
2. crée les tags et parcours qui n'existent pas encore pour le site ;
3. met à jour le POI s'il existe déjà, ou l'insère sinon ;
4. relie le POI à ses tags et à ses parcours (tables de liaison).

### Format du fichier attendu

La première ligne contient les en-têtes. La lecture s'arrête à la première ligne dont la première cellule est vide.

| Colonne | Contenu |
| --- | --- |
| 1 | Numéro |
| 2 | Titre du POI |
| 3 | Texte court |
| 4 | Texte long |
| 5 | Parcours, séparés par `;` |
| 6 | Tags, séparés par `;` |
| 7 | Coordonnées, au format `x,y` |

Un exemple est fourni dans `ProjetJava/src/main/resources/Data/`.

## Technologies

- Java, Maven
- Apache POI (lecture des fichiers .xlsx)
- JDBC avec requêtes paramétrées (`PreparedStatement`), base Oracle
- SLF4J / Logback pour les journaux

## Structure du code

| Fichier | Rôle |
| --- | --- |
| `Visite76.java` | Point d'entrée : connexion, choix du site et lancement de l'import |
| `fonctionXLSX.java` | Lecture du fichier et orchestration de l'import |
| `POI.java`, `Tag.java`, `Parcours.java`, `Site.java`, `carte.java` | Accès aux tables correspondantes |
| `OracleDriver.java` | Connexion à la base |

## Lancer le module

Le module nécessite un accès à la base Oracle de l'application, qui n'est pas publique. Le code est publié à titre de démonstration.

Les paramètres de connexion sont lus dans des variables d'environnement :

```
VISITE76_DB_URL=jdbc:oracle:thin:@<hote>:<port>/<service>
VISITE76_DB_USER=<utilisateur>
VISITE76_DB_PASSWORD=<mot de passe>
```

Le fichier à importer est passé en argument :

```
cd ProjetJava
mvn compile exec:java -Dexec.mainClass=org.cd76.visite76.import_.Visite76 -Dexec.args="chemin/vers/fichier.xlsx"
```

Le site ciblé et l'identifiant de carte sont définis dans `Visite76.java`.

## Captures

Captures de l'application alimentée par le module, dans [`Screenshots of the final product`](Screenshots%20of%20the%20final%20product) :

| Accueil | Sites | Carte |
| --- | --- | --- |
| ![Accueil](Screenshots%20of%20the%20final%20product/home.PNG) | ![Sites](Screenshots%20of%20the%20final%20product/sites.PNG) | ![Carte](Screenshots%20of%20the%20final%20product/map.PNG) |

## Remarques

- Ni les identifiants de connexion ni les données réelles des sites ne sont publiés.
- Le code est conservé dans son état de fin de stage. Avec le recul, j'externaliserais aussi le choix du site, j'ajouterais des transactions et des tests automatisés.
