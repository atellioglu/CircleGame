package com.tll.circles;

/**
 * Created by abdullaht on 26.10.2017.
 */

public enum Skill {
    NONE(1,0),
    BARRIER_REMOVE(1,500),
    START_FROM_LAST_CIRCLE(1,200),
    SLOW_GAME(0.5f,500);
    private final float mVal;
    private final int mPrice;
    Skill(float val,int price){
        mVal = val;
        mPrice = price;
    }
    public int getPrice(){
        return mPrice;
    }
    public float getValue(){
        return mVal;
    }
}
