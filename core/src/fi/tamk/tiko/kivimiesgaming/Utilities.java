package fi.tamk.tiko.kivimiesgaming;

/**
 * @author Atte-Petteri Ronkanen, Risto Pulkkinen
 */
public class Utilities {

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
}
