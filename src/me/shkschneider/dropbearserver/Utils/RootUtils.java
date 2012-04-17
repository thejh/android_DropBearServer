package me.shkschneider.dropbearserver.Utils;

import java.util.concurrent.TimeoutException;
import android.util.Log;
import com.stericson.RootTools.RootTools;

public abstract class RootUtils
{
	public static String TAG = "RootUtils";

	public static Boolean hasRootAccess = false;
	public static Boolean hasBusybox = false;
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
					Log.w(TAG, "checkRootAccess(): access rejected");
				}
			}
			catch (TimeoutException e) {
				Log.w(TAG, "checkRootAccess(): " + e.getMessage());
			}
		}
		else {
			Log.w(TAG, "checkRootAccess(): su not found");
		}
		return hasRootAccess;
	}

	public static boolean checkBusybox()
	{
		hasBusybox = false;
		if (RootTools.checkUtil("busybox")) {
			hasBusybox = true;
		}
		else {
			Log.w(TAG, "checkBusybox(): busybox not found");
		}
		return hasBusybox;
	}

	public static boolean checkDropbear()
	{
		hasDropbear = false;
		if (RootTools.checkUtil("dropbear")) {
			if (RootTools.checkUtil("dropbearkey")) {
				hasDropbear = true;
			}
			else {
				Log.w(TAG, "checkDropbear(): dropbearkey not found");
			}
		}
		else {
			Log.w(TAG, "checkDropbear(): dropbear not found");
		}
		return hasDropbear;
	}
}
