package demo;

import window.*;
import window.Window;

import java.awt.*;

import render.Screen;
import utils.Timer;
import world.Map;
import world.*;

public class Demo {
    private Screen screen;
    private Window window;

    private Map map;

    private Player player;

    public Demo() {
        this.window = new Window("Demo");
        this.screen = new Screen();
        this.window.setScreen(this.screen);

        int[][] map = {
            {0, 0, 0, 0, 0, 0, 0},
            {0, 1, 0, 0, 0, 0, 0},
            {0, 1, 0, 0, 0, 0, 0},
            {0, 1, 1, 1, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0},
        };

        this.map = new Map(map);
        
        Thread updateThread = new Thread(new Runnable() {
        	Timer timer = new Timer();
        	public void run() {
        		while (true) {
        			double dt = timer.mark();
					long before = System.currentTimeMillis();
					update(dt);
					double addt = (System.currentTimeMillis() - before) / 1000.0;
					timer.wait(0.05 - addt);
        		}
        	}
        });
        updateThread.start();
        Thread renderThread = new Thread(new Runnable() {
        	Timer timer = new Timer();
        	public void run() {
        		while (true) {
        			double dt = timer.mark();
					long before = System.currentTimeMillis();
					render();
					double addt = (System.currentTimeMillis() - before) / 1000.0;
					timer.wait(0.033 - addt);
        		}
        	}
        });
        renderThread.start();
    }
    
    private void update(double dt) {
    	System.out.println(dt);
    }
    
    private void render() {
        Graphics2D g = this.screen.getGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0,0,this.screen.width(),this.screen.height());
        this.window.render();
        g.setColor(Color.BLACK);
        int size = 25;
        int width = size * map.width();
        int height = size * map.height();
        g.fillRect(0,0,width,height);
        int[][] rawMap = map.get();
        for (int x = 0; x < map.width(); x++) {
            for (int y = 0; y < map.height(); y++) {
                if (map.get(x,y) > 0)
                    g.setColor(Color.BLUE);
                else
                    g.setColor(Color.WHITE);
                g.fillRect(x * size + 1, y * size + 1, size - 2, size - 2);
            }
        }
    }
}
