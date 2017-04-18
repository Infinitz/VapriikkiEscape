package fi.tamk.tiko.kivimiesgaming;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;

/**
 * Created by atter on 18-Apr-17.
 */

public class ProgressBar {

    private ImageActor bg;
    private ImageActor fill;
    private ImageActor frame;

    private Group group;

    private float fillAmount;

    public ProgressBar(float x, float y, float size, float fillAmount) {
        //bg = new ImageActor(, size);
        //fill = new ImageActor();
        //frame = new ImageActor(, size);

        fill.setSize(size / 2);
        fill.setPosition(15, size / 4);
        group.addActor(bg);
        group.addActor(fill);
        group.addActor(frame);
        group.setPosition(x, y);
    }

    public void setFill(float fill) {
        this.fillAmount = fill;
        updateFill();
    }
    private void updateFill() {
        fill.setScaleX(MathUtils.lerp(0, 1, fillAmount));
    }
}