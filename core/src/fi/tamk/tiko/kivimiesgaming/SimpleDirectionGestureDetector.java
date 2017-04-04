package fi.tamk.tiko.kivimiesgaming;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector;

/**
 * Created by risto on 3.4.2017.
 */

public class SimpleDirectionGestureDetector extends GestureDetector {

    public interface DirectionListener {
        void onLeft();

        void onRight();

        void onUp();

        void onDown();
    }

    public SimpleDirectionGestureDetector(DirectionListener directionListener) {
        super(new DirectionGestureListener(directionListener));
    }

    private static class DirectionGestureListener extends GestureAdapter{
        DirectionListener directionListener;

        public DirectionGestureListener(DirectionListener directionListener){
            this.directionListener = directionListener;
        }

        @Override
        public boolean fling(float velocityX, float velocityY, int button) {
            float deviceVelocity = Gdx.graphics.getHeight() * 1.5f;
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
                System.out.println(velocityY);
            }
            return super.fling(velocityX, velocityY, button);
        }
    }
}
