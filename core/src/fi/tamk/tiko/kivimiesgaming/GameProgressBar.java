package fi.tamk.tiko.kivimiesgaming;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import java.util.ArrayList;

/**
 * Created by atter on 28-Apr-17.
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

        starsToNextThresholdLabel = new Label(Integer.toString(starsToNextUnlock(0) -
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
        if (progressBar.getFillAmount() >= thresholds.get(0).value) {
            reachedThreshold();
        }
        starsToNextThresholdLabel.setText(Integer.toString(starsToNextUnlock(count) -
                Vescape.lastTotalStars - count));
    }

    public void addTreshold(float threshold) {

        Threshold thresholdActor = new Threshold(assets.get("proggbar_marker.png", Texture.class)
                , progressBar.getHeight() * 1.2f, threshold);
        thresholdActor.setPosition(
                MathUtils.lerp(0, progressBar.getWidth(), threshold),
                0.05f * progressBar.getHeight());
        thresholdActor.setX(thresholdActor.getX() - thresholdActor.getSizeX() / 2);
        thresholds.add(thresholdActor);
        group.addActor(thresholdActor);
    }

    public void reachedThreshold() {
        final ImageActor threshold = thresholds.remove(0);
        threshold.addAction(Actions.sequence(
                Actions.moveBy(0, -threshold.getSizeY() / 5, 0.25f, Interpolation.pow2),
                Actions.moveBy(0, 3 * threshold.getSizeY() / 5, 1f, Interpolation.pow2)
        ));
    }

    private int starsToNextUnlock(int additional) {
        boolean first = true;
        int stars = 0;
        for (int i = 0; i < game.getMachineParts().length; ++i) {
            if (game.getMachineParts()[i].getStarsToUnlock() > Vescape.lastTotalStars + additional) {
                if (first) {
                    stars = game.getMachineParts()[i].getStarsToUnlock();
                    first = !first;
                }
                addTreshold((float)game.getMachineParts()[i].getStarsToUnlock() /
                        game.getMaxStars());
            }
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
        public ImageActor actor;

        public Threshold(Texture texture, float value) {
            super(texture);
            this.value = value;
        }

        public Threshold(Texture texture, float size, float value) {
            super(texture, size);
            this.value = value;
        }

    }
}
