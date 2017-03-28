package fi.tamk.tiko.kivimiesgaming;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
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
    private static final String RIDDLE_END = ";";
    private static final String RIDDLE_FILE_COMMENT_MARK = "#";
    private static final String RIDDLE_FILE_NAME = "riddles.txt";

    private SpriteBatch batch;
    private I18NBundle myBundle;
    private BitmapFont buttonFont;
    private BitmapFont fontBig;
    private TextButton.TextButtonStyle textButtonStyle;
    private TextField.TextFieldStyle textFieldStyle;
    private HashMap<RoomType, RoomData> roomData;

    private HashMap<RoomType, ArrayList<Riddle>> riddles;

    @Override
    public void create() {
        batch = new SpriteBatch();
        Gdx.input.setCatchBackKey(true);
        createStylesAndFonts();
        loadRoomData();
        loadRiddles();

        for (RoomType key : roomData.keySet()) {
            System.out.println(key.toString());
            for (int i = 0; i < roomData.get(key).riddles.size(); ++i) {
                System.out.println(roomData.get(key).riddles.get(i).toString());
            }
        }

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

    public TextField.TextFieldStyle getTextFieldStyle() {
        return textFieldStyle;
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
        for (Actor a : g.getChildren()) {
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

    private void createStylesAndFonts() {
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

        TextureRegionDrawable textFieldBG = new TextureRegionDrawable(
                new TextureRegion(
                        new Texture("riddle_info_box_fill.png")));

        textFieldStyle = new TextField.TextFieldStyle(fontBig, Color.BLACK,
                null, null,
                textFieldBG);
    }

    private void loadRoomData() {
        roomData = new HashMap<RoomType, RoomData>();

        RoomData temp;

        temp = new RoomData(RoomType.ROCK,
                new Texture("F1_rock_active.png"),
                new Texture("F1_rock_active.png"),
                new Texture("map_icons_kivi.png"),
                new Texture("MENU_bg.png"));
        temp.setIconLocalPosition(0.6f, 0.6f);
        roomData.put(RoomType.ROCK, temp);

        temp = new RoomData(RoomType.TAMMER,
                new Texture("F1_tammer_active.png"),
                new Texture("F1_tammer_active.png"),
                new Texture("map_icons_tammerkoski.png"),
                new Texture("MENU_bg.png"));
        temp.setIconLocalPosition(-0.5f, 0.1f);
        roomData.put(RoomType.TAMMER, temp);

        temp = new RoomData(RoomType.POSTAL,
                new Texture("F1_postal_active.png"),
                new Texture("F1_postal_active.png"),
                new Texture("map_icons_postal.png"),
                new Texture("MENU_bg.png"));
        temp.setIconLocalPosition(0.5f, 0.3f);
        roomData.put(RoomType.POSTAL, temp);

        temp = new RoomData(RoomType.TUTORIAL,
                new Texture("F1_tutorial_active.png"),
                new Texture("F1_tutorial_active.png"),
                new Texture("map_icons_tutorial.png"),
                new Texture("MENU_bg.png"));
        temp.setIconLocalPosition(0.3f, 0.15f);
        roomData.put(RoomType.TUTORIAL, temp);

        temp = new RoomData(RoomType.GAME,
                new Texture("F2_game_active.png"),
                new Texture("F2_game_active.png"),
                new Texture("map_icons_game.png"),
                new Texture("MENU_bg.png"));
        temp.setIconLocalPosition(0.4f, -0.2f);
        roomData.put(RoomType.GAME, temp);

        temp = new RoomData(RoomType.ICEHOCKEY,
                new Texture("F2_icehokey_active.png"),
                new Texture("F2_icehokey_active.png"),
                new Texture("map_icons_icehockey.png"),
                new Texture("MENU_bg.png"));
        temp.setIconLocalPosition(-0.4f, 0.1f);
        roomData.put(RoomType.ICEHOCKEY, temp);

        temp = new RoomData(RoomType.MEDIA,
                new Texture("F2_media_active.png"),
                new Texture("F2_media_active.png"),
                new Texture("map_icons_media.png"),
                new Texture("MENU_bg.png"));
        temp.setIconLocalPosition(0.3f, 0.3f);
        roomData.put(RoomType.MEDIA, temp);

        temp = new RoomData(RoomType.DOLL,
                new Texture("F2_doll_active.png"),
                new Texture("F2_doll_active.png"),
                new Texture("map_icons_doll.png"),
                new Texture("MENU_bg.png"));
        temp.setIconLocalPosition(-0.4f, 0.4f);
        roomData.put(RoomType.DOLL, temp);

        temp = new RoomData(RoomType.NATURE,
                new Texture("F2_nature_active.png"),
                new Texture("F2_nature_active.png"),
                new Texture("map_icons_nature.png"),
                new Texture("MENU_bg.png"));
        temp.setIconLocalPosition(0.15f, 0.3f);
        roomData.put(RoomType.NATURE, temp);
    }

    private void loadRiddles() {

        System.out.println(Gdx.files.internal(RIDDLE_FILE_NAME).exists());

        try {
            BufferedReader reader = new BufferedReader(new FileReader(RIDDLE_FILE_NAME));

            RoomType currentRoom = null;
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {

                String currentLine = line.trim();
                if (currentLine.length() == 0) {
                    continue;
                }
                if (RIDDLE_FILE_COMMENT_MARK.compareTo(
                        currentLine.substring(0, RIDDLE_FILE_COMMENT_MARK.length())) == 0) {
                    continue;
                }

                if (ROOM_DATA_MARK.compareTo(
                        currentLine.substring(0, ROOM_DATA_MARK.length())) == 0) {

                    currentRoom = RoomType.typeFromString(
                            currentLine.substring(ROOM_DATA_MARK.length()));
                    continue;
                }

                if (currentLine.length() > 0) {
                    Riddle riddle = new Riddle(currentLine);

                    while (true) {
                        currentLine = reader.readLine().trim();

                        boolean riddleEnd = currentLine.endsWith(RIDDLE_END);
                        if (riddleEnd) {
                            currentLine = currentLine.substring(0, currentLine.length() - 1);
                        }
                        String[] temp = currentLine.split(RIDDLE_SEPARATOR);

                        riddle.addRiddleText(new RiddleTexts(temp[0], temp[1], temp[2]));
                        if (riddleEnd) {
                            roomData.get(currentRoom).riddles.add(riddle);
                            break;
                        }
                    }

                }
            }


            reader.close();
        } catch (Exception e) {
            System.out.println("Unable to load file: " + RIDDLE_FILE_NAME);
        }

    }
}
