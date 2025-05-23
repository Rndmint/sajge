package io.proto.throwawaycode.exceptions;

import io.proto.throwawaycode.exceptions.math.Matrix4x4f;
import io.proto.throwawaycode.exceptions.math.Vector2f;
import io.proto.throwawaycode.exceptions.math.Vector3f;
import io.proto.throwawaycode.exceptions.math.Vector4f;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class SoftwareRendererDemo extends JPanel {

    private final BufferedImage framebuffer;
    private final int width = 800;
    private final int height = 600;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Software Pipeline (Orthographic)");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        SoftwareRendererDemo panel = new SoftwareRendererDemo();
        frame.setContentPane(panel);
        frame.setSize(panel.width, panel.height);
        frame.setVisible(true);
    }

    public SoftwareRendererDemo() {
        framebuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        render();
    }

    private void render() {
        clear(Color.BLACK);

        // Triangle in model space
        Vector4f[] triangle = {
                new Vector4f(-0.5f, -0.5f, 0f, 1f),
                new Vector4f( 0.5f, -0.5f, 0f, 1f),
                new Vector4f( 0f,    0.5f, 0f, 1f)
        };

        // Matrices
        Matrix4x4f model = Matrix4x4f.identity();
        Matrix4x4f view = lookAt(
                new Vector3f(0f, 0f, 1f),   // eye
                new Vector3f(0f, 0f, 0f),   // target
                new Vector3f(0f, 1f, 0f)    // up
        );
        Matrix4x4f projection = orthographic(-1, 1, -1, 1, -1, 1);
        Matrix4x4f mvp = projection.multiply(view).multiply(model);

        // Transform and rasterize
        Vector2f[] screen = new Vector2f[3];
        for (int i = 0; i < 3; i++) {
            Vector4f clip = mvp.multiply(triangle[i]);
            Vector3f ndc = clip.toCartesian3f();
            screen[i] = ndcToScreen(ndc);
        }

        rasterizeTriangle(screen[0], screen[1], screen[2], Color.WHITE);
    }

    private void clear(Color color) {
        Graphics2D g = framebuffer.createGraphics();
        g.setColor(color);
        g.fillRect(0, 0, width, height);
        g.dispose();
    }

    private Matrix4x4f orthographic(float left, float right, float bottom, float top, float near, float far) {
        float sx = 2f / (right - left);
        float sy = 2f / (top - bottom);
        float sz = -2f / (far - near);
        float tx = -(right + left) / (right - left);
        float ty = -(top + bottom) / (top - bottom);
        float tz = -(far + near) / (far - near);

        return new Matrix4x4f(
                sx, 0,  0,  tx,
                0,  sy, 0,  ty,
                0,  0,  sz, tz,
                0,  0,  0,  1
        );
    }

    private Matrix4x4f lookAt(Vector3f eye, Vector3f center, Vector3f up) {
        Vector3f f = center.sub(eye).normalize();
        Vector3f r = f.cross(up).normalize();
        Vector3f u = r.cross(f).normalize();

        float[][] m = new float[4][4];
        m[0][0] = r.data[0]; m[0][1] = r.data[1]; m[0][2] = r.data[2]; m[0][3] = -r.dot(eye);
        m[1][0] = u.data[0]; m[1][1] = u.data[1]; m[1][2] = u.data[2]; m[1][3] = -u.dot(eye);
        m[2][0] = -f.data[0]; m[2][1] = -f.data[1]; m[2][2] = -f.data[2]; m[2][3] = f.dot(eye);
        m[3][0] = 0; m[3][1] = 0; m[3][2] = 0; m[3][3] = 1;

        return new Matrix4x4f(m);
    }

    private Vector2f ndcToScreen(Vector3f ndc) {
        float x = (ndc.data[0] + 1f) * 0.5f * width;
        float y = (1f - (ndc.data[1] + 1f) * 0.5f) * height; // flip y
        return new Vector2f(x, y);
    }

    private void rasterizeTriangle(Vector2f a, Vector2f b, Vector2f c, Color color) {
        int minX = (int) Math.floor(Math.min(a.data[0], Math.min(b.data[0], c.data[0])));
        int minY = (int) Math.floor(Math.min(a.data[1], Math.min(b.data[1], c.data[1])));
        int maxX = (int) Math.ceil(Math.max(a.data[0], Math.max(b.data[0], c.data[0])));
        int maxY = (int) Math.ceil(Math.max(a.data[1], Math.max(b.data[1], c.data[1])));

        float area = edgeFunction(a, b, c);

        for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {
                Vector2f p = new Vector2f(x + 0.5f, y + 0.5f);

                float w0 = edgeFunction(b, c, p);
                float w1 = edgeFunction(c, a, p);
                float w2 = edgeFunction(a, b, p);

                if (w0 >= 0 && w1 >= 0 && w2 >= 0) {
                    setPixel(x, y, color);
                }
            }
        }
    }

    private float edgeFunction(Vector2f a, Vector2f b, Vector2f c) {
        return (c.data[0] - a.data[0]) * (b.data[1] - a.data[1])
                - (c.data[1] - a.data[1]) * (b.data[0] - a.data[0]);
    }

    private void setPixel(int x, int y, Color color) {
        if (x >= 0 && y >= 0 && x < width && y < height) {
            framebuffer.setRGB(x, y, color.getRGB());
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.drawImage(framebuffer, 0, 0, null);
    }
}
