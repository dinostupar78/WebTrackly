package hr.javafx.webtrackly.app.generics;

public class DataContainer<data>{
    private data data;

    public DataContainer(data data) {
        this.data = data;
    }

    public data getData() {
        return data;
    }

    public void setData(data data) {
        this.data = data;
    }
}
