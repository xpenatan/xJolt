package jolt.example.samples.app.tests.vehicle;

import jolt.example.samples.app.Test;
import jolt.jolt.physics.body.Body;

public abstract class VehicleTest extends Test {

    @Override
    protected void initialize() {

        Body floor = CreateMeshTerrain();
        floor.SetFriction(1.0f);
    }
}