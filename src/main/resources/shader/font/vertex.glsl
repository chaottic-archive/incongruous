#version 460 core

layout (location = 0) in vec4 position;

uniform mat4 projection;
uniform mat4 view;
uniform mat4 model;

struct info {
    mat4 translation;
};

layout (std430, binding = 0) readonly buffer buf {
    info array[];
};

out gl_PerVertex {
    vec4 gl_Position;
};

void main() {
    info instanceInfo = array[gl_InstanceID];

    gl_Position = projection * model * instanceInfo.translation * position;
}
