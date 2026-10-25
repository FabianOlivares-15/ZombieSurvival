package puppy.code;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Color;


public class PantallaJuego implements Screen {

	private SpaceNavigation game;
	private OrthographicCamera camera;	
	private SpriteBatch batch;
	private Sound explosionSound;
	private Music gameMusic;
	private int score;
	private int ronda;
	private int velXAsteroides; 
	private int velYAsteroides; 
	private int cantAsteroides;
	
	private Nave4 nave;
	private ZombieRapido zombiePrueba;
	private ZombieNormal zombieNormal;
	private Texture texturaZombie;
	private  ArrayList<Ball2> balls1 = new ArrayList<>();
	private  ArrayList<Ball2> balls2 = new ArrayList<>();
	private  ArrayList<Bullet> balas = new ArrayList<>();


	public PantallaJuego(SpaceNavigation game, int ronda, int vidas, int score,  
			int velXAsteroides, int velYAsteroides, int cantAsteroides) {
		this.game = game;
		this.ronda = ronda;
		this.score = score;
		this.velXAsteroides = velXAsteroides;
		this.velYAsteroides = velYAsteroides;
		this.cantAsteroides = cantAsteroides;
		
		batch = game.getBatch();
		camera = new OrthographicCamera();	
		camera.setToOrtho(false, 800, 640);
		//inicializar assets; musica de fondo y efectos de sonido
		explosionSound = Gdx.audio.newSound(Gdx.files.internal("explosion.ogg"));
		explosionSound.setVolume(1,0.5f);
		gameMusic = Gdx.audio.newMusic(Gdx.files.internal("piano-loops.wav")); //
		
		gameMusic.setLooping(true);
		gameMusic.setVolume(0.5f);
		gameMusic.play();
		
	    // cargar imagen de la nave, 64x64   
	    nave = new Nave4(Gdx.graphics.getWidth()/2-50,30,new Texture(Gdx.files.internal("MainShip3.png")),
	    				Gdx.audio.newSound(Gdx.files.internal("hurt.ogg")), 
	    				new Texture(Gdx.files.internal("Rocket2.png")), 
	    				Gdx.audio.newSound(Gdx.files.internal("pop-sound.mp3"))); 
        nave.setVidas(vidas);
        texturaZombie = new Texture(Gdx.files.internal("aGreyMedium4.png"));


	     // Crear zombi rapido
	     zombiePrueba = new ZombieRapido(100, 500, texturaZombie);
	
	     // El zombi rapido sera rojo
	     zombiePrueba.setColor(Color.RED);
	
	     // Crear zombi normal
	     zombieNormal = new ZombieNormal(600, 400, texturaZombie);
	
	     // El zombi normal sera verde
	     zombieNormal.setColor(Color.GREEN);

        //crear asteroides
        Random r = new Random();
	    for (int i = 0; i < cantAsteroides; i++) {
	        Ball2 bb = new Ball2(r.nextInt((int)Gdx.graphics.getWidth()),
	  	            50+r.nextInt((int)Gdx.graphics.getHeight()-50),
	  	            20+r.nextInt(10), velXAsteroides+r.nextInt(4), velYAsteroides+r.nextInt(4), 
	  	            new Texture(Gdx.files.internal("aGreyMedium4.png")));	   
	  	    balls1.add(bb);
	  	    balls2.add(bb);
	  	}
	}
    
	public void dibujaEncabezado() {
		CharSequence str = "Vidas: "+nave.getVidas()+" Ronda: "+ronda;
		game.getFont().getData().setScale(2f);		
		game.getFont().draw(batch, str, 10, 30);
		game.getFont().draw(batch, "Score:"+this.score, Gdx.graphics.getWidth()-150, 30);
		game.getFont().draw(batch, "HighScore:"+game.getHighScore(), Gdx.graphics.getWidth()/2-100, 30);
	}
	@Override
	public void render(float delta) {
		  Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
          batch.begin();
		  dibujaEncabezado();
		      // colisiones entre balas y asteroides y su destruccion  

			for (int i = 0; i < balas.size(); i++) {
			
			    Bullet b = balas.get(i);
			    b.update();
			
			    // Comprobar colisiones de balas con asteroides
			    for (int j = 0; j < balls1.size(); j++) {
			
			        if (b.checkCollision(balls1.get(j))) {
			            explosionSound.play();
			            balls1.remove(j);
			            balls2.remove(j);
			            j--;
			            score += 10;
			        }
			    }
			
			    // Eliminar balas destruidas
			    if (b.isDestroyed()) {
			        balas.remove(b);
			        i--;
			    }
			
			} // IMPORTANTE: aqui termina el for de las balas
			
			// Los asteroides mantienen su comportamiento original
			if (!nave.estaHerido()) {
			
			    // Actualizar movimiento de asteroides
			    for (Ball2 ball : balls1) {
			        ball.update();
			    }
			
			    // Colisiones entre asteroides y sus rebotes
			    for (int i = 0; i < balls1.size(); i++) {
			
			        Ball2 ball1 = balls1.get(i);
			
			        for (int j = 0; j < balls2.size(); j++) {
			
			            Ball2 ball2 = balls2.get(j);
			
			            if (i < j) {
			                ball1.checkCollision(ball2);
			            }
			        }
			    }
			
			} // Aqui termina el if de proteccion
			
			// Dibujar balas
			for (Bullet b : balas) {
			    b.draw(batch);
			}
			
			// Dibujar jugador
			nave.draw(batch, this);

	   // Obtener posicion actual del jugador
	      Vector2 posicionJugador = new Vector2(nave.getX() + 22.5f, nave.getY() + 22.5f);


		   // ZOMBI RAPIDO
		   if (!zombiePrueba.estaEliminado()) {
	
		       zombiePrueba.actualizar(delta, posicionJugador);
		       zombiePrueba.dibujar(batch);
	
		       nave.recibirDanioZombie(zombiePrueba);
		   }
	
		   // ZOMBI NORMAL
		   if (!zombieNormal.estaEliminado()) {
	
		       zombieNormal.actualizar(delta, posicionJugador);
		       zombieNormal.dibujar(batch);
	
		       nave.recibirDanioZombie(zombieNormal);
		   }
	
		   // COMPROBAR DISPAROS CONTRA ZOMBIS
		   for (int i = balas.size() - 1; i >= 0; i--) {
	
		       Bullet bala = balas.get(i);
	
		       boolean impacto = false;
	
		       // Comprobar impacto contra el zombi rapido
		       if (!zombiePrueba.estaEliminado()) {
		           impacto = bala.checkCollision(zombiePrueba);
		       }
	
		       // Comprobar impacto contra el zombi normal
		       if (!impacto && !zombieNormal.estaEliminado()) {
		           impacto = bala.checkCollision(zombieNormal);
		       }
	
		       // Si la bala elimino a un zombi
		       if (impacto) {
	
		           explosionSound.play();
	
		           score += 10;
	
		           balas.remove(i);
		       }
		   }


	      //dibujar asteroides y manejar colision con nave
	      for (int i = 0; i < balls1.size(); i++) {
	    	    Ball2 b=balls1.get(i);
	    	    b.draw(batch);
		          //perdió vida o game over
	              if (nave.checkCollision(b)) {
		            //asteroide se destruye con el choque             
	            	 balls1.remove(i);
	            	 balls2.remove(i);
	            	 i--;
              }   	  
  	        }
	      
	      if (nave.estaDestruido()) {
  			if (score > game.getHighScore())
  				game.setHighScore(score);
	    	Screen ss = new PantallaGameOver(game);
  			ss.resize(1200, 800);
  			game.setScreen(ss);
  			dispose();
  		  }
	      batch.end();
	      //nivel completado
	      if (balls1.size()==0) {
			Screen ss = new PantallaJuego(game,ronda+1, nave.getVidas(), score, velXAsteroides+3, velYAsteroides+3, cantAsteroides+10);
			ss.resize(1200, 800);
			game.setScreen(ss);
			dispose();
		  }
	    	 
	}
    
    public boolean agregarBala(Bullet bb) {
    	return balas.add(bb);
    }
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		gameMusic.play();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		this.explosionSound.dispose();
		this.gameMusic.dispose();
		texturaZombie.dispose();
	}
   
}
