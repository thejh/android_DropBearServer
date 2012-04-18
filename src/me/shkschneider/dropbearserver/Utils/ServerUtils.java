/*
 * Sherif elKhatib <http://stackoverflow.com/questions/6896618/read-command-output-inside-su-process>
 */
package me.shkschneider.dropbearserver.Utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

import android.util.Log;

public abstract class ServerUtils
{
	public static String TAG = "ServerUtils";
	
	public static int getServerPid() {
		try {
			Process suProcess = Runtime.getRuntime().exec("su");

			// stdin
			DataOutputStream stdin = new DataOutputStream(suProcess.getOutputStream());
			Log.d(TAG, "# ps dropbear");
			stdin.writeBytes("ps dropbear\n");
			stdin.flush();
			stdin.writeBytes("exit\n");
			stdin.flush();

			// stdout
			BufferedReader reader = new BufferedReader(new InputStreamReader(suProcess.getInputStream()));
			ArrayList<String> output = new ArrayList<String>();
			String line;
			while ((line = reader.readLine()) != null) {
				output.add(line);
			}

			// parsing
			if (output.size() >= 2) {
				line = null;
				Iterator<String> itr = output.iterator();
				while (itr.hasNext()) {
					String element = itr.next();
					if (element.matches("^\\S+\\s+[0-9]+\\s+.+\\sdropbear(\\s.+)?$")) {
						line = element;
						break ;
					}
				}
				if (line != null) {
					line = line.replaceFirst("^^\\S+\\s+([0-9]+)\\s+.+\\sdropbear(\\s.+)?$", "$1");
					if (line.matches("^[0-9]+$")) {
						int pid = Integer.parseInt(line);
						Log.i(TAG, "PID #" + pid);
						return pid;
					}
				}
			}
			return 0;
		}
		catch (IOException ex) {
			Log.w(TAG, "Can't get root access");
		}
		catch (SecurityException ex) {
			Log.w(TAG, "Can't get root access");
		}
		catch (Exception ex) {
			Log.w(TAG, "Error executing internal operation");
		}

		return -1;
	}
	
	public static final Boolean killServer(int pid) {
		if (pid <= 0)
			pid = getServerPid();
		if (pid > 0) {
			return ShellUtils.kill(9, pid);
		}
		return false;
	}
}
