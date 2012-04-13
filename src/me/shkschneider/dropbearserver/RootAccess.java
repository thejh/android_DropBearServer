/*
 * Muzikant <http://muzikant-android.blogspot.fr/2011/02/how-to-get-root-access-and-execute.html>
 */

package me.shkschneider.dropbearserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.util.Log;

public abstract class RootAccess
{
	private static final String TAG = "RootAccess";
	public static Boolean suAvailable = false;
	public static Boolean dropbearAvailable = false;
	
	public static ArrayList<String> commandsToExecute = new ArrayList<String>();

	public static boolean isSuAvailable()
	{
		boolean retval = false;
		Process suProcess;

		try {
			suProcess = Runtime.getRuntime().exec("su");
			DataOutputStream os = new DataOutputStream(suProcess.getOutputStream());
			DataInputStream osRes = new DataInputStream(suProcess.getInputStream());

			if (null != os && null != osRes) {
				// Getting the id of the current user to check if this is root
				os.writeBytes("id\n");
				os.flush();

				String currUid = osRes.readLine();
				boolean exitSu = false;
				if (null == currUid) {
					Log.d(TAG, "isSuAvailable(): su access denied by user");
				}
				else if (true == currUid.contains("uid=0")) {
					retval = true;
					exitSu = true;
					suAvailable = true;
					Log.d(TAG, "isSuAvailable(): su access granted");
				}
				else {
					exitSu = true;
					Log.d(TAG, "isSuAvailable(): su access rejected: " + currUid);
				}

				if (exitSu) {
					os.writeBytes("exit\n");
					os.flush();
				}
			}
		}
		catch (Exception e) {
			Log.d(TAG, "isRootAvailable(): su access rejected: [" + e.getClass().getName() + "] : " + e.getMessage());
		}

		return retval;
	}

	public static boolean isDropbearAvailable()
	{
		boolean retval = false;
		Process dbProcess;

		try {
			dbProcess = Runtime.getRuntime().exec("dropbear -h");
			DataInputStream osRes = new DataInputStream(dbProcess.getInputStream());

			if (null != osRes) {
				String dropbearHelp = osRes.readLine();
				if (null == dropbearHelp) {
					Log.d(TAG, "isDropbearAvailable(): dropbear access denied by user");
				}
				else if (true == dropbearHelp.contains("Dropbear sshd v")) {
					retval = true;
					dropbearAvailable = true;
					Log.d(TAG, "isDropbearAvailable(): dropbear access granted");
				}
				else {
					Log.d(TAG, "isDropbearAvailable(): dropbear access rejected: " + dropbearHelp);
				}
			}
		}
		catch (Exception e) {
			Log.d(TAG, "isRootAvailable(): dropbear access rejected: [" + e.getClass().getName() + "] : " + e.getMessage());
		}

		return retval;
	}

	public static boolean execute()
	{
		boolean retval = false;

		try
		{
			if (null != commandsToExecute && commandsToExecute.size() > 0) {
				Process suProcess = Runtime.getRuntime().exec("su");
				DataOutputStream os = new DataOutputStream(suProcess.getOutputStream());

				// Execute commands that require root access
				for (String currCommand : commandsToExecute) {
					os.writeBytes(currCommand + "\n");
					os.flush();
				}
				os.writeBytes("exit\n");
				os.flush();

				try {
					int suProcessRetval = suProcess.waitFor();
					if (suProcessRetval == 0) {
						Log.d(TAG, "execute(): Root access granted");
						retval = true;
					}
					else {
						Log.d(TAG, "execute(): Root access rejected");
						retval = false;
					}
				}
				catch (Exception ex) {
					Log.e(TAG, "execute(): Error executing root action", ex);
				}
			}
		}
		catch (IOException ex) {
			Log.w(TAG, "execute(): Can't get root access", ex);
		}
		catch (SecurityException ex) {
			Log.w(TAG, "execute(): Can't get root access", ex);
		}
		catch (Exception ex) {
			Log.w(TAG, "execute(): Error executing internal operation", ex);
		}

		commandsToExecute.clear();

		return retval;
	}
}
