package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/** Superviviente controlado por WASD. Conserva la protección después de recibir daño. */
public class Nave4 {
    private static final float VELOCIDAD = 230f;
    private static final float PROTECCION_SEGUNDOS = 3f;
    private static final float VELOCIDAD_BALA = 360f;

    private final Sprite sprite;
    private final Texture texturaJugador;
    private final Texture texturaBala;
    private final Sound sonidoHerido;
    private final Sound sonidoBala;

    private boolean destruida;
    private float tiempoHerido;
    private float direccionX = 0f;
    private float direccionY = 1f;

    public Nave4(int x, int y, Texture texturaJugador, Sound sonidoHerido,
                 Texture texturaBala, Sound sonidoBala) {
        this.texturaJugador = texturaJugador;
        this.texturaBala = texturaBala;
        this.sonidoHerido = sonidoHerido;
        this.sonidoBala = sonidoBala;
        sprite = new Sprite(texturaJugador);
        sprite.setBounds(x, y, 42f, 56f);
        sprite.setOriginCenter();
    }

    public void draw(SpriteBatch batch, PantallaJuego juego, float delta) {
        float dx = 0f;
        float dy = 0f;
        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) dx--;
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) dx++;
        if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) dy--;
        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) dy++;

        float longitud = (float) Math.sqrt(dx * dx + dy * dy);
        if (longitud > 0f) {
            dx /= longitud;
            dy /= longitud;
            direccionX = dx;
            direccionY = dy;
            sprite.setRotation(MathUtils.atan2(-direccionX, direccionY) * MathUtils.radiansToDegrees);
        }

        float x = MathUtils.clamp(sprite.getX() + dx * VELOCIDAD * delta,
                0f, Math.max(0f, Gdx.graphics.getWidth() - sprite.getWidth()));
        float y = MathUtils.clamp(sprite.getY() + dy * VELOCIDAD * delta,
                0f, Math.max(0f, Gdx.graphics.getHeight() - sprite.getHeight()));
        sprite.setPosition(x, y);

        if (tiempoHerido > 0f) {
            tiempoHerido = Math.max(0f, tiempoHerido - delta);
        }
        // Parpadea durante la protección, pero puede caminar y disparar.
        if (tiempoHerido <= 0f || ((int) (tiempoHerido * 10f)) % 2 == 0) {
            sprite.draw(batch);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && !destruida) {
            Vector2 centro = getCentro();
            float xBala = centro.x + direccionX * 25f - texturaBala.getWidth() / 2f;
            float yBala = centro.y + direccionY * 25f - texturaBala.getHeight() / 2f;
            juego.agregarBala(new Bullet(xBala, yBala,
                    direccionX * VELOCIDAD_BALA, direccionY * VELOCIDAD_BALA, texturaBala));
            sonidoBala.play();
        }
    }

    public boolean recibirDanioZombie(Zombie zombie) {
        if (destruida || estaHerido() || zombie == null || zombie.estaEliminado()
                || !sprite.getBoundingRectangle().overlaps(zombie.getArea())) {
            return false;
        }
        EstadoPartida estado = EstadoPartida.getInstance();
        estado.perderVida();
        sonidoHerido.play();
        tiempoHerido = PROTECCION_SEGUNDOS;
        if (estado.getVidas() <= 0) destruida = true;
        return true;
    }

    public Vector2 getCentro() {
        return new Vector2(sprite.getX() + sprite.getWidth() / 2f,
                sprite.getY() + sprite.getHeight() / 2f);
    }

    public boolean estaDestruido() { return destruida; }
    public boolean estaHerido() { return tiempoHerido > 0f; }
    public int getVidas() { return EstadoPartida.getInstance().getVidas(); }

    public void setColor(Color color) {
        sprite.setColor(color);
    }

    public void dispose() {
        texturaJugador.dispose();
        texturaBala.dispose();
        sonidoHerido.dispose();
        sonidoBala.dispose();
    }
}
