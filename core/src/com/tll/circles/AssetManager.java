package com.tll.circles;

import com.badlogic.gdx.graphics.Texture;

/**
 * Created by abdullahtellioglu on 09/07/17.
 */
public class AssetManager {


    public static final Texture shield = new Texture("shield_not_active.png");
    public static final Texture shieldActive = new Texture("shield_active.png");
    public static final Texture star = new Texture("star.png");
    public static class WhiteTheme{
        public static final Texture levelName = new Texture("level_white_theme.png");
        public static final Texture menu = new Texture("menu_white_theme.png");
        public static final Texture retry = new Texture("retry_white_theme.png");
        public static final Texture circle = new Texture("circle_white_theme.png");
        public static final Texture defaultArrow = new Texture("arrow_white_theme.png");
        public static final Texture endCircle = new Texture("end_white_theme.png");
    }
    public static class BlackTheme{
        public static final Texture levelName = new Texture("level_black_theme.png");
        public static final Texture menu = new Texture("menu_black_theme.png");
        public static final Texture retry = new Texture("retry_black_theme.png");
        public static final Texture circle = new Texture("circle_black_theme.png");
        public static final Texture defaultArrow = new Texture("arrow_black_theme.png");
        public static final Texture endCircle = new Texture("end_black_theme.png");
    }
}
