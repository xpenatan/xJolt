package jolt.gdx;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.model.MeshPart;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.utils.ScreenUtils;

public class GdxGraphicApi implements GraphicApi{
    @Override
    public void clearScreen(float r, float g, float b, float a, boolean clearDepth) {
        ScreenUtils.clear(r, g, b, a, clearDepth);
    }

    @Override
    public DebugRenderer createDebugRenderer() {
        return new DebugRenderer(new GdxModelBatch());
    }

    @Override
    public ModelBuilder createModelBuilder() {
        return new ModelBuilder();
    }

    @Override
    public MeshBuilderExt createMeshBuilder() {
        return new GdxMeshBuilder();
    }

    @Override
    public Model createModel() {
        return new Model();
    }

    @Override
    public MeshPart createMeshPart() {
        return new MeshPart();
    }

    @Override
    public MeshPart createMeshPart(String id, Mesh mesh, int offset, int size, int type) {
        return new MeshPart(id, mesh, offset, size, type);
    }

    @Override
    public Mesh createMesh(boolean isStatic, int maxVertices, int maxIndices, VertexAttribute... attributes) {
        return new Mesh(isStatic, maxVertices, maxIndices, attributes);
    }

    @Override
    public Mesh createMesh(boolean isStatic, int maxVertices, int maxIndices, VertexAttributes attributes) {
        return new Mesh(isStatic, maxVertices, maxIndices, attributes);
    }

    @Override
    public Texture createTexture(Pixmap pixmap, boolean useMipMaps) {
        return new Texture(pixmap, useMipMaps);
    }
}