package utils;

public class Transform {
    public double x, y;
    public double width, height;
    public double rotation;

    public Transform(double x, double y) {
        this(x, y, 1, 1);
    }

    public Transform(double x, double y, double width, double height) {
        this(x,y,width,height,0);
    }

    public Transform(double x, double y, double width, double height, double rotation) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.rotation = rotation;
    }
}
