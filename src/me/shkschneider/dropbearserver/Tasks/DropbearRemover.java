package me.shkschneider.dropbearserver.Tasks;

import me.shkschneider.dropbearserver.Utils.ServerUtils;
import me.shkschneider.dropbearserver.Utils.ShellUtils;
import me.shkschneider.dropbearserver.Utils.Utils;

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
		if (ServerUtils.getServerPidFromPs() > 0) {
			// ServerStopper
			ServerStopper serverStopper = new ServerStopper(mContext, null);
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
	
	// TODO: log errors

	@Override
	protected Boolean doInBackground(Void... params) {
		Log.i(TAG, "DropbearRemover: doInBackground()");
		
		int step = 0;
		int steps = 9;

		// read-write
		publishProgress("" + step++, "" + steps, "/system read-write");
		if (Utils.remountReadWrite("/system") == false)
			return false;

		// data/dropbear
		publishProgress("" + step++, "" + steps, "/data/dropbear");
		ShellUtils.rmRecursive("/data/dropbear");
		
		// data/data
		publishProgress("" + step++, "" + steps, ServerUtils.getLocalDir(mContext) + "/dropbearmulti");
		ShellUtils.rm(ServerUtils.getLocalDir(mContext) + "/dropbearmulti");
		
		// system/xbin
		publishProgress("" + step++, "" + steps, "/system/xbin/dropbear");
		ShellUtils.rm("/system/xbin/dropbear");
		publishProgress("" + step++, "" + steps, "/system/xbin/dropbearkey");
		ShellUtils.rm("/system/xbin/dropbearkey");
		publishProgress("" + step++, "" + steps, "/system/xbin/ssh");
		ShellUtils.rm("/system/xbin/ssh");
		publishProgress("" + step++, "" + steps, "/system/xbin/scp");
		ShellUtils.rm("/system/xbin/scp");

		// read-only
		publishProgress("" + step++, "" + steps, "/system read-only");
		if (Utils.remountReadOnly("/system") == false)
			return false;
		
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