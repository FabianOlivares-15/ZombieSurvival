package puppy.code;

/** Estado unico compartido durante una partida. */
public final class EstadoPartida {

    private static EstadoPartida instancia;

    private int puntaje;
    private int vidas;
    private int oleada;

    private EstadoPartida() {
        reiniciar();
    }

    public static EstadoPartida getInstance() {
        if (instancia == null) instancia = new EstadoPartida();
        return instancia;
    }

    public void reiniciar() {
        puntaje = 0;
        vidas = 3;
        oleada = 1;
    }

    public int getPuntaje() { return puntaje; }
    public int getVidas() { return vidas; }
    public int getOleada() { return oleada; }

    public void avanzarOleada() {
        oleada++;
    }

    public void sumarPuntos(int cantidad) {
        if (cantidad > 0) puntaje += cantidad;
    }

    public void perderVida() {
        if (vidas > 0) vidas--;
    }

    /** Un botiquin recupera una vida, sin superar el maximo inicial. */
    public boolean recuperarVida() {
        if (vidas >= 3) return false;
        vidas++;
        return true;
    }
}
