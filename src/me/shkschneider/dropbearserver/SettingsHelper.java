/*
 * Bill Mote <http://stackoverflow.com/questions/5734721/android-shared-preferences>
 */
package me.shkschneider.dropbearserver;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.Log;

public class SettingsHelper {
	
	private static final String TAG = "DropBearServer";

	private static final String PREF_ASSUME_ROOT_ACCESS = "assumeRootAccess";
	private static final String PREF_NOTIFICATION = "notification";
	private static final String PREF_KEEP_SCREEN_ON = "keepScreenOn";
	private static final String PREF_ONLY_OVER_WIFI = "onlyOverWifi";
	private static final String PREF_DISALLOW_ROOT_LOGINS = "disallowRootLogins";
	private static final String PREF_DISABLE_PASSWORD_LOGINS = "disablePasswordLogins";
	private static final String PREF_DISABLE_PASSWORD_LOGINS_FOR_ROOT = "disablePasswordLoginsForRoot";
	private static final String PREF_LISTENING_PORT = "listeningPort";
	private static final String PREF_CREDENTIALS_LOGIN = "credentialsLogin";
	private static final String PREF_CREDENTIALS_PASSWD = "credentialsPasswd";

	public static final Boolean ASSUME_ROOT_ACCESS_DEFAULT = false;
	public static final Boolean NOTIFICATION_DEFAULT = false;
	public static final Boolean KEEP_SCREEN_ON_DEFAULT = false;
	public static final Boolean ONLY_OVER_WIFI_DEFAULT = false;
	public static final Boolean DISALLOW_ROOT_LOGINS_DEFAULT = false;
	public static final Boolean DISABLE_PASSWORD_LOGINS_DEFAULT = false;
	public static final Boolean DISABLE_PASSWORD_LOGINS_FOR_ROOT_DEFAULT = false;
	public static final Integer LISTENING_PORT_DEFAULT = 22;
	public static final Boolean CREDENTIALS_LOGIN_DEFAULT = true;
	public static final String CREDENTIALS_PASSWD_DEFAULT = "42";
	
	private static SettingsHelper mSettingsHelper = null;
	private static SharedPreferences mSharedPreferences = null;
    
    public static SettingsHelper getInstance(Context context) {
    	if (mSettingsHelper == null) {
    		mSettingsHelper = new SettingsHelper(context);
    	}
    	return mSettingsHelper;
    }
    
    private SettingsHelper(Context context) {
    	if (context != null) {
    		mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    	}
    	else {
    		Log.e(TAG, "SettingsHelper: SettingsHelper(): Context is null");
    	}
    }
    
    // assumeRootAccess
    public Boolean getAssumeRootAccess() {
    	return mSharedPreferences.getBoolean(PREF_ASSUME_ROOT_ACCESS, ASSUME_ROOT_ACCESS_DEFAULT);
    }
    
    public void setAssumeRootAccess(Boolean b) {
    	Log.d(TAG, "SettingsHelper: setAssumeRootAccess(" + b + ")");
    	Editor editor = mSharedPreferences.edit();
    	editor.putBoolean(PREF_ASSUME_ROOT_ACCESS, b);
    	editor.commit();
    }
    
    // notification
    public Boolean getNotification() {
    	return mSharedPreferences.getBoolean(PREF_NOTIFICATION, NOTIFICATION_DEFAULT);
    }
    
    public void setNotification(Boolean b) {
    	Log.d(TAG, "SettingsHelper: setNotification(" + b + ")");
    	Editor editor = mSharedPreferences.edit();
    	editor.putBoolean(PREF_NOTIFICATION, b);
    	editor.commit();
    }
    
    // keepScreenOn
    public Boolean getKeepScreenOn() {
    	return mSharedPreferences.getBoolean(PREF_KEEP_SCREEN_ON, KEEP_SCREEN_ON_DEFAULT);
    }
    
    public void setKeepScreenOn(Boolean b) {
    	Log.d(TAG, "SettingsHelper: setKeepScreenOn(" + b + ")");
    	Editor editor = mSharedPreferences.edit();
    	editor.putBoolean(PREF_KEEP_SCREEN_ON, b);
    	editor.commit();
    }
    
    // onlyOverWifi
    public Boolean getOnlyOverWifi() {
    	return mSharedPreferences.getBoolean(PREF_ONLY_OVER_WIFI, ONLY_OVER_WIFI_DEFAULT);
    }
    
    public void setOnlyOverWifi(Boolean b) {
    	Log.d(TAG, "SettingsHelper: setOnlyOverWifi(" + b + ")");
    	Editor editor = mSharedPreferences.edit();
    	editor.putBoolean(PREF_ONLY_OVER_WIFI, b);
    	editor.commit();
    }
    
    // disallowRootLogins
    public Boolean getDisallowRootLogins() {
    	return mSharedPreferences.getBoolean(PREF_DISALLOW_ROOT_LOGINS, DISABLE_PASSWORD_LOGINS_DEFAULT);
    }
    
    public void setDisallowRootLogins(Boolean b) {
    	Log.d(TAG, "SettingsHelper: setDisallowRootLogins(" + b + ")");
    	Editor editor = mSharedPreferences.edit();
    	editor.putBoolean(PREF_DISALLOW_ROOT_LOGINS, b);
    	editor.commit();
    }
    
    // disablePasswordLogins
    public Boolean getDisablePasswordLogins() {
    	return mSharedPreferences.getBoolean(PREF_DISABLE_PASSWORD_LOGINS, DISABLE_PASSWORD_LOGINS_DEFAULT);
    }
    
    public void setDisablePasswordLogins(Boolean b) {
    	Log.d(TAG, "SettingsHelper: setDisablePasswordLogins(" + b + ")");
    	Editor editor = mSharedPreferences.edit();
    	editor.putBoolean(PREF_DISABLE_PASSWORD_LOGINS, b);
    	editor.commit();
    }
    
    // disablePasswordLoginsForRoot
    public Boolean getDisablePasswordLoginsForRoot() {
    	return mSharedPreferences.getBoolean(PREF_DISABLE_PASSWORD_LOGINS_FOR_ROOT, DISABLE_PASSWORD_LOGINS_FOR_ROOT_DEFAULT);
    }
    
    public void setDisablePasswordLoginsForRoot(Boolean b) {
    	Log.d(TAG, "SettingsHelper: setDisablePasswordLoginsForRoot(" + b + ")");
    	Editor editor = mSharedPreferences.edit();
    	editor.putBoolean(PREF_DISABLE_PASSWORD_LOGINS_FOR_ROOT, b);
    	editor.commit();
    }
    
    // listeningPort
    public Integer getListeningPort() {
    	return mSharedPreferences.getInt(PREF_LISTENING_PORT, LISTENING_PORT_DEFAULT);
    }
    
    public void setListeningPort(Integer i) {
    	Log.d(TAG, "SettingsHelper: setListeningPort(" + i + ")");
    	Editor editor = mSharedPreferences.edit();
    	editor.putInt(PREF_LISTENING_PORT, i);
    	editor.commit();
    }
    
    // credentialsLogin
    public Boolean getCredentialsLogin() {
    	return mSharedPreferences.getBoolean(PREF_CREDENTIALS_LOGIN, CREDENTIALS_LOGIN_DEFAULT);
    }
    
    public void setCredentialsLogin(Boolean b) {
    	Log.d(TAG, "SettingsHelper: setCredentialsLogin(" + b + ")");
    	Editor editor = mSharedPreferences.edit();
    	editor.putBoolean(PREF_CREDENTIALS_LOGIN, b);
    	editor.commit();
    }
    
    // credentialsPasswd
    public String getCredentialsPasswd() {
    	return mSharedPreferences.getString(PREF_CREDENTIALS_PASSWD, CREDENTIALS_PASSWD_DEFAULT);
    }
    
    public void setCredentialsPasswd(String s) {
    	Log.d(TAG, "SettingsHelper: setCredentialsPasswd(" + s + ")");
    	Editor editor = mSharedPreferences.edit();
    	editor.putString(PREF_CREDENTIALS_PASSWD, s);
    	editor.commit();
    }
    
}
