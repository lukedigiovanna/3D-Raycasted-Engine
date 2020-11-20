package world;

import render.Texture;

/**
 * A sprite is a game object with a texture that will be rendered. This is
 * useful for things like enemies or other objects in the world
 * 
 * Keep in mind that sprites are assumed to have the same width and height that
 * walls do To make a smaller sprite use
 */
public class Sprite extends GameObject {
    public static int CENTER = 0, FLOOR = 1, CEILING = 2;

    private Texture texture;
    public double xScale, yScale;
    public int renderPos = FLOOR;

    public Sprite(Texture texture, Map map, double x, double y) {
        this(texture, map, x, y, 1.0, 1.0);
    }

    public Sprite(Texture texture, Map map, double x, double y, double xScale, double yScale) {
        super(map, x, y);
        this.texture = texture;
        this.xScale = xScale;
        this.yScale = yScale;
    }

    public Texture getTexture() {
        return this.texture;
    }
    
}
