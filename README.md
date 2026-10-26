# Zombie Survival

**Proyecto de Programación Avanzada**  
**Tecnologías:** Java 11, LibGDX 1.12.1 y Gradle

## Descripción

Zombie Survival es un videojuego de supervivencia 2D en el que el jugador debe resistir oleadas de zombis, evitar el contacto con los enemigos y sumar puntos eliminándolos. Antes de comenzar la partida, se puede elegir entre cuatro personajes.

El proyecto parte de la estructura de SpaceNav2024 y adapta sus mecánicas a un juego de supervivencia con enemigos de distintos comportamientos y oleadas progresivas.

## Requisitos

- Oracle JDK 11.
- Eclipse IDE con soporte para Gradle (Buildship), o una terminal compatible.
- Windows para utilizar el lanzador de escritorio incluido.

## Ejecución

**Desde Eclipse**

1. Importar la carpeta raíz del proyecto como **Existing Gradle Project**.
2. Configurar el proyecto para utilizar **JDK 11**.
3. Abrir **Gradle Tasks > SpaceNav2024-lwjgl3 > application > run**.

**Desde CMD en Windows**

En la carpeta raíz del proyecto:

```cmd
set "JAVA_HOME=C:\Program Files\Java\jdk-11.0.32.1"
set "PATH=%JAVA_HOME%\bin;%PATH%"
gradlew.bat :lwjgl3:run
```

La ruta de `JAVA_HOME` debe ajustarse si JDK 11 está instalado en otra ubicación.

## Controles

| Acción | Control |
| --- | --- |
| Seleccionar personaje | Clic en la tarjeta o teclas 1, 2, 3 y 4 |
| Iniciar la partida | Botón **JUGAR** o Enter |
| Mover al superviviente | WASD o flechas de dirección |
| Disparar | Barra espaciadora |
| Reintentar tras Game Over | Enter, barra espaciadora o clic |
| Volver al menú desde Game Over | Escape |

Los disparos siguen la última dirección de movimiento del personaje, incluso cuando este se encuentra detenido.

## Mecánicas

- Selección de cuatro supervivientes.
- Movimiento en ocho direcciones y disparos direccionales.
- Dos tipos de zombis: normales y rápidos, con estrategias de movimiento distintas.
- Tres vidas iniciales y tres segundos de protección después de recibir daño.
- Diez puntos por cada zombi eliminado.
- Oleadas que comienzan automáticamente después de eliminar a todos los enemigos de la ronda anterior, con una espera de tres segundos.
- Generación de enemigos cerca de los bordes y a una distancia segura del jugador, cuando el espacio disponible lo permite.
- Pantallas de inicio y Game Over, con reinicio de partida.

La composición de las oleadas sigue las expresiones:

- Zombis normales: `1 + 2 * númeroDeOleada`.
- Zombis rápidos: `1 + númeroDeOleada`.

Por ejemplo, la primera oleada contiene cinco enemigos y la segunda contiene ocho.

## Arquitectura y patrones de diseño

| Patrón | Clases principales | Aplicación |
| --- | --- | --- |
| Singleton | `EstadoPartida` | Centraliza las vidas, el puntaje y el número de oleada. |
| Strategy | `MovimientoZombie`, `PersecucionDirecta`, `MovimientoErratico`, `Zombie` | Permite intercambiar algoritmos de movimiento en tiempo de ejecución. |
| Template Method | `Zombie`, `ZombieNormal`, `ZombieRapido` | Define la secuencia de actualización de los zombis y permite personalizar su comportamiento. |
| Builder | `Oleada`, `OleadaBuilder`, `OleadaConcretaBuilder`, `DirectorOleadas` | Construye oleadas con cantidades variables de enemigos. |

La clase abstracta `Zombie` es extendida por `ZombieNormal` y `ZombieRapido`. La interfaz `MovimientoZombie` cuenta con implementaciones diferentes, y la lógica del juego utiliza polimorfismo para gestionar las entidades.

## Organización del proyecto

| Ruta | Contenido |
| --- | --- |
| `core/src/main/java/puppy/code/` | Lógica del juego, pantallas, entidades y patrones de diseño. |
| `lwjgl3/` | Lanzador de escritorio. |
| `assets/` | Imágenes, fondos, efectos de sonido y música. |
| `gradle/`, `gradlew`, `gradlew.bat` | Herramientas y configuración del entorno de compilación. |

Los nombres internos `SpaceNavigation` y `SpaceNav2024` corresponden a la estructura heredada del proyecto base. El nombre visible del videojuego es **Zombie Survival**.
