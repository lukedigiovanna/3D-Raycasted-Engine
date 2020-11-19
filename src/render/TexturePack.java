package render;

import java.util.*;

import utils.ImageTools;

import java.awt.image.*;
import java.awt.Color;

public class TexturePack {

    public static final int DEFAULT_TEX_WIDTH = 64, DEFAULT_TEX_HEIGHT = 64;

    private static Texture NULL_TEXTURE = new Texture(ImageTools.IMAGE_NOT_FOUND, DEFAULT_TEX_WIDTH, DEFAULT_TEX_HEIGHT);

    private List<Texture> textures;
    public Texture floorTexture, ceilingTexture;

    public TexturePack() {
        this.textures = new ArrayList<Texture>();
    }

    public void add(Texture texture) {
        this.textures.add(texture);
    }

    public void add(Color color) {
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        img.setRGB(0,0,color.getRGB());
        this.add(new Texture(img, DEFAULT_TEX_WIDTH, DEFAULT_TEX_HEIGHT));
    }

    public void add(BufferedImage bi) {
        this.add(new Texture(bi, DEFAULT_TEX_WIDTH, DEFAULT_TEX_HEIGHT));
    }

    public void add(String fp) {
        this.add(new Texture(ImageTools.getImage(fp),DEFAULT_TEX_WIDTH, DEFAULT_TEX_HEIGHT));
    }

    public Texture get(int index) {
        if (index < 0 || index > textures.size() - 1) return NULL_TEXTURE;
        return textures.get(index);
    }
}
