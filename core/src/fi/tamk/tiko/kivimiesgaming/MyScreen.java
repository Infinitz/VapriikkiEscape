package fi.tamk.tiko.kivimiesgaming;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * Created by atter on 04-Mar-17.
 */

public abstract class MyScreen implements Screen {

    protected Vescape game;
    protected Stage stage;
    protected OrthographicCamera cam;
    protected AssetManager assetManager;

    protected MyScreen nextScreen = null;

    public MyScreen(Vescape game, AssetManager assetManager) {
        this.game = game;
        this.assetManager = assetManager;
        cam = new OrthographicCamera();
        cam.setToOrtho(false, Vescape.GUI_VIEWPORT_WIDTH, Vescape.GUI_VIEWPORT_HEIGHT);
        stage = new Stage(
                new FitViewport(Vescape.GUI_VIEWPORT_WIDTH, Vescape.GUI_VIEWPORT_HEIGHT, cam),
                game.getBatch());
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float dt) {

        if (nextScreen != null) {
            if (assetManager.update()) {
                game.setScreen(nextScreen);
                nextScreen.transitionTo();
                nextScreen = null;
            }
        }

        update(dt);
        stage.act(dt);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        cam.update();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public abstract void onStart();

    public Stage getStage() {
        return stage;
    }

    public Vescape getGame() {
        return game;
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public abstract TextButton getPanelButton1();

    public abstract TextButton getPanelButton2();

    protected void update(float dt) {

    }

    protected void setNextScreen(MyScreen nextScreen) {
        ScreenDarkener sD = new ScreenDarkener(assetManager.get("black.png", Texture.class), true);
        ImageActor loading = new ImageActor(assetManager.get("loading.png", Texture.class), 150);
        loading.setPosition(Vescape.GUI_VIEWPORT_WIDTH / 2 - loading.getSizeX() / 2,
                Vescape.GUI_VIEWPORT_HEIGHT / 4);

        loading.addAction(Actions.forever(
                Actions.rotateBy(360, 1)
        ));
        stage.addActor(sD);
        stage.addActor(loading);
        this.nextScreen = nextScreen;
    }

    public void transitionTo() {
        ScreenDarkener sD = new ScreenDarkener(assetManager.get("black.png", Texture.class), true);
        sD.enable(false, true);
        stage.addActor(sD);
    }
}
