package fi.tamk.tiko.kivimiesgaming;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * @author Atte-Petteri Ronkanen, Risto Pulkkinen
 *
 * Abstract class which all the games screens implement
 */

public abstract class MyScreen implements Screen {

    /**
     * Reference to instance of the main class of the game
     */
    protected Vescape game;

    /**
     * Stage of the screen
     */
    protected Stage stage;

    /**
     * Camera of the screen
     */
    protected OrthographicCamera cam;

    /**
     * Asset manager that is used fot loading assets
     */
    protected AssetManager assetManager;

    /**
     * Next screen where transition happens to after loading its assets
     */
    protected MyScreen nextScreen = null;

    /**
     * Constructor of the class
     *
     * @param game Reference to main class of the game
     * @param assetManager Asset manager which is used for loading assets
     */
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

    /**
     * Is called on every frame. Updates logic and draws the stage.
     *
     * @param dt Delta time between last and current frame
     */
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

    /**
     * Called when transitions happens to this screen.
     */
    public void transitionTo() {
        ScreenDarkener sD = new ScreenDarkener(assetManager.get("black.png", Texture.class), true);
        sD.enable(false, true);
        stage.addActor(sD);
    }

    /**
     * Create and return top Text button in current screens burger menu
     *
     * @return Upper text button in burger menu
     */
    public abstract TextButton getPanelButton1();

    /**
     * Create and return bottom Text button in current screens burger menu
     *
     * @return Lower text button in burger menu
     */
    public abstract TextButton getPanelButton2();

    /**
     * Is called when this screens assets are loaded and this is set to current screen
     */
    public abstract void onStart();

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

    /**
     * Disposes stage
     */
    @Override
    public void dispose() {
        stage.dispose();
    }

    /**
     * Getter for stage
     *
     * @return stage
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * Getter for game
     *
     * @return game
     */
    public Vescape getGame() {
        return game;
    }

    /**
     * Getter for assetManager
     *
     * @return assetManager
     */
    public AssetManager getAssetManager() {
        return assetManager;
    }

    /**
     * Is called every frame. Used for updating logic and catching player input
     *
     * @param dt
     */
    protected void update(float dt) {

    }

    /**
     * Start transitioning to next screen
     *
     * @param nextScreen Screen where to transitions happens
     */
    protected void setNextScreen(MyScreen nextScreen) {
        ScreenDarkener sD = new ScreenDarkener(assetManager.get("black.png", Texture.class), true);
        ImageActor loading = new ImageActor(assetManager.get("loading.png", Texture.class), 150);
        loading.setPosition(Vescape.GUI_VIEWPORT_WIDTH / 2 - loading.getSizeX() / 2,
                Vescape.GUI_VIEWPORT_HEIGHT / 2 - loading.getSizeY() / 2);

        loading.addAction(Actions.forever(
                Actions.rotateBy(360, 1)
        ));
        stage.addActor(sD);
        stage.addActor(loading);
        this.nextScreen = nextScreen;
    }

}
