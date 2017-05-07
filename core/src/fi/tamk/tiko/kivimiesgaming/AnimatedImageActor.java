package fi.tamk.tiko.kivimiesgaming;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by atter on 07-May-17.
 */

public class AnimatedImageActor extends ImageActor {
    public boolean loop = false;

    private Animation<TextureRegion> currentAnimation;
    private float stateTime = 0;

    public AnimatedImageActor(Animation<TextureRegion> animation, float size) {
        super(null, size);
        setTextureRegion(animation.getKeyFrame(0f));
        currentAnimation = animation;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (renderEnabled) {
            stateTime += delta;
        }
    }

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

    public boolean isAnimationReady() {
        return currentAnimation.isAnimationFinished(stateTime);
    }
}
