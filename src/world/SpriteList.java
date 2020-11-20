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
     * @param position The GameObject to calculate distance from
     * @return An array of sprites sorted from far to close
     */
    public Sprite[] getOrdered(GameObject position) {
        final int N = sprites.size();
        double[] distances = new double[N];
        for (int i = 0; i < N; i++) 
            distances[i] = sprites.get(i).squaredDistanceTo(position);
        int[] ordered = new int[N];
        // run selection sort
        for (int i = 0; i < N; i++) ordered[i] = i;
        for (int i = 0; i < N; i++) {
            int max = i;
            for (int j = i + 1; j < N; j++) 
                if (distances[j] > distances[max])
                    max = j;
            // swap
            double temp = distances[max];
            distances[max] = distances[i];
            distances[i] = temp;   
            int iTemp = ordered[i];
            ordered[i] = ordered[max];
            ordered[max] = iTemp;
        }       
        Sprite[] spritesOrdered = new Sprite[N];
        for (int i = 0; i < N; i++)
            spritesOrdered[i] = sprites.get(ordered[i]);
        return spritesOrdered;
    }
}
