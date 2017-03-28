package fi.tamk.tiko.kivimiesgaming;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

/**
 * Created by atter on 22-Mar-17.
 */

public class RoomView extends MyScreen {
    private static final int TOTAL_RIDDLES = 3;
    private RoomData roomData;
    private BurgerButton burgerButton;
    private ImageActor bg;
    private Group riddlePanel;

    private int currentRiddle = 1;
    private int correctAnswers = 0;

    public RoomView(Vescape game, RoomData roomData) {
        super(game);
        this.roomData = roomData;
        for (Riddle r : roomData.riddles) {
            r.load();
        }

        bg = new ImageActor(roomData.getBackground(),
                Vescape.GUI_VIEWPORT_HEIGHT);
        bg.setX((Vescape.GUI_VIEWPORT_WIDTH - bg.getSizeX()) / 2);

        riddlePanel = createNewRiddle(roomData.getRandomRiddle());

        TextField textField = new TextField("", getGame().getTextFieldStyle());
       //( textField.
        //ImageActor riddleImage;
        //Arvotus tila
            //teksti
            //kuva
        // vastaus tila
        //Vastaa nappi
        //Joku vitun popuppi
        // mones monestako arvotuksesta

        stage.addActor(bg);
        stage.addActor(riddlePanel);
        burgerButton = new BurgerButton(this);
    }

    public void answer(String playerAnswer) {
        // check if answer is correct or not

        if (currentRiddle != TOTAL_RIDDLES) {
            ++currentRiddle;
            riddlePanel = createNewRiddle(roomData.getRandomRiddle());
        } else {
            //arvostelu!!
            //JOKU VITUN POPUP -> sieltä sit sinne päin
        }
    }

    private Group createNewRiddle(Riddle currentRiddle) {
        ImageActor riddlePanelBg = new ImageActor(new Texture("riddle_info_box_fill.png"));

        float panelTargetW = Vescape.GUI_VIEWPORT_WIDTH;
        riddlePanelBg.setSize(riddlePanelBg.getSizeY() * (panelTargetW / riddlePanelBg.getSizeX()));
        riddlePanelBg.setPosition((Vescape.GUI_VIEWPORT_WIDTH - riddlePanelBg.getSizeX()) / 2,
                (Vescape.GUI_VIEWPORT_HEIGHT - riddlePanelBg.getSizeY()) - 150);

        ImageActor riddleImage = new ImageActor(currentRiddle.image, riddlePanelBg.getSizeX() / 2);
        riddleImage.setPosition(riddlePanelBg.getX() +
                (riddlePanelBg.getSizeX() - riddleImage.getWidth()) / 2,
                riddlePanelBg.getY() + riddlePanelBg.getSizeY() - riddleImage.getHeight() - 25);

        Label.LabelStyle labelStyle = new Label.LabelStyle(getGame().getFontBig(),
                Color.BLACK);
        Label riddleLabel = new Label(
                currentRiddle.getRiddle(getGame().getMyBundle().getLocale().getLanguage()).riddle,
                labelStyle);
        riddleLabel.setPosition(riddlePanelBg.getX() + (riddlePanelBg.getSizeX() - riddleLabel.getWidth()) / 2,
                riddlePanelBg.getY() + riddlePanelBg.getSizeY() - riddleLabel.getHeight() - 25);

        Group g = new Group();
        g.addActor(riddlePanelBg);
        g.addActor(riddleImage);
        g.addActor(riddleLabel);

        return g;
    }

    @Override
    protected void update(float dt) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK) ||
                Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {

            burgerButton.togglePanel();

        }
    }

    @Override
    public TextButton getPanelButton1() {
        String name = getGame().getMyBundle().get("continueButton");

        TextButton button = new TextButton(name,
                getGame().getTextButtonStyle());

        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                burgerButton.togglePanel();
            }
        });

        return button;
    }

    @Override
    public TextButton getPanelButton2() {
        String name = getGame().getMyBundle().get("exitRoomButton");

        TextButton button = new TextButton(name,
                getGame().getTextButtonStyle());

        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                getGame().setScreen(new RoomSelection(getGame()));
            }
        });

        return button;
    }

    @Override
    public void dispose() {
        super.dispose();
        for (Riddle r : roomData.riddles) {
            r.dispose();
        }
    }
}
