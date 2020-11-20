package demo;

import render.Screen;
import render.TexturePack;
import utils.Loop;
import utils.Loopable;
import window.Window;
import world.GameObject;
import world.Map;
import render.Renderer;

import java.awt.Color; 
import java.awt.event.*;

public class EasyDemo {
    public static void main(String[] args) {
        // create a window desktop display
        Window window = new Window("Simple Demo");
        // make a screen to draw our game to
        Screen screen = new Screen(480,360);
        // assign the screeen to the window
        window.setScreen(screen);
        
        // set the textures
        TexturePack tp = new TexturePack();
        tp.add("wall.jpg"); // assigns a wall texture to the 1 value
        tp.setFloorTex("grass.png"); // make the floor grass
        tp.setCeilingTex(Color.CYAN); // make the ceiling blue to simulate sky
        Renderer.setTexturePack(tp);

        // generate a map
        int[][] mapData = {
            {0, 0, 1, 1, 1},
            {1, 0, 0, 0, 1},
            {1, 0, 0, 0, 1},
            {1, 0, 0, 0, 1},
            {1, 1, 1, 1, 1},
        };
        Map map = new Map(mapData);

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
                Renderer.render(screen, map, player); // use the our map to determine the scene and player for position
                // update the window to display the new screen
                window.render();
            }
        }, 20); // tell the loop to run at 20 iterations per second
        // start the game loop
        gameLoop.start();
    }
}