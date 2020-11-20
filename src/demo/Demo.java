package demo;

import window.Window;

import java.awt.event.*;

import render.Renderer;
import render.Screen;
import render.Texture;
import render.TexturePack;
import utils.*;
import world.Map;
import world.Sprite;
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
        this.screen = new Screen(1920,1080);
        this.window.setScreen(this.screen);

        // set up a t-pack
        TexturePack tp = new TexturePack();
        Texture planks = new Texture("planks2x2.png");
        tp.add("diamond2x2.png");
        tp.add("mcstone2x2.png");
        tp.add(planks);
        tp.setFloorTex(new Texture("grass2x2.png"));
        tp.setCeilingTex(planks);
        Renderer.setTexturePack(tp);

        
        this.map = new Map("maze.txt");
        player = new GameObject(this.map, 1.5,1.5);
        this.sprites = new SpriteList();
        Texture creeper = new Texture("creeper.png", 64, 64);
        for (int i = 0; i < 35; i++) {
            Sprite s = new Sprite(creeper,map,Math.random()*map.width(),Math.random()*map.height(), 0.75, 0.75);
            s.renderPos = Sprite.FLOOR;
            sprites.add(s);
        }
        
        Loop updateLoop = new Loop(new Loopable() {
            public void loop(double dt) {
                update(dt);
                render();
            }
        }, 20);
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
        Renderer.render(this.screen, this.map, this.sprites, this.player);
        this.window.render();
    }

    public static void main(String[] args) {
        Demo demo = new Demo();
    }
}
