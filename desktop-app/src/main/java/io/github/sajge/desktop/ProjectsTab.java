package io.github.sajge.desktop;

import io.github.sajge.client.ClientService;
import io.github.sajge.client.accounts.ListUsersClient.UserDto;
import io.github.sajge.client.collaborations.ListProjectCollaboratorsClient.CollaboratorDto;
import io.github.sajge.client.projects.ListCollaboratedProjectsClient.CollaboratedProjectDto;
import io.github.sajge.client.projects.ListOwnedProjectsWithCollaboratorsClient.OwnedProjectDto;
import io.github.sajge.engine.SceneEditorPanel;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class ProjectsTab extends JPanel {
    private final ClientService service;
    private final JButton newBtn = new JButton("New Project");
    private final JButton refreshBtn = new JButton("Refresh");
    private final JComboBox<Mode> modeCombo = new JComboBox<>(Mode.values());
    private final JTable projectTable;
    private final OwnedTableModel ownedModel = new OwnedTableModel();
    private final CollabTableModel collabModel = new CollabTableModel();
    private final JLabel idLabel = new JLabel();
    private final JLabel ownerLabel = new JLabel();
    private final JTextField nameField = new JTextField(20);
    private final JTextArea descArea = new JTextArea(5, 20);
    private final JTextArea sceneArea = new JTextArea(5, 20);
    private final JLabel createdAtLabel = new JLabel();
    private final DefaultListModel<CollaboratorDto> collabListModel =
            new DefaultListModel<>();
    private final JList<CollaboratorDto> collaboratorList =
            new JList<>(collabListModel);
    private final JButton updateBtn = new JButton("Update");
    private final JButton deleteBtn = new JButton("Delete");
    private final JButton inviteBtn = new JButton("Invite…");


    private final JButton editSceneBtn = new JButton("Edit Scene");


    private Mode currentMode = Mode.OWNED;

    public ProjectsTab(ClientService service) {
        this.service = service;
        this.projectTable = new JTable(ownedModel);
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(6, 6));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        top.add(newBtn);
        top.add(refreshBtn);
        top.add(new JLabel("Show:"));
        top.add(modeCombo);
        add(top, BorderLayout.NORTH);

        projectTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane tableScroll = new JScrollPane(projectTable);

        sceneArea.setEditable(false);
        collaboratorList.setVisibleRowCount(4);

        JPanel detail = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;
        int y = 0;

        gbc.gridx = 0;
        gbc.gridy = y;
        detail.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1;
        detail.add(idLabel, gbc);
        y++;

        gbc.gridx = 0;
        gbc.gridy = y;
        detail.add(new JLabel("You own?"), gbc);
        gbc.gridx = 1;
        detail.add(ownerLabel, gbc);
        y++;

        gbc.gridx = 0;
        gbc.gridy = y;
        detail.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        detail.add(nameField, gbc);
        y++;

        gbc.gridx = 0;
        gbc.gridy = y;
        detail.add(new JLabel("Desc:"), gbc);
        gbc.gridx = 1;
        detail.add(new JScrollPane(descArea), gbc);
        y++;

        gbc.gridx = 0;
        gbc.gridy = y;
        detail.add(new JLabel("Scene JSON:"), gbc);
        gbc.gridx = 1;
        detail.add(new JScrollPane(sceneArea), gbc);
        y++;

        gbc.gridx = 0;
        gbc.gridy = y;
        detail.add(new JLabel("Created At:"), gbc);
        gbc.gridx = 1;
        detail.add(createdAtLabel, gbc);
        y++;

        gbc.gridx = 0;
        gbc.gridy = y;
        detail.add(new JLabel("Collaborators:"), gbc);
        gbc.gridx = 1;
        detail.add(new JScrollPane(collaboratorList), gbc);
        y++;

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        btns.add(updateBtn);
        btns.add(deleteBtn);
        btns.add(inviteBtn);

        btns.add(editSceneBtn);
        editSceneBtn.setEnabled(false);
        editSceneBtn.addActionListener(e -> openSceneEditor());

        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.gridwidth = 2;
        detail.add(btns, gbc);

        JSplitPane split = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                tableScroll, detail
        );
        split.setDividerLocation(300);
        add(split, BorderLayout.CENTER);

        refreshBtn.addActionListener(e -> loadProjects());
        newBtn.addActionListener(e -> doCreate());
        modeCombo.addActionListener(e -> {
            currentMode = (Mode) modeCombo.getSelectedItem();
            loadProjects();
        });
        projectTable.getSelectionModel().addListSelectionListener(
                (ListSelectionListener) e -> {
                    if (!e.getValueIsAdjusting()) showSelected();
                }
        );
        updateBtn.addActionListener(e -> doUpdate());
        deleteBtn.addActionListener(e -> doDelete());
        inviteBtn.addActionListener(e -> doInvite());

        clearDetail();
    }

    public void clear() {
        ownedModel.setProjects(List.of());
        collabModel.setProjects(List.of());
        clearDetail();
    }

    private void loadProjects() {
        refreshBtn.setEnabled(false);
        new SwingWorker<List<?>, Void>() {
            protected List<?> doInBackground() {
                try {
                    if (currentMode == Mode.OWNED) {
                        return service.listOwnedProjectsWithCollaborators();
                    } else {
                        return service.listCollaboratedProjects();
                    }
                } catch (Exception ex) {
                    SwingUtilities.invokeLater(() ->
                            JOptionPane.showMessageDialog(
                                    ProjectsTab.this,
                                    "Load failed:\n" + ex.getMessage(),
                                    "Error", JOptionPane.ERROR_MESSAGE
                            )
                    );
                    return List.of();
                }
            }

            @SuppressWarnings("unchecked")
            protected void done() {
                refreshBtn.setEnabled(true);
                clearDetail();
                try {
                    if (currentMode == Mode.OWNED) {
                        ownedModel.setProjects((List<OwnedProjectDto>) get());
                        projectTable.setModel(ownedModel);
                    } else {
                        collabModel.setProjects((List<CollaboratedProjectDto>) get());
                        projectTable.setModel(collabModel);
                    }
                } catch (Exception ignored) {
                }
            }
        }.execute();
    }

    private void showSelected() {
        clearDetail();
        int row = projectTable.getSelectedRow();
        if (row < 0) return;

        boolean isOwner = currentMode == Mode.OWNED;
        long pid;
        String name, desc, scene, created;

        if (isOwner) {
            var p = ownedModel.getProjectAt(row);
            pid = p.id();
            name = p.name();
            desc = p.description();
            scene = p.sceneJson();
            created = p.createdAt();
            updateBtn.setEnabled(true);
            deleteBtn.setEnabled(true);
            inviteBtn.setEnabled(true);
        } else {
            var p = collabModel.getProjectAt(row);
            pid = p.id();
            name = p.name();
            desc = p.description();
            scene = p.sceneJson();
            created = p.createdAt();
            updateBtn.setEnabled(true);
            deleteBtn.setEnabled(false);
            inviteBtn.setEnabled(false);
        }

        editSceneBtn.setEnabled(true);

        ownerLabel.setText(isOwner ? "Yes" : "No");
        idLabel.setText(String.valueOf(pid));
        nameField.setText(name);
        descArea.setText(desc);
        sceneArea.setText(scene);
        createdAtLabel.setText(created);

        collabListModel.clear();
        new SwingWorker<List<CollaboratorDto>, Void>() {
            protected List<CollaboratorDto> doInBackground() {
                try {
                    return service.listProjectCollaborators();
                } catch (Exception ex) {
                    return List.of();
                }
            }

            protected void done() {
                try {
                    for (var c : get()) collabListModel.addElement(c);
                } catch (Exception ignored) {
                }
            }
        }.execute();
    }

    private void clearDetail() {
        editSceneBtn.setEnabled(false);
        idLabel.setText("");
        ownerLabel.setText("");
        nameField.setText("");
        descArea.setText("");
        sceneArea.setText("");
        createdAtLabel.setText("");
        collabListModel.clear();
        updateBtn.setEnabled(false);
        deleteBtn.setEnabled(false);
        inviteBtn.setEnabled(false);
    }

    private void doCreate() {
        JTextField nm = new JTextField(20);
        JTextArea da = new JTextArea(4, 20);
        JPanel pnl = new JPanel(new GridBagLayout());
        var gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;
        gbc.gridy = 0;
        pnl.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        pnl.add(nm, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        pnl.add(new JLabel("Desc:"), gbc);
        gbc.gridx = 1;
        pnl.add(new JScrollPane(da), gbc);

        if (JOptionPane.showConfirmDialog(
                this, pnl, "New Project",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        ) != JOptionPane.OK_OPTION) return;

        new SwingWorker<Long, Void>() {
            protected Long doInBackground() {
                try {
                    return service.createProject(
                            nm.getText().trim(),
                            da.getText(),
                            "{}"
                    );
                } catch (Exception ex) {
                    SwingUtilities.invokeLater(() ->
                            JOptionPane.showMessageDialog(
                                    ProjectsTab.this,
                                    "Create failed:\n" + ex.getMessage(),
                                    "Error", JOptionPane.ERROR_MESSAGE
                            )
                    );
                    return -1L;
                }
            }

            protected void done() {
                try {
                    long id = get();
                    if (id >= 0) {
                        JOptionPane.showMessageDialog(
                                ProjectsTab.this, "Created ID " + id
                        );
                        loadProjects();
                    }
                } catch (Exception ignored) {
                }
            }
        }.execute();
    }

    private void doUpdate() {
        long pid = Long.parseLong(idLabel.getText());
        String nm = nameField.getText().trim();
        String ds = descArea.getText();
        String sc = sceneArea.getText();

        new SwingWorker<Boolean, Void>() {
            protected Boolean doInBackground() {
                try {
                    return service.updateProject(pid, nm, ds, sc);
                } catch (Exception ex) {
                    return false;
                }
            }

            protected void done() {
                try {
                    if (get()) {
                        JOptionPane.showMessageDialog(
                                ProjectsTab.this, "Updated"
                        );
                        loadProjects();
                    } else {
                        JOptionPane.showMessageDialog(
                                ProjectsTab.this,
                                "Update failed", "Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                } catch (Exception ignored) {
                }
            }
        }.execute();
    }

    private void doDelete() {
        long pid = Long.parseLong(idLabel.getText());
        if (JOptionPane.showConfirmDialog(
                this, "Delete project " + pid + "?", "Confirm",
                JOptionPane.YES_NO_OPTION
        ) != JOptionPane.YES_OPTION) return;

        new SwingWorker<Boolean, Void>() {
            protected Boolean doInBackground() {
                try {
                    return service.deleteProject(pid);
                } catch (Exception ex) {
                    return false;
                }
            }

            protected void done() {
                try {
                    if (get()) {
                        JOptionPane.showMessageDialog(
                                ProjectsTab.this, "Deleted"
                        );
                        loadProjects();
                    } else {
                        JOptionPane.showMessageDialog(
                                ProjectsTab.this,
                                "Delete failed", "Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                } catch (Exception ignored) {
                }
            }
        }.execute();
    }

    private void doInvite() {
        List<UserDto> users;
        try {
            users = service.listUsers();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this, "Failed loading users:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        DefaultListModel<UserDto> mdl = new DefaultListModel<>();
        users.forEach(mdl::addElement);
        JList<UserDto> ul = new JList<>(mdl);
        ul.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JCheckBox ce = new JCheckBox("Can Edit", true);

        JPanel pnl = new JPanel(new BorderLayout(4, 4));
        pnl.add(new JScrollPane(ul), BorderLayout.CENTER);
        pnl.add(ce, BorderLayout.SOUTH);

        if (JOptionPane.showConfirmDialog(
                this, pnl, "Invite Collaborator",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
        ) != JOptionPane.OK_OPTION) return;

        var sel = ul.getSelectedValue();
        if (sel == null) return;
        long pid = Long.parseLong(idLabel.getText());

        new SwingWorker<Boolean, Void>() {
            protected Boolean doInBackground() {
                try {
                    service.inviteCollaborator(pid, sel.id(), ce.isSelected());
                    return true;
                } catch (Exception ex) {
                    return false;
                }
            }

            protected void done() {
                try {
                    if (get()) {
                        JOptionPane.showMessageDialog(
                                ProjectsTab.this, "Invited"
                        );
                        loadProjects();
                    } else {
                        JOptionPane.showMessageDialog(
                                ProjectsTab.this,
                                "Invite failed", "Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                } catch (Exception ignored) {
                }
            }
        }.execute();
    }

    private void openSceneEditor() {
        int row = projectTable.getSelectedRow();
        if (row < 0) return;

        final long projectId;
        final String projectName;
        final String projectDesc;
        final String initialSceneJson;
        if (currentMode == Mode.OWNED) {
            var p = ownedModel.getProjectAt(row);
            projectId       = p.id();
            projectName     = p.name();
            projectDesc     = p.description();
            initialSceneJson= p.sceneJson();
        } else {
            var p = collabModel.getProjectAt(row);
            projectId       = p.id();
            projectName     = p.name();
            projectDesc     = p.description();
            initialSceneJson= p.sceneJson();
        }

        try {
            SceneEditorPanel editorPanel = new SceneEditorPanel(
                    initialSceneJson,
                    newJson -> {
                        SwingWorker<Void,Void> saveWorker = new SwingWorker<>() {
                            @Override protected Void doInBackground() throws Exception {
                                service.updateProject(
                                        projectId,
                                        projectName,
                                        projectDesc,
                                        newJson
                                );
                                return null;
                            }
                            @Override protected void done() {
                                try {
                                    get();
                                    loadProjects();
                                    JOptionPane.showMessageDialog(
                                            ProjectsTab.this,
                                            "Scene saved!",
                                            "Success",
                                            JOptionPane.INFORMATION_MESSAGE
                                    );
                                } catch (Exception ex) {
                                    JOptionPane.showMessageDialog(
                                            ProjectsTab.this,
                                            "Save failed:\n" + ex.getMessage(),
                                            "Error",
                                            JOptionPane.ERROR_MESSAGE
                                    );
                                }
                            }
                        };
                        saveWorker.execute();
                    }
            );

            JDialog dlg = new JDialog(
                    SwingUtilities.getWindowAncestor(this),
                    "Edit Scene — " + projectName,
                    Dialog.ModalityType.APPLICATION_MODAL
            );
            dlg.getContentPane().add(editorPanel);
            dlg.pack();
            dlg.setLocationRelativeTo(this);
            dlg.setVisible(true);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Could not open scene editor:\n" + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }


    private enum Mode {
        OWNED("My Projects"), COLLAB("Collaborations");
        private final String label;

        Mode(String l) {
            label = l;
        }

        @Override
        public String toString() {
            return label;
        }
    }

    private static class OwnedTableModel extends AbstractTableModel {
        private final String[] cols = {"ID", "Name", "Description", "Created"};
        private List<OwnedProjectDto> data = new ArrayList<>();

        public void setProjects(List<OwnedProjectDto> list) {
            data = new ArrayList<>(list);
            fireTableDataChanged();
        }

        public OwnedProjectDto getProjectAt(int r) {
            return data.get(r);
        }

        @Override
        public int getRowCount() {
            return data.size();
        }

        @Override
        public int getColumnCount() {
            return cols.length;
        }

        @Override
        public String getColumnName(int c) {
            return cols[c];
        }

        @Override
        public Object getValueAt(int r, int c) {
            var p = data.get(r);
            return switch (c) {
                case 0 -> p.id();
                case 1 -> p.name();
                case 2 -> p.description();
                case 3 -> p.createdAt();
                default -> null;
            };
        }
    }

    private static class CollabTableModel extends AbstractTableModel {
        private final String[] cols = {"ID", "Name", "Desc", "Owner", "Created"};
        private List<CollaboratedProjectDto> data = new ArrayList<>();

        public void setProjects(List<CollaboratedProjectDto> list) {
            data = new ArrayList<>(list);
            fireTableDataChanged();
        }

        public CollaboratedProjectDto getProjectAt(int r) {
            return data.get(r);
        }

        @Override
        public int getRowCount() {
            return data.size();
        }

        @Override
        public int getColumnCount() {
            return cols.length;
        }

        @Override
        public String getColumnName(int c) {
            return cols[c];
        }

        @Override
        public Object getValueAt(int r, int c) {
            var p = data.get(r);
            return switch (c) {
                case 0 -> p.id();
                case 1 -> p.name();
                case 2 -> p.description();
                case 3 -> p.ownerId();
                case 4 -> p.createdAt();
                default -> null;
            };
        }
    }
}
