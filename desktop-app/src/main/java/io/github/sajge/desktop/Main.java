package io.github.sajge.desktop;

import io.github.sajge.logger.Logger;

import javax.swing.*;

public class Main {
    private static final Logger log = Logger.get(Main.class);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame mf = new MainFrame();
            mf.setVisible(true);
        });
    }

}
