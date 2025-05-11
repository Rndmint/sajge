package io.github.sajge.engine.renderer.scene;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.sajge.engine.renderer.core.Vec3;
import io.github.sajge.logger.Logger;

import java.awt.Color;
import java.util.Arrays;

public class Triangle {
    private static final Logger log = Logger.get(Triangle.class);

    @JsonIgnore
    private Color color;

    @JsonProperty("color")
    private String colorHex;

    private int id;
    private int[] indices;
    private Vec3 normal;

    public Triangle() {
        this.colorHex = "#808080";
        this.color = Color.decode(colorHex);
        log.debug("Created default Triangle with colorHex={}", colorHex);
    }

    @JsonCreator
    public Triangle(
            @JsonProperty("id") int id,
            @JsonProperty("indices") int[] indices,
            @JsonProperty("normal") Vec3 normal,
            @JsonProperty("color") String hex
    ) {
        this.id = id;
        this.indices = indices;
        this.normal = normal;
        this.colorHex = hex;
        this.color = Color.decode(hex);
        log.debug("Deserialized Triangle id={} indices={} normal={} colorHex={}",
                id, Arrays.toString(indices), normal, colorHex);
    }

    @JsonProperty("id")
    public int getId() {
        log.trace("getId() => {}", id);
        return id;
    }

    @JsonProperty("id")
    public void setId(int id) {
        log.trace("setId({})", id);
        this.id = id;
    }

    @JsonProperty("indices")
    public int[] getIndices() {
        log.trace("getIndices() => length={}", indices != null ? indices.length : 0);
        return indices;
    }

    @JsonProperty("indices")
    public void setIndices(int[] indices) {
        log.trace("setIndices length={}", indices != null ? indices.length : 0);
        this.indices = indices;
    }

    @JsonProperty("normal")
    public Vec3 getNormal() {
        log.trace("getNormal() => {}", normal);
        return normal;
    }

    @JsonProperty("normal")
    public void setNormal(Vec3 normal) {
        log.trace("setNormal({})", normal);
        this.normal = normal;
    }

    @JsonProperty("color")
    public String getColorHex() {
        log.trace("getColorHex() => {}", colorHex);
        return colorHex;
    }

    @JsonProperty("color")
    public void setColorHex(String hex) {
        log.trace("setColorHex({})", hex);
        this.colorHex = hex;
        this.color = Color.decode(hex);
    }

    @JsonIgnore
    public Color getColor() {
        return color;
    }

    @JsonIgnore
    public void setColor(Color c) {
        this.color = c;
        this.colorHex = String.format("#%06X", c.getRGB() & 0xFFFFFF);
        log.trace("setColor(Color) -> colorHex={}", colorHex);
    }

    @Override
    public String toString() {
        return "Triangle(id=" + id + ", indices=" + Arrays.toString(indices) +
                ", normal=" + normal + ", colorHex=" + colorHex + ")";
    }
}
