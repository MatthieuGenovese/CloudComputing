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


# mettre une partie du code que tu m'a montré sur skype matthieu et l'expliquer (je le mettrai moi meme se soir au pire et je l'expliquerai à moin quelqun d'autre soit chaud)


  ## Calcule du coup
  
  Cela va dépendre du nombre de personne qu'on souhaite atteindre , prenons par exemple 100 bronze 50 silver et 25 gold et calculons le coup à partir de se cas utilisation.
