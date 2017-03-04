package fi.tamk.tiko.kivimiesgaming;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.Locale;

public class Vescape extends Game {

    public static final float GUI_VIEWPORT_WIDTH = 900;
    public static final float GUI_VIEWPORT_HEIGHT = 1600;

    private SpriteBatch batch;
    private Skin skin;
    private I18NBundle myBundle;

    @Override
    public void create() {
        batch = new SpriteBatch();
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        setFinnish();

        setScreen(new MainMenu(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void setScreen(Screen screen) {
        Screen temp = getScreen();
        super.setScreen(screen);
        if (temp != null) {
            temp.dispose();
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public Skin getSkin() {
        return skin;
    }

    public I18NBundle getMyBundle() {
        return myBundle;
    }

    public void setFinnish() {
        setLocale(new Locale("fi", "FI"));
    }

    public void setEnglish() {
        setLocale(null);
    }

    private void setLocale(Locale locale) {
        if (locale != null) {
            myBundle = I18NBundle.createBundle(Gdx.files.internal("MyBundle"), locale);
        } else {
            myBundle = I18NBundle.createBundle(Gdx.files.internal("MyBundle"));
        }

    }
}
