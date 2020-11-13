package demo;

import window.*;
import render.Screen;
import utils.Timer;
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
        
        Thread updateThread = new Thread(new Runnable() {
        	Timer timer = new Timer();
        	public void run() {
        		while (true) {
        			double dt = timer.mark();
					long before = System.currentTimeMillis();
					update(dt);
					double addt = (System.currentTimeMillis() - before) / 1000.0f;
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
					double addt = (System.currentTimeMillis() - before) / 1000.0f;
					timer.wait(0.033 - addt);
        		}
        	}
        });
        renderThread.start();
    }
    
    private void update(double dt) {
    	
    }
    
    private void render() {
    	
    }
}
