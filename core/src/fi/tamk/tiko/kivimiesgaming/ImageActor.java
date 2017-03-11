package fi.tamk.tiko.kivimiesgaming;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

/**
 * Created by atter on 04-Mar-17.
 */

public class ImageActor extends Actor {
    private ChangeListener listener;

    private TextureRegion textureRegion;
    private Texture tex;
    private float aspectRatio = 1.0f;
    private float size = 1.0f;

    private boolean isTouched = false;


    public ImageActor (Texture texture) {
        this(texture, texture.getHeight());
    }

    public ImageActor (Texture texture, float size) {
        setTex(texture);
        setSize(size);

        addListener(new InputListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y,
                                int pointer, int button) {

            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                if (listener != null && ImageActor.this != null)
                    listener.changed(null, ImageActor.this);

                return true;
            }
        });
    }

    public void setTex(Texture tex) {
        this.tex = tex;
        setTextureRegion(new TextureRegion(tex));
    }

    public void setTextureRegion(TextureRegion texReg) {
        setAspectRatio((float)(texReg.getRegionWidth()) / texReg.getRegionHeight());
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
        //this.setSize(size * aspectRatio, size);
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
        batch.draw(textureRegion, getX(), getY(),
                getSizeX() / 2,
                getSizeY() / 2,
                getSizeX(), getSizeY(), 1, 1,
                getRotation());
    }

    public void setClickListener(ChangeListener listener) {
        this.listener = listener;
    }

}
