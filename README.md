# Zombie Survival — Programación Avanzada

Videojuego 2D en Java 11 y LibGDX 1.12.1, adaptado a partir de SpaceNav2024.

## Cómo ejecutarlo en Windows

1. Tener instalado **Oracle JDK 11** (el JDK elegido para la asignatura).
2. Importar el proyecto Gradle existente en Eclipse, sin crear uno desde cero.
3. Abrir **Gradle Tasks → SpaceNav2024-lwjgl3 → application → run**.
4. Como alternativa, desde CMD, en la raíz del proyecto:

   ```cmd
   set "JAVA_HOME=C:\Program Files\Java\jdk-11.0.32.1"
   set "PATH=%JAVA_HOME%\bin;%PATH%"
   gradlew.bat :lwjgl3:run
   ```

   Si el JDK está en otra ruta, reemplazar `JAVA_HOME` por su instalación.

## Controles

- **WASD** o **flechas**: caminar en 8 direcciones y apuntar.
- **Espacio**: disparar en la última dirección del movimiento.
- **Menú**: cualquier tecla o clic para iniciar.
- **Game Over**: Enter o clic para reintentar; Escape para volver al menú.

## Reglas

- El jugador tiene 3 vidas y 3 segundos de invulnerabilidad tras recibir daño.
- Los zombis verdes patrullan y persiguen al acercarse; los rojos son más rápidos.
- Cada zombi eliminado da 10 puntos.
- Al terminar una oleada, la siguiente llega después de 3 segundos.
- Número de enemigos: oleada 1 = 3 normales + 2 rápidos; oleada N = (1 + 2N) normales + (1 + N) rápidos.
- Zombis nacen en los bordes, procurando mantenerse lejos del jugador.

## Patrones de diseño de la asignatura

| Patrón | Clases | Uso en el juego |
| --- | --- | --- |
| Singleton | `EstadoPartida` | Única instancia que centraliza vidas, puntaje y oleada |
| Strategy | `MovimientoZombie`, `PersecucionDirecta`, `MovimientoErratico`, `Zombie` | Intercambiar comportamiento de movimiento durante la partida |
| Template Method | `Zombie`, `ZombieNormal`, `ZombieRapido` | `Zombie.actualizar()` es `final` y ejecuta los pasos del algoritmo; las hijas implementan `comportamientoEspecial()` |
| Builder | `Oleada`, `OleadaBuilder`, `OleadaConcretaBuilder`, `DirectorOleadas` | Construcción de oleadas configuradas con múltiples tipos de enemigos |

**GM-4:** `Zombie` es clase abstracta con dos subclases concretas; `MovimientoZombie` es interfaz implementada por dos clases.

## Estructura del proyecto

- `core/`: lógica del juego y patrones de diseño.
- `lwjgl3/`: lanzador de escritorio LibGDX.
- `assets/`: sprites y sonidos.

Se conservaron los nombres internos históricos `SpaceNavigation` y `SpaceNav2024-core/-lwjgl3` para no romper la configuración de Eclipse/Gradle. El juego visible se llama Zombie Survival.

## Pendiente antes de la entrega final

- Reemplazar el sprite provisional `aGreyMedium4.png` de los zombis y `MainShip3.png` del jugador por arte definitivo.
- Preparar UML e informe GM-1 a GM-10 con referencias a métodos/líneas.
- Revisar que haya al menos 6 commits y ejecutar una prueba final con Oracle JDK 11.

Los cambios de esta revisión se describen en `CAMBIOS_REVISION.md`.
