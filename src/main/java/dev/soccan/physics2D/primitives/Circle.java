package dev.soccan.physics2D.primitives;

import org.joml.Vector2f;

import dev.soccan.physics2D.rigidbody.Rigidbody2D;

public class Circle {
    private float radius = 1.0f;
    private Rigidbody2D rigidbody = null;

    public float getRadius() {
        return radius;
    }

    public Vector2f getCenter() {
        return rigidbody.getPosition();
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public void serRigidBody(Rigidbody2D rigidbody) {
        this.rigidbody = rigidbody;
    }

}
