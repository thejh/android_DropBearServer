/*
 * augusto <http://code.google.com/p/droidsshd/source/browse/src/br/com/bott/droidsshd/system/BootReceiver.java>
 */
package me.shkschneider.dropbearserver;

import me.shkschneider.dropbearserver.Utils.ServerUtils;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {

    private static final String TAG = "BootReceiver";
    
    @Override
    public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(android.content.Intent.ACTION_BOOT_COMPLETED)) {
                    Integer pid = ServerUtils.getServerPidFromPs();
                    Boolean wasRunningBefore = (pid > 0);
                    Boolean startDaemonAtBoot = false; //startAtBoot
                    Boolean startDaemonAtBootOnlyIfRunningBefore = false; //startAtBootOnlyIfRunningBefore

                    Log.d(TAG, "ACTION_BOOT_COMPLETED");

                    if (wasRunningBefore) {
                    	// rm PID
                    }

                    if (startDaemonAtBoot) {
                            if (!startDaemonAtBootOnlyIfRunningBefore || (startDaemonAtBootOnlyIfRunningBefore && wasRunningBefore)) {
                            	//context.startService(new Intent(context, me.shkschneider.dropbearserver.Service.class));
                            }
                    }
            }
    }
}