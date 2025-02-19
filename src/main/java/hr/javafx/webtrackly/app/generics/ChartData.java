package hr.javafx.webtrackly.app.generics;

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
