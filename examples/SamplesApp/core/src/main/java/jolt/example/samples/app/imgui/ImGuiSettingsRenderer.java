package jolt.example.samples.app.imgui;

import com.badlogic.gdx.utils.Array;
import imgui.ImGui;
import imgui.idl.helper.IDLBool;
import jolt.BodyManagerDrawSettings;
import jolt.EShapeColor;
import jolt.ESoftBodyConstraintColor;
import jolt.example.samples.app.jolt.JoltInstance;

public class ImGuiSettingsRenderer {

    private IDLBool idlBool;

    private int mDrawSoftBodyConstraintColor;
    private int mDrawShapeColor;
    private Array<IntStringPair> mDrawSoftBodyConstraintColorArray;
    private Array<IntStringPair> mDrawShapeColorArray;

    public ImGuiSettingsRenderer() {
        idlBool = new IDLBool();
        mDrawSoftBodyConstraintColorArray = new Array<>();
        mDrawSoftBodyConstraintColorArray.add(new IntStringPair("ConstraintType", ESoftBodyConstraintColor.ESoftBodyConstraintColor_ConstraintType));
        mDrawSoftBodyConstraintColorArray.add(new IntStringPair("ConstraintGroup", ESoftBodyConstraintColor.ESoftBodyConstraintColor_ConstraintGroup));
        mDrawSoftBodyConstraintColorArray.add(new IntStringPair("ConstraintOrder", ESoftBodyConstraintColor.ESoftBodyConstraintColor_ConstraintOrder));
        mDrawShapeColorArray = new Array<>();
        mDrawShapeColorArray.add(new IntStringPair("InstanceColor", EShapeColor.EShapeColor_InstanceColor));
        mDrawShapeColorArray.add(new IntStringPair("ShapeTypeColor", EShapeColor.EShapeColor_ShapeTypeColor));
        mDrawShapeColorArray.add(new IntStringPair("MotionTypeColor", EShapeColor.EShapeColor_MotionTypeColor));
        mDrawShapeColorArray.add(new IntStringPair("SleepColor", EShapeColor.EShapeColor_SleepColor));
        mDrawShapeColorArray.add(new IntStringPair("IslandColor", EShapeColor.EShapeColor_IslandColor));
        mDrawShapeColorArray.add(new IntStringPair("MaterialColor", EShapeColor.EShapeColor_MaterialColor));
    }

    public void dispose() {
        idlBool.dispose();
    }

    public void render(BodyManagerDrawSettings settings) {
        idlBool.set(settings.get_mDrawShape());
        if(ImGui.Checkbox("DrawShape", idlBool)) {
            settings.set_mDrawShape(idlBool.getValue());
        }
        idlBool.set(settings.get_mDrawShapeWireframe());
        if(ImGui.Checkbox("DrawShapeWireframe", idlBool)) {
            settings.set_mDrawShapeWireframe(idlBool.getValue());
        }
        mDrawShapeColor = settings.get_mDrawShapeColor();
        IntStringPair colorPair = mDrawShapeColorArray.get(mDrawShapeColor);
        if (ImGui.BeginCombo("##mDrawShapeColor", colorPair.name)) {
            for(int i = 0; i < mDrawShapeColorArray.size; i++) {
                IntStringPair item = mDrawShapeColorArray.get(i);
                boolean selected = i == this.mDrawShapeColor;
                if(ImGui.Selectable(item.name, selected)) {
                    this.mDrawShapeColor = i;
                    settings.set_mDrawShapeColor(mDrawShapeColor);
                }
            }
            ImGui.EndCombo();
        }
        idlBool.set(settings.get_mDrawGetSupportFunction());
        if(ImGui.Checkbox("DrawGetSupportFunction", idlBool)) {
            settings.set_mDrawGetSupportFunction(idlBool.getValue());
        }
        idlBool.set(settings.get_mDrawSupportDirection());
        if(ImGui.Checkbox("DrawSupportDirection", idlBool)) {
            settings.set_mDrawSupportDirection(idlBool.getValue());
        }
        idlBool.set(settings.get_mDrawGetSupportingFace());
        if(ImGui.Checkbox("DrawGetSupportingFace", idlBool)) {
            settings.set_mDrawGetSupportingFace(idlBool.getValue());
        }
        idlBool.set(settings.get_mDrawBoundingBox());
        if(ImGui.Checkbox("DrawBoundingBox", idlBool)) {
            settings.set_mDrawBoundingBox(idlBool.getValue());
        }
        idlBool.set(settings.get_mDrawCenterOfMassTransform());
        if(ImGui.Checkbox("DrawCenterOfMassTransform", idlBool)) {
            settings.set_mDrawCenterOfMassTransform(idlBool.getValue());
        }
        idlBool.set(settings.get_mDrawWorldTransform());
        if(ImGui.Checkbox("DrawWorldTransform", idlBool)) {
            settings.set_mDrawWorldTransform(idlBool.getValue());
        }
        idlBool.set(settings.get_mDrawVelocity());
        if(ImGui.Checkbox("mDrawVelocity", idlBool)) {
            settings.set_mDrawVelocity(idlBool.getValue());
        }
        idlBool.set(settings.get_mDrawMassAndInertia());
        if(ImGui.Checkbox("DrawMassAndInertia", idlBool)) {
            settings.set_mDrawMassAndInertia(idlBool.getValue());
        }
        idlBool.set(settings.get_mDrawSleepStats());
        if(ImGui.Checkbox("DrawSleepStats", idlBool)) {
            settings.set_mDrawSleepStats(idlBool.getValue());
        }
        idlBool.set(settings.get_mDrawSoftBodyVertices());
        if(ImGui.Checkbox("DrawSoftBodyVertices", idlBool)) {
            settings.set_mDrawSoftBodyVertices(idlBool.getValue());
        }
        idlBool.set(settings.get_mDrawSoftBodyVertexVelocities());
        if(ImGui.Checkbox("DrawSoftBodyVertexVelocities", idlBool)) {
            settings.set_mDrawSoftBodyVertexVelocities(idlBool.getValue());
        }
        idlBool.set(settings.get_mDrawSoftBodyEdgeConstraints());
        if(ImGui.Checkbox("DrawSoftBodyEdgeConstraints", idlBool)) {
            settings.set_mDrawSoftBodyEdgeConstraints(idlBool.getValue());
        }
        idlBool.set(settings.get_mDrawSoftBodyBendConstraints());
        if(ImGui.Checkbox("DrawSoftBodyBendConstraints", idlBool)) {
            settings.set_mDrawSoftBodyBendConstraints(idlBool.getValue());
        }
        idlBool.set(settings.get_mDrawSoftBodyVolumeConstraints());
        if(ImGui.Checkbox("DrawSoftBodyVolumeConstraints", idlBool)) {
            settings.set_mDrawSoftBodyVolumeConstraints(idlBool.getValue());
        }
        idlBool.set(settings.get_mDrawSoftBodySkinConstraints());
        if(ImGui.Checkbox("DrawSoftBodySkinConstraints", idlBool)) {
            settings.set_mDrawSoftBodySkinConstraints(idlBool.getValue());
        }
        idlBool.set(settings.get_mDrawSoftBodyLRAConstraints());
        if(ImGui.Checkbox("DrawSoftBodyLRAConstraints", idlBool)) {
            settings.set_mDrawSoftBodyLRAConstraints(idlBool.getValue());
        }
        idlBool.set(settings.get_mDrawSoftBodyPredictedBounds());
        if(ImGui.Checkbox("DrawSoftBodyPredictedBounds", idlBool)) {
            settings.set_mDrawSoftBodyPredictedBounds(idlBool.getValue());
        }
        mDrawSoftBodyConstraintColor = settings.get_mDrawSoftBodyConstraintColor();
        IntStringPair selectedText = mDrawSoftBodyConstraintColorArray.get(mDrawSoftBodyConstraintColor);
        if (ImGui.BeginCombo("##mDrawSoftBodyConstraintColor", selectedText.name)) {
            for(int i = 0; i < mDrawSoftBodyConstraintColorArray.size; i++) {
                IntStringPair item = mDrawSoftBodyConstraintColorArray.get(i);
                boolean selected = i == this.mDrawSoftBodyConstraintColor;
                if(ImGui.Selectable(item.name, selected)) {
                    this.mDrawSoftBodyConstraintColor = i;
                    settings.set_mDrawSoftBodyConstraintColor(mDrawSoftBodyConstraintColor);
                }
            }
            ImGui.EndCombo();
        }
    }

    public void render(JoltInstance instance) {

    }

    private static class IntStringPair {
        public String name;
        public int value;
        public IntStringPair(String name, int value) {
            this.name = name;
            this.value = value;
        }
    }
}
