package dev.soccan.physics2D;

import static org.junit.Assert.assertTrue;

import org.joml.Vector2f;
import org.junit.Test;

import dev.soccan.physics2D.rigidbody.IntersectionDetector2D;
import dev.soccan.renderer.Line2D;

public class CollisionDetectorTests {
    private final float EPSILON = 0.000001f;

    @Test
    public void pointOnLine2DStartShouldReturnTrue() {
        Line2D line = new Line2D(new Vector2f(0, 0), new Vector2f(12, 4));
        Vector2f point = new Vector2f(0, 0);

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
}
