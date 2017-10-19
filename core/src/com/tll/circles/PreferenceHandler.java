package com.tll.circles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * Created by abdullahtellioglu on 06/08/17.
 */
public class PreferenceHandler {
    private static final String PREFERENCE_NAME = "DefaultPref";
    public static final String STAR_KEY = "STAR_KEY";
    public static final String CURRENT_LEVEL = "CURRENT_LEVEL";
    public static final String CURRENT_THEME = "CURRENT_THEME";
    public static String getString(String key,String def){
        Preferences preferences = Gdx.app.getPreferences(PREFERENCE_NAME);
        return preferences.getString(key,def);
    }
    public static int getInt(String key,int defValue){
        Preferences preferences = Gdx.app.getPreferences(PREFERENCE_NAME);
        return preferences.getInteger(key,defValue);
    }
    public static void saveBoolean(String key,boolean value){
        Preferences preferences = Gdx.app.getPreferences(PREFERENCE_NAME);
        preferences.putBoolean(key,value);
        preferences.flush();
    }
    public static boolean getBoolean(String key){
        Preferences preferences = Gdx.app.getPreferences(PREFERENCE_NAME);
        return preferences.getBoolean(key,false);

    }
    public static void saveString(String key,String value){
        Preferences preferences = Gdx.app.getPreferences(PREFERENCE_NAME);
        preferences.putString(key,value);
        preferences.flush();
    }
    public static void saveInt(String key,int value){
        Preferences preferences = Gdx.app.getPreferences(PREFERENCE_NAME);
        preferences.putInteger(key, value);
        preferences.flush();
    }
    public static int getCurrentLevel(){
        return getInt(CURRENT_LEVEL,11);
    }
    public static String getCurrentTheme(){
        return getString(CURRENT_THEME,"WHITE");
    }
    public static void setCurrentTheme(String theme){
        saveString(CURRENT_THEME,theme);
    }
    public static void saveCurrentLevel(int level){
        int levelMax = getCurrentLevel();
        if(level > levelMax){
            saveInt(CURRENT_LEVEL,level);
        }
    }
}
