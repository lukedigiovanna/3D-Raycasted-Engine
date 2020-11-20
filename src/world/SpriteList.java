package world;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores a list of all sprites in a game
 */
public class SpriteList {
    private List<Sprite> sprites;

    public SpriteList() {
        sprites = new ArrayList<Sprite>();
    }

    public void add(Sprite sprite) {
        this.sprites.add(sprite);
    }

    /**
     * Sorts the sprites with the furthest away from the inputted position first
     * and the closest ones at the end.
     * @param position
     * @return
     */
    public Sprite[] getOrdererd(GameObject position) {
        final int N = sprites.size();
        double[] distances = new double[N];
        for (int i = 0; i < N; i++) {
            Sprite s = sprites.get(i);
            distances[i] = (s.position.x - position.position.x) * (s.position.x - position.position.x) + (s.position.y - position.position.y) * (s.position.y - position.position.y);
        }
        int[] ordered = new int[N];
        
        Sprite[] spritesOrdered = new  Sprite[N];
        for (int i = 0; i < ordered.length; i++)
            spritesOrdered[ordered[i]] = sprites.get(i);
        return spritesOrdered;
    }
}
