package fi.tamk.tiko.kivimiesgaming;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

/**
 * Created by atter on 11-Mar-17.
 */

public class RoomButton extends SelectableButton {

    private AssetManager assetManager;
    private RoomData roomData;
    private ImageActor roomIcon;
    private Group roomElements;

    public RoomButton(RoomData data, AssetManager assetManager, float size) {
        super(data.getLockedTexture(), data.getUnlockedTexture(), size);
        this.assetManager = assetManager;
        this.roomData = data;
        roomIcon = new ImageActor(data.getIconTexture(), 200);

        roomElements = new Group();
        roomElements.addActor(this);
        roomElements.addActor(roomIcon);

        if (!data.isLocked) {
            if (data.unlockAnimation) {
                unlockAnimation();

            } else {
                setTex(data.getUnlockedTexture());
            }
        } else {
            setTouchable(Touchable.disabled);
        }
    }

    public Group getRoomElements() {
        roomIcon.setPosition(getX() + roomData.iconLocalPosX * getSizeX(),
                getY() + roomData.iconLocalPosY  * getSizeY());

        if (!roomData.isLocked && !roomData.unlockAnimation) {
            Stars s = new Stars(roomIcon.getX() + roomIcon.getSizeX() / 2,
                    roomIcon.getY(), 1, roomData.highscore, false, assetManager);
            s.addStarsToGroup(roomElements);
        }

        return roomElements;
    }

    public RoomData getRoomData() {
        return roomData;
    }

    @Override
    protected void onSelect(boolean selected) {
        if (selected) {
            roomIcon.addAction(Actions.parallel(
                    Actions.moveBy(0, 50, 0.4f, Interpolation.pow2),
                    Actions.scaleTo(1.3f, 1.3f, 0.3f)
            ));
        } else {
            roomIcon.addAction(Actions.parallel(
                    Actions.moveBy(0, -50,
                            0.8f, Interpolation.bounceOut),
                    Actions.scaleTo(1f, 1f, 0.15f)
            ));
        }
    }

    public void unlockAnimation() {
        if (roomData.unlockAnimation) {
            addAction(Actions.sequence(
                    Actions.delay(1f),
                    Actions.parallel(
                            Actions.moveBy(0, -50, 1.5f, Interpolation.pow2),
                            Actions.scaleTo(0.9f, 0.9f, 1.5f, Interpolation.pow2)
                    ),
                    Actions.parallel(
                            Actions.moveBy(0, 75, 0.3f, Interpolation.pow2),
                            Actions.scaleTo(1.15f, 1.15f, 0.3f, Interpolation.pow2)
                    ),
                    Actions.run(new Runnable() {
                        @Override
                        public void run() {
                            setTex(roomData.getUnlockedTexture());
                            setTouchable(Touchable.enabled);
                            roomData.unlockAnimation = false;
                            Stars s = new Stars(roomIcon.getX() + roomIcon.getSizeX() / 2,
                                    roomIcon.getY(), 1, roomData.highscore, false, assetManager);
                            s.addStarsToGroup(roomElements);
                            roomIcon.addAction(
                                    Actions.sequence(
                                            Actions.parallel(
                                                    Actions.scaleTo(1.1f, 1.1f, 0.1f),
                                                    Actions.moveBy(15, 15, 0.1f)
                                            ),
                                            Actions.delay(0.15f),
                                            Actions.parallel(
                                                    Actions.scaleTo(1f, 1f, 1f),
                                                    Actions.moveBy(-15, -15, 1f)
                                            )

                            ));
                        }
                    }),
                    Actions.delay(0.25f),
                    Actions.parallel(
                            Actions.moveBy(0, -25, 1f, Interpolation.pow2),
                            Actions.scaleTo(1f, 1f, 1f, Interpolation.pow2)
                    )
            ));
        }

    }
}
