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
	public static Boolean rootAvailable = false;
	public static Boolean dropbearAvailable = false;
	
	public static ArrayList<String> commandsToExecute = new ArrayList<String>();

	public static boolean isBusyboxAvailable()
	{
		return true;
	}

	public static boolean isRootAvailable()
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
					retval = false;
					exitSu = false;
					Log.d(TAG, "isRootAvailable(): Can't get root access or denied by user");
				}
				else if (true == currUid.contains("uid=0")) {
					retval = true;
					exitSu = true;
					Log.d(TAG, "isRootAvailable(): Root access granted");
				}
				else {
					retval = false;
					exitSu = true;
					Log.d(TAG, "isRootAvailable(): Root access rejected: " + currUid);
				}

				if (exitSu) {
					os.writeBytes("exit\n");
					os.flush();
				}
			}
		}
		catch (Exception e) {
			// Can't get root !
			// Probably broken pipe exception on trying to write to output
			// stream after su failed, meaning that the device is not rooted

			retval = false;
			Log.d(TAG, "isRootAvailable(): Root access rejected [" + e.getClass().getName() + "] : " + e.getMessage());
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
