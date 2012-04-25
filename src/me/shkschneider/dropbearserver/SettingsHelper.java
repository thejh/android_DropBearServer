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
	private static final String PREF_START_AT_BOOT = "startAtBoot";
	private static final String PREF_ONLY_IF_RUNNING_BEFORE = "onlyIfRunningBefore";
	private static final String PREF_KEEP_SCREEN_ON = "keepScreenOn";
	private static final String PREF_ONLY_OVER_WIFI = "onlyOverWifi";
	private static final String PREF_BANNER = "banner";
	private static final String PREF_DISALLOW_ROOT_LOGINS = "disallowRootLogins";
	private static final String PREF_DISABLE_PASSWORD_LOGINS = "disablePasswordLogins";
	private static final String PREF_DISABLE_PASSWORD_LOGINS_FOR_ROOT = "disablePasswordLoginsForRoot";
	private static final String PREF_LISTENING_PORT = "listeningPort";
	private static final String PREF_ACCOUNTS_LOGIN = "accountsLogin";
	private static final String PREF_ACCOUNTS_PASSWD = "accountsPasswd";

	public static final Boolean ASSUME_ROOT_ACCESS_DEFAULT = false;
	public static final Boolean START_AT_BOOT_DEFAULT = false;
	public static final Boolean ONLY_IF_RUNNING_BEFORE_DEFAULT = true;
	public static final Boolean KEEP_SCREEN_ON_DEFAULT = false;
	public static final Boolean ONLY_OVER_WIFI_DEFAULT = false;
	public static final String BANNER_DEFAULT = "";
	public static final Boolean DISALLOW_ROOT_LOGINS_DEFAULT = false;
	public static final Boolean DISABLE_PASSWORD_LOGINS_DEFAULT = false;
	public static final Boolean DISABLE_PASSWORD_LOGINS_FOR_ROOT_DEFAULT = false;
	public static final Integer LISTENING_PORT_DEFAULT = 22;
	public static final String ACCOUNTS_LOGIN_DEFAULT = "root";
	public static final String ACCOUNTS_PASSWD_DEFAULT = "42";
	
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
    
    // startAtBoot
    public Boolean getStartAtBoot() {
    	return mSharedPreferences.getBoolean(PREF_START_AT_BOOT, START_AT_BOOT_DEFAULT);
    }
    
    public void setStartAtBoot(Boolean b) {
    	Log.d(TAG, "SettingsHelper: setStartAtBoot(" + b + ")");
    	Editor editor = mSharedPreferences.edit();
    	editor.putBoolean(PREF_START_AT_BOOT, b);
    	editor.commit();
    }
    
    // onlyIfRunningBefore
    public Boolean getOnlyIfRunningBefore() {
    	return mSharedPreferences.getBoolean(PREF_ONLY_IF_RUNNING_BEFORE, ONLY_IF_RUNNING_BEFORE_DEFAULT);
    }
    
    public void setOnlyIfRunningBefore(Boolean b) {
    	Log.d(TAG, "SettingsHelper: setOnlyIfRunningBefore(" + b + ")");
    	Editor editor = mSharedPreferences.edit();
    	editor.putBoolean(PREF_ONLY_IF_RUNNING_BEFORE, b);
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
    
    // banner
    public String getBanner() {
    	return mSharedPreferences.getString(PREF_BANNER, BANNER_DEFAULT);
    }
    
    public void setBanner(String s) {
    	Log.d(TAG, "SettingsHelper: setBanner(" + s + ")");
    	Editor editor = mSharedPreferences.edit();
    	editor.putString(PREF_BANNER, s);
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
    
    // accountsLogin
    public String getAccountsLogin() {
    	return mSharedPreferences.getString(PREF_ACCOUNTS_LOGIN, ACCOUNTS_LOGIN_DEFAULT);
    }
    
    public void setAccountsLogin(String s) {
    	Log.d(TAG, "SettingsHelper: setAccountsLogin(" + s + ")");
    	Editor editor = mSharedPreferences.edit();
    	editor.putString(PREF_ACCOUNTS_LOGIN, s);
    	editor.commit();
    }
    
    // accountsPasswd
    public String getAccountsPasswd() {
    	return mSharedPreferences.getString(PREF_ACCOUNTS_PASSWD, ACCOUNTS_PASSWD_DEFAULT);
    }
    
    public void setAccountsPasswd(String s) {
    	Log.d(TAG, "SettingsHelper: setAccountsPasswd(" + s + ")");
    	Editor editor = mSharedPreferences.edit();
    	editor.putString(PREF_ACCOUNTS_PASSWD, s);
    	editor.commit();
    }
    
}
