package jolt.example.samples.app.jolt;

import jolt.core.TempAllocatorImpl;
import jolt.example.samples.app.Layers;
import jolt.Jolt;
import jolt.core.Factory;
import jolt.core.JobSystemThreadPool;
import jolt.physics.PhysicsSystem;
import jolt.physics.collision.ObjectLayerPairFilterTable;
import jolt.physics.collision.broadphase.BroadPhaseLayer;
import jolt.physics.collision.broadphase.BroadPhaseLayerInterfaceTable;
import jolt.physics.collision.broadphase.ObjectVsBroadPhaseLayerFilterTable;

public class JoltInstance {

    private PhysicsSystem physicsSystem;
    private Factory factory;
    private ObjectVsBroadPhaseLayerFilterTable mObjectVsBroadPhaseLayerFilter;
    private BroadPhaseLayer BP_LAYER_NON_MOVING;
    private BroadPhaseLayer BP_LAYER_MOVING;
    private BroadPhaseLayerInterfaceTable mBroadPhaseLayerInterface;
    private ObjectLayerPairFilterTable mObjectLayerPairFilter;
    private TempAllocatorImpl mTempAllocator;
    private JobSystemThreadPool mJobSystem;

    public JoltInstance() {
        Jolt.Init();

        int mMaxBodies = 10240;
        int mMaxBodyPairs = 65536;
        int mMaxContactConstraints = 10240;
        int mTempAllocatorSize = 10 * 1024 * 1024;
        int cNumBodyMutexes = 0;

        // Layer that objects can be in, determines which other objects it can collide with
        // Typically you at least want to have 1 layer for moving bodies and 1 layer for static bodies, but you can have more
        // layers if you want. E.g. you could have a layer for high detail collision (which is not used by the physics simulation
        // but only if you do collision testing).

        mObjectLayerPairFilter = new ObjectLayerPairFilterTable(Layers.NUM_LAYERS);
        mObjectLayerPairFilter.EnableCollision(Layers.NON_MOVING, Layers.MOVING);
        mObjectLayerPairFilter.EnableCollision(Layers.MOVING, Layers.MOVING);

        // Each broadphase layer results in a separate bounding volume tree in the broad phase. You at least want to have
        // a layer for non-moving and moving objects to avoid having to update a tree full of static objects every frame.
        // You can have a 1-on-1 mapping between object layers and broadphase layers (like in this case) but if you have
        // many object layers you'll be creating many broad phase trees, which is not efficient.

        int NUM_BROAD_PHASE_LAYERS = 2;
        mBroadPhaseLayerInterface = new BroadPhaseLayerInterfaceTable(Layers.NUM_LAYERS, NUM_BROAD_PHASE_LAYERS);
        BP_LAYER_NON_MOVING = new BroadPhaseLayer((short)0);
        mBroadPhaseLayerInterface.MapObjectToBroadPhaseLayer(Layers.NON_MOVING, BP_LAYER_NON_MOVING);
        BP_LAYER_MOVING = new BroadPhaseLayer((short)1);
        mBroadPhaseLayerInterface.MapObjectToBroadPhaseLayer(Layers.MOVING, BP_LAYER_MOVING);

        mObjectVsBroadPhaseLayerFilter = new ObjectVsBroadPhaseLayerFilterTable(mBroadPhaseLayerInterface, NUM_BROAD_PHASE_LAYERS, mObjectLayerPairFilter, Layers.NUM_LAYERS);

        mTempAllocator = Jolt.New_TempAllocatorImpl(mTempAllocatorSize);
        mJobSystem = Jolt.New_JobSystemThreadPool(4);

        factory = Jolt.New_Factory();
        Factory.set_sInstance(factory);
        Jolt.RegisterTypes();
        physicsSystem = Jolt.New_PhysicsSystem();
        physicsSystem.Init(mMaxBodies, cNumBodyMutexes, mMaxBodyPairs, mMaxContactConstraints, mBroadPhaseLayerInterface, mObjectVsBroadPhaseLayerFilter, mObjectLayerPairFilter);
    }

    public PhysicsSystem getPhysicsSystem() {
        return physicsSystem;
    }

    public JobSystemThreadPool getJobSystem() {
        return mJobSystem;
    }

    public void update(float deltaTime, int inCollisionSteps) {
        physicsSystem.Update(deltaTime, inCollisionSteps, mTempAllocator, mJobSystem);
    }

    public void clearWorld() {
        Jolt.ClearWorld(physicsSystem);
    }

    public void dispose() {
        physicsSystem.dispose();
        BP_LAYER_NON_MOVING.dispose();
        BP_LAYER_MOVING.dispose();
        mObjectLayerPairFilter.dispose();

        Factory.set_sInstance(null);
        factory.dispose();
        Jolt.UnregisterTypes();
    }
}
