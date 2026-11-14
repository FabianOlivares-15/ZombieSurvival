package puppy.code;

import java.util.ArrayList;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;

/** Pantalla principal de Zombie Survival. */
public class PantallaJuego implements Screen {
    private final SpaceNavigation game;
    private final SpriteBatch batch;
    private final OrthographicCamera camera;
    private final Sound explosionSound;
    private final Music gameMusic;
    private final Nave4 nave;
    private final Texture texturaZombieNormal;
    private final Texture texturaZombieRapido;
    private final Texture texturaSuelo;
    private final Texture texturaBotiquin;
    private final Texture texturaCajaArmas;
    private final EstadoPartida estadoPartida;
    private final OleadaConcretaBuilder builderOleadas;
    private final DirectorOleadas directorOleadas;
    private final ArrayList<Bullet> balas = new ArrayList<Bullet>();

    private ArrayList<Zombie> zombies;
    private float esperaOleada = -1f;
    private ObjetoRecogible botiquin;
    private ObjetoRecogible cajaArmas;
    private float tiempoProximoBotiquin = 10f;
    private float tiempoProximaCaja = 8f;
    private float mensajeDuracion;
    private String mensajeRecogida = "";

    public PantallaJuego(SpaceNavigation game) {
        this.game = game;
        batch = game.getBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        estadoPartida = EstadoPartida.getInstance();
        estadoPartida.reiniciar(); // Solamente al iniciar una partida, no entre oleadas.

        explosionSound = Gdx.audio.newSound(Gdx.files.internal("explosion.ogg"));
        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("piano-loops.ogg"));
        gameMusic.setLooping(true);
        gameMusic.setVolume(0.35f);

        nave = new Nave4(Gdx.graphics.getWidth() / 2 - 27, 60,
                new Texture(Gdx.files.internal(
                        Personajes.rutaImagen(game.getPersonajeSeleccionado()))),
                Gdx.audio.newSound(Gdx.files.internal("hurt.ogg")),
                new Texture(Gdx.files.internal("Rocket2.png")),
                Gdx.audio.newSound(Gdx.files.internal("pop-sound.mp3")));

        // Configurar el aspecto del superviviente seleccionado en el menu.
        nave.setColor(Personajes.color(game.getPersonajeSeleccionado()));

        // Cargar las texturas de los dos tipos de zombis.
        texturaZombieNormal = new Texture(Gdx.files.internal("zombie_normal.png"));
        texturaZombieRapido = new Texture(Gdx.files.internal("zombie_rapido.png"));
        texturaSuelo = new Texture(Gdx.files.internal("suelo.png"));
        texturaBotiquin = new Texture(Gdx.files.internal("botiquin.png"));
        texturaCajaArmas = new Texture(Gdx.files.internal("caja_armas.png"));
        builderOleadas = new OleadaConcretaBuilder(texturaZombieNormal, texturaZombieRapido);
        builderOleadas.setPosicionJugador(nave.getCentro());
        directorOleadas = new DirectorOleadas(builderOleadas);
        Oleada primera = directorOleadas.construirOleada(estadoPartida.getOleada());
        zombies = primera.getZombies();
    }

    @Override
    public void render(float delta) {
        float paso = Math.min(delta, 1f / 20f); // Evita saltos grandes si la ventana se congela.
        ScreenUtils.clear(0.06f, 0.09f, 0.12f, 1f);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(texturaSuelo, 0f, 0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Actualizar primero al personaje, para que los zombis persigan su posición actual.
        nave.draw(batch, this, paso);
        actualizarRecogibles(paso);
        Vector2 posicionJugador = nave.getCentro();

        // Actualizar las balas y detectar impactos sin modificar la lista durante el for-each.
        for (int i = balas.size() - 1; i >= 0; i--) {
            Bullet bala = balas.get(i);
            bala.update(paso);
            if (!bala.isDestroyed()) {
                for (Zombie zombie : zombies) {
                    if (bala.checkCollision(zombie)) {
                        explosionSound.play(0.5f);
                        estadoPartida.sumarPuntos(10);
                        break;
                    }
                }
            }
            if (bala.isDestroyed()) {
                balas.remove(i);
            } else {
                bala.draw(batch);
            }
        }

        // Patrón Template Method + Strategy: mismo flujo, distinta estrategia por zombi.
        for (Zombie zombie : zombies) {
            if (!zombie.estaEliminado()) {
                zombie.actualizar(paso, posicionJugador);
                zombie.dibujar(batch);
                nave.recibirDanioZombie(zombie);
            }
        }

        dibujaEncabezado();
        batch.end();

        // Cambiar pantalla sólo después de terminar el SpriteBatch.
        if (nave.estaDestruido()) {
            if (estadoPartida.getPuntaje() > game.getHighScore()) {
                game.setHighScore(estadoPartida.getPuntaje());
            }
            game.setScreen(new PantallaGameOver(game));
            dispose();
            return;
        }
        actualizarOleadas(paso);
    }

    private void actualizarRecogibles(float delta) {
        mensajeDuracion = Math.max(0f, mensajeDuracion - delta);

        if (botiquin != null) {
            botiquin.actualizar(delta);
            if (botiquin.expiro()) {
                botiquin = null;
            } else if (botiquin.getArea().overlaps(nave.getArea())
                    && estadoPartida.recuperarVida()) {
                botiquin = null;
                mostrarMensaje("Botiquin: +1 vida");
            }
        }
        if (botiquin == null && estadoPartida.getVidas() < 3) {
            tiempoProximoBotiquin -= delta;
            if (tiempoProximoBotiquin <= 0f) {
                botiquin = crearRecogible(ObjetoRecogible.Tipo.BOTIQUIN,
                        texturaBotiquin, 50f, 63f);
                tiempoProximoBotiquin = 16f;
            }
        }
        if (estadoPartida.getVidas() == 3) tiempoProximoBotiquin = 10f;

        if (cajaArmas != null) {
            cajaArmas.actualizar(delta);
            if (cajaArmas.expiro()) {
                cajaArmas = null;
            } else if (cajaArmas.getArea().overlaps(nave.getArea())) {
                nave.activarEscopeta();
                cajaArmas = null;
                mostrarMensaje("Escopeta triple: 15 segundos");
            }
        }
        if (cajaArmas == null && !nave.tieneEscopeta()) {
            tiempoProximaCaja -= delta;
            if (tiempoProximaCaja <= 0f) {
                cajaArmas = crearRecogible(ObjetoRecogible.Tipo.CAJA_ARMAS,
                        texturaCajaArmas, 60f, 60f);
                tiempoProximaCaja = 22f;
            }
        }

        if (botiquin != null) botiquin.dibujar(batch);
        if (cajaArmas != null) cajaArmas.dibujar(batch);
    }

    private ObjetoRecogible crearRecogible(ObjetoRecogible.Tipo tipo,
                                          Texture textura, float ancho, float alto) {
        float maxX = Math.max(15f, Gdx.graphics.getWidth() - ancho - 15f);
        float maxY = Math.max(15f, Gdx.graphics.getHeight() - alto - 15f);
        Vector2 jugador = nave.getCentro();
        float mejorX = 15f;
        float mejorY = 15f;
        float mayorDistancia = -1f;
        for (int i = 0; i < 30; i++) {
            float x = MathUtils.random(15f, maxX);
            float y = MathUtils.random(15f, maxY);
            float dx = x + ancho / 2f - jugador.x;
            float dy = y + alto / 2f - jugador.y;
            float distancia = (float) Math.sqrt(dx * dx + dy * dy);
            if (distancia > mayorDistancia) {
                mayorDistancia = distancia;
                mejorX = x;
                mejorY = y;
            }
            if (distancia >= 165f) {
                mejorX = x;
                mejorY = y;
                break;
            }
        }
        return new ObjetoRecogible(tipo, textura, mejorX, mejorY,
                ancho, alto, 14f);
    }

    private void mostrarMensaje(String texto) {
        mensajeRecogida = texto;
        mensajeDuracion = 2.5f;
    }

    private int contarZombisVivos() {
        int vivos = 0;
        for (Zombie zombie : zombies) {
            if (!zombie.estaEliminado()) vivos++;
        }
        return vivos;
    }

    private void actualizarOleadas(float delta) {
        if (contarZombisVivos() > 0) {
            esperaOleada = -1f;
            return;
        }
        if (esperaOleada < 0f) {
            esperaOleada = 3f;
            return;
        }
        esperaOleada -= delta;
        if (esperaOleada <= 0f) {
            estadoPartida.avanzarOleada();
            builderOleadas.setPosicionJugador(nave.getCentro());
            zombies = directorOleadas.construirOleada(estadoPartida.getOleada()).getZombies();
            esperaOleada = -1f;
        }
    }

    private void dibujaEncabezado() {
        game.getFont().getData().setScale(1.6f);
        float alto = Gdx.graphics.getHeight();
        float ancho = Gdx.graphics.getWidth();
        game.getFont().draw(batch,
                "Vidas: " + estadoPartida.getVidas() + "   Oleada: " + estadoPartida.getOleada(),
                18f, alto - 16f);
        game.getFont().draw(batch, "Puntos: " + estadoPartida.getPuntaje(),
                ancho - 230f, alto - 16f);
        game.getFont().draw(batch, "Zombis restantes: " + contarZombisVivos(),
                18f, alto - 53f);
        game.getFont().getData().setScale(1.15f);
        if (nave.tieneEscopeta()) {
            game.getFont().draw(batch,
                    "ARMA: Escopeta triple (" + (int) Math.ceil(nave.getTiempoEscopeta()) + "s)",
                    18f, alto - 90f);
        } else {
            game.getFont().draw(batch, "ARMA: Pistola", 18f, alto - 90f);
        }
        game.getFont().draw(batch,
                "Botiquin: recupera 1 vida    |    Caja de municiones: escopeta temporal",
                18f, 26f);
        if (mensajeDuracion > 0f) {
            game.getFont().getData().setScale(1.5f);
            game.getFont().draw(batch, mensajeRecogida,
                    ancho / 2f - 175f, alto - 85f);
        }
        game.getFont().getData().setScale(1.6f);
        if (esperaOleada > 0f) {
            game.getFont().draw(batch,
                    "Siguiente oleada en " + (int) Math.ceil(esperaOleada) + "...",
                    ancho / 2f - 165f, alto / 2f);
        }
    }

    public boolean agregarBala(Bullet bala) { return balas.add(bala); }

    @Override public void show() { gameMusic.play(); }

    @Override public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
        camera.update();
    }

    @Override public void pause() { gameMusic.pause(); }
    @Override public void resume() { gameMusic.play(); }
    @Override public void hide() { gameMusic.pause(); }

    @Override public void dispose() {
        gameMusic.dispose();
        explosionSound.dispose();
        texturaZombieNormal.dispose();
        texturaZombieRapido.dispose();
        texturaSuelo.dispose();
        texturaBotiquin.dispose();
        texturaCajaArmas.dispose();
        nave.dispose();
    }
}
