package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;

public class PantallaMenu implements Screen {
    private final SpaceNavigation game;
    private final OrthographicCamera camera = new OrthographicCamera();

    public PantallaMenu(SpaceNavigation game) {
        this.game = game;
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.06f, 0.09f, 0.12f, 1f);
        camera.update();
        game.getBatch().setProjectionMatrix(camera.combined);
        game.getFont().getData().setScale(2.4f);
        game.getBatch().begin();
        float ancho = Gdx.graphics.getWidth();
        float alto = Gdx.graphics.getHeight();
        game.getFont().draw(game.getBatch(), "ZOMBIE SURVIVAL",
                0f, alto * 0.62f, ancho, Align.center, false);
        game.getFont().getData().setScale(1.5f);
        game.getFont().draw(game.getBatch(), "Presiona cualquier tecla para sobrevivir",
                0f, alto * 0.51f, ancho, Align.center, false);
        game.getFont().draw(game.getBatch(), "WASD / Flechas: mover    |    ESPACIO: disparar",
                0f, alto * 0.40f, ancho, Align.center, false);
        game.getBatch().end();

        if (Gdx.input.isTouched() || Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
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
