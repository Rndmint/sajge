package io.proto.spike.renderer.scene;

import java.awt.*;

public class Material {
    public Color color;

    public Material(Color color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "Material{" +
                "color=" + color +
                '}';
    }
}
