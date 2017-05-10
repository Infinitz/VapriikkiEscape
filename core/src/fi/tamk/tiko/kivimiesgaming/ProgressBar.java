package fi.tamk.tiko.kivimiesgaming;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

/**
 * @author Atte-Petteri Ronkanen, Risto Pulkkinen
 *
 * Class for generic progress bar
 */

public class ProgressBar {

    /**
     * Time it takes to fill from empty to full
     */
    public float animateTime = 15f;

    /**
     * Back ground of the progress bar
     */
    private ImageActor bg;

    /**
     * Fill of the progress bar
     */
    private ImageActor fill;

    /**
     * frame of the progress bar
     */
    private ImageActor frame;

    /**
     * Current fill amount in range of [0, 1]
     */
    private float fillAmount = 0;

    /**
     * Offset in x size of the fill in relation of progress bars back ground
     */
    private float fillOffset = 0f;

    /**
     * Fills x scale when the fill is full
     */
    private float fillScaleMax;

    /**
     * Constructor of the progress bar
     *
     * @param size Height of the progress bar
     * @param fillAmount Current fill amount in range of [0, 1]
     * @param style Style of the progress bar
     */
    public ProgressBar(float size, float fillAmount, ProgressBarStyle style) {

        this.fillAmount = fillAmount;
        bg = new ImageActor(style.bg, size);
        fill = new ImageActor(style.fill, size);
        frame = new ImageActor(style.frame, size);

        fill.setSize(size);
        fill.setPosition(0, 0);
        fill.getTex().setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        fillScaleMax = (bg.getSizeX() - 2 * fillOffset) / fill.getSizeX();
        fill.originCenter = false;
        fill.setScaleX(MathUtils.lerp(0, fillScaleMax, fillAmount));

    }

    /**
     * Set current fill and animate changes
     *
     * @param newFill New fill amount in range of [0, 1]
     */
    public void setFill(float newFill) {
        float oldFill = fillAmount;
        this.fillAmount = newFill;

        fill.addAction(Actions.sequence(
                Actions.scaleTo(MathUtils.lerp(0, fillScaleMax, fillAmount), 1,
                (newFill - oldFill) * animateTime, Interpolation.pow2)));
    }

    /**
     * Getter for fillAmount
     *
     * @return fillAmount
     */
    public float getFillAmount() {
        return fillAmount;
    }

    /**
     * Returns all the actors of the bar
     *
     * @return actors of the bar
     */
    public ImageActor[] getActors() {
        ImageActor[] actors = new ImageActor[3];
        actors[0] = bg;
        actors[1] = fill;
        actors[2] = frame;

        return actors;
    }

    /**
     * Getter for width of the bar
     *
     * @return width of the bar
     */
    public float getWidth() {
        return bg.getSizeX();
    }

    /**
     * Getter for height of the bar
     *
     * @return height of the bar
     */
    public float getHeight() {
        return bg.getSizeY();
    }

    /**
     * Class to store progress bar textures
     */
    public static class ProgressBarStyle {

        /**
         * Background texture
         */
        public Texture bg;

        /**
         * frame texture
         */
        public Texture frame;

        /**
         * fill texture
         */
        public Texture fill;

        /**
         * Constructor of the Progress bar style
         *
         * @param bg Background texture
         * @param frame frame texture
         * @param fill fill texture
         */
        public ProgressBarStyle(Texture bg, Texture frame, Texture fill) {
            this.bg = bg;
            this.frame = frame;
            this.fill = fill;
        }
    }

}