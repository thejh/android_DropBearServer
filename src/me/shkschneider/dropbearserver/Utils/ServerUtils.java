package me.shkschneider.dropbearserver.Utils;

import java.io.File;
import java.io.IOException;
import android.util.Log;

public abstract class ServerUtils
{
	public static String TAG = "ServerUtils";

	public static Boolean setServerPid(int pid) {
		if (pid <= 0) {
			Log.e(TAG, "setServerPid(): invalid pid " + pid);
			return false;
		}
		File d = new File("/data/dropbear");
		if (!d.exists() || !d.isDirectory()) {
			Log.e(TAG, "setServerPid(): /data/dropbear does not exists");
			return false;
		}
		else {
			File f = new File("/data/dropbear/pid");
			if (pid == 0) {
				if (f.delete() == false) {
					Log.e(TAG, "setServerPid(): delete() failed");
					return false;
				}
				return true;
			}
			else {
				try {
					f.createNewFile();
					return true;
				} catch (IOException e) {
					Log.e(TAG, "setServerPid(): " + e.getMessage());
					return false;
				}
			}
		}
	}

	public static int getServerPid() {
		File d = new File("/data/dropbear");
		if (d.exists() == false || d.isDirectory() == false) {
			Log.e(TAG, "setServerPid(): /data/dropbear does not exists");
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
