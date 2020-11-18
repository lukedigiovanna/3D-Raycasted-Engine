package demo;

import window.*;
import window.Window;

import java.awt.*;
import java.awt.event.*;

import render.Screen;
import utils.Timer;
import utils.Vector2;
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
            {1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 2, 0, 0, 0, 0, 0, 1},
            {1, 0, 2, 0, 0, 0, 0, 0, 1},
            {1, 0, 2, 2, 2, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1},
        };

        this.map = new Map(map);
        player = new Player(2.5,5.0);
        
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
					long before = System.currentTimeMillis();
                    render();
                    System.out.println("frame");
					double addt = (System.currentTimeMillis() - before) / 1000.0;
                    timer.wait(0.033 - addt);
        		}
        	}
        });
        renderThread.start();
    }
    
    private void update(double dt) {
        //System.out.println(dt);
        if (window.input.keyDown(KeyEvent.VK_LEFT)) {
            player.rotate(-Math.PI * 0.8 * dt);
        }
        if (window.input.keyDown(KeyEvent.VK_RIGHT)) {
            player.rotate(Math.PI * 0.8 * dt);
        }

        double forward = 0;
        double side = 0;
        if (window.input.keyDown(KeyEvent.VK_W)) 
            forward += 1;
        if (window.input.keyDown(KeyEvent.VK_S))
            forward -= 0.5;
        if (window.input.keyDown(KeyEvent.VK_A))
            side -= 0.75;
        if (window.input.keyDown(KeyEvent.VK_D))
            side += 0.75;
        forward *= dt;
        side *= dt;
        player.move(forward,side);
    }
    
    private void render() {
        Graphics2D g = this.screen.getGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0,0,this.screen.width(),this.screen.height());
        
        for (int x = 0; x < screen.width(); x++) {
            double cameraX = 2 * x / (double)screen.width() - 1.0;
            Vector2 rayDir = new Vector2(player.direction.x + player.plane.x * cameraX, player.direction.y + player.plane.y * cameraX);
            // starting box
            int mapX = (int)player.position.x, mapY = (int)player.position.y;
            
            //length of ray from current x/y to next x/y
            double sideDistX, sideDistY;

            //length of ray from one x/y to next x/y
            double deltaDistX = Math.abs(1.0 / rayDir.x),
                    deltaDistY = Math.abs(1.0 / rayDir.y);

            double perpWallDist;

            // which direction to step in (+1 or -1)
            int stepX, stepY;

            int side = 0;

            if (rayDir.x < 0) {
                stepX = -1;
                sideDistX = (player.position.x - mapX) * deltaDistX;
            } else {
                stepX = 1;
                sideDistX = (mapX + 1.0 - player.position.x) * deltaDistX;
            }
            if (rayDir.y < 0) {
                stepY = -1;
                sideDistY = (player.position.y - mapY) * deltaDistY;
            } else {
                stepY = 1;
                sideDistY = (mapY + 1.0 - player.position.y) * deltaDistY; 
            }

            // DDA
            while (map.get(mapX,mapY) == 0) {
                if (sideDistX < sideDistY) {
                    sideDistX += deltaDistX;
                    mapX += stepX;
                    side = 0;
                } else {
                    sideDistY += deltaDistY;
                    mapY += stepY;
                    side = 1;
                }
            }

            if (side == 0)
                perpWallDist = (mapX - player.position.x + (1 - stepX) / 2) / rayDir.x;
            else
                perpWallDist = (mapY - player.position.y + (1 - stepY) / 2) / rayDir.y;

            int lineHeight = (int)(screen.height() / perpWallDist);
            int drawStart = -lineHeight/2 + screen.height()/2;
            if (drawStart < 0) drawStart = 0;
            int drawEnd = lineHeight / 2 + screen.height()/2;
            if (drawEnd >= screen.height()) drawEnd = screen.height() - 1;
            switch (map.get(mapX,mapY)) {
            case 1:
                g.setColor(Color.BLUE);
                break;
            case 2:
                g.setColor(Color.RED);
                break;
            }
            if (side == 1) {
                Color c = g.getColor();
                int r = c.getRed() / 2,
                    gr = c.getGreen() / 2,
                    b = c.getBlue() / 2; 
                g.setColor(new Color(r, gr, b));
            }
            g.drawLine(x, drawStart, x, drawEnd);
        }

        // int size = 40;
        // int width = size * map.width();
        // int height = size * map.height();
        // g.fillRect(0,0,width,height);
        // for (int x = 0; x < map.width(); x++) {
        //     for (int y = 0; y < map.height(); y++) {
        //         if (map.get(x,y) > 0)
        //             g.setColor(Color.BLACK);
        //         else
        //             g.setColor(Color.WHITE);
        //         g.fillRect(x * size + 1, y * size + 1, size - 2, size - 2);
        //     }
        // }
        // g.setColor(Color.GREEN);
        // int cx = (int)(player.transform.x * size), cy = (int)(player.transform.y * size);
        // g.fillOval(cx - size/4, cy - size/4, size/2, size/2);


        this.window.render();
    }
}
