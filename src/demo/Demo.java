package demo;

import window.*;
import render.Screen;
import world.Map;

public class Demo {
    private Screen screen;
    private Window window;

    private Map map;

    public Demo() {
        this.window = new Window("Demo");
        this.screen = new Screen();
        this.window.setScreen(this.screen);

        int[][] map = {
            {0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0},
        };

        this.map = new Map(map);
    }
}
