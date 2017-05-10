package fi.tamk.tiko.kivimiesgaming;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

/**
 * @author Atte-Petteri Ronkanen, Risto Pulkkinen
 *
 * Class that manages playing audio
 */

public class AudioManager {

    /**
     * Volume of sound effects
     */
    private static float soundVol = 1.0f;

    /**
     * Volume of music
     */
    private static float musicVol = 0.7f;

    /**
     * Used for managing assets
     */
    private static AssetManager assetManager;

    /**
     * Is sounds enabled
     */
    private static boolean SOUNDS_ENABLED = true;

    /**
     * Is music enabled
     */
    private static boolean MUSIC_ENABLED = true;

    /**
     * File path of the current music
     */
    private static String currentMusic;

    /**
     * Constructor of the class
     *
     * @param assetManager Asset manager for managing assets
     */
    public AudioManager(AssetManager assetManager) {
        this.assetManager = assetManager;

    }

    /**
     * Setter for SOUNDS_ENABLED
     *
     * @param enabled If true -> enable, false -> disabled
     */
    public static void enableSounds(boolean enabled) {
        SOUNDS_ENABLED = enabled;
    }

    /**
     * Getter for SOUNDS_ENABLED
     *
     * @return SOUNDS_ENABLED
     */
    public static boolean isSoundsEnabled() {
        return SOUNDS_ENABLED;
    }

    /**
     * Setter for MUSIC_ENABLED
     *
     * @param enabled If true -> enable, false -> disabled
     */
    public static void enableMusic(boolean enabled) {
        MUSIC_ENABLED = enabled;
        if (enabled) {
            assetManager.get(currentMusic, Music.class).play();
        } else {
            assetManager.get(currentMusic, Music.class).pause();
        }
    }

    /**
     * Starts playing given music on loop
     *
     * @param path Path where the music file is found
     */
    public static void playMusic(String path) {
        currentMusic = path;
        if (MUSIC_ENABLED) {
            assetManager.get(currentMusic, Music.class).setLooping(true);
            assetManager.get(currentMusic, Music.class).setVolume(musicVol);
            assetManager.get(currentMusic, Music.class).play();
        }

    }

    /**
     * Plays given sound effect
     *
     * @param path Path where the sound file is found
     */
    public static void playSound(String path) {
        if (SOUNDS_ENABLED) {
            assetManager.get(path, Sound.class).play(soundVol);
        }
    }

    /**
     * Getter for MUSIC_ENABLED
     *
     * @return MUSIC_ENABLED
     */
    public static boolean isMusicEnabled() {
        return MUSIC_ENABLED;
    }

}
