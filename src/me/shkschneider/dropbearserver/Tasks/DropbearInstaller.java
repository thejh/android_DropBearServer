package me.shkschneider.dropbearserver.Tasks;


import me.shkschneider.dropbearserver.Utils.ServerUtils;
import me.shkschneider.dropbearserver.Utils.ShellUtils;
import me.shkschneider.dropbearserver.Utils.Utils;
import me.shkschneider.dropbearserver.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class DropbearInstaller extends AsyncTask<Void, String, Boolean> {

	private static final String TAG = "DropBearServer";

	private Context mContext = null;
	private ProgressDialog mProgressDialog = null;

	private DropbearInstallerCallback<Boolean> mCallback;

	public DropbearInstaller(Context context, DropbearInstallerCallback<Boolean> callback) {
		mContext = context;
		mCallback = callback;
		if (mContext != null) {
			mProgressDialog = new ProgressDialog(mContext);
			mProgressDialog.setTitle("Installing Dropbear");
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
	protected void onProgressUpdate(String... progress) {
		super.onProgressUpdate(progress);
		if (mProgressDialog != null) {
			Float f = (Float.parseFloat(progress[0] + ".0") / Float.parseFloat(progress[1] + ".0") * 100);
			mProgressDialog.setProgress(Math.round(f));
			mProgressDialog.setMessage(progress[2]);
		}
	}
	
	private Boolean falseWithError(String error) {
		Log.d(TAG, "DropBearInstall: " + error);
		Toast.makeText(mContext, "Error: " + error, Toast.LENGTH_LONG).show();
		return false;
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		Log.i(TAG, "DropbearInstaller: doInBackground()");
		
		int step = 0;
		int steps = 23;
		
		String dropbearmulti = ServerUtils.getLocalDir(mContext) + "/dropbearmulti";
		String dropbear = "/system/xbin/dropbear";
		String dropbearkey = "/system/xbin/dropbearkey";
		String ssh = "/system/xbin/ssh";
		String scp = "/system/xbin/scp";
		String host_rsa = ServerUtils.getLocalDir(mContext) + "/host_rsa";
		String host_dss = ServerUtils.getLocalDir(mContext) + "/host_dss";
		String authorized_keys = ServerUtils.getLocalDir(mContext) + "/authorized_keys";
		String pid = ServerUtils.getLocalDir(mContext) + "/pid";

		// system rw
		if (ShellUtils.remountReadWrite("/system") == false) {
			return falseWithError("/system");
		}
		
		// dropbearmulti
		publishProgress("" + step++, "" + steps, dropbearmulti);
		if (Utils.copyRawFile(mContext, R.raw.dropbearmulti, dropbearmulti) == false) {
			return falseWithError(dropbearmulti);
		}

		// dropbear
		publishProgress("" + step++, "" + steps, dropbear);
		if (ShellUtils.lnSymbolic(dropbearmulti, dropbear) == false) {
			return falseWithError(dropbear);
		}
		publishProgress("" + step++, "" + steps, dropbear);
		if (ShellUtils.chown(dropbear, "0:0") == false) {
			return falseWithError(dropbear);
		}
		publishProgress("" + step++, "" + steps, dropbear);
		if (ShellUtils.chmod(dropbear, "755") == false) {
			return falseWithError(dropbear);
		}
		
		// dropbearkey
		publishProgress("" + step++, "" + steps, dropbearkey);
		if (ShellUtils.lnSymbolic(dropbearmulti, dropbearkey) == false) {
			return falseWithError(dropbearkey);
		}
		publishProgress("" + step++, "" + steps, dropbear);
		if (ShellUtils.chown(dropbearkey, "0:0") == false) {
			return falseWithError(dropbearkey);
		}
		publishProgress("" + step++, "" + steps, dropbear);
		if (ShellUtils.chmod(dropbearkey, "755") == false) {
			return falseWithError(dropbearkey);
		}
		
		// ssh
		publishProgress("" + step++, "" + steps, ssh);
		if (ShellUtils.lnSymbolic(dropbearmulti, ssh) == false) {
			return falseWithError(ssh);
		}
		publishProgress("" + step++, "" + steps, dropbear);
		if (ShellUtils.chown(ssh, "0:0") == false) {
			return falseWithError(ssh);
		}
		publishProgress("" + step++, "" + steps, dropbear);
		if (ShellUtils.chmod(ssh, "755") == false) {
			return falseWithError(ssh);
		}
		// scp
		publishProgress("" + step++, "" + steps, scp);
		if (ShellUtils.lnSymbolic(dropbearmulti, scp) == false) {
			return falseWithError(scp);
		}
		publishProgress("" + step++, "" + steps, dropbear);
		if (ShellUtils.chown(scp, "0:0") == false) {
			return falseWithError(scp);
		}
		publishProgress("" + step++, "" + steps, dropbear);
		if (ShellUtils.chmod(scp, "755") == false) {
			return falseWithError(scp);
		}

		// authorized_keys
		publishProgress("" + step++, "" + steps, authorized_keys);
		ServerUtils.createIfNeeded(authorized_keys);
		publishProgress("" + step++, "" + steps, authorized_keys);
		if (ShellUtils.touch(authorized_keys) == false) {
			return falseWithError(authorized_keys);
		}
		
		// host_rsa
		publishProgress("" + step++, "" + steps, host_rsa);
		if (ShellUtils.chown(host_rsa, "0:0") == false) {
			return falseWithError(host_rsa);
		}
		publishProgress("" + step++, "" + steps, host_rsa);
		if (ServerUtils.generateRsaPrivateKey(host_rsa) == false) {
			return falseWithError(host_rsa);
		}
		
		// host_dss
		publishProgress("" + step++, "" + steps, host_dss);
		if (ShellUtils.chown(host_dss, "0:0") == false) {
			return falseWithError(host_dss);
		}
		publishProgress("" + step++, "" + steps, host_dss);
		if (ServerUtils.generateDssPrivateKey(host_dss) == false) {
			return falseWithError(host_dss);
		}
		
		// pid
		publishProgress("" + step++, "" + steps, pid);
		ServerUtils.createIfNeeded(pid);
		publishProgress("" + step++, "" + steps, pid);
		if (ShellUtils.echoToFile("0", pid) == false) {
			return falseWithError(pid);
		}

		// system ro
		if (ShellUtils.remountReadOnly("/system") == false) {
			return falseWithError("/system");
		}
		
		return true;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
		}
		if (mCallback != null) {
			mCallback.onDropbearInstallerComplete(result);
		}
	}
}