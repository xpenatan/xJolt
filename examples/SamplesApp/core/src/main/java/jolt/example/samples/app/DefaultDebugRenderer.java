package jolt.example.samples.app;

import jolt.DebugRendererEm;
import jolt.RMat44;
import jolt.RVec3;
import jolt.jolt.core.Color;
import jolt.jolt.geometry.AABox;

public class DefaultDebugRenderer extends DebugRendererEm {
    @Override
    protected void DrawLine(RVec3 inFrom, RVec3 inTo, Color inColor) {
        System.out.println("DrawLine");
    }

    @Override
    protected void DrawTriangle(RVec3 inV1, RVec3 inV2, RVec3 inV3, Color inColor, int inCastShadow) {
        System.out.println("DrawTriangle");
    }

    @Override
    protected void DrawText3D(RVec3 inPosition, long inString, int inStringLen, Color inColor, float inHeight) {
        System.out.println("DrawText3D");
    }

    @Override
    protected void DrawGeometryWithID(RMat44 inModelMatrix, AABox inWorldSpaceBounds, float inLODScaleSq, Color inModelColor, int inGeometryID, int inCullMode, int inCastShadow, int inDrawMode) {
        System.out.println("DrawGeometryWithID");
    }

    @Override
    protected int CreateTriangleBatchID(long inTriangles, int inTriangleCount) {
        System.out.println("CreateTriangleBatchID");
        return 0;
    }

    @Override
    protected int CreateTriangleBatchIDWithIndex(long inVertices, int inVertexCount, long inIndices, int inIndexCount) {
        System.out.println("CreateTriangleBatchIDWithIndex");
        return 0;
    }
}
