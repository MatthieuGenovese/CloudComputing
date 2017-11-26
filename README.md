Author : Liechtensteger, Génovèse, Chennouf, Fezai

### 1. Lancer le projet

Ouvrez un shell google SDK, et exécutez les commandes :  
-gcloud config set project sacc-liechtensteger-182811  
-gcloud app create  
-gcloud app deploy  

Puis dans votre ide :  
Créez une configuration maven : appengine:update  



 ## Architecture
 
 ![pdf architecture du projet](/image/SACC.pdf)
 
Comme vous pouvez le voir dans ce schéma, le client effectue une requête sur notre service de demandes. Il précise le nom, et la durée de sa vidéo. Le service effectue une vérification sur la base de données et lui attribue des permissions sur le nombre de vidéo qu'il peut soumettre suivant son abonnement (GOLD, SILVER, BRONZE).
Si la demande est acceptée (s'il est sur la BD) alors on effectue une requête sur la queue Bronze ou nos deux queues Silver et Gold.
Les tâches sont traitées par notre partie convertisseur puis un événement est envoyé au service qui s’occupe de l’envoi de mails pour notifier l’utilisateur.

 ## Choix de conception 

Nous avons choisi de mettre 1 queue push pour les bronzes car la queue push repose sur le fait que tous les utilisateurs sont traités les uns après les autres, dans l’ordre de leur arrivée.

Nous avons choisi de mettre 2 queues pull pour les silvers et golds. Ce choix repose sur le fait qu'on est capable de choisir qu'elle que soit la requête , celui qu'on souhaite réaliser. Nous avons choisi de mélanger les Silver et Golds dans ces 2 queues afin d'utiliser un maximum les pulls queues dans le cas où il n'y a aucun Gold ou aucun Silver à un instant T.


 ## Elasticité
 
 Nous allons mettre en place une élasticité horizontale, par augmentation du nombre de serveurs . Cela ne pose généralement pas de problème pour les infrastructures Web.

La difficulté se concentre davantage sur le code applicatif. « Il est possible d'allouer un process Java à un thread que l’on peut exécuter sur un cœur virtuel, mais pour répartir vraiment l'exécution d'une application complète sur plusieurs serveurs virtuels, cela reste compliqué ». Heuresement google est notre ami et gère tout cela avec le code suivant : 

 ```xml
<appengine-web-app xmlns="http://appengine.google.com/ns/1.0">
  <application>simple-app</application>
  <module>default</module>
  <version>uno</version>
  <threadsafe>true</threadsafe>
  <instance-class>F2</instance-class>
  <automatic-scaling>
    <min-idle-instances>5</min-idle-instances>
    <!-- ‘automatic’ is the default value. -->
    <max-idle-instances>10</max-idle-instances>
    <!-- ‘automatic’ is the default value. -->
    <min-pending-latency>30ms</min-pending-latency>
    <max-pending-latency>automatic</max-pending-latency>
    <max-concurrent-requests>50</max-concurrent-requests>
  </automatic-scaling>
</appengine-web-app>
 ```
 
 On considère que 60% sont des bronzes 30%s sont des silvers et 10 % sont des golds. Prenons le cas critique qui est 60 % des gens prennent 1 vidéo  , 30 % des gens prennent 3 vidéos en simultané et 10 % prennent 5 vidéos en simultané . Cela  nous fait une moyenne de 2 vidéos en simultané. Arbitrairement une vidéo fait 5 minutes, et comme en moyenne une conversion fait 1.8 fois plus que la durée de la vidéo source cela nous donne : 9 minutes. Pour ne pas faire attendre la personne il nous faut de manière générale 2 fois plus d'instances que de personne.
 
Nous avons choisi d'avoir 5 instances afin de pouvoir gérer les 5 requêtes que peut faire le gold dans 5 serveurs différents cela nous permet en outre d'avoir un temps de réponse minimum pour ce cas précis. Nous avons choisi d'avoir un scaling qui peut doubler notre charge  soit 10 instances au maximum.

 
 ## Calcul du coût
  
Nous avons une base de 5 instances, qui nous coûtent au total 139$/mois. Lorsqu'un surplus d’utilisateur silver et gold surchargent une instance, on scale et on ajoute une machine pour supporter la charge. On a un scaling qui peut au pire des cas doubler soit 278$/mois.

On considère qu'au minimum nous avons 50 utilisateurs, on souhaite être rentable à partir de ce nombre minimum estimé, donc cela nous donne : 4.99$/mois pour les bronzes, 9.99$/mois pour les silvers, 14.99$/mois pour les golds

## Requetes implémentées

### Créer un utilisateur

Requete POST application/json à l'adresse http://sacc-liechtensteger-182811.appspot.com/createaccount  
contenu du json :  
```{ "username" : "francislebg", "email":"toto@gmail.com", "accountlevel" : "gold"} ```

### Poster une video

Requete POST application/json à l'adresse http://sacc-liechtensteger-182811.appspot.com/login  
contenu du json :   
```{ "username" : "francislebg", "video" : "gfgdfddfdssdfsgsdfsdfshgffghfghfghfffghfghfghfhdfgdfgdgfdfgddgfs", "length" : "40"}```


### Lister les videos courantes 

Requete POST application/json à l'adresse http://sacc-liechtensteger-182811.appspot.com/status
contenu du json :  
```{ "username" : "francislebg" }```
  
  



