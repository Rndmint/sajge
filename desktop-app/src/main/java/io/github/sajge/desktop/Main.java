package io.github.sajge.desktop;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import io.github.sajge.logger.Logger;

import javax.swing.*;

public class Main {
    private static final Logger log = Logger.get(Main.class);

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatMacDarkLaf());
            UIManager.put( "Component.arc", 12 );
            UIManager.put( "Button.arc",    999 );
            UIManager.put( "TextComponent.arc", 8 );
        } catch( Exception ex ) {
            log.error( "Failed to initialize LaF", ex );
        }

        SwingUtilities.invokeLater(() -> {
            MainFrame mf;
            try {
                mf = new MainFrame();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            mf.setVisible(true);
        });
    }

}
