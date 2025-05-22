package io.github.sajge.client;

//██████  ███████ ██   ██  ██████  ██      ██████  ██
//██   ██ ██      ██   ██ ██    ██ ██      ██   ██ ██
//██████  █████   ███████ ██    ██ ██      ██   ██ ██
//██   ██ ██      ██   ██ ██    ██ ██      ██   ██
//██████  ███████ ██   ██  ██████  ███████ ██████  ██

import io.github.sajge.client.accounts.*;
import io.github.sajge.client.check.CheckSessionClient;
import io.github.sajge.client.collaborations.*;
import io.github.sajge.client.projects.*;

import java.util.List;
import java.util.stream.Collectors;

public class ClientService {

    private String token = "";

    public void setToken(String token) {
        this.token = token;
    }

    private final SignupClient
            signupClient =
            new SignupClient();

    private final LoginClient
            loginClient =
            new LoginClient();

    private final LogoutClient
            logoutClient =
            new LogoutClient();

    private final CheckSessionClient
            checkSessionClient =
            new CheckSessionClient();

    private final DeleteAccountClient
            deleteAccountClient =
            new DeleteAccountClient();

    private final CreateProjectClient
            createProjectClient =
            new CreateProjectClient();

    private final UpdateProjectClient
            updateProjectClient =
            new UpdateProjectClient();

    private final DeleteProjectClient
            deleteProjectClient =
            new DeleteProjectClient();

    private final InviteCollaboratorClient
            inviteCollaboratorClient =
            new InviteCollaboratorClient();

    private final RemoveCollaboratorClient
            removeCollaboratorClient =
            new RemoveCollaboratorClient();

    private final AcceptInviteClient
            acceptInviteClient =
            new AcceptInviteClient();

    private final RefuseInviteClient
            refuseInviteClient =
            new RefuseInviteClient();

    private final ListOwnedProjectsClient
            listOwnedProjectsClient =
            new ListOwnedProjectsClient();

    private final ListPendingInvitesClient
            listPendingInvitesClient =
            new ListPendingInvitesClient();

    private final ListSentInvitesClient
            listSentInvitesClient =
            new ListSentInvitesClient();

    private final ListUsersClient
            listUsersClient =
            new ListUsersClient();

    private final ListCollaboratedProjectsClient
            listCollaboratedProjectsClient =
            new ListCollaboratedProjectsClient();

    private final ListProjectCollaboratorsClient
            listProjectCollaboratorsClient =
            new ListProjectCollaboratorsClient();

    private final ListOwnedProjectsWithCollaboratorsClient
            listOwnedProjectsWithCollaboratorsClient =
            new ListOwnedProjectsWithCollaboratorsClient();

    public String getToken() {
        return token;
    }

    public boolean signup(String username, String password) throws Exception {
        signupClient.send(username, password);
        return signupClient.isSuccess();
    }

    public boolean login(String username, String password) throws Exception {
        loginClient.send(username, password);
        if (loginClient.isSuccess()) {
            this.token = loginClient.getLastSuccess().token();
            return true;
        } else {
            return false;
        }
    }

    public boolean logout() throws Exception {
        logoutClient.send(token);
        if (logoutClient.isSuccess()) {
            token = "";
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteAccount(String username) throws Exception {
        deleteAccountClient.send(username, token);
        if (deleteAccountClient.isSuccess()) {
            token = "";
            return true;
        } else {
            return false;
        }
    }

    public boolean checkSession() throws Exception {
        checkSessionClient.send(token);
        return checkSessionClient.isSuccess() && checkSessionClient.getLastSuccess().valid();
    }

    public long createProject(String name, String description, String sceneJson) throws Exception {
        createProjectClient.send(token, name, description, sceneJson);
        if (!createProjectClient.isSuccess()) {
            throw new RuntimeException(createProjectClient.getLastError().message());
        }
        return createProjectClient.getLastSuccess().projectId();
    }

    public boolean updateProject(long projectId, String name, String description, String sceneJson)
            throws Exception {
        updateProjectClient.send(token, projectId, name, description, sceneJson);
        return updateProjectClient.isSuccess();
    }

    public boolean deleteProject(long projectId) throws Exception {
        deleteProjectClient.send(token, projectId);
        return deleteProjectClient.isSuccess();
    }

    public String inviteCollaborator(long projectId, long collaboratorUserId, boolean canEdit)
            throws Exception {
        inviteCollaboratorClient.send(token, projectId, collaboratorUserId, canEdit);
        if (!inviteCollaboratorClient.isSuccess()) {
            throw new RuntimeException(inviteCollaboratorClient.getLastError().message());
        }
        return inviteCollaboratorClient.getLastSuccess().accessKey();
    }

    public boolean removeCollaborator(long projectId, long collaboratorUserId) throws Exception {
        removeCollaboratorClient.send(token, projectId, collaboratorUserId);
        return removeCollaboratorClient.isSuccess();
    }

    public boolean acceptInvite(String accessKey) throws Exception {
        acceptInviteClient.send(token, accessKey);
        return acceptInviteClient.isSuccess();
    }

    public boolean refuseInvite(String accessKey) throws Exception {
        refuseInviteClient.send(token, accessKey);
        return refuseInviteClient.isSuccess();
    }

    public List<ListOwnedProjectsClient.ProjectDto> listOwnedProjects() throws Exception {
        listOwnedProjectsClient.send(token);
        if (!listOwnedProjectsClient.isSuccess()) {
            throw new RuntimeException(listOwnedProjectsClient.getLastError().message());
        }
        return listOwnedProjectsClient.getLastSuccess().projects();
    }

    public List<ListPendingInvitesClient.PendingInviteDto> listPendingInvites() throws Exception {
        listPendingInvitesClient.send(token);
        if (!listPendingInvitesClient.isSuccess()) {
            throw new RuntimeException(listPendingInvitesClient.getLastError().message());
        }
        return listPendingInvitesClient.getLastSuccess().invites();
    }

    public List<ListSentInvitesClient.SentInviteDto> listSentInvites() throws Exception {
        listSentInvitesClient.send(token);
        if (!listSentInvitesClient.isSuccess()) {
            throw new RuntimeException(listSentInvitesClient.getLastError().message());
        }
        return listSentInvitesClient.getLastSuccess().invites();
    }

    public List<ListUsersClient.UserDto> listUsers() throws Exception {
        listUsersClient.send(token);
        if (!listUsersClient.isSuccess()) {
            throw new RuntimeException(listUsersClient.getLastError().message());
        }
        return listUsersClient.getLastSuccess().users();
    }

    public List<ListCollaboratedProjectsClient.CollaboratedProjectDto> listCollaboratedProjects()
            throws Exception {
        listCollaboratedProjectsClient.send(token);
        if (!listCollaboratedProjectsClient.isSuccess()) {
            throw new RuntimeException(listCollaboratedProjectsClient.getLastError().message());
        }
        return listCollaboratedProjectsClient.getLastSuccess().projects();
    }

    public List<ListProjectCollaboratorsClient.CollaboratorDto> listProjectCollaborators()
            throws Exception {
        listProjectCollaboratorsClient.send(token);
        if (!listProjectCollaboratorsClient.isSuccess()) {
            throw new RuntimeException(listProjectCollaboratorsClient.getLastError().message());
        }
        return listProjectCollaboratorsClient.getLastSuccess().collaborators();
    }

    public List<ListOwnedProjectsWithCollaboratorsClient.OwnedProjectDto> listOwnedProjectsWithCollaborators()
            throws Exception {
        listOwnedProjectsWithCollaboratorsClient.send(token);
        if (!listOwnedProjectsWithCollaboratorsClient.isSuccess()) {
            throw new RuntimeException(listOwnedProjectsWithCollaboratorsClient.getLastError().message());
        }
        return listOwnedProjectsWithCollaboratorsClient.getLastSuccess().projects();
    }

    public List<Long> createProjects(List<CreateProjectClient.Payload> toCreate) {
        return toCreate.stream()
                .map(p -> {
                    try {
                        return createProject(p.name(), p.description(), p.sceneJson());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }
}

