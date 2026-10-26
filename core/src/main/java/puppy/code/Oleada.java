package puppy.code;

import java.util.ArrayList;

public class Oleada {

    private ArrayList<Zombie> zombies;

    public Oleada() {
        zombies = new ArrayList<Zombie>();
    }

    public void agregarZombie(Zombie zombie) {
        zombies.add(zombie);
    }

    public ArrayList<Zombie> getZombies() {
        return new ArrayList<Zombie>(zombies);
    }

    public int getCantidadZombies() {
        return zombies.size();
    }
}
