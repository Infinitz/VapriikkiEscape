package fi.tamk.tiko.kivimiesgaming;

import com.badlogic.gdx.graphics.Texture;

/**
 * Created by atter on 11-Mar-17.
 */

public class RoomData {

    public RoomType type;

    private int stars;
    private Texture texture;

    public RoomData(RoomType type, Texture bgTex, int stars) {
        this.type = type;
        this.texture = bgTex;
        this.stars = stars;
    }


    public int getStars() {
        return stars;
    }

    public Texture getTexture() {
        return texture;
    }
}
