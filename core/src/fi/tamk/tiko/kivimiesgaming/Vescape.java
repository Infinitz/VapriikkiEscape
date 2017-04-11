package fi.tamk.tiko.kivimiesgaming;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.I18NBundle;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Locale;

public class Vescape extends Game {

    public static final float GUI_VIEWPORT_WIDTH = 900;
    public static final float GUI_VIEWPORT_HEIGHT = 1600;
    public static final int MAX_CHARS_PER_LINE = 36;
    public static final int MAX_CHARS_IN_ANSWER = 20;
    public static final int TOTAL_RIDDLES_ROOM = 5;
    public static final float HINT_PENALTY = 0.33f;

    public static final String ROOM_DATA_MARK = "&";
    public static final String RIDDLE_ANSWER_SEPARATOR = "//";
    public static final String RIDDLE_SEPARATOR = "::";
    public static final String RIDDLE_END = ";";
    public static final String RIDDLE_FILE_COMMENT_MARK = "#";
    public static final String RIDDLE_FILE_PATH = "data/riddles.txt";
    public static final String RIDDLE_IMAGES_PATH = "riddle_images/";

    private Preferences settingsPref;
    private Preferences scoresPref;
    private AssetManager assetManager;
    private SpriteBatch batch;
    private I18NBundle myBundle;
    private BitmapFont buttonFont;
    private BitmapFont fontBig;
    private BitmapFont riddleFont;
    private BitmapFont answerFieldFont;
    private TextButton.TextButtonStyle textButtonStyle;
    private TextField.TextFieldStyle textFieldStyle;
    private HashMap<RoomType, RoomData> roomData;



    private boolean initialAssetsLoaded = false;

    @Override
    public void create() {
        batch = new SpriteBatch();
        assetManager = new AssetManager();
        new AudioManager(assetManager);
        settingsPref = Gdx.app.getPreferences("settings");
        scoresPref = Gdx.app.getPreferences("scores");
        Gdx.input.setCatchBackKey(true);

        if (settingsPref.getString("language", "finnish").equalsIgnoreCase("finnish")) {
            setFinnish();
        } else if (settingsPref.getString("language").equalsIgnoreCase("english")) {
            setEnglish();
        }
        loadGlobalAssets();
    }

    @Override
    public void render() {
        if (!initialAssetsLoaded) {
            if (assetManager.update()) {
                initialAssetsLoaded = true;
                createStylesAndFonts();
                loadRoomData();
                loadRiddles();
                loadScores();
                AudioManager.playMusic("music_bg.mp3");
                AudioManager.enableMusic(settingsPref.getBoolean("musicEnabled", true));
                AudioManager.enableSounds(settingsPref.getBoolean("soundEnabled", true));
                setScreen(new MainMenu(this, assetManager));
            }
        }
        super.render();
    }

    @Override
    public void setScreen(Screen screen) {
        Screen temp = getScreen();
        if (temp != null) {
            temp.dispose();
        }
        super.setScreen(screen);
        ((MyScreen)screen).onStart();
    }

    @Override
    public void dispose() {

        if (getScreen() != null) {
            getScreen().dispose();
        }
        batch.dispose();
        assetManager.dispose();
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

    public BitmapFont getRiddleFont() {
        return riddleFont;
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

    public void saveSettings() {
        settingsPref.putBoolean("musicEnabled", AudioManager.isMusicEnabled());
        settingsPref.putBoolean("soundEnabled", AudioManager.isSoundsEnabled());
        settingsPref.putString("language", myBundle.get("language"));
        settingsPref.flush();
    }

    public void saveScores() {
        String scores = "";
        for (RoomType key : roomData.keySet()) {
            scores += key.toString() + ":" + roomData.get(key).highscore + ";";
        }
        scoresPref.putString("scores", scores);
        scoresPref.flush();
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

    private void loadGlobalAssets() {
        assetManager.load("MENU_bg.jpg", Texture.class);
        assetManager.load("MENU_button.png", Texture.class);
        assetManager.load("MENU_button_pressed.png", Texture.class);
        assetManager.load("MENU_flag_fi.png", Texture.class);
        assetManager.load("MENU_flag_en.png", Texture.class);
        assetManager.load("MENU_sound_on.png", Texture.class);
        assetManager.load("MENU_sound_off.png", Texture.class);
        assetManager.load("MENU_music_on.png", Texture.class);
        assetManager.load("MENU_music_off.png", Texture.class);
        assetManager.load("riddle_info_box_fill.png", Texture.class);
        assetManager.load("star_empty.png", Texture.class);
        assetManager.load("star_full.png", Texture.class);
        assetManager.load("black.png", Texture.class);
        assetManager.load("map_room_info_box.png", Texture.class);
        assetManager.load("indicator_line.jpg", Texture.class);

        assetManager.load("menurger.png", Texture.class);
        assetManager.load("menurger_pressed.png", Texture.class);


        assetManager.load("music_bg.mp3", Music.class);

        assetManager.load("button_press.wav", Sound.class);
        assetManager.load("button_toggle.wav", Sound.class);
        assetManager.load("panel_open.wav", Sound.class);
        assetManager.load("panel_close.wav", Sound.class);

    }

    private void createStylesAndFonts() {
        FreeTypeFontGenerator fontGen = new FreeTypeFontGenerator(Gdx.files.internal("tahoma.ttf"));

        FreeTypeFontGenerator.FreeTypeFontParameter parameter =
                new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = 70;
        parameter.shadowOffsetX = 5;

        buttonFont = fontGen.generateFont(parameter);

        parameter.size = 86;
        parameter.shadowOffsetX = 5;
        fontBig = fontGen.generateFont(parameter);

        parameter.size = 50;
        parameter.shadowOffsetX = 1;
        riddleFont = fontGen.generateFont(parameter);

        parameter.size = 80;
        parameter.shadowOffsetX = 1;
        answerFieldFont = fontGen.generateFont(parameter);

        TextureRegionDrawable buttonImage = new TextureRegionDrawable(
                new TextureRegion(assetManager.get("MENU_button.png", Texture.class)));

        TextureRegionDrawable buttonPressedImage = new TextureRegionDrawable(
                new TextureRegion(assetManager.get("MENU_button_pressed.png", Texture.class)));

        textButtonStyle = new TextButton.TextButtonStyle(buttonImage, buttonPressedImage,
                buttonImage, getButtonFont());

        TextureRegionDrawable textFieldCursor = new TextureRegionDrawable(
                new TextureRegion(assetManager.get("indicator_line.jpg", Texture.class)));


        textFieldStyle = new TextField.TextFieldStyle(answerFieldFont, Color.BLACK,
                textFieldCursor, null,
                null);
    }

    private void loadRoomData() {
        roomData = new HashMap<RoomType, RoomData>();

        RoomData temp;

        temp = new RoomData(RoomType.ROCK,
                "F1_rock_active.png",
                "F1_rock_active.png",
                "map_icons_kivi.png",
                "bg_kivi.jpg",
                assetManager);
        temp.setIconLocalPosition(0.6f, 0.6f);
        roomData.put(RoomType.ROCK, temp);

        temp = new RoomData(RoomType.TAMMER,
                "F1_tammer_active.png",
                "F1_tammer_active.png",
                "map_icons_tammerkoski.png",
                "bg_tammer.jpg",
                assetManager);
        temp.setIconLocalPosition(-0.5f, 0.1f);
        roomData.put(RoomType.TAMMER, temp);

        temp = new RoomData(RoomType.POSTAL,
                "F1_postal_active.png",
                "F1_postal_active.png",
                "map_icons_postal.png",
                "bg_postal.jpg",
                assetManager);
        temp.setIconLocalPosition(0.5f, 0.3f);
        roomData.put(RoomType.POSTAL, temp);

        temp = new RoomData(RoomType.TUTORIAL,
                "F1_tutorial_active.png",
                "F1_tutorial_active.png",
                "map_icons_tutorial.png",
                "bg_tutorial.jpg",
                assetManager);
        temp.setIconLocalPosition(0.3f, 0.15f);
        roomData.put(RoomType.TUTORIAL, temp);

        temp = new RoomData(RoomType.GAME,
                "F2_game_active.png",
                "F2_game_active.png",
                "map_icons_game.png",
                "MENU_bg.jpg",
                assetManager);
        temp.setIconLocalPosition(0.4f, -0.2f);
        roomData.put(RoomType.GAME, temp);

        temp = new RoomData(RoomType.ICEHOCKEY,
                "F2_icehokey_active.png",
                "F2_icehokey_active.png",
                "map_icons_icehockey.png",
                "MENU_bg.jpg",
                assetManager);
        temp.setIconLocalPosition(-0.4f, 0.1f);
        roomData.put(RoomType.ICEHOCKEY, temp);

        temp = new RoomData(RoomType.MEDIA,
                "F2_media_active.png",
                "F2_media_active.png",
                "map_icons_media.png",
                "MENU_bg.jpg",
                        assetManager);
        temp.setIconLocalPosition(0.3f, 0.3f);
        roomData.put(RoomType.MEDIA, temp);

        temp = new RoomData(RoomType.DOLL,
                "F2_doll_active.png",
                "F2_doll_active.png",
                "map_icons_doll.png",
                "MENU_bg.jpg",
                        assetManager);
        temp.setIconLocalPosition(-0.4f, 0.4f);
        roomData.put(RoomType.DOLL, temp);

        temp = new RoomData(RoomType.NATURE,
                "F2_nature_active.png",
                "F2_nature_active.png",
                "map_icons_nature.png",
                "bg_nature.jpg",
                        assetManager);
        temp.setIconLocalPosition(0.15f, 0.3f);
        roomData.put(RoomType.NATURE, temp);
    }

    private void loadRiddles() {

        int lineIndex = 0;
        String currentLine = "";
        try {
            FileHandle handle = Gdx.files.internal(RIDDLE_FILE_PATH);
            String text = handle.readString();
            InputStream is = new ByteArrayInputStream(text.getBytes());

            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            RoomType currentRoom = null;
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                ++lineIndex;
                currentLine = line.trim();
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
                        ++lineIndex;
                        currentLine = reader.readLine().trim();
                        boolean riddleEnd = currentLine.endsWith(RIDDLE_END);
                        if (riddleEnd) {
                            currentLine = currentLine.substring(0, currentLine.length() - 1);
                        }
                        String[] temp = currentLine.split(RIDDLE_SEPARATOR);
                        riddle.addRiddleText(new RiddleTexts(temp[0], temp[1], temp[2], temp[3]));
                        if (riddleEnd) {
                            roomData.get(currentRoom).riddles.add(riddle);
                            break;
                        }
                    }

                }
            }


            reader.close();
        } catch (Exception e) {
            System.out.println("Unable to load file: " + RIDDLE_FILE_PATH + "\n" +
            "Error in the line: " + lineIndex);
            System.out.println(currentLine);
        }

    }

    private void loadScores() {
        String scores = scoresPref.getString("scores", "");
        if (scores.length() == 0) {
            return;
        }

        String[] scoreArray = scores.split(";");
        for (int i = 0; i < scoreArray.length; ++i) {
            String[] temp = scoreArray[i].split(":");
            String name = temp[0];
            int score = Integer.parseInt(temp[1]);
            roomData.get(RoomType.typeFromString(name)).highscore = score;
        }

    }
}
