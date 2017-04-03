package fi.tamk.tiko.kivimiesgaming;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.particles.influencers.ColorInfluencer;
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
    private Riddle currentRiddle;

    private BurgerButton burgerButton;
    private ImageActor bg;
    private Group riddlePanel;
    private TextField answerField;
    private TextButton answerButton;
    private ImageActor[] answerResults;

    private Texture riddlePanelTexture;
    private Texture riddlePanelBorderTexture;

    private Texture emptyAnswerTex;
    private Texture wrongAnswerTex;
    private Texture correctAnswerTex;
    private Texture perfectAnswerTex;

    private boolean hintUsed = false;

    private int currentRiddleCount = 0;
    private int correctAnswerCount = 0;
    private int hintsUsedCount = 0;

    public RoomView(Vescape game, RoomData roomData) {
        super(game);
        this.roomData = roomData;

        riddlePanelTexture = new Texture("riddle_info_box_fill.png");
        riddlePanelBorderTexture = new Texture("riddle_info_box_border.png");

        emptyAnswerTex = new Texture("riddle_slot_empty.png");
        wrongAnswerTex = new Texture("riddle_slot_nope.png");
        correctAnswerTex = new Texture("riddle_slot_done.png");
        perfectAnswerTex = new Texture("riddle_slot_done_golden.png");

    }

    @Override
    public void onStart() {
        roomData.loadRiddles();

        bg = new ImageActor(roomData.getBackground(),
                Vescape.GUI_VIEWPORT_HEIGHT);
        bg.setX((Vescape.GUI_VIEWPORT_WIDTH - bg.getSizeX()) / 2);

        riddlePanel = createNewRiddlePanel();

        answerField = new TextField("", getGame().getTextFieldStyle());
        answerField.setPosition(0, 200);
        answerField.setSize(Vescape.GUI_VIEWPORT_WIDTH, 200);
        answerField.setOnscreenKeyboard(new TextField.OnscreenKeyboard() {
            @Override
            public void show(boolean visible) {

                Gdx.input.getTextInput(new Input.TextInputListener() {
                    @Override
                    public void input(String text) {
                        answerField.setText(text);
                    }

                    @Override
                    public void canceled() {

                    }
                }, getGame().getMyBundle().get("answer"), answerField.getText(),
                        answerField.getText().length() == 0 ?
                                getGame().getMyBundle().get("answer") : "");
            }
        });

        answerResults = new ImageActor[TOTAL_RIDDLES];

        float answerSlotSpace = 20;
        for (int i = 0; i < answerResults.length; ++i) {
            answerResults[i] = new ImageActor(emptyAnswerTex, 100);
            float centerX = Vescape.GUI_VIEWPORT_WIDTH / 2 - answerResults[i].getSizeX() / 2;
            float x = centerX +
                    (-(answerResults.length / 2f) + 0.5f + i) * (answerResults[i].getSizeX()
                            + answerSlotSpace);

            float y = Vescape.GUI_VIEWPORT_HEIGHT - answerResults[i].getSizeY() - 10;
            answerResults[i].setPosition(x, y);
        }

        answerButton = new TextButton(getGame().getMyBundle().get("answerButton"),
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
        for (int i = 0; i < answerResults.length; ++i) {
            stage.addActor(answerResults[i]);
        }
        stage.addActor(riddlePanel);
        stage.addActor(answerField);
        stage.addActor(answerButton);
        burgerButton = new BurgerButton(this);
    }

    public void answer(String playerAnswer) {
        boolean correctAnswer = currentRiddle.getRiddle(
                getGame().getMyBundle().getLocale().getLanguage()).isCorrectAnswer(playerAnswer);

        ImageActor answerResult = null;
        if (correctAnswer) {
            correctAnswerCount++;
            if (hintUsed) {
                answerResult = new ImageActor(correctAnswerTex, answerResults[0].getSizeY());
            } else {
                answerResult = new ImageActor(perfectAnswerTex, answerResults[0].getSizeY());
            }
        } else if (!hintUsed){
            hintUsed = true;
            hintsUsedCount++;
            //Open hint popup!
            return;
        } else {
            answerResult = new ImageActor(wrongAnswerTex, answerResults[0].getSizeY());
        }
        if (answerResult != null) {
            answerField.setDisabled(true);
            answerField.setText("");
            answerButton.setDisabled(true);

            answerResult.setPosition(Vescape.GUI_VIEWPORT_WIDTH / 2,
                    3 * Vescape.GUI_VIEWPORT_HEIGHT / 2);
            answerResult.setScale(5.5f);
            getStage().addActor(answerResult);
            answerResult.addAction(Actions.sequence(
                    Actions.parallel(
                            Actions.moveTo(
                                    Vescape.GUI_VIEWPORT_WIDTH / 2 - answerResult.getSizeX() / 2,
                                    Vescape.GUI_VIEWPORT_HEIGHT / 2 -
                                            answerResult.getSizeY() / 2, 0.45f,
                                    Interpolation.pow2),
                            Actions.scaleTo(3, 3, 0.6f, Interpolation.bounceOut),
                            Actions.rotateTo(((float)Math.random() - 0.5f) * 40f)
                    ),
                    Actions.delay(0.25f),

                    Actions.run(new Runnable() {
                        @Override
                        public void run() {
                            nextRiddle();
                        }
                    }),
                    Actions.parallel(
                            Actions.moveTo(answerResults[currentRiddleCount].getX(),
                                    answerResults[currentRiddleCount].getY(),
                                    0.75f, Interpolation.pow2),
                            Actions.scaleTo(1, 1, 0.5f),
                            Actions.rotateTo(0, 0.5f)
                    )
            ));
        }
    }

    private void nextRiddle() {
        hintUsed = false;

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
                                if (currentRiddleCount == TOTAL_RIDDLES - 1) {
                                    return;
                                }
                                riddlePanel = createNewRiddlePanel();
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
                        answerButton.setDisabled(false);
                        if (currentRiddleCount == TOTAL_RIDDLES - 1) {
                            roomCompleted();
                        }
                        ++currentRiddleCount;
                    }
                })
        ));
    }

    private Group createNewRiddlePanel() {
        currentRiddle = roomData.getRandomRiddle();

        ImageActor riddlePanelBg = new ImageActor(riddlePanelTexture);

        float panelTargetW = Vescape.GUI_VIEWPORT_WIDTH;
        riddlePanelBg.setSize(riddlePanelBg.getSizeY() * (panelTargetW / riddlePanelBg.getSizeX()));
        riddlePanelBg.setPosition((Vescape.GUI_VIEWPORT_WIDTH - riddlePanelBg.getSizeX()) / 2,
                (Vescape.GUI_VIEWPORT_HEIGHT - riddlePanelBg.getSizeY()) - 150);

        ImageActor riddlePanelBorder =
                new ImageActor(riddlePanelBorderTexture, riddlePanelBg.getSizeY());
        riddlePanelBorder.setPosition(riddlePanelBg.getX(), riddlePanelBg.getY());

        ImageActor riddleImage = new ImageActor(currentRiddle.image,
                2 * riddlePanelBg.getSizeX() / 3);

        riddleImage.setPosition(
                riddlePanelBg.getX() + (riddlePanelBg.getSizeX() - riddleImage.getWidth()) / 2,
                riddlePanelBg.getY() + riddlePanelBg.getSizeY() - riddleImage.getHeight() - 25);

        Label.LabelStyle labelStyle = new Label.LabelStyle(getGame().getRiddleFont(),
                Color.BLACK);
        Label riddleLabel = new Label(
                currentRiddle.getRiddle(getGame().getMyBundle().getLocale().getLanguage()).riddle,
                labelStyle);
        riddleLabel.setPosition(
                riddlePanelBg.getX() + 30,
                riddleImage.getY() - riddleLabel.getHeight() - 25);

        Group g = new Group();
        g.addActor(riddlePanelBg);
        g.addActor(riddleImage);
        g.addActor(riddlePanelBorder);
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
        roomData.disposeRiddleImages();
        riddlePanelTexture.dispose();
        riddlePanelBorderTexture.dispose();
    }

    private void roomCompleted() {
        int score = correctAnswerCount;
        if (score == 3 && hintsUsedCount > 0) {
            --score;
        }
        roomData.latestScore = score;
        if (roomData.latestScore > roomData.highscore) {
            roomData.highscore = roomData.latestScore;
        }

        new RoomFinishedPopUp(this, roomData);
    }
}
