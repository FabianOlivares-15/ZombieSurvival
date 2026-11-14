package puppy.code;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

/** Zombi de desplazamiento moderado: patrulla y persigue al estar cerca. */
public class ZombieNormal extends Zombie {

    private boolean persiguiendo;

    public ZombieNormal(float x, float y, Texture textura) {
        super(x, y, textura, 100f, new MovimientoErratico());
    }

    /** Primer paso variable: cambia entre patrulla y persecucion. */
    @Override
    protected void seleccionarEstrategia(Vector2 jugador) {
        float distancia = distanciaAlJugador(jugador);
        if (!persiguiendo && distancia < 220f) {
            setEstrategiaMovimiento(new PersecucionDirecta());
            persiguiendo = true;
        } else if (persiguiendo && distancia > 260f) {
            setEstrategiaMovimiento(new MovimientoErratico());
            persiguiendo = false;
        }
    }

    /** Segundo paso variable: camina mas lento mientras patrulla. */
    @Override
    protected float calcularVelocidad(Vector2 jugador) {
        return persiguiendo ? getVelocidadBase() : getVelocidadBase() * 0.80f;
    }
}
