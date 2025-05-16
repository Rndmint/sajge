package io.github.sajge.desktop;

import com.formdev.flatlaf.FlatDarkLaf;
import io.github.sajge.logger.Logger;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;

public class DeskApp {
    private static final Logger log = Logger.get(DeskApp.class);

    public static void main(String[] args) {
        log.info("Starting DeskApp");
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
            log.debug("FlatDarkLaf set successfully");
        } catch (UnsupportedLookAndFeelException e) {
            log.error("Failed to set FlatDarkLaf", e);
        }
        SwingUtilities.invokeLater(UIBuilder::createAndShowGUI);
    }
}

class DarkPanel extends JPanel {
    private static final Logger log = Logger.get(DarkPanel.class);

    DarkPanel(Color backgroundColor) {
        log.debug("Creating DarkPanel with color {}", backgroundColor);
        setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1, true));
        setBackground(backgroundColor);
    }
}

class TablePanel extends JPanel {
    private static final Logger log = Logger.get(TablePanel.class);
    private DefaultTableModel model;
    private JTable table;
    private TableRowSorter<DefaultTableModel> sorter;
    private int columnCount;

    TablePanel(Color bg, Object[][] data, MouseAdapter popupListener) {
        setLayout(new BorderLayout(5,5));
        setBackground(bg);
        columnCount = data.length > 0 ? data[0].length : 1;
        String[] colNames = new String[columnCount];
        for (int i = 0; i < columnCount; i++) colNames[i] = "Column " + (i+1);
        model = new DefaultTableModel(colNames, 0);
        for (Object[] row : data) model.addRow(row);
        table = new JTable(model);
        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        JScrollPane scroll = new JScrollPane(table);
        scroll.addMouseListener(popupListener);
        table.addMouseListener(popupListener);

        JPanel top = new JPanel(new BorderLayout(5,5));
        top.setBackground(bg);
        top.addMouseListener(popupListener);
        JTextField searchField = new JTextField();
        searchField.addMouseListener(popupListener);
        JButton searchBtn = new JButton("Search");
        searchBtn.addMouseListener(popupListener);
        searchBtn.addActionListener(e -> {
            String text = searchField.getText();
            if (text.trim().isEmpty()) sorter.setRowFilter(null);
            else sorter.setRowFilter(RowFilter.regexFilter(text));
        });
        top.add(searchField, BorderLayout.CENTER);
        top.add(searchBtn, BorderLayout.EAST);

        JPanel crud = new JPanel(new FlowLayout(FlowLayout.LEFT,5,5));
        crud.setBackground(bg);
        crud.addMouseListener(popupListener);
        JButton add = new JButton("Add");
        JButton edit = new JButton("Edit");
        JButton delete = new JButton("Delete");
        add.addMouseListener(popupListener);
        edit.addMouseListener(popupListener);
        delete.addMouseListener(popupListener);
        add.addActionListener(this::addRow);
        edit.addActionListener(this::editRow);
        delete.addActionListener(this::deleteRow);
        crud.add(add); crud.add(edit); crud.add(delete);

        add(top, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(crud, BorderLayout.SOUTH);
    }

    TablePanel(Color bg, MouseAdapter popupListener) {
        this(bg, new Object[0][], popupListener);
    }

    private void addRow(ActionEvent e) {
        String input = JOptionPane.showInputDialog(this, "Enter row values separated by commas:");
        if (input != null) {
            String[] parts = input.split(",");
            Object[] row = new Object[columnCount];
            for (int i = 0; i < columnCount; i++) row[i] = i < parts.length ? parts[i].trim() : "";
            model.addRow(row);
        }
    }

    private void editRow(ActionEvent e) {
        int row = table.getSelectedRow();
        if (row >= 0) {
            int mrow = table.convertRowIndexToModel(row);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < columnCount; i++) {
                sb.append(model.getValueAt(mrow, i));
                if (i < columnCount - 1) sb.append(",");
            }
            String input = JOptionPane.showInputDialog(this, "Edit row values separated by commas:", sb.toString());
            if (input != null) {
                String[] parts = input.split(",");
                for (int i = 0; i < columnCount; i++) model.setValueAt(i < parts.length ? parts[i].trim() : "", mrow, i);
            }
        }
    }

    private void deleteRow(ActionEvent e) {
        int row = table.getSelectedRow();
        if (row >= 0) {
            int mrow = table.convertRowIndexToModel(row);
            model.removeRow(mrow);
        }
    }
}

class DynamicSplitPane extends JPanel {
    private static final Logger log = Logger.get(DynamicSplitPane.class);
    private JComponent content;
    private JComponent previousContent;
    private Color bg;
    private final MouseAdapter popupListener;

    DynamicSplitPane(Color bg) {
        log.debug("Initializing DynamicSplitPane");
        this.bg = bg;
        setLayout(new BorderLayout());
        popupListener = new MouseAdapter() {
            public void mousePressed(MouseEvent e) { maybeShowMenu(e); }
            public void mouseReleased(MouseEvent e) { maybeShowMenu(e); }
            private void maybeShowMenu(MouseEvent e) { if (e.isPopupTrigger()) showMenu(e.getX(), e.getY()); }
        };
        setContent(new DarkPanel(bg));
    }

    private void showMenu(int x, int y) {
        JPopupMenu menu = new JPopupMenu();
        menu.add(new JMenuItem("Split Horizontal")).addActionListener(a -> split(JSplitPane.HORIZONTAL_SPLIT));
        menu.add(new JMenuItem("Split Vertical")).addActionListener(a -> split(JSplitPane.VERTICAL_SPLIT));
        if (content instanceof JSplitPane) {
            JSplitPane sp = (JSplitPane) content;
            boolean hor = sp.getOrientation() == JSplitPane.HORIZONTAL_SPLIT;
            menu.addSeparator();
            menu.add(new JMenuItem(hor ? "Remove Left Panel" : "Remove Top Panel")).addActionListener(a -> removePanel(sp, true));
            menu.add(new JMenuItem(hor ? "Remove Right Panel" : "Remove Bottom Panel")).addActionListener(a -> removePanel(sp, false));
        } else if (content instanceof JScrollPane && ((JScrollPane) content).getViewport().getView() instanceof JTextArea) {
            menu.addSeparator();
            menu.add(new JMenuItem("Remove Text Area")).addActionListener(a -> revertContent());
        } else if (content instanceof TablePanel) {
            menu.addSeparator();
            menu.add(new JMenuItem("Remove Table")).addActionListener(a -> revertContent());
        } else {
            menu.addSeparator();
            menu.add(new JMenuItem("Convert to Text Area")).addActionListener(a -> convertToTextArea());
            menu.add(new JMenuItem("Open Table")).addActionListener(a -> convertToTable());
        }
        menu.show(this, x, y);
    }

    private void convertToTextArea() {
        log.info("Converting panel to text area");
        previousContent = content;
        JTextArea ta = new JTextArea();
        ta.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1, true));
        ta.setBackground(bg);
        JScrollPane sp = new JScrollPane(ta);
        sp.addMouseListener(popupListener);
        ta.addMouseListener(popupListener);
        setContent(sp);
    }

    private void convertToTable() {
        log.info("Converting panel to table");
        previousContent = content;
        TablePanel tp = new TablePanel(bg, popupListener);
        setContent(tp);
    }

    private void revertContent() {
        if (previousContent != null) {
            log.info("Reverting to previous content");
            setContent(previousContent);
            previousContent = null;
        }
    }

    private void removeTextArea() {
        revertContent();
    }

    private void removeTable() {
        revertContent();
    }

    private void removePanel(JSplitPane sp, boolean first) {
        log.info("Removing {} component", first ? "first" : "second");
        Component child = first ? sp.getLeftComponent() : sp.getRightComponent();
        if (child instanceof JComponent) setContent((JComponent) child);
    }

    private void setContent(JComponent comp) {
        log.debug("Setting content: {}", comp.getClass().getSimpleName());
        removeMouseListener(popupListener);
        if (content != null) {
            content.removeMouseListener(popupListener);
            if (content instanceof JSplitPane) {
                BasicSplitPaneUI ui = (BasicSplitPaneUI) ((JSplitPane) content).getUI();
                ui.getDivider().removeMouseListener(popupListener);
            }
        }
        content = comp;
        comp.addMouseListener(popupListener);
        if (comp instanceof JSplitPane) {
            BasicSplitPaneUI ui = (BasicSplitPaneUI) ((JSplitPane) comp).getUI();
            ui.getDivider().addMouseListener(popupListener);
        }
        removeAll();
        add(comp, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void split(int orientation) {
        log.info("Splitting pane orientation: {}", orientation == JSplitPane.HORIZONTAL_SPLIT ? "HORIZONTAL" : "VERTICAL");
        DynamicSplitPane first = new DynamicSplitPane(bg);
        DynamicSplitPane second = new DynamicSplitPane(bg);
        JSplitPane sp = new JSplitPane(orientation, first, second);
        sp.setResizeWeight(0.5);
        sp.setContinuousLayout(true);
        sp.setOneTouchExpandable(false);
        sp.setDividerSize(8);
        sp.setUI(new BasicSplitPaneUI() {
            @Override
            public BasicSplitPaneDivider createDefaultDivider() {
                BasicSplitPaneDivider d = super.createDefaultDivider();
                d.addMouseListener(popupListener);
                return d;
            }
        });
        previousContent = null;
        setContent(sp);
    }
}

class UIBuilder {
    private static final Logger log = Logger.get(UIBuilder.class);

    static void createAndShowGUI() {
        log.info("Creating main frame");
        JFrame frame = new JFrame("Desk App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 650);
        frame.setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBorder(new EmptyBorder(10, 10, 10, 10));
        frame.setContentPane(root);

        DynamicSplitPane dynamicRoot = new DynamicSplitPane(new Color(0x2E2E2E));
        root.add(dynamicRoot, BorderLayout.CENTER);

        frame.setVisible(true);
        log.info("DeskApp visible");
    }
}
