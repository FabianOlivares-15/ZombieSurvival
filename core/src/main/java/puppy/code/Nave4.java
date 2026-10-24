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
    private int vidas = 3;
    private float xVel = 0;
    private float yVel = 0;
    private Sprite spr;
    private Sound sonidoHerido;
    private Sound soundBala;
    private Texture txBala;
    private boolean herido = false;
    private int tiempoHeridoMax=50;
    private int tiempoHerido;
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
    public void draw(SpriteBatch batch, PantallaJuego juego){
        float x =  spr.getX();
        float y =  spr.getY();

		if (!herido) {
		
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
		        
		        direccionX = dx;
		        direccionY = dy;
		        
		        float angulo = MathUtils.atan2(-direccionX,  direccionY) * MathUtils.radiansToDegrees;
		        spr.setRotation(angulo);
		    }
		
		    // Calcular nueva posicion
		    float nuevaX = spr.getX() + dx * velocidad * delta;
		    float nuevaY = spr.getY() + dy * velocidad * delta;
		
		    // Mantener personaje dentro de la ventana
		    nuevaX = MathUtils.clamp(
		            nuevaX, 0, Gdx.graphics.getWidth() - spr.getWidth());
		
		    nuevaY = MathUtils.clamp(
		            nuevaY, 0, Gdx.graphics.getHeight() - spr.getHeight());
		
		    spr.setPosition(nuevaX, nuevaY);
		
		    spr.draw(batch);
		
		
		}else {
           spr.setX(spr.getX()+MathUtils.random(-2,2));
 		   spr.draw(batch); 
 		  spr.setX(x);
 		   tiempoHerido--;
 		   if (tiempoHerido<=0) herido = false;
 		 }
        // disparo
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {         
        	float velocidadBala = 5f;

            // Direccion del disparo
            float velocidadX = direccionX * velocidadBala;
            float velocidadY = direccionY * velocidadBala;

            // Centro del personaje
            float centroX = spr.getX() + spr.getWidth() / 2;
            float centroY = spr.getY() + spr.getHeight() / 2;

            // La bala aparece delante del personaje
            float balaX = centroX + direccionX * 25 - txBala.getWidth() / 2f;
            float balaY = centroY + direccionY * 25 - txBala.getHeight() / 2f;

            Bullet bala = new Bullet(balaX, balaY, velocidadX, velocidadY, txBala);

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
            vidas--;
            herido = true;
  		    tiempoHerido=tiempoHeridoMax;
  		    sonidoHerido.play();
            if (vidas<=0) 
          	    destruida = true; 
            return true;
        }
        return false;
    }
    
    public boolean estaDestruido() {
       return !herido && destruida;
    }
    public boolean estaHerido() {
 	   return herido;
    }
    
    public int getVidas() {return vidas;}
    //public boolean isDestruida() {return destruida;}
    public int getX() {return (int) spr.getX();}
    public int getY() {return (int) spr.getY();}
	public void setVidas(int vidas2) {vidas = vidas2;}
}
