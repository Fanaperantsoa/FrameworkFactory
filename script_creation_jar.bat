@echo off

:: definition de variable Working directory initialisee a argument 1 de la commande script.bat
set "racine=%~1"
:: definition de variable Working directory initialisee a argument 2 de la commande script.bat
set "projet=%~2"



mkdir "%racine%\out"


mkdir "%racine%\classes"

for /r "%racine%\src" %%f in (*.java) do copy "%%f" "%racine%\out"



:: ON COMPILE TOUS LES .java 
javac -cp "%racine%\lib\*" -d "%racine%\classes" "%racine%\out\*.java"
:: -cp veut dire classpath

:: ON ARCHIVE EN .jar LE DOSSIER mg
jar cvf "%racine%\lib\%projet%.jar" -C "%racine%\classes" .

if exist "%racine%\out" (
    rmdir /s /q "%racine%\out"
    echo "Le dossier bin dans le dossier deploiement a deja existe et vient d'etre supprimee"
)

:: ET VOILA !!



