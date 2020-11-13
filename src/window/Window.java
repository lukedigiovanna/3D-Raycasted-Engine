package window;

import javax.swing.*;

import utils.Program;
import render.Screen;

public class Window {
    private JFrame frame;
    private JPanel panel;
    private Screen screen;

    public Window(String name) {
        this(name, Program.DEFAULT_WINDOW_WIDTH, Program.DEFAULT_WINDOW_HEIGHT);
    }

    public Window(String name, int width, int height) {
        frame = new JFrame(name);
        frame.setSize(width, height);
        frame.setLocation((Program.SCREEN_WIDTH-width)/2, (Program.SCREEN_HEIGHT-height)/2);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel = new Panel(this);
        frame.setContentPane(panel);
        frame.setVisible(true);
    }

    public void setScreen(Screen screen) {
        this.screen = screen;
    }

    public Screen getScreen() {
        return this.screen;
    }

    public void render() {
        panel.repaint();
    }

    /**
     * Forcibly closes the application
     */
    public void close() {
        System.exit(0);
    }
}