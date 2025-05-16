package io.github.sajge.server.invites;

public class InviteService {
    private final InviteDao dao;

    public InviteService(InviteDao dao) {
        this.dao = dao;
    }

    public void inviteUser(String inviter, long projectId, String invitee) throws Exception {
        dao.inviteUser(inviter, projectId, invitee);
    }
}
