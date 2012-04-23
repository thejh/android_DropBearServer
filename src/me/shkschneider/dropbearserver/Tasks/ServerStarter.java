package me.shkschneider.dropbearserver.Tasks;

import me.shkschneider.dropbearserver.SettingsHelper;
import me.shkschneider.dropbearserver.Utils.ServerUtils;
import me.shkschneider.dropbearserver.Utils.ShellUtils;
import me.shkschneider.dropbearserver.Utils.Utils;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class ServerStarter extends AsyncTask<Void, String, Boolean> {

	public Context mContext = null;
	public ProgressDialog mProgressDialog = null;

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

	@Override
	protected Boolean doInBackground(Void... params) {
		SettingsHelper settingsHelper = new SettingsHelper(mContext);
		if (settingsHelper.getOnlyOverWifi() == true && Utils.isConnectedToWiFi(mContext) == false) {
			return false;
		}
		
		String login = "root";
		String passwd = "42";
		String hostRsa = "/data/dropbear/host_rsa";
		String hostDss = "/data/dropbear/host_dss";
		String authorizedKeys = "/data/dropbear/authorized_keys";
		String uid = "0";
		String listeningPort = "22";
		String pidFile = ServerUtils.getLocalDir(mContext) + "/pid";
		
		String command = "dropbear";
		command = command.concat(" -A " + login + " -N " + login);
		command = command.concat(" -C " + passwd);
		command = command.concat(" -r " + hostRsa + " -d " + hostDss);
		command = command.concat(" -R " + authorizedKeys);
		command = command.concat(" -U " + uid + " -G " + uid);
		command = command.concat(" -p " + listeningPort);
		command = command.concat(" -P " + pidFile);
		
		if (settingsHelper.getDisallowRootLogins() == true) {
			command = command.concat(" -w");
		}
		if (settingsHelper.getDisablePasswordLogins() == true) {
			command = command.concat(" -s");
		}
		if (settingsHelper.getDisablePasswordLoginsForRoot() == true) {
			command = command.concat(" -g");
		}
		
		ShellUtils.commands.add(command);
		if (ShellUtils.execute() == false)
			return false;
		
		return true;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		mProgressDialog.dismiss();
		if (mCallback != null) {
			mCallback.onServerStarterComplete(result);
		}
	}
}