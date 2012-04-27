package me.shkschneider.dropbearserver.Tasks;

import me.shkschneider.dropbearserver.Utils.ServerUtils;
import me.shkschneider.dropbearserver.Utils.ShellUtils;
import me.shkschneider.dropbearserver.Utils.Utils;
import me.shkschneider.dropbearserver.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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
		//Toast.makeText(mContext, "Error: " + error, Toast.LENGTH_LONG).show();
		return false;
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		Log.i(TAG, "DropbearInstaller: doInBackground()");
		
		int step = 0;
		int steps = 24;
		
		String dropbearmulti = ServerUtils.getLocalDir(mContext) + "/dropbearmulti";
		String scp = ServerUtils.getLocalDir(mContext) + "/scp";
		String host_rsa = ServerUtils.getLocalDir(mContext) + "/host_rsa";
		String host_dss = ServerUtils.getLocalDir(mContext) + "/host_dss";
		String authorized_keys = ServerUtils.getLocalDir(mContext) + "/authorized_keys";
		String pid = ServerUtils.getLocalDir(mContext) + "/pid";

		// system rw
		publishProgress("" + step++, "" + steps, "/system Read-Write");
		if (ShellUtils.remountReadWrite("/system") == false) {
			return falseWithError("/system");
		}
		
		// dropbearmulti
		publishProgress("" + step++, "" + steps, "Dropbearmulti binary");
		if (Utils.copyRawFile(mContext, R.raw.dropbearmulti, dropbearmulti) == false) {
			return falseWithError(dropbearmulti);
		}

		// dropbear
		publishProgress("" + step++, "" + steps, "Dropbear binary");
		if (ShellUtils.lnSymbolic(dropbearmulti, "/system/xbin/dropbear") == false) {
			return falseWithError("/system/xbin/dropbear");
		}
		publishProgress("" + step++, "" + steps, "Dropbear binary");
		if (ShellUtils.chown("/system/xbin/dropbear", "0:0") == false) {
			return falseWithError("/system/xbin/dropbear");
		}
		publishProgress("" + step++, "" + steps, "Dropbear binary");
		if (ShellUtils.chmod("/system/xbin/dropbear", "755") == false) {
			return falseWithError("/system/xbin/dropbear");
		}
		
		// dropbearkey
		publishProgress("" + step++, "" + steps, "Dropbearkey binary");
		if (ShellUtils.lnSymbolic(dropbearmulti, "/system/xbin/dropbearkey") == false) {
			return falseWithError("/system/xbin/dropbearkey");
		}
		publishProgress("" + step++, "" + steps, "Dropbearkey binary");
		if (ShellUtils.chown("/system/xbin/dropbearkey", "0:0") == false) {
			return falseWithError("/system/xbin/dropbearkey");
		}
		publishProgress("" + step++, "" + steps, "Dropbearkey binary");
		if (ShellUtils.chmod("/system/xbin/dropbearkey", "755") == false) {
			return falseWithError("/system/xbin/dropbearkey");
		}
		
		// ssh
		publishProgress("" + step++, "" + steps, "SSH binary");
		if (ShellUtils.lnSymbolic(dropbearmulti, "/system/xbin/ssh") == false) {
			return falseWithError("/system/xbin/ssh");
		}
		publishProgress("" + step++, "" + steps, "SSH binary");
		if (ShellUtils.chown("/system/xbin/ssh", "0:0") == false) {
			return falseWithError("/system/xbin/ssh");
		}
		publishProgress("" + step++, "" + steps, "SSH binary");
		if (ShellUtils.chmod("/system/xbin/ssh", "755") == false) {
			return falseWithError("/system/xbin/ssh");
		}
		
		// scp
		publishProgress("" + step++, "" + steps, "SCP binary");
		if (Utils.copyRawFile(mContext, R.raw.scp, scp) == false) {
			return falseWithError(scp);
		}
		publishProgress("" + step++, "" + steps, "SCP binary");
		if (ShellUtils.lnSymbolic(scp, "/system/xbin/scp") == false) {
			return falseWithError("/system/xbin/scp");
		}
		publishProgress("" + step++, "" + steps, "SCP binary");
		if (ShellUtils.chown("/system/xbin/scp", "0:0") == false) {
			return falseWithError("/system/xbin/scp");
		}
		publishProgress("" + step++, "" + steps, "SCP binary");
		if (ShellUtils.chmod("/system/xbin/scp", "755") == false) {
			return falseWithError("/system/xbin/scp");
		}

		// authorized_keys
		publishProgress("" + step++, "" + steps, "Authorized keys");
		ServerUtils.createIfNeeded(authorized_keys);
		publishProgress("" + step++, "" + steps, "Authorized keys");
		if (ShellUtils.touch(authorized_keys) == false) {
			return falseWithError(authorized_keys);
		}
		
		// host_rsa
		publishProgress("" + step++, "" + steps, "Host RSA key");
		if (ShellUtils.chown(host_rsa, "0:0") == false) {
			return falseWithError(host_rsa);
		}
		publishProgress("" + step++, "" + steps, "Host RSA key");
		if (ServerUtils.generateRsaPrivateKey(host_rsa) == false) {
			return falseWithError(host_rsa);
		}
		
		// host_dss
		publishProgress("" + step++, "" + steps, "Host DSS key");
		if (ShellUtils.chown(host_dss, "0:0") == false) {
			return falseWithError(host_dss);
		}
		publishProgress("" + step++, "" + steps, "Host DSS key");
		if (ServerUtils.generateDssPrivateKey(host_dss) == false) {
			return falseWithError(host_dss);
		}
		
		// pid
		publishProgress("" + step++, "" + steps, "ProcessId file");
		ServerUtils.createIfNeeded(pid);
		publishProgress("" + step++, "" + steps, "ProcessId file");
		if (ShellUtils.echoToFile("0", pid) == false) {
			return falseWithError(pid);
		}

		// system ro
		publishProgress("" + step++, "" + steps, "/system Read-Only");
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