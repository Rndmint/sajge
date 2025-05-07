package io.proto.spike.renderer.buffer;

import java.awt.*;
import java.awt.image.BufferedImage;

public class FrameBuffer {
    public BufferedImage image;

    public FrameBuffer(int w, int h) {
        image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
    }

    public void clear(Color c) {
        Graphics2D g = image.createGraphics();
        g.setColor(c);
        g.fillRect(0, 0, image.getWidth(), image.getHeight());
        g.dispose();
    }

    public void setPixel(int x, int y, Color c) {
        image.setRGB(x, y, c.getRGB());
    }
}
