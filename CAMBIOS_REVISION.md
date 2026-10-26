# Revisión de Zombie Survival — paquete corregido

## Archivo fuente de la revisión

Se revisó el ZIP de GitHub `ZombieSurvival-main.zip` entregado durante la conversación.

## Errores y restos de SpaceNav corregidos

1. **Asteroides obsoletos:** retiramos de `PantallaJuego.java` sus dos listas, actualización, colisiones, puntaje y parámetros. `Ball2.java` deja de formar parte del proyecto.
2. **Cambio de pantalla:** `PantallaJuego.render()` termina con `batch.end()` antes de cambiar a Game Over y libera después los recursos de la pantalla anterior.
3. **Balas:** `Bullet.update(float delta)` calcula avance en píxeles por segundo y elimina balas destruidas con un recorrido inverso, sin saltar elementos.
4. **Colisiones:** una bala puede eliminar como máximo un zombi. Zombis eliminados ya no actualizan ni causan daño.
5. **Protección:** tres segundos temporizados con `delta`, permitiendo caminar y disparar. El jugador parpadea sin bloquearse.
6. **Recursos:** se liberan texturas/sonidos del personaje, textura compartida de zombis, música de fondo y sonido de explosión al salir de la partida.
7. **Menú y Game Over:** textos centrados, instrucciones de controles y retorno al menú al presionar Esc.
8. **Encabezado:** muestra vidas, oleada, puntaje, zombis restantes y el contador entre oleadas.
9. **Patrones:** permanecen Singleton, Strategy, Template Method y Builder integrados. `Zombie.actualizar()` prioriza el cambio de estrategia antes de realizar el movimiento del mismo fotograma.
10. **Compatibilidad:** se mantienen JDK 11 en Gradle, el nombre interno de las tareas Eclipse y todos los archivos de assets originales necesarios.

## Archivos Java modificados

- `PantallaJuego.java`
- `Nave4.java`
- `Bullet.java`
- `PantallaMenu.java`
- `PantallaGameOver.java`
- `Zombie.java`
- `Oleada.java`
- `SpaceNavigation.java`

## Archivo Java eliminado

- `Ball2.java` (ya no se emplea). **Si utilizas el ZIP de parches para sobrescribir tu repositorio, borra este archivo manualmente de `core/src/main/java/puppy/code/`.**

## Validación y límites

- El código del módulo `core` se verificó sintácticamente con `javac --release 11` usando *stubs* locales mínimos de la API LibGDX. Esta comprobación **no reemplaza** una compilación real contra la librería ni una ejecución del juego.
- El entorno de revisión no pudo descargar Gradle 8.10.1 ni las dependencias de LibGDX (sin acceso a servicios.gradle.org). Para verificar de forma definitiva, en el equipo del estudiante ejecuta `gradlew.bat :lwjgl3:run` con Oracle JDK 11 y prueba oleadas, choques, reinicio y Game Over.
- Siguen pendientes los gráficos definitivos, UML, memoria GM-1 a GM-10 y validación final por el estudiante.

## Aplicar correcciones en repositorio Git existente

1. Antes de reemplazar archivos: `git status` y respaldo/copia de la carpeta.
2. Extraer el ZIP de **archivos modificados** dentro de `C:\Proyecto Java\ZombieSurvival` conservando sus subcarpetas, permitiendo sobrescribir los archivos `.java`.
3. Eliminar manualmente `core/src/main/java/puppy/code/Ball2.java`.
4. Eclipse: Gradle Refresh / refrescar proyectos, guardar y ejecutar la tarea `lwjgl3:run`.
5. Revisar las pruebas y luego ejecutar `git add .`, `git commit -m "Refactorizar Zombie Survival y eliminar asteroides"`, `git push`.
