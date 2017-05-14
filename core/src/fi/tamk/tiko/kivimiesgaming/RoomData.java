package fi.tamk.tiko.kivimiesgaming;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;

/**
 * @author Atte-Petteri Ronkanen, Risto Pulkkinen
 *
 * Contains all the data of the room.
 */

public class RoomData {

    /**
     * The room type information.
     */
    public RoomType type;

    /**
     * The current highscore for the room.
     */
    public int highscore = 0;

    /**
     * The latest score which shows in the popup.
     */
    public int latestScore = 0;

    /**
     * How many stars are needed to unlock next part.
     */
    public int starsToUnlock = 0;

    /**
     * Checks if the room is locked.
     */
    public boolean isLocked = true;

    /**
     * Checks if the unlock animation is needed.
     */
    public boolean unlockAnimation = false;

    /**
     * For loading and unloading assets.
     */
    private AssetManager assetManager;

    /**
     * Path for locked texture.
     */
    private String lockedTexPath;

    /**
     * Path for unlocked texture.
     */
    private String unlockedTexPath;

    /**
     * Path for icon texture.
     */
    private String iconTextureP;

    /**
     * Path for background.
     */
    private String backgroundP;

    /**
     * Sets icon positions.
     */
    public float iconLocalPosX, iconLocalPosY = 0;

    /**
     * Array of riddles that are used so they don't show up again.
     */
    private boolean[] usedRiddle;

    /**
     * List of riddles.
     */
    public ArrayList<Riddle> riddles;

    /**
     * Specific riddle for last riddle of the room.
     */
    public Riddle lastRiddle;

    private int currentRiddleIndex = 0;

    /**
     * Class constructor.
     *
     * @param type          Room type.
     * @param starsToUnlock How many stars to unlock room.
     * @param texP          Texture path.
     * @param selectedTexP  Selected texture path.
     * @param iconTextureP  Icon texture path.
     * @param backgroundP   Background texture path.
     * @param assetManager  For loading and unloading assets.
     */
    public RoomData(RoomType type, int starsToUnlock, String texP,
                    String selectedTexP, String iconTextureP,
                    String backgroundP, AssetManager assetManager) {
        this.starsToUnlock = starsToUnlock;
        this.assetManager = assetManager;
        this.type = type;
        this.lockedTexPath = texP;
        this.unlockedTexPath = selectedTexP;
        this.iconTextureP = iconTextureP;
        this.backgroundP = backgroundP;
        riddles = new ArrayList<Riddle>();
    }

    /**
     * Loads the textures.
     */
    public void loadTextures() {
        assetManager.load(lockedTexPath, Texture.class);
        assetManager.load(unlockedTexPath, Texture.class);
        assetManager.load(iconTextureP, Texture.class);
        assetManager.load(backgroundP, Texture.class);
    }

    /**
     * Unloads the textures.
     */
    public void unloadTextures() {
        assetManager.unload(lockedTexPath);
        assetManager.unload(unlockedTexPath);
        assetManager.unload(iconTextureP);
        assetManager.unload(backgroundP);
    }

    /**
     * Loads the riddles images.
     *
     * @param assets Used for loading.
     */
    public ArrayList<Riddle> loadRiddles(AssetManager assets) {
        currentRiddleIndex = 0;
        ArrayList<Riddle> roomRiddles = new ArrayList<Riddle>();
        usedRiddle = new boolean[riddles.size()];

        for (int i = 0; i < riddles.size(); ++i) {
            usedRiddle[i] = false;
        }

        int asd = lastRiddle != null ? 1 : 0;
        if (type != RoomType.TUTORIAL) {
            for (int i = 0; i < Vescape.TOTAL_RIDDLES_ROOM - asd; ++i) {
                roomRiddles.add(getRandomRiddle());
                assets.load(roomRiddles.get(i).imagePath, Texture.class);
            }
        } else {
            for (int i = 0; i < 2 - asd; ++i) {
                roomRiddles.add(getRandomRiddle());
                assets.load(roomRiddles.get(i).imagePath, Texture.class);
            }
        }

        if (lastRiddle != null) {
            roomRiddles.add(lastRiddle);
            assets.load(lastRiddle.imagePath, Texture.class);
        }

        return roomRiddles;
    }

    /**
     * Gets random riddle from the pool as long as it has not been used.
     *
     * @return Returns the riddle.
     */
    private Riddle getRandomRiddle() {
        int index = (int) (Math.random() * riddles.size());
        int tryCount = 0;
        int maxTries = 10;
        while (usedRiddle[index] && tryCount < 10) {
            index = (int) (Math.random() * riddles.size());
            ++tryCount;
        }
        if (tryCount > maxTries) {
            boolean unUsedFound = false;
            for (int i = 0; i < riddles.size(); ++i) {
                if (!usedRiddle[i]) {
                    index = i;
                    unUsedFound = true;
                    break;
                }
            }
            if (!unUsedFound) {
                for (int i = 0; i < riddles.size(); ++i) {
                    usedRiddle[i] = false;
                }
                return getRandomRiddle();
            }
        }

        usedRiddle[index] = true;
        return riddles.get(index);
    }

    /**
     * Getter for locked texture.
     *
     * @return Locked texture.
     */
    public Texture getLockedTexture() {
        return assetManager.get(lockedTexPath, Texture.class);
    }

    /**
     * Getter for unlocked texture.
     *
     * @return Unlocked texture.
     */
    public Texture getUnlockedTexture() {
        return assetManager.get(unlockedTexPath, Texture.class);
    }

    /**
     * Getter for icon texture.
     *
     * @return Icon texture.
     */
    public Texture getIconTexture() {
        return assetManager.get(iconTextureP, Texture.class);
    }

    /**
     * Getter for backgroun texture.
     *
     * @return Background texture.
     */
    public Texture getBackground() {
        return assetManager.get(backgroundP, Texture.class);
    }

    /**
     * Sets icon positions.
     *
     * @param x X coordinate.
     * @param y Y coordinate.
     */
    public void setIconLocalPosition(float x, float y) {
        iconLocalPosX = x;
        iconLocalPosY = y;
    }
}
