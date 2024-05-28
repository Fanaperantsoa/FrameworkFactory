@echo off

:: definition de variable Working directory initialisee a argument 1 de la commande script.bat
set "racine=%~1"
:: definition de variable Nom du projet initialisee a argument 2 de la commande script.bat
set "projet=%~2"



if exist "%racine%\out" (
    rmdir /s /q "%racine%\out"
    echo "Le dossier out dans le dossier temp du  deploiement a deja existe et vient d'etre supprimee"
)

if exist "%racine%\classes" (
    rmdir /s /q "%racine%\classes"
    echo "Le dossier classes dans le dossier temp du  deploiement a deja existe et vient d'etre supprimee"
)




:: POUR STOCKER TEMPORAIREMENT LA GLOBALITE DE TOUS LES .java DANS LE DOSSIER ET SOUS-DOSSIERS DU src
mkdir "%racine%\out"
:: POUR CONTENIR LES RESULTATS ISSU DE LA COMPILATION DE TOUS LES FICHIERS DANS LE out
mkdir "%racine%\classes"


:: IL EST NECESSAIRE D'UTILISER UNE BOUCLE ICI POUR ATTEINDRE TOUS LES FICHIERS .java POUR COPIER CHACUN D'EUX UN A UN DANS out
for /r "%racine%\src" %%f in (*.java) do copy "%%f" "%racine%\out"



:: ON COMPILE TOUS LES .java FRAICHEMENT COPIES DANS out
javac -cp "%racine%\lib\*" -d "%racine%\classes" "%racine%\out\*.java"
:: -cp veut dire classpath

:: ON ARCHIVE EN .jar LE DOSSIER mg AVEC SES SOUS-DOSSIERS
jar cvf "%racine%\lib\%projet%.jar" -C "%racine%\classes" .

:: ON SUPPRIME LE DOSSIER out QUI CONTENAIT LES FICHIERS .java A COMPILER
if exist "%racine%\out" (
    rmdir /s /q "%racine%\out"
)

:: ET VOILA !!



