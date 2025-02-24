package jolt.example.samples.app;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;
import jolt.DebugRendererSimpleEm;
import jolt.RVec3;
import jolt.jolt.core.Color;

public class DebugRendererSimple extends DebugRendererSimpleEm {

    private Camera camera;
    private ShapeRenderer filledShapeRenderer;
    private ShapeRenderer lineShapeRenderer;
    private SpriteBatch spriteBatch;
    private com.badlogic.gdx.graphics.Color color;

    public DebugRendererSimple() {
        spriteBatch = new SpriteBatch();
        filledShapeRenderer = new ShapeRenderer();
        lineShapeRenderer = new ShapeRenderer();
        color = new com.badlogic.gdx.graphics.Color();
    }

    @Override
    protected void DrawLine(RVec3 inFrom, RVec3 inTo, Color inColor) {
        if(camera != null) {
            float fromX = (float)inFrom.GetX();
            float fromY = (float)inFrom.GetY();
            float fromZ = (float)inFrom.GetZ();
            float toX = (float)inTo.GetX();
            float toY = (float)inTo.GetY();
            float toZ = (float)inTo.GetZ();
            color.set(inColor.get_mU32());
            lineShapeRenderer.setColor(color);
            lineShapeRenderer.line(fromX, fromY, fromZ, toX, toY, toZ);
        }
    }

    @Override
    protected void DrawTriangle(RVec3 inV1, RVec3 inV2, RVec3 inV3, Color inColor, int inCastShadow) {
        if(camera != null) {
            float x1 = (float)inV1.GetX();
            float y1 = (float)inV1.GetY();
            float z1 = (float)inV1.GetZ();
            float x2 = (float)inV2.GetX();
            float y2 = (float)inV2.GetY();
            float z2 = (float)inV2.GetZ();
            float x3 = (float)inV3.GetX();
            float y3 = (float)inV3.GetY();
            float z3 = (float)inV3.GetZ();
            color.set(inColor.get_mU32());
            filledShapeRenderer.setColor(color);
            triangle3D(x1, y1, z1, x2, y2, z2, x3, y3, z3);
        }
    }

    @Override
    protected void DrawText3D(RVec3 inPosition, long inString, int inStringLen, Color inColor, float inHeight) {
        if(camera != null) {
            System.out.println("DrawText3D");
        }
    }

    public void begin(Camera camera) {
        this.camera = camera;

        filledShapeRenderer.setProjectionMatrix(camera.combined);
        filledShapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        lineShapeRenderer.setProjectionMatrix(camera.combined);
        lineShapeRenderer.begin(ShapeRenderer.ShapeType.Line);
    }

    public void begin(Viewport viewport) {
        begin(viewport.getCamera());
    }

    public void end() {
        filledShapeRenderer.end();
        lineShapeRenderer.end();
        camera = null;
    }

    public void dispose() {
        super.dispose();
        filledShapeRenderer.dispose();
        lineShapeRenderer.dispose();
        spriteBatch.dispose();
    }

    public void triangle3D(float x1, float y1, float z1,
                           float x2, float y2, float z2,
                           float x3, float y3, float z3) {
        ImmediateModeRenderer renderer = filledShapeRenderer.getRenderer();
        float colorBits = color.toFloatBits();
        renderer.color(colorBits);
        renderer.vertex(x1, y1, z1);
        renderer.color(colorBits);
        renderer.vertex(x2, y2, z2);
        renderer.color(colorBits);
        renderer.vertex(x3, y3, z3);
    }
}
