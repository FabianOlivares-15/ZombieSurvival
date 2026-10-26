package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

/** Catalogo de personajes: admite PNG propios sin cambiar el codigo. */
public final class Personajes {
    public static final int TOTAL = 4;
    private static final Color[] COLORES_PROVISIONALES = {
            new Color(0.55f, 0.88f, 1f, 1f),
            new Color(1f, 0.73f, 0.56f, 1f),
            new Color(0.70f, 1f, 0.68f, 1f),
            new Color(0.94f, 0.72f, 1f, 1f)
    };

    private Personajes() { }

    public static String nombre(int indice) {
        return "PERSONAJE " + (indice + 1);
    }

    public static String rutaImagen(int indice) {
        comprobar(indice);
        String ruta = "personaje" + (indice + 1) + ".png";
        return Gdx.files.internal(ruta).exists() ? ruta : "superviviente.png";
    }

    public static Color color(int indice) {
        comprobar(indice);
        // Cuando el usuario reemplace un PNG, respetar los colores originales.
        String ruta = "personaje" + (indice + 1) + ".png";
        return Gdx.files.internal(ruta).exists()
                ? Color.WHITE : COLORES_PROVISIONALES[indice];
    }

    private static void comprobar(int indice) {
        if (indice < 0 || indice >= TOTAL) {
            throw new IllegalArgumentException("Personaje inexistente: " + indice);
        }
    }
}
