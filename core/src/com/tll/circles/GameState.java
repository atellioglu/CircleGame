package com.tll.circles;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.tll.circles.elements.ActiveCircle;
import com.tll.circles.elements.Arrow;
import com.tll.circles.elements.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abdullahtellioglu on 09/07/17.
 */
public class GameState extends State {
    private List<Element> elements;
    private Arrow userArrow;
    private boolean paused = false,started=true;

    public GameState(GameStateManager gsm) {
        super(gsm);

    }

    @Override
    protected void init() {
        // TODO: 10/07/17 Harita burada olusturulacak
        elements = new ArrayList<Element>();
        ActiveCircle activeCircle =new ActiveCircle(new Vector3(100,100,0));
        userArrow = new Arrow(activeCircle);
        elements.add(activeCircle);
        elements.add(userArrow);
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        for(int i =0;i<elements.size();i++){
            elements.get(i).render(sb);
        }
        sb.end();
    }

    @Override
    public void update(float dt) {
        for(int i =0;i<elements.size();i++){
            elements.get(i).update(dt);
        }
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(!paused){
            userArrow.detach();
        }
        return false;
    }
}
