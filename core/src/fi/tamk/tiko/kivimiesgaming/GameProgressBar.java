package fi.tamk.tiko.kivimiesgaming;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import java.util.ArrayList;

/**
 * @author Atte-Petteri Ronkanen, Risto Pulkkinen
 *
 * Class for progress bar for the score system
 */

public class GameProgressBar {

    /**
     * Asset manager for managing assets
     */
    private AssetManager assets;

    /**
     * Reference to the instance of the main class
     */
    private Vescape game;

    /**
     * Group where the elements of the progress bar are
     */
    private Group group;

    /**
     * The actual progress bar of this progress bar
     */
    private ProgressBar progressBar;

    /**
     * Stars count to next threshold label
     */
    private Label starsToNextThresholdLabel;

    /**
     * Image of the star
     */
    private ImageActor starImage;

    /**
     * List of all unreached thresholds
     */
    private ArrayList<Threshold> thresholds;

    /**
     * Coordinates of this progres bars bottom left corner
     */
    private float x, y;

    /**
     * How many percentages one star is from max stars
     */
    private float stepInFill;

    /**
     * Constructor of the class
     *
     * @param x X coordinate of this element
     * @param y Y coordinate of this element
     * @param size Height of this element
     * @param assets Asset manager for managing assets
     * @param game Reference to the instance of the main class
     */
    public GameProgressBar(float x, float y, float size, AssetManager assets, Vescape game) {
        this.assets = assets;
        this.game = game;
        this.x = x;
        this.y = y;
        stepInFill = 1f / game.getMaxStars();
        thresholds = new ArrayList<Threshold>();
        group = new Group();

        ProgressBar.ProgressBarStyle progressBarStyle =
                new ProgressBar.ProgressBarStyle(
                        assets.get("proggbar_back.png", Texture.class),
                        assets.get("proggbar_border.png", Texture.class),
                        assets.get("proggbar_fill.png", Texture.class));

        progressBar =
                new ProgressBar(size, (float)Vescape.lastTotalStars / game.getMaxStars(),
                        progressBarStyle);

        ImageActor[] pBarActors = progressBar.getActors();
        for (int i = 0; i < pBarActors.length; ++i) {
            group.addActor(pBarActors[i]);
        }

        starImage = new ImageActor(assets.get("star_full.png", Texture.class),
                120);

        starImage.setPosition(progressBar.getWidth(), 0);

        starsToNextThresholdLabel = new Label(Integer.toString(starsToNextUnlock(0, true) -
                Vescape.lastTotalStars),
                new Label.LabelStyle(game.getFontBig(),
                        new Color(0.3f, 0.3f, 0.3f, 0.7f)));
        starsToNextThresholdLabel.setPosition(progressBar.getWidth() + starImage.getSizeX() * 0.3f,
                0.1f * starImage.getSizeY());

        group.addActor(starImage);
        group.addActor(starsToNextThresholdLabel);

        group.setPosition(x, y);
    }

    /**
     * Progresses the bar with given amount of stars with animation
     *
     * @param count How many steps is progressed
     */
    public void animatedStepProgress(int count) {
        progressBar.setFill(progressBar.getFillAmount() + count * stepInFill);
        starsToNextThresholdLabel.setText(Integer.toString(starsToNextUnlock(count, false) -
                Vescape.lastTotalStars - count));

        if (thresholds.isEmpty())
            return;

        if (progressBar.getFillAmount() >= thresholds.get(0).value ||
                Math.abs(progressBar.getFillAmount() - thresholds.get(0).value) < 0.00001f) {
            reachedThreshold();
        }
    }

    /**
     * Adds new threshold to the progress bar
     *
     * @param threshold Value of the new threshold
     */
    public void addTreshold(float threshold) {

        Threshold thresholdActor = new Threshold(
                assets.get("proggbar_marker_closed.png", Texture.class),
                progressBar.getHeight(), threshold);
        thresholdActor.setPosition(
                MathUtils.lerp(0, progressBar.getWidth(), threshold),
                0);
        thresholdActor.setX(thresholdActor.getX() - thresholdActor.getSizeX() / 2);
        thresholds.add(thresholdActor);
        group.addActor(thresholdActor);
    }

    /**
     * Removes and animates first threshold in the list
     */
    public void reachedThreshold() {
        final Threshold threshold = thresholds.remove(0);

        threshold.unlockAnimation(group,
                assets.get("proggbar_marker_open_upper.png", Texture.class),
                assets.get("proggbar_marker_open_lower.png", Texture.class));
    }

    /**
     * Returns the count of stars needed for next unlock
     *
     * @param additional Additional stars which are not yet saved to the scores
     * @param addThresholds If true, the method will add thresholds to the bar
     * @return Count of stars needed for next unlock
     */
    private int starsToNextUnlock(int additional, boolean addThresholds) {
        boolean first = true;
        int stars = 0;
        for (int i = 0; i < game.getMachineParts().length; ++i) {
            if (game.getMachineParts()[i].getStarsToUnlock() > Vescape.lastTotalStars + additional) {
                if (first) {
                    stars = game.getMachineParts()[i].getStarsToUnlock();
                    first = !first;
                }
                if (addThresholds) {
                    addTreshold((float)game.getMachineParts()[i].getStarsToUnlock() /
                            game.getMaxStars());
                }
            }
        }
        if (first) {
            return game.getMaxStars();
        }
        return stars;
    }

    /**
     * Getter for group
     *
     * @return group
     */
    public Group getGroup() {
        return group;
    }

    /**
     * Getter for x
     *
     * @return x
     */
    public float getX() {
        return x;
    }

    /**
     * Getter for y
     *
     * @return y
     */
    public float getY() {
        return y;
    }

    /**
     * Setter for x
     *
     * @param x
     */
    public void setX(float x) {
        this.x = x;
        group.setX(x);
    }

    /**
     * Setter for y
     *
     * @param y
     */
    public void setY(float y) {
        this.y = y;
        group.setY(y);
    }

    /**
     * Returns width of the progress bar
     *
     * @return Width of the progress bar
     */
    public float getWidth() {
        return progressBar.getWidth();
    }

    /**
     * Returns height of the progress bar
     *
     * @return Height of the progress bar
     */
    public float getHeight() {
        return progressBar.getHeight();
    }

    /**
     * Class for threshold in progress bar
     */
    private class Threshold extends ImageActor{

        /**
         * Value of the threshold in range of [0, 1]
         */
        public float value;

        /**
         * Constructor of the threshold
         *
         * @param tex Texture of the locked threshold
         * @param size Height of the threshold
         * @param value Value of the threshold in range of [0, 1]
         */
        public Threshold(Texture tex, float size, float value) {
            super(tex, size);
            this.value = value;
        }

        /**
         * Plays unlock animation
         *
         * @param group Progres bar's element group
         * @param top Texture of thresholds top part
         * @param bottom Texture of thresholds bottom part
         */
        public void unlockAnimation(Group group, Texture top, Texture bottom) {
            ImageActor topActor = new ImageActor(top,
                    1.15f * getSizeY() / 2 * top.getHeight() / bottom.getHeight());

            ImageActor bottomActor = new ImageActor(bottom,
                    1.15f * getSizeY() / 2);

            remove();

            float xOffset = (bottomActor.getSizeX() - getSizeX()) / 2;
            float yOffset = ((topActor.getSizeY() + bottomActor.getSizeY()) - getSizeY()) / 2;
            bottomActor.setPosition(getX() - xOffset, getY() - yOffset);
            topActor.setPosition(getX() - xOffset + bottomActor.getSizeX() / 4,
                    bottomActor.getY() + bottomActor.getSizeY());


            group.addActor(topActor);
            group.addActor(bottomActor);

            topActor.addAction(Actions.sequence(
                   // Actions.moveBy(0, -getSizeY() / 50, 0.1f, Interpolation.pow2),
                    Actions.parallel(
                            Actions.moveBy(0, getSizeY() / 6, 0.7f, Interpolation.pow2)
                            //, Actions.scaleTo(1.25f, 1.25f, 0.7f, Interpolation.pow2)
                    )

            ));
            bottomActor.addAction(Actions.sequence(
                   // Actions.moveBy(0, getSizeY() / 50, 0.1f, Interpolation.pow2),
                    Actions.parallel(
                            Actions.moveBy(0, -getSizeY() / 6, 0.7f, Interpolation.pow2)
                            //, Actions.scaleTo(1.25f, 1.25f, 0.7f, Interpolation.pow2)
                    )
            ));


        }
    }
}
