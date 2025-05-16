package io.github.sajge.server.projects;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sajge.logger.Logger;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProjectService {
    private static final Logger logger = Logger.get(ProjectService.class);
    private final ProjectDao dao;
    private final ObjectMapper mapper = new ObjectMapper();

    public ProjectService(ProjectDao dao) {
        this.dao = dao;
    }

    public void createProject(String ownerUsername, String projectName) throws Exception {
        dao.createProject(ownerUsername, projectName);
    }

    public List<ProjectDto> listProjects(String username) throws Exception {
        long uid = dao.getUserId(username);
        List<Map<String,Object>> rows = dao.findProjectsByUser(uid);
        return rows.stream().map(r -> new ProjectDto(
                ((Number)r.get("id")).longValue(),
                (String)r.get("name"),
                mapper.convertValue(r.get("scene"), JsonNode.class)
        )).collect(Collectors.toList());
    }

    public void renameProject(String username, long pid, String newName) throws Exception {
        long uid = dao.getUserId(username);
        dao.updateName(uid, pid, newName);
    }

    public void updateProjectScene(String username, long pid, String sceneJson) throws Exception {
        long uid = dao.getUserId(username);
        dao.updateScene(uid, pid, sceneJson);
    }

    public void deleteProject(String username, long pid) throws Exception {
        long uid = dao.getUserId(username);
        dao.deleteProject(uid, pid);
    }
}
