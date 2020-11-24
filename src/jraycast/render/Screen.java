package jraycast.render;

import jraycast.utils.Program;
import java.awt.image.*;
import java.awt.*;

public class Screen {
    private BufferedImage screen;

    public Screen() {
        this(Program.DEFAULT_WINDOW_WIDTH, Program.DEFAULT_WINDOW_HEIGHT);
    }
    
    public Screen(int width, int height) {
        this.screen = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }

    public Graphics2D getGraphics() {
        return this.screen.createGraphics();
    }

    public BufferedImage get() {
        return this.screen;
    }

    public int width() {
        return screen.getWidth();
    }

    public int height() {
        return screen.getHeight();
    }

    public int getWidth() {
        return screen.getWidth();
    }

    public int getHeight() {
        return screen.getHeight();
    }
}
