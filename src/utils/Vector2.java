package utils;

public class Vector2 {

    public static final Vector2 X_AXIS = new Vector2(1,0), Y_AXIS = new Vector2(0,1);

    double x, y;
    
    public Vector2(double x, double y) {    
        this.x = x;
        this.y = y;
    }

    public double squaredMagnitude() {
        return this.x * this.x + this.y * this.y;
    }

    public double magnitude() {
        return Math.sqrt(squaredMagnitude());
    }

    public double dot(Vector2 vec) {
        return this.x * vec.x + this.y * vec.y;
    }

    public double angle() {
        double dot = this.dot(X_AXIS);
        double cos = dot/magnitude();
        double angle = Math.acos(cos);
        if (this.y < 0)
            angle = -angle + Math.PI * 2;
        return angle;
    }
}