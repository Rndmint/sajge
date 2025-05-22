package io.github.sajge.desktop.placeholders;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class PlaceholderPanel extends JPanel implements PlaceHolder {
    public PlaceholderPanel() {
        Random random = new Random();
        setBackground(
                new Color(
                        random.nextInt(256),
                        random.nextInt(256),
                        random.nextInt(256)
                )
        );
    }
}
