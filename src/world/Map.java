package world;

public class Map {
    /**
     * 0 represents air, 1... represents a wall/texture
     */
    private int[][] map;

    public Map(int[][] map) {
        this.map = map;
    }

    public Map(int width, int height) {
        map = new int[width][height];
    }

    public void set(int x, int y, int val) {
        map[x][y] = val;
    }

    public int get(int x, int y) {
        return map[x][y];
    }

    public int width() {
        return map[0].length;
    }

    public int height() {
        return map.length;
    }

    public int[][] get() {
        return this.map;
    }
}
