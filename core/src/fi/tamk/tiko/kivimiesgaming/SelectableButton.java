package fi.tamk.tiko.kivimiesgaming;

import com.badlogic.gdx.graphics.Texture;

/**
 * Created by atter on 11-Mar-17.
 */

public class SelectableButton extends ImageActor {

    protected boolean selected = false;
    private Texture otherTex;

    public SelectableButton(Texture texture, Texture selected) {
        this(texture, selected, texture.getHeight());
    }

    public SelectableButton(Texture texture, Texture selected, float size) {
        super(texture, size);
        otherTex = selected;
    }

    public void setSelected(boolean selected) {
        if (this.selected == selected)
            return;

        this.selected = selected;
        onSelect(this.selected);
        //swawpTextures
        Texture temp = getTex();
        setTex(otherTex);
        otherTex = temp;
    }

    public void select() {
        this.selected = !this.selected;
        onSelect(this.selected);
        //swawpTextures
        Texture temp = getTex();
        setTex(otherTex);
        otherTex = temp;
    }

    public boolean isSelected() {
        return selected;
    }

    protected void onSelect(boolean selected) {

    }
}
