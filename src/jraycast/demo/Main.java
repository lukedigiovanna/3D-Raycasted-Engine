package jraycast.demo;

import jraycast.render.Renderer;
import jraycast.render.Screen;
import jraycast.render.TexturePack;
import jraycast.utils.Loop;
import jraycast.utils.Loopable;
import jraycast.window.Window;
import jraycast.world.GameObject;
import jraycast.world.Map;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static int WIDTH = 480, HEIGHT = 360;
    public static void main(String[] args0) {
        Window window = new Window("Demo Game");
        Screen screen = new Screen(WIDTH,HEIGHT);
        window.setScreen(screen);

        TexturePack tp = new TexturePack();
        tp.add("demo/images/cobblestone.png");
        tp.add("demo/images/mossycobblestone.png");
        tp.add("demo/images/waterycobblestone.png");
        tp.setFloorTex("demo/images/dirt.png");
        tp.setCeilingTex(Color.BLACK);
        Renderer.setTexturePack(tp);

        Map map = new Map("demo/rooms/crossroads.txt");
        String[] names = {"smallroom","turn","wetpath","crossroads","tsection"};
        int[] rots = {4,4,2,0,4};
        List<Map> rooms = new ArrayList<Map>();
        for (int i = 0; i < names.length; i++) {
            Map room = new Map("demo/rooms/"+names[i]+".txt");
            rooms.add(room);
            if (rots[i] == 2) 
                rooms.add(room.rotated(Map.ROT_90));
            else if (rots[i] == 4) {
                rooms.add(room.rotated(Map.ROT_90));
                rooms.add(room.rotated(Map.ROT_180));
                rooms.add(room.rotated(Map.ROT_270));
            }
        }
        
        List<int[]> exits;
        int[] dirs = {Map.EAST,Map.SOUTH,Map.WEST,Map.NORTH};
        int count = 0;
        while ((exits = map.exits()).size() > 0) {
            // choose the first exit
            int[] exit = exits.get((int)(Math.random()*exits.size()));
            Map nextRoom = rooms.get((int)(Math.random()*rooms.size()));
            // find all exits
            List<int[]> nextExits = nextRoom.exits();
            for (int[] nextExit : nextExits) 
                for (int dir : dirs)
                    if (map.connect(nextRoom, dir, exit, nextExit)) 
                        count = 0;
            count++;
            if (count > 2500) map.set(exit[0],exit[1],1);
        }

        GameObject player = new GameObject(map, map.width()/2+0.5, map.height()/2+2.5);
        Loop gameLoop = new Loop(new Loopable() {
            public void loop(double dt) {
                double forward = 0, side = 0, rot = 0;
                double moveSpeed = 1;
                if (window.input.keyDown(KeyEvent.VK_SHIFT)) moveSpeed *= 1.5;
                if (window.input.keyDown(KeyEvent.VK_W)) forward += moveSpeed;
                if (window.input.keyDown(KeyEvent.VK_S)) forward -= moveSpeed*0.5;
                if (window.input.keyDown(KeyEvent.VK_A)) side -= moveSpeed * 0.75;
                if (window.input.keyDown(KeyEvent.VK_D)) side += moveSpeed * 0.75;
                if (window.input.keyDown(KeyEvent.VK_LEFT)) rot -= 180;
                if (window.input.keyDown(KeyEvent.VK_RIGHT)) rot += 180;
                Renderer.hideHUD();
                if (window.input.keyDown(KeyEvent.VK_CONTROL)) Renderer.showHUD();
                player.move(forward * dt, side * dt);
                player.rotateDeg(rot * dt);

                Renderer.render(screen, map, player);
                window.render();
            }
        }, 20);
        gameLoop.start();
    }
}