package fi.tamk.tiko.kivimiesgaming;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import java.util.ArrayList;

/**
 * Created by atter on 18-Apr-17.
 */

public class ProgressBar {

    public float animateTime = 15f;

    private ImageActor bg;
    private ImageActor fill;
    private ImageActor frame;

    private float fillAmount = 0;
    private float fillOffset = 0f;

    private float fillScaleMax;

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

    public void setFill(float newFill) {
        float oldFill = fillAmount;
        this.fillAmount = newFill;

        fill.addAction(Actions.sequence(
                Actions.scaleTo(MathUtils.lerp(0, fillScaleMax, fillAmount), 1,
                (newFill - oldFill) * animateTime, Interpolation.pow2)));
    }

    public float getFillAmount() {
        return fillAmount;
    }

    public ImageActor[] getActors() {
        ImageActor[] actors = new ImageActor[3];
        actors[0] = bg;
        actors[1] = fill;
        actors[2] = frame;

        return actors;
    }

    public float getWidth() {
        return bg.getSizeX();
    }

    public float getHeight() {
        return bg.getSizeY();
    }

    public static class ProgressBarStyle {

        public Texture bg;
        public Texture frame;
        public Texture fill;

        public ProgressBarStyle(Texture bg, Texture frame, Texture fill) {
            this.bg = bg;
            this.frame = frame;
            this.fill = fill;
        }
    }

}