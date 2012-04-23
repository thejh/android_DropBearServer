/*
 * Bill Mote <http://stackoverflow.com/questions/5734721/android-shared-preferences>
 */
package me.shkschneider.dropbearserver;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class SettingsHelper {
	
	private static final String TAG = "SettingsHelper";
	
	private static final String PREF_START_AT_BOOT = "startAtBoot";
	private static final String PREF_KEEP_SCREEN_ON = "keepScreenOn";
	private static final String PREF_ONLY_OVER_WIFI = "onlyOverWifi";
	private static final String PREF_BANNER = "banner";
	private static final String PREF_MESSAGE_OF_THE_DAY = "messageOfTheDay";
	private static final String PREF_DISALLOW_ROOT_LOGINS = "disallowRootLogins";
	private static final String PREF_DISABLE_PASSWORD_LOGINS = "disablePasswordLogins";
	private static final String PREF_DISABLE_PASSWORD_LOGINS_FOR_ROOT = "disablePasswordLoginsForRoot";
	private static final String PREF_ENABLE_MASTER_PASSWORD = "enableMasterPassword";
	private static final String PREF_LISTENING_PORT = "listeningPort";
	
	public static final Boolean START_AT_BOOT_DEFAULT = false;
	public static final Boolean KEEP_SCREEN_ON_DEFAULT = false;
	public static final Boolean ONLY_OVER_WIFI_DEFAULT = false;
	public static final String BANNER_DEFAULT = "";
	public static final String MESSAGE_OF_THE_DAY_DEFAULT = "";
	public static final Boolean DISALLOW_ROOT_LOGINS_DEFAULT = false;
	public static final Boolean DISABLE_PASSWORD_LOGINS_DEFAULT = false;
	public static final Boolean DISABLE_PASSWORD_LOGINS_FOR_ROOT_DEFAULT = false;
	public static final Boolean ENABLE_MASTER_PASSWORD_DEFAULT = true;
	public static final Integer LISTENING_PORT_DEFAULT = 22;
	
    private SharedPreferences mSharedPreferences;
    private Editor mEditor;
    
    public SettingsHelper(Context context) {
    	Log.d(TAG, "SettingsHelper()");
    	
    	mSharedPreferences = context.getSharedPreferences("shared_preferences", Activity.MODE_PRIVATE);
    	mEditor = mSharedPreferences.edit();
    }
    
    // startAtBoot
    public Boolean getStartAtBoot() {
    	return mSharedPreferences.getBoolean(PREF_START_AT_BOOT, START_AT_BOOT_DEFAULT);
    }
    
    public void setStartAtBoot(Boolean b) {
    	mEditor.putBoolean(PREF_START_AT_BOOT, b);
    	mEditor.commit();
    }
    
    // keepScreenOn
    public Boolean getKeepScreenOn() {
    	return mSharedPreferences.getBoolean(PREF_KEEP_SCREEN_ON, KEEP_SCREEN_ON_DEFAULT);
    }
    
    public void setKeepScreenOn(Boolean b) {
    	mEditor.putBoolean(PREF_START_AT_BOOT, b);
    	mEditor.commit();
    }
    
    // onlyOverWifi
    public Boolean getOnlyOverWifi() {
    	return mSharedPreferences.getBoolean(PREF_ONLY_OVER_WIFI, ONLY_OVER_WIFI_DEFAULT);
    }
    
    public void setOnlyOverWifi(Boolean b) {
    	mEditor.putBoolean(PREF_START_AT_BOOT, b);
    	mEditor.commit();
    }
    
    // banner
    public String getBanner() {
    	return mSharedPreferences.getString(PREF_BANNER, BANNER_DEFAULT);
    }
    
    public void setBanner(String s) {
    	mEditor.putString(PREF_BANNER, s);
    	mEditor.commit();
    }
    
    // messageOfTheDay
    public String getMessageOfTheDay() {
    	return mSharedPreferences.getString(PREF_MESSAGE_OF_THE_DAY, MESSAGE_OF_THE_DAY_DEFAULT);
    }
    
    public void setMessageOfTheDay(String s) {
    	mEditor.putString(PREF_MESSAGE_OF_THE_DAY, s);
    	mEditor.commit();
    }
    
    // disallowRootLogins
    public Boolean getDisallowRootLogins() {
    	return mSharedPreferences.getBoolean(PREF_DISALLOW_ROOT_LOGINS, DISABLE_PASSWORD_LOGINS_DEFAULT);
    }
    
    public void setDisallowRootLogins(Boolean b) {
    	mEditor.putBoolean(PREF_DISALLOW_ROOT_LOGINS, b);
    	mEditor.commit();
    }
    
    // disablePasswordLogins
    public Boolean getDisablePasswordLogins() {
    	return mSharedPreferences.getBoolean(PREF_DISABLE_PASSWORD_LOGINS, DISABLE_PASSWORD_LOGINS_DEFAULT);
    }
    
    public void setDisablePasswordLogins(Boolean b) {
    	mEditor.putBoolean(PREF_DISABLE_PASSWORD_LOGINS, b);
    	mEditor.commit();
    }
    
    // disablePasswordLoginsForRoot
    public Boolean getDisablePasswordLoginsForRoot() {
    	return mSharedPreferences.getBoolean(PREF_DISABLE_PASSWORD_LOGINS_FOR_ROOT, DISABLE_PASSWORD_LOGINS_FOR_ROOT_DEFAULT);
    }
    
    public void setDisablePasswordLoginsForRoot(Boolean b) {
    	mEditor.putBoolean(PREF_DISABLE_PASSWORD_LOGINS_FOR_ROOT, b);
    	mEditor.commit();
    }
    
    // enableMasterPassword
    public Boolean getEnableMasterPassword() {
    	return mSharedPreferences.getBoolean(PREF_ENABLE_MASTER_PASSWORD, ENABLE_MASTER_PASSWORD_DEFAULT);
    }
    
    public void setEnableMasterPassword(Boolean b) {
    	mEditor.putBoolean(PREF_ENABLE_MASTER_PASSWORD, b);
    	mEditor.commit();
    }
    
    // listeningPort
    public Integer getListeningPort() {
    	return mSharedPreferences.getInt(PREF_LISTENING_PORT, LISTENING_PORT_DEFAULT);
    }
    
    public void setListeningPort(Integer i) {
    	Log.d(TAG, "***** setListeningPort() [ OK ]");
    	mEditor.putInt(PREF_LISTENING_PORT, i);
    	mEditor.commit();
    }
    
}
