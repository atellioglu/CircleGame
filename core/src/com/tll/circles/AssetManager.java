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
    public static final Texture retryIcon = new Texture("retry_black_theme.png");
    public static final Texture levelText = new Texture("level_black_theme.png");
    public static final Texture endCircle = new Texture("end_black_theme.png");
    public static final Texture[] circles = new Texture[]{
            new Texture(Gdx.files.internal("circles/circle_type_0.png"),true),
            new Texture(Gdx.files.internal("circles/circle_type_1.png"),true),
            new Texture(Gdx.files.internal("circles/circle_type_2.png"),true),
            new Texture(Gdx.files.internal("circles/circle_type_3.png"),true)
    };
    public static final Texture defaultArrow = new Texture("arrow_black_theme.png");
    public static final Texture overlay = new Texture("overlay.png");
}
