package hr.javafx.webtrackly.app.generics;

/**
 * Klasa koja predstavlja generički objekt za uređivanje podataka.
 * Omogućuje pohranu i manipulaciju podacima tipa T.
 *
 * @param <T> Tip podataka koji se pohranjuje u objektu.
 */

public class EditData<T>{
    private T data;

    public EditData(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
