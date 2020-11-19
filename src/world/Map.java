package world;

import java.io.*;

public class Map {
    /**
     * 0 represents air, 1... represents a wall/texture
     */
    private int[][] map;

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

        for (int y = 0; y < this.height(); y++) {
            for (int x = 0; x < this.width(); x++) {
                System.out.print(this.get(x,y)+" ");    
            }
            System.out.println();
        }
    }

    public Map(int width, int height) {
        map = new int[width][height];
    }

    public void set(int x, int y, int val) {
        map[x][y] = val;
    }

    public int get(int x, int y) {
        if (x < 0 || y < 0 || x > width()-1 || y > height()-1)
            return -1;
        else
            return map[x][y];
    }

    public int width() {
        return map.length;
    }

    public int height() {
        return map[0].length;
    }

    public int[][] get() {
        return this.map;
    }
}
