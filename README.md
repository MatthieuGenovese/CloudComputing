### 1. Lancer le projet

Ouvrez un shell google SDK, et exécutez les commandes :  
-gcloud config set project sacc-liechtensteger-182811  
-gcloud app create  
-gcloud app deploy  

Puis dans votre ide :  
Créez une configuration maven : appengine:update  



 ## Architecture
 
 ![pdf architecture du projet](/image/SACC.pdf)
 
Comme vous pouvez le voir dans ce shéma , le client effectue une requête sur notre service de demandes. Il précise le Nom ,et la durée de sa vidéo. Le service effectue une vérification sur la base de données et lui attribue des permissions sur le nombre de vidéo qu' il peut soumettre suivant son abonnement (GOLD , SILVER ,BRONZE).

Si la demande est acceptée (si il est sur la BD ) alors on effectue une requête sur la queue Bronze ou nos deux queues Silver et Gold.
Les tâches sont traitées par notre partie convertisseur puis un événement est envoyé au service qui s’occupe de l’envoie de mails pour notifier l’utilisateur.

Choix de conception :

Nous avons choisi de mettre 1 queue push pour les bronzes car la queue bronze repose sur le fait que tous les utilisateurs sont traités les uns après les autres, dans l’ordre de leur arrivée.

Nous avons choisi de mettre 2 queues pull pour les silvers et golds. Ce choix  repose sur le fait qu'on est capable de choisir qu'elle est la requete qu'on souhaite réaliser pour chaque utilisateur. Nous avons choisi de mélanger les Silvers et Golds dans ces 2 queues afin d'utilisé un maximum les pull queues dans le cas ou il n'y a aucun Golds ou aucun Silver


 ## Elasticité
 
 Nous allons mettre en place une élasticité horizontale, par augmentation du nombre de serveurs . Cela ne pose généralement pas de problème pour les infrastructures Web.

La difficulté se concentre davantage sur le code applicatif. « Il est possible d'allouer un process Java à un thread que l’on peut exécuter sur un cœur virtuel, mais pour répartir vraiment l'exécution d'une application complète sur plusieurs serveurs virtuels, cela reste compliqué ». Heuresement google est notre amis et gère tous cela avec un code qui est mis à notre disposition : 

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
 
 Dans cette exemple on peut voir qu'on a au minimum 5 server qui tournent et au maximum un scaling qui peut aller jusqu'a 10 server.
 
On concidère que 60% sont des bronzes 30% sont des silvers et 10 % sont des golds. donc prenons le cas critique qui est 60 % des gens prennent 1 video 30 % des gens font 3 vidéo en simutané et 10 % mettent 5 vidéo en simutané : se qui nous fait une moyenne de 2 vidéo en simutané. Arbitrairement une vidéo fait 5 minutes , et comme en moyenne une conversion fait 1.8 fois plus que la durée de la vidéo source cela nous donne : 9 minutes. Pour ne pas faire attendre la personne il nous faut de manière général 2 fois plus d'intence que de personne.

Nous avons choisi d'avoir 5 intences afain de pouvoir gérer les 5 requetes que peut faire le gold dans 5 serveur différent cela nous permet en autre avoir un temps de réponse minimum pour ce cas précis. Nous avons choisi de d'avoir un scaling qui peut doubler notre charge : 5 de plus , soit 10 instances au maximum.

 
 ## Calcule du coup
  
Nous avons de base de 5 instances, qui nous coûtent au total 139$/mois. Lorsque trop d’utilisateur silver et gold  surchargent une instance, on scale et on ajoute une machine pour supporter la charge. On a un scaling qui peut au pire des cas peut doubler soit 278$/mois.

On concidère qu'au minimum nous avons 50 utilisateurs , on souhaite etre rentable à partir de ce nombre minimum estimé donc cela nous donne : 4.99$/mois pour les bronzes , 9.99$/mois pour les silvers , 14.99$/mois pour les golds.
  
  



