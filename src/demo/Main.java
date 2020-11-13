package demo;

import window.*;
import window.Window;
import render.*;

import java.awt.*;

public class Main {
    public static void main(String[] args) {
        Window window = new Window("Test");
        Screen screen = new Screen();
        window.setScreen(screen);

        Graphics2D g = screen.getGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, screen.getWidth(), screen.getHeight());
        g.setColor(Color.BLUE);
        g.fillOval(100,100,200,200);
        window.render();
    }
}