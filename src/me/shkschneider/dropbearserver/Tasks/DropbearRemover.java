package me.shkschneider.dropbearserver.Tasks;

import me.shkschneider.dropbearserver.Pages.ServerPage;
import me.shkschneider.dropbearserver.Utils.ServerUtils;
import me.shkschneider.dropbearserver.Utils.ShellUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

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
		Toast.makeText(mContext, "Error: " + error, Toast.LENGTH_LONG).show();
		return false;
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		Log.i(TAG, "DropbearRemover: doInBackground()");
		
		int step = 0;
		int steps = 11;
		
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

		// dropbear
		publishProgress("" + step++, "" + steps, dropbear);
		if (ShellUtils.rm(dropbear) == false) {
			return falseWithError(dropbear);
		}

		// dropbearkey
		publishProgress("" + step++, "" + steps, dropbearkey);
		if (ShellUtils.rm(dropbearkey) == false) {
			return falseWithError(dropbearkey);
		}

		// ssh
		publishProgress("" + step++, "" + steps, ssh);
		if (ShellUtils.rm(ssh) == false) {
			return falseWithError(ssh);
		}

		// scp
		publishProgress("" + step++, "" + steps, scp);
		if (ShellUtils.rm(scp) == false) {
			return falseWithError(scp);
		}

		// dropbearmulti
		publishProgress("" + step++, "" + steps, dropbearmulti);
		if (ShellUtils.rm(dropbearmulti) == false) {
			return falseWithError(dropbearmulti);
		}

		// authorized_keys
		publishProgress("" + step++, "" + steps, authorized_keys);
		if (ShellUtils.rm(authorized_keys) == false) {
			return falseWithError(authorized_keys);
		}
		
		// host_rsa
		publishProgress("" + step++, "" + steps, host_rsa);
		if (ShellUtils.rm(host_rsa) == false) {
			return falseWithError(host_rsa);
		}
		
		// host_dss
		publishProgress("" + step++, "" + steps, host_dss);
		if (ShellUtils.rm(host_dss) == false) {
			return falseWithError(host_dss);
		}
		
		// pid
		publishProgress("" + step++, "" + steps, pid);
		if (ShellUtils.rm(pid) == false) {
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
			mCallback.onDropbearRemoverComplete(result);
		}
	}
}