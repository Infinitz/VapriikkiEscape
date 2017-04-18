package fi.tamk.tiko.kivimiesgaming;

/**
 * Created by atter on 13-Apr-17.
 */

public class MachinePart {
    public boolean unlocked;

    private int starsToUnlock;
    private String imagePath;

    public MachinePart(int starsToUnlock, int totalStars, String imagePath) {
        this.starsToUnlock = starsToUnlock;
        this.unlocked = starsToUnlock <= totalStars;
        this.imagePath = imagePath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public int getStarsToUnlock() {
        return starsToUnlock;
    }
}
