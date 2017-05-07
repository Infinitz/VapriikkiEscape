package fi.tamk.tiko.kivimiesgaming;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import java.util.ArrayList;

/**
 * Created by atter on 07-May-17.
 */

public abstract class StoryScreen extends MyScreen {

    protected final int marksInLineBubble = 25;
    protected final float bubbleSize = 800f;
    protected final float transitionAnimDuration = 3f;

    protected ArrayList<RunnableAction> storySequence;
    protected AnimatedImageActor currentAnimation;
    protected boolean ableToProgress = false;

    protected ImageActor rossFace;
    protected Group currentBubbleGroup;
    protected Label.LabelStyle labelStyle;

    public StoryScreen(Vescape game, AssetManager assetManager) {
        super(game, assetManager);
    }

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

    @Override
    protected void onAssetsLoaded() {
        ImageActor background = new ImageActor(assetManager.get("story_bg.jpg", Texture.class),
                Vescape.GUI_VIEWPORT_HEIGHT);

        labelStyle = new Label.LabelStyle(getGame().getRiddleFont(),
                Color.BLACK);

        rossFace = new ImageActor(assetManager.get("story_ross_temp.png", Texture.class), 300);
        rossFace.setPosition(Vescape.GUI_VIEWPORT_WIDTH - 2f * rossFace.getSizeX(), 0);

        stage.addActor(background);
        stage.addActor(rossFace);
        createStorySequence();
        progressStory();
    }

    @Override
    public TextButton getPanelButton1() {
        return null;
    }

    @Override
    public TextButton getPanelButton2() {
        return null;
    }

    abstract void createStorySequence();

    protected void newBubble(String text, boolean talk) {
        ableToProgress = false;
        ImageActor bubble;
        if (talk) {
            bubble = new ImageActor(assetManager.get("talk_bubble.png", Texture.class), bubbleSize);
        } else {
            bubble = new ImageActor(assetManager.get("shout_bubble.png", Texture.class), bubbleSize);
        }
        Label bubbleText = new Label(Utilities.splitTextIntoLines(text, marksInLineBubble), labelStyle);

        final Group bubbleGroup = new Group();
        bubbleText.setPosition(bubble.getSizeX() / 2 - bubbleText.getWidth() / 2,
                bubble.getSizeY() / 2 - bubbleText.getHeight() / 2);

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
        if (currentBubbleGroup == null) {
            currentBubbleGroup = bubbleGroup;
        }
        bubbleGroup.setPosition(Vescape.GUI_VIEWPORT_WIDTH - bubble.getSizeX(),
                Vescape.GUI_VIEWPORT_HEIGHT / 5);
        stage.addActor(bubbleGroup);
    }

    protected void progressStory() {
        if (!storySequence.isEmpty()) {
            storySequence.get(0).run();
            storySequence.remove(0);
        }
    }
}
