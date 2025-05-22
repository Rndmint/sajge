package io.github.sajge.desktop.testing;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

public class LearningApp extends JFrame {
    private static final int COLLAPSED_WIDTH = 50;
    private static final int EXPANDED_WIDTH = 180;

    private final JPanel sidePanel;
    private final CardLayout mainCards = new CardLayout();
    private final JPanel mainPanel = new JPanel(mainCards);
    private final CardLayout rightCards = new CardLayout();
    private final JPanel rightPanel = new JPanel(rightCards);
    private final Map<String, JButton> navButtons = new HashMap<>();
    private boolean expanded = true;

    public LearningApp() {
        super("FlatLaf Swing Test App");
        FlatLightLaf.setup();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setBackground(new Color(0xD3CCE3));

        sidePanel = new JPanel();
        sidePanel.setLayout(new BorderLayout());
        sidePanel.setBackground(new Color(0xA3D5D3));
        sidePanel.setPreferredSize(new Dimension(EXPANDED_WIDTH, getHeight()));

        JButton profileButton = createOvalButton(new Color(0x800000), 40);
        profileButton.addActionListener(e -> switchView("Profile"));
        navButtons.put("Profile", profileButton);
        JPanel topWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        topWrapper.setOpaque(false);
        topWrapper.add(profileButton);
        sidePanel.add(topWrapper, BorderLayout.NORTH);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setOpaque(false);
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        String[] names = {"Projects", "Renderer", "Settings"};
        Icon icon = loadIcon("/icons/placeholder.png");

        for (String name : names) {
            JButton btn = new JButton(name, icon);
            btn.setAlignmentX(Component.LEFT_ALIGNMENT);
            btn.setBackground(new Color(0x00BFFF));
            btn.setFocusPainted(false);
            btn.setBorder(new EmptyBorder(5, 5, 5, 5));
            btn.setHorizontalAlignment(SwingConstants.LEFT);
            btn.addActionListener(e -> switchView(name));
            navButtons.put(name, btn);
            buttonsPanel.add(Box.createVerticalStrut(10));
            buttonsPanel.add(btn);
        }
        buttonsPanel.add(Box.createVerticalGlue());
        sidePanel.add(buttonsPanel, BorderLayout.CENTER);

        JButton toggle = createOvalButton(new Color(0x9932CC), 20);
        toggle.addActionListener(this::toggleSidePanel);
        JPanel bottomWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        bottomWrapper.setOpaque(false);
        bottomWrapper.add(toggle);
        sidePanel.add(bottomWrapper, BorderLayout.SOUTH);

        add(sidePanel, BorderLayout.WEST);

        for (String name : new String[]{"Profile", "Projects", "Renderer", "Settings"}) {
            JPanel p = new JPanel();
            p.setBackground(new Color(0xF0AD4E));
            p.add(new JLabel(name + " View"));
            mainPanel.add(p, name);
        }
        add(mainPanel, BorderLayout.CENTER);

        rightPanel.setPreferredSize(new Dimension(150, getHeight()));
        for (String name : new String[]{"Profile", "Projects", "Renderer", "Settings"}) {
            JPanel p = new JPanel();
            p.setBackground(new Color(0xFFFF00));
            p.add(new JLabel(name + " Controls"));
            rightPanel.add(p, name);
        }
        add(rightPanel, BorderLayout.EAST);

        switchView("Profile");

        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LearningApp::new);
    }

    private void toggleSidePanel(ActionEvent e) {
        expanded = !expanded;
        int width = expanded ? EXPANDED_WIDTH : COLLAPSED_WIDTH;
        sidePanel.setPreferredSize(new Dimension(width, getHeight()));

        for (Map.Entry<String, JButton> entry : navButtons.entrySet()) {
            String name = entry.getKey();
            JButton btn = entry.getValue();
            if ("Profile".equals(name)) {
                continue;
            }
            btn.setText(expanded ? name : "");
        }

        revalidate();
        repaint();
    }

    private void switchView(String name) {
        mainCards.show(mainPanel, name);
        rightCards.show(rightPanel, name);
    }

    private Icon loadIcon(String path) {
        java.net.URL imgUrl = getClass().getResource(path);
        if (imgUrl != null) {
            return new ImageIcon(imgUrl);
        } else {
            return new EmptyIcon(16, 16);
        }
    }

    private JButton createOvalButton(Color bg, int diameter) {
        JButton btn = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(diameter, diameter);
            }
        };
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setBackground(bg);
        return btn;
    }

    private static class EmptyIcon implements Icon {
        private final int w, h;

        public EmptyIcon(int w, int h) {
            this.w = w;
            this.h = h;
        }

        public void paintIcon(Component c, Graphics g, int x, int y) {
        }

        public int getIconWidth() {
            return w;
        }

        public int getIconHeight() {
            return h;
        }
    }
}



