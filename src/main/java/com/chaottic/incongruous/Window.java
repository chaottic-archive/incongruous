package com.chaottic.incongruous;

import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL46.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL46.glClear;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.system.MemoryUtil.memAddress;

public final class Window {
    private final long window;

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

                var width = buffer.get(0);
                var height = buffer.get(1);

                glfwSetWindowPos(window,
                        (buffer.get(4) - width) / 2,
                        (buffer.get(5) - height) / 2);
            }
        }

        glfwMakeContextCurrent(window);
    }

    public void draw() {
        GL.createCapabilities();

        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT);

            glfwSwapBuffers(window);

            glfwPollEvents();
        }
    }

    public void destroy() {
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
    }
}
