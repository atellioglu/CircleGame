package com.tll.circles;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by abdullahtellioglu on 20/09/17.
 */
public class ThemeFactory {
    private static ThemeFactory instance;
    private Theme currentTheme;

    public static ThemeFactory getInstance(){
        if(instance == null)
            setInstance(PreferenceHandler.getCurrentTheme());
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
            theme.shieldActive = AssetManager.WhiteTheme.shieldActive;
            theme.shieldNotActive = AssetManager.WhiteTheme.shield;
            theme.menuStartText = AssetManager.WhiteTheme.menuStartText;
            theme.menuLevelText = AssetManager.WhiteTheme.menuLevelText;
            theme.menuVoiceText = AssetManager.WhiteTheme.menuVoiceText;
            theme.menuThemeText = AssetManager.WhiteTheme.menuThemeText;
            theme.menuThemeSwitch = AssetManager.WhiteTheme.menuThemeSwitch;
            theme.menuBackgroundColor = Color.WHITE;
            theme.menuIcon = AssetManager.WhiteTheme.gameIcon;
            theme.menuCircle = AssetManager.WhiteTheme.menuEllipseCircle;
        }else if(name.equals("BLACK")){
            theme = new Theme();
            theme.menu = AssetManager.BlackTheme.menu;
            theme.level = AssetManager.BlackTheme.levelName;
            theme.retry = AssetManager.BlackTheme.retry;
            theme.circle = AssetManager.BlackTheme.circle;
            theme.endCircle = AssetManager.BlackTheme.endCircle;
            theme.arrow = AssetManager.BlackTheme.defaultArrow;
            theme.backgroundColor = new Color(32f/255,32f/255,32f/255,1);
            theme.lineColor = new Color(38f/255f,38/255f,39/255f,0.9f);
            theme.shieldActive = AssetManager.BlackTheme.shieldActive;
            theme.shieldNotActive = AssetManager.BlackTheme.shield;
        }
        return theme;

    }
}
