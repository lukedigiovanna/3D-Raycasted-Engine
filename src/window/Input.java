package window;

import java.awt.event.*;
import java.util.HashMap;

import javax.swing.JPanel;

public class Input {
    
    private HashMap<Integer, Boolean> heldKeys;
    private KeyAdapter key;

    public Input() {
        this.heldKeys = new HashMap<Integer, Boolean>();
        key = new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                heldKeys.put(e.getKeyCode(), true);
            }
    
            public void keyReleased(KeyEvent e) {
                heldKeys.put(e.getKeyCode(), false);
            }
        };
    }

    public void link(JPanel panel) {
        panel.addKeyListener(key);
    }

    public boolean keyDown(int kc) {
        if (!this.heldKeys.containsKey(kc))
            return false;
        else
            return this.heldKeys.get(kc);
    }

}
