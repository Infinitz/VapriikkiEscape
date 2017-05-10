package fi.tamk.tiko.kivimiesgaming;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

/**
 * @author Atte-Petteri Ronkanen, Risto Pulkkinen
 *
 * Class for room buttons in room selection screen
 */

public class RoomButton extends SelectableButton {

    /**
     * Asset manager for managing assetss
     */
    private AssetManager assetManager;

    /**
     * Room's data
     */
    private RoomData roomData;

    /**
     * Image actor for the room icon
     */
    private ImageActor roomIcon;

    /**
     * Group for all the elements in this class
     */
    private Group roomElements;

    /**
     * Image actor for the possible lock image
     */
    private ImageActor lock;

    /**
     * Constructor of the RoomButton
     *
     * @param data Room's data
     * @param assetManager Asset manager for managing assets
     * @param size Asset manager for managing assets
     */
    public RoomButton(RoomData data, AssetManager assetManager, float size) {
        super(data.getLockedTexture(), data.getUnlockedTexture(), size);
        this.assetManager = assetManager;
        this.roomData = data;
        roomIcon = new ImageActor(data.getIconTexture(), 200);

        roomElements = new Group();
        roomElements.addActor(this);
        roomElements.addActor(roomIcon);

        if (!data.unlockAnimation && !data.isLocked) {
            setTex(data.getUnlockedTexture());
        } else {
            setTouchable(Touchable.disabled);
            lock = new ImageActor(assetManager.get("map_lock.png", Texture.class), 100f);
            roomIcon.setColor(0.7f, 0.7f, 0.7f, 1f);
            roomElements.addActor(lock);
        }
    }

    /**
     * Returns all the room elements
     *
     * @return all the room elements
     */
    public Group getRoomElements() {
        roomIcon.setPosition(getX() + roomData.iconLocalPosX * getSizeX(),
                getY() + roomData.iconLocalPosY  * getSizeY());

        if (!roomData.isLocked && !roomData.unlockAnimation) {
            Stars s = new Stars(roomIcon.getX() + roomIcon.getSizeX() / 2,
                    roomIcon.getY(), 1, roomData.highscore, false, assetManager);
            s.addStarsToGroup(roomElements);
        } else {
            setTouchable(Touchable.disabled);
            lock.setPosition(roomIcon.getX(), roomIcon.getY());
        }

        return roomElements;
    }

    /**
     * Getter for room data
     *
     * @return room data
     */
    public RoomData getRoomData() {
        return roomData;
    }

    /**
     * Animate when selected or delected
     *
     * @param selected selected or delected
     */
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

    /**
     * Animation when the room is unlocked
     */
    public void unlockAnimation() {
        if (roomData.unlockAnimation) {
            SequenceAction shakeAction = new SequenceAction();
            float shakeTime = 0.7f;
            float moveTime = 0.033f;
            float maxMovement = 1f;
            float t = 0;
            float maxDelta = 8f;
            float xDelta = 0;
            float yDelta = 0;
            while (t < shakeTime) {
                float xMov = ((float)Math.random() -
                        MathUtils.lerp(0, 1, xDelta / maxDelta)) * maxMovement;
                float yMov = ((float)Math.random() -
                        MathUtils.lerp(0, 1, yDelta / maxDelta)) * maxMovement;
                xDelta += xMov;
                yDelta += yMov;

                shakeAction.addAction(Actions.moveBy(
                        xMov, yMov, moveTime));

                maxMovement *= 1.05f;
                t += moveTime;
            }

            lock.addAction(Actions.sequence(
                    Actions.sequence(
                            Actions.run(new Runnable() {
                                @Override
                                public void run() {
                                    AudioManager.playSound("unlock_lock.wav");
                                }
                            })
                    ),
                    Actions.parallel(
                            Actions.parallel(
                                    Actions.moveBy(10, 10, 0.3f, Interpolation.pow2),
                                    Actions.scaleTo(0.5f, 0.5f, 0.3f, Interpolation.pow2)
                            ),
                            shakeAction
                    ),

                    Actions.parallel(
                            Actions.moveBy(0, 75, 0.2f, Interpolation.pow2),
                            Actions.scaleTo(1.25f, 1.25f, 0.45f, Interpolation.bounce),
                            Actions.sequence(
                                    Actions.delay(0.3f),
                                    Actions.run(new Runnable() {
                                        @Override
                                        public void run() {
                                            setTex(roomData.getUnlockedTexture());
                                            roomIcon.setColor(1, 1, 1, 1);
                                            setTouchable(Touchable.enabled);
                                            roomData.unlockAnimation = false;
                                            Stars s = new Stars(
                                                    roomIcon.getX() + roomIcon.getSizeX() / 2,
                                                    roomIcon.getY(), 1,
                                                    roomData.highscore, false, assetManager);
                                            s.addStarsToGroup(roomElements);
                                            lock.setTex(assetManager.get(
                                                    "map_lock_unlocked.png", Texture.class));
                                        }
                                    })
                            )
                    ),
                    Actions.sequence(

                            Actions.parallel(
                                    Actions.scaleTo(1.35f, 1.35f, 2f),
                                    Actions.moveBy(25, 50, 2f),
                                    Actions.rotateBy(-45, 2f),
                                    Actions.repeat(50, Actions.run(new Runnable() {
                                        @Override
                                        public void run() {
                                            lock.alpha -= 0.02f;
                                        }
                                    }))
                            ),
                            Actions.run(new Runnable() {
                                @Override
                                public void run() {
                                    lock.remove();
                                }
                            })
                    )
            ));
        }

    }
}
