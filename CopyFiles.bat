@echo off
set /p v="Version Sorcicube: "
md "./copy"
cls
echo "Version Copy %v%"
copy ".\SorciCubeAPI\target\SorciCubeAPI-%v%-SNAPSHOT-built.jar" ".\copy\SorciCubeAPI.jar"
copy ".\SorciCubeApp\target\SorciCubeApp-%v%-SNAPSHOT-built.jar" .\copy\SorciCubeApp-%v%.jar"
copy ".\SorciCubeSpell\target\SorciCubeSpell-%v%-SNAPSHOT-built.jar" .\copy\SorciCubeSpell.jar"
pause>nul