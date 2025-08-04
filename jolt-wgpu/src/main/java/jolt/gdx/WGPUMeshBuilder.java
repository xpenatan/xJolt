package jolt.gdx;

import com.badlogic.gdx.graphics.Mesh;
import com.monstrous.gdx.webgpu.graphics.WgMesh;
import com.monstrous.gdx.webgpu.graphics.utils.WgMeshBuilder;

public class WGPUMeshBuilder extends WgMeshBuilder implements MeshBuilderExt {
    @Override
    public Mesh end(Mesh mesh) {
        return super.end((WgMesh)mesh);
    }
}