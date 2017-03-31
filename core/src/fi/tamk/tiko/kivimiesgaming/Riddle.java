package fi.tamk.tiko.kivimiesgaming;

import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;

/**
 * Created by atter on 28-Mar-17.
 */

public class Riddle {

    private ArrayList<RiddleTexts> riddleTexts;
    public String imagePath;

    public Texture image;

    public Riddle(String imagePath) {
        this.imagePath = Vescape.RIDDLE_IMAGES_PATH + imagePath;
        riddleTexts = new ArrayList<RiddleTexts>();
    }

    public void addRiddleText(RiddleTexts riddleText) {
        riddleTexts.add(riddleText);
    }

    public RiddleTexts getRiddle(String language) {
        for (int i = 0; i < riddleTexts.size(); ++i) {
            if (language == riddleTexts.get(i).language)
                return riddleTexts.get(i);
        }
        return riddleTexts.get(0);
    }

    public void load() {
        image = new Texture(imagePath);
    }

    public void dispose() {
        image.dispose();
        image = null;
    }

    /*
    @Override
    public String toString() {
        String temp = imagePath + "\n";

        for (int i = 0; i < riddleTexts.size(); ++i) {
            temp += riddleTexts.get(i).language + riddleTexts.get(i).riddle +
                    riddleTexts.get(i).answer + "\n";
        }

        return temp;
    }
    */
}
