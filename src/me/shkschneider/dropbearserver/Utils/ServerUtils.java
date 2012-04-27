/*
 * Sherif elKhatib <http://stackoverflow.com/questions/6896618/read-command-output-inside-su-process>
 * Martin <http://www.droidnova.com/get-the-ip-address-of-your-device,304.html>
 * javadb <http://www.javadb.com/remove-a-line-from-a-text-file>
 */
package me.shkschneider.dropbearserver.Utils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

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

	// WARNING: this is not threaded
	public static final String getLocalIpAddress() {
		if (ipAddress == null) {
			try {
				for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
					NetworkInterface intf = en.nextElement();
					for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
						InetAddress inetAddress = enumIpAddr.nextElement();
						String ip4 = inetAddress.getHostAddress().toString();
						if (!inetAddress.isLoopbackAddress() && InetAddressUtils.isIPv4Address(ip4)) {
							Log.d(TAG, "ServerUtils: getLocalIpAddress(): " + inetAddress.getHostAddress().toString());
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
	// WARNING: this is not threaded
	public static final Integer getServerPidFromFile(Context context) {
		File f = new File(ServerUtils.getLocalDir(context) + "/pid");
		if (f.exists() == true && f.isFile() == true) {
			try {
				FileInputStream fis = new FileInputStream(ServerUtils.getLocalDir(context) + "/pid");
				DataInputStream dis = new DataInputStream(fis);
				BufferedReader br = new BufferedReader(new InputStreamReader(dis));
				String line = br.readLine();
				dis.close();
				if (line != null) {
					try {
						Integer pid = Integer.parseInt(line);
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
		}
		return 0;
	}

	// WARNING: this is not threaded
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

	// WARNING: this is not threaded
	public static final Boolean generateRsaPrivateKey(String path) {
		ShellUtils.commands.add("dropbearkey -t rsa -f " + path);
		return ShellUtils.execute();
	}

	// WARNING: this is not threaded
	public static final Boolean generateDssPrivateKey(String path) {
		ShellUtils.commands.add("dropbearkey -t dss -f " + path);
		return ShellUtils.execute();
	}

	// WARNING: this is not threaded
	public static List<String> getPublicKeys(String path) {
		List<String> publicKeysList = new ArrayList<String>();
		if (new File(path).exists() == true) {
			try {
				FileInputStream fis = new FileInputStream(path);
				DataInputStream dis = new DataInputStream(fis);
				BufferedReader br = new BufferedReader(new InputStreamReader(dis));
				String line = null;
				while ((line = br.readLine()) != null) {
					publicKeysList.add(line);
				}
				dis.close();
			}
			catch (Exception e) {
				Log.e(TAG, "ServerUtils: getPublicKeys(): " + e.getMessage());
			}
		}
		else {
			Log.w(TAG, "ServerUtils: getPublicKeys(): File could not be found: " + path);
		}
		return publicKeysList;
	}

	// WARNING: this is not threaded
	public static final Boolean addPublicKey(String publicKey, String authorized_keys) {
		return ShellUtils.echoAppendToFile(publicKey, authorized_keys);
	}

	// WARNING: this is not threaded
	public static final Boolean removePublicKey(String publicKey, String authorized_keys) {
		try {
			File inFile = new File(authorized_keys);
			File tempFile = new File(inFile.getAbsolutePath() + ".tmp");

			BufferedReader br = new BufferedReader(new FileReader(authorized_keys));
			PrintWriter pw = new PrintWriter(new FileWriter(tempFile));

			String line = null;

			while ((line = br.readLine()) != null) {
				if (!line.trim().equals(publicKey)) {

					pw.println(line);
					pw.flush();
				}
			}
			pw.close();
			br.close();

			if (!inFile.delete()) {
				Log.d(TAG, "ServerUtils: removePublicKey(): delete() failed");
				return false;
			} 
			if (!tempFile.renameTo(inFile)) {
				Log.d(TAG, "ServerUtils: removePublicKey(): renameTo() failed");
				return false;
			}
		}
		catch (Exception e) {
			Log.d(TAG, "ServerUtils: removePublicKey(): " + e.getMessage());
			return false;
		}
		return true;
	}

	public static final Boolean createIfNeeded(String path) {
		File file = new File(path);
		if (file.exists() == false) {
			try {
				file.createNewFile();
			} catch (Exception e) {
				Log.d(TAG, "ServerUtils: touch(): " + e.getMessage());
				return false;
			}
		}
		return true;
	}

	// WARNING: this is not threaded
	public static final String getDropbearVersion() {
		if (RootUtils.hasBusybox == false) {
			return null;
		}
		String version = null;
		try {
			Process suProcess = Runtime.getRuntime().exec("su");

			// stdin
			DataOutputStream stdin = new DataOutputStream(suProcess.getOutputStream());
			Log.d(TAG, "ServerUtils: getDropbearVersion(): # dropbear -h");
			stdin.writeBytes("dropbear -h 2>&1 | busybox head -1\n");
			stdin.flush();
			stdin.writeBytes("exit\n");
			stdin.flush();

			// stdout
			BufferedReader reader = new BufferedReader(new InputStreamReader(suProcess.getInputStream()));
			String line = reader.readLine();

			// parsing
			if (line != null && line.matches("^Dropbear sshd v[0-9\\.]+$")) {
				version = line.replaceFirst("^Dropbear sshd v", "");
			}
		}
		catch (Exception e) {
			Log.e(TAG, "ServerUtils: getServerPidFromPs(): " + e.getMessage());
		}
		return version;
	}

}
