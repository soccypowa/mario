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
import dev.soccan.physics2D.primitives.Circle;
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
        obj2 = new Transform(new Vector2f(100, 300));
        rb1 = new Rigidbody2D();
        rb2 = new Rigidbody2D();
        rb1.setRawTransform(obj1);
        rb2.setRawTransform(obj2);
        rb1.setMass(100.0f);
        rb2.setMass(200.0f);

        Circle c1 = new Circle();
        c1.setRadius(10.0f);
        c1.setRigidBody(rb1);
        Circle c2 = new Circle();
        c2.setRadius(20.0f);
        c2.setRigidBody(rb2);
        rb1.setCollider(c1);
        rb2.setCollider(c2);

        physics.addRigidbody(rb1, true);
        physics.addRigidbody(rb2, false);

        loadResources();
        this.camera = new Camera(new Vector2f(-250, 0));
        sprites = AssetPool.getSpriteSheet("assets/images/spritesheets/decorationsAndBlocks.png");
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

        DebugDraw.addCircle(obj1.position, 10.0f, new Vector3f(1,
                0, 0));
        DebugDraw.addCircle(obj2.position, 20.0f, new Vector3f(0,
                1, 0));
        physics.update(dt);

    }

    @Override
    public void render() {
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
