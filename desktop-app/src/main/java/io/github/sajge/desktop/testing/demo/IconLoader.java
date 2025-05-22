package io.github.sajge.desktop.testing.demo;

import io.github.sajge.logger.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;

public class IconLoader {
    private static final Logger log = Logger.get(IconLoader.class);

    public static ImageIcon load(String resourcePath) {
        URL res = IconLoader.class
                .getClassLoader()
                .getResource(resourcePath);
        if (res == null) {
            log.warn("Icon resource not found: {}. Using default error icon.",
                    resourcePath);
            Icon err = UIManager.getIcon("OptionPane.errorIcon");
            return new ImageIcon(
                    toImage(err)
                            .getScaledInstance(Constants.ICON_SIZE,
                                    Constants.ICON_SIZE,
                                    Image.SCALE_SMOOTH)
            );
        }
        ImageIcon raw = new ImageIcon(res);
        return new ImageIcon(
                raw.getImage()
                        .getScaledInstance(Constants.ICON_SIZE,
                                Constants.ICON_SIZE,
                                Image.SCALE_SMOOTH)
        );
    }

    private static Image toImage(Icon icon) {
        if (icon instanceof ImageIcon) {
            return ((ImageIcon) icon).getImage();
        }
        BufferedImage buf = new BufferedImage(
                icon.getIconWidth(),
                icon.getIconHeight(),
                BufferedImage.TYPE_INT_ARGB
        );
        Graphics2D g2 = buf.createGraphics();
        icon.paintIcon(null, g2, 0, 0);
        g2.dispose();
        return buf;
    }
}
