package fi.tamk.tiko.kivimiesgaming;

import com.badlogic.gdx.assets.AssetManager;

/**
 * Created by atter on 02-May-17.
 */

public class TutorialRoomView extends RoomView {
    public TutorialRoomView(Vescape game, RoomData roomData, AssetManager assetManager) {
        super(game, roomData, assetManager);
        riddlesInRoom = 2;
    }
}
