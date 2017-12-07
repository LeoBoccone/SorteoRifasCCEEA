# SorteoRifasCCEEA
Programa para ejecutar un sorteo de números de rifas para Ciencias Económicas - UdelaR - Uruguay

La comisión BD abrió un formulario en el SG en donde cada integrante del GV (titulares y acompañantes) podía pedir 5 números, terminaciones o una mezcla de ambos.
Ej.:
- 45123
- 60314
- *94 (cualquier número terminado en 94)
- *750 (cualquier número terminado en 750)
- 1234

Una vez cerrado el formulario, la comisión bajaba los resultados en un Excel, depuraba los campos que contestaron mal (ejemplo los que ponían: "N/A", "ninguno", "terminado en 40", 2912345, etc).
En caso de colisión (que dos personas pidieran el mismo número), se les daba prioridad al que contestó antes el formulario.

Y con ese listado, el programa que hice en Java asignaba los números pedidos y luego sorteaba aleatoriamente los números que quedaron libres hasta que cada integrante tuviera 100 números asignados.
El programa generaba "un archivo de Excel" (en verdad, un .csv) que luego se cargaba desde el SG y le quedaban asignados los números a cada integrante.

A partir de ahí, arrancaba el intercambio de rifas entre integrantes por medio del SG.

Después hubo otras instancias de pedidos de más rifas, pero ahí ya no se podían elegir números en particular.


El programa:
Lo hice en Java (porque me quedaba cómodo. Podría haberse construido en otro lenguaje), en los tiempos que pude y hace un poco lo que te conté más arriba.
No precisan ser programadores ni tener grandes conocimientos para usarlo tal cuál está (sólo precisan tener instalado la JRE o JDK, lo cuál es bastante fácil). Obvio que si precisan conocimientos para modificarlo o agregarle funcionalidades, etc.
