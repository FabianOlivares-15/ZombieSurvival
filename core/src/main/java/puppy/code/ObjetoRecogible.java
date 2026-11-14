package puppy.code;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

/** Recurso recogible con tiempo limitado de permanencia en el escenario. */
public class ObjetoRecogible {
    public enum Tipo { BOTIQUIN, CAJA_ARMAS }

    private final Tipo tipo;
    private final Sprite sprite;
    private float duracion;

    public ObjetoRecogible(Tipo tipo, Texture textura, float x, float y,
                          float ancho, float alto, float duracion) {
        this.tipo = tipo;
        this.sprite = new Sprite(textura);
        this.sprite.setBounds(x, y, ancho, alto);
        this.duracion = duracion;
    }

    public void actualizar(float delta) {
        duracion = Math.max(0f, duracion - delta);
    }

    public boolean expiro() {
        return duracion <= 0f;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public Rectangle getArea() {
        return sprite.getBoundingRectangle();
    }

    public void dibujar(SpriteBatch batch) {
        sprite.draw(batch);
    }
}
