package io.github.sajge.engine;

import io.github.sajge.engine.ui.AppFrame;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(AppFrame::init);
    }
}
