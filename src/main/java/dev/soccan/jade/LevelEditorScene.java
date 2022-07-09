package dev.soccan.jade;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;

import dev.soccan.components.SpriteRenderer;
import dev.soccan.renderer.Shader;
import dev.soccan.renderer.Texture;
import dev.soccan.util.Time;

public class LevelEditorScene extends Scene {

    private float[] vertexArray = {
            // Position (3) color (4) UV coordinates (2)
            100.5f, 0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1, 1, // Bottom right 0
            0.5f, 100.5f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0, 0, // top left 1
            100.5f, 100.5f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1, 0, // top right 2
            0.5f, 0.5f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 0, 1, // bottom left 3
    };

    // IMPORTANT: THIS MUST BE IN COUNTER CLOCKWISE ORDER
    private int[] elementArray = {
            2, 1, 0, // top right triangle
            0, 1, 3 // bottom left triangle
    };

    // VertexArrayObject, VertexBufferObject, ElementBufferObject
    private int vaoID, vboID, eboID;

    private Shader defaultShader;
    private Texture testTexture;

    GameObject testObj;
    private boolean firstTime = false;

    public LevelEditorScene() {
    }

    @Override
    public void init() {
        System.out.println("Creating testobject");
        this.testObj = new GameObject("testObject");
        this.testObj.addComponent(new SpriteRenderer());
        this.addGameObjectToScene(this.testObj);

        this.camera = new Camera(new Vector2f());
        defaultShader = new Shader("assets/shaders/default.glsl");
        defaultShader.compile();
        this.testTexture = new Texture("assets/images/testImage.jpg");

        // ==============================================================
        // Genereate VAO, VBO, and EBO buffer objects and send to the GPU
        // ==============================================================
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // Create a float buffer of vertecies
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        // Create VBO upload the vertex buffer
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        // Create den indeces and upload
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        // Add the vertex attribute pointers
        int positionsSize = 3;
        int colorSize = 4;
        int uvSize = 2;
        int vertexSizeBytes = (positionsSize + colorSize + uvSize) * Float.BYTES;

        // This is the 0 and 1 from "location" in the shader
        glVertexAttribPointer(0, positionsSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionsSize * Float.BYTES);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, uvSize, GL_FLOAT, false, vertexSizeBytes, (positionsSize + colorSize) * Float.BYTES);
        glEnableVertexAttribArray(2);
    }

    @Override
    public void update(float dt) {
        camera.position.x -= dt * 50.0f;
        defaultShader.use();

        // Upload texture to shader
        defaultShader.uploadTexture("TEX_SAMPLER", 0);
        glActiveTexture(GL_TEXTURE0);
        testTexture.bind();

        defaultShader.uploadMat4f("uProjection", camera.getProjectionMatrix());
        defaultShader.uploadMat4f("uView", camera.getViewMatrix());
        defaultShader.uploadFloat("uTime", Time.getTime());
        // Bind our VAO
        glBindVertexArray(vaoID);
        // Enable Vertex attribute pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        // Unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
        defaultShader.detach();

        if (!firstTime) {
            System.out.println("Creating another game object!");
            GameObject go = new GameObject("Game test 2");
            go.addComponent(new SpriteRenderer());
            this.addGameObjectToScene(go);
            firstTime = true;
        }

        for (GameObject go : this.gameObjects) {
            go.update(dt);
        }
    }

}
