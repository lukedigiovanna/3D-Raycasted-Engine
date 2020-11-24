package jraycast.utils;

import java.awt.*;

public class Program {
    private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    public static final int SCREEN_WIDTH = screenSize.width, 
                            SCREEN_HEIGHT = screenSize.height;
    public static final int DEFAULT_WINDOW_WIDTH = Program.SCREEN_WIDTH/2, 
                            DEFAULT_WINDOW_HEIGHT = (int)((Program.SCREEN_WIDTH/2)/Program.DEFAULT_ASPECT_RATIO);
    public static final double DEFAULT_ASPECT_RATIO = 1.33333333; // W:H

    public static final String JRAYCAST_VERSION = "JRaycast Game Engine 1.0.0";

}
