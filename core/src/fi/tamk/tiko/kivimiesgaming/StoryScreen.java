package fi.tamk.tiko.kivimiesgaming;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import java.util.ArrayList;

/**
 * @author Atte-Petteri Ronkanen, Risto Pulkkinen
 *
 * This class controls the story of the game.
 */

public abstract class StoryScreen extends MyScreen {

    /**
     * Controls how many marks are allowed in a line in a bubble.
     */
    protected final int marksInLineBubble = 21;

    /**
     * Controls the bubble size.
     */
    protected final float bubbleSize = 700f;

    /**
     * Stores the story sequence.
     */
    protected ArrayList<RunnableAction> storySequence;

    /**
     * This is the current animation for the story.
     */
    protected AnimatedImageActor currentAnimation;

    /**
     * Controls when the user is allowed to move on to the next part of the story.
     */
    protected boolean ableToProgress = false;

    /**
     * Texture for rossFace.
     */
    protected ImageActor rossFace;

    /**
     * The current group containing the bubble and the text.
     */
    protected Group currentBubbleGroup;

    /**
     * The style for the text.
     */
    protected Label.LabelStyle labelStyle;


    /**
     * Class constructor.
     *
     * @param game         Main class of the game.
     * @param assetManager AssetManager for loading and unloading.
     */
    public StoryScreen(Vescape game, AssetManager assetManager) {
        super(game, assetManager);
    }


    /**
     * Controls the animation and checks for user input
     *
     * @param dt Deltatime
     */
    @Override
    protected void update(float dt) {
        super.update(dt);

        if (currentAnimation != null && currentAnimation.isAnimationReady()) {
            final AnimatedImageActor temp = currentAnimation;
            temp.addAction(Actions.sequence(
                    Actions.repeat(50, Actions.run(new Runnable() {
                                @Override
                                public void run() {
                                    temp.alpha -= 0.02f;
                                }
                            })
                    ),
                    Actions.delay(0.4f),
                    Actions.run(new Runnable() {
                        @Override
                        public void run() {
                            progressStory();
                        }
                    })
            ));
            currentAnimation = null;
        } else if (currentAnimation == null && ableToProgress && Gdx.input.justTouched()) {
            progressStory();
        }

    }

    /**
     * When story starts this creates all the necessary things.
     */
    @Override
    public void onStart() {
        ImageActor background = new ImageActor(assetManager.get("story_bg.jpg", Texture.class),
                Vescape.GUI_VIEWPORT_HEIGHT);

        labelStyle = new Label.LabelStyle(getGame().getRiddleFont(),
                Color.BLACK);

        rossFace = new ImageActor(assetManager.get("story_ross_temp.png", Texture.class), 600);
        rossFace.setPosition(Vescape.GUI_VIEWPORT_WIDTH - 1.25f * rossFace.getSizeX(), 75);

        stage.addActor(background);
        stage.addActor(rossFace);
        createStorySequence();
        progressStory();
        game.saveScores();
    }

    /**
     * Getter for panelbutton1.
     *
     * @return panelbutton1
     */
    @Override
    public TextButton getPanelButton1() {
        return null;
    }

    /**
     * Getter for panelbutton2.
     *
     * @return panelbutton2
     */
    @Override
    public TextButton getPanelButton2() {
        return null;
    }

    /**
     * Initializes story sequence.
     */
    abstract void createStorySequence();

    /**
     * Creates new bubble for story.
     *
     * @param text Text for the bubble.
     * @param talk Controls bubble texture, if talking normal bubble, if false a yelling bubble.
     */
    protected void newBubble(String text, boolean talk) {
        ableToProgress = false;
        ImageActor bubble;
        if (text.length() == 0) {
            if (currentBubbleGroup != null) {
                currentBubbleGroup.addAction(
                        Actions.sequence(
                                Actions.run(new Runnable() {
                                    @Override
                                    public void run() {
                                        for (Actor a : currentBubbleGroup.getChildren()) {
                                            a.addAction(Actions.fadeOut(0.5f));
                                        }
                                        currentBubbleGroup.addAction(Actions.sequence(
                                                Actions.fadeOut(0.5f),
                                                Actions.run(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        currentBubbleGroup.remove();
                                                        currentBubbleGroup = null;
                                                    }
                                                })
                                        ));
                                    }
                                })
                        ));
            }
            return;
        }
        if (talk) {
            bubble = new ImageActor(assetManager.get("talk_bubble.png", Texture.class), bubbleSize);
        } else {
            bubble = new ImageActor(assetManager.get("shout_bubble.png", Texture.class), bubbleSize);
        }
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
                                if (currentAnimation == null) {
                                    ableToProgress = true;
                                }

                            }
                        })
                )
        );
        bubbleGroup.setPosition(Vescape.GUI_VIEWPORT_WIDTH - bubble.getSizeX() * 1.15f,
                Vescape.GUI_VIEWPORT_HEIGHT / 3 + 175f);
        stage.addActor(bubbleGroup);
    }

    /**
     * Gets the next element from list.
     */
    protected void progressStory() {
        if (!storySequence.isEmpty()) {
            storySequence.get(0).run();
            storySequence.remove(0);
        }
    }
}
