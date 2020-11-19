package world;

import utils.Vector2;

public class GameObject {
    public Vector2 position, dimension, direction, plane;

    private Map map;

    public GameObject(Map map, double x, double y) {
        this.position = new Vector2(x, y);
        this.dimension = new Vector2(0.25,0.25);
        // used if this entity will be used as a camera
        this.direction = new Vector2(1.0, 0);
        this.plane = new Vector2(0.0, 0.66);
        //init map
        this.map = map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public void move(double forward, double side) {
        Vector2 dir = this.direction.getNormalized().times(forward);
        Vector2 sideV = this.plane.getNormalized().times(side);
        Vector2 movement = dir.add(sideV);
        // try to add the x movement
        if (map.get((int)(this.position.x + movement.x), (int)this.position.y) <= 0)
            this.position.x += movement.x;
        if (map.get((int)this.position.x,(int)(this.position.y + movement.y)) <= 0)
            this.position.y += movement.y;
    }

    public void rotate(double theta) {
        this.direction.rotate(theta);
        this.plane.rotate(theta);
    }

}