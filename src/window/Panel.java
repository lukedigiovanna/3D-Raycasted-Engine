package window;

import java.awt.*;
import javax.swing.JPanel;

public class Panel extends JPanel {
    private Window window;

    public Panel(Window window) {
        this.window = window;
        this.setFocusable(true);
    }

    public void paintComponent(Graphics g) {
        g.drawImage(window.getScreen().get(), 0, 0, getWidth(), getHeight(), null);
    }
}
