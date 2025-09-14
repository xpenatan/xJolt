package jolt.gdx;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.utils.Disposable;

public interface ModelBatchExt extends Disposable {
    void begin(final Camera cam);
    void render(final RenderableProvider renderableProvider, final Environment environment);
    void end();
}