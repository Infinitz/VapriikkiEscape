package fi.tamk.tiko.kivimiesgaming;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

/**
 * @author Atte-Petteri Ronkanen, Risto Pulkkinen
 */
public class StartScreen extends MyScreen {
    public StartScreen(Vescape game, AssetManager assetManager) {
        super(game, assetManager);
    }

    @Override
    public void onStart() {
        ImageActor bg = new ImageActor(assetManager.get("MENU_bg.jpg", Texture.class),
                Vescape.GUI_VIEWPORT_HEIGHT);
        bg.setX((Vescape.GUI_VIEWPORT_WIDTH - bg.getSizeX()) / 2);

        stage.addActor(bg);
        setNextScreen(new MainMenu(game, assetManager));
    }

    @Override
    public TextButton getPanelButton1() {
        return null;
    }

    @Override
    public TextButton getPanelButton2() {
        return null;
    }

}
