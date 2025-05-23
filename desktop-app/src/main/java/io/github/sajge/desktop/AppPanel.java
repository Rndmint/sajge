package io.github.sajge.desktop;

import io.github.sajge.client.ClientService;

import javax.swing.*;
import java.awt.*;

public class AppPanel extends JPanel {
    private final ProfileTab profileTab;
    private final ProjectsTab projectsTab;
    private final PendingInvitesTab pendingTab;
    private final SentInvitesTab sentTab;

    public AppPanel(MainFrame frame, ClientService service) {
        setLayout(new BorderLayout());
        JTabbedPane tabs = new JTabbedPane();

        profileTab = new ProfileTab(frame, service);
        projectsTab = new ProjectsTab(service);
        pendingTab = new PendingInvitesTab(service);
        sentTab = new SentInvitesTab(service);

        tabs.addTab("Profile", profileTab);
        tabs.addTab("Projects", projectsTab);
        tabs.addTab("Pending Invites", pendingTab);
        tabs.addTab("Sent Invites", sentTab);

        add(tabs, BorderLayout.CENTER);
    }

    public void resetAllTabs() {
        projectsTab.clear();
        pendingTab.clear();
        sentTab.clear();
    }

    public ProfileTab getProfileTab() {
        return profileTab;
    }
}
