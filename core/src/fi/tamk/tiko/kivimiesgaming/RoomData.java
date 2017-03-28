package fi.tamk.tiko.kivimiesgaming;

import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;

/**
 * Created by atter on 11-Mar-17.
 */

public class RoomData {

    public RoomType type;

    private int stars;
    private Texture texture;
    private Texture selectedTex;
    private Texture iconTexture;
    public float iconLocalPosX, iconLocalPosY = 0;

    public ArrayList<Riddle> riddles;

    public RoomData(RoomType type, Texture tex, Texture selectedTex, Texture iconTexture, int stars) {
        this.type = type;
        this.texture = tex;
        this.selectedTex = selectedTex;
        this.iconTexture = iconTexture;
        this.stars = stars;

        riddles = new ArrayList<Riddle>();
    }
/*
    public RoomData(RoomType type, Texture tex, Texture selectedTex, Texture iconTexture, int stars,
                    float iconLocalPosX, float iconLocalPosY) {
        this.type = type;
        this.texture = tex;
        this.selectedTex = selectedTex;
        this.iconTexture = iconTexture;
        this.stars = stars;
        this.iconLocalPosX = iconLocalPosX;
        this.iconLocalPosY = iconLocalPosY;
    }*/

    public int getStars() {
        return stars;
    }

    public Texture getTexture() {
        return texture;
    }

    public Texture getSelectedTex() {
        return selectedTex;
    }

    public Texture getIconTexture() {
        return iconTexture;
    }

    public RoomType getType() {
        return type;
    }

    public void setIconLocalPosition(float x, float y) {
        iconLocalPosX = x;
        iconLocalPosY = y;
    }
}
