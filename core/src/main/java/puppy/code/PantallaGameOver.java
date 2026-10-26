package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;

public class PantallaGameOver implements Screen {
    private final SpaceNavigation game;
    private final OrthographicCamera camera = new OrthographicCamera();

    public PantallaGameOver(SpaceNavigation game) {
        this.game = game;
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.12f, 0.05f, 0.07f, 1f);
        camera.update();
        game.getBatch().setProjectionMatrix(camera.combined);
        float ancho = Gdx.graphics.getWidth();
        float alto = Gdx.graphics.getHeight();
        EstadoPartida estado = EstadoPartida.getInstance();
        game.getBatch().begin();
        game.getFont().getData().setScale(2.4f);
        game.getFont().draw(game.getBatch(), "GAME OVER",
                0f, alto * 0.65f, ancho, Align.center, false);
        game.getFont().getData().setScale(1.6f);
        game.getFont().draw(game.getBatch(),
                "Puntos: " + estado.getPuntaje() + "    Oleada alcanzada: " + estado.getOleada(),
                0f, alto * 0.52f, ancho, Align.center, false);
        game.getFont().draw(game.getBatch(), "Record: " + game.getHighScore(),
                0f, alto * 0.45f, ancho, Align.center, false);
        game.getFont().draw(game.getBatch(), "ENTER / clic: reintentar    |    ESC: menu",
                0f, alto * 0.33f, ancho, Align.center, false);
        game.getBatch().end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new PantallaMenu(game));
            dispose();
        } else if (Gdx.input.isTouched() || Gdx.input.isKeyJustPressed(Input.Keys.ENTER)
                || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            game.setScreen(new PantallaJuego(game));
            dispose();
        }
    }

    @Override public void show() { }
    @Override public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
    }
    @Override public void pause() { }
    @Override public void resume() { }
    @Override public void hide() { }
    @Override public void dispose() { }
}
