package fi.tamk.tiko.kivimiesgaming;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

/**
 * @author Atte-Petteri Ronkanen, Risto Pulkkinen
 *
 * This class helps to split text into lines.
 */
public class Utilities {

    /**
     * Class constructor.
     * @param str The string that needs to be split.
     * @param charsPerLine How many characters are allowed for each line.
     * @return Returns the splitted string.
     */
    public static String splitTextIntoLines(String str, int charsPerLine) {
        int currentLineStart = 0;
        String splittedString = "";
        while (true) {

            int currentIndex =  currentLineStart + charsPerLine;

            if (currentIndex >= str.length()) {
                currentIndex = str.length();
                splittedString += str.substring(currentLineStart,
                        currentIndex);
                break;
            }

            while (true) {
                if (str.charAt(currentIndex) == ' ') {
                    splittedString += str.substring(currentLineStart,
                            currentIndex) + "\n";
                    currentLineStart = currentIndex + 1;
                    break;
                } else if (currentIndex == currentLineStart) {

                    currentIndex = currentLineStart + charsPerLine - 1;

                    if (currentIndex >= str.length()) {
                        currentIndex = str.length();

                        splittedString += str.substring(currentLineStart,
                                currentIndex);
                        break;
                    } else {
                        splittedString += str.substring(currentLineStart,
                                currentIndex) + "\n";
                        currentLineStart = currentIndex + 1;

                        break;
                    }
                }
                --currentIndex;
            }
        }
        return splittedString;
    }

    /**
     * Moves given groups origin to given coordinates without moving the actors
     * inside the group
     *
     * @param g
     * @param x
     * @param y
     */
    public static void setGroupOrigin(Group g, float x, float y) {
        float offsetX = g.getX() - x;
        float offsetY = g.getY() - y;
        g.setPosition(x, y);
        for (Actor a : g.getChildren()) {
            a.setPosition(a.getX() + offsetX, a.getY() + offsetY);
        }
    }
}
