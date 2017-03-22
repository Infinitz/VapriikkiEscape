package fi.tamk.tiko.kivimiesgaming;

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

    private ArrayList<ImageActor> additionalStars;
    private ImageActor[] stars;

    private float starSize = 50;
    private float fullStarSizeMultiplier = 1.15f;
    private float middleStarOffset = 25f;

    public Stars(float x, float y, float scale, int starCount, boolean animated) {
        additionalStars = new ArrayList<ImageActor>();
        if (animated) {
            createAnimatedStars(x, y, scale, starCount);
        } else {
            createStars(x, y, scale, starCount);
        }
    }

    private void createStars(float x, float y, float scale, int starCount) {
        Texture starEmpty = new Texture("star_empty.png");
        Texture starFull = new Texture("star_full.png");

        stars = new ImageActor[3];

        for (int i = 0; i < stars.length; ++i) {
            if (starCount > i) {
                stars[i] = new ImageActor(starFull, fullStarSizeMultiplier * starSize * scale);
            } else {
                stars[i] = new ImageActor(starEmpty, starSize * scale);
            }

            float startX = x - stars[i].getSizeX() / 8 - 3 * stars[i].getSizeX() / 2;
            float space = stars[i].getSizeX() / 8;

            stars[i].setPosition(
                    startX + i * stars[i].getSizeX() + i * space,
                    y - stars[i].getSizeY());

            stars[i].setTouchable(Touchable.disabled);
        }

        stars[1].setY(stars[1].getY() - middleStarOffset);
    }

    private void createAnimatedStars(float x, float y, float scale, int starCount) {
        Texture starEmpty = new Texture("star_empty.png");
        Texture starFull = new Texture("star_full.png");

        stars = new ImageActor[3];

        float emptyStarAnimLength = 0.75f;
        for (int i = 0; i < stars.length; ++i) {
            stars[i] = new ImageActor(starEmpty, starSize * scale);

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
                float animDelay = 1;
                float preAnimLength = 1f;
                float animLength = 0.3f;
                float animDeltaY = 400f;

                ImageActor fullStar = new ImageActor(starFull,
                        fullStarSizeMultiplier * starSize * scale);

                fullStar.setPosition(posX, posY - animDeltaY);
                fullStar.setScale(0);

                final ImageActor emptyStar = stars[i];
                fullStar.addAction(Actions.sequence(
                        Actions.delay(i * animDelay + emptyStarAnimLength),
                        Actions.parallel(
                                Actions.scaleTo(5f / 4, 5f / 4, preAnimLength, Interpolation.pow3),
                                Actions.moveTo(posX, posY + animDeltaY,
                                        preAnimLength, Interpolation.pow3)
                        ),
                        Actions.parallel(
                                Actions.scaleTo(1, 1, animLength, Interpolation.pow3),
                                Actions.moveTo(posX, posY,
                                        animLength / 2, Interpolation.pow3)
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
/*
        float animDelay = 1;
        float animLength = 0.7f;
        for (int i = 0; i < starCount; ++i) {
            ImageActor fullStar = new ImageActor(starFull,
                    fullStarSizeMultiplier * starSize * scale);

            fullStar.setPosition(stars[i].getX(), stars[i].getY() + 600);
            fullStar.setScale(0);

            final ImageActor emptyStar = stars[i];
            fullStar.addAction(Actions.sequence(
                    Actions.delay(i * animDelay + emptyStarAnimLength),
                    Actions.parallel(
                            Actions.scaleTo(1, 1, animLength, Interpolation.pow3),
                            Actions.moveTo(stars[i].getX(), stars[i].getY(),
                                    animLength, Interpolation.pow3)
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
        */
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
