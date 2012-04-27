/*
 * Mestre <http://code.google.com/p/droidsshd/source/browse/src/br/com/bott/droidsshd/system/BootReceiver.java>
 */
package me.shkschneider.dropbearserver;

import me.shkschneider.dropbearserver.Tasks.ServerStarter;
import me.shkschneider.dropbearserver.Utils.ServerUtils;
import me.shkschneider.dropbearserver.Utils.ShellUtils;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {

	private static final String TAG = "DropBearServer";

	@Override
	public void onReceive(Context context, Intent i) {
		if (i.getAction().equals(android.content.Intent.ACTION_BOOT_COMPLETED)) {
			Log.d(TAG, "BootReceiver: onReceive(ACTION_BOOT_COMPLETED)");

			Boolean hasPid = (ServerUtils.getServerPidFromFile(context) > 0 ? true : false);
			Boolean startAtBoot = SettingsHelper.getInstance(context).getStartAtBoot();
			Boolean onlyIfRunningbefore = SettingsHelper.getInstance(context).getOnlyIfRunningBefore();

			if (hasPid) {
				ShellUtils.rm(ServerUtils.getLocalDir(context) + "/pid");
			}

			if (startAtBoot) {
				if (!onlyIfRunningbefore || (onlyIfRunningbefore && hasPid)) {
					Log.i(TAG, "BootReceiver: DropBear Server starting at boot");
					
					// ServerStarter
					ServerStarter serverStarter = new ServerStarter(null, null);
					serverStarter.execute();
					
					// notification
					if (SettingsHelper.getInstance(context).getNotification() == true) {
						String infos = "ssh ";
						if (SettingsHelper.getInstance(context).getCredentialsLogin() == true) {
							infos = infos.concat("root@");
						}
						infos = infos.concat(ServerUtils.getLocalIpAddress());
						if (SettingsHelper.getInstance(context).getListeningPort() != SettingsHelper.LISTENING_PORT_DEFAULT) {
							infos = infos.concat(" -p " + SettingsHelper.getInstance(context).getListeningPort());
						}
						
						NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
						Notification notification = new Notification(R.drawable.ic_launcher, "DropBear Server is running", System.currentTimeMillis());
						Intent intent = new Intent(context, MainActivity.class);
						PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
						notification.setLatestEventInfo(context, "DropBear Server", infos, pendingIntent);
						notification.flags |= Notification.FLAG_ONGOING_EVENT;
						notificationManager.notify(1, notification);
					}
				}
			}
		}
	}

}