package com.tll.circles;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Stack;

/**
 * Created by abdullahtellioglu on 09/07/17.
 */
public class GameStateManager {
    private Stack<State> stack;
    public GameStateManager(){
        stack = new Stack<State>();
    }

    public void push(State state){
        stack.push(state);
    }
    public State pop(){
        return stack.pop();
    }
    public void set(State st){
        stack.pop();
        stack.push(st);
    }
    public void update(float dt){
        stack.peek().update(dt);
    }
    public void render(SpriteBatch sb){
        stack.peek().render(sb);
    }
}
