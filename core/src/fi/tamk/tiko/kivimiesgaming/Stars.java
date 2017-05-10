package fi.tamk.tiko.kivimiesgaming;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import java.util.ArrayList;

/**
 * @author Atte-Petteri Ronkanen, Risto Pulkkinen
 *
 * This class controls the star system.
 */

public class Stars {

    /**
     * Empty star texture.
     */
    private Texture starEmptyTex;

    /**
     * Full star texture.
     */
    private Texture starFullTex;

    /**
     * When collecting stars this stores the additional ones and removes old ones.
     */
    private ArrayList<ImageActor> additionalStars;

    /**
     * Used for creating stars.
     */
    private ImageActor[] stars;

    /**
     * Controls the star size.
     */
    private float starSize = 50;

    /**
     * Full star is a bit larger than an empty one.
     */
    private float fullStarSizeMultiplier = 1.15f;

    /**
     * Middle star is a bit higher than the rest.
     */
    private float middleStarOffset = 25f;

    /**
     * Class constructor.
     *
     * @param x         X coordinate.
     * @param y         Y coordinate.
     * @param scale     Star scale.
     * @param starCount Star count.
     * @param animated  If the star is animated or not.
     * @param assets    For managing assets.
     */
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

    /**
     * Creates stars.
     *
     * @param x         X coordinate.
     * @param y         Y coordinate.
     * @param scale     Star scale.
     * @param starCount Star count.
     */
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

    /**
     * Creates animated stars.
     *
     * @param x         X coordinate.
     * @param y         Y coordinate.
     * @param scale     Star scale.
     * @param starCount Star count.
     */
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

    /**
     * Adds additional stars to a group for control.
     *
     * @param g The group where the stars are stored.
     */
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
