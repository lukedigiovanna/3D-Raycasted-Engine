package demo;

import window.Window;

import java.awt.event.*;

import render.Renderer;
import render.Screen;
import render.TexturePack;
import utils.*;
import world.Map;
import demo.entities.Player;
import java.awt.Color;

public class Demo {
    private Screen screen;
    private Window window;

    private Map map;

    private Player player;

    public Demo() {
        this.window = new Window("Demo");
        this.screen = new Screen();
        this.window.setScreen(this.screen);

        // set up a t-pack
        TexturePack tp = new TexturePack();
        tp.add("wall.jpg");
        tp.add("rid.jpg");
        Renderer.setTexturePack(tp);

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
        
        Loop updateLoop = new Loop(20, new Loopable() {
            public void loop(double dt) {
                update(dt);
            }
        });
        updateLoop.start();
        Loop renderLoop = new Loop(30, new Loopable() {
            public void loop(double dt) {
                render();
            }
        });
        renderLoop.start();
    }
    
    private void update(double dt) {
        if (window.input.keyDown(KeyEvent.VK_LEFT)) {
            player.camera.rotate(-Math.PI * 0.8 * dt);
        }
        if (window.input.keyDown(KeyEvent.VK_RIGHT)) {
            player.camera.rotate(Math.PI * 0.8 * dt);
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
        Renderer.render(this.screen, this.map, this.player.camera);
        this.window.render();


        

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
        // int cx = (int)(player.position.x * size), cy = (int)(player.position.y * size);
        // g.fillOval(cx - size/4, cy - size/4, size/2, size/2);


    }
}
