package fi.tamk.tiko.kivimiesgaming;

import java.util.ArrayList;

/**
 * @author Atte-Petteri Ronkanen, Risto Pulkkinen
 */

public class Riddle {

    private ArrayList<RiddleTexts> riddleTexts;
    public String imagePath;

    public Riddle(String imagePath) {
        this.imagePath = Vescape.RIDDLE_IMAGES_PATH + imagePath;
        riddleTexts = new ArrayList<RiddleTexts>();
    }

    public void addRiddleText(RiddleTexts riddleText) {
        riddleTexts.add(riddleText);
    }

    public RiddleTexts getRiddle(String language) {
        for (int i = 0; i < riddleTexts.size(); ++i) {
            if (language.equalsIgnoreCase(riddleTexts.get(i).language))
                return riddleTexts.get(i);
        }
        return riddleTexts.get(0);
    }
}
