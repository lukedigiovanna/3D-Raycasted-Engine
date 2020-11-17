package world;

public class Player extends GameObject {
    
    public Player(double x, double y) {
        super(x,y);
        this.transform.width = 1;
        this.transform.height = 1;
    }

}