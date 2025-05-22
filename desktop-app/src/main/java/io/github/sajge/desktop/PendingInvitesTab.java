package io.github.sajge.desktop;

import io.github.sajge.client.ClientService;
import io.github.sajge.client.collaborations.ListPendingInvitesClient.PendingInviteDto;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class PendingInvitesTab extends JPanel {
    private final ClientService service;

    private final JButton refreshBtn = new JButton("Refresh");
    private final JTable  table;
    private final PendingTableModel tableModel;

    private final JButton acceptBtn = new JButton("Accept");
    private final JButton refuseBtn = new JButton("Refuse");

    public PendingInvitesTab(ClientService service) {
        this.service = service;
        tableModel  = new PendingTableModel();
        table       = new JTable(tableModel);
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(6,6));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(refreshBtn);
        add(top, BorderLayout.NORTH);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 4));
        bottom.add(acceptBtn);
        bottom.add(refuseBtn);
        add(bottom, BorderLayout.SOUTH);

        acceptBtn.setEnabled(false);
        refuseBtn.setEnabled(false);

        refreshBtn.addActionListener(e -> loadInvites());
        table.getSelectionModel().addListSelectionListener(e -> {
            boolean sel = table.getSelectedRow() >= 0;
            acceptBtn.setEnabled(sel);
            refuseBtn.setEnabled(sel);
        });
        acceptBtn.addActionListener(e -> handleAccept());
        refuseBtn.addActionListener(e -> handleRefuse());

    }

    private void loadInvites() {
        refreshBtn.setEnabled(false);
        new SwingWorker<List<PendingInviteDto>,Void>() {
            protected List<PendingInviteDto> doInBackground() throws Exception {
                return service.listPendingInvites();
            }
            protected void done() {
                refreshBtn.setEnabled(true);
                try {
                    tableModel.setInvites(get());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(PendingInvitesTab.this,
                            "Failed to load invites:\n" + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }

    private void handleAccept() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        PendingInviteDto dto = tableModel.getInviteAt(row);
        String key = dto.accessKey();

        acceptBtn .setEnabled(false);
        refuseBtn .setEnabled(false);

        new SwingWorker<Boolean,Void>() {
            protected Boolean doInBackground() throws Exception {
                return service.acceptInvite(key);
            }
            protected void done() {
                acceptBtn .setEnabled(true);
                refuseBtn .setEnabled(true);
                try {
                    if (get()) {
                        JOptionPane.showMessageDialog(PendingInvitesTab.this,
                                "Invite accepted");
                        loadInvites();
                    } else {
                        JOptionPane.showMessageDialog(PendingInvitesTab.this,
                                "Failed to accept invite", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(PendingInvitesTab.this,
                            "Error: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }

    private void handleRefuse() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        PendingInviteDto dto = tableModel.getInviteAt(row);
        String key = dto.accessKey();

        acceptBtn .setEnabled(false);
        refuseBtn .setEnabled(false);

        new SwingWorker<Boolean,Void>() {
            protected Boolean doInBackground() throws Exception {
                return service.refuseInvite(key);
            }
            protected void done() {
                acceptBtn .setEnabled(true);
                refuseBtn .setEnabled(true);
                try {
                    if (get()) {
                        JOptionPane.showMessageDialog(PendingInvitesTab.this,
                                "Invite refused");
                        loadInvites();
                    } else {
                        JOptionPane.showMessageDialog(PendingInvitesTab.this,
                                "Failed to refuse invite", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(PendingInvitesTab.this,
                            "Error: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }

    public void clear() {
        tableModel.setInvites(List.of());
        acceptBtn.setEnabled(false);
        refuseBtn.setEnabled(false);
    }

    private static class PendingTableModel extends AbstractTableModel {
        private final String[] cols = {
                "Inviter ID", "Inviter Username", "Project ID", "Access Key", "Created At"
        };
        private List<PendingInviteDto> data = new ArrayList<>();

        public void setInvites(List<PendingInviteDto> list) {
            this.data = list;
            fireTableDataChanged();
        }

        public PendingInviteDto getInviteAt(int row) {
            return data.get(row);
        }

        @Override public int getRowCount()    { return data.size(); }
        @Override public int getColumnCount() { return cols.length; }
        @Override public String getColumnName(int col) { return cols[col]; }

        @Override public Object getValueAt(int row, int col) {
            PendingInviteDto p = data.get(row);
            return switch(col) {
                case 0 -> p.inviterId();
                case 1 -> p.inviterUsername();
                case 2 -> p.projectId();
                case 3 -> p.accessKey();
                case 4 -> p.createdAt();
                default -> null;
            };
        }
    }
}
