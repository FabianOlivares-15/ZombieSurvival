package puppy.code;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class MovimientoErratico implements MovimientoZombie {

    private Vector2 direccion = new Vector2(1, 0);
    private float tiempoCambio = 0;

    @Override
    public void mover(Vector2 posicion, Vector2 objetivo,
                      float velocidad, float delta) {

        // Reducir el tiempo hasta el siguiente cambio
        tiempoCambio -= delta;

        // Elegir otra direccion cada cierto tiempo
        if (tiempoCambio <= 0) {

            float angulo = MathUtils.random(0f, 360f);

            direccion.set(
                MathUtils.cosDeg(angulo),
                MathUtils.sinDeg(angulo)
            );

            // Cambiar de direccion cada 1 a 2 segundos
            tiempoCambio = MathUtils.random(1f, 2f);
        }

        // Calcular el desplazamiento
        Vector2 desplazamiento = new Vector2(direccion);
        desplazamiento.scl(velocidad * delta);

        // Mover al zombi
        posicion.add(desplazamiento);
    }
}

