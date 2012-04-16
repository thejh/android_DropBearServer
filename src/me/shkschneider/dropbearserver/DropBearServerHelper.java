package me.shkschneider.dropbearserver;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.stericson.RootTools.RootTools;

public abstract class DropBearServerHelper
{
	public static String TAG = "DropBearServerHelper";

	public static Boolean hasRootAccess = false;
	public static Boolean hasDropbear = false;

	public static boolean checkRootAccess()
	{
		hasRootAccess = false;
		if (RootTools.isRootAvailable()) {
			try {
				if (RootTools.isAccessGiven()) {
					hasRootAccess = true;
				}
				else {
					Log.d(TAG, "checkRootAccess(): access rejected");
				}
			}
			catch (TimeoutException e) {
				Log.d(TAG, "checkRootAccess(): access timeout");
				e.printStackTrace();
			}
		}
		else {
			Log.d(TAG, "checkRootAccess(): su not found");
		}
		return hasRootAccess;
	}

	public static boolean checkDropbear()
	{
		hasDropbear = false;
		if (RootTools.checkUtil("dropbear")) {
			if (RootTools.checkUtil("dropbearkey")) {
				hasDropbear = true;
			}
			else {
				Log.d(TAG, "checkDropbear(): dropbearkey not found");
			}
		}
		else {
			Log.d(TAG, "checkDropbear(): dropbear not found");
		}
		return hasDropbear;
	}

	public static void marketNotFound(Context context)
	{
		Toast.makeText(context, "Market not found!", Toast.LENGTH_SHORT).show();
	}
	
	public static Boolean setServerPid(int pid) {
		if (pid <= 0) {
			Log.d(TAG, "setServerPid(): invalid pid " + pid);
			return false;
		}
		File d = new File("/data/dropbear");
		if (!d.exists() || !d.isDirectory()) {
			Log.d(TAG, "setServerPid(): /data/dropbear does not exists");
			return false;
		}
		else {
			File f = new File("/data/dropbear/pid");
			if (pid == 0) {
				if (f.delete() == false) {
					Log.d(TAG, "setServerPid(): delete() failed");
					return false;
				}
				return true;
			}
			else {
				try {
					f.createNewFile();
					return true;
				} catch (IOException e) {
					Log.d(TAG, "setServerPid(): createNewFile() failed");
					e.printStackTrace();
					return false;
				}
			}
		}
	}
	
	public static int getServerPid() {
		File d = new File("/data/dropbear");
		if (d.exists() == false || d.isDirectory() == false) {
			Log.d(TAG, "setServerPid(): /data/dropbear does not exists");
			return -1;
		}
		else {
			File f = new File("/data/dropbear/pid");
			if (!f.exists() || f.isFile()) {
				return 0;
			}
			else {
				return 42;
			}
		}
	}
}
