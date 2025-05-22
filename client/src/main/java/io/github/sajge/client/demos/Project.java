package io.github.sajge.client.demos;

import java.sql.Timestamp;

public record Project
        (
                long id,
                String name,
                String description,
                User owner,
                String scene,
                Timestamp timestamp
        ) {}
