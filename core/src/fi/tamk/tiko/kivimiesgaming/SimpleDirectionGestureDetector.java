package fi.tamk.tiko.kivimiesgaming;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector;

/**
 * @author Atte-Petteri Ronkanen, Risto Pulkkinen
 *
 * This class controls the swiping mechanics.
 */

public class SimpleDirectionGestureDetector extends GestureDetector {

    /**
     * This interface must be implemented in order to use swipe
     */
    public interface DirectionListener {
        void onLeft();

        void onRight();

        void onUp();

        void onDown();
    }

    /**
     * Class constructor.
     * @param directionListener sends direction listener to super.
     */
    public SimpleDirectionGestureDetector(DirectionListener directionListener) {
        super(new DirectionGestureListener(directionListener));
    }

    /**
     * Extends GestureAdapter
     */
    private static class DirectionGestureListener extends GestureAdapter{
        DirectionListener directionListener;

        /**
         * Sets up variable directionListener.
         * @param directionListener is the new variable.
         */
        public DirectionGestureListener(DirectionListener directionListener){
            this.directionListener = directionListener;
        }

        /**
         * Conrols the swipe speed and direction.
         * @param velocityX controls horizontal swipe velocity.
         * @param velocityY controls vertical swipe velocity.
         * @param button is not used.
         * @return the swipe to super.
         */
        @Override
        public boolean fling(float velocityX, float velocityY, int button) {
            float deviceVelocity = Gdx.graphics.getHeight() * 1.25f;
            if(Math.abs(velocityX)>Math.abs(velocityY)){
                if(velocityX>0){
                    directionListener.onRight();
                }else{
                    directionListener.onLeft();
                }
            }else if (Math.abs(velocityY)>Math.abs(deviceVelocity)){
                if(velocityY > 0){
                    directionListener.onDown();
                }else{
                    directionListener.onUp();
                }
            }
            return super.fling(velocityX, velocityY, button);
        }
    }
}
