package jolt.gdx;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.model.MeshPart;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

public interface GraphicApi {
    void clearScreen(float r, float g, float b, float a, boolean clearDepth);
    DebugRenderer createDebugRenderer();
    ModelBuilder createModelBuilder();
    MeshBuilderExt createMeshBuilder();
    Model createModel();
    MeshPart createMeshPart();
    MeshPart createMeshPart(final String id, final Mesh mesh, final int offset, final int size, final int type);
    Mesh createMesh(boolean isStatic, int maxVertices, int maxIndices, VertexAttribute... attributes);
    Mesh createMesh(boolean isStatic, int maxVertices, int maxIndices, VertexAttributes attributes);
    Texture createTexture(Pixmap pixmap, boolean useMipMaps);
}