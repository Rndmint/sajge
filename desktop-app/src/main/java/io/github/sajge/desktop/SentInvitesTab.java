package io.github.sajge.desktop;

import io.github.sajge.client.ClientService;
import io.github.sajge.client.collaborations.ListSentInvitesClient.SentInviteDto;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class SentInvitesTab extends JPanel {
    private final ClientService service;

    private final JButton refreshBtn = new JButton("Refresh");
    private final JTable  table;
    private final SentTableModel tableModel;

    private final JButton revokeBtn  = new JButton("Revoke");

    public SentInvitesTab(ClientService service) {
        this.service    = service;
        this.tableModel = new SentTableModel();
        this.table      = new JTable(tableModel);
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(6,6));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(refreshBtn);
        add(top, BorderLayout.NORTH);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bot = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 4));
        bot.add(revokeBtn);
        add(bot, BorderLayout.SOUTH);

        revokeBtn.setEnabled(false);

        refreshBtn.addActionListener(e -> loadSentInvites());
        table.getSelectionModel().addListSelectionListener(e -> {
            revokeBtn.setEnabled(table.getSelectedRow() >= 0);
        });
        revokeBtn.addActionListener(e -> doRevoke());

    }

    public void clear() {
        tableModel.setInvites(List.of());
        revokeBtn.setEnabled(false);
    }

    private void loadSentInvites() {
        refreshBtn.setEnabled(false);
        new SwingWorker<List<SentInviteDto>,Void>() {
            protected List<SentInviteDto> doInBackground() throws Exception {
                return service.listSentInvites();
            }
            protected void done() {
                refreshBtn.setEnabled(true);
                try {
                    tableModel.setInvites(get());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(SentInvitesTab.this,
                            "Failed to load sent invites:\n" + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }

    private void doRevoke() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        SentInviteDto dto = tableModel.getInviteAt(row);

        revokeBtn.setEnabled(false);
        new SwingWorker<Boolean,Void>() {
            protected Boolean doInBackground() throws Exception {
                return service.removeCollaborator(dto.projectId(), dto.invitedUserId());
            }
            protected void done() {
                revokeBtn.setEnabled(true);
                try {
                    if (get()) {
                        JOptionPane.showMessageDialog(SentInvitesTab.this,
                                "Invitation revoked");
                        loadSentInvites();
                    } else {
                        JOptionPane.showMessageDialog(SentInvitesTab.this,
                                "Revoke failed", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(SentInvitesTab.this,
                            "Error: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }

    private static class SentTableModel extends AbstractTableModel {
        private final String[] cols = {
                "Invited ID", "Invited Username", "Project ID",
                "Access Key", "Status", "Created At"
        };
        private List<SentInviteDto> data = new ArrayList<>();

        public void setInvites(List<SentInviteDto> list) {
            this.data = list;
            fireTableDataChanged();
        }

        public SentInviteDto getInviteAt(int row) {
            return data.get(row);
        }

        @Override public int getRowCount()    { return data.size(); }
        @Override public int getColumnCount() { return cols.length; }
        @Override public String getColumnName(int c) { return cols[c]; }

        @Override public Object getValueAt(int r, int c) {
            SentInviteDto s = data.get(r);
            return switch(c) {
                case 0 -> s.invitedUserId();
                case 1 -> s.invitedUsername();
                case 2 -> s.projectId();
                case 3 -> s.accessKey();
                case 4 -> s.status();
                case 5 -> s.createdAt();
                default -> null;
            };
        }
    }
}
