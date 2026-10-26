package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;

/** Menu de seleccion para cuatro supervivientes, por mouse o teclado. */
public class PantallaMenu implements Screen {
    private final SpaceNavigation game;
    private final OrthographicCamera camera = new OrthographicCamera();
    private final Texture fondo;
    private final Texture[] personajes = new Texture[Personajes.TOTAL];
    private final Texture pixel;
    private int seleccionado;

    private static final Color PANEL = new Color(0.10f, 0.17f, 0.21f, 0.88f);
    private static final Color PANEL_ACTIVO = new Color(0.14f, 0.38f, 0.29f, 0.92f);
    private static final Color BORDE_ACTIVO = new Color(0.58f, 0.96f, 0.70f, 1f);
    private static final Color BOTON = new Color(0.15f, 0.52f, 0.34f, 0.95f);
    private static final Color SOMBRA = new Color(0f, 0f, 0f, 0.45f);

    public PantallaMenu(SpaceNavigation game) {
        this.game = game;
        seleccionado = game.getPersonajeSeleccionado();
        fondo = new Texture(Gdx.files.internal(
                Gdx.files.internal("fondo_menu.png").exists() ? "fondo_menu.png" : "suelo.png"));
        for (int i = 0; i < Personajes.TOTAL; i++) {
            personajes[i] = new Texture(Gdx.files.internal(Personajes.rutaImagen(i)));
        }
        Pixmap img = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        img.setColor(Color.WHITE);
        img.fill();
        pixel = new Texture(img);
        img.dispose();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void render(float delta) {
        float ancho = Gdx.graphics.getWidth();
        float alto = Gdx.graphics.getHeight();
        float anchoCarta = Math.min(170f, (ancho - 85f) / 4.5f);
        float altoCarta = 192f;
        float separacion = 18f;
        float inicioX = (ancho - (4f * anchoCarta + 3f * separacion)) / 2f;
        float cartasY = alto * 0.35f;
        float botonAncho = 260f;
        float botonAlto = 65f;
        float botonX = (ancho - botonAncho) / 2f;
        float botonY = alto * 0.19f;

        ScreenUtils.clear(0.06f, 0.09f, 0.12f, 1f);
        camera.update();
        SpriteBatch batch = game.getBatch();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.setColor(Color.WHITE);
        batch.draw(fondo, 0f, 0f, ancho, alto);

        // Oscurecer un poco para que el texto se lea mejor.
        batch.setColor(SOMBRA);
        batch.draw(pixel, 0f, 0f, ancho, alto);
        batch.setColor(Color.WHITE);

        game.getFont().getData().setScale(2.5f);
        game.getFont().draw(batch, "ZOMBIE SURVIVAL", 0f,
                alto * 0.89f, ancho, Align.center, false);
        game.getFont().getData().setScale(1.5f);
        game.getFont().draw(batch, "ELIGE TU SUPERVIVIENTE", 0f,
                alto * 0.78f, ancho, Align.center, false);

        for (int i = 0; i < Personajes.TOTAL; i++) {
            float x = inicioX + i * (anchoCarta + separacion);
            boolean activo = i == seleccionado;
            batch.setColor(activo ? PANEL_ACTIVO : PANEL);
            batch.draw(pixel, x, cartasY, anchoCarta, altoCarta);
            if (activo) {
                batch.setColor(BORDE_ACTIVO);
                float b = 3f;
                batch.draw(pixel, x, cartasY, anchoCarta, b);
                batch.draw(pixel, x, cartasY + altoCarta - b, anchoCarta, b);
                batch.draw(pixel, x, cartasY, b, altoCarta);
                batch.draw(pixel, x + anchoCarta - b, cartasY, b, altoCarta);
            }
            float altoImagen = 121f;
            float anchoImagen = altoImagen * personajes[i].getWidth()
                    / (float) personajes[i].getHeight();
            if (anchoImagen > anchoCarta - 16f) {
                anchoImagen = anchoCarta - 16f;
                altoImagen = anchoImagen * personajes[i].getHeight()
                        / (float) personajes[i].getWidth();
            }
            batch.setColor(Personajes.color(i));
            batch.draw(personajes[i], x + (anchoCarta - anchoImagen) / 2f,
                    cartasY + 64f, anchoImagen, altoImagen);
            batch.setColor(Color.WHITE);
            game.getFont().getData().setScale(1.15f);
            game.getFont().draw(batch, Personajes.nombre(i), x,
                    cartasY + 48f, anchoCarta, Align.center, false);
            game.getFont().getData().setScale(1f);
            game.getFont().draw(batch, activo ? "ELEGIDO" : "TECLA " + (i + 1),
                    x, cartasY + 24f, anchoCarta, Align.center, false);
        }

        batch.setColor(BOTON);
        batch.draw(pixel, botonX, botonY, botonAncho, botonAlto);
        batch.setColor(Color.WHITE);
        game.getFont().getData().setScale(1.8f);
        game.getFont().draw(batch, "JUGAR", botonX, botonY + 44f,
                botonAncho, Align.center, false);
        game.getFont().getData().setScale(1.1f);
        game.getFont().draw(batch, "Clic en un personaje o teclas 1-4. ENTER para jugar.",
                0f, alto * 0.10f, ancho, Align.center, false);
        batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) seleccionado = 0;
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) seleccionado = 1;
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) seleccionado = 2;
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) seleccionado = 3;
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            empezar();
            return;
        }
        if (Gdx.input.justTouched()) {
            float mouseX = Gdx.input.getX();
            float mouseY = alto - Gdx.input.getY();
            for (int i = 0; i < Personajes.TOTAL; i++) {
                float x = inicioX + i * (anchoCarta + separacion);
                if (dentro(mouseX, mouseY, x, cartasY, anchoCarta, altoCarta)) {
                    seleccionado = i;
                    return;
                }
            }
            if (dentro(mouseX, mouseY, botonX, botonY, botonAncho, botonAlto)) {
                empezar();
            }
        }
    }

    private boolean dentro(float mx, float my, float x, float y, float w, float h) {
        return mx >= x && mx <= x + w && my >= y && my <= y + h;
    }

    private void empezar() {
        game.setPersonajeSeleccionado(seleccionado);
        game.setScreen(new PantallaJuego(game));
        dispose();
    }

    @Override public void show() { }
    @Override public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
    }
    @Override public void pause() { }
    @Override public void resume() { }
    @Override public void hide() { }
    @Override public void dispose() {
        for (Texture personaje : personajes) personaje.dispose();
        fondo.dispose();
        pixel.dispose();
    }
}
