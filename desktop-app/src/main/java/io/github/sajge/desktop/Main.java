package io.github.sajge.desktop;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import io.github.sajge.logger.Logger;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;

public class Main {
    private JFrame frame;
    private JPanel colorPanel;
    private JPanel colorSelectors;
    private JSpinner colorCountSpinner;

    private JPanel tablePanel;
    private JTable table;
    private JSpinner rowSpinner;
    private JSpinner colSpinner;
    private JButton loadTableButton;

    private JPanel textPanel;
    private JTextArea textArea;

    private JTabbedPane imageTabs;
    private JButton addImageTabButton;

    public static void main(String[] args) {
        FlatLightLaf.setup();
        SwingUtilities.invokeLater(() -> {
            new Main().createAndShowGui();
        });
    }

    private void createAndShowGui() {
        frame = new JFrame("Swing FlatLaf Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);

        buildColorPanel();
        buildTablePanel();
        buildTextPanel();
        buildImagePanel();

        JSplitPane leftSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, colorPanel, tablePanel);
        leftSplit.setResizeWeight(0.5);

        JSplitPane rightSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, textPanel, createImageContainer());
        rightSplit.setResizeWeight(0.3);

        JSplitPane mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftSplit, rightSplit);
        mainSplit.setResizeWeight(0.4);

        frame.getContentPane().add(mainSplit);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void buildColorPanel() {
        colorPanel = new JPanel(new BorderLayout());
        colorPanel.setBorder(BorderFactory.createTitledBorder("Color Selector"));

        colorSelectors = new JPanel(new GridLayout(0, 1, 5, 5));
        JScrollPane scrollPane = new JScrollPane(colorSelectors);
        colorPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel control = new JPanel(new FlowLayout(FlowLayout.LEFT));
        control.add(new JLabel("Number of colors:"));
        colorCountSpinner = new JSpinner(new SpinnerNumberModel(3, 1, 20, 1));
        colorCountSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                updateColorSelectors((Integer) colorCountSpinner.getValue());
            }
        });
        control.add(colorCountSpinner);
        colorPanel.add(control, BorderLayout.NORTH);

        updateColorSelectors((Integer) colorCountSpinner.getValue());
    }

    private void updateColorSelectors(int count) {
        colorSelectors.removeAll();
        for (int i = 0; i < count; i++) {
            JButton btn = new JButton("Color " + (i + 1));
            btn.addActionListener(e -> {
                Color c = JColorChooser.showDialog(frame, "Choose Color", btn.getBackground());
                if (c != null) {
                    btn.setBackground(c);
                }
            });
            btn.setBackground(Color.WHITE);
            colorSelectors.add(btn);
        }
        colorSelectors.revalidate();
        colorSelectors.repaint();
    }

    private void buildTablePanel() {
        tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Dynamic Table"));

        table = new JTable();
        JScrollPane tableScroll = new JScrollPane(table);
        tablePanel.add(tableScroll, BorderLayout.CENTER);

        JPanel control = new JPanel(new FlowLayout(FlowLayout.LEFT));
        control.add(new JLabel("Rows:"));
        rowSpinner = new JSpinner(new SpinnerNumberModel(5, 1, 100, 1));
        control.add(rowSpinner);
        control.add(new JLabel("Cols:"));
        colSpinner = new JSpinner(new SpinnerNumberModel(5, 1, 50, 1));
        control.add(colSpinner);
        loadTableButton = new JButton("Load Table");
        loadTableButton.addActionListener(e -> loadTable());
        control.add(loadTableButton);
        tablePanel.add(control, BorderLayout.NORTH);
    }

    private void loadTable() {
        int rows = (Integer) rowSpinner.getValue();
        int cols = (Integer) colSpinner.getValue();
        DefaultTableModel model = new DefaultTableModel(rows, cols);
        table.setModel(model);
    }

    private void buildTextPanel() {
        textPanel = new JPanel(new BorderLayout());
        textPanel.setBorder(BorderFactory.createTitledBorder("Text Editor"));
        textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textPanel.add(new JScrollPane(textArea), BorderLayout.CENTER);
    }

    private void buildImagePanel() {
        imageTabs = new JTabbedPane();
    }

    private JPanel createImageContainer() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Image Viewer"));
        panel.add(new JScrollPane(imageTabs), BorderLayout.CENTER);
        addImageTabButton = new JButton("Add Image Tab");
        addImageTabButton.addActionListener(e -> addImageTab());
        panel.add(addImageTabButton, BorderLayout.NORTH);
        return panel;
    }

    private void addImageTab() {
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            try {
                BufferedImage img = ImageIO.read(file);
                JLabel lbl = new JLabel(new ImageIcon(img));
                JScrollPane scroll = new JScrollPane(lbl);
                imageTabs.addTab(file.getName(), scroll);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Failed to load image: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

