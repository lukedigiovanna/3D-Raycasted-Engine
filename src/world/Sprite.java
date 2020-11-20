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
    private Texture texture;

    public Sprite(Texture texture, Map map, double x, double y) {
        super(map, x, y);
        this.texture = texture;
    }

    public Texture getTexture() {
        return this.texture;
    }
    
}
