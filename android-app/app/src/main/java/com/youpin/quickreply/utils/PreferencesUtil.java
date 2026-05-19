package com.youpin.quickreply.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesUtil {
    
    private static final String PREFS_NAME = "quick_reply_prefs";
    private static final String KEY_FLOATING_ENABLED = "floating_enabled";
    private static final String KEY_FLOATING_SIZE = "floating_size";
    private static final String KEY_FLOATING_POSITION_X = "floating_position_x";
    private static final String KEY_FLOATING_POSITION_Y = "floating_position_y";
    private static final String KEY_NICKNAME = "nickname";
    private static final String KEY_AVATAR = "avatar";
    
    private SharedPreferences prefs;
    
    public PreferencesUtil(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }
    
    public void setFloatingEnabled(boolean enabled) {
        prefs.edit().putBoolean(KEY_FLOATING_ENABLED, enabled).apply();
    }
    
    public boolean isFloatingEnabled() {
        return prefs.getBoolean(KEY_FLOATING_ENABLED, true);
    }
    
    public void setFloatingSize(int size) {
        prefs.edit().putInt(KEY_FLOATING_SIZE, size).apply();
    }
    
    public int getFloatingSize() {
        return prefs.getInt(KEY_FLOATING_SIZE, 60);
    }
    
    public void setFloatingPosition(int x, int y) {
        prefs.edit().putInt(KEY_FLOATING_POSITION_X, x)
                .putInt(KEY_FLOATING_POSITION_Y, y).apply();
    }
    
    public int getFloatingPositionX() {
        return prefs.getInt(KEY_FLOATING_POSITION_X, -1);
    }
    
    public int getFloatingPositionY() {
        return prefs.getInt(KEY_FLOATING_POSITION_Y, -1);
    }
    
    public void setNickname(String nickname) {
        prefs.edit().putString(KEY_NICKNAME, nickname).apply();
    }
    
    public String getNickname() {
        return prefs.getString(KEY_NICKNAME, "用户");
    }
    
    public void setAvatar(String avatar) {
        prefs.edit().putString(KEY_AVATAR, avatar).apply();
    }
    
    public String getAvatar() {
        return prefs.getString(KEY_AVATAR, "");
    }
    
    public void clearCache() {
        prefs.edit().clear().apply();
    }
}
