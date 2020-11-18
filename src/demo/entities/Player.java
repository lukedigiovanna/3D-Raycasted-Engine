package demo.entities;

import render.Camera;
import utils.Vector2;

public class Player {
    public Camera camera;

    private Vector2 position;

    public Player(double x, double y) {
        this.position = new Vector2(x, y);
        this.camera = new Camera(x, y);
    }

    public void move(double forward, double side) {
        Vector2 dir = this.camera.direction.getNormalized();
        dir.times(forward);
        this.position.add(dir);
        Vector2 sideV = this.camera.plane.getNormalized();
        sideV.times(side);
        this.position.add(sideV);

        this.camera.position = this.position;
    }
}
