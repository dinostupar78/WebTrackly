package hr.javafx.webtrackly.app.generics;

public class EditContainer<T>{
    private T data;

    public EditContainer(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
