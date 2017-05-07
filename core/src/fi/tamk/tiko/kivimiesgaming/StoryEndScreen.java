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

public class StoryEndScreen extends StoryScreen {

    public StoryEndScreen(Vescape game, AssetManager assetManager) {
        super(game, assetManager);
        assetManager.load("story_bg.jpg", Texture.class);
        assetManager.load("departure_animation.png", Texture.class);
        assetManager.load("bling_animation.png", Texture.class);

        assetManager.load("story_ross_confuse.png", Texture.class);
        assetManager.load("story_ross_joy.png", Texture.class);
        assetManager.load("story_ross_puuh.png", Texture.class);
        assetManager.load("story_ross_temp.png", Texture.class);
        assetManager.load("story_ross_what.png", Texture.class);
        assetManager.load("talk_bubble.png", Texture.class);
        assetManager.load("shout_bubble.png", Texture.class);
    }

    @Override
    public void dispose() {
        super.dispose();
        assetManager.unload("story_bg.jpg");
        assetManager.unload("departure_animation.png");
        assetManager.unload("bling_animation.png");

        assetManager.unload("story_ross_confuse.png");
        assetManager.unload("story_ross_joy.png");
        assetManager.unload("story_ross_puuh.png");
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
                ableToProgress = false;
                rossFace.setX(rossFace.getX() + rossFace.getSizeX() * 3);
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
                rossFace.setTex(assetManager.get("story_ross_puuh.png", Texture.class));
                newBubble(game.getMyBundle().get("storyEnd_1"), true);
            }
        }));

        storySequence.add(Actions.run(new Runnable() {
            @Override
            public void run() {
                ableToProgress = false;
                rossFace.setTex(assetManager.get("story_ross_joy.png", Texture.class));
                newBubble(game.getMyBundle().get("storyEnd_2"), true);
            }
        }));

        storySequence.add(Actions.run(new Runnable() {
            @Override
            public void run() {
                ableToProgress = false;
                rossFace.setTex(assetManager.get("story_ross_temp.png", Texture.class));
                newBubble(game.getMyBundle().get("storyEnd_3"), true);
            }
        }));

        storySequence.add(Actions.run(new Runnable() {
            @Override
            public void run() {
                ableToProgress = false;
                rossFace.setTex(assetManager.get("story_ross_confuse.png", Texture.class));
                newBubble(game.getMyBundle().get("storyEnd_4"), true);
            }
        }));

        storySequence.add(Actions.run(new Runnable() {
            @Override
            public void run() {
                ableToProgress = false;
                rossFace.setTex(assetManager.get("story_ross_what.png", Texture.class));
                newBubble(game.getMyBundle().get("storyEnd_5"), false);

                /*
                rossFace.addAction(Actions.sequence(
                        Actions.delay(0.4f),
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                ableToProgress = true;
                            }
                        }))
                );*/
            }
        }));

        storySequence.add(Actions.run(new Runnable() {
            @Override
            public void run() {
                ableToProgress = false;
                Texture sheet = assetManager.get("departure_animation.png", Texture.class);
                TextureRegion[][] frames = TextureRegion.split(
                        sheet, sheet.getWidth() / 6, sheet.getHeight());
                Animation<TextureRegion> animation = new Animation<TextureRegion>(1f / 10, frames[0]);
                currentAnimation = new AnimatedImageActor(animation, Vescape.GUI_VIEWPORT_HEIGHT * 0.7f);
                currentAnimation.setX(500);
                stage.addActor(currentAnimation);
            }
        }));

        storySequence.add(Actions.run(new Runnable() {
            @Override
            public void run() {
                ableToProgress = false;
                Texture sheet = assetManager.get("bling_animation.png", Texture.class);
                TextureRegion[][] frames = TextureRegion.split(
                        sheet, sheet.getWidth() / 4, sheet.getHeight());
                Animation<TextureRegion> animation = new Animation<TextureRegion>(1f / 10, frames[0]);
                currentAnimation = new AnimatedImageActor(animation, 400);
                currentAnimation.setPosition(250, 950);
                stage.addActor(currentAnimation);
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
