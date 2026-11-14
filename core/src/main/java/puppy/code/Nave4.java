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
    private static final float ANCHO_PERSONAJE = 54f;
    private static final float ALTO_PERSONAJE = 72f;
    private static final float DURACION_ESCOPETA = 15f;
    private static final float ANGULO_DISPERSION = 17f;

    private final Sprite sprite;
    private final Texture texturaJugador;
    private final Texture texturaBala;
    private final Sound sonidoHerido;
    private final Sound sonidoBala;

    private boolean destruida;
    private float tiempoHerido;
    private float tiempoEscopeta;
    private float recargaDisparo;
    private float direccionX = 0f;
    private float direccionY = 1f;

    public Nave4(int x, int y, Texture texturaJugador, Sound sonidoHerido,
                 Texture texturaBala, Sound sonidoBala) {
        this.texturaJugador = texturaJugador;
        this.texturaBala = texturaBala;
        this.sonidoHerido = sonidoHerido;
        this.sonidoBala = sonidoBala;
        sprite = new Sprite(texturaJugador);
        sprite.setBounds(x, y, ANCHO_PERSONAJE, ALTO_PERSONAJE);
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

        tiempoHerido = Math.max(0f, tiempoHerido - delta);
        tiempoEscopeta = Math.max(0f, tiempoEscopeta - delta);
        recargaDisparo = Math.max(0f, recargaDisparo - delta);

        // El personaje sigue moviendose y disparando durante su proteccion.
        if (tiempoHerido <= 0f || ((int) (tiempoHerido * 10f)) % 2 == 0) {
            sprite.draw(batch);
        }

        // Mantener Espacio permite disparar de forma continua con una cadencia limitada.
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && !destruida && recargaDisparo <= 0f) {
            disparar(juego);
            recargaDisparo = tieneEscopeta() ? 0.48f : 0.27f;
        }
    }

    private void disparar(PantallaJuego juego) {
        if (tieneEscopeta()) {
            crearProyectil(juego, -ANGULO_DISPERSION);
            crearProyectil(juego, 0f);
            crearProyectil(juego, ANGULO_DISPERSION);
        } else {
            crearProyectil(juego, 0f);
        }
        sonidoBala.play(0.55f);
    }

    private void crearProyectil(PantallaJuego juego, float grados) {
        float angulo = grados * MathUtils.degreesToRadians;
        float coseno = (float) Math.cos(angulo);
        float seno = (float) Math.sin(angulo);
        float dirX = direccionX * coseno - direccionY * seno;
        float dirY = direccionX * seno + direccionY * coseno;
        Vector2 centro = getCentro();
        float separacion = 40f;
        float x = centro.x + direccionX * separacion - texturaBala.getWidth() / 2f;
        float y = centro.y + direccionY * separacion - texturaBala.getHeight() / 2f;
        juego.agregarBala(new Bullet(x, y, dirX * VELOCIDAD_BALA,
                dirY * VELOCIDAD_BALA, texturaBala));
    }

    public void activarEscopeta() {
        tiempoEscopeta = DURACION_ESCOPETA;
        recargaDisparo = 0f;
    }

    public boolean tieneEscopeta() {
        return tiempoEscopeta > 0f;
    }

    public float getTiempoEscopeta() {
        return tiempoEscopeta;
    }

    public com.badlogic.gdx.math.Rectangle getArea() {
        return sprite.getBoundingRectangle();
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
