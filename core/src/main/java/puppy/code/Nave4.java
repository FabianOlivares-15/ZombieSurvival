package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;



public class Nave4 {
	
	private boolean destruida = false;
    private float xVel = 0;
    private float yVel = 0;
    private Sprite spr;
    private Sound sonidoHerido;
    private Sound soundBala;
    private Texture txBala;
    private boolean herido = false;
    private float tiempoHeridoMax= 3f;
    private float tiempoHerido;
    private float direccionX = 0;
    private float direccionY = 1;
    
    public Nave4(int x, int y, Texture tx, Sound soundChoque, Texture txBala, Sound soundBala) {
    	sonidoHerido = soundChoque;
    	this.soundBala = soundBala;
    	this.txBala = txBala;
    	spr = new Sprite(tx);
    	spr.setPosition(x, y);
    	//spr.setOriginCenter();
    	spr.setBounds(x, y, 45, 45);
    	spr.setOriginCenter();

    }

	public void draw(SpriteBatch batch, PantallaJuego juego) {
	
	    float delta = Gdx.graphics.getDeltaTime();
	    float velocidad = 230f;
	
	    float dx = 0;
	    float dy = 0;
	
	    // Movimiento horizontal
	    if (Gdx.input.isKeyPressed(Input.Keys.A)
	            || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
	        dx = -1;
	    }
	
	    if (Gdx.input.isKeyPressed(Input.Keys.D)
	            || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
	        dx = 1;
	    }
	
	    // Movimiento vertical
	    if (Gdx.input.isKeyPressed(Input.Keys.W)
	            || Gdx.input.isKeyPressed(Input.Keys.UP)) {
	        dy = 1;
	    }
	
	    if (Gdx.input.isKeyPressed(Input.Keys.S)
	            || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
	        dy = -1;
	    }
	
	    // Normalizar movimiento diagonal
	    float longitud = (float) Math.sqrt(dx * dx + dy * dy);
	
	    if (longitud > 0) {
	        dx /= longitud;
	        dy /= longitud;
	
	        // Guardar hacia donde mira el personaje
	        direccionX = dx;
	        direccionY = dy;
	
	        // Rotar el personaje
	        float angulo = MathUtils.atan2(-direccionX, direccionY)
	                * MathUtils.radiansToDegrees;
	
	        spr.setRotation(angulo);
	    }
	
	    // Calcular posicion
	    float nuevaX = spr.getX() + dx * velocidad * delta;
	    float nuevaY = spr.getY() + dy * velocidad * delta;
	
	    // Limites de la pantalla
	    nuevaX = MathUtils.clamp(
	            nuevaX, 0,
	            Gdx.graphics.getWidth() - spr.getWidth());
	
	    nuevaY = MathUtils.clamp(
	            nuevaY, 0,
	            Gdx.graphics.getHeight() - spr.getHeight());
	
	    spr.setPosition(nuevaX, nuevaY);
	
	    // Dibujar personaje
	    spr.draw(batch);
	
	    // PROTECCION: el tiempo baja, pero no bloquea movimiento
	    if (herido) {
	        tiempoHerido -= delta;
	
	        if (tiempoHerido <= 0) {
	            herido = false;
	        }
	    }
	
	    // Disparar hacia donde mira el personaje
	    if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
	
	        float velocidadBala = 5f;
	
	        float velocidadX = direccionX * velocidadBala;
	        float velocidadY = direccionY * velocidadBala;
	
	        float centroX = spr.getX() + spr.getWidth() / 2;
	        float centroY = spr.getY() + spr.getHeight() / 2;
	
	        float balaX = centroX + direccionX * 25
	                - txBala.getWidth() / 2f;
	
	        float balaY = centroY + direccionY * 25
	                - txBala.getHeight() / 2f;
	
	        Bullet bala = new Bullet(
	                balaX,
	                balaY,
	                velocidadX,
	                velocidadY,
	                txBala
	        );
	
	        juego.agregarBala(bala);
	        soundBala.play();
	    }
	}

      
    public boolean checkCollision(Ball2 b) {
        if(!herido && b.getArea().overlaps(spr.getBoundingRectangle())){
        	// rebote
            if (xVel ==0) xVel += b.getXSpeed()/2;
            if (b.getXSpeed() ==0) b.setXSpeed(b.getXSpeed() + (int)xVel/2);
            xVel = - xVel;
            b.setXSpeed(-b.getXSpeed());
            
            if (yVel ==0) yVel += b.getySpeed()/2;
            if (b.getySpeed() ==0) b.setySpeed(b.getySpeed() + (int)yVel/2);
            yVel = - yVel;
            b.setySpeed(- b.getySpeed());
            // despegar sprites
      /*      int cont = 0;
            while (b.getArea().overlaps(spr.getBoundingRectangle()) && cont<xVel) {
               spr.setX(spr.getX()+Math.signum(xVel));
            }   */
        	//actualizar vidas y herir
            EstadoPartida.getInstance().perderVida();
            herido = true;
  		    tiempoHerido = tiempoHeridoMax;
  		    sonidoHerido.play();
  		    if (getVidas() <= 0) 
          	    destruida = true; 
            return true;
        }
        return false;
    }

	public boolean recibirDanioZombie(Zombie zombie) {
	
	    // No recibir dano si ya esta herido o destruido
	    if (herido || destruida) {
	        return false;
	    }
	
	    // Comprobar si el zombi toca al jugador
	    if (!zombie.getArea().overlaps(
	            spr.getBoundingRectangle())) {
	        return false;
	    }
	
	    // Restar una vida
	    EstadoPartida.getInstance().perderVida();
	
	    // Activar proteccion temporal
	    herido = true;
	    tiempoHerido = tiempoHeridoMax;
	
	    // Reproducir sonido de dano
	    sonidoHerido.play();
	
	    // Comprobar si se quedo sin vidas
	    if (getVidas() <= 0) {
	        destruida = true;
	    }
	
	    return true;
	}

    
    public boolean estaDestruido() {
       return !herido && destruida;
    }
    public boolean estaHerido() {
 	   return herido;
    }
    
    public int getVidas() {
    	 return EstadoPartida.getInstance().getVidas();
    }
    //public boolean isDestruida() {return destruida;}
    public int getX() {return (int) spr.getX();}
    public int getY() {return (int) spr.getY();}
	public void setVidas(int vidas2) {
		EstadoPartida.getInstance().setVidas(vidas2);
	}
}
