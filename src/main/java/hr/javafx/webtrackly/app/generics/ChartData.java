package hr.javafx.webtrackly.app.generics;

public class ChartData<data1, data2>{
    private data1 x;
    private data2 y;

    public ChartData(data1 x, data2 y) {
        this.x = x;
        this.y = y;
    }

    public data1 getX() {
        return x;
    }

    public void setX(data1 x) {
        this.x = x;
    }

    public data2 getY() {
        return y;
    }

    public void setY(data2 y) {
        this.y = y;
    }

}
