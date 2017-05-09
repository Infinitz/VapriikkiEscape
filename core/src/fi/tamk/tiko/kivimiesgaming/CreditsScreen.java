package fi.tamk.tiko.kivimiesgaming;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

/**
 * Created by atter on 08-May-17.
 */

public class CreditsScreen extends MyScreen {
    public CreditsScreen(Vescape game, AssetManager assetManager) {
        super(game, assetManager);
        assetManager.load("menu_logo.png", Texture.class);
    }

    @Override
    public void onStart() {
        ImageActor bg = new ImageActor(assetManager.get("MENU_bg.jpg", Texture.class),
                Vescape.GUI_VIEWPORT_HEIGHT);
        bg.setX((Vescape.GUI_VIEWPORT_WIDTH - bg.getSizeX()) / 2);
        stage.addActor(bg);

        Group creditsGroup = new Group();

        float x = 50;
        float innerX = 150;
        float y = 0;
        float space = 90f;

        ImageActor title = new ImageActor(assetManager.get("menu_logo.png", Texture.class), 300);
        title.setPosition(x, y);
        creditsGroup.addActor(title);
        y -= space * 2;

        Label.LabelStyle labelStyleSmall = new Label.LabelStyle(getGame().getRiddleFont(),
                Color.WHITE);

        Label.LabelStyle labelStyleBig = new Label.LabelStyle(getGame().getFontBig(),
                Color.WHITE);

        Label madeByLabel = new Label(game.getMyBundle().get("madeBy"), labelStyleBig);
        madeByLabel.setPosition(x, y);
        y -= space;
        creditsGroup.addActor(madeByLabel);

        Label kiviLabel = new Label("Kivimies Gaming", labelStyleSmall);
        kiviLabel.setPosition(innerX, y);
        y -= space * 2;
        creditsGroup.addActor(kiviLabel);

        Label graphicsLabel = new Label(game.getMyBundle().get("graphics"), labelStyleBig);
        graphicsLabel.setPosition(x, y);
        y -= space;
        creditsGroup.addActor(graphicsLabel);

        Label lauriLabel = new Label("Lauri Pirttimäki", labelStyleSmall);
        lauriLabel.setPosition(innerX, y);
        y -= space * 2;
        creditsGroup.addActor(lauriLabel);

        Label programmingLabel = new Label(game.getMyBundle().get("programming"), labelStyleBig);
        programmingLabel.setPosition(x, y);
        y -= space;
        creditsGroup.addActor(programmingLabel);

        Label atteLabel = new Label("Atte-Petteri Ronkanen", labelStyleSmall);
        atteLabel.setPosition(innerX, y);
        y -= space;
        creditsGroup.addActor(atteLabel);

        Label ristoLabel = new Label("Risto Pulkkinen", labelStyleSmall);
        ristoLabel.setPosition(innerX, y);
        y -= space * 2;
        creditsGroup.addActor(ristoLabel);

        Label riddleLabel = new Label(game.getMyBundle().get("riddleDesign"), labelStyleBig);
        riddleLabel.setPosition(x, y);
        y -= space;
        creditsGroup.addActor(riddleLabel);

        Label sofiaLabel = new Label("Sofia Pääkkönen", labelStyleSmall);
        sofiaLabel.setPosition(innerX, y);
        y -= space * 2;
        creditsGroup.addActor(sofiaLabel);

        Label musicLabel = new Label(game.getMyBundle().get("music"), labelStyleBig);
        musicLabel.setPosition(x, y);
        y -= space;
        creditsGroup.addActor(musicLabel);

        Label jayLabel = new Label("\"Magical Night\" by Jay Man\nwww.ourmusicbox.com", labelStyleSmall);
        y -= jayLabel.getHeight() / 2;
        jayLabel.setPosition(innerX, y);
        y -= space * 2;
        creditsGroup.addActor(jayLabel);


        Label specialThanksLabel = new Label(game.getMyBundle().get("specialThanks"), labelStyleBig);
        specialThanksLabel.setPosition(x, y);
        y -= space;
        creditsGroup.addActor(specialThanksLabel);

        Label vapriikkiLabel = new Label("Vapriikki", labelStyleSmall);
        vapriikkiLabel.setPosition(innerX, y);
        y -= space;
        creditsGroup.addActor(vapriikkiLabel);

        Label sallaLabel = new Label("Sallamari Angeria", labelStyleSmall);
        sallaLabel.setPosition(innerX, y);
        y -= space;
        creditsGroup.addActor(sallaLabel);

        Label tikoLabel = new Label("16tiko4", labelStyleSmall);
        tikoLabel.setPosition(innerX, y);
        y -= space;
        creditsGroup.addActor(tikoLabel);

        Label testUserLabel = new Label(game.getMyBundle().get("testUsers"), labelStyleSmall);
        testUserLabel.setPosition(innerX, y);
        creditsGroup.addActor(testUserLabel);

        stage.addActor(creditsGroup);
        creditsGroup.setY(-title.getSizeY());
        creditsGroup.addAction(Actions.sequence(
                Actions.moveBy(0, -1 * y + Vescape.GUI_VIEWPORT_HEIGHT * 1.25f, 20),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        setNextScreen(new MainMenu(game, assetManager));
                    }
                })
        ));
    }

    @Override
    protected void update(float dt) {
        if (nextScreen != null)
            return;

        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK) ||
                Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            setNextScreen(new MainMenu(game, assetManager));
        }
    }
    @Override
    public TextButton getPanelButton1() {
        return null;
    }

    @Override
    public TextButton getPanelButton2() {
        return null;
    }

    @Override
    public void dispose() {
        assetManager.unload("menu_logo.png");
        super.dispose();
    }

}
