package fi.tamk.tiko.kivimiesgaming;

import com.badlogic.gdx.graphics.Texture;

/**
 * Created by atter on 28-Mar-17.
 */

public class Riddle {

    public String riddle;
    public String answer;
    public String imagePath;

    public Texture image;

    public Riddle(String riddle, String answer, String imagePath) {
        this.riddle = riddle;
        this.answer = answer;
        this.imagePath = imagePath;
    }

    public Riddle(String riddleData) {

    }

    public void load() {
        image = new Texture(imagePath);
    }

    public void dispose() {
        image.dispose();
        image = null;
    }

}
