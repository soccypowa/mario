package dev.soccan.physics2D;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Vector;

import org.joml.Vector2f;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import dev.soccan.physics2D.primitives.Box2D;
import dev.soccan.physics2D.primitives.Circle;
import dev.soccan.physics2D.rigidbody.IntersectionDetector2D;
import dev.soccan.physics2D.rigidbody.Rigidbody2D;
import dev.soccan.renderer.Line2D;
import dev.soccan.util.JMath;

public class IntersectionDetector2DTests {
    private final float EPSILON = 0.000001f;

    // ============================================================================================
    // Line Intersection tests
    // ============================================================================================
    @Test
    public void pointOnLine2DStartShouldReturnTrue() {
        Line2D line = new Line2D(new Vector2f(0, 0), new Vector2f(12, 4));
        Vector2f point = new Vector2f(0, 0);

        assertTrue(IntersectionDetector2D.pointOnLIne(point, line));
    }

    @Test
    public void pointOnLIne2DMiddleShouldReturnTrue() {
        Line2D line = new Line2D(new Vector2f(0, 0), new Vector2f(12, 4));
        Vector2f point = new Vector2f(6, 2);

        assertTrue(IntersectionDetector2D.pointOnLIne(point, line));
    }

    @Test
    public void pointOnLine2DEndShouldReturnTrue() {
        Line2D line = new Line2D(new Vector2f(0, 0), new Vector2f(12, 4));
        Vector2f point = new Vector2f(12, 4);

        assertTrue(IntersectionDetector2D.pointOnLIne(point, line));
    }

    @Test
    public void pointOnVerticalLine2DMiddleShouldReturnTrue() {
        Line2D line = new Line2D(new Vector2f(0, 0), new Vector2f(0, 10));
        Vector2f point = new Vector2f(0, 5);

        // To get a more helpful test you could
        // boolean result = IntersectionDetector2D.pointOnLIne(point, line);
        // assertTrue(result);
        assertTrue(IntersectionDetector2D.pointOnLIne(point, line));
    }

    @Test
    public void pointNotOnLine2DShouldReturnFalse() {
        Line2D line = new Line2D(new Vector2f(0, 0), new Vector2f(12, 4));
        Vector2f point = new Vector2f(4, 1);

        assertFalse(IntersectionDetector2D.pointOnLIne(point, line));
    }

    @Test
    @Disabled
    public void closestPointToLine2DShouldReturnTrue() {
        Line2D line = new Line2D(new Vector2f(0, 0), new Vector2f(12, 4));
        Vector2f point = new Vector2f(6, 2);

        Vector2f calculatedClosestPoint = IntersectionDetector2D.closestPoint(point, line);
        Vector2f actualClosestPoint = new Vector2f(6, 2);

        assertTrue(JMath.compare(calculatedClosestPoint, actualClosestPoint));
    }

    // =========================================================================================================
    // Ray2Dcast IntersectionDetector2D tests
    // =========================================================================================================

    // =========================================================================================================
    // Circle intersection tester tests
    // =========================================================================================================

    @Test
    public void pointInCircleShouldReturnTrue() {
        Circle circle = new Circle();
        circle.setRadius(5f);
        Rigidbody2D rigidbody = new Rigidbody2D();
        circle.setRigidBody(rigidbody);

        Vector2f p1 = new Vector2f(3, -2);
        Vector2f p2 = new Vector2f(-4.9f, 0);

        assertAll(
                () -> assertTrue(IntersectionDetector2D.pointInCircle(p1, circle)),
                () -> assertTrue(IntersectionDetector2D.pointInCircle(p2, circle)));
    }

    @Test
    public void pointNotInCircleShouldReturnFalse() {
        Circle circle = new Circle();
        circle.setRadius(5f);
        Rigidbody2D rigidbody = new Rigidbody2D();
        circle.setRigidBody(rigidbody);

        Vector2f point = new Vector2f(-6, -6);

        assertFalse(IntersectionDetector2D.pointInCircle(point, circle));
    }

    @Test
    public void pointInMovedCircleShouldReturnTrue() {
        Circle circle = new Circle();
        circle.setRadius(5f);
        Rigidbody2D rigidbody = new Rigidbody2D();
        rigidbody.setTransform(new Vector2f(10));
        circle.setRigidBody(rigidbody);

        Vector2f point = new Vector2f(-4.9f + 10, 0 + 10);

        assertTrue(IntersectionDetector2D.pointInCircle(point, circle));
    }

    @Test
    public void pointNotInMovedCircleShouldReturnFalse() {
        Circle circle = new Circle();
        circle.setRadius(5f);
        Rigidbody2D rigidbody = new Rigidbody2D();
        rigidbody.setTransform(new Vector2f(10));
        circle.setRigidBody(rigidbody);

        Vector2f point = new Vector2f(-6 + 10, -6 + 10);

        assertFalse(IntersectionDetector2D.pointInCircle(point, circle));
    }

    // =========================================================================================================
    // Box2D intersection tester tests
    // =========================================================================================================

    @Test
    public void pointInBox2DShouldReturnTrue() {
        Box2D box = new Box2D();
        box.setSize(new Vector2f(10));
        Rigidbody2D rigidbody = new Rigidbody2D();
        box.serRigidBody(rigidbody);

        Vector2f p1 = new Vector2f(4, 4.3f);
        Vector2f p2 = new Vector2f(-4.9f, -4.9f);

        assertAll(
                () -> assertTrue(IntersectionDetector2D.pointInBox2D(p1, box)),
                () -> assertTrue(IntersectionDetector2D.pointInBox2D(p2, box)));
    }

    @Test
    public void pointNotInBox2DShouldReturnFalse() {
        Box2D box = new Box2D();
        box.setSize(new Vector2f(10));
        Rigidbody2D rigidbody = new Rigidbody2D();
        box.serRigidBody(rigidbody);

        Vector2f point = new Vector2f(0, 5.1f);

        assertFalse(IntersectionDetector2D.pointInBox2D(point, box));
    }

    @Test
    public void pointInMovedBox2DShouldReturnTrue() {
        Box2D box = new Box2D();
        box.setSize(new Vector2f(10));
        Rigidbody2D rigidbody = new Rigidbody2D();
        rigidbody.setTransform(new Vector2f(10));
        box.serRigidBody(rigidbody);

        Vector2f p1 = new Vector2f(4 + 10, 4.3f + 10);
        Vector2f p2 = new Vector2f(-4.9f + 10, -4.9f + 10);

        assertAll(
                () -> assertTrue(IntersectionDetector2D.pointInBox2D(p1, box)),
                () -> assertTrue(IntersectionDetector2D.pointInBox2D(p2, box)));
    }

    @Test
    public void pointNotInMovedBox2DShouldReturnFalse() {
        Box2D box = new Box2D();
        box.setSize(new Vector2f(10));
        Rigidbody2D rigidbody = new Rigidbody2D();
        rigidbody.setTransform(new Vector2f(10));
        box.serRigidBody(rigidbody);

        Vector2f point = new Vector2f(0 + 10, 5.1f + 10);

        assertFalse(IntersectionDetector2D.pointInBox2D(point, box));
    }

    @Test
    public void pointInRotatedBox2DShouldReturnTrue() {
        Box2D box = new Box2D();
        box.setSize(new Vector2f(10));
        Rigidbody2D rigidbody = new Rigidbody2D();
        rigidbody.setTransform(new Vector2f(0), 45);
        box.serRigidBody(rigidbody);

        Vector2f p1 = new Vector2f(-1, -1);
        Vector2f p2 = new Vector2f(-3.43553390593f, 3.43553390593f);

        assertAll(
                () -> assertTrue(IntersectionDetector2D.pointInBox2D(p1, box)),
                () -> assertTrue(IntersectionDetector2D.pointInBox2D(p2, box)));
    }

    @Test
    public void pointNotInRotatedBox2DShouldReturnFalse() {
        Box2D box = new Box2D();
        box.setSize(new Vector2f(10));
        Rigidbody2D rigidbody = new Rigidbody2D();
        rigidbody.setTransform(new Vector2f(0), 45);
        box.serRigidBody(rigidbody);

        Vector2f point = new Vector2f(-3.63553390593f, 3.63553390593f);

        assertFalse(IntersectionDetector2D.pointInBox2D(point, box));
    }

    @Test
    public void pointInMovedRotatedBox2DShouldReturnTrue() {
        Box2D box = new Box2D();
        box.setSize(new Vector2f(10));
        Rigidbody2D rigidbody = new Rigidbody2D();
        rigidbody.setTransform(new Vector2f(10), 45);
        box.serRigidBody(rigidbody);

        Vector2f p1 = new Vector2f(-1 + 10, -1 + 10);
        Vector2f p2 = new Vector2f(-3.43553390593f + 10, 3.43553390593f + 10);

        assertAll(
                () -> assertTrue(IntersectionDetector2D.pointInBox2D(p1, box)),
                () -> assertTrue(IntersectionDetector2D.pointInBox2D(p2, box)));
    }

    @Test
    public void pointNotInMovedRotatedBox2DShouldReturnFalse() {
        Box2D box = new Box2D();
        box.setSize(new Vector2f(10));
        Rigidbody2D rigidbody = new Rigidbody2D();
        rigidbody.setTransform(new Vector2f(10), 45);
        box.serRigidBody(rigidbody);

        Vector2f point = new Vector2f(-3.63553390593f + 10, 3.63553390593f + 10);

        assertFalse(IntersectionDetector2D.pointInBox2D(point, box));
    }
}
