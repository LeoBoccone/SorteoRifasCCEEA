#/bin/bash
echo '\033]2;'SORTEO RIFAS'\007'

rm -rf output.txt generados
java -jar SorteoRifasCCEEA-1.0-SNAPSHOT.jar
