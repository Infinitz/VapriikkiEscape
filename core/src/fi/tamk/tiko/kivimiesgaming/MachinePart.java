package fi.tamk.tiko.kivimiesgaming;

/**
 * Created by atter on 13-Apr-17.
 */

public class MachinePart {
    public boolean unlocked;

    private int starsToUnlock;
    private String partImagePath;
    private String shipImagePath;

    public MachinePart(int starsToUnlock, int totalStars, String partImagePath, String shipImagePath) {
        this.starsToUnlock = starsToUnlock;
        this.unlocked = starsToUnlock <= totalStars;
        this.partImagePath = partImagePath;
        this.shipImagePath = shipImagePath;
    }

    public String getShipImagePath() {
        return shipImagePath;
    }

    public String getPartImagePath() {
        return partImagePath;
    }

    public int getStarsToUnlock() {
        return starsToUnlock;
    }
}
