package puppy.code;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

/** Zombi veloz: prioriza la persecucion y acelera al acercarse. */
public class ZombieRapido extends Zombie {

    private boolean persiguiendo = true;

    public ZombieRapido(float x, float y, Texture textura) {
        super(x, y, textura, 180f, new PersecucionDirecta());
    }

    /** Primer paso variable: persigue a mayor distancia que el zombi normal. */
    @Override
    protected void seleccionarEstrategia(Vector2 jugador) {
        float distancia = distanciaAlJugador(jugador);
        if (persiguiendo && distancia > 500f) {
            setEstrategiaMovimiento(new MovimientoErratico());
            persiguiendo = false;
        } else if (!persiguiendo && distancia < 420f) {
            setEstrategiaMovimiento(new PersecucionDirecta());
            persiguiendo = true;
        }
    }

    /** Segundo paso variable: reduce velocidad al deambular y acelera de cerca. */
    @Override
    protected float calcularVelocidad(Vector2 jugador) {
        if (!persiguiendo) return getVelocidadBase() * 0.75f;
        if (distanciaAlJugador(jugador) < 150f) return getVelocidadBase() * 1.20f;
        return getVelocidadBase();
    }
}
