package demo;

import render.Screen;
import render.Texture;
import render.TexturePack;
import utils.ImageTools;
import utils.Loop;
import utils.Loopable;
import window.Window;
import world.GameObject;
import world.Map;
import world.Sprite;
import world.SpriteList;
import render.Renderer;

import java.awt.Color; 
import java.awt.event.*;

public class EasyDemo {
    public static void main(String[] args) {
        // create a window desktop display
        Window window = new Window("Simple Demo");
        // make a screen to draw our game to
        Screen screen = new Screen(960,720);
        // assign the screeen to the window
        window.setScreen(screen);
        
        // set the textures
        TexturePack tp = new TexturePack();
        tp.add("wall.jpg"); // assigns a wall texture to the 1 value
        tp.setFloorTex("grass.jpg"); // make the floor grass
        tp.setCeilingTex(Color.CYAN); // make the ceiling blue to simulate sky
        Renderer.setTexturePack(tp);

        int[][] mapData = new int[25][25];
        for (int i = 0; i < 25; i++) { mapData[i][0] = 1; mapData[i][24] = 1; mapData[0][i] = 1; mapData[24][i] = 1; }
        Map map = new Map(mapData);
        
        SpriteList sprites = new SpriteList();
        for (int i = 0; i < 5; i++) 
            sprites.add(new Sprite(new Texture(ImageTools.getImage("creeper.png"), 64, 64), map, Math.random() * 10, Math.random() * 10));

        // create a player to be the base of the camera
        GameObject player = new GameObject(map, 2, 2);

        // generate the game loop
        Loop gameLoop = new Loop(new Loopable() {
            public void loop(double dt) {
                // handle basic player movement
                if (window.input.keyDown(KeyEvent.VK_W))
                    player.moveForward(dt);
                if (window.input.keyDown(KeyEvent.VK_S))
                    player.moveForward(-dt);
                if (window.input.keyDown(KeyEvent.VK_D))
                    player.rotateDeg(180 * dt);
                if (window.input.keyDown(KeyEvent.VK_A))
                    player.rotateDeg(-180 * dt);

                // render the raycasted scene to our screen.
                Renderer.render(screen, map, sprites, player); // use the our map to determine the scene and player for position
                // update the window to display the new screen
                window.render();
            }
        }, 20); // tell the loop to run at 20 iterations per second
        // start the game loop
        gameLoop.start();
    }
}