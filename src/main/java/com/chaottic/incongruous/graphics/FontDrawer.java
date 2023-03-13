package com.chaottic.incongruous.graphics;

import com.chaottic.incongruous.graphics.ProgramPipeline;
import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.lwjgl.opengl.GL46.*;
import static org.lwjgl.system.MemoryUtil.*;

public final class FontDrawer {
    private final ProgramPipeline pipeline;

    private final int vao;
    private final int vbo;
    private final int ebo;

    {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            {
                var buffer = stack.callocInt(3);

                var address = memAddress(buffer);

                nglCreateVertexArrays(1, address);
                nglCreateBuffers(2, address + 4);

                vao = buffer.get(0);
                vbo = buffer.get(1);
                ebo = buffer.get(2);
            }

            {
                var size = 8.0F / 64.0F;

                var buffer = memCalloc(88);

                buffer
                        .putFloat(-size).putFloat(-size).putFloat(0.0F).putFloat(1.0F)
                        .putFloat( size).putFloat(-size).putFloat(0.0F).putFloat(1.0F)
                        .putFloat( size).putFloat( size).putFloat(0.0F).putFloat(1.0F)
                        .putFloat(-size).putFloat( size).putFloat(0.0F).putFloat(1.0F);

                buffer
                        .putInt(0).putInt(1).putInt(2)
                        .putInt(2).putInt(3).putInt(0);

                buffer.flip();

                var vertices = 16 * 4;
                var element = 6 * 4;

                buffer.limit(vertices);
                glNamedBufferStorage(vbo, buffer, 0);
                buffer.position(vertices);

                buffer.limit(vertices + element);
                glNamedBufferStorage(ebo, buffer, 0);
                buffer.position(0);

                memFree(buffer);
            }

            glVertexArrayVertexBuffer(vao, 0, vbo, 0, 16);

            glVertexArrayElementBuffer(vao, ebo);

            pipeline = ProgramPipeline.create("font", stack);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void draw(List<String> list, Matrix4f projection, Matrix4f view, Matrix4f model) {
        glBindProgramPipeline(pipeline.pipeline());

        pipeline.uploadMatrices(projection, view, model);

        glBindVertexArray(vao);

        glEnableVertexArrayAttrib(vao, 0);

        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);

        glDisableVertexArrayAttrib(vao, 0);

        glBindVertexArray(0);

        glBindProgramPipeline(0);
    }

    private void translate(List<String> list) {
        var sum = list.stream().mapToInt(String::length).sum();

        var buffer = memCalloc(64 * sum);

        var matrix4f = new Matrix4f();

        for (byte[] bytes : list.stream().map(s -> s.getBytes(StandardCharsets.UTF_8)).toList()) {
            for (int i = 0; i < bytes.length; i++) {

                matrix4f.identity();
                matrix4f.translate(0.0F, 0.0F, 0.0F);

                matrix4f.get(64 * i, buffer);
            }
        }

        memFree(buffer);
    }
}
