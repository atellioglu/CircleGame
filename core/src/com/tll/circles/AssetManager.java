package com.tll.circles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * Created by abdullahtellioglu on 09/07/17.
 */
public class AssetManager {


    public static final Texture star = new Texture(Gdx.files.internal("star.png"),true);
    public static final BitmapFont raviaBlue32 = new BitmapFont(Gdx.files.internal("font/ravia_blue_32.fnt"),Gdx.files.internal("font/ravia_blue_32.png"),false);
    public static final BitmapFont raviaBlue48 = new BitmapFont(Gdx.files.internal("font/ravia_blue_48.fnt"),Gdx.files.internal("font/ravia_blue_48.png"),false);
    public static final Texture menuStartIcon = new Texture("main/start_icon.png");
    public static final Texture menuLevelIcon = new Texture("main/level_icon.png");
    public static final Texture menuSoundOnIcon = new Texture("main/sound_on.png");
    public static final Texture[] circles = new Texture[]{
            new Texture(Gdx.files.internal("circles/circle_type_0.png"),true),
            new Texture(Gdx.files.internal("circles/circle_type_1.png"),true),
            new Texture(Gdx.files.internal("circles/circle_type_2.png"),true),
            new Texture(Gdx.files.internal("circles/circle_type_3.png"),true)
    };
    public static class WhiteTheme{
        public static final Texture shield = new Texture("shield_not_active_white_theme.png");
        public static final Texture shieldActive = new Texture("shield_active_white_theme.png");
        public static final Texture levelName = new Texture("level_white_theme.png");
        public static final Texture menu = new Texture("menu_white_theme.png");
        public static final Texture retry = new Texture("retry_white_theme.png");
        public static final Texture circle = new Texture("circle_white_theme.png");
        public static final Texture defaultArrow = new Texture("arrow_white_theme.png");
        public static final Texture endCircle = new Texture("end_white_theme.png");
        public static final Texture menuLevelText = new Texture("main/level_text_white_theme.png");
        public static final Texture menuStartText = new Texture("main/start_text_white_theme.png");
        public static final Texture menuVoiceText = new Texture("main/voice_text_white_theme.png");
        public static final Texture menuThemeText = new Texture("main/theme_text_white_theme.png");
        public static final Texture menuThemeSwitch = new Texture("main/theme_switch_white_theme.png");
        public static final Texture gameIcon = new Texture("main/icon_white_theme.png");
        public static final Texture menuEllipseCircle = new Texture("main/circle_with_ellipse_white_theme.png");
        }
    public static class BlackTheme{
        public static final Texture shield = new Texture("shield_not_active_black_theme.png");
        public static final Texture shieldActive = new Texture("shield_active_black_theme.png");
        public static final Texture levelName = new Texture("level_black_theme.png");
        public static final Texture menu = new Texture("menu_black_theme.png");
        public static final Texture retry = new Texture("retry_black_theme.png");
        public static final Texture circle = new Texture("circle_black_theme.png");
        public static final Texture defaultArrow = new Texture("arrow_black_theme.png");
        public static final Texture endCircle = new Texture("end_black_theme.png");
        public static final Texture menuLevelText = new Texture("main/level_text_white_theme.png");
        public static final Texture menuStartText = new Texture("main/start_text_white_theme.png");
        public static final Texture menuVoiceText = new Texture("main/voice_text_white_theme.png");
        public static final Texture menuThemeText = new Texture("main/theme_text_white_theme.png");
        public static final Texture menuThemeSwitch = new Texture("main/theme_switch_white_theme.png");
    }
}
