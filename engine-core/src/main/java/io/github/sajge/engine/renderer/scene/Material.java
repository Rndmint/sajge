package io.github.sajge.engine.renderer.scene;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.awt.Color;
import io.github.sajge.logger.Logger;

public class Material {
    private static final Logger log = Logger.get(Material.class);

    @JsonIgnore
    private Color color;

    @JsonProperty("color")
    private String colorHex;

    public Material() {
        this.color = Color.WHITE;
        this.colorHex = "#FFFFFF";
        log.debug("Created default Material with white");
    }

    @JsonCreator
    public Material(@JsonProperty("color") String hex) {
        this.colorHex = hex;
        this.color = Color.decode(hex);
        log.debug("Deserialized Material with color={}", color);
    }

    public Material(Color c) {
        this.colorHex = String.format("#%06X", c.getRGB() & 0xFFFFFF);
        this.color = c;
        log.debug("Deserialized Material with color={}", color);
    }

    @JsonProperty("color")
    public String getColorHex() {
        log.trace("getColorHex() => {}", colorHex);
        return colorHex;
    }

    @JsonProperty("color")
    public void setColorHex(String hex) {
        this.colorHex = hex;
        this.color = Color.decode(hex);
        log.trace("setColorHex({}) -> {}", hex, color);
    }

    @JsonIgnore
    public Color getColor() {
        return color;
    }

    @JsonIgnore
    public void setColor(Color c) {
        this.color = c;
        this.colorHex = String.format("#%06X", c.getRGB() & 0xFFFFFF);
        log.trace("setColor(Color) -> hex={}", colorHex);
    }
}
