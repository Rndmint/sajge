package io.github.sajge.desktop.testing;

import com.formdev.flatlaf.FlatLightLaf;
import io.github.sajge.logger.Logger;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SwingApp extends JFrame {
    private static final int COLLAPSED_WIDTH = 60;
    private static final int EXPANDED_WIDTH = 200;
    private static final int ICON_SIZE = 24;

    private static final Logger log = Logger.get(SwingApp.class);

    private final JPanel sidePanel;
    private final CardLayout mainCards = new CardLayout();
    private final JPanel mainPanel = new JPanel(mainCards);
    private final CardLayout rightCards = new CardLayout();
    private final JPanel rightPanel = new JPanel(rightCards);
    private final Map<String, JToggleButton> navButtons = new HashMap<>();
    private boolean expanded = true;

    public SwingApp() {
        super("Swing App");
        FlatLightLaf.setup();
        log.info("Starting SwingApp UI");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        sidePanel = new JPanel(new BorderLayout());
        sidePanel.setBackground(UIManager.getColor("Panel.background"));
        sidePanel.setPreferredSize(new Dimension(EXPANDED_WIDTH, getHeight()));

        JPanel navWrapper = new JPanel();
        navWrapper.setOpaque(false);
        navWrapper.setLayout(new BoxLayout(navWrapper, BoxLayout.Y_AXIS));
        String[] names = {"Profile", "Projects", "Renderer", "Settings"};
        ButtonGroup navGroup = new ButtonGroup();

        for (String name : names) {
            ImageIcon icon = loadIcon("icons/flat/" + name.toLowerCase() + ".png");
            JToggleButton btn = new JToggleButton(name, icon);
            btn.setAlignmentX(Component.LEFT_ALIGNMENT);
            btn.setHorizontalAlignment(SwingConstants.LEFT);
            btn.setIconTextGap(8);
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

        ImageIcon toggleIcon = loadIcon("icons/flat/menu.png");
        JButton toggle = new JButton(toggleIcon);
        toggle.setToolTipText("Toggle navigation");
        toggle.setBorderPainted(false);
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
        log.info("SwingApp UI visible");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(new FlatLightLaf());
                log.info("FlatLightLaf set as look and feel.");
            } catch (Exception ex) {
                log.error("Failed to set look and feel.", ex);
            }
            new SwingApp();
        });
    }

    private void toggleSidePanel(ActionEvent e) {
        expanded = !expanded;
        log.debug("Toggling side panel: expanded={}", expanded);
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
        log.debug("Switching view to '{}'.", name);
        mainCards.show(mainPanel, name);
        rightCards.show(rightPanel, name);
    }

    private ImageIcon loadIcon(String resourceName) {
        URL res = getClass().getClassLoader().getResource(resourceName);
        if (res == null) {
            log.warn("Icon resource not found: {}. Using default error icon.", resourceName);
            Icon err = UIManager.getIcon("OptionPane.errorIcon");
            Image img = toImage(err);
            Image scaled = img.getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        }
        ImageIcon raw = new ImageIcon(res);
        Image image = raw.getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH);
        return new ImageIcon(image);
    }

    private Image toImage(Icon icon) {
        if (icon instanceof ImageIcon) {
            return ((ImageIcon) icon).getImage();
        }
        BufferedImage buff = new BufferedImage(
                icon.getIconWidth(), icon.getIconHeight(),
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = buff.createGraphics();
        icon.paintIcon(null, g2, 0, 0);
        g2.dispose();
        return buff;
    }
}
