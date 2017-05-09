package fi.tamk.tiko.kivimiesgaming;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

/**
 * Created by atter on 02-May-17.
 */

public class TutorialRoomView extends RoomView {

    private final int marksInLineBubble = 21;
    private final float bubbleSize = 700f;

    protected ImageActor rossFace;
    protected Group currentBubbleGroup;
    protected Label.LabelStyle labelStyle;
    protected boolean ableToProgress;

    public TutorialRoomView(Vescape game, RoomData roomData, AssetManager assetManager) {
        super(game, roomData, assetManager);
        assetManager.load("story_ross_temp.png", Texture.class);
        assetManager.load("talk_bubble.png", Texture.class);
        riddlesInRoom = 2;
    }

    @Override
    public void onStart() {
        super.onStart();
        labelStyle = new Label.LabelStyle(getGame().getRiddleFont(),
                Color.BLACK);

        rossFace = new ImageActor(assetManager.get("story_ross_temp.png", Texture.class), 600);
        rossFace.setPosition(
                        Vescape.GUI_VIEWPORT_WIDTH -
                        1.25f * rossFace.getSizeX() +
                        rossFace.getSizeX() * 3,
                        75);
    }

    @Override
    protected void onNewRiddleStarted() {
        if (currentRiddleCount == 0) {
            rossTalk(game.getMyBundle().get("tutorialText1"));
        } else if(currentRiddleCount == 1) {
            rossTalk(game.getMyBundle().get("tutorialText2"));
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        assetManager.unload("story_ross_temp.png");
        assetManager.unload("talk_bubble.png");
    }

    protected void rossTalk(final String text) {
        final ImageActor touchDetector = new ImageActor(
                assetManager.get("black.png", Texture.class), Vescape.GUI_VIEWPORT_HEIGHT);
        touchDetector.setTouchable(Touchable.enabled);
        touchDetector.alpha = 0;

        stage.addActor(rossFace);
        stage.addActor(touchDetector);
        rossFace.addAction(Actions.sequence(
                Actions.moveBy(-rossFace.getSizeX() * 3, 0, 0.5f, Interpolation.pow2),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        newBubble(text);
                        touchDetector.setClickListener(new ChangeListener() {
                            @Override
                            public void changed(ChangeEvent event, Actor actor) {
                                touchDetector.remove();
                                newBubble("");
                                rossFace.addAction(Actions.sequence(
                                        Actions.moveBy(rossFace.getSizeX() * 3, 0, 0.5f, Interpolation.pow2),
                                        Actions.run(new Runnable() {
                                            @Override
                                            public void run() {
                                                rossFace.remove();
                                            }
                                        })
                                ));
                            }
                        });
                    }
                })
        ));
    }

    protected void newBubble(String text) {
        ImageActor bubble;
        if (text.length() == 0) {
            if (currentBubbleGroup != null) {
                for (Actor a : currentBubbleGroup.getChildren()) {
                    a.addAction(Actions.fadeOut(0.5f));
                }
                currentBubbleGroup.addAction(
                        Actions.sequence(
                                Actions.delay(0.5f),
                                Actions.run(new Runnable() {
                                    @Override
                                    public void run() {
                                        currentBubbleGroup.remove();
                                        currentBubbleGroup = null;
                                    }
                                })
                        ));
            }
            return;
        }

        bubble = new ImageActor(assetManager.get("talk_bubble.png", Texture.class), bubbleSize);

        Label bubbleText = new Label(Utilities.splitTextIntoLines(text, marksInLineBubble), labelStyle);

        final Group bubbleGroup = new Group();
        bubbleText.setPosition(bubble.getSizeX() / 2 - bubbleText.getWidth() / 2,
                bubble.getSizeY() / 2 - bubbleText.getHeight() / 2 + bubble.getSizeY() / 9);

        bubbleGroup.addActor(bubble);
        bubbleGroup.addActor(bubbleText);

        if (currentBubbleGroup != null) {
            currentBubbleGroup.addAction(
                    Actions.sequence(
                            Actions.run(new Runnable() {
                                @Override
                                public void run() {
                                    currentBubbleGroup.remove();
                                    currentBubbleGroup = bubbleGroup;
                                }
                            })
                    ));
        } else {
            currentBubbleGroup = bubbleGroup;
        }

        bubbleGroup.addAction(
                Actions.sequence(
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                //ableToProgress = true;
                            }
                        })
                )
        );

        if (currentBubbleGroup == null) {
            currentBubbleGroup = bubbleGroup;
        }
        bubbleGroup.setPosition(Vescape.GUI_VIEWPORT_WIDTH - bubble.getSizeX() * 1.15f,
                Vescape.GUI_VIEWPORT_HEIGHT / 3 + 175f);
        stage.addActor(bubbleGroup);
    }
}
