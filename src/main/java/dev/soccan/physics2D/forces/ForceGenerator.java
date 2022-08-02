package dev.soccan.physics2D.forces;

import dev.soccan.physics2D.rigidbody.Rigidbody2D;

public interface ForceGenerator {
    void updateForce(Rigidbody2D rigidbody, float dt);
}
