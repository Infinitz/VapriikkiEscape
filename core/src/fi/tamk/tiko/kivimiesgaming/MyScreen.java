package fi.tamk.tiko.kivimiesgaming;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * Created by atter on 04-Mar-17.
 */

public class MyScreen implements Screen {

    protected Vescape game;
    protected Stage stage;
    protected OrthographicCamera cam;

    public MyScreen(Vescape game) {
        this.game = game;
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

    protected void update(float dt) {

    }

    @Override
    public void render(float dt) {

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

    public Stage getStage() {
        return stage;
    }

    public Vescape getGame() {
        return game;
    }
}
