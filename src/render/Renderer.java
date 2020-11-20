package render;

import java.awt.*;

import utils.Vector2;
import world.GameObject;
import world.Map;

public class Renderer {
    private static final TexturePack NULL_PACK = new TexturePack();
    private static TexturePack texturePack = NULL_PACK;

    private static boolean showHUD = false;

    public static void setTexturePack(TexturePack aTexturePack) {
        if (aTexturePack != null)
            texturePack = aTexturePack;
    }

    public static void render(Screen screen, Map map, GameObject camera) {
        Graphics2D g = screen.getGraphics();

        Texture floorTex = texturePack.getFloorTexture(),
                ceilingTex = texturePack.getCeilingTexture();

        Vector2 dir0 = camera.direction.subtract(camera.plane); // left most ray
        Vector2 dir1 = camera.direction.add(camera.plane); // right most ray
        double posZ = 0.5 * screen.height(); // puts the horizon in the center of the screen
        for (int y = 0; y < screen.height()/2; y++) {
            int dy = - y + screen.height()/2;

            double rowDistance = posZ/dy;

            Vector2 floorStep = dir1.subtract(dir0).times(rowDistance).divide(screen.width());
            Vector2 floor = camera.position.add(dir0.times(rowDistance));

            for (int x = 0; x < screen.width(); x++) {
                int floorTX = (int)(floorTex.width() * (floor.x - (int)floor.x)) & (floorTex.width() - 1),
                    floorTY = (int)(floorTex.height() * (floor.y - (int)floor.y)) & (floorTex.height() - 1);
                screen.get().setRGB(x, screen.height() - y - 1, floorTex.get(floorTX, floorTY));
                int ceilTX = (int)(ceilingTex.width() * (floor.x - (int)floor.x)) & (ceilingTex.width() - 1),
                    ceilTY = (int)(ceilingTex.height() * (floor.y - (int)floor.y)) & (ceilingTex.height() - 1);
                screen.get().setRGB(x, y, ceilingTex.get(ceilTX, ceilTY));
                
                floor.x += floorStep.x;
                floor.y += floorStep.y;
            }
        }
        
        for (int x = 0; x < screen.width(); x+=1) {
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
            wallX -= Math.floor(wallX); // get just the decimal component
            int texX = (int)(wallX * tex.width());
            // if (side == 0 && rayDir.x < 0) texX = tex.width() - texX - 1;
            // if (side == 1 && rayDir.y < 0) texX = tex.width() - texX - 1;

            double step = (double)tex.height()/lineHeight;
            double texPos = (drawStart - screen.height()/2 + lineHeight/2) * step;
            for (int y = drawStart; y < drawEnd; y++) {
                int texY = (int)texPos & (tex.height() - 1);
                texPos += step;
                int colVal = tex.get(texX,texY);
                Color c = new Color(colVal);
                if (side == 1)
                    c = new Color(c.getRed()/2,c.getGreen()/2,c.getBlue()/2);
                screen.get().setRGB(x, y, c.getRGB());
            }

            // g.setColor(Color.BLACK);
            // g.drawLine(x, 0, x, drawStart);
            // g.setColor(Color.GRAY);
            // g.drawLine(x, drawEnd, x, screen.height()-1);
        }

        if (showHUD) {
            int size = 20; 
            int width = size * map.width();
            int height = size * map.height();
            g.fillRect(0,0,width,height);
            for (int x = 0; x < map.width(); x++) {
                for (int y = 0; y < map.height(); y++) {
                    if (map.get(x,y) > 0)
                        g.setColor(Color.BLACK);
                    else
                        g.setColor(Color.WHITE);
                    g.fillRect(x * size + 1, y * size + 1, size - 2, size - 2);
                }
            }
            g.setColor(Color.GREEN);
            int cx = (int)(camera.position.x * size), cy = (int)(camera.position.y * size);
            g.fillOval(cx - size/4, cy - size/4, size/2, size/2);
        }
    }
}
