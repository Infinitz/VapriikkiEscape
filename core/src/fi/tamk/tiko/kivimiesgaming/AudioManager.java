package fi.tamk.tiko.kivimiesgaming;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;


/**
 * Created by atter on 04-Mar-17.
 */

public class AudioManager {
    private static float soundVol = 1.0f;
    private static float musicVol = 0.7f;
    private static AssetManager assetManager;
    private static boolean SOUNDS_ENABLED = true;
    private static boolean MUSIC_ENABLED = true;

    private static String currentMusic;

    public AudioManager(AssetManager assetManager) {
        this.assetManager = assetManager;

    }

    public static void enableSounds(boolean enabled) {
        SOUNDS_ENABLED = enabled;
    }

    public static boolean isSoundsEnabled() {
        return SOUNDS_ENABLED;
    }

    public static void enableMusic(boolean enabled) {
        MUSIC_ENABLED = enabled;
        if (enabled) {
            assetManager.get(currentMusic, Music.class).play();
        } else {
            assetManager.get(currentMusic, Music.class).pause();
        }
    }

    public static void playMusic(String path) {
        currentMusic = path;
        if (MUSIC_ENABLED) {
            assetManager.get(currentMusic, Music.class).setLooping(true);
            assetManager.get(currentMusic, Music.class).setVolume(musicVol);
            assetManager.get(currentMusic, Music.class).play();
        }

    }

    public static void playSound(String path) {
        if (SOUNDS_ENABLED) {
            assetManager.get(path, Sound.class).play(soundVol);
        }
    }

    public static boolean isMusicEnabled() {
        return MUSIC_ENABLED;
    }

}
