package me.shkschneider.dropbearserver.Tasks;

import me.shkschneider.dropbearserver.Pages.ServerPage;
import me.shkschneider.dropbearserver.Utils.ServerUtils;
import me.shkschneider.dropbearserver.Utils.ShellUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class DropbearRemover extends AsyncTask<Void, String, Boolean> {

	private static final String TAG = "DropBearServer";

	private Context mContext = null;
	private ProgressDialog mProgressDialog = null;

	private DropbearRemoverCallback<Boolean> mCallback;

	public DropbearRemover(Context context, DropbearRemoverCallback<Boolean> callback) {
		mContext = context;
		mCallback = callback;
		if (mContext != null) {
			mProgressDialog = new ProgressDialog(mContext);
			mProgressDialog.setTitle("Removing Dropbear");
			mProgressDialog.setMessage("Please wait...");
			mProgressDialog.setCancelable(false);
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			mProgressDialog.setMax(100);
			mProgressDialog.setIcon(0);
		}
	}

	@Override
	protected void onPreExecute() {
		if (ServerPage.mServerPid > 0) {
			// ServerStopper
			ServerStopper serverStopper = new ServerStopper(mContext, null, ServerPage.mServerPid);
			serverStopper.execute();
		}
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
		Log.d(TAG, "DropBearRemover: " + error);
		//Toast.makeText(mContext, "Error: " + error, Toast.LENGTH_LONG).show();
		return false;
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		Log.i(TAG, "DropbearRemover: doInBackground()");
		
		int step = 0;
		int steps = 12;
		
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

		// dropbear
		publishProgress("" + step++, "" + steps, "Dropbear binary");
		if (ShellUtils.rm("/system/xbin/dropbear") == false) {
			return falseWithError("/system/xbin/dropbear");
		}

		// dropbearkey
		publishProgress("" + step++, "" + steps, "Dropbearkey binary");
		if (ShellUtils.rm("/system/xbin/dropbearkey") == false) {
			return falseWithError("/system/xbin/dropbearkey");
		}

		// ssh
		publishProgress("" + step++, "" + steps, "SSH binary");
		if (ShellUtils.rm("/system/xbin/ssh") == false) {
			return falseWithError("/system/xbin/ssh");
		}

		// scp
		publishProgress("" + step++, "" + steps, "SCP binary");
		if (ShellUtils.rm("/system/xbin/scp") == false) {
			return falseWithError("/system/xbin/scp");
		}
		publishProgress("" + step++, "" + steps, "SCP binary");
		if (ShellUtils.rm(scp) == false) {
			return falseWithError(scp);
		}

		// dropbearmulti
		publishProgress("" + step++, "" + steps, "Dropbearmulti binary");
		if (ShellUtils.rm(dropbearmulti) == false) {
			return falseWithError(dropbearmulti);
		}

		// authorized_keys
		publishProgress("" + step++, "" + steps, "Authorized keys");
		if (ShellUtils.rm(authorized_keys) == false) {
			return falseWithError(authorized_keys);
		}
		
		// host_rsa
		publishProgress("" + step++, "" + steps, "Host RSA key");
		if (ShellUtils.rm(host_rsa) == false) {
			return falseWithError(host_rsa);
		}
		
		// host_dss
		publishProgress("" + step++, "" + steps, "Host DSS key");
		if (ShellUtils.rm(host_dss) == false) {
			return falseWithError(host_dss);
		}
		
		// pid
		publishProgress("" + step++, "" + steps, "ProcessId file");
		if (ShellUtils.rm(pid) == false) {
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
			mCallback.onDropbearRemoverComplete(result);
		}
	}
}