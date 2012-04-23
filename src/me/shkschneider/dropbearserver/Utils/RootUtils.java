package me.shkschneider.dropbearserver.Utils;

import java.io.File;

import android.content.Context;
import android.util.Log;

import com.stericson.RootTools.RootTools;

public abstract class RootUtils {

	private static final String TAG = "RootUtils";

	public static Boolean hasRootAccess = false;
	public static Boolean hasBusybox = false;
	public static Boolean hasDropbear = false;

	public static final Boolean checkRootAccess() {
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
			catch (Exception e) {
				Log.e(TAG, "checkRootAccess(): " + e.getMessage());
			}
		}
		else {
			Log.w(TAG, "checkRootAccess(): su not found");
		}
		return hasRootAccess;
	}

	public static final Boolean checkBusybox() {
		hasBusybox = false;
		if (RootTools.checkUtil("busybox")) {
			hasBusybox = true;
		}
		else {
			Log.w(TAG, "checkBusybox(): busybox not found");
		}
		return hasBusybox;
	}

	public static final Boolean checkDropbear(Context context) {
		hasDropbear = false;
		File f = null;

		f = new File(ServerUtils.getLocalDir(context) + "/dropbearmulti");
		if (f.exists() == false || f.isFile() == false || f.canExecute() == false) {
			Log.w(TAG, "checkDropear(): dropbearmulti");
			return false;
		}
		f = new File("/data/dropbear/host_rsa");
		if (f.exists() == false || f.isFile() == false || f.canRead() == false) {
			Log.w(TAG, "checkDropear(): host_rsa");
			return false;
		}
		f = new File("/data/dropbear/host_dss");
		if (f.exists() == false || f.isFile() == false || f.canRead() == false) {
			Log.w(TAG, "checkDropear(): host_dss");
			return false;
		}
		if (RootTools.checkUtil("dropbear")) {
			if (RootTools.checkUtil("dropbearkey")) {
				hasDropbear = true;
			}
			else {
				Log.w(TAG, "checkDropbear(): dropbearkey");
			}
		}
		else {
			Log.w(TAG, "checkDropbear(): dropbear");
		}
		return hasDropbear;
	}
}
