package io.github.sajge.engine.renderer.buffer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import io.github.sajge.logger.Logger;

public class FrameBuffer {
    private static final Logger log = Logger.get(FrameBuffer.class);

    private final BufferedImage image;

    public FrameBuffer(int width, int height) {
        log.info("Initializing FrameBuffer with width={} and height={}", width, height);
        this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }

    public void clear(Color c) {
        log.debug("Clearing FrameBuffer with color={} (ARGB={})", c, c.getRGB());
        Graphics2D g = image.createGraphics();
        try {
            g.setColor(c);
            g.fillRect(0, 0, image.getWidth(), image.getHeight());
            log.trace("Filled rectangle from (0,0) to ({},{})", image.getWidth(), image.getHeight());
        } finally {
            g.dispose();
            log.debug("Graphics2D disposed after clear");
        }
    }

    public void setPixel(int x, int y, Color c) {
        log.trace("Setting pixel at (x={}, y={}) with color={} (RGB={})", x, y, c, c.getRGB());
        image.setRGB(x, y, c.getRGB());
    }

    public BufferedImage getImage() {
        log.debug("Retrieving BufferedImage from FrameBuffer");
        return image;
    }
}
