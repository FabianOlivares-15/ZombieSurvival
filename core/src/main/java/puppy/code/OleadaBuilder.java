package puppy.code;

public interface OleadaBuilder {

    void reset();

    void agregarNormales(int cantidad);

    void agregarRapidos(int cantidad);

    Oleada getResult();
}
