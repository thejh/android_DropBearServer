package me.shkschneider.dropbearserver.Tasks;

import me.shkschneider.dropbearserver.Utils.ServerUtils;
import me.shkschneider.dropbearserver.Utils.ShellUtils;
import me.shkschneider.dropbearserver.Utils.Utils;
import me.shkschneider.dropbearserver.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class DropbearInstaller extends AsyncTask<Void, String, Boolean> {

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

	@Override
	protected Boolean doInBackground(Void... params) {
		int step = 0;
		int steps = 20;
		
		// read-write
		publishProgress("" + step++, "" + steps, "/system read-write");
		if (Utils.remountReadWrite("/system") == false)
			return false;

		// data/dropbear
		publishProgress("" + step++, "" + steps, "/data/dropbear");
		if (ShellUtils.mkdir("/data/dropbear") == false)
			return false;
		publishProgress("" + step++, "" + steps, "/data/dropbear");
		if (ShellUtils.chown("/data/dropbear", "0:0") == false)
			return false;
		publishProgress("" + step++, "" + steps, "/data/dropbear");
		if (ShellUtils.chmod("/data/dropbear", "755") == false)
			return false;

		// system/xbin
		publishProgress("" + step++, "" + steps, mContext.getCacheDir() + "/dropbearmulti");
		if (Utils.copyRawFile(mContext, R.raw.dropbearmulti, mContext.getCacheDir() + "/dropbearmulti") == false)
			return false;
		publishProgress("" + step++, "" + steps, mContext.getCacheDir() + "/dropbearmulti");
		if (ShellUtils.chmod(mContext.getCacheDir() + "/dropbearmulti", "755") == false)
			return false;
		publishProgress("" + step++, "" + steps, "/system/xbin/dropbear");
		if (ShellUtils.lnSymbolic(mContext.getCacheDir() + "/dropbearmulti", "/system/xbin/dropbear") == false)
			return false;
		publishProgress("" + step++, "" + steps, "/system/xbin/dropbearkey");
		if (ShellUtils.lnSymbolic(mContext.getCacheDir() + "/dropbearmulti", "/system/xbin/dropbearkey") == false)
			return false;
		publishProgress("" + step++, "" + steps, "/system/xbin/ssh");
		if (ShellUtils.lnSymbolic(mContext.getCacheDir() + "/dropbearmulti", "/system/xbin/ssh") == false)
			return false;
		publishProgress("" + step++, "" + steps, "/system/xbin/scp");
		if (ShellUtils.lnSymbolic(mContext.getCacheDir() + "/dropbearmulti", "/system/xbin/scp") == false)
			return false;
		
		// data/dropbear/rsa
		publishProgress("" + step++, "" + steps, "/data/dropbear/host_rsa");
		if (ServerUtils.generateRsaPrivateKey("/data/dropbear/host_rsa") == false)
			return false;
		publishProgress("" + step++, "" + steps, "/data/dropbear/host_rsa");
		if (ShellUtils.chown("/data/dropbear/host_rsa", "0:0") == false)
			return false;
		publishProgress("" + step++, "" + steps, "/data/dropbear/host_rsa");
		if (ShellUtils.chmod("/data/dropbear/host_rsa", "644") == false)
			return false;
		
		// data/dropbear/dss
		publishProgress("" + step++, "" + steps, "/data/dropbear/host_dss");
		if (ServerUtils.generateDssPrivateKey("/data/dropbear/host_dss") == false)
			return false;
		publishProgress("" + step++, "" + steps, "/data/dropbear/host_dss");
		if (ShellUtils.chown("/data/dropbear/host_dss", "0:0") == false)
			return false;
		publishProgress("" + step++, "" + steps, "/data/dropbear/host_dss");
		if (ShellUtils.chmod("/data/dropbear/host_dss", "644") == false)
			return false;

		// data/dropbear/authorized_keys
		publishProgress("" + step++, "" + steps, "/data/dropbear/authorized_keys");
		if (ShellUtils.touch("/data/dropbear/authorized_keys") == false)
			return false;
		publishProgress("" + step++, "" + steps, "/data/dropbear/authorized_keys");
		if (ShellUtils.chown("/data/dropbear/authorized_keys", "0:0") == false)
			return false;
		publishProgress("" + step++, "" + steps, "/data/dropbear/authorized_keys");
		if (ShellUtils.chmod("/data/dropbear/authorized_keys", "600") == false)
			return false;

		// read-only
		publishProgress("" + step++, "" + steps, "/system read-only");
		if (Utils.remountReadOnly("/system") == false)
			return false;
		
		return true;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		mProgressDialog.dismiss();
		if (mCallback != null) {
			mCallback.onDropbearInstallerComplete(result);
		}
	}
}