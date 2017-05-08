package fi.tamk.tiko.kivimiesgaming;

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
    public static final int MAX_CHARS_PER_LINE = 29;
    public static final int MAX_CHARS_IN_ANSWER = 21;
    public static final int TOTAL_RIDDLES_ROOM = 5;
    public static final float HINT_PENALTY = 0.33f;

    public static final String ROOM_DATA_MARK = "&";
    public static final String RIDDLE_ANSWER_SEPARATOR = "//";
    public static final String RIDDLE_SEPARATOR = "::";
    public static final String RIDDLE_END = ";";
    public static final String LAST_RIDDLE_END = "@";
    public static final String RIDDLE_FILE_COMMENT_MARK = "#";
    public static final String RIDDLE_FILE_PATH = "data/riddles.txt";
    public static final String RIDDLE_IMAGES_PATH = "riddle_images/";

    public static int lastTotalStars = 0;
    public static boolean storyStartSeen = false;

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
    private MachinePart[] machineParts;


    private boolean initialAssetsLoaded = false;

    @Override
    public void create() {
        batch = new SpriteBatch();
        assetManager = new AssetManager();
        new AudioManager(assetManager);
        settingsPref = Gdx.app.getPreferences("settings");
        scoresPref = Gdx.app.getPreferences("scores");
        Gdx.input.setCatchBackKey(true);

        String language = Locale.getDefault().getLanguage().equalsIgnoreCase("fi")
                ? "finnish" : "english";
        if (settingsPref.getString("language", language).equalsIgnoreCase("finnish")) {
            setFinnish();
        } else if (settingsPref.getString("language", language).equalsIgnoreCase("english")) {
            setEnglish();
        }
        loadInitialAssets();
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
                loadGlobalAssets();
                setScreen(new StartScreen(this, assetManager));
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

    public void resetScores() {
        scoresPref.putString("scores", "");
        scoresPref.putBoolean("storyStartSeen", false);
        scoresPref.flush();

        loadScores();
    }

    public int getMaxStars() {
        return roomData.keySet().size() * 3;
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

    public MachinePart[] getMachineParts() {
        return machineParts;
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
        scoresPref.putBoolean("storyStartSeen", storyStartSeen);

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


        assetManager.load("MENU_flag_fi.png", Texture.class);
        assetManager.load("MENU_flag_en.png", Texture.class);
        assetManager.load("MENU_sound_on.png", Texture.class);
        assetManager.load("MENU_sound_off.png", Texture.class);
        assetManager.load("MENU_music_on.png", Texture.class);
        assetManager.load("MENU_music_off.png", Texture.class);
        assetManager.load("star_empty.png", Texture.class);
        assetManager.load("star_full.png", Texture.class);

        assetManager.load("map_room_info_box.png", Texture.class);


        assetManager.load("menurger.png", Texture.class);
        assetManager.load("menurger_pressed.png", Texture.class);

        assetManager.load("button_press.wav", Sound.class);
        assetManager.load("button_toggle.wav", Sound.class);
        assetManager.load("panel_open.wav", Sound.class);
        assetManager.load("panel_close.wav", Sound.class);

    }

    private void loadInitialAssets() {
        assetManager.load("MENU_bg.jpg", Texture.class);
        assetManager.load("loading.png", Texture.class);
        assetManager.load("music_bg.mp3", Music.class);
        assetManager.load("MENU_button.png", Texture.class);
        assetManager.load("MENU_button_pressed.png", Texture.class);
        assetManager.load("indicator_line.jpg", Texture.class);
        assetManager.load("black.png", Texture.class);
    }
    private void createStylesAndFonts() {
        FreeTypeFontGenerator fontGen =
                new FreeTypeFontGenerator(Gdx.files.internal("fonts/GOTHICBI.TTF"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter =
                new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = 75;
        parameter.shadowOffsetX = 3;
        parameter.borderColor = Color.BLACK;
        fontBig = fontGen.generateFont(parameter);

        fontGen = new FreeTypeFontGenerator(Gdx.files.internal("fonts/GOTHICI.TTF"));
        parameter.size = 65;
        parameter.shadowOffsetX = 0;
        buttonFont = fontGen.generateFont(parameter);


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
        //1, 4, 9, 14, 21
        /* Progress test
        temp = new RoomData(RoomType.ROCK, 1,
                "F1_rock.png",
                "F1_rock_active.png",
                "map_icons_kivi.png",
                "bg_kivi.jpg",
                assetManager);
        temp.setIconLocalPosition(0.6f, 0.6f);
        roomData.put(RoomType.ROCK, temp);

        temp = new RoomData(RoomType.TAMMER, 1,
                "F1_tammer.png",
                "F1_tammer_active.png",
                "map_icons_tammerkoski.png",
                "bg_tammer.jpg",
                assetManager);
        temp.setIconLocalPosition(-0.5f, 0.1f);
        roomData.put(RoomType.TAMMER, temp);

        temp = new RoomData(RoomType.POSTAL, 4,
                "F1_postal.png",
                "F1_postal_active.png",
                "map_icons_postal.png",
                "bg_postal.jpg",
                assetManager);
        temp.setIconLocalPosition(0.5f, 0.3f);
        roomData.put(RoomType.POSTAL, temp);

        temp = new RoomData(RoomType.TUTORIAL, 0,
                "F1_tutorial.png",
                "F1_tutorial_active.png",
                "map_icons_tutorial.png",
                "bg_tutorial.jpg",
                assetManager);
        temp.setIconLocalPosition(0.3f, 0.15f);
        roomData.put(RoomType.TUTORIAL, temp);

        temp = new RoomData(RoomType.GAME, 21,
                "F2_game.png",
                "F2_game_active.png",
                "map_icons_game.png",
                "bg_game.jpg",
                assetManager);
        temp.setIconLocalPosition(0.4f, -0.2f);
        roomData.put(RoomType.GAME, temp);

        temp = new RoomData(RoomType.ICEHOCKEY, 14,
                "F2_icehokey.png",
                "F2_icehokey_active.png",
                "map_icons_icehockey.png",
                "bg_hockey.jpg",
                assetManager);
        temp.setIconLocalPosition(-0.4f, 0.1f);
        roomData.put(RoomType.ICEHOCKEY, temp);

        temp = new RoomData(RoomType.MEDIA, 14,
                "F2_media.png",
                "F2_media_active.png",
                "map_icons_media.png",
                "bg_media.jpg",
                assetManager);
        temp.setIconLocalPosition(0.3f, 0.3f);
        roomData.put(RoomType.MEDIA, temp);

        temp = new RoomData(RoomType.DOLL, 9,
                "F2_doll.png",
                "F2_doll_active.png",
                "map_icons_doll.png",
                "bg_doll.jpg",
                assetManager);
        temp.setIconLocalPosition(-0.4f, 0.4f);
        roomData.put(RoomType.DOLL, temp);

        temp = new RoomData(RoomType.NATURE, 9,
                "F2_nature.png",
                "F2_nature_active.png",
                "map_icons_nature.png",
                "bg_nature.jpg",
                assetManager);
        temp.setIconLocalPosition(0.15f, 0.3f);
        roomData.put(RoomType.NATURE, temp);
        */
        temp = new RoomData(RoomType.ROCK, 1,
                "F1_rock.png",
                "F1_rock_active.png",
                "map_icons_kivi.png",
                "bg_kivi.jpg",
                assetManager);
        temp.setIconLocalPosition(0.6f, 0.6f);
        roomData.put(RoomType.ROCK, temp);

        temp = new RoomData(RoomType.TAMMER, 1,
                "F1_tammer.png",
                "F1_tammer_active.png",
                "map_icons_tammerkoski.png",
                "bg_tammer.jpg",
                assetManager);
        temp.setIconLocalPosition(-0.5f, 0.1f);
        roomData.put(RoomType.TAMMER, temp);

        temp = new RoomData(RoomType.POSTAL, 1,
                "F1_postal.png",
                "F1_postal_active.png",
                "map_icons_postal.png",
                "bg_postal.jpg",
                assetManager);
        temp.setIconLocalPosition(0.5f, 0.3f);
        roomData.put(RoomType.POSTAL, temp);

        temp = new RoomData(RoomType.TUTORIAL, 0,
                "F1_tutorial.png",
                "F1_tutorial_active.png",
                "map_icons_tutorial.png",
                "bg_tutorial.jpg",
                assetManager);
        temp.setIconLocalPosition(0.3f, 0.15f);
        roomData.put(RoomType.TUTORIAL, temp);

        temp = new RoomData(RoomType.GAME, 1,
                "F2_game.png",
                "F2_game_active.png",
                "map_icons_game.png",
                "bg_game.jpg",
                assetManager);
        temp.setIconLocalPosition(0.4f, -0.2f);
        roomData.put(RoomType.GAME, temp);

        temp = new RoomData(RoomType.ICEHOCKEY, 1,
                "F2_icehokey.png",
                "F2_icehokey_active.png",
                "map_icons_icehockey.png",
                "bg_hockey.jpg",
                assetManager);
        temp.setIconLocalPosition(-0.4f, 0.1f);
        roomData.put(RoomType.ICEHOCKEY, temp);

        temp = new RoomData(RoomType.MEDIA, 1,
                "F2_media.png",
                "F2_media_active.png",
                "map_icons_media.png",
                "bg_media.jpg",
                        assetManager);
        temp.setIconLocalPosition(0.3f, 0.3f);
        roomData.put(RoomType.MEDIA, temp);

        temp = new RoomData(RoomType.DOLL, 1,
                "F2_doll.png",
                "F2_doll_active.png",
                "map_icons_doll.png",
                "bg_doll.jpg",
                        assetManager);
        temp.setIconLocalPosition(-0.4f, 0.4f);
        roomData.put(RoomType.DOLL, temp);

        temp = new RoomData(RoomType.NATURE, 1,
                "F2_nature.png",
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
                        boolean lastRiddle = currentLine.endsWith(LAST_RIDDLE_END);
                        if (riddleEnd ||lastRiddle) {
                            currentLine = currentLine.substring(0, currentLine.length() - 1);
                        }
                        String[] temp = currentLine.split(RIDDLE_SEPARATOR);
                        riddle.addRiddleText(new RiddleTexts(temp[0], temp[1], temp[2], temp[3]));
                        if (riddleEnd) {
                            roomData.get(currentRoom).riddles.add(riddle);
                            break;
                        } else if (lastRiddle) {
                            roomData.get(currentRoom).lastRiddle = riddle;
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
        storyStartSeen = scoresPref.getBoolean("storyStartSeen", false);
        String scores = scoresPref.getString("scores", "");
        if (scores.length() > 0) {
            String[] scoreArray = scores.split(";");
            for (int i = 0; i < scoreArray.length; ++i) {
                String[] temp = scoreArray[i].split(":");
                String name = temp[0];
                int score = Integer.parseInt(temp[1]);
                roomData.get(RoomType.typeFromString(name)).highscore = score;
            }
        } else {
            for (RoomType t : roomData.keySet()) {
                roomData.get(t).highscore = 0;
            }
        }
        int totalScore = 0;
        for (RoomType t : roomData.keySet()) {
            totalScore += roomData.get(t).highscore;
        }
        lastTotalStars = totalScore;
        for (RoomType t : roomData.keySet()) {
            roomData.get(t).isLocked = totalScore < roomData.get(t).starsToUnlock;
        }
        System.out.println(totalScore);
        machineParts = new MachinePart[5];
        machineParts[0] = new MachinePart(1, totalScore,
                "time_machine_parts/time_machine_part_1.png",
                "time_machine_parts/time_machine_1.png",
                0.2f, 0.55f);

        machineParts[1] = new MachinePart(4, totalScore,
                "time_machine_parts/time_machine_part_2.png",
                "time_machine_parts/time_machine_2.png",
                0.27f, 0.52f);

        machineParts[2] = new MachinePart(9, totalScore,
                "time_machine_parts/time_machine_part_3.png",
                "time_machine_parts/time_machine_3.png",
                0.2f, 0.1f);

        machineParts[3] = new MachinePart(14, totalScore,
                "time_machine_parts/time_machine_part_4.png",
                "time_machine_parts/time_machine_4.png",
                0.53f, 0.38f);

        machineParts[4] = new MachinePart(21, totalScore,
                "time_machine_parts/time_machine_part_5.png",
                "time_machine_parts/time_machine_5.png",
                0.55f, 0.4f);
    }
}
