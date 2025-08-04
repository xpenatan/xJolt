package jolt.gdx;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.model.MeshPart;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.PixmapTextureData;
import com.monstrous.gdx.webgpu.graphics.WgMesh;
import com.monstrous.gdx.webgpu.graphics.WgTexture;
import com.monstrous.gdx.webgpu.graphics.g3d.WgModel;
import com.monstrous.gdx.webgpu.graphics.g3d.model.WgMeshPart;
import com.monstrous.gdx.webgpu.graphics.g3d.utils.WgModelBuilder;
import com.monstrous.gdx.webgpu.graphics.utils.WgScreenUtils;

public class WGPUGraphicApi implements GraphicApi {
    @Override
    public void clearScreen(float r, float g, float b, float a, boolean clearDepth) {
        WgScreenUtils.clear(r, g, b, a, clearDepth);
    }

    @Override
    public DebugRenderer createDebugRenderer() {
        return new WGPUDebugRenderer();
    }

    @Override
    public ModelBuilder createModelBuilder() {
        return new WgModelBuilder();
    }

    @Override
    public MeshBuilderExt createMeshBuilder() {
        return new WGPUMeshBuilder();
    }

    @Override
    public Model createModel() {
        return new WgModel();
    }

    @Override
    public MeshPart createMeshPart() {
        return new WgMeshPart();
    }

    @Override
    public MeshPart createMeshPart(String id, Mesh mesh, int offset, int size, int type) {
        return new WgMeshPart(id, mesh, offset, size, type);
    }

    @Override
    public Mesh createMesh(boolean isStatic, int maxVertices, int maxIndices, VertexAttribute... attributes) {
        return new WgMesh(isStatic, maxVertices, maxIndices, attributes);
    }

    @Override
    public Mesh createMesh(boolean isStatic, int maxVertices, int maxIndices, VertexAttributes attributes) {
        return new WgMesh(isStatic, maxVertices, maxIndices, attributes);
    }

    @Override
    public Texture createTexture(Pixmap pixmap, boolean useMipMaps) {
        PixmapTextureData pixmapTextureData = new PixmapTextureData(pixmap, null, useMipMaps, false);
        return new WgTexture(pixmapTextureData);
    }
}