package com.tll.circles;

/**
 * Created by abdullahtellioglu on 15/08/17.
 */

public final class Size {
    public int width;
    public int height;

    public Size(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public Size() {
        this(0, 0);
    }

    @Override public String toString() {
        return "com.tll.circles.Size(" + width + ", " + height + ")";
    }
}