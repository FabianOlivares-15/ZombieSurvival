package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

/** Proyectil. Velocidades medidas en píxeles por segundo, no por fotograma. */
public class Bullet {
    private final float xSpeed;
    private final float ySpeed;
    private final Sprite sprite;
    private boolean destroyed;

    public Bullet(float x, float y, float xSpeed, float ySpeed, Texture textura) {
        sprite = new Sprite(textura);
        sprite.setPosition(x, y);
        sprite.setOriginCenter();
        sprite.setRotation(MathUtils.atan2(-xSpeed, ySpeed) * MathUtils.radiansToDegrees);
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }

    public void update(float delta) {
        if (destroyed) return;
        sprite.translate(xSpeed * delta, ySpeed * delta);
        if (sprite.getX() < -sprite.getWidth() || sprite.getX() > Gdx.graphics.getWidth()
                || sprite.getY() < -sprite.getHeight() || sprite.getY() > Gdx.graphics.getHeight()) {
            destroyed = true;
        }
    }

    public boolean checkCollision(Zombie zombie) {
        if (destroyed || zombie == null || zombie.estaEliminado()) return false;
        if (!sprite.getBoundingRectangle().overlaps(zombie.getArea())) return false;
        destroyed = true;
        return zombie.recibirDisparo();
    }

    public void draw(SpriteBatch batch) {
        if (!destroyed) sprite.draw(batch);
    }

    public boolean isDestroyed() { return destroyed; }
}
