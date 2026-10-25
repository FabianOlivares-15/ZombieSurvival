package puppy.code;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class ZombieNormal extends Zombie {

    private boolean persiguiendo = false;

    public ZombieNormal(float x, float y, Texture textura) {

        super(x, y, textura, 100f, new MovimientoErratico());

    }

    @Override
    protected void comportamientoEspecial(float delta, Vector2 jugador) {

        // Calcular distancia entre el zombi y el jugador
        float distancia = getPosicion().dst(jugador);

        // Si el jugador se acerca, comenzar a perseguirlo
        if (distancia < 220f && !persiguiendo) {

            setEstrategiaMovimiento(new PersecucionDirecta());

            persiguiendo = true;
        }

        // Si el jugador se aleja, volver a caminar erraticamente
        if (distancia > 260f && persiguiendo) {

            setEstrategiaMovimiento(new MovimientoErratico());

            persiguiendo = false;
        }
    }
}
