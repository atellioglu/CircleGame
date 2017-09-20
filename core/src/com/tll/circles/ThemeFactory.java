package com.tll.circles;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by abdullahtellioglu on 20/09/17.
 */
public class ThemeFactory {
    private static ThemeFactory instance;
    private Theme currentTheme;

    public static ThemeFactory getInstance(){
        return instance;
    }
    public static void setInstance(String themeName){
        instance = new ThemeFactory();
        instance.currentTheme =getTheme(themeName);
    }
    private ThemeFactory(){

    }
    public Theme getTheme(){
        return currentTheme;
    }

    private static Theme getTheme(String name){
        Theme theme = null;
        if(name.equals("WHITE")){
            theme = new Theme();
            theme.menu = AssetManager.WhiteTheme.menu;
            theme.level = AssetManager.WhiteTheme.levelName;
            theme.retry = AssetManager.WhiteTheme.retry;
            theme.circle = AssetManager.WhiteTheme.circle;
            theme.endCircle = AssetManager.WhiteTheme.endCircle;
            theme.arrow = AssetManager.WhiteTheme.defaultArrow;
            theme.backgroundColor = Color.WHITE;
            theme.lineColor = new Color(0.765f,0.886f,0.922f,0.7f);
        }else if(name.equals("BLACK")){
            theme = new Theme();
            theme.menu = AssetManager.BlackTheme.menu;
            theme.level = AssetManager.BlackTheme.levelName;
            theme.retry = AssetManager.BlackTheme.retry;
            theme.circle = AssetManager.BlackTheme.circle;
            theme.endCircle = AssetManager.BlackTheme.endCircle;
            theme.arrow = AssetManager.BlackTheme.defaultArrow;
            theme.backgroundColor = Color.BLACK;
            theme.lineColor = new Color(43f/255f,48/255f,49/255f,0.7f);
        }
        return theme;

    }
}
