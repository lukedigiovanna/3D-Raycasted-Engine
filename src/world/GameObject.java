package world;

import utils.Vector2;

public class GameObject {
    public Vector2 position, dimension, direction, plane;

    private Map map;

    /**
     * Constructs a game object with a given map for collision and an initial position
     * @param map Map to start with
     * @param x initial x position
     * @param y initial y position
     */
    public GameObject(Map map, double x, double y) {
        this.position = new Vector2(x, y);
        this.dimension = new Vector2(0.25,0.25);
        // used if this entity will be used as a camera
        this.direction = new Vector2(1.0, 0);
        this.plane = new Vector2(0.0, 0.66);
        //init map
        this.map = map;
    }

    /**
     * Sets the current map to use
     * Determines where the object can and cannot move to
     * @param map
     */
    public void setMap(Map map) {
        this.map = map;
    }

    /**
     * Moves the object forward the specified distance.
     * Handles collision.
     * Enter a negative value to move backward
     * @param forward
     */
    public void moveForward(double forward) {
        this.move(forward, 0);
    }

    /**
     * Moves the object forward the given forward amount and to the side the given side amount.
     * Positive numbers move the object forward or to the right.
     * Negative numbers move the object backward or to the left.
     * @param forward
     * @param side
     */
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

    /**
     * Rotates the object about its z-axis a given radians.
     * Adjusts the direction and planar vectors.
     * @param theta Radians to rotate
     */
    public void rotate(double radians) {
        this.direction.rotate(radians);
        this.plane.rotate(radians);
    }

    /**
     * Rotates the object about its z-axis a given degrees.
     * Adjusts the direction and  planar vectors.
     * @param degrees Degrees to rotate
     */
    public void rotateDeg(double degrees) {
        this.rotate(Math.toRadians(degrees));
    }

}