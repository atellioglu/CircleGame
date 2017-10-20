package com.tll.circles.elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.tll.circles.Size;

/**
 * Created by abdullaht on 19.10.2017.
 */

public class FadeActiveCircle extends ActiveCircle {
    private float fadeTime,waitTime;
    private float alpha = 1;
    private float waitDelta = 0.0f;
    private FadeStates state;
    private float remainingUntilInvisible;

    public FadeActiveCircle(Texture texture, Size size, Vector3 position) {
        super(texture, size, position);
        state = FadeStates.VISIBLE;
        remainingUntilInvisible = waitTime + fadeTime;
    }

    public FadeActiveCircle(Size size, Vector3 position) {
        super(size, position);
        state = FadeStates.VISIBLE;
        remainingUntilInvisible = waitTime + fadeTime;
    }
    public FadeStates getState(){
        return state;
    }
    public float getFadeTime() {
        return fadeTime;
    }

    public void setFadeTime(float fadeTime) {
        this.fadeTime = fadeTime;
    }

    public float getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(float waitTime) {
        this.waitTime = waitTime;
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
    }

    @Override
    public void update(float dt) {
        if(attachedArrow!=null) {
            mSprite.rotate(-getRotationAngleSpeed());
        }
        Gdx.app.log("FADEActiveCircle",state.name()+" WaitTime : "+waitTime +", WaitDelta :"+waitDelta +", Alpha :"+alpha);
        if(state == FadeStates.VISIBLE){
            waitDelta +=dt;
            interaction = true;
            if(waitDelta >= waitTime){
                waitDelta = 0;
                state = FadeStates.FADING_OUT;
            }
        }else if (state == FadeStates.INVISIBLE){
            if(attachedArrow!=null){
                Gdx.app.log("TEST","YANDI");
                listener.onTimeout();
                return;
            }
            waitDelta +=dt;
            interaction = false;
            if(waitDelta >= waitTime){
                waitDelta = 0;
                state = FadeStates.FADING_IN;
                remainingUntilInvisible = waitTime + fadeTime+ fadeTime;// tam gorunur olcak bekliycek gorunmez olcak
            }
        }else if(state == FadeStates.FADING_OUT){
            alpha -= dt/fadeTime;
            if(alpha <= 0){
                interaction = false;
                state = FadeStates.INVISIBLE;
                alpha = 0;
            }
            mSprite.setAlpha(alpha);
        }else if(state == FadeStates.FADING_IN){
            interaction = true;
            alpha += dt/fadeTime;
            if(alpha >= 1){
                state = FadeStates.VISIBLE;
                alpha = 1;
            }
            mSprite.setAlpha(alpha);
        }

    }
    public enum FadeStates{
        FADING_IN,
        FADING_OUT,
        VISIBLE,
        INVISIBLE
    }
}
