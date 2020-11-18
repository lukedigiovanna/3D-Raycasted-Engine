package render;

import utils.Vector2;

public class Camera {
    public Vector2 position, direction, plane;

    public Camera(double x, double y) {
        this.position = new Vector2(x,y);
        this.direction = new Vector2(1.0, 0);
        this.plane = new Vector2(0.0, 0.66);
    }

    public void rotate(double theta) {
        this.direction.rotate(theta);
        this.plane.rotate(theta);
    }
}
