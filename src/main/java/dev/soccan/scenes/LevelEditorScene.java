package dev.soccan.scenes;

import org.joml.Vector2f;
import org.joml.Vector3f;

import dev.soccan.components.GridLines;
import dev.soccan.components.MouseControls;
import dev.soccan.components.Sprite;
import dev.soccan.components.SpriteRenderer;
import dev.soccan.components.SpriteSheet;
import dev.soccan.jade.Camera;
import dev.soccan.jade.GameObject;
import dev.soccan.jade.Prefabs;
import dev.soccan.jade.Transform;
import dev.soccan.physics2D.PhysicsSystem2D;
import dev.soccan.physics2D.rigidbody.Rigidbody2D;
import dev.soccan.renderer.DebugDraw;
import dev.soccan.util.AssetPool;
import imgui.ImGui;
import imgui.ImVec2;

public class LevelEditorScene extends Scene {

    private SpriteSheet sprites;
    GameObject levelEditorStuff = new GameObject("LevelEditor", new Transform(new Vector2f()), 0);
    PhysicsSystem2D physics = new PhysicsSystem2D(1.0f / 60.0f, new Vector2f(0, -10));
    Transform obj1, obj2;
    Rigidbody2D rb1, rb2;

    public LevelEditorScene() {
    }

    @Override
    public void init() {
        levelEditorStuff.addComponent(new MouseControls());
        levelEditorStuff.addComponent(new GridLines());

        obj1 = new Transform(new Vector2f(100, 500));
        obj2 = new Transform(new Vector2f(200, 500));
        rb1 = new Rigidbody2D();
        rb2 = new Rigidbody2D();
        rb1.setRawTransform(obj1);
        rb2.setRawTransform(obj2);
        rb1.setMass(100.0f);
        rb2.setMass(200.0f);

        physics.addRigidbody(rb1);
        physics.addRigidbody(rb2);

        loadResources();
        this.camera = new Camera(new Vector2f(-250, 0));
        sprites = AssetPool.getSpriteSheet("assets/images/spritesheets/decorationsAndBlocks.png");

        if (levelLoaded) {
            // if (gameObjects.size() > 0) {
            // this.activeGameObject = gameObjects.get(1);
            // }
            return;
        }

        // GameObject obj2 = new GameObject("Object 2", new Transform(new Vector2f(400,
        // 100), new Vector2f(256, 256)), 4);
        // SpriteRenderer obj2SpriteRenderer = new SpriteRenderer();
        // Sprite obj2Sprite = new Sprite();
        // obj2Sprite.setTexture(AssetPool.getTexture("assets/images/blendimage2.png"));
        // obj2SpriteRenderer.setSprite(obj2Sprite);
        // obj2.addComponent(obj2SpriteRenderer);
        // this.addGameObjectToScene(obj2);

        // obj1 = new GameObject("Object 1", new Transform(new Vector2f(200, 100), new
        // Vector2f(256, 256)), 2);
        // SpriteRenderer obj1Sprite = new SpriteRenderer();
        // obj1Sprite.setColor(new Vector4f(1, 0, 0, 1));
        // obj1.addComponent(obj1Sprite);
        // obj1.addComponent(new RigidBody());
        // this.addGameObjectToScene(obj1);
        // this.activeGameObject = obj1;

    }

    private void loadResources() {
        AssetPool.getShader("assets/shaders/default.glsl");

        AssetPool.addSpriteSheet("assets/images/spritesheets/decorationsAndBlocks.png",
                new SpriteSheet(AssetPool.getTexture("assets/images/spritesheets/decorationsAndBlocks.png"), 16, 16, 81,
                        0));
        AssetPool.getTexture("assets/images/blendimage2.png");

        for (GameObject go : gameObjects) {
            if (go.getComponent(SpriteRenderer.class) != null) {
                SpriteRenderer spr = go.getComponent(SpriteRenderer.class);
                if (spr.getTexture() != null) {
                    spr.setTexture(AssetPool.getTexture(spr.getTexture().getFilePath()));
                }
            }
        }
    }

    float x = 0.0f;
    float y = 0.0f;

    @Override
    public void update(float dt) {
        levelEditorStuff.update(dt);
        // DebugDraw.addCircle(new Vector2f(x, y), 64, new Vector3f(0, 1, 0), 1);
        // x += 50f * dt;
        // y += 50f * dt;

        for (GameObject go : this.gameObjects) {
            go.update(dt);
        }

        // DebugDraw.addBox2D(obj1.position, new Vector2f(32, 32), 0.0f, new Vector3f(1,
        // 0, 0));
        // DebugDraw.addBox2D(obj2.position, new Vector2f(32, 32), 0.0f, new Vector3f(0,
        // 1, 0));
        // physics.update(dt);

        this.renderer.render();
    }

    @Override
    public void imgui() {
        ImGui.begin("Test window");

        ImVec2 windowPos = new ImVec2();
        ImGui.getWindowPos(windowPos);
        ImVec2 windowSize = new ImVec2();
        ImGui.getWindowSize(windowSize);
        ImVec2 itemSpacing = new ImVec2();
        ImGui.getStyle().getItemInnerSpacing(itemSpacing);

        float windowX2 = windowPos.x + windowSize.x;
        for (int i = 0; i < sprites.size(); i++) {
            Sprite sprite = sprites.getSprite(i);
            float spriteWidth = sprite.getWidth() * 2;
            float spriteHeight = sprite.getHeight() * 2;
            int id = sprite.getTexId();
            Vector2f[] texCoords = sprite.getTexCoords();

            ImGui.pushID(i);
            if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x,
                    texCoords[2].y)) {
                GameObject object = Prefabs.generateSpriteObject(sprite, 32, 32);
                // Attach this to the mouse cursor
                levelEditorStuff.getComponent(MouseControls.class).pickupObject(object);

            }
            ImGui.popID();

            ImVec2 lastButtonPos = new ImVec2();
            ImGui.getItemRectMax(lastButtonPos);
            float lastButtonX2 = lastButtonPos.x;
            float nextButtonPosX2 = lastButtonX2 + itemSpacing.x + spriteWidth;
            if (i + 1 < sprites.size() && nextButtonPosX2 < windowX2) {
                ImGui.sameLine();
            }
        }

        ImGui.end();
    }

}
