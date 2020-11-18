package render;

import java.awt.image.*;

import utils.ImageTools;

public class Texture {
    private BufferedImage image;

    /**
     * Generate a texture based on a file path
     * @param fp
     */
    public Texture(String fp) {
        this.image = ImageTools.getImage(fp);
    }

    /**
     * Generate a texture based on file size and inputted width/height
     * @param fp
     * @param width
     * @param height
     */
    public Texture(String fp, int width, int height) {
        this(fp);
        this.image = ImageTools.rescale(this.image, width, height);
    }

    public Texture(BufferedImage image) {
        this.image = image;
    }

    public Texture(BufferedImage image, int width, int height) {
        this.image = ImageTools.rescale(image, width, height);
    }

    public int get(int px, int py) {
        return image.getRGB(px, py);
    }

    public int width() {
        return image.getWidth();
    }

    public int height() {
        return image.getHeight();
    }
}
