package com.tll.circles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * Created by abdullahtellioglu on 06/08/17.
 */
public class PreferenceHandler {
    private static final String PREFERENCE_NAME = "DefaultPref";
    public static String getString(String key,String def){
        Preferences preferences = Gdx.app.getPreferences(PREFERENCE_NAME);
        return preferences.getString(key,def);
    }
    public static int getInt(String key,int defValue){
        Preferences preferences = Gdx.app.getPreferences(PREFERENCE_NAME);
        return preferences.getInteger(key,defValue);

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
}
