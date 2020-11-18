package world;

import utils.Transform;

public class GameObject {
    
    public Transform transform;

    public GameObject(double x, double y) {
        this.transform = new Transform(x, y);
    }
}