package puppy.code;

import com.badlogic.gdx.math.Vector2;

public class PersecucionDirecta implements MovimientoZombie {

    @Override
    public void mover(Vector2 posicion, Vector2 objetivo,
                      float velocidad, float delta) {

        // Calcular la direccion desde el zombi al jugador
        Vector2 direccion = new Vector2(objetivo).sub(posicion);

        // Si ya esta en la posicion del jugador, no moverse
        if (direccion.isZero()) {
            return;
        }

        // Convertir la direccion en un vector de longitud 1
        direccion.nor();

        // Calcular cuanto debe avanzar
        direccion.scl(velocidad * delta);

        // Actualizar la posicion del zombi
        posicion.add(direccion);
    }
}

