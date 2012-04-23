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
	private static final String PREF_ONLY_IF_RUNNING_BEFORE = "onlyIfRunningBefore";
	private static final String PREF_KEEP_SCREEN_ON = "keepScreenOn";
	private static final String PREF_ONLY_OVER_WIFI = "onlyOverWifi";
	private static final String PREF_BANNER = "banner";
	private static final String PREF_DISALLOW_ROOT_LOGINS = "disallowRootLogins";
	private static final String PREF_DISABLE_PASSWORD_LOGINS = "disablePasswordLogins";
	private static final String PREF_DISABLE_PASSWORD_LOGINS_FOR_ROOT = "disablePasswordLoginsForRoot";
	private static final String PREF_LISTENING_PORT = "listeningPort";

	public static final Boolean START_AT_BOOT_DEFAULT = false;
	public static final Boolean ONLY_IF_RUNNING_BEFORE_DEFAULT = true;
	public static final Boolean KEEP_SCREEN_ON_DEFAULT = false;
	public static final Boolean ONLY_OVER_WIFI_DEFAULT = false;
	public static final String BANNER_DEFAULT = "";
	public static final Boolean DISALLOW_ROOT_LOGINS_DEFAULT = false;
	public static final Boolean DISABLE_PASSWORD_LOGINS_DEFAULT = false;
	public static final Boolean DISABLE_PASSWORD_LOGINS_FOR_ROOT_DEFAULT = false;
	public static final Integer LISTENING_PORT_DEFAULT = 22;
	
    private static SharedPreferences mSharedPreferences = null;
    private static Editor mEditor = null;
    
    public SettingsHelper(Context context) {
    	if (mSharedPreferences == null) {
    		mSharedPreferences = context.getSharedPreferences("shared_preferences", Activity.MODE_PRIVATE);
    	}
    	if (mEditor == null) {
    		mEditor = mSharedPreferences.edit();
    	}
    }
    
    // startAtBoot
    public Boolean getStartAtBoot() {
    	return mSharedPreferences.getBoolean(PREF_START_AT_BOOT, START_AT_BOOT_DEFAULT);
    }
    
    public void setStartAtBoot(Boolean b) {
    	Log.d(TAG, "setStartAtBoot(" + b + ")");
    	mEditor.putBoolean(PREF_START_AT_BOOT, b);
    	mEditor.commit();
    }
    
    // onlyIfRunningBefore
    public Boolean getOnlyIfRunningBefore() {
    	return mSharedPreferences.getBoolean(PREF_ONLY_IF_RUNNING_BEFORE, ONLY_IF_RUNNING_BEFORE_DEFAULT);
    }
    
    public void setOnlyIfRunningBefore(Boolean b) {
    	Log.d(TAG, "setOnlyIfRunningBefore(" + b + ")");
    	mEditor.putBoolean(PREF_ONLY_IF_RUNNING_BEFORE, b);
    	mEditor.commit();
    }
    
    // keepScreenOn
    public Boolean getKeepScreenOn() {
    	return mSharedPreferences.getBoolean(PREF_KEEP_SCREEN_ON, KEEP_SCREEN_ON_DEFAULT);
    }
    
    public void setKeepScreenOn(Boolean b) {
    	Log.d(TAG, "setKeepScreenOn(" + b + ")");
    	mEditor.putBoolean(PREF_KEEP_SCREEN_ON, b);
    	mEditor.commit();
    }
    
    // onlyOverWifi
    public Boolean getOnlyOverWifi() {
    	return mSharedPreferences.getBoolean(PREF_ONLY_OVER_WIFI, ONLY_OVER_WIFI_DEFAULT);
    }
    
    public void setOnlyOverWifi(Boolean b) {
    	Log.d(TAG, "setOnlyOverWifi(" + b + ")");
    	mEditor.putBoolean(PREF_ONLY_OVER_WIFI, b);
    	mEditor.commit();
    }
    
    // banner
    public String getBanner() {
    	return mSharedPreferences.getString(PREF_BANNER, BANNER_DEFAULT);
    }
    
    public void setBanner(String s) {
    	Log.d(TAG, "setBanner(" + s + ")");
    	mEditor.putString(PREF_BANNER, s);
    	mEditor.commit();
    }
    
    // disallowRootLogins
    public Boolean getDisallowRootLogins() {
    	return mSharedPreferences.getBoolean(PREF_DISALLOW_ROOT_LOGINS, DISABLE_PASSWORD_LOGINS_DEFAULT);
    }
    
    public void setDisallowRootLogins(Boolean b) {
    	Log.d(TAG, "setDisallowRootLogins(" + b + ")");
    	mEditor.putBoolean(PREF_DISALLOW_ROOT_LOGINS, b);
    	mEditor.commit();
    }
    
    // disablePasswordLogins
    public Boolean getDisablePasswordLogins() {
    	return mSharedPreferences.getBoolean(PREF_DISABLE_PASSWORD_LOGINS, DISABLE_PASSWORD_LOGINS_DEFAULT);
    }
    
    public void setDisablePasswordLogins(Boolean b) {
    	Log.d(TAG, "setDisablePasswordLogins(" + b + ")");
    	mEditor.putBoolean(PREF_DISABLE_PASSWORD_LOGINS, b);
    	mEditor.commit();
    }
    
    // disablePasswordLoginsForRoot
    public Boolean getDisablePasswordLoginsForRoot() {
    	return mSharedPreferences.getBoolean(PREF_DISABLE_PASSWORD_LOGINS_FOR_ROOT, DISABLE_PASSWORD_LOGINS_FOR_ROOT_DEFAULT);
    }
    
    public void setDisablePasswordLoginsForRoot(Boolean b) {
    	Log.d(TAG, "setDisablePasswordLoginsForRoot(" + b + ")");
    	mEditor.putBoolean(PREF_DISABLE_PASSWORD_LOGINS_FOR_ROOT, b);
    	mEditor.commit();
    }
    
    // listeningPort
    public Integer getListeningPort() {
    	return mSharedPreferences.getInt(PREF_LISTENING_PORT, LISTENING_PORT_DEFAULT);
    }
    
    public void setListeningPort(Integer i) {
    	Log.d(TAG, "setListeningPort(" + i + ")");
    	mEditor.putInt(PREF_LISTENING_PORT, i);
    	mEditor.commit();
    }
    
}
