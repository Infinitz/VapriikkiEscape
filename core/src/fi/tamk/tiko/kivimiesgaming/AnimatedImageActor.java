package fi.tamk.tiko.kivimiesgaming;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * @author Atte-Petteri Ronkanen, Risto Pulkkinen
 *
 * ImageActor that suppoerts frame by frame animation
 */

public class AnimatedImageActor extends ImageActor {
    /**
     * Is the animation looping
     */
    public boolean loop = false;

    /**
     * Current animation of the actor
     */
    private Animation<TextureRegion> currentAnimation;

    /**
     * Current state time of the animation
     */
    private float stateTime = 0;

    /**
     * Constructor of the class
     *
     * @param animation Actors current animation
     * @param size Height of the actor
     */
    public AnimatedImageActor(Animation<TextureRegion> animation, float size) {
        super(null, size);
        setTextureRegion(animation.getKeyFrame(0f));
        currentAnimation = animation;
    }

    /**
     * Is called on every frame. Increments state time when rendering is enabled
     *
     * @param delta Delta time between last and current frame
     */
    @Override
    public void act(float delta) {
        super.act(delta);
        if (renderEnabled) {
            stateTime += delta;
        }
    }

    /**
     * Method where the actor is drawn
     *
     * @param batch Batch which is used for the drawing
     * @param alpha Parents alpha value. It isn't used here but it's needed so the mehdo can be overridden
     */
    @Override
    public void draw(Batch batch, float alpha) {
        if (renderEnabled) {
            Color c = batch.getColor();
            batch.setColor(getColor().r * c.r, getColor().g * c.g, getColor().b * c.b,
                    getColor().a * c.a * this.alpha);
            float oX = 0, oY = 0;
            if (originCenter) {
                oX = getSizeX() / 2;
                oY = getSizeY() / 2;
            }
            batch.draw(currentAnimation.getKeyFrame(stateTime, loop), getX(), getY(),
                    oX,
                    oY,
                    getSizeX(), getSizeY(), getScaleX(), getScaleY(),
                    getRotation());
            batch.setColor(c.r, c.g, c.b, 1);
        }
    }

    /**
     * Returns true if animation is completed at least once
     *
     * @return true if animation is completed at least once
     */
    public boolean isAnimationReady() {
        return currentAnimation.isAnimationFinished(stateTime);
    }
}
