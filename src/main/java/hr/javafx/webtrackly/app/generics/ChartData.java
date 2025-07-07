package hr.javafx.webtrackly.app.generics;

/**
 * Generička klasa koja predstavlja podatke za grafikon.
 * Sadrži dva generička tipa podataka: D1 i D2.
 *
 * @param <D1> Tip podataka za x-os
 * @param <D2> Tip podataka za y-os
 */

public class ChartData<D1, D2>{
    private D1 x;
    private D2 y;

    public ChartData(D1 x, D2 y) {
        this.x = x;
        this.y = y;
    }

    public D1 getX() {
        return x;
    }

    public void setX(D1 x) {
        this.x = x;
    }

    public D2 getY() {
        return y;
    }

    public void setY(D2 y) {
        this.y = y;
    }

}
