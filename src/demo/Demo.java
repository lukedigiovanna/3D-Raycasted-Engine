package demo;

import window.Window;

import java.awt.event.*;

import render.Renderer;
import render.Screen;
import render.Texture;
import render.TexturePack;
import utils.*;
import world.Map;
import world.SpriteList;
import world.GameObject;

public class Demo {
    private Screen screen;
    private Window window;

    private Map map;

    private GameObject player;
    private SpriteList sprites;

    public Demo() {
        this.window = new Window("Demo");
        this.screen = new Screen(720,480);
        this.window.setScreen(this.screen);

        // set up a t-pack
        TexturePack tp = new TexturePack();
        Texture planks = new Texture("planks.png");
        tp.add("mcstone.png");
        tp.add(planks);
        tp.setFloorTex(new Texture("grass.png"));
        tp.setCeilingTex(planks);
        Renderer.setTexturePack(tp);

        this.map = new Map("maze.txt");
        player = new GameObject(this.map, 1.5,1.5);
        this.sprites = new SpriteList();
        

        
        Loop updateLoop = new Loop(new Loopable() {
            public void loop(double dt) {
                update(dt);
                render();
            }
        }, 15);
        updateLoop.start();
    }
    
    private double moveSpeed = 1;
    private void update(double dt) {
        if (window.input.keyDown(KeyEvent.VK_LEFT)) {
            player.rotate(-Math.PI * 0.8 * dt);
        }
        if (window.input.keyDown(KeyEvent.VK_RIGHT)) {
            player.rotate(Math.PI * 0.8 * dt);
        }

        moveSpeed = 1;
        if (window.input.keyDown(KeyEvent.VK_SHIFT))
            moveSpeed = 2;
        double forward = 0;
        double side = 0;
        if (window.input.keyDown(KeyEvent.VK_W)) 
            forward += moveSpeed;
        if (window.input.keyDown(KeyEvent.VK_S))
            forward -= moveSpeed/2;
        if (window.input.keyDown(KeyEvent.VK_A))
            side -= moveSpeed * 0.75;
        if (window.input.keyDown(KeyEvent.VK_D))
            side += moveSpeed * 0.75;
        forward *= dt;
        side *= dt;
        player.move(forward,side);
    }
    
    private void render() {
        Renderer.render(this.screen, this.map, this.player);
        this.window.render();
    }

    public static void main(String[] args) {
        Demo demo = new Demo();
    }
}
