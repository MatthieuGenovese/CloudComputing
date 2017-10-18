### 1. Lancer le projet

Ouvrez un shell google SDK, et exécutez les commandes :  
-gcloud config set project sacc-liechtensteger-182811  
-gcloud app create  
-gcloud app deploy  

Puis dans votre ide :  
Créez une configuration maven : appengine:update  



 ## Architecture
 
 ![pdf architecture du projet](/image/SACC.pdf)
 
 
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
    <max-idle-instances>automatic</max-idle-instances>
    <!-- ‘automatic’ is the default value. -->
    <min-pending-latency>30ms</min-pending-latency>
    <max-pending-latency>automatic</max-pending-latency>
    <max-concurrent-requests>50</max-concurrent-requests>
  </automatic-scaling>
</appengine-web-app>
 ```
  ## Calcule du coup
  
  Cela va dépendre du nombre de personne qu'on souhaite atteindre , prenons par exemple 100 bronze 50 silver et 25 gold et calculons le coup à partir de se cas utilisation.
