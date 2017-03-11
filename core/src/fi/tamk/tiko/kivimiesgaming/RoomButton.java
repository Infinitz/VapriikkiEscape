package fi.tamk.tiko.kivimiesgaming;

import com.badlogic.gdx.graphics.Texture;

/**
 * Created by atter on 11-Mar-17.
 */

public class RoomButton extends ImageActor {

    private RoomData roomData;

    public RoomButton(RoomData data) {
        super(data.getTexture());
    }

    public RoomButton(RoomData data, float size) {
        super(data.getTexture(), size);
    }



}
