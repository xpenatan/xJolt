package jolt.example.samples.app.tests.raycast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.actions.IntAction;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.LongArray;
import java.lang.reflect.Array;
import jolt.example.samples.app.tests.shapes.BoxShapeTest;
import jolt.gdx.GraphicManagerApi;

public abstract class RayCastTest extends BoxShapeTest {

    protected ImmediateModeRenderer renderer;
    protected Vector3 start = new Vector3();
    protected Vector3 end = new Vector3();
    protected Batch batch;
    protected BitmapFont font;
    protected LongArray rayCastUserData = new LongArray();

    private OrthographicCamera textCamera;

    @Override
    public void initialize() {
        super.initialize();
        renderer = GraphicManagerApi.graphicApi.createImmediateModeRenderer();
        batch = GraphicManagerApi.graphicApi.createSpriteBatch();
        font = GraphicManagerApi.graphicApi.createBitmapFont();
        textCamera = new OrthographicCamera();
    }


    @Override
    public void postPhysicsUpdate(boolean isPlaying, float deltaTime) {
        textCamera.setToOrtho(false);

        batch.setProjectionMatrix(textCamera.combined);
        batch.begin();
        int y = 200;
        font.draw(batch, "RayCast Size: " + rayCastUserData.size, 100, y);
        for(int i = 0; i < rayCastUserData.size; i++) {
            long userData = rayCastUserData.get(i);
            y -= 20;
            font.draw(batch, "UserData[" + i + "]: " + userData, 100, y);
        }
        batch.end();

        renderer.begin(camera.combined, ShapeRenderer.ShapeType.Line.getGlType());
        renderer.color(1, 0, 0, 1);
        renderer.vertex(start.x, start.y, start.z);
        renderer.vertex(end.x, end.y, end.z);
        renderer.end();
    }
}