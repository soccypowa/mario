package dev.soccan.physics2D.rigidbody;

import java.util.Vector;

import org.joml.Vector2f;

import dev.soccan.physics2D.primitives.AABB;
import dev.soccan.physics2D.primitives.Box2D;
import dev.soccan.physics2D.primitives.Circle;
import dev.soccan.renderer.Line2D;
import dev.soccan.util.JMath;

public class IntersectionDetector2D {
    // ====================
    // Point vs Primitive tests
    // ====================

    public static boolean pointOnLIne(Vector2f point, Line2D line) {
        float dy = line.getEnd().y - line.getStart().y;
        float dx = line.getEnd().x - line.getStart().x;
        float m = dy / dx;

        float b = line.getEnd().y - (m * line.getEnd().x);

        // Check the line equation
        return point.y == m * point.x + b;
    }

    public static boolean pointInCircle(Vector2f point, Circle circle) {
        Vector2f circleCenter = circle.getCenter();
        Vector2f centerToPoint = new Vector2f(point).sub(circleCenter);

        return centerToPoint.lengthSquared() <= circle.getRadius() * circle.getRadius();
    }

    public static boolean pointInAABB(Vector2f point, AABB box) {
        Vector2f min = box.getMin();
        Vector2f max = box.getMax();

        return point.x <= max.x && min.x <= point.x && point.y <= max.y && min.y <= point.y;
    }

    public static boolean pointInBox2D(Vector2f point, Box2D box) {
        // Translate teh point into local space
        Vector2f pointLocalBoxSpace = new Vector2f(point);
        JMath.rotate(pointLocalBoxSpace, box.getRigidbody().getRotation(), box.getRigidbody().getPosition());

        Vector2f min = box.getMin();
        Vector2f max = box.getMax();

        return pointLocalBoxSpace.x <= max.x && min.x <= pointLocalBoxSpace.x && pointLocalBoxSpace.y <= max.y
                && min.y <= pointLocalBoxSpace.y;
    }

    // ====================
    // Line vs Primitive tests
    // ====================
}
