package world;

import utils.Vector2;

public class Player extends GameObject {
    
    public Vector2 position;
    public Vector2 direction;
    public Vector2 plane;

    public Player(double x, double y) {
        super(x,y);
        this.position = new Vector2(x,y);
        this.direction = new Vector2(1,0);
        this.plane = new Vector2(0,0.66);
    }

    public void rotate(double theta) {
        this.direction.rotate(theta);
        this.plane.rotate(theta);
    }

    public double getX() {
        return this.position.x;
    }

    public double getY() {
        return this.position.y;
    }

    public void move(double forward, double side) {
        Vector2 dir = this.direction.getNormalized();
        dir.times(forward);
        this.position.add(dir);
        Vector2 sideV = this.plane.getNormalized();
        sideV.times(side);
        this.position.add(sideV);
    }

}