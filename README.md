# FrameworkFactory
Le Framework s'appelle FrameworkFactory

## Description
Ceci est un projet de fabrique de framework Java en utilisant un FrontServlet comme FrontController

## Configuration
Pour utiliser ce framework, il est necessaire de bien parametrer son environnement de developpement.
Pour ce faire, suivez les consignes ci-apres :
  - Le nom du fichier .jar representant notre Framework est : Prom16_Framework.jar
  - Notre FrontController se trouve dans ce fichier jar, dans le package : mg.itu.prom16
  - Notre FrontController s'appelle : FrontServlet.class ; comme son nom l'indique : il s'agit d'un servlet.
  - Vous devez avoir une version de Java superieure a Java 19, nous avons utilise Java 20 pour ce Framework
  
  - Vous devrez :
        
        => specifier le nom du Servlet dans la balise <servlet-name> de l'element <servlet>
        
        => specifier la classe du Servlet avec ses packages dans la balise <servlet-class> de l'element <servlet>

        => specifier dans la balise <servlet-mapping> : "/" pour l'<url-pattern> afin que n'importe quel URL puisse etre gere par le Servlet (c'est-a-dire pour permettre a notre FrontServlet de gerer tous les URL et n'importe lequel)

        => definir <init-param> dans le servlet defini et de specifier le nom du package et sa valeur respectivement dans <param-name> et <param-value>

        => mettre dans une seule et meme package tous les Controller que vous voudrez creer
        
        => annoter avec l'annotation : @ControllerAnous toutes les classes que vous voudrez utiliser comme  Controller (si vous voulez specifier l'attribut ce sera : @ControllerAnous(valeur = "ce_que_vous_voulez_mettre"))


  - Il ne vous reste plus qu'a vous lancer : Alefaaaa !!!