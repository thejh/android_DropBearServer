/*
 * augusto <http://code.google.com/p/droidsshd/source/browse/src/br/com/bott/droidsshd/system/BootReceiver.java>
 */
package me.shkschneider.dropbearserver;

import me.shkschneider.dropbearserver.Utils.ServerUtils;
import me.shkschneider.dropbearserver.Utils.ShellUtils;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {

	private static final String TAG = "BootReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(android.content.Intent.ACTION_BOOT_COMPLETED)) {
			Log.d(TAG, "ACTION_BOOT_COMPLETED");
			
			SettingsHelper settingsHelper = new SettingsHelper(context);
			Boolean hasPid = (ServerUtils.getServerPidFromFile(context) > 0 ? true : false);
			Boolean startAtBoot = settingsHelper.getStartAtBoot();
			Boolean onlyIfRunningbefore = settingsHelper.getOnlyIfRunningBefore();

			if (hasPid) {
				ShellUtils.rm(ServerUtils.getLocalDir(context) + "/pid");
			}

			if (startAtBoot) {
				if (!onlyIfRunningbefore || (onlyIfRunningbefore && hasPid)) {
					Log.i(TAG, "Dropbear server starting at boot");
					//context.startService(new Intent(context, me.shkschneider.dropbearserver.Service.class));
				}
			}
		}
	}
}