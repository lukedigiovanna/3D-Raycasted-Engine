package jraycast.render;

import java.util.*;

import jraycast.utils.ImageTools;

import java.awt.image.*;
import java.awt.Color;

public class TexturePack {

    public static final int DEFAULT_TEX_WIDTH = 64, DEFAULT_TEX_HEIGHT = 64;

    private static Texture NULL_TEXTURE = new Texture(ImageTools.IMAGE_NOT_FOUND, DEFAULT_TEX_WIDTH, DEFAULT_TEX_HEIGHT);

    private List<Texture> textures;
    private Texture floorTexture, ceilingTexture;

    public TexturePack() {
        this.textures = new ArrayList<Texture>();
    }

    public void setFloorTex(Texture tex) {
        this.floorTexture = tex;
    }

    public void setFloorTex(Color color) {
        this.setFloorTex(new Texture(color, DEFAULT_TEX_WIDTH, DEFAULT_TEX_HEIGHT));
    }

    public void setFloorTex(String fp) {
        this.setFloorTex(new Texture(fp, DEFAULT_TEX_WIDTH, DEFAULT_TEX_HEIGHT));
    }

    public void setFloorTex(BufferedImage bi) {
        this.setFloorTex(new Texture(bi, DEFAULT_TEX_WIDTH, DEFAULT_TEX_HEIGHT));
    }

    public Texture getFloorTexture() {
        return (this.floorTexture == null) ? NULL_TEXTURE : this.floorTexture;
    }

    public void setCeilingTex(Texture tex) {
        this.ceilingTexture = tex;
    }

    public void setCeilingTex(Color color) {
        this.setCeilingTex(new Texture(color, DEFAULT_TEX_WIDTH, DEFAULT_TEX_HEIGHT));
    }

    public void setCeilingTex(String fp) {
        this.setCeilingTex(new Texture(fp, DEFAULT_TEX_WIDTH, DEFAULT_TEX_HEIGHT));
    }

    public void setCeilingTex(BufferedImage bi) {
        this.setCeilingTex(new Texture(bi, DEFAULT_TEX_WIDTH, DEFAULT_TEX_HEIGHT));
    }

    public Texture getCeilingTexture() {
        return (this.ceilingTexture == null) ? NULL_TEXTURE : this.ceilingTexture;
    }

    public void add(Texture texture) {
        this.textures.add(texture);
    }

    public void add(Color color) {
        this.add(new Texture(color, DEFAULT_TEX_WIDTH, DEFAULT_TEX_HEIGHT));
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
