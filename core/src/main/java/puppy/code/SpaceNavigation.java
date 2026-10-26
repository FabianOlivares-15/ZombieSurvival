package puppy.code;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;




public class SpaceNavigation extends Game {
	private SpriteBatch batch;
	private BitmapFont font;
	private int highScore;
    private int personajeSeleccionado = 0;	

	public void create() {
		highScore = 0;
		batch = new SpriteBatch();
		font = new BitmapFont(); // usa Arial font x defecto
		font.getData().setScale(2f);
		Screen ss = new PantallaMenu(this);
		this.setScreen(ss);
	}

	public void render() {
		super.render(); // important!
	}

	public void dispose() {
        super.dispose();
		batch.dispose();
		font.dispose();
	}

	public SpriteBatch getBatch() {
		return batch;
	}

	public BitmapFont getFont() {
		return font;
	}

	public int getHighScore() {
		return highScore;
	}

	public void setHighScore(int highScore) {
		this.highScore = highScore;
	}
	
	

    public int getPersonajeSeleccionado() {
        return personajeSeleccionado;
    }

    public void setPersonajeSeleccionado(int indice) {
        if (indice < 0 || indice >= Personajes.TOTAL) {
            throw new IllegalArgumentException("Personaje invalido: " + indice);
        }
        personajeSeleccionado = indice;
    }
}