package fi.tamk.tiko.kivimiesgaming;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;

import java.util.ArrayList;

/**
 * Created by atter on 07-May-17.
 */

public class StoryStartScreen extends StoryScreen {

    public StoryStartScreen(Vescape game, AssetManager assetManager) {
        super(game, assetManager);
        assetManager.load("story_bg.jpg", Texture.class);
        assetManager.load("arrival_animation.png", Texture.class);

        assetManager.load("story_ross_hmm.png", Texture.class);
        assetManager.load("story_ross_sad.png", Texture.class);
        assetManager.load("story_ross_temp.png", Texture.class);
        assetManager.load("story_ross_what.png", Texture.class);
        assetManager.load("talk_bubble.png", Texture.class);
        assetManager.load("shout_bubble.png", Texture.class);
    }

    @Override
    public void dispose() {
        super.dispose();
        assetManager.unload("story_bg.jpg");
        assetManager.unload("arrival_animation.png");

        assetManager.unload("story_ross_hmm.png");
        assetManager.unload("story_ross_sad.png");
        assetManager.unload("story_ross_temp.png");
        assetManager.unload("story_ross_what.png");
        assetManager.unload("talk_bubble.png");
        assetManager.unload("shout_bubble.png");
    }

    @Override
    void createStorySequence() {
        storySequence = new ArrayList<RunnableAction>();

        storySequence.add(Actions.run(new Runnable() {
            @Override
            public void run() {
                rossFace.setX(rossFace.getX() + rossFace.getSizeX() * 3);
                Texture sheet = assetManager.get("arrival_animation.png", Texture.class);
                TextureRegion[][] frames = TextureRegion.split(
                        sheet, sheet.getWidth() / 7, sheet.getHeight());
                Animation<TextureRegion> animation = new Animation<TextureRegion>(1f / 10, frames[0]);
                currentAnimation = new AnimatedImageActor(animation, 0.56f * Vescape.GUI_VIEWPORT_HEIGHT );
                currentAnimation.setX(Vescape.GUI_VIEWPORT_WIDTH - currentAnimation.getSizeX());
                currentAnimation.setY(250);
                stage.addActor(currentAnimation);
            }
        }));

        storySequence.add(Actions.run(new Runnable() {
            @Override
            public void run() {
                ableToProgress = false;
                rossFace.addAction(Actions.sequence(
                        Actions.moveBy(-rossFace.getSizeX() * 3, 0, 0.5f, Interpolation.pow2),
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                progressStory();
                            }
                        })
                ));
            }
        }));

        storySequence.add(Actions.run(new Runnable() {
            @Override
            public void run() {
                ableToProgress = false;
                rossFace.setTex(assetManager.get("story_ross_temp.png", Texture.class));
                newBubble(game.getMyBundle().get("storyStart_1"), true);
            }
        }));

        storySequence.add(Actions.run(new Runnable() {
            @Override
            public void run() {
                ableToProgress = false;
                rossFace.setTex(assetManager.get("story_ross_hmm.png", Texture.class));
                newBubble(game.getMyBundle().get("storyStart_2"), true);
            }
        }));

        storySequence.add(Actions.run(new Runnable() {
            @Override
            public void run() {
                ableToProgress = false;
                rossFace.setTex(assetManager.get("story_ross_what.png", Texture.class));
                newBubble(game.getMyBundle().get("storyStart_3"), false);
            }
        }));

        storySequence.add(Actions.run(new Runnable() {
            @Override
            public void run() {
                ableToProgress = false;
                rossFace.setTex(assetManager.get("story_ross_sad.png", Texture.class));
                newBubble(game.getMyBundle().get("storyStart_4"), true);
            }
        }));

        storySequence.add(Actions.run(new Runnable() {
            @Override
            public void run() {
                ableToProgress = false;
                rossFace.setTex(assetManager.get("story_ross_temp.png", Texture.class));
                newBubble(game.getMyBundle().get("storyStart_5"), true);
            }
        }));

        storySequence.add(Actions.run(new Runnable() {
            @Override
            public void run() {
                getGame().setScreen(new RoomSelection(getGame(), assetManager));
            }
        }));
    }
}
