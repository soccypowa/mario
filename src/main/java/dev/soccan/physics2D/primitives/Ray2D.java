package dev.soccan.physics2D.primitives;

import org.joml.Vector2f;

public class Ray2D {
    private Vector2f origin;
    private Vector2f direction;

    public Ray2D(Vector2f origin, Vector2f direction) {
        this.origin = origin;
        this.direction = direction;
    }

    public Vector2f getOrigin() {
        return origin;
    }

    public Vector2f getDirection() {
        return direction;
    }

}
