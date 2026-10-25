package puppy.code;

public class DirectorOleadas {

    private OleadaBuilder builder;

    public DirectorOleadas(OleadaBuilder builder) {
        this.builder = builder;
    }

    public Oleada construirOleada(int numeroOleada) {

        if (numeroOleada < 1) {
            throw new IllegalArgumentException(
                    "El numero de oleada debe ser mayor que cero");
        }

        // Comenzar una nueva construccion
        builder.reset();

        // Aumentar enemigos segun la oleada
        int normales = 1 + numeroOleada * 2;
        int rapidos = 1 + numeroOleada;

        // Definir el orden de construccion
        builder.agregarNormales(normales);
        builder.agregarRapidos(rapidos);

        // Obtener el producto construido
        return builder.getResult();
    }
}
