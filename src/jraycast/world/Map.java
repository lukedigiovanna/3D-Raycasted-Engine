package jraycast.world;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Map {
    /**
     * 0 represents air, 1... represents a wall/texture
     */
    private int[][] map;

    /**
     * Generates a map from a given 2D array of raw values
     * @param map
     */
    public Map(int[][] map) {
        this.map = map;
    }

    /**
     * Generates a map from an inputted text file
     * @param fp
     */
    public Map(String fp) {
        try {
            File f = new File(fp);
            BufferedReader in = new BufferedReader(new FileReader(f));

            String row;
            int height = 0;
            int width = 0;
            while ((row = in.readLine()) != null) {
                width = row.split(" ").length;
                height++;
            }
            in.close(); 

            this.map = new int[width][height];
            in = new BufferedReader(new FileReader(f));
            int y = 0;
            while ((row = in.readLine()) != null) {
                String[] tokens = row.split(" ");
                for (int i = 0; i < tokens.length; i++) {
                    int val = Integer.parseInt(tokens[i]);
                    this.map[i][y] = val;
                }
                y++;
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Generates a blank map with the given width and height
     * @param width
     * @param height
     */
    public Map(int width, int height) {
        map = new int[width][height];
    }

    /**
     * Prints numerical representation to the console
     */
    public void print() {
        for (int y = 0; y < this.height(); y++) {
            for (int x = 0; x < this.width(); x++) {
                System.out.print(this.get(x,y)+" ");    
            }
            System.out.println();
        }
    }

    /**
     * Sets the value at the given x, y
     * @param x
     * @param y
     * @param val
     */
    public void set(int x, int y, int val) {
        map[x][y] = val;
    }

    /**
     * Returns the grid value at the given x, y for this map
     * Returns -1 if the x, y falls out of the map
     * @param x
     * @param y
     * @return
     */
    public int get(int x, int y) {
        if (x < 0 || y < 0 || x > width()-1 || y > height()-1)
            return -1;
        else
            return map[x][y];
    }

    public boolean hasWall(int x, int y) {
        int get = get(x, y);
        if (get == 0 || get == -9) // -9 represents a door
            return false;
        else 
            return true;
    }

    /**
     * Helper method for connect method which generates a new map
     * @param width
     * @param height
     */
    private int[][] newMap(int width, int height) {
        int[][] newMap = new int[width][height];
        for (int x = 0; x < width; x++) for (int y = 0; y < height; y++)
            newMap[x][y] = -1;
        return newMap;
    }

    public static final int EAST = 0, SOUTH = 1, WEST = 2, NORTH = 3;
    /**
     * Connects the maps together by their inputted connectors on the specified side
     * @param other The other map to connect to this one
     * @param side Indicates what side to join the connections at
     * @param connection1 Indicates the connection on THIS map
     * @param connection2 Indicates the connection on the OTHER map
     */
    public boolean connect(Map other, int side, int[] aConnection1, int[] aConnection2) {
        try {
        int yOffset, otherHeight, xOffset, otherWidth, width, height;
        int[][] newMap;
        Map m1 = this, m2 = other; // EAST, SOUTH SIDES
        int[] connection1 = {aConnection1[0],aConnection1[1]}, connection2 = {aConnection2[0], aConnection2[1]};
        if (side == WEST || side == NORTH) {
            // swap
            m2 = this; m1 = other;
            connection1 = new int[] {aConnection2[0], aConnection2[1]};
            connection2 = new int[] {aConnection1[0], aConnection1[1]};
        }
        switch (side) {
        case EAST: case WEST:
            yOffset = connection1[1] - connection2[1]; // indicates the relative y position
            otherHeight = (yOffset < 0) ? m2.height() : m2.height() + Math.abs(yOffset);
            height = (m1.height() > otherHeight) ? m1.height() : otherHeight;
            width = connection1[0] + 1 + m2.width() - connection2[0];
            newMap = new int[width][height];
            for (int x = 0; x < m1.width(); x++) {
                int startY = 0;
                if (yOffset < 0) startY = Math.abs(yOffset);
                for (int y = startY; y < startY + m1.height(); y++) 
                    newMap[x][y] = (x == connection1[0] && y - startY == connection1[1]) ? 0 : m1.get(x, y-startY);
            }
            for (int x = connection1[0]+1-connection2[0]; x < width; x++) {
                int startY = yOffset;
                if (yOffset < 0) startY = 0;
                for (int y = startY; y < startY + m2.height(); y++) {
                    if (newMap[x][y] != 0) return false; // we are adjusting an area already set by our other map
                    newMap[x][y] = (x - connection1[0] - 1 + connection2[0] == connection2[0] && y - startY == connection2[1]) ? 0 : m2.get(x - connection1[0] - 1 + connection2[0], y - startY);
                }
            }
            break;
        case SOUTH: case NORTH: // same exact code as above exact x/y and w/h are flipped
            xOffset = connection1[0] - connection2[0]; 
            otherWidth = (xOffset < 0) ? m2.width() : m2.width() + Math.abs(xOffset);
            width = (m1.width() > otherWidth) ? m1.width() : otherWidth;
            height = connection1[1] + 1 + m2.height() - connection2[1];
            newMap = new int[width][height];
            for (int y = 0; y < m1.height(); y++) {
                int startX = 0;
                if (xOffset < 0) startX = Math.abs(xOffset);
                for (int x = startX; x < startX + m1.width(); x++) 
                    newMap[x][y] = (x - startX == connection1[0] && y == connection1[1]) ? 0 : m1.get(x - startX, y);
            }
            for (int y = connection1[1]+1-connection2[1]; y < height; y++) {
                int startX = xOffset;
                if (xOffset < 0) startX = 0;
                for (int x = startX; x < startX + m2.width(); x++) {
                    if (newMap[x][y] != 0) return false; // we are adjusting an area already set by our other map
                    newMap[x][y] = (x - startX == connection2[0] && y - connection1[1] - 1 + connection2[1] == connection2[1]) ? 0 : m2.get(x - startX, y - connection1[1] - 1 + connection2[1]);
                }
            }
            break;
        default:
            newMap = this.map;
        }
        this.map = newMap;
        return true; // made it through successfully
        } catch (Exception e) {return false;}
    }

    /**
     * Returns a list where value is an array holding
     * the 2D coordinates of an exit to the given room
     * Exits are defined as any 0 on an edge that has two adjacent walls
     */
    public List<int[]> exits() {
        List<int[]> exits = new ArrayList<int[]>();
        for (int x = 0; x < this.width(); x++) 
            for (int y = 0; y < this.height(); y++)
                if (get(x, y) == -9)
                    exits.add(new int[] { x, y });
        return exits;
    }

    public static final int ROT_90 = 0, ROT_180 = 1, ROT_270 = 2;
    /**
     * Rotates the map the amount specified to the right (90, 180, 270)
     * @param amount
     */
    public Map rotated(int amount) {
        Map toRotate = this;
        for (int i = 0; i < amount + 1; i++) {
            toRotate = rotated(toRotate);
        }
        return toRotate;
    }

    /**
     * Rotates the map 90deg to the right.
     */
    private static Map rotated(Map toRotate) {
        int[][] buffer = new int[toRotate.height()][toRotate.width()];
        for (int x = 0; x < toRotate.width(); x++) 
            for (int y = 0; y < toRotate.height(); y++)
                buffer[toRotate.height()-1-y][x] = toRotate.map[x][y];
        return new Map(buffer);
    }

    /**
     * Returns the width of this map grid    
     * @return
     */
    public int width() {
        return map.length;
    }

    /**
     * Returns the height of this map grid
     * @return
     */
    public int height() {
        return map[0].length;
    }

    /**
     * Returns the raw 2D array of this map
     * @return
     */
    public int[][] get() {
        return this.map;
    }
}
