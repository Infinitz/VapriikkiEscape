package fi.tamk.tiko.kivimiesgaming;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import java.util.ArrayList;


/**
 * Created by atter on 21-Mar-17.
 */

public class Stars {

    private Texture starEmptyTex;
    private Texture starFullTex;

    private ArrayList<ImageActor> additionalStars;
    private ImageActor[] stars;

    private float starSize = 50;
    private float fullStarSizeMultiplier = 1.15f;
    private float middleStarOffset = 25f;

    public Stars(float x, float y, float scale, int starCount, boolean animated,
                 AssetManager assets) {
        starEmptyTex = assets.get("star_empty.png", Texture.class);
        starFullTex = assets.get("star_full.png", Texture.class);
        additionalStars = new ArrayList<ImageActor>();
        if (animated) {
            createAnimatedStars(x, y, scale, starCount);
        } else {
            createStars(x, y, scale, starCount);
        }
    }

    private void createStars(float x, float y, float scale, int starCount) {

        stars = new ImageActor[3];

        float emptyStarAnimLength = 0.75f;
        for (int i = 0; i < stars.length; ++i) {
            Texture temp = starEmptyTex;
            if (starCount > i) {
                temp = starFullTex;
            }
            stars[i] = new ImageActor(temp, starSize * scale);

            float startX = x - stars[i].getSizeX() / 8 - 3 * stars[i].getSizeX() / 2;
            float space = stars[i].getSizeX() / 8;

            float posX = startX + i * stars[i].getSizeX() + i * space;
            float posY = y - stars[i].getSizeY();

            if (i == 1)
                posY -= middleStarOffset;

            stars[i].setPosition(
                    posX,
                    posY - 300f);

            stars[i].setScale(0);
            stars[i].setTouchable(Touchable.disabled);
            stars[i].addAction(Actions.parallel(
                    Actions.scaleTo(1, 1, emptyStarAnimLength, Interpolation.pow3),
                    Actions.moveTo(posX, posY,
                            emptyStarAnimLength, Interpolation.pow3)
            ));
        }
    }

    private void createAnimatedStars(float x, float y, float scale, int starCount) {

        stars = new ImageActor[3];

        float emptyStarAnimLength = 0.75f;
        for (int i = 0; i < stars.length; ++i) {
            stars[i] = new ImageActor(starEmptyTex, starSize * scale);

            float startX = x - stars[i].getSizeX() / 8 - 3 * stars[i].getSizeX() / 2;
            float space = stars[i].getSizeX() / 8;

            float posX = startX + i * stars[i].getSizeX() + i * space;
            float posY = y - stars[i].getSizeY();

            if (i == 1)
                posY -= middleStarOffset;

            stars[i].setPosition(
                    posX,
                    posY - 300f);

            stars[i].setScale(0);
            stars[i].setTouchable(Touchable.disabled);
            stars[i].addAction(Actions.parallel(
                    Actions.scaleTo(1, 1, emptyStarAnimLength, Interpolation.pow3),
                    Actions.moveTo(posX, posY,
                            emptyStarAnimLength, Interpolation.pow3)
            ));

            additionalStars.add(stars[i]);

            if (i < starCount) {
                float animDelay = 0.9f;
                float preAnimLength = 0.6f;
                float animLength = 0.3f;
                float animDeltaY = 400f;

                ImageActor fullStar = new ImageActor(starFullTex,
                        fullStarSizeMultiplier * starSize * scale);

                fullStar.setPosition(posX, posY - animDeltaY);
                fullStar.setScale(0);

                final ImageActor emptyStar = stars[i];
                fullStar.addAction(Actions.sequence(
                        Actions.delay(i * animDelay + emptyStarAnimLength),
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                AudioManager.playSound("star_enter.wav");
                            }
                        }),
                        Actions.parallel(
                                Actions.scaleTo(5f / 4, 5f / 4, preAnimLength, Interpolation.pow3),
                                Actions.moveTo(posX, posY + animDeltaY,
                                        preAnimLength, Interpolation.pow3)
                        ),
                        Actions.parallel(
                                Actions.scaleTo(1, 1, animLength, Interpolation.pow3),
                                Actions.moveTo(posX, posY,
                                        animLength / 2, Interpolation.pow3),
                                Actions.run(new Runnable() {
                                    @Override
                                    public void run() {
                                        AudioManager.playSound("star_hit.wav");
                                    }
                                })
                        ),
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                emptyStar.remove();
                            }
                        })
                ));

                stars[i] = fullStar;
            }
        }

    }

    public void addStarsToGroup(Group g) {
        while (!additionalStars.isEmpty()) {
            g.addActor(additionalStars.get(0));
            additionalStars.remove(0);
        }

        for (int i = 0; i < stars.length; ++i) {
            g.addActor(stars[i]);
        }

    }
}
