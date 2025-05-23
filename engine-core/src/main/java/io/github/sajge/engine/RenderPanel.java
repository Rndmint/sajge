package io.github.sajge.engine;

import io.github.sajge.engine.renderer.Engine;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class RenderPanel extends JPanel {
    private final Engine engine;

    public RenderPanel(Engine engine) {
        this.engine = engine;
        setPreferredSize(new Dimension(engine.getWidth(), engine.getHeight()));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        BufferedImage img = engine.getFrame();
        if (img != null) {
            g.drawImage(img, 0, 0, null);
        }
    }
}
