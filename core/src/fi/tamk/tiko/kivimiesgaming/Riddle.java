package fi.tamk.tiko.kivimiesgaming;

import java.util.ArrayList;

/**
 * @author Atte-Petteri Ronkanen, Risto Pulkkinen
 *
 * Riddle class for storing riddle image and riddle texts in different languages
 */

public class Riddle {

    /**
     * Riddle texts in different languages
     */
    private ArrayList<RiddleTexts> riddleTexts;

    /**
     * Path to riddle image
     */
    public String imagePath;

    /**
     * Contructor for the riddle
     *
     * @param imagePath Path to riddle image
     */
    public Riddle(String imagePath) {
        this.imagePath = Vescape.RIDDLE_IMAGES_PATH + imagePath;
        riddleTexts = new ArrayList<RiddleTexts>();
    }

    /**
     * Adds new riddle text to the list
     *
     * @param riddleText New riddle text
     */
    public void addRiddleText(RiddleTexts riddleText) {
        riddleTexts.add(riddleText);
    }

    /**
     * Returns riddle text in given language
     *
     * @param language Language
     * @return Returns riddle text in given language
     */
    public RiddleTexts getRiddle(String language) {
        for (int i = 0; i < riddleTexts.size(); ++i) {
            if (language.equalsIgnoreCase(riddleTexts.get(i).language))
                return riddleTexts.get(i);
        }
        return riddleTexts.get(0);
    }
}
