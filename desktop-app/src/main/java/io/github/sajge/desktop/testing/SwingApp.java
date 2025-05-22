package io.github.sajge.desktop.testing;

import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

public class SwingApp extends JFrame {
    private static final int COLLAPSED_WIDTH = 60;
    private static final int EXPANDED_WIDTH = 200;

    private final JPanel sidePanel;
    private final CardLayout mainCards = new CardLayout();
    private final JPanel mainPanel = new JPanel(mainCards);
    private final CardLayout rightCards = new CardLayout();
    private final JPanel rightPanel = new JPanel(rightCards);
    private final Map<String, JToggleButton> navButtons = new HashMap<>();
    private boolean expanded = true;

    public SwingApp() {
        super("FlatLaf Swing App");
        FlatLightLaf.setup();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        sidePanel = new JPanel();
        sidePanel.setLayout(new BorderLayout());
        sidePanel.setBackground(UIManager.getColor("Panel.background"));
        sidePanel.setPreferredSize(new Dimension(EXPANDED_WIDTH, getHeight()));

        JPanel navWrapper = new JPanel();
        navWrapper.setOpaque(false);
        navWrapper.setLayout(new BoxLayout(navWrapper, BoxLayout.Y_AXIS));
        String[] names = {"Profile", "Projects", "Renderer", "Settings"};
        ButtonGroup navGroup = new ButtonGroup();

        for (String name : names) {
            FlatSVGIcon icon = loadIcon("/icons/flat/" + name.toLowerCase() + ".svg", 20);
            JToggleButton btn = new JToggleButton(name, icon);
            btn.setAlignmentX(Component.LEFT_ALIGNMENT);
            btn.setHorizontalAlignment(SwingConstants.LEFT);
            btn.setIconTextGap(10);
            btn.setBorder(new EmptyBorder(8, 12, 8, 12));
            btn.setFocusPainted(false);
            btn.setBackground(UIManager.getColor("Button.background"));
            btn.addActionListener(e -> switchView(name));
            navGroup.add(btn);
            navWrapper.add(btn);
            navWrapper.add(Box.createVerticalStrut(5));
            navButtons.put(name, btn);
        }
        navWrapper.add(Box.createVerticalGlue());
        sidePanel.add(navWrapper, BorderLayout.NORTH);

        FlatSVGIcon toggleIcon = loadIcon("/icons/flat/menu.svg", 16);
        JButton toggle = new JButton(toggleIcon);
        toggle.setToolTipText("Toggle navigation");
        toggle.setBorder(null);
        toggle.setFocusPainted(false);
        toggle.setContentAreaFilled(false);
        toggle.addActionListener(this::toggleSidePanel);
        JPanel bottomWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        bottomWrapper.setOpaque(false);
        bottomWrapper.add(toggle);
        sidePanel.add(bottomWrapper, BorderLayout.SOUTH);

        add(sidePanel, BorderLayout.WEST);

        for (String name : names) {
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBorder(new EmptyBorder(20, 20, 20, 20));
            JLabel label = new JLabel(name + " View");
            label.setFont(label.getFont().deriveFont(Font.BOLD, 18f));
            panel.add(label, BorderLayout.NORTH);
            mainPanel.add(panel, name);
        }
        add(mainPanel, BorderLayout.CENTER);

        rightPanel.setPreferredSize(new Dimension(180, getHeight()));
        rightPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        for (String name : names) {
            JPanel ctrl = new JPanel();
            ctrl.setLayout(new BoxLayout(ctrl, BoxLayout.Y_AXIS));
            ctrl.setBorder(new EmptyBorder(10, 10, 10, 10));
            ctrl.add(new JLabel(name + " Controls"));
            ctrl.add(Box.createVerticalGlue());
            rightPanel.add(ctrl, name);
        }
        add(rightPanel, BorderLayout.EAST);

        navButtons.get("Profile").setSelected(true);
        switchView("Profile");

        setSize(900, 650);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void toggleSidePanel(ActionEvent e) {
        expanded = !expanded;
        int width = expanded ? EXPANDED_WIDTH : COLLAPSED_WIDTH;
        sidePanel.setPreferredSize(new Dimension(width, getHeight()));

        for (Map.Entry<String, JToggleButton> entry : navButtons.entrySet()) {
            JToggleButton btn = entry.getValue();
            btn.setText(expanded ? entry.getKey() : "");
            btn.setHorizontalAlignment(expanded ? SwingConstants.LEFT : SwingConstants.CENTER);
        }

        revalidate();
        repaint();
    }

    private void switchView(String name) {
        mainCards.show(mainPanel, name);
        rightCards.show(rightPanel, name);
    }

    private FlatSVGIcon loadIcon(String path, int size) {
        FlatSVGIcon icon = new FlatSVGIcon(String.valueOf(getClass().getResource(path)), size, size);
        return icon;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(new FlatLightLaf());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            new SwingApp();
        });
    }
}
