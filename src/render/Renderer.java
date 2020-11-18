package render;

import java.awt.*;

import utils.Vector2;
import world.Map;

public class Renderer {
    private static TexturePack texturePack;

    public static void setTexturePack(TexturePack aTexturePack) {
        texturePack = aTexturePack;
    }

    public static void render(Screen screen, Map map, Camera camera) {
        Graphics2D g = screen.getGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0,0,screen.width(),screen.height());
        
        for (int x = 0; x < screen.width(); x++) {
            double cameraX = 2 * x / (double)screen.width() - 1.0;
            Vector2 rayDir = new Vector2(camera.direction.x + camera.plane.x * cameraX, camera.direction.y + camera.plane.y * cameraX);
            // starting box
            int mapX = (int)camera.position.x, mapY = (int)camera.position.y;
            
            //length of ray from current x/y to next x/y
            double sideDistX, sideDistY;

            //length of ray from one x/y to next x/y
            double deltaDistX = Math.abs(1.0 / rayDir.x),
                    deltaDistY = Math.abs(1.0 / rayDir.y);

            double perpWallDist;

            // which direction to step in (+1 or -1)
            int stepX, stepY;

            int side = 0;

            if (rayDir.x < 0) {
                stepX = -1;
                sideDistX = (camera.position.x - mapX) * deltaDistX;
            } else {
                stepX = 1;
                sideDistX = (mapX + 1.0 - camera.position.x) * deltaDistX;
            }
            if (rayDir.y < 0) {
                stepY = -1;
                sideDistY = (camera.position.y - mapY) * deltaDistY;
            } else {
                stepY = 1;
                sideDistY = (mapY + 1.0 - camera.position.y) * deltaDistY; 
            }

            // DDA
            while (map.get(mapX,mapY) == 0) {
                if (sideDistX < sideDistY) {
                    sideDistX += deltaDistX;
                    mapX += stepX;
                    side = 0;
                } else {
                    sideDistY += deltaDistY;
                    mapY += stepY;
                    side = 1;
                }
            }

            if (side == 0)
                perpWallDist = (mapX - camera.position.x + (1 - stepX) / 2) / rayDir.x;
            else
                perpWallDist = (mapY - camera.position.y + (1 - stepY) / 2) / rayDir.y;

            // determining where to draw the line on this vertical stripe
            int lineHeight = (int)(screen.height() / perpWallDist);
            int center = screen.height()/2;
            int drawStart = -lineHeight/2 + center;
            if (drawStart < 0) drawStart = 0;
            int drawEnd = lineHeight / 2 + center;
            if (drawEnd >= screen.height()) drawEnd = screen.height() - 1;
            
            
            // actually rendering to the screen
            Texture tex = texturePack.get(map.get(mapX, mapY)-1);
            double wallX = (side == 0) ? camera.position.y + perpWallDist * rayDir.y : camera.position.x + perpWallDist * rayDir.x;
           // System.out.println(perpWallDist+" "+wallX);
            wallX -= Math.floor(wallX);
            int texX = (int)(wallX * tex.width());
            // if (side == 0 && rayDir.x < 0) texX = tex.width() - texX - 1;
            // if (side == 1 && rayDir.y < 0) texX = tex.width() - texX - 1;

            double step = (double)tex.height()/lineHeight;
            double texPos = (drawStart - screen.height()/2 + lineHeight/2) * step;
            for (int y = drawStart; y < drawEnd; y++) {
                int texY = (int)texPos & (tex.height() - 1);
                texPos += step;
               // System.out.println(texX+","+texY);
                int colVal = tex.get(texX,texY);
                Color c = new Color(colVal);
                if (side == 1)
                    c = new Color(c.getRed()/2,c.getGreen()/2,c.getBlue()/2);
                screen.get().setRGB(x, y, c.getRGB());
            }

            boolean t = false;
            if (t) { // disabled for now
                switch (map.get(mapX,mapY)) {
                case 1:
                    g.setColor(Color.BLUE);
                    break;
                case 2:
                    g.setColor(Color.RED);
                    break;
                }
                if (side == 1) {
                    Color c = g.getColor();
                    int r = c.getRed() / 2,
                        gr = c.getGreen() / 2,
                        b = c.getBlue() / 2; 
                    g.setColor(new Color(r, gr, b));
                }
                g.drawLine(x, drawStart, x, drawEnd);
            }
        }
    }
}
