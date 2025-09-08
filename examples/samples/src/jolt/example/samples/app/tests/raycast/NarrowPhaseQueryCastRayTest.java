package jolt.example.samples.app.tests.raycast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import jolt.RRayCast;
import jolt.example.samples.app.tests.shapes.BoxShapeTest;
import jolt.gdx.GraphicManagerApi;
import jolt.math.Vec3;
import jolt.physics.body.BodyID;
import jolt.physics.collision.ArrayRayCastResult;
import jolt.physics.collision.CastRayAllHitCollisionCollector;
import jolt.physics.collision.NarrowPhaseQuery;
import jolt.physics.collision.RayCastResult;
import jolt.physics.collision.RayCastSettings;

public class NarrowPhaseQueryCastRayTest extends BoxShapeTest {

    private ImmediateModeRenderer renderer;
    private Vector3 start = new Vector3();
    private Vector3 end = new Vector3();

    @Override
    public void initialize() {
        super.initialize();
        renderer = GraphicManagerApi.graphicApi.createImmediateModeRenderer();
    }

    @Override
    public void prePhysicsUpdate(boolean isPlaying) {
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            Ray pickRay = camera.getPickRay(Gdx.input.getX(), Gdx.input.getY());
            start.set(pickRay.origin);
            pickRay.getEndPoint(end, 1000);

            final Vec3 rayOrigin = new Vec3(start.x, start.y, start.z);
            final Vec3 rayDirection = new Vec3(end.x, end.y, end.z);

            final RRayCast ray = new RRayCast(rayOrigin, rayDirection);
            rayOrigin.dispose();
            rayDirection.dispose();
            final RayCastSettings settings = new RayCastSettings();
            final CastRayAllHitCollisionCollector collector = new CastRayAllHitCollisionCollector();
            NarrowPhaseQuery narrowPhaseQuery = mPhysicsSystem.GetNarrowPhaseQuery();

            narrowPhaseQuery.CastRay(ray, settings, collector);
            ray.dispose();
            settings.dispose();
            collector.Sort();

            ArrayRayCastResult mHits = collector.get_mHits();
            int size = mHits.size();
            System.out.println("RayCast Hit: " + collector.HadHit() + " Size: " + size);
            for(int i = 0; i < size; i++) {
                RayCastResult result = mHits.at(i);
                BodyID mBodyID = result.get_mBodyID();
                long userData = mBodyInterface.GetUserData(mBodyID);
                System.out.println("UserData[" + i + "]: " + userData);
            }
            collector.dispose();
        }

        renderer.begin(camera.combined, ShapeRenderer.ShapeType.Line.getGlType());
        renderer.color(1, 0, 0, 1);
        renderer.vertex(start.x, start.y, start.z);
        renderer.vertex(end.x, end.y, end.z);
        renderer.end();
    }
}