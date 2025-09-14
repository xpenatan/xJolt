package jolt.gdx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.model.MeshPart;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public interface MeshBuilderExt {
    void begin(final long attributes);
    void begin(final VertexAttributes attributes);
    void begin(final long attributes, int primitiveType);
    void begin(final VertexAttributes attributes, int primitiveType);
    Mesh end();
    Mesh end(Mesh mesh);
    int getNumVertices();
    int getNumIndices();
    VertexAttributes getAttributes();
    short vertex(Vector3 pos, Vector3 nor, Color col, Vector2 uv);
    short vertex(final float... values);
    void index (final short value1, final short value2, final short value3);
    void index (final short value);
    MeshPart part(final String id, int primitiveType);
    void box(float width, float height, float depth);
}