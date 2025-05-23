package io.github.sajge.desktop;

import io.github.sajge.logger.Logger;

import javax.swing.*;

public class Main {
    private static final Logger log = Logger.get(Main.class);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame mf = null;
            try {
                mf = new MainFrame();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            mf.setVisible(true);
        });
    }

}
