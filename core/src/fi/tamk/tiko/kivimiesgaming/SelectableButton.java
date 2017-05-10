package fi.tamk.tiko.kivimiesgaming;

import com.badlogic.gdx.graphics.Texture;

/**
 * @author Atte-Petteri Ronkanen, Risto Pulkkinen
 *
 * Used for controlling selectable buttons.
 */

public class SelectableButton extends ImageActor {

    /**
     * Checks if the button is selected.
     */
    protected boolean selected = false;

    /**
     * Different texture for selected button.
     */
    private Texture otherTex;

    /**
     * Class constructor.
     *
     * @param texture  Texture for the button.
     * @param selected Texture for the button when it is selected.
     * @param size     Size of the button.
     */
    public SelectableButton(Texture texture, Texture selected, float size) {
        super(texture, size);
        otherTex = selected;
    }

    /**
     * Sets the button selected.
     *
     * @param selected Instant return if the button is selected.
     */
    public void setSelected(boolean selected) {
        if (this.selected == selected)
            return;

        this.selected = selected;
        onSelect(this.selected);

        if (otherTex != null) {
            Texture temp = getTex();
            setTex(otherTex);
            otherTex = temp;
        }
    }

    /**
     * Checks if the button is selected.
     *
     * @return Returns true if the button is selected.
     */

    public boolean isSelected() {
        return selected;
    }

    /**
     * Is called when the button is selected.
     *
     * @param selected True if the button is selected.
     */
    protected void onSelect(boolean selected) {

    }
}
