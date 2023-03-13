package com.chaottic.incongruous.graphics;

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
    private final int ssbo;

    {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            {
                var buffer = stack.callocInt(4);

                var address = memAddress(buffer);

                nglCreateVertexArrays(1, address);
                nglCreateBuffers(3, address + 4);

                vao = buffer.get(0);
                vbo = buffer.get(1);
                ebo = buffer.get(2);
                ssbo = buffer.get(3);
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

            // No DSA equivalent.
            glBindVertexArray(vao);
            glBindBuffer(GL_SHADER_STORAGE_BUFFER, ssbo);
            glBindBufferBase(GL_SHADER_STORAGE_BUFFER, 0, ssbo);

            glBindBuffer(GL_SHADER_STORAGE_BUFFER, 0);

            glBindVertexArray(0);

            glVertexArrayVertexBuffer(vao, 0, vbo, 0, 16);

            glVertexArrayElementBuffer(vao, ebo);

            pipeline = ProgramPipeline.create("font", stack);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void draw(List<String> list, Matrix4f projection, Matrix4f view, Matrix4f model) {
        var sum = list.stream().mapToInt(String::length).sum();

        translate(list, sum);

        glBindProgramPipeline(pipeline.pipeline());

        pipeline.uploadMatrices(projection, view, model);

        glBindVertexArray(vao);

        glEnableVertexArrayAttrib(vao, 0);

        glDrawElementsInstanced(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0, sum);

        glDisableVertexArrayAttrib(vao, 0);

        glBindVertexArray(0);

        glBindProgramPipeline(0);
    }

    private void translate(List<String> list, int sum) {
        var buffer = memCalloc(64 * sum);

        var matrix4f = new Matrix4f();

        var size = 8.0F / 64.0F;

        for (byte[] bytes : list.stream().map(s -> s.getBytes(StandardCharsets.UTF_8)).toList()) {
            for (var j = 0; j < bytes.length; j++) {
                matrix4f.identity();
                matrix4f.translate(size * j, 0.0F, 0.0F);

                matrix4f.get(64 * j, buffer);
            }
        }

        glNamedBufferData(ssbo, buffer, GL_DYNAMIC_DRAW);

        memFree(buffer);
    }
}
