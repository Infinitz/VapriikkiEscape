package fi.tamk.tiko.kivimiesgaming;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.I18NBundle;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class Vescape extends Game {

    public static final float GUI_VIEWPORT_WIDTH = 900;
    public static final float GUI_VIEWPORT_HEIGHT = 1600;
    private static final String ROOM_DATA_MARK = "&";
    private static final String RIDDLE_SEPARATOR = "::";
    private static final String RIDDLE_FILE_COMMENT_MARK = "#";
    private static final String RIDDLE_FILE_NAME = "riddles.txt";

    private SpriteBatch batch;
    private I18NBundle myBundle;
    private BitmapFont buttonFont;
    private BitmapFont fontBig;
    private TextButton.TextButtonStyle textButtonStyle;
    private HashMap<RoomType, RoomData> roomData;

    private HashMap<RoomType, ArrayList<Riddle>> riddles;

    @Override
    public void create() {
        batch = new SpriteBatch();
        Gdx.input.setCatchBackKey(true);
        createTextButtonStyle();
        loadRoomData();
        loadRiddles();

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
        if (getScreen() != null) {
            getScreen().dispose();
        }
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public BitmapFont getButtonFont() {
        return buttonFont;
    }

    public BitmapFont getFontBig() {
        return fontBig;
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
        setLocale(new Locale("en", "US"));
    }


    public static void setGroupOrigin(Group g, float x, float y) {
        float offsetX = g.getX() - x;
        float offsetY = g.getY() - y;
        g.setPosition(x, y);
        for (Actor a: g.getChildren()) {
            a.setPosition(a.getX() + offsetX, a.getY() + offsetY);
        }
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

        parameter.size = 78;
        parameter.shadowOffsetX = 5;
        fontBig = fontGen.generateFont(parameter);

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

        temp = new RoomData(RoomType.ROCK, new Texture("F1_rock_active.png"),
                new Texture("F1_rock_active.png"), new Texture("englishFlag.png"), 2);
        temp.setIconLocalPosition(0.6f, 0.6f);
        roomData.put(RoomType.ROCK, temp);

        temp = new RoomData(RoomType.TAMMER, new Texture("F1_tammer_active.png"),
                new Texture("F1_tammer_active.png"), new Texture("map_icons_tammerkoski.png"), 2);
        temp.setIconLocalPosition(-0.5f, 0.1f);
        roomData.put(RoomType.TAMMER, temp);

        temp = new RoomData(RoomType.POSTAL, new Texture("F1_postal_active.png"),
                new Texture("F1_postal_active.png"), new Texture("map_icons_postal.png"), 2);
        temp.setIconLocalPosition(0.5f, 0.3f);
        roomData.put(RoomType.POSTAL, temp);

        temp = new RoomData(RoomType.TUTORIAL, new Texture("F1_tutorial_active.png"),
                new Texture("F1_tutorial_active.png"), new Texture("map_icons_tutorial.png"), 2);
        temp.setIconLocalPosition(0.3f, 0.15f);
        roomData.put(RoomType.TUTORIAL, temp);

        temp = new RoomData(RoomType.GAME, new Texture("F2_game_active.png"),
                new Texture("F2_game_active.png"), new Texture("map_icons_game.png"), 2);
        temp.setIconLocalPosition(0.4f, -0.2f);
        roomData.put(RoomType.GAME, temp);

        temp = new RoomData(RoomType.ICEHOCKEY, new Texture("F2_icehokey_active.png"),
                new Texture("F2_icehokey_active.png"), new Texture("map_icons_icehockey.png"), 2);
        temp.setIconLocalPosition(-0.4f, 0.1f);
        roomData.put(RoomType.ICEHOCKEY, temp);

        temp = new RoomData(RoomType.MEDIA, new Texture("F2_media_active.png"),
                new Texture("F2_media_active.png"), new Texture("englishFlag.png"), 2);
        temp.setIconLocalPosition(0.3f, 0.3f);
        roomData.put(RoomType.MEDIA, temp);

        temp = new RoomData(RoomType.DOLL, new Texture("F2_doll_active.png"),
                new Texture("F2_doll_active.png"), new Texture("englishFlag.png"), 2);
        temp.setIconLocalPosition(-0.4f, 0.4f);
        roomData.put(RoomType.DOLL, temp);

        temp = new RoomData(RoomType.NATURE, new Texture("F2_nature_active.png"),
                new Texture("F2_nature_active.png"), new Texture("englishFlag.png"), 2);
        temp.setIconLocalPosition(0.15f, 0.3f);
        roomData.put(RoomType.NATURE, temp);
    }

    private void loadRiddles() {


        try {
            BufferedReader reader = new BufferedReader(new FileReader(RIDDLE_FILE_NAME));

            RoomType currentRoom = null;
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {

                String currentLine = line.trim();
                System.out.println(line + "  line");
                if (currentLine.length() == 0) {
                    continue;
                }
                if (RIDDLE_FILE_COMMENT_MARK.compareTo(
                        currentLine.substring(0, RIDDLE_FILE_COMMENT_MARK.length())) == 0) {
                    System.out.println("COMMENT");
                    continue;
                }

                if (ROOM_DATA_MARK.compareTo(
                        currentLine.substring(0, ROOM_DATA_MARK.length())) == 0) {
                    currentRoom = RoomType.valueOf(currentLine.substring(ROOM_DATA_MARK.length()));
                    System.out.println("NEW ROOM");
                    continue;
                }

                if (currentLine.length() > 0) {
                    //Create riddle
                    System.out.println("CREATE RIDDLE");
                    roomData.get(currentRoom).addRiddle(currentLine);
                }
            }


            reader.close();
        } catch (Exception e) {
            System.out.println("Unable to load file: " + RIDDLE_FILE_NAME);
        }

    }
}
