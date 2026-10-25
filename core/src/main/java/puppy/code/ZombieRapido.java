package puppy.code;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class ZombieRapido extends Zombie {

    private boolean persiguiendo = true;

    public ZombieRapido(float x, float y, Texture textura) {

        // El zombi rapido comienza persiguiendo al jugador
        super(x, y, textura, 180f, new PersecucionDirecta());

    }

    @Override
    protected void comportamientoEspecial(float delta, Vector2 jugador) {

        // Calcular la distancia entre el zombi y el jugador
        float distancia = getPosicion().dst(jugador);

        // Si el jugador esta muy lejos, dejar de perseguirlo
        if (distancia > 500f && persiguiendo) {

            setEstrategiaMovimiento(new MovimientoErratico());

            persiguiendo = false;
        }

        // Si el jugador vuelve a acercarse, perseguirlo
        if (distancia < 420f && !persiguiendo) {

            setEstrategiaMovimiento(new PersecucionDirecta());

            persiguiendo = true;
        }
    }
}
