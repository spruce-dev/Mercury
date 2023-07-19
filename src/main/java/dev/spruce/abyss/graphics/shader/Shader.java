package dev.spruce.abyss.graphics.shader;

import dev.spruce.abyss.graphics.model.Model;
import dev.spruce.abyss.graphics.model.ModelLoader;
import dev.spruce.abyss.graphics.model.TexturedModel;
import dev.spruce.abyss.graphics.texture.Texture;
import dev.spruce.abyss.graphics.texture.TextureLoader;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public abstract class Shader {

    private String vertexShaderSrc;
    private String fragmentShaderSrc;

    private int vertexShader;
    private int fragmentShader;
    private int shaderProgram;

    private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);

    public Shader(String vertexFileLocation, String fragmentFileLocation){
        this.vertexShaderSrc = loadShaderFile(vertexFileLocation);
        this.fragmentShaderSrc = loadShaderFile(fragmentFileLocation);
        init();
        getAllUniformLocations();
    }

    private void init(){
        vertexShader = compileShader(vertexShaderSrc, GL_VERTEX_SHADER);
        fragmentShader = compileShader(fragmentShaderSrc, GL_FRAGMENT_SHADER);
        shaderProgram = linkProgram(vertexShader, fragmentShader);
    }

    public void begin(){
        glUseProgram(shaderProgram);
    }

    public void end(){
        glUseProgram(0);
    }

    public void free(){
        end();
        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);
        glDeleteProgram(shaderProgram);
    }

    protected abstract void bindAttributes();

    protected void bindAttribute(int attribute, String variableName){
        glBindAttribLocation(shaderProgram, attribute, variableName);
    }

    protected int getUniformLocation(String uniform) {
        return glGetUniformLocation(shaderProgram, uniform);
    }

    protected void uploadFloat(int location, float value) {
        glUniform1f(location, value);
    }

    protected void uploadVector3f(int location, Vector3f vector) {
        glUniform3f(location, vector.x, vector.y, vector.z);
    }

    protected void uploadBoolean(int location, boolean value) {
        glUniform1f(location, value ? 1f : 0f);
    }

    protected void uploadMatrix4f(int location, Matrix4f matrix) {
        float[] floatData = new float[16];
        matrix.get(floatData);
        matrixBuffer.put(floatData);
        matrixBuffer.flip();
        glUniformMatrix4fv(location, false, matrixBuffer);
    }

    protected abstract void getAllUniformLocations();

    private int compileShader(String shaderSource, int shaderType) {
        int shaderId = glCreateShader(shaderType);
        glShaderSource(shaderId, shaderSource);
        glCompileShader(shaderId);

        int success = glGetShaderi(shaderId, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            String log = glGetShaderInfoLog(shaderId);
            throw new RuntimeException("Shader compilation failed:\n" + log);
        }

        return shaderId;
    }

    private int linkProgram(int vertexShader, int fragmentShader) {
        int programId = glCreateProgram();
        glAttachShader(programId, vertexShader);
        glAttachShader(programId, fragmentShader);
        bindAttributes();
        glLinkProgram(programId);

        int success = glGetProgrami(programId, GL_LINK_STATUS);
        if (success == GL_FALSE) {
            String log = glGetProgramInfoLog(programId);
            throw new RuntimeException("Shader program linking failed:\n" + log);
        }

        glDetachShader(programId, vertexShader);
        glDetachShader(programId, fragmentShader);

        return programId;
    }

    private String loadShaderFile(String filePath) {
        StringBuilder shaderSource = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = reader.readLine()) != null){
                shaderSource.append(line).append("\n");
            }
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return shaderSource.toString();
    }
}
