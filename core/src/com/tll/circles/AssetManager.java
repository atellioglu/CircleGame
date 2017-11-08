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
    public static final Texture retryIcon = new Texture("retry_black_theme.png");
    public static final Texture levelText = new Texture("level_black_theme.png");
    public static final Texture endCircle = new Texture("end_black_theme.png");
    public static final Texture blueEmptyCircle = new Texture("blue_circle_empty.png");
    public static final Texture grayEmptyCircle = new Texture("gray_circle_empty.png");
    public static final Texture backgroundMenu = new Texture("bgtest2.jpg");
    public static final Texture background = new Texture("background.jpg");
    public static final Texture menuStartButton = new Texture(Gdx.files.internal("menu_start_button.png"),true);
    public static final Texture menuLevelButton = new Texture(Gdx.files.internal("menu_level_button.png"),true);
    public static final Texture menuSoundOnButton = new Texture(Gdx.files.internal("menu_sound_on.png"),true);
    public static final Texture menuSoundOffButton = new Texture(Gdx.files.internal("menu_sound_off.png"),true);
    public static final Texture menuStarIcon = new Texture(Gdx.files.internal("menu_star_icon.png"),true);
    public static final Texture[] circles = new Texture[]{
            new Texture(Gdx.files.internal("circle_type_0.png"),true),
            new Texture(Gdx.files.internal("circle_type_1.png"),true),
            new Texture(Gdx.files.internal("circle_type_2.png"),true),
            new Texture(Gdx.files.internal("circle_type_3.png"),true)
    };
    public static final Texture defaultArrow = new Texture("arrow.png");
    public static final Texture overlay = new Texture("overlay.png");
}
