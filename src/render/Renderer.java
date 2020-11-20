package render;

import java.awt.*;

import utils.Vector2;
import world.GameObject;
import world.Map;
import world.Sprite;
import world.SpriteList;

public class Renderer {
    private static final TexturePack NULL_PACK = new TexturePack();
    private static TexturePack texturePack = NULL_PACK;

    private static boolean showHUD = false;

    public static void setTexturePack(TexturePack aTexturePack) {
        if (aTexturePack != null)
            texturePack = aTexturePack;
    }

    /**
     * Renders a raycasted scene using the current texture pack.
     * Draws to the inputted screen and uses the map for wall positions/texture and camera for position and direction.
     * Does not draw sprites.
     * @param screen
     * @param map
     * @param camera
     */
    public static void render(Screen screen, Map map, GameObject camera) {
        render(screen, map, null, camera);
    }

    /**
     * Renders a raycasted scene using the current texture pack.
     * Draws to the inputted screen and uses the map for wall positions/texture and camera for position and direction.
     * Also draws sprites from the inputted sprite list.
     * @param screen
     * @param map
     * @param sprites
     * @param camera
     */
    public static void render(Screen screen, Map map, SpriteList sprites, GameObject camera) {
        Graphics2D g = screen.getGraphics();

        Texture floorTex = texturePack.getFloorTexture(),
                ceilingTex = texturePack.getCeilingTexture();

        boolean[][] buff = new boolean[screen.width()][screen.height()]; // true if the pixel has a wall drawn on it
        double[] zBuff = new double[screen.width()];
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
            zBuff[x] = perpWallDist;

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
                buff[x][y] = true;
            }
        }

        Vector2 dir0 = camera.direction.subtract(camera.plane); // left most ray
        Vector2 dir1 = camera.direction.add(camera.plane); // right most ray
        double posZ = 0.5 * screen.height(); // puts the horizon in the center of the screen
        for (int y = 0; y < screen.height()/2; y++) {
            int dy = - y + screen.height()/2;

            double rowDistance = posZ/dy;

            Vector2 floorStep = dir1.subtract(dir0).times(rowDistance).divide(screen.width());
            Vector2 floor = camera.position.add(dir0.times(rowDistance));

            for (int x = 0; x < screen.width(); x++) {
                if (buff[x][y]) { // drawing the ceiling/floor at a pos with no wall drawn there
                    floor.x += floorStep.x;
                    floor.y += floorStep.y;
                    continue;
                }
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
        
        // now draw sprites
        if (sprites != null) {
            double invDet = 1.0 / (camera.plane.x * camera.direction.y - camera.direction.x * camera.plane.y);
            Sprite[] ordered = sprites.getOrdered(camera);
            for (int i = 0; i < ordered.length; i++) {
                Sprite sprite = ordered[i];
                Texture tex = sprite.getTexture();
                
                double sx = sprite.position.x - camera.position.x,
                    sy = sprite.position.y - camera.position.y;
                
                double transformX = invDet * (camera.direction.y * sx - camera.direction.x * sy),
                    transformY = invDet * (-camera.plane.y * sx + camera.plane.x * sy);
                
                int spriteScreenX = (int)((screen.width()/2)*(1+transformX/transformY));
                
                double vMove = 0;
                if (sprite.renderPos == Sprite.FLOOR)
                    vMove = tex.height()*3;
                if (sprite.renderPos == Sprite.CEILING)
                    vMove = -tex.height()*3; 
                int vMoveScreen = (int)(vMove/transformY);

                int spriteHeight = Math.abs((int)(screen.height() / transformY * sprite.yScale));
                int drawStartY = -spriteHeight / 2 + screen.height()/2 + vMoveScreen;
                int drawEndY = drawStartY + spriteHeight;   
                if (drawStartY < 0) drawStartY = 0;     
                if (drawEndY > screen.height() - 1) drawEndY = screen.height() - 1;
                
                int spriteWidth = Math.abs((int)(screen.height() / transformY * sprite.xScale));
                int drawStartX = -spriteWidth / 2 + spriteScreenX;
                int drawEndX = drawStartX + spriteWidth;
                if (drawStartX < 0) drawStartX = 0;
                if (drawEndX > screen.width() - 1) drawEndX = screen.width() -  1;

                for (int x = drawStartX; x < drawEndX; x++) {
                    int texX = (int)((x - (-spriteWidth/2+spriteScreenX)) * tex.width()/spriteWidth);    
                    if (transformY > 0 && transformY < zBuff[x])
                    for (int y = drawStartY; y < drawEndY; y++) {
                        double d = y - vMoveScreen - screen.height() / 2.0 + spriteHeight / 2.0;
                        int texY = (int) ((d * tex.height()) / spriteHeight);
                        int rgb = tex.get(texX,texY);
                        if (rgb >>> 24 > 0)
                            screen.get().setRGB(x, y, rgb);
                    }
                }
            }
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
