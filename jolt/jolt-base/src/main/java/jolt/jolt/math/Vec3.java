package jolt.jolt.math;

public class Vec3 {

    public native float GetX();
    public native float GetY();
    public native float GetZ();

    @Override
    public String toString() {
        return "X: " + GetX() + " Y: " + GetY() + " Z: " + GetZ();
    }
}