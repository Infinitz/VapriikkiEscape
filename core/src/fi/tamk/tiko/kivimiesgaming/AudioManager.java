package fi.tamk.tiko.kivimiesgaming;

/**
 * Created by atter on 04-Mar-17.
 */

public class AudioManager {
    private static boolean SOUNDS_ENABLED = true;
    private static boolean MUSIC_ENABLED = true;

    public AudioManager() {
        //load sounds and music
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
            //play music
        } else {
            //stop music
        }
    }

    public static boolean isMusicEnabled() {
        return MUSIC_ENABLED;
    }

    public void dispose() {
        //dispose sounds and music
    }
}
