/*
 * Pawel Nadolski <http://stackoverflow.com/questions/10319471/android-is-the-groupid-of-sdcard-rw-always-1015/>
 */
package me.shkschneider.dropbearserver.Tasks;

import me.shkschneider.dropbearserver.SettingsHelper;
import me.shkschneider.dropbearserver.Utils.ServerUtils;
import me.shkschneider.dropbearserver.Utils.ShellUtils;
import me.shkschneider.dropbearserver.Utils.Utils;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class ServerStarter extends AsyncTask<Void, String, Boolean> {

	private static final String TAG = "DropBearServer";
	private static final int ID_ROOT = 0;
	private static final int ID_SDCARD_RW = 1015;

	private Context mContext = null;
	private ProgressDialog mProgressDialog = null;

	private ServerStarterCallback<Boolean> mCallback;

	public ServerStarter(Context context, ServerStarterCallback<Boolean> callback) {
		mContext = context;
		mCallback = callback;
		if (mContext != null) {
			mProgressDialog = new ProgressDialog(mContext);
			mProgressDialog.setTitle("Starting server");
			mProgressDialog.setMessage("Please wait...");
			mProgressDialog.setCancelable(false);
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			mProgressDialog.setMax(100);
			mProgressDialog.setIcon(0);
		}
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (mProgressDialog != null) {
			mProgressDialog.show();
		}
	}

	private Boolean falseWithError(String error) {
		Log.d(TAG, "ServerStarter: " + error);
		//Toast.makeText(mContext, "Error: " + error, Toast.LENGTH_LONG).show();
		return false;
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		Log.i(TAG, "ServerStarter: doInBackground()");

		if (SettingsHelper.getInstance(mContext).getOnlyOverWifi() == true && Utils.isConnectedToWiFi(mContext) == false) {
			return falseWithError("You are not over WiFi network");
		}

		if (ServerUtils.isDropbearRunning() == true) {
			Log.i(TAG, "ServerStopper: Killing processes");
			if (ShellUtils.killall("dropbear") == false)
				return falseWithError("killall(dropbear)");
		}

		String login = (SettingsHelper.getInstance(mContext).getCredentialsLogin() ? "root" : "android");
		String passwd = SettingsHelper.getInstance(mContext).getCredentialsPasswd();
		String banner = ServerUtils.getLocalDir(mContext) + "/banner";
		String hostRsa = ServerUtils.getLocalDir(mContext) + "/host_rsa";
		String hostDss = ServerUtils.getLocalDir(mContext) + "/host_dss";
		String authorizedKeys = ServerUtils.getLocalDir(mContext) + "/authorized_keys";
		Integer listeningPort = SettingsHelper.getInstance(mContext).getListeningPort();
		String pidFile = ServerUtils.getLocalDir(mContext) + "/pid";

		String command = "/system/xbin/dropbear";
		command = command.concat(" -A -N " + login);
		command = command.concat(" -C " + passwd);
		command = command.concat(" -r " + hostRsa + " -d " + hostDss);
		command = command.concat(" -R " + authorizedKeys);
		if (login.equals("root")) {
			command = command.concat(" -U " + ID_ROOT + " -G " + ID_ROOT);
		}
		else {
			// TODO: uid=app gid=app groups=1015(sdcard_rw),3003(inet)
			command = command.concat(" -U " + mContext.getApplicationInfo().uid + " -G " + ID_SDCARD_RW);
		}
		command = command.concat(" -p " + listeningPort);
		command = command.concat(" -P " + pidFile);

		if (SettingsHelper.getInstance(mContext).getDisallowRootLogins() == true) {
			command = command.concat(" -w");
		}
		if (SettingsHelper.getInstance(mContext).getDisablePasswordLogins() == true) {
			command = command.concat(" -s");
		}
		if (SettingsHelper.getInstance(mContext).getDisablePasswordLoginsForRoot() == true) {
			command = command.concat(" -g");
		}
		if (SettingsHelper.getInstance(mContext).getBanner() == true) {
			command = command.concat(" -b " + banner);
		}

		ShellUtils.commands.add(command);

		if (ShellUtils.execute() == false) {
			return falseWithError("execute(" + command + ")");
		}

		return true;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
		}
		if (result == true) {
			ShellUtils.echoToFile("0" + android.os.Process.myPid(), ServerUtils.getLocalDir(mContext) + "/lock");
		}
		if (mCallback != null) {
			mCallback.onServerStarterComplete(result);
		}
	}
}