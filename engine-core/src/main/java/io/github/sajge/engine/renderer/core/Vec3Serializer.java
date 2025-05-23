package io.github.sajge.engine.renderer.core;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class Vec3Serializer extends JsonSerializer<Vec3> {
    @Override
    public void serialize(Vec3 v, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartArray();
        writeNumber(gen, v.x);
        writeNumber(gen, v.y);
        writeNumber(gen, v.z);
        gen.writeEndArray();
    }

    private void writeNumber(JsonGenerator gen, float f) throws IOException {
        if (f == (int) f) {
            gen.writeNumber((int) f);
        } else {
            gen.writeNumber(f);
        }
    }
}
