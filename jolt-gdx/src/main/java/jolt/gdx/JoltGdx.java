package jolt.gdx;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.Vector4;
import jolt.JoltNew;
import jolt.math.Mat44;
import jolt.math.Quat;
import jolt.math.Vec3;
import jolt.math.Vec4;

public class JoltGdx {

    public static Vec3 TMP_VEC3_01;
    public static Vec3 TMP_VEC3_02;
    public static Vec3 TMP_VEC3_03;

    public static Vec4 TMP_VEC4_01;
    public static Vec4 TMP_VEC4_02;
    public static Vec4 TMP_VEC4_03;

    public static Quat TMP_QUAT_01;
    public static Quat TMP_QUAT_02;

    public static Mat44 TMP_MAT44_01;
    public static Mat44 TMP_MAT44_02;

    private JoltGdx() {};

    public static void mat44_to_matrix4(Mat44 in, Matrix4 out) {
        float[] outArray = out.val;
        // Column-major order: copy each column directly
        for (int col = 0; col < 4; col++) {
            Vec4 vec4 = in.GetColumn4(col);
            for (int row = 0; row < 4; row++) {
                float val = vec4.GetComponent(row);
                outArray[col * 4 + row] = val; // m[col][row]
            }
        }
    }

    public static void matrix4_to_mat44(Matrix4 in, Mat44 out) {
        float[] outArray = in.val; // Source Matrix4 data
        for (int col = 0; col < 4; col++) {
            // Extract column elements and create Vec4
            Vec4 vec4 = out.GetColumn4(col);
            vec4.Set(
                outArray[col * 4 + 0], // Row 0
                outArray[col * 4 + 1], // Row 1
                outArray[col * 4 + 2], // Row 2
                outArray[col * 4 + 3]  // Row 3
            );
            out.SetColumn4(col, vec4); // Set column in destination Mat44
        }
    }

    public static void vec3_to_vector3(Vec3 in, Vector3 out) {
        out.set(in.GetX(), in.GetY(), in.GetZ());
    }

    public static void vector3_to_vec3(Vector3 in, Vec3 out) {
        out.Set(in.x, in.y, in.z);
    }

    public static void vec4_to_vector3(Vec4 in, Vector4 out) {
        out.set(in.GetX(), in.GetY(), in.GetZ(), in.GetW());
    }

    public static void vector4_to_vec4(Vector4 in, Vec4 out) {
        out.Set(in.x, in.y, in.z, in.w);
    }

    public static void quat_to_Quaternion(Quat in, Quaternion out) {
        out.set(in.GetX(), in.GetY(), in.GetZ(), in.GetW());
    }

    public static void quaternion_to_quat(Quaternion in, Quat out) {
        out.Set(in.x, in.y, in.z, in.w);
    }

    public static void init() {
        TMP_VEC3_01 = JoltNew.Vec3();
        TMP_VEC3_02 = JoltNew.Vec3();
        TMP_VEC3_03 = JoltNew.Vec3();
        TMP_VEC4_01 = JoltNew.Vec4();
        TMP_VEC4_02 = JoltNew.Vec4();
        TMP_VEC4_03 = JoltNew.Vec4();
        TMP_QUAT_01 = JoltNew.Quat();
        TMP_QUAT_02 = JoltNew.Quat();
        TMP_MAT44_01 = JoltNew.Mat44();
        TMP_MAT44_02 = JoltNew.Mat44();
    }

    public static void dispose() {
        TMP_VEC3_01.dispose();
        TMP_VEC3_02.dispose();
        TMP_VEC3_03.dispose();
        TMP_VEC4_01.dispose();
        TMP_VEC4_02.dispose();
        TMP_VEC4_03.dispose();
        TMP_QUAT_01.dispose();
        TMP_QUAT_02.dispose();
        TMP_MAT44_01.dispose();
        TMP_MAT44_02.dispose();
    }
}