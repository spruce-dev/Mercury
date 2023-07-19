#version 400 core

in vec2 pass_textureCoords;
in vec3 surfaceNormal;
in vec3 vectorToLight;
in vec3 vectorToCamera;
in float visibility;

out vec4 FragColour;

uniform sampler2D textureSampler;
uniform vec3 lightColour;
uniform float shineDamper;
uniform float reflectivity;
uniform vec3 skyColour;

void main(void) {
    vec3 unitNormal = normalize(surfaceNormal);
    vec3 unitLightVector = normalize(vectorToLight);

    float nDotProduct = dot(unitNormal, unitLightVector);
    float brightness = max(nDotProduct, 0.0);
    vec3 diffuse = brightness * lightColour;

    vec3 unitVectorToCamera = normalize(vectorToCamera);
    vec3 lightDirection = -unitLightVector;
    vec3 reflectedLightDir = reflect(lightDirection, unitNormal);

    float specularFactor = dot(reflectedLightDir, unitVectorToCamera);
    specularFactor = max(specularFactor, 0.2);
    float dampedFactor = pow(specularFactor, shineDamper);
    vec3 finalSpecular = dampedFactor * lightColour * reflectivity;

    FragColour = vec4(diffuse, 1.0) * texture(textureSampler, pass_textureCoords) + vec4(finalSpecular, 1.0);
    FragColour = mix(vec4(skyColour, 1.0), FragColour, visibility);
}