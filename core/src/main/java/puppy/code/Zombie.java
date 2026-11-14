package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/** Comportamiento compartido de los enemigos y estructura de su actualizacion. */
public abstract class Zombie {

    private final Vector2 posicion;
    private final Sprite sprite;
    private final float velocidadBase;
    private MovimientoZombie estrategiaMovimiento;
    private boolean eliminado;

    protected Zombie(float x, float y, Texture textura, float velocidadBase,
                     MovimientoZombie estrategia) {
        if (estrategia == null) {
            throw new IllegalArgumentException("La estrategia no puede ser null");
        }
        if (velocidadBase <= 0f) {
            throw new IllegalArgumentException("La velocidad debe ser positiva");
        }
        this.posicion = new Vector2(x, y);
        this.sprite = new Sprite(textura);
        this.sprite.setBounds(x, y, 42f, 56f);
        this.velocidadBase = velocidadBase;
        this.estrategiaMovimiento = estrategia;
    }

    /**
     * Template Method: fija el orden de actualizacion de todos los zombis.
     * Las subclases implementan dos pasos diferentes: eleccion de estrategia
     * y calculo de velocidad. El desplazamiento y los limites son comunes.
     */
    public final void actualizar(float delta, Vector2 jugador) {
        if (eliminado) return;
        seleccionarEstrategia(jugador);
        float velocidadActual = calcularVelocidad(jugador);
        mover(delta, jugador, velocidadActual);
        actualizarPosicionSprite();
    }

    /** Paso variable 1: la subclase decide como desplazarse. */
    protected abstract void seleccionarEstrategia(Vector2 jugador);

    /** Paso variable 2: la subclase calcula la velocidad para este fotograma. */
    protected abstract float calcularVelocidad(Vector2 jugador);

    /** Strategy: ejecuta el algoritmo de movimiento seleccionado. */
    private void mover(float delta, Vector2 jugador, float velocidadActual) {
        Vector2 destino = new Vector2(jugador).sub(sprite.getWidth() / 2f,
                                                  sprite.getHeight() / 2f);
        estrategiaMovimiento.mover(posicion, destino, velocidadActual, delta);
    }

    /** Paso compartido del Template Method. */
    private void actualizarPosicionSprite() {
        posicion.x = MathUtils.clamp(posicion.x, 0f,
                Math.max(0f, Gdx.graphics.getWidth() - sprite.getWidth()));
        posicion.y = MathUtils.clamp(posicion.y, 0f,
                Math.max(0f, Gdx.graphics.getHeight() - sprite.getHeight()));
        sprite.setPosition(posicion.x, posicion.y);
    }

    protected final float distanciaAlJugador(Vector2 jugador) {
        return posicion.dst(jugador);
    }

    protected final float getVelocidadBase() {
        return velocidadBase;
    }

    public void dibujar(SpriteBatch batch) {
        if (!eliminado) sprite.draw(batch);
    }

    public void setEstrategiaMovimiento(MovimientoZombie estrategia) {
        if (estrategia == null) {
            throw new IllegalArgumentException("La estrategia no puede ser null");
        }
        this.estrategiaMovimiento = estrategia;
    }

    public Vector2 getPosicion() {
        return new Vector2(posicion);
    }

    public void setColor(Color color) {
        sprite.setColor(color);
    }

    public Rectangle getArea() {
        return sprite.getBoundingRectangle();
    }

    public boolean recibirDisparo() {
        if (eliminado) return false;
        eliminado = true;
        return true;
    }

    public boolean estaEliminado() {
        return eliminado;
    }
}
