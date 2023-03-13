package com.chaottic.incongruous;

import com.chaottic.incongruous.graphics.FontDrawer;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.util.List;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL46.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL46.glClear;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.system.MemoryUtil.memAddress;

public final class Window {
    private final long window;

    private int width;
    private int height;

    {
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 6);

        if ((window = glfwCreateWindow(854, 480, "Incongruous", NULL, NULL)) == NULL) {
            glfwTerminate();
            throw new RuntimeException("Failed to create a new GLFW Window");
        }

        var monitor = glfwGetPrimaryMonitor();

        @Nullable
        var videoMode = glfwGetVideoMode(monitor);

        if (videoMode != null) {
            try (MemoryStack stack = MemoryStack.stackPush()) {
                var buffer = stack.callocInt(6);

                var address = memAddress(buffer);

                nglfwGetWindowSize(window, address, address + 4);

                nglfwGetMonitorWorkarea(monitor, address + 8, address + 12, address + 16, address + 20);

                width = buffer.get(0);
                height = buffer.get(1);

                glfwSetWindowPos(window,
                        (buffer.get(4) - width) / 2,
                        (buffer.get(5) - height) / 2);
            }
        }

        glfwSetWindowSizeCallback(window, (window, width, height) -> {
            this.width = width;
            this.height = height;

            glViewport(0, 0, width, height);
        });

        glfwMakeContextCurrent(window);
    }

    public void draw() {
        GL.createCapabilities();

        var projection = new Matrix4f();
        var view = new Matrix4f();
        var model = new Matrix4f();

        var fontDrawer = new FontDrawer();

        glClearColor(1.0F, 0.0F, 0.0F, 0.0F);

        while (!glfwWindowShouldClose(window)) {
            var aspectRatio = (float) width / (float) height;

            var fieldOfView = 2.0F;

            projection.identity();
            projection.ortho(-fieldOfView * aspectRatio, fieldOfView * aspectRatio, -fieldOfView, fieldOfView, 1.0F, -1.0F);

            view.identity();
            model.identity();

            glClear(GL_COLOR_BUFFER_BIT);

            fontDrawer.draw(List.of("hello world"), projection, view, model);

            glfwSwapBuffers(window);

            glfwPollEvents();
        }
    }

    public void destroy() {
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
    }
}
