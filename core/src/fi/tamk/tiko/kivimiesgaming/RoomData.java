package fi.tamk.tiko.kivimiesgaming;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;

/**
 * Created by atter on 11-Mar-17.
 */

public class RoomData {

    public RoomType type;

    public int highscore = 0;
    public int latestScore = 0;
    public int starsToUnlock = 0;
    public boolean isLocked = true;
    public boolean unlockAnimation = false;

    private AssetManager assetManager;
    private String lockedTexPath;
    private String unlockedTexPath;
    private String iconTextureP;
    private String backgroundP;
    public float iconLocalPosX, iconLocalPosY = 0;

    private boolean[] usedRiddle;
    public ArrayList<Riddle> riddles;
    public Riddle lastRiddle;

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

    public Riddle getRandomRiddle() {
        int index = (int)(Math.random() * riddles.size());
        int tryCount = 0;
        while (usedRiddle[index] && tryCount < 10) {
            index = (int)(Math.random() * riddles.size());
            ++tryCount;
        }
        if (tryCount > 10) {
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

    public void loadTextures() {
        assetManager.load(lockedTexPath, Texture.class);
        assetManager.load(unlockedTexPath, Texture.class);
        assetManager.load(iconTextureP, Texture.class);
        assetManager.load(backgroundP, Texture.class);
    }

    public void unloadTextures() {
        assetManager.unload(lockedTexPath);
        assetManager.unload(unlockedTexPath);
        assetManager.unload(iconTextureP);
        assetManager.unload(backgroundP);
    }

    public void loadRiddles(AssetManager assets) {
        usedRiddle = new boolean[riddles.size()];
        for (int i = 0; i < riddles.size(); ++i) {
            assets.load(riddles.get(i).imagePath, Texture.class);
            usedRiddle[i] = false;
        }
        assets.load(lastRiddle.imagePath, Texture.class);    }

    public void unloadRiddleImages(AssetManager assets) {
        for (Riddle r : riddles) {
            assets.unload(r.imagePath);
        }
        assets.unload(lastRiddle.imagePath);
    }

    public Texture getLockedTexture() {
        return assetManager.get(lockedTexPath, Texture.class);
    }

    public Texture getUnlockedTexture() {
        return assetManager.get(unlockedTexPath, Texture.class);
    }

    public Texture getIconTexture() {
        return assetManager.get(iconTextureP, Texture.class);
    }

    public Texture getBackground() {
        return assetManager.get(backgroundP, Texture.class);
    }

    public void setIconLocalPosition(float x, float y) {
        iconLocalPosX = x;
        iconLocalPosY = y;
    }
}
