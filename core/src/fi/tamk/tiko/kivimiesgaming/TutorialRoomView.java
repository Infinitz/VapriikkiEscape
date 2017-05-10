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
 * @author Atte-Petteri Ronkanen, Risto Pulkkinen
 *
 * This class controls the tutorial, which is slightly different than other rooms.
 */
public class TutorialRoomView extends RoomView {

    /**
     * Controls how marks are allowed in a line in the text bubble.
     */
    private final int marksInLineBubble = 21;

    /**
     * Controls the bubble size.
     */
    private final float bubbleSize = 700f;

    /**
     * rossFace texture.
     */
    protected ImageActor rossFace;

    /**
     * Current bubble group consisting of the bubble texture and the text.
     */
    protected Group currentBubbleGroup;

    /**
     * LabelStyle for the text.
     */
    protected Label.LabelStyle labelStyle;


    /**
     * Class constructor.
     *
     * @param game         Main class of the game.
     * @param roomData     Data of the room in question
     * @param assetManager AssetManager for loading the textures.
     */
    public TutorialRoomView(Vescape game, RoomData roomData, AssetManager assetManager) {
        super(game, roomData, assetManager);
        assetManager.load("story_ross_temp.png", Texture.class);
        assetManager.load("talk_bubble.png", Texture.class);
        riddlesInRoom = 2;
    }

    /**
     * Starts the room as in parent class, plus creates the character's face and text style.
     */
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
    /**
     * Checks what happens when the player answers.
     *
     * @param playerAnswer The player's answer as string.
     */
    public void answer(String playerAnswer) {
        playerAnswer = playerAnswer.trim();
        boolean correctAnswer = currentRiddle.getRiddle(
                getGame().getMyBundle().getLocale().getLanguage()).isCorrectAnswer(playerAnswer);

        if (correctAnswer) {
            ++currentRiddleCount;
            correctAnswerCount++;
            if (hintUsed) {
                hintsUsedCount++;
                //AudioManager.playSound("answer_right.wav");
                answerResults[currentRiddleCount - 1] =
                        new ImageActor(correctAnswerTex, answerResultSlots[0].getSizeY());
            } else {
                AudioManager.playSound("answer_perfect.wav");
                answerResults[currentRiddleCount - 1] =
                        new ImageActor(perfectAnswerTex, answerResultSlots[0].getSizeY());
            }
        } else if (!hintUsed) {
            if (currentRiddleCount != 1) {
                hintGroup.addAction(
                        Actions.moveBy(-hintButtonAnimMovement, 0, 0.5f, Interpolation.pow2));
            }
            hintUsed = true;

            return;
        } else {
            ++currentRiddleCount;
            answerResults[currentRiddleCount - 1] =
                    new ImageActor(wrongAnswerTex, answerResultSlots[0].getSizeY());
        }

        if (hintUsed) {
            hintGroup.addAction(
                    Actions.moveBy(hintButtonAnimMovement, 0, 0.5f, Interpolation.pow2));
        }


        answerField.setDisabled(true);
        answerField.setText("");

        answerResults[currentRiddleCount - 1].setPosition(Vescape.GUI_VIEWPORT_WIDTH / 2,
                3 * Vescape.GUI_VIEWPORT_HEIGHT / 2);
        answerResults[currentRiddleCount - 1].setScale(10f);
        getStage().addActor(answerResults[currentRiddleCount - 1]);
        answerResults[currentRiddleCount - 1].addAction(Actions.sequence(
                Actions.parallel(
                        Actions.moveTo(
                                Vescape.GUI_VIEWPORT_WIDTH / 2 -
                                        answerResults[currentRiddleCount - 1].getSizeX() / 2,
                                Vescape.GUI_VIEWPORT_HEIGHT / 2 -
                                        answerResults[currentRiddleCount - 1].getSizeY() / 2, 0.3f,
                                Interpolation.pow2),
                        Actions.scaleTo(3.5f, 3.5f, 0.6f, Interpolation.bounceOut),
                        Actions.rotateTo(((float) Math.random() - 0.5f) * 25f, 0.35f),
                        Actions.sequence(
                                Actions.delay(0.25f),
                                Actions.run(new Runnable() {
                                    @Override
                                    public void run() {
                                        AudioManager.playSound("answer_result_hit.wav");
                                    }
                                })
                        )
                ),
                Actions.delay(0.25f),

                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        nextRiddle();
                    }
                }),
                Actions.parallel(
                        Actions.moveTo(answerResultSlots[currentRiddleCount - 1].getX(),
                                answerResultSlots[currentRiddleCount - 1].getY(),
                                0.75f, Interpolation.pow2),
                        Actions.scaleTo(1, 1, 0.5f),
                        Actions.rotateTo(0, 0.5f)
                )
        ));

    }

    /**
     * Checks which of the 2 riddles is in play and adds the text according to that.
     */
    @Override
    protected void onNewRiddleStarted() {
        if (currentRiddleCount == 0) {
            rossTalk(game.getMyBundle().get("tutorialText1"));
        } else if (currentRiddleCount == 1) {
            rossTalk(game.getMyBundle().get("tutorialText2"));
        }
    }

    /**
     * Disposes the screen.
     */
    @Override
    public void dispose() {
        super.dispose();
        assetManager.unload("story_ross_temp.png");
        assetManager.unload("talk_bubble.png");
    }

    /**
     * Adds talking character to the screen.
     *
     * @param text The text what the character says.
     */
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
                                                if (currentRiddleCount == 1) {
                                                    hintGroup.addAction(
                                                            Actions.moveBy(-hintButtonAnimMovement, 0, 0.5f, Interpolation.pow2));
                                                }

                                            }
                                        })
                                ));
                            }
                        });
                    }
                })
        ));
    }

    /**
     * Creates new bubble.
     *
     * @param text The text what the bubble says.
     */
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

        if (currentBubbleGroup == null) {
            currentBubbleGroup = bubbleGroup;
        }
        bubbleGroup.setPosition(Vescape.GUI_VIEWPORT_WIDTH - bubble.getSizeX() * 1.15f,
                Vescape.GUI_VIEWPORT_HEIGHT / 3 + 175f);
        stage.addActor(bubbleGroup);
    }
}
