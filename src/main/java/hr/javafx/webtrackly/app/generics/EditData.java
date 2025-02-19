package hr.javafx.webtrackly.app.generics;

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
