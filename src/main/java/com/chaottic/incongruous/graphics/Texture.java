package com.chaottic.incongruous.graphics;

import com.chaottic.incongruous.Resources;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.util.List;

import static org.lwjgl.opengl.GL46.*;
import static org.lwjgl.stb.STBImage.nstbi_image_free;
import static org.lwjgl.stb.STBImage.nstbi_load_from_memory;
import static org.lwjgl.system.MemoryUtil.memAddress;
import static org.lwjgl.system.MemoryUtil.memFree;

public final class Texture {

    private Texture() {}

    public static int createTextureArray(List<String> paths, MemoryStack stack, int width, int height) throws IOException {
        var buffer = stack.callocInt(3);

        var address = memAddress(buffer);

        nglCreateTextures(GL_TEXTURE_2D_ARRAY, 1, address);

        var texture = buffer.get(0);

        glTextureParameteri(texture, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTextureParameteri(texture, GL_TEXTURE_MIN_FILTER, GL_NEAREST_MIPMAP_LINEAR);

        var size = paths.size();

        glTextureStorage3D(texture, 1, GL_RGBA8, width, height, size);

        for (int i = 0; i < size; i++) {
            var pixels = Resources.readPixels(paths.get(i));

            var l = nstbi_load_from_memory(memAddress(pixels), pixels.remaining(), address + 4, address + 8, address + 12, 4);

            width = buffer.get(1);
            height = buffer.get(2);

            glTextureSubImage3D(texture, 0, 0, 0, i, width, height, 1, GL_RGBA, GL_UNSIGNED_BYTE, l);

            nstbi_image_free(l);

            memFree(pixels);
        }

        return texture;
    }
}
