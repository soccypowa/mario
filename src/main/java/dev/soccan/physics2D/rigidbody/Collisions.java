package dev.soccan.physics2D.rigidbody;

import org.joml.Vector2f;

import dev.soccan.physics2D.primitives.Circle;

public class Collisions {
    public static CollisionManifold findCollisionFeatures(Circle a, Circle b) {
        CollisionManifold result = new CollisionManifold();
        float sumRadii = a.getRadius() + b.getRadius();
        Vector2f distance = new Vector2f(b.getCenter()).sub(a.getCenter());
        if (distance.lengthSquared() - (sumRadii * sumRadii) > 0) {
            return result;
        }

        // Multiply by 0.5 because we want to speprate each circle the sam amount.
        // Consider updateingto consider in the momentum and velocity.
        float depth = Math.abs(distance.length() - sumRadii) * 0.5f;
        Vector2f normal = new Vector2f(distance);
        normal.normalize();
        float distanceToPoint = a.getRadius() - depth;
        Vector2f contactPoint = new Vector2f(a.getCenter()).add(new Vector2f(normal).mul(distanceToPoint));

        result = new CollisionManifold(normal, depth);
        result.addContactPoint(contactPoint);

        return result;
    }
}
