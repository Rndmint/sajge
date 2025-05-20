package io.github.sajge.server.projects;

import io.github.sajge.logger.Logger;
import io.github.sajge.server.projects.lists.collaborated.CollaboratedProjectDto;
import io.github.sajge.server.projects.lists.collaborators.CollaboratorDto;
import io.github.sajge.server.projects.lists.invited.SentInviteDto;
import io.github.sajge.server.projects.lists.pendings.PendingInviteDto;
import io.github.sajge.server.projects.lists.projectncollab.OwnedProjectWithCollaboratorsDto;
import io.github.sajge.server.security.SessionManager;

import java.util.*;
import java.util.stream.Collectors;

public class ProjectService {
    private static final Logger logger = Logger.get(ProjectService.class);
    private final ProjectDao dao;

    public ProjectService(ProjectDao dao) {
        logger.debug("ProjectService initialized with dao={}", dao);
        this.dao = dao;
    }

    public long createProject(String token,
                              String name,
                              String description,
                              String sceneJson)
            throws Exception {
        logger.debug("createProject() called with token={}, name={}, description length={}, sceneJson length={}",
                token, name, description != null ? description.length() : 0,
                sceneJson != null ? sceneJson.length() : 0);

        long userId = SessionManager.INSTANCE.getUserId(token);
        logger.debug("createProject: resolved userId={} from token={}", userId, token);

        long projectId = dao.createProject(name, description, sceneJson, userId);
        logger.info("Project created: id={} by user={}", projectId, userId);
        return projectId;
    }

    public boolean updateProject(String token,
                                 long projectId,
                                 String name,
                                 String description,
                                 String sceneJson)
            throws Exception {
        logger.debug(
                "updateProject() called with token={}, projectId={}, name={}, description length={}, sceneJson length={}",
                token, projectId, name, description != null ? description.length() : 0,
                sceneJson != null ? sceneJson.length() : 0);

        long userId = SessionManager.INSTANCE.getUserId(token);
        logger.debug("updateProject: resolved userId={} from token={}", userId, token);

        Map<String, Object> proj = dao.findProjectById(projectId);
        logger.debug("updateProject: fetched project {} = {}", projectId, proj);
        if (proj == null) {
            logger.warn("updateProject: project {} not found", projectId);
            return false;
        }

        long ownerId = ((Number) proj.get("owner_id")).longValue();
        logger.debug("updateProject: project ownerId={}", ownerId);

        boolean isOwner = ownerId == userId;
        boolean canEdit = false;
        if (!isOwner) {
            Map<String, Object> perm = dao.findPermission(projectId, userId);
            logger.debug("updateProject: fetched permission = {}", perm);
            canEdit = perm != null
                    && Boolean.TRUE.equals(perm.get("can_edit"))
                    && "accepted".equals(perm.get("status"));
            logger.debug("updateProject: canEdit={}", canEdit);
        }

        if (!isOwner && !canEdit) {
            logger.warn("updateProject: user {} not authorized to update project {}", userId, projectId);
            return false;
        }

        int updated = dao.updateProject(projectId, name, description, sceneJson);
        boolean success = updated > 0;
        if (success) {
            logger.info("updateProject: project {} updated by user {}, {} rows affected",
                    projectId, userId, updated);
        } else {
            logger.warn("updateProject: project {} update by user {} affected no rows", projectId, userId);
        }
        return success;
    }

    public boolean deleteProject(String token,
                                 long projectId)
            throws Exception {
        logger.debug("deleteProject() called with token={}, projectId={}", token, projectId);

        long userId = SessionManager.INSTANCE.getUserId(token);
        logger.debug("deleteProject: resolved userId={} from token={}", userId, token);

        Map<String, Object> proj = dao.findProjectById(projectId);
        logger.debug("deleteProject: fetched project {} = {}", projectId, proj);
        if (proj == null) {
            logger.warn("deleteProject: project {} not found", projectId);
            return false;
        }

        long ownerId = ((Number) proj.get("owner_id")).longValue();
        if (ownerId != userId) {
            logger.warn("deleteProject: user {} is not owner of project {}", userId, projectId);
            return false;
        }

        int deleted = dao.deleteProject(projectId);
        boolean success = deleted > 0;
        if (success) {
            logger.info("deleteProject: project {} deleted by user {}", projectId, userId);
        } else {
            logger.warn("deleteProject: project {} deletion by user {} affected no rows", projectId, userId);
        }
        return success;
    }

    public String inviteCollaborator(String token,
                                     long projectId,
                                     long collaboratorUserId,
                                     boolean canEdit)
            throws Exception {
        logger.debug("inviteCollaborator() called with token={}, projectId={}, collaboratorUserId={}, canEdit={}",
                token, projectId, collaboratorUserId, canEdit);

        long userId = SessionManager.INSTANCE.getUserId(token);
        logger.debug("inviteCollaborator: resolved userId={} from token={}", userId, token);

        if (collaboratorUserId == userId) {
            throw new IllegalArgumentException("Cannot invite yourself as a collaborator");
        }

        Map<String, Object> proj = dao.findProjectById(projectId);
        logger.debug("inviteCollaborator: fetched project {} = {}", projectId, proj);
        if (proj == null || ((Number) proj.get("owner_id")).longValue() != userId) {
            logger.warn("inviteCollaborator: user {} is not owner of project {}", userId, projectId);
            throw new IllegalAccessException("Only owner can invite");
        }

        String accessKey = UUID.randomUUID().toString();
        dao.addPermission(projectId, collaboratorUserId, canEdit, accessKey);
        logger.info(
                "inviteCollaborator: collaborator {} invited to project {} by user {} with accessKey={} and canEdit={}",
                collaboratorUserId, projectId, userId, accessKey, canEdit);

        return accessKey;
    }

    public boolean removeCollaborator(String token,
                                      long projectId,
                                      long collaboratorUserId) throws Exception {
        long userId = SessionManager.INSTANCE.getUserId(token);
        var proj = dao.findProjectById(projectId);
        if (proj == null || ((Number)proj.get("owner_id")).longValue() != userId) {
            throw new IllegalAccessException("Only owner can remove collaborators");
        }
        int rows = dao.deletePermission(projectId, collaboratorUserId);
        return rows > 0;
    }

    public boolean acceptInvite(String token,
                                String accessKey)
            throws Exception {
        logger.debug("acceptInvite() called with token={}, accessKey={}", token, accessKey);

        long userId = SessionManager.INSTANCE.getUserId(token);
        logger.debug("acceptInvite: resolved userId={} from token={}", userId, token);

        Map<String, Object> perm = dao.findPermissionByAccessKey(accessKey);
        logger.debug("acceptInvite: fetched permission = {}", perm);
        if (perm == null || ((Number) perm.get("user_id")).longValue() != userId) {
            logger.warn("acceptInvite: invalid or unauthorized accessKey={} for user {}", accessKey, userId);
            return false;
        }

        String status = (String) perm.get("status");
        if (!"pending".equals(status)) {
            return false;
        }

        long projectId = ((Number) perm.get("project_id")).longValue();
        int updated = dao.updatePermissionStatus(projectId, accessKey, "accepted");
        boolean success = updated > 0;
        if (success) {
            logger.info("acceptInvite: accessKey={} accepted by user={} on project={}, {} rows affected",
                    accessKey, userId, projectId, updated);
        } else {
            logger.warn("acceptInvite: accessKey={} acceptance by user={} affected no rows", accessKey, userId);
        }
        return success;
    }

    public boolean refuseInvite(String token,
                                String accessKey) throws Exception {
        long userId = SessionManager.INSTANCE.getUserId(token);
        var perm = dao.findPermissionByAccessKey(accessKey);
        if (perm == null || ((Number)perm.get("user_id")).longValue() != userId) {
            return false;
        }
        return dao.updatePermissionStatus(
                ((Number)perm.get("project_id")).longValue(),
                accessKey,
                "revoked"
        ) > 0;
    }

    public List<ProjectDto> listOwnedProjects(String token) throws Exception {
        long userId = SessionManager.INSTANCE.getUserId(token);
        List<Map<String, Object>> rows = dao.listProjectsByOwner(userId);

        List<ProjectDto> result = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            result.add(new ProjectDto(
                    ((Number) row.get("id")).longValue(),
                    (String) row.get("name"),
                    (String) row.get("description"),
                    ((Number) row.get("owner_id")).longValue(),
                    (String) row.get("scene"),
                    row.get("created_at").toString()
            ));
        }
        return result;
    }

    public List<PendingInviteDto> listPendingInvites(String token) throws Exception {
        long userId = SessionManager.INSTANCE.getUserId(token);
        List<Map<String, Object>> rows = dao.listPendingInvitesForUser(userId);

        List<PendingInviteDto> result = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            result.add(new PendingInviteDto(
                    ((Number) row.get("inviter_id")).longValue(),
                    (String) row.get("inviter_username"),
                    ((Number) row.get("project_id")).longValue(),
                    (String) row.get("access_key"),
                    row.get("created_at").toString()
            ));
        }
        return result;
    }

    public List<SentInviteDto> listSentInvites(String token) throws Exception {
        long ownerId = SessionManager.INSTANCE.getUserId(token);
        List<Map<String, Object>> rows = dao.listSentInvitesByOwner(ownerId);

        List<SentInviteDto> result = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            result.add(new SentInviteDto(
                    ((Number) row.get("invited_user_id")).longValue(),
                    (String) row.get("invited_username"),
                    ((Number) row.get("project_id")).longValue(),
                    (String) row.get("access_key"),
                    (String) row.get("status"),
                    row.get("created_at").toString()
            ));
        }
        return result;
    }

    public List<CollaboratedProjectDto> listCollaboratedProjects(String token) throws Exception {
        long userId = SessionManager.INSTANCE.getUserId(token);
        List<Map<String, Object>> rows = dao.listCollaboratedProjects(userId);

        List<CollaboratedProjectDto> out = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            out.add(new CollaboratedProjectDto(
                    ((Number) row.get("id")).longValue(),
                    (String) row.get("name"),
                    (String) row.get("description"),
                    (String) row.get("scene"),
                    ((Number) row.get("owner_id")).longValue(),
                    row.get("created_at").toString()
            ));
        }
        return out;
    }

    public List<CollaboratorDto> listProjectCollaborators(String token) throws Exception {
        long ownerId = SessionManager.INSTANCE.getUserId(token);
        List<Map<String, Object>> rows = dao.listCollaboratorsByOwner(ownerId);

        List<CollaboratorDto> out = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            out.add(new CollaboratorDto(
                    ((Number) row.get("collaborator_id")).longValue(),
                    (String) row.get("collaborator_username")
            ));
        }
        return out;
    }

    public List<OwnedProjectWithCollaboratorsDto> listOwnedProjectsWithCollaborators(String token)
            throws Exception {
        long ownerId = SessionManager.INSTANCE.getUserId(token);
        List<Map<String,Object>> rows =
                dao.listOwnedProjectsWithCollaborators(ownerId);

        Map<Long, OwnedProjectWithCollaboratorsDto.Builder> map = new LinkedHashMap<>();
        for (Map<String,Object> row : rows) {
            long pid = ((Number)row.get("project_id")).longValue();
            OwnedProjectWithCollaboratorsDto.Builder b = map.get(pid);
            if (b == null) {
                b = new OwnedProjectWithCollaboratorsDto.Builder(
                        pid,
                        (String) row.get("project_name"),
                        (String) row.get("project_description"),
                        (String) row.get("project_scene"),
                        ownerId,
                        row.get("project_created_at").toString()
                );
                map.put(pid, b);
            }
            Object collIdObj = row.get("collaborator_id");
            if (collIdObj != null) {
                long collId = ((Number)collIdObj).longValue();
                String collName = (String) row.get("collaborator_username");
                b.addCollaborator(new CollaboratorDto(collId, collName));
            }
        }
        return map.values().stream()
                .map(OwnedProjectWithCollaboratorsDto.Builder::build)
                .collect(Collectors.toList());
    }

}
