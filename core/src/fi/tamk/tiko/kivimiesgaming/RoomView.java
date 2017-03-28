package fi.tamk.tiko.kivimiesgaming;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
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
    private Texture riddlePanelTexture;
    private TextField answerField;

    private int currentRiddle = 1;
    private int correctAnswers = 0;

    public RoomView(Vescape game, RoomData roomData) {
        super(game);
        this.roomData = roomData;
        for (Riddle r : roomData.riddles) {
            r.load();
        }
        riddlePanelTexture = new Texture("riddle_info_box_fill.png");
        bg = new ImageActor(roomData.getBackground(),
                Vescape.GUI_VIEWPORT_HEIGHT);
        bg.setX((Vescape.GUI_VIEWPORT_WIDTH - bg.getSizeX()) / 2);

        riddlePanel = createNewRiddle(roomData.getRandomRiddle());

        answerField = new TextField("", getGame().getTextFieldStyle());
        answerField.setPosition(0, 200);
        answerField.setSize(Vescape.GUI_VIEWPORT_WIDTH, 200);
        //Joku vitun popuppi
        // mones monestako arvotuksesta

        TextButton answerButton = new TextButton(getGame().getMyBundle().get("answerButton"),
                getGame().getTextButtonStyle());

        answerButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                answer(answerField.getText());
            }
        });
        answerButton.setSize(Vescape.GUI_VIEWPORT_WIDTH / 2, 175);
        answerButton.setPosition(Vescape.GUI_VIEWPORT_WIDTH / 2 - answerButton.getWidth() / 2,
                0);

        stage.addActor(bg);
        stage.addActor(riddlePanel);
        stage.addActor(answerField);
        stage.addActor(answerButton);
        burgerButton = new BurgerButton(this);
    }

    public void answer(String playerAnswer) {
        // check if answer is correct or not
        System.out.println(playerAnswer);
        if (currentRiddle != TOTAL_RIDDLES) {
            ++currentRiddle;
            //riddlePanel = createNewRiddle(roomData.getRandomRiddle());
        } else {
            //arvostelu!!
            //JOKU VITUN POPUP -> sieltä sit sinne päin
        }

        answerField.setDisabled(true);
        answerField.setText("");

        final float animLength = 0.85f;
        final Group oldRiddle = riddlePanel;
        oldRiddle.addAction(Actions.sequence(
                Actions.delay(0.1f),
                Actions.parallel(
                        Actions.scaleTo(0.7f, 0.7f, animLength),
                        Actions.moveBy(-1000f, 0, animLength, Interpolation.pow2),
                        Actions.rotateBy(90, animLength),
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                riddlePanel = createNewRiddle(roomData.getRandomRiddle());
                                riddlePanel.setRotation(-45);
                                riddlePanel.setScale(0.5f);
                                riddlePanel.setX(1000);
                                riddlePanel.addAction(Actions.parallel(
                                        Actions.scaleTo(1f, 1f, animLength, Interpolation.pow2),
                                        Actions.rotateBy(45, animLength, Interpolation.pow2),
                                        Actions.moveBy(-1000f, 0, animLength, Interpolation.pow2)));
                                getStage().addActor(riddlePanel);
                                burgerButton.reAddElementsToStage();
                            }
                        })
                ),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        oldRiddle.remove();
                        answerField.setDisabled(false);
                    }
                })
        ));

    }

    private Group createNewRiddle(Riddle currentRiddle) {
        ImageActor riddlePanelBg = new ImageActor(riddlePanelTexture);

        float panelTargetW = Vescape.GUI_VIEWPORT_WIDTH;
        riddlePanelBg.setSize(riddlePanelBg.getSizeY() * (panelTargetW / riddlePanelBg.getSizeX()));
        riddlePanelBg.setPosition((Vescape.GUI_VIEWPORT_WIDTH - riddlePanelBg.getSizeX()) / 2,
                (Vescape.GUI_VIEWPORT_HEIGHT - riddlePanelBg.getSizeY()) - 150);

        ImageActor riddleImage = new ImageActor(currentRiddle.image, riddlePanelBg.getSizeX() / 2);
        riddleImage.setPosition(
                riddlePanelBg.getX() + (riddlePanelBg.getSizeX() - riddleImage.getWidth()) / 2,
                riddlePanelBg.getY() + riddlePanelBg.getSizeY() - riddleImage.getHeight() - 25);

        Label.LabelStyle labelStyle = new Label.LabelStyle(getGame().getFontBig(),
                Color.BLACK);
        Label riddleLabel = new Label(
                currentRiddle.getRiddle(getGame().getMyBundle().getLocale().getLanguage()).riddle,
                labelStyle);
        riddleLabel.setPosition(
                riddlePanelBg.getX() + 15,
                riddleImage.getY() - riddleLabel.getHeight() - 25);

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
        riddlePanelTexture.dispose();
    }
}
