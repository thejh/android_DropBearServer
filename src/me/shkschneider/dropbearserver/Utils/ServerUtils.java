/*
 * Sherif elKhatib <http://stackoverflow.com/questions/6896618/read-command-output-inside-su-process>
 * Martin <http://www.droidnova.com/get-the-ip-address-of-your-device,304.html>
 */
package me.shkschneider.dropbearserver.Utils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;

import org.apache.http.conn.util.InetAddressUtils;

import android.content.Context;
import android.util.Log;

public abstract class ServerUtils {

	private static final String TAG = "DropBearServer";

	public static String localDir = null;
	public static String ipAddress = null;

	public static final String getLocalDir(Context context) {
		if (localDir == null) {
			localDir = context.getDir("data", Context.MODE_PRIVATE).toString();
		}
		return localDir;
	}
	public static final String getLocalIpAddress() {
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
			catch (Exception e) {
				Log.e(TAG, "ServerUtils: getLocalIpAddress(): " + e.getMessage());
			}
			ipAddress = null;
		}
		return ipAddress;
	}

	/*
	 * Dropbear seems to take some time to write the pidFile
	 * As a consequence, this is only used by the BootReceiver
	 */
	public static final Integer getServerPidFromFile(Context context) {
		try {
			FileInputStream fis = new FileInputStream(ServerUtils.getLocalDir(context) + "/pid");
			DataInputStream dis = new DataInputStream(fis);
			BufferedReader br = new BufferedReader(new InputStreamReader(dis));
			String line = br.readLine();
			dis.close();
			if (line != null) {
				try {
					Integer pid = Integer.parseInt(line);
					Log.i(TAG, "ServerUtils: PID #" + pid);
					return pid;
				}
				catch (Exception e) {
					Log.e(TAG, "ServerUtils: getServerPidFromFile(): " + e.getMessage());
					return -1;
				}
			}
		}
		catch (Exception e) {
			Log.e(TAG, e.getMessage());
			return -1;
		}
		return 0;
	}

	public static final Integer getServerPidFromPs() {
		try {
			Process suProcess = Runtime.getRuntime().exec("su");

			// stdin
			DataOutputStream stdin = new DataOutputStream(suProcess.getOutputStream());
			Log.d(TAG, "ServerUtils: getServerPidFromPs(): # ps dropbear");
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
					if (Utils.isNumeric(line)) {
						Integer pid = Integer.parseInt(line);
						Log.i(TAG, "ServerUtils: PID #" + pid);
						return pid;
					}
				}
			}
		}
		catch (Exception e) {
			Log.e(TAG, "ServerUtils: getServerPidFromPs(): " + e.getMessage());
			return -1;
		}
		return 0;
	}

	public static final Boolean generateRsaPrivateKey(String path) {
		ShellUtils.commands.add("dropbearkey -t rsa -f " + path);
		return ShellUtils.execute();
	}

	public static final Boolean generateDssPrivateKey(String path) {
		ShellUtils.commands.add("dropbearkey -t dss -f " + path);
		return ShellUtils.execute();
	}

}
