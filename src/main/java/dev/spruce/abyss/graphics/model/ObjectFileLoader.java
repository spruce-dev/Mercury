package dev.spruce.abyss.graphics.model;

import de.javagl.obj.Obj;
import de.javagl.obj.ObjData;
import de.javagl.obj.ObjReader;
import de.javagl.obj.ObjUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class ObjectFileLoader {

    public static Model loadFromObjectFile(String filePath){
        Model model = null;
        try {
            InputStream stream = new FileInputStream(filePath);
            Obj object = ObjUtils.convertToRenderable(ObjReader.read(stream));

            IntBuffer indices = ObjData.getFaceVertexIndices(object);
            int[] inds = new int[indices.capacity()];
            indices.get(inds);

            FloatBuffer vertices = ObjData.getVertices(object);
            float[] verts = new float[vertices.capacity()];
            vertices.get(verts);

            FloatBuffer texCoords = ObjData.getTexCoords(object, 2);
            float[] texs = new float[texCoords.capacity()];
            texCoords.get(texs);

            FloatBuffer normals = ObjData.getNormals(object);
            float[] norms = new float[normals.capacity()];
            normals.get(norms);

            model = ModelLoader.get().loadToVAO(verts, inds, texs, norms);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize file stream reader when loading object file!");
        }
        return model;
    }
}
