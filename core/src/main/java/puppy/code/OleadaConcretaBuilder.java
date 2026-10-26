package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class OleadaConcretaBuilder implements OleadaBuilder {

    private Oleada oleada;
    private final Texture texturaNormal;
    private final Texture texturaRapido;
    private Vector2 posicionJugador = new Vector2();

    public OleadaConcretaBuilder(Texture texturaNormal, Texture texturaRapido) {
        this.texturaNormal = texturaNormal;
        this.texturaRapido = texturaRapido;
        reset();
    }
    public void setPosicionJugador(Vector2 posicionJugador) {
        this.posicionJugador.set(posicionJugador);
    }

    @Override
    public void reset() {
        oleada = new Oleada();
    }

    @Override
    public void agregarNormales(int cantidad) {

        for (int i = 0; i < cantidad; i++) {

        	Vector2 posicion = generarPosicionSegura();

        	ZombieNormal zombie = new ZombieNormal(
        	        posicion.x,
        	        posicion.y,
        	        texturaNormal
        	);

            zombie.setColor(Color.WHITE);

            oleada.agregarZombie(zombie);
        }
    }

    @Override
    public void agregarRapidos(int cantidad) {

        for (int i = 0; i < cantidad; i++) {

        	Vector2 posicion = generarPosicionSegura();

        	ZombieRapido zombie = new ZombieRapido(
        	        posicion.x,
        	        posicion.y,
        	        texturaRapido
        	);

            zombie.setColor(Color.WHITE);

            oleada.agregarZombie(zombie);
        }
    }

    @Override
    public Oleada getResult() {
        return oleada;
    }


	private Vector2 generarPosicionSegura() {
	
	    float maxX = Math.max(0f,
	            Gdx.graphics.getWidth() - 42f);
	
	    float maxY = Math.max(0f,
	            Gdx.graphics.getHeight() - 56f);
	
	    float distanciaMinima = 280f;
	
	    Vector2 mejorPosicion = new Vector2();
	    float mayorDistancia = -1f;
	
	    // Buscar una posicion segura hasta 40 veces
	    for (int intento = 0; intento < 40; intento++) {
	
	        float x = MathUtils.random(0f, maxX);
	        float y = MathUtils.random(0f, maxY);
	
	        // Elegir uno de los cuatro bordes
	        int borde = MathUtils.random(0, 3);
	
	        switch (borde) {
	            case 0:
	                x = 0;
	                break;
	
	            case 1:
	                x = maxX;
	                break;
	
	            case 2:
	                y = 0;
	                break;
	
	            case 3:
	                y = maxY;
	                break;
	        }
	
	        Vector2 posicion = new Vector2(x, y);
	
	        // Distancia desde el centro del zombi
	        Vector2 centroZombie =
	                new Vector2(x + 21f, y + 28f);
	
	        float distancia =
	                centroZombie.dst(posicionJugador);
	
	        // Guardar la mejor posicion encontrada
	        if (distancia > mayorDistancia) {
	            mayorDistancia = distancia;
	            mejorPosicion.set(posicion);
	        }
	
	        // Aceptar si esta suficientemente lejos
	        if (distancia >= distanciaMinima) {
	            return posicion;
	        }
	    }
	
	    // Si no se encontro una posicion ideal,
	    // utilizar la mas lejana encontrada
	    return mejorPosicion;
	}

}
