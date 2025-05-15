package io.github.sajge.desktop;

import com.formdev.flatlaf.FlatDarkLaf;
import io.github.sajge.logger.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
    private static final Logger log = Logger.get(Main.class);
    public static void main(String[] args) throws UnsupportedLookAndFeelException {

        UIManager.setLookAndFeel(new FlatDarkLaf());

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("hi");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 400);
            frame.setLayout(new BorderLayout());

            JPanel leftPanel = new JPanel();
            leftPanel.add(new JLabel("Left Panel"));

            JPanel rightPanel = new JPanel();
            rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

            JButton toggleButton = new JButton("Toggle Left Panel");
            toggleButton.addActionListener(new ActionListener() {
                boolean isLeftPanelVisible = true;

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (isLeftPanelVisible) {
                        leftPanel.setVisible(false);
                    } else {
                        leftPanel.setVisible(true);
                    }
                    isLeftPanelVisible = !isLeftPanelVisible;
                    frame.revalidate();
                    frame.repaint();
                }
            });

            rightPanel.add(toggleButton);

            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
            splitPane.setDividerLocation(200);

            frame.add(splitPane, BorderLayout.CENTER);
            frame.setVisible(true);
        });
    }
}
