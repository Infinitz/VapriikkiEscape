package fi.tamk.tiko.kivimiesgaming;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;

public class Vescape extends Game {

    public static final float GUI_VIEWPORT_WIDTH = 900;
    public static final float GUI_VIEWPORT_HEIGHT = 1600;

    private SpriteBatch batch;
    private Skin skin;
    private I18NBundle myBundle;
    private BitmapFont buttonFont;
    private TextButton.TextButtonStyle textButtonStyle;
    private HashMap<RoomType, RoomData> roomData;

    @Override
    public void create() {
        batch = new SpriteBatch();
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        Gdx.input.setCatchBackKey(true);
        createTextButtonStyle();
        loadRoomData();

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

    public BitmapFont getButtonFont() {
        return buttonFont;
    }

    public TextButton.TextButtonStyle getTextButtonStyle() {
        return textButtonStyle;
    }

    public I18NBundle getMyBundle() {
        return myBundle;
    }

    public RoomData getRoomData(RoomType type) {
        return roomData.get(type);
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

    private void createTextButtonStyle() {
        FreeTypeFontGenerator fontGen = new FreeTypeFontGenerator(Gdx.files.internal("tahoma.ttf"));

        FreeTypeFontGenerator.FreeTypeFontParameter parameter =
                new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 70;
        parameter.shadowOffsetX = 5;

        buttonFont = fontGen.generateFont(parameter);

        TextureRegionDrawable buttonImage = new TextureRegionDrawable(
                new TextureRegion(
                        new Texture("MENU_button.png")));

        TextureRegionDrawable buttonPressedImage = new TextureRegionDrawable(
                new TextureRegion(
                        new Texture("MENU_button_pressed.png")));

        textButtonStyle = new TextButton.TextButtonStyle(buttonImage, buttonPressedImage,
                buttonImage, getButtonFont());
    }

    private void loadRoomData() {
        roomData = new HashMap<RoomType, RoomData>();

        RoomData temp;

        temp = new RoomData(RoomType.ROCK, new Texture("F1_rock.png"),
                new Texture("F1_rock_active.png"), 0);
        roomData.put(RoomType.ROCK, temp);

        temp = new RoomData(RoomType.TAMMER, new Texture("F1_tammer.png"),
                new Texture("F1_tammer_active.png"), 0);
        roomData.put(RoomType.TAMMER, temp);

        temp = new RoomData(RoomType.POSTAL, new Texture("F1_postal.png"),
                new Texture("F1_postal_active.png"), 0);
        roomData.put(RoomType.POSTAL, temp);

        temp = new RoomData(RoomType.TUTORIAL, new Texture("F1_tutorial.png"),
                new Texture("F1_tutorial_active.png"), 0);
        roomData.put(RoomType.TUTORIAL, temp);

        temp = new RoomData(RoomType.POSTALUP, new Texture("F1_tammer.png"),
                new Texture("F1_tammer.png"), 0);
        roomData.put(RoomType.POSTALUP, temp);

        temp = new RoomData(RoomType.GAME, new Texture("F1_tammer.png"),
                new Texture("F1_tammer.png"), 0);
        roomData.put(RoomType.GAME, temp);

        temp = new RoomData(RoomType.ICEHOCKEY, new Texture("F1_tammer.png"),
                new Texture("F1_tammer.png"), 0);
        roomData.put(RoomType.ICEHOCKEY, temp);

        temp = new RoomData(RoomType.MEDIA, new Texture("F1_tammer.png"),
                new Texture("F1_tammer.png"), 0);
        roomData.put(RoomType.MEDIA, temp);

        temp = new RoomData(RoomType.DOLL, new Texture("F1_tammer.png"),
                new Texture("F1_tammer.png"), 0);
        roomData.put(RoomType.DOLL, temp);

        temp = new RoomData(RoomType.NATURE, new Texture("F1_tammer.png"),
                new Texture("F1_tammer.png"), 0);
        roomData.put(RoomType.NATURE, temp);
    }
}
