/*
 * Mestre <http://code.google.com/p/droidsshd/source/browse/src/br/com/bott/droidsshd/system/BootReceiver.java>
 */
package me.shkschneider.dropbearserver;

import me.shkschneider.dropbearserver.Tasks.ServerStarter;
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
			Log.d(TAG, "onReceive(ACTION_BOOT_COMPLETED)");

			Boolean hasPid = (ServerUtils.getServerPidFromFile(context) > 0 ? true : false);
			Boolean startAtBoot = SettingsHelper.getInstance(context).getStartAtBoot();
			Boolean onlyIfRunningbefore = SettingsHelper.getInstance(context).getOnlyIfRunningBefore();

			if (hasPid) {
				ShellUtils.rm(ServerUtils.getLocalDir(context) + "/pid");
			}

			if (startAtBoot) {
				if (!onlyIfRunningbefore || (onlyIfRunningbefore && hasPid)) {
					Log.i(TAG, "Dropbear server starting at boot");
					// ServerStarter
					ServerStarter serverStarter = new ServerStarter(null, null);
					serverStarter.execute();
				}
			}
		}
	}

}