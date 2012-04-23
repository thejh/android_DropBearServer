/*
 * Sherif elKhatib <http://stackoverflow.com/questions/6896618/read-command-output-inside-su-process>
 * Martin <http://www.droidnova.com/get-the-ip-address-of-your-device,304.html>
 */
package me.shkschneider.dropbearserver.Utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;

import org.apache.http.conn.util.InetAddressUtils;

import android.content.Context;
import android.util.Log;

public abstract class ServerUtils {

	public static final String TAG = "ServerUtils";

	public static String ipAddress = null;
	public static String dropbearPath = null;

	public static String getLocalBin(Context context) {
		if (dropbearPath == null) {
			if (context != null) {
				dropbearPath = context.getDir("bin", Context.MODE_PRIVATE).toString();
			}
		}
		return dropbearPath;
	}
	public static String getLocalIpAddress() {
		if (ipAddress == null) {
			try {
				for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
					NetworkInterface intf = en.nextElement();
					for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
						InetAddress inetAddress = enumIpAddr.nextElement();
						String ip4 = inetAddress.getHostAddress().toString();
						if (!inetAddress.isLoopbackAddress() && InetAddressUtils.isIPv4Address(ip4)) {
							Log.d(TAG, inetAddress.getHostAddress().toString());
							ipAddress = ip4;
							return ipAddress;
						}
					}
				}
			}
			catch (SocketException ex) {
				Log.e(TAG, ex.getMessage());
			}
			return null;
		}
		return ipAddress;
	}

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

	public static boolean generateRsaPrivateKey(String path) {
		ShellUtils.commands.add("dropbearkey -t rsa -f " + path);
		return ShellUtils.execute();
	}

	public static boolean generateDssPrivateKey(String path) {
		ShellUtils.commands.add("dropbearkey -t dss -f " + path);
		return ShellUtils.execute();
	}

	public static boolean generatePublicKey(String privatePath, String publicPath) {
		ShellUtils.commands.add("dropbearkey -f " + privatePath + " -y > " + publicPath);
		return ShellUtils.execute();
	}

}
