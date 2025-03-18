package jolt.example.samples.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.utils.ScreenUtils;
import imgui.ImDrawData;
import imgui.ImGui;
import imgui.ImGuiCol;
import imgui.ImGuiConfigFlags;
import imgui.ImGuiIO;
import imgui.ImGuiStyle;
import imgui.gdx.ImGuiGdxImpl;
import imgui.gdx.ImGuiGdxInput;
import imgui.gdx.ImGuiGdxInputMultiplexer;
import jolt.example.samples.app.tests.vehicle.VehicleConstraintTest;

public class GameScreen extends ScreenAdapter {

    private SamplesApp samplesApp;
    private FPSLogger fpsLogger;

    private ImGuiGdxImpl impl;
    private ImGuiGdxInput input;
    private InputMultiplexer inputMultiplexer;

    @Override
    public void show() {
        inputMultiplexer = new InputMultiplexer();
        samplesApp = new SamplesApp();
        fpsLogger = new FPSLogger();

        ImGui.CreateContext();
        ImGuiIO io = ImGui.GetIO();
        io.set_ConfigFlags(ImGuiConfigFlags.ImGuiConfigFlags_DockingEnable);
        input = new ImGuiGdxInput();
        impl = new ImGuiGdxImpl();
        Gdx.input.setInputProcessor(inputMultiplexer);
        inputMultiplexer.addProcessor(input);
        samplesApp.setup(inputMultiplexer);
        samplesApp.startTest(VehicleConstraintTest.class);

        ImGuiStyle imGuiStyle = ImGui.GetStyle();
        imGuiStyle.Colors(ImGuiCol.ImGuiCol_WindowBg, 0.00f, 0.00f, 0.00f, 0.6f);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.1f, 0.1f, 0.8f, 1, true);
        samplesApp.render(delta);
        fpsLogger.log();
        impl.newFrame();
        samplesApp.renderUI();
        ImGui.Render();
        ImDrawData drawData = ImGui.GetDrawData();
        impl.render(drawData);
    }

    @Override
    public void dispose() {
        impl.dispose();
        samplesApp.dispose();
    }
}