package puppy.code;

/** Decide la composicion y el orden de construccion de cada oleada. */
public class DirectorOleadas {

    private final OleadaBuilder builder;

    public DirectorOleadas(OleadaBuilder builder) {
        if (builder == null) {
            throw new IllegalArgumentException("El builder no puede ser null");
        }
        this.builder = builder;
    }

    public Oleada construirOleada(int numeroOleada) {
        if (numeroOleada < 1) {
            throw new IllegalArgumentException("El numero de oleada debe ser mayor que cero");
        }

        builder.reset();
        // Un zombi normal adicional en todas las oleadas.
        // Cada tres oleadas, se suma otro zombi rapido.
        int normales = 2 + numeroOleada * 2;
        int rapidos = 1 + numeroOleada + numeroOleada / 3;
        builder.agregarNormales(normales);
        builder.agregarRapidos(rapidos);
        return builder.getResult();
    }
}
