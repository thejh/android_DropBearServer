package me.shkschneider.dropbearserver.Utils;

import java.io.File;

import android.content.Context;
import android.util.Log;

import com.stericson.RootTools.RootTools;

public abstract class RootUtils {

	private static final String TAG = "DropBearServer";

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
					Log.w(TAG, "RootUtils: checkRootAccess(): access rejected");
				}
			}
			catch (Exception e) {
				Log.e(TAG, "RootUtils: checkRootAccess(): " + e.getMessage());
			}
		}
		else {
			Log.w(TAG, "RootUtils: checkRootAccess(): su not found");
		}
		return hasRootAccess;
	}

	public static final Boolean checkBusybox() {
		hasBusybox = false;
		if (RootTools.checkUtil("busybox")) {
			hasBusybox = true;
		}
		else {
			Log.w(TAG, "RootUtils: checkBusybox(): busybox not found");
		}
		return hasBusybox;
	}

	public static final Boolean checkDropbear(Context context) {
		hasDropbear = false;
		File file = null;

		file = new File(ServerUtils.getLocalDir(context) + "/dropbearmulti");
		if (file.exists() == false || file.isFile() == false) {
			Log.w(TAG, "RootUtils: checkDropear(): dropbearmulti");
			return false;
		}
		file = new File(ServerUtils.getLocalDir(context) + "/host_rsa");
		if (file.exists() == false || file.isFile() == false) {
			Log.w(TAG, "RootUtils: checkDropear(): host_rsa");
			return false;
		}
		file = new File(ServerUtils.getLocalDir(context) + "/host_dss");
		if (file.exists() == false || file.isFile() == false) {
			Log.w(TAG, "RootUtils: checkDropear(): host_dss");
			return false;
		}
		file = new File(ServerUtils.getLocalDir(context) + "/authorized_keys");
		if (file.exists() == false || file.isFile() == false || file.canRead() == false) {
			Log.w(TAG, "RootUtils: checkDropear(): authorized_keys");
			return false;
		}
		if (RootTools.checkUtil("dropbear")) {
			if (RootTools.checkUtil("dropbearkey")) {
				hasDropbear = true;
			}
			else {
				Log.w(TAG, "RootUtils: checkDropbear(): dropbearkey");
			}
		}
		else {
			Log.w(TAG, "RootUtils: checkDropbear(): dropbear");
		}
		return hasDropbear;
	}
}
