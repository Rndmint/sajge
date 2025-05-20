package io.github.sajge.server.projects.lists.projectncollab;

import io.github.sajge.server.projects.lists.collaborators.CollaboratorDto;

import java.util.ArrayList;
import java.util.List;

public record OwnedProjectWithCollaboratorsDto(
        long id,
        String name,
        String description,
        String sceneJson,
        long ownerId,
        String createdAt,
        List<CollaboratorDto> collaborators
) {
    public static class Builder {
        private final long id;
        private final String name, description, sceneJson;
        private final long ownerId;
        private final String createdAt;
        private final List<CollaboratorDto> collaborators = new ArrayList<>();

        public Builder(long id, String name, String description,
                       String sceneJson, long ownerId, String createdAt) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.sceneJson = sceneJson;
            this.ownerId = ownerId;
            this.createdAt = createdAt;
        }
        public void addCollaborator(CollaboratorDto dto) {
            collaborators.add(dto);
        }
        public OwnedProjectWithCollaboratorsDto build() {
            return new OwnedProjectWithCollaboratorsDto(
                    id, name, description, sceneJson, ownerId, createdAt,
                    List.copyOf(collaborators)
            );
        }
    }
}
