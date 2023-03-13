package com.chaottic.incongruous.graphics;

import com.chaottic.incongruous.Resources;
import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;

import static org.lwjgl.opengl.GL46.*;

public record ProgramPipeline(int pipeline, int fragment, int vertex, int projectionAddress, int viewAddress, int modelAddress) {

    public void uploadMatrices(Matrix4f projection, Matrix4f view, Matrix4f model) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            var buffer = stack.callocFloat(16);

            glProgramUniformMatrix4fv(vertex, projectionAddress, false, projection.get(buffer));
            glProgramUniformMatrix4fv(vertex, viewAddress, false, view.get(buffer));
            glProgramUniformMatrix4fv(vertex, modelAddress, false, model.get(buffer));
        }
    }

    public static ProgramPipeline create(String path, MemoryStack stack) throws IOException {
        var buffer = stack.callocInt(1);

        glCreateProgramPipelines(buffer);

        var pipeline = buffer.get(0);

        var fragment = glCreateShaderProgramv(GL_FRAGMENT_SHADER, Resources.readString("shader/font/fragment.glsl"));
        var vertex = glCreateShaderProgramv(GL_VERTEX_SHADER, Resources.readString("shader/font/vertex.glsl"));

        glUseProgramStages(pipeline, GL_FRAGMENT_SHADER_BIT, fragment);
        glUseProgramStages(pipeline, GL_VERTEX_SHADER_BIT, vertex);

        glValidateProgramPipeline(pipeline);

        return new ProgramPipeline(pipeline, fragment, vertex,
                glGetUniformLocation(vertex, "projection"),
                glGetUniformLocation(vertex, "view"),
                glGetUniformLocation(vertex, "model"));
    }
}
