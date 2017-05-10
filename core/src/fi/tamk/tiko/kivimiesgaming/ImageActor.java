package fi.tamk.tiko.kivimiesgaming;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

/**
 * @author Atte-Petteri Ronkanen, Risto Pulkkinen
 *
 * Class for using basic actors
 */

public class ImageActor extends Actor {

    /**
     * Is rendering enabled
     */
    public boolean renderEnabled = true;

    /**
     * Additional alpha value
     */
    public float alpha = 1.0f;

    /**
     * If the origin is center or bottom left corner
     */
    public boolean originCenter = true;

    /**
     * listener used for click events
     */
    private ChangeListener listener;

    /**
     * Texture region of the actor
     */
    private TextureRegion textureRegion;

    /**
     * Texture of the actor
     */
    private Texture tex;

    /**
     * aspect ratio of current texture
     */
    private float aspectRatio = 1.0f;

    /**
     * Height of the image actor
     */
    private float size = 1.0f;

    /**
     * Contructor for the actor
     *
     * @param texture Texture of the actor
     */
    public ImageActor(Texture texture) {
        this(texture, texture.getHeight());
    }

    /**
     * Contructor for the actor
     *
     * @param texture Texture of the actor
     * @param size Height of the image actor
     */
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

    /**
     * Setter for actor's texture
     *
     * @param tex Texture that is set to the actor
     */
    public void setTex(Texture tex) {
        if (tex == null)
            return;

        this.tex = tex;
        setTextureRegion(new TextureRegion(tex));
    }

    /**
     * Setter for actor's texture region
     *
     * @param texReg Texture region that is set to the actor
     */
    public void setTextureRegion(TextureRegion texReg) {
        setAspectRatio((float) (texReg.getRegionWidth()) / texReg.getRegionHeight());
        textureRegion = texReg;
    }

    /**
     * Getter for texture
     *
     * @return texture
     */
    public Texture getTex() {
        return tex;
    }

    /**
     * Getter for size
     *
     * @return size
     */
    public float getSize() {
        return size;
    }

    /**
     * Getter for size X
     *
     * @return size X
     */
    public float getSizeX() {
        return size * aspectRatio;
    }

    /**
     * Getter for size Y
     *
     * @return size Y
     */
    public float getSizeY() {
        return size;
    }

    /**
     * Setter for actor's height
     *
     * @param size New height for the actor
     */
    public void setSize(float size) {
        if (size <= 0) {
            throw new IllegalArgumentException();
        }
        float oldSize = this.size;
        this.size = size;
        onSizeChange(oldSize);
    }

    /**
     * Is called when the size is changed
     *
     * @param oldSize Previous size of the actor
     */
    private void onSizeChange(float oldSize) {
        setBounds(getX(), getY(), getSizeX(), getSizeY());
        setSize(getSizeX(), getSizeY());
    }

    /**
     * Setter for aspect ration
     *
     * @param aspectRatio New aspect ratio of the actor
     */
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
            batch.draw(textureRegion, getX(), getY(),
                    oX,
                    oY,
                    getSizeX(), getSizeY(), getScaleX(), getScaleY(),
                    getRotation());
            batch.setColor(c.r, c.g, c.b, 1);

        }
    }

    /**
     * Returns actors true x coordinate which is not relative to its parents
     *
     * @return X coordinate which is not relative to its parents
     */
    public float getTrueX() {
        Actor temp = this;
        float coordX = getX();
        while (temp.getParent() != null) {
            coordX += temp.getParent().getX();
            temp = temp.getParent();
        }
        return coordX;
    }

    /**
     * Returns actors true y coordinate which is not relative to its parents
     *
     * @return Y coordinate which is not relative to its parents
     */
    public float getTrueY() {
        Actor temp = this;
        float coordY = getY();
        while (temp.getParent() != null) {
            coordY += temp.getParent().getY();
            temp = temp.getParent();
        }
        return coordY;
    }

    /**
     * Sets click listener for the actor
     *
     * @param listener Click listener for the actor
     */
    public void setClickListener(ChangeListener listener) {
        this.listener = listener;
        setTouchable(Touchable.enabled);
    }
}
