package puppy.code;

public class EstadoPartida {

    // Unica instancia de la clase
    private static EstadoPartida instancia;

    private int puntaje;
    private int vidas;
    private int oleada;

    // Constructor privado
    private EstadoPartida() {
        reiniciar();
    }

    // Metodo de acceso a la unica instancia
    public static EstadoPartida getInstance() {

        if (instancia == null) {
            instancia = new EstadoPartida();
        }

        return instancia;
    }

    // Reiniciar los datos para una nueva partida
    public void reiniciar() {
        puntaje = 0;
        vidas = 3;
        oleada = 1;
    }

    public int getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(int puntaje) {
        this.puntaje = puntaje;
    }

    public int getVidas() {
        return vidas;
    }

    public void setVidas(int vidas) {
        this.vidas = vidas;
    }

    public int getOleada() {
        return oleada;
    }

    // Avanzar a la siguiente oleada
    public void avanzarOleada() {
        oleada++;
    }

	 // Sumar puntos por eliminar enemigos
	 public void sumarPuntos(int cantidad) {
	
	     if (cantidad > 0) {
	         puntaje += cantidad;
	     }
	 }
	
	 // Restar una vida al jugador
	 public void perderVida() {
	
	     if (vidas > 0) {
	         vidas--;
	     }
	 }

}
