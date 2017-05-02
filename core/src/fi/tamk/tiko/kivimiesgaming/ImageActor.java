package fi.tamk.tiko.kivimiesgaming;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;

/**
 * Created by atter on 04-Mar-17.
 */

public class ImageActor extends Actor {

    public boolean renderEnabled = true;
    public float alpha = 1.0f;

    private ChangeListener listener;

    private TextureRegion textureRegion;
    private Texture tex;

    private float aspectRatio = 1.0f;
    private float size = 1.0f;

    public ImageActor(Texture texture) {
        this(texture, texture.getHeight());
    }

    public ImageActor(Texture texture, float size) {
        setTex(texture);
        setSize(size);

        addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y,
                                int pointer, int button) {
                ImageActor thisActor = ImageActor.this;
                if (listener != null && thisActor == thisActor.
                        getStage().hit(x + thisActor.getTrueX(), y + thisActor.getTrueY(), true)) {
                    listener.changed(null, ImageActor.this);
                }
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                return true;
            }
        });
        setTouchable(Touchable.disabled);
    }

    public void setTex(Texture tex) {
        this.tex = tex;
        setTextureRegion(new TextureRegion(tex));
    }

    public void setTextureRegion(TextureRegion texReg) {
        setAspectRatio((float) (texReg.getRegionWidth()) / texReg.getRegionHeight());
        textureRegion = texReg;
    }

    public Texture getTex() {
        return tex;
    }

    public float getAspectRatio() {
        return aspectRatio;
    }

    public float getSize() {
        return size;
    }

    public float getSizeX() {
        return size * aspectRatio;
    }

    public float getSizeY() {
        return size;
    }

    public void setSize(float size) {
        if (size <= 0) {
            throw new IllegalArgumentException();
        }
        float oldSize = this.size;
        this.size = size;
        onSizeChange(oldSize);
    }

    private void onSizeChange(float oldSize) {
        setBounds(getX(), getY(), getSizeX(), getSizeY());
        setSize(getSizeX(), getSizeY());
    }


    public void setAspectRatio(float aspectRatio) {
        if (aspectRatio <= 0) {
            throw new IllegalArgumentException();
        }
        if (this.aspectRatio != aspectRatio) {
            this.aspectRatio = aspectRatio;
            onSizeChange(getSize());

            setSize(getHeight() * aspectRatio, getHeight());
        }
    }

    @Override
    public void draw(Batch batch, float alpha) {
        if (renderEnabled) {

            Color c = batch.getColor();
            batch.setColor(getColor().r * c.r, getColor().g * c.g, getColor().b * c.b,
                    getColor().a * c.a * this.alpha);
            batch.draw(textureRegion, getX(), getY(),
                    getSizeX() / 2,
                    getSizeY() / 2,
                    getSizeX(), getSizeY(), getScaleX(), getScaleY(),
                    getRotation());
            batch.setColor(c.r, c.g, c.b, 1);

        }
    }

    public float getTrueX() {
        Actor temp = this;
        float coordX = super.getX();
        while (temp.getParent() != null) {
            coordX += temp.getParent().getX();
            temp = temp.getParent();
        }
        return coordX;
    }

    public float getTrueY() {
        Actor temp = this;
        float coordY = super.getY();
        while (temp.getParent() != null) {
            coordY += temp.getParent().getY();
            temp = temp.getParent();
        }
        return coordY;
    }

    public void setClickListener(ChangeListener listener) {
        this.listener = listener;
        setTouchable(Touchable.enabled);
    }
}
