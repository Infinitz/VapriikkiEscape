package fi.tamk.tiko.kivimiesgaming;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import java.util.ArrayList;

/**
 * @author Atte-Petteri Ronkanen, Risto Pulkkinen
 */

public class GameProgressBar {

    private AssetManager assets;
    private Vescape game;
    private Group group;
    private ProgressBar progressBar;
    private Label starsToNextThresholdLabel;
    private ImageActor starImage;

    private ArrayList<Threshold> thresholds;

    private float x, y;

    private float stepInFill;

    public GameProgressBar(float x, float y, float size, AssetManager assets, Vescape game) {
        this.assets = assets;
        this.game = game;
        this.x = x;
        this.y = y;
        stepInFill = 1f / game.getMaxStars();
        thresholds = new ArrayList<Threshold>();
        group = new Group();

        ProgressBar.ProgressBarStyle progressBarStyle =
                new ProgressBar.ProgressBarStyle(
                        assets.get("proggbar_back.png", Texture.class),
                        assets.get("proggbar_border.png", Texture.class),
                        assets.get("proggbar_fill.png", Texture.class));

        progressBar =
                new ProgressBar(size, (float)Vescape.lastTotalStars / game.getMaxStars(),
                        progressBarStyle);

        ImageActor[] pBarActors = progressBar.getActors();
        for (int i = 0; i < pBarActors.length; ++i) {
            group.addActor(pBarActors[i]);
        }

        starImage = new ImageActor(assets.get("star_full.png", Texture.class),
                120);

        starImage.setPosition(progressBar.getWidth(), 0);

        starsToNextThresholdLabel = new Label(Integer.toString(starsToNextUnlock(0, true) -
                Vescape.lastTotalStars),
                new Label.LabelStyle(game.getFontBig(),
                        new Color(0.3f, 0.3f, 0.3f, 0.7f)));
        starsToNextThresholdLabel.setPosition(progressBar.getWidth() + starImage.getSizeX() * 0.3f,
                0.1f * starImage.getSizeY());

        group.addActor(starImage);
        group.addActor(starsToNextThresholdLabel);

        group.setPosition(x, y);
    }

    public void animatedStepProgress(int count) {
        progressBar.setFill(progressBar.getFillAmount() + count * stepInFill);
        starsToNextThresholdLabel.setText(Integer.toString(starsToNextUnlock(count, false) -
                Vescape.lastTotalStars - count));

        if (thresholds.isEmpty())
            return;

        if (progressBar.getFillAmount() >= thresholds.get(0).value ||
                Math.abs(progressBar.getFillAmount() - thresholds.get(0).value) < 0.00001f) {
            reachedThreshold();
        }
    }

    public void addTreshold(float threshold) {

        Threshold thresholdActor = new Threshold(
                assets.get("proggbar_marker_closed.png", Texture.class),
                progressBar.getHeight(), threshold);
        thresholdActor.setPosition(
                MathUtils.lerp(0, progressBar.getWidth(), threshold),
                0);
        thresholdActor.setX(thresholdActor.getX() - thresholdActor.getSizeX() / 2);
        thresholds.add(thresholdActor);
        group.addActor(thresholdActor);
    }

    public void reachedThreshold() {
        final Threshold threshold = thresholds.remove(0);

        threshold.unlockAnimation(group,
                assets.get("proggbar_marker_open_upper.png", Texture.class),
                assets.get("proggbar_marker_open_lower.png", Texture.class));
    }

    private int starsToNextUnlock(int additional, boolean addThresholds) {
        boolean first = true;
        int stars = 0;
        for (int i = 0; i < game.getMachineParts().length; ++i) {
            if (game.getMachineParts()[i].getStarsToUnlock() > Vescape.lastTotalStars + additional) {
                if (first) {
                    stars = game.getMachineParts()[i].getStarsToUnlock();
                    first = !first;
                }
                if (addThresholds) {
                    addTreshold((float)game.getMachineParts()[i].getStarsToUnlock() /
                            game.getMaxStars());
                }
            }
        }
        if (first) {
            return game.getMaxStars();
        }
        return stars;
    }
    public Group getGroup() {
        return group;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float x) {
        this.x = x;
        group.setX(x);
    }

    public void setY(float y) {
        this.y = y;
        group.setY(y);
    }

    public float getWidth() {
        return progressBar.getWidth();
    }

    public float getHeight() {
        return progressBar.getHeight();
    }


    private class Threshold extends ImageActor{
        public float value;

        public Threshold(Texture tex, float size, float value) {
            super(tex, size);
            this.value = value;
        }


        public void unlockAnimation(Group group, Texture top, Texture bottom) {
            ImageActor topActor = new ImageActor(top,
                    1.15f * getSizeY() / 2 * top.getHeight() / bottom.getHeight());

            ImageActor bottomActor = new ImageActor(bottom,
                    1.15f * getSizeY() / 2);

            remove();

            float xOffset = (bottomActor.getSizeX() - getSizeX()) / 2;
            float yOffset = ((topActor.getSizeY() + bottomActor.getSizeY()) - getSizeY()) / 2;
            bottomActor.setPosition(getX() - xOffset, getY() - yOffset);
            topActor.setPosition(getX() - xOffset + bottomActor.getSizeX() / 4,
                    bottomActor.getY() + bottomActor.getSizeY());


            group.addActor(topActor);
            group.addActor(bottomActor);

            topActor.addAction(Actions.sequence(
                   // Actions.moveBy(0, -getSizeY() / 50, 0.1f, Interpolation.pow2),
                    Actions.parallel(
                            Actions.moveBy(0, getSizeY() / 6, 0.7f, Interpolation.pow2)
                            //, Actions.scaleTo(1.25f, 1.25f, 0.7f, Interpolation.pow2)
                    )

            ));
            bottomActor.addAction(Actions.sequence(
                   // Actions.moveBy(0, getSizeY() / 50, 0.1f, Interpolation.pow2),
                    Actions.parallel(
                            Actions.moveBy(0, -getSizeY() / 6, 0.7f, Interpolation.pow2)
                            //, Actions.scaleTo(1.25f, 1.25f, 0.7f, Interpolation.pow2)
                    )
            ));


        }
    }
}
