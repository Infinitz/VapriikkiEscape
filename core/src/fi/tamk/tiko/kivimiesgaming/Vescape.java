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

/**
 * @author Atte-Petteri Ronkanen, Risto Pulkkinen
 *
 * Main class of the game
 */
public class Vescape extends Game {

    /**
     * Camera's viewport width
     */
    public static final float GUI_VIEWPORT_WIDTH = 900;

    /**
     * Camera's viewport height
     */
    public static final float GUI_VIEWPORT_HEIGHT = 1600;

    /**
     * Max amount of characters in line on the screen
     */
    public static final int MAX_CHARS_PER_LINE = 29;

    /**
     * Max amount of characters in riddle answerfield
     */
    public static final int MAX_CHARS_IN_ANSWER = 21;

    /**
     * Total riddles per one room
     */
    public static final int TOTAL_RIDDLES_ROOM = 5;

    /**
     * Scoring penalty which is applied when hint is used
     */
    public static final float HINT_PENALTY = 0.33f;

    /**
     * Mark which separates different rooms in text file
     */
    public static final String ROOM_DATA_MARK = "&";

    /**
     * Mark which separates different answer alternatives in text file
     */
    public static final String RIDDLE_ANSWER_SEPARATOR = "//";

    /**
     * Mark which separates language/riddle/answer/hint in text file
     */
    public static final String RIDDLE_SEPARATOR = "::";

    /**
     * Mark which communicates that the riddle line has ended in text file
     */
    public static final String RIDDLE_END = ";";

    /**
     * Mark which communicates that this riddle is used as last riddle in the room
     */
    public static final String LAST_RIDDLE_END = "@";

    /**
     * Mark which communicates that this line is comments in text file
     */
    public static final String RIDDLE_FILE_COMMENT_MARK = "#";

    /**
     * Path to text file where all the riddles are
     */
    public static final String RIDDLE_FILE_PATH = "data/riddles.txt";

    /**
     * Path to folder where the riddle images are
     */
    public static final String RIDDLE_IMAGES_PATH = "riddle_images/";

    /**
     * Result of previous score calculations
     */
    public static int lastTotalStars = 0;

    /**
     * True if player has seen the story allready
     */
    public static boolean storyStartSeen = false;

    /**
     * Setting preferences where all settings are saved
     */
    private Preferences settingsPref;

    /**
     * Score preferences where all scores are saved
     */
    private Preferences scoresPref;

    /**
     * Asset manager that is used to load image and audio files
     */
    private AssetManager assetManager;

    /**
     * Batch that is used for drawing all the graphics
     */
    private SpriteBatch batch;

    /**
     * Bundle that is used for localisation
     */
    private I18NBundle myBundle;

    /**
     * Font of text buttons
     */
    private BitmapFont buttonFont;

    /**
     * Bigger font that is used for headers etc
     */
    private BitmapFont fontBig;

    /**
     * Smaller font that is used for riddles and story
     */
    private BitmapFont riddleFont;

    /**
     * Medium sized font that is used for answer field
     */
    private BitmapFont answerFieldFont;

    /**
     * Style that is used for all text buttons
     */
    private TextButton.TextButtonStyle textButtonStyle;

    /**
     * Style that is used for text fields
     */
    private TextField.TextFieldStyle textFieldStyle;

    /**
     * Hash map where all room data are stored. RoomType is used as a key.
     */
    private HashMap<RoomType, RoomData> roomData;

    /**
     * All machine part data
     */
    private MachinePart[] machineParts;

    /**
     * True if initial assets for loading screen are loaded
     */
    private boolean initialAssetsLoaded = false;

    /**
     * This is called at start of the game. Initialize all the neccessary objects and start loading initial assets.
     */
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

    /**
     * Is called on every frame
     */
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

    /**
     * Changes current screen to given screen
     *
     * @param screen New screen which current oone is changed to
     */
    @Override
    public void setScreen(Screen screen) {
        Screen temp = getScreen();
        if (temp != null) {
            temp.dispose();
        }
        super.setScreen(screen);
        ((MyScreen) screen).onStart();
    }

    /**
     * Dispose all the neccessary objects on closing the game
     */
    @Override
    public void dispose() {

        if (getScreen() != null) {
            getScreen().dispose();
        }
        batch.dispose();
        assetManager.dispose();
    }

    /**
     * Reset saved scores
     */
    public void resetScores() {
        scoresPref.putString("scores", "");
        scoresPref.putBoolean("storyStartSeen", false);
        scoresPref.flush();

        loadScores();
    }

    /**
     * Returns maximum amount of stars
     *
     * @return Maximum amount of stars
     */
    public int getMaxStars() {
        return roomData.keySet().size() * 3;
    }

    /**
     * Getter for batch
     *
     * @return batch
     */
    public SpriteBatch getBatch() {
        return batch;
    }

    /**
     * Getter for buttonFont
     *
     * @return buttonFont
     */
    public BitmapFont getButtonFont() {
        return buttonFont;
    }

    /**
     * Getter for fontBig
     *
     * @return fontBig
     */
    public BitmapFont getFontBig() {
        return fontBig;
    }

    /**
     * Getter for riddleFont
     *
     * @return riddleFont
     */
    public BitmapFont getRiddleFont() {
        return riddleFont;
    }

    /**
     * Getter for textButtonStyle
     *
     * @return textButtonStyle
     */
    public TextButton.TextButtonStyle getTextButtonStyle() {
        return textButtonStyle;
    }

    /**
     * Getter for textFieldStyle
     *
     * @return textFieldStyle
     */
    public TextField.TextFieldStyle getTextFieldStyle() {
        return textFieldStyle;
    }

    /**
     * Getter for myBundle
     *
     * @return myBundle
     */
    public I18NBundle getMyBundle() {
        return myBundle;
    }

    /**
     * Returns data of room which has the given type
     *
     * @param type Type that determines which room data is returned
     * @return Room data that has the given type
     */
    public RoomData getRoomData(RoomType type) {
        return roomData.get(type);
    }

    /**
     * Getter for machineParts
     *
     * @return machineParts
     */
    public MachinePart[] getMachineParts() {
        return machineParts;
    }

    /**
     * Set language to finnish
     */
    public void setFinnish() {
        setLocale(new Locale("fi", "FI"));
    }

    /**
     * Set language to English
     */
    public void setEnglish() {
        setLocale(new Locale("en", "US"));
    }

    /**
     * Save current settings to file
     */
    public void saveSettings() {
        settingsPref.putBoolean("musicEnabled", AudioManager.isMusicEnabled());
        settingsPref.putBoolean("soundEnabled", AudioManager.isSoundsEnabled());
        settingsPref.putString("language", myBundle.get("language"));
        settingsPref.flush();
    }

    /**
     * Save current scores to file
     */
    public void saveScores() {
        String scores = "";
        scoresPref.putBoolean("storyStartSeen", storyStartSeen);

        for (RoomType key : roomData.keySet()) {
            scores += key.toString() + ":" + roomData.get(key).highscore + ";";
        }
        scoresPref.putString("scores", scores);
        scoresPref.flush();
    }

    /**
     * Set bundles locale to given locale
     *
     * @param locale New locale that is set
     */
    private void setLocale(Locale locale) {
        if (locale != null) {
            myBundle = I18NBundle.createBundle(Gdx.files.internal("MyBundle"), locale);
        } else {
            myBundle = I18NBundle.createBundle(Gdx.files.internal("MyBundle"));
        }
    }

    /**
     * Load all the assets that are needed in most of the screens
     */
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
        assetManager.load("confirm_box.jpg", Texture.class);
        assetManager.load("druckknopf_ja.png", Texture.class);
        assetManager.load("druckknopf_nein.png", Texture.class);
        assetManager.load("back_button.png", Texture.class);

        assetManager.load("menurger.png", Texture.class);
        assetManager.load("menurger_pressed.png", Texture.class);

        assetManager.load("button_press.wav", Sound.class);
        assetManager.load("button_toggle.wav", Sound.class);
        assetManager.load("panel_open.wav", Sound.class);
        assetManager.load("panel_close.wav", Sound.class);

    }

    /**
     * Load initial assets that are needed for loading screen
     */
    private void loadInitialAssets() {
        assetManager.load("MENU_bg.jpg", Texture.class);
        assetManager.load("loading.png", Texture.class);
        assetManager.load("music_bg.mp3", Music.class);
        assetManager.load("MENU_button.png", Texture.class);
        assetManager.load("MENU_button_pressed.png", Texture.class);
        assetManager.load("indicator_line.jpg", Texture.class);
        assetManager.load("black.png", Texture.class);
    }

    /**
     * Create all the styles and fonts
     */
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

    /**
     * Initialize room data
     */
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
        temp = new RoomData(RoomType.ROCK, 4,
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

        temp = new RoomData(RoomType.GAME, 4,
                "F2_game.png",
                "F2_game_active.png",
                "map_icons_game.png",
                "bg_game.jpg",
                assetManager);
        temp.setIconLocalPosition(0.4f, -0.2f);
        roomData.put(RoomType.GAME, temp);

        temp = new RoomData(RoomType.ICEHOCKEY, 4,
                "F2_icehokey.png",
                "F2_icehokey_active.png",
                "map_icons_icehockey.png",
                "bg_hockey.jpg",
                assetManager);
        temp.setIconLocalPosition(-0.4f, 0.1f);
        roomData.put(RoomType.ICEHOCKEY, temp);

        temp = new RoomData(RoomType.MEDIA, 4,
                "F2_media.png",
                "F2_media_active.png",
                "map_icons_media.png",
                "bg_media.jpg",
                assetManager);
        temp.setIconLocalPosition(0.3f, 0.3f);
        roomData.put(RoomType.MEDIA, temp);

        temp = new RoomData(RoomType.DOLL, 4,
                "F2_doll.png",
                "F2_doll_active.png",
                "map_icons_doll.png",
                "bg_doll.jpg",
                assetManager);
        temp.setIconLocalPosition(-0.4f, 0.4f);
        roomData.put(RoomType.DOLL, temp);

        temp = new RoomData(RoomType.NATURE, 4,
                "F2_nature.png",
                "F2_nature_active.png",
                "map_icons_nature.png",
                "bg_nature.jpg",
                assetManager);
        temp.setIconLocalPosition(0.15f, 0.3f);
        roomData.put(RoomType.NATURE, temp);


    }

    /**
     * Load the riddles from the text file
     */
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
                        if (riddleEnd || lastRiddle) {
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

    /**
     * Load the scores from the preferences
     */
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
