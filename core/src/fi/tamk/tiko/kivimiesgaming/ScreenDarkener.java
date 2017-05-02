package fi.tamk.tiko.kivimiesgaming;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

/**
 * Created by atter on 11-Apr-17.
 */

public class ScreenDarkener extends ImageActor {

    public ScreenDarkener(Texture texture) {
        super(texture, Vescape.GUI_VIEWPORT_HEIGHT);
        setTouchable(Touchable.enabled);
        alpha = 0;

        setX(Vescape.GUI_VIEWPORT_WIDTH);
    }

    public void enable(boolean enabled) {
        if (enabled) {
            setPosition(getX() - Vescape.GUI_VIEWPORT_WIDTH,
                    0);
            addAction(Actions.repeat(10, Actions.run(new Runnable() {
                        @Override
                        public void run() {
                            alpha += 0.08f;
                        }
                    })
            ));
        } else {
            addAction(Actions.sequence(
                    Actions.repeat(10, Actions.run(new Runnable() {
                        @Override
                        public void run() {
                            alpha -= 0.08f;
                        }
                    })),
                    Actions.run(new Runnable() {
                        @Override
                        public void run() {
                            setPosition(getX() + Vescape.GUI_VIEWPORT_WIDTH,
                                    0);
                        }
                    })));
        }

    }
}
