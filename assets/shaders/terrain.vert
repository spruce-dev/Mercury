#version 400 core

in vec3 position;
in vec2 textureCoords;
in vec3 normal;

out vec2 pass_textureCoords;
out vec3 surfaceNormal;
out vec3 vectorToLight;
out vec3 vectorToCamera;
out float visibility;

uniform mat4 transformMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 lightPosition;

const float fogDensity = 0.007;
const float fogGradient = 5;

void main(void) {
    vec4 worldPosition = transformMatrix * vec4(position, 1.0);
    vec4 relativePositionToCamera = viewMatrix * worldPosition;
    gl_Position = projectionMatrix * relativePositionToCamera;
    pass_textureCoords = textureCoords * 40.0;

    surfaceNormal = worldPosition.xyz;
    vectorToLight = lightPosition - worldPosition.xyz;
    vectorToCamera = (inverse(viewMatrix) * vec4(0.0, 0.0, 0.0, 1.0)).xyz - worldPosition.xyz;

    float distance = length(relativePositionToCamera.xyz);
    visibility = exp(-pow((distance * fogDensity), fogGradient));
    visibility = clamp(visibility, 0.0, 1.0);
}