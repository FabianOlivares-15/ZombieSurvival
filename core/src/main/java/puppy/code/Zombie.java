package puppy.code;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public abstract class Zombie {

    private Vector2 posicion;
    private Sprite sprite;
    private float velocidad;
    private MovimientoZombie estrategiaMovimiento;
    private boolean eliminado = false;

    public Zombie(float x, float y, Texture textura,
                  float velocidad, MovimientoZombie estrategia) {

        posicion = new Vector2(x, y);

        sprite = new Sprite(textura);
        sprite.setBounds(x, y, 45, 45);

        this.velocidad = velocidad;
        this.estrategiaMovimiento = estrategia;
    }

    // TEMPLATE METHOD: define el orden de actualizacion
    public final void actualizar(float delta, Vector2 jugador) {

        comportamientoEspecial(delta, jugador);

        mover(delta, jugador);

        actualizarPosicionSprite();
    }

    // STRATEGY: delega el movimiento a una estrategia
    private void mover(float delta, Vector2 jugador) {

        // Estrategias trabajan con la esquina inferior izquierda del sprite.
        Vector2 destino = new Vector2(jugador).sub(sprite.getWidth() / 2f,
                sprite.getHeight() / 2f);
        estrategiaMovimiento.mover(posicion, destino, velocidad, delta);
    }

    // Cada tipo de zombi implementara su comportamiento
    protected abstract void comportamientoEspecial(
            float delta, Vector2 jugador);


	private void actualizarPosicionSprite() {
	
	    // Mantener al zombi dentro de los limites
	    posicion.x = MathUtils.clamp(
	            posicion.x,
	            0,
	            Gdx.graphics.getWidth() - sprite.getWidth()
	    );
	
	    posicion.y = MathUtils.clamp(
	            posicion.y,
	            0,
	            Gdx.graphics.getHeight() - sprite.getHeight()
	    );
	
	    // Actualizar la posicion de la imagen
	    sprite.setPosition(posicion.x, posicion.y);
	}


    public void dibujar(SpriteBatch batch) {
        sprite.draw(batch);
    }

    // Permite cambiar la estrategia de movimiento
    public void setEstrategiaMovimiento(
            MovimientoZombie estrategia) {

        if (estrategia == null) {
            throw new IllegalArgumentException(
                    "La estrategia no puede ser null");
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
	
	    // No recibir impactos si ya fue eliminado
	    if (eliminado) {
	        return false;
	    }
	
	    // Eliminar al zombi
	    eliminado = true;
	
	    return true;
	}
	
	public boolean estaEliminado() {
	    return eliminado;
	}


}
