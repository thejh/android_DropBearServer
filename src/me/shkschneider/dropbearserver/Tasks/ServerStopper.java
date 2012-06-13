package me.shkschneider.dropbearserver.Tasks;

import me.shkschneider.dropbearserver.Utils.ServerUtils;
import me.shkschneider.dropbearserver.Utils.ShellUtils;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class ServerStopper extends AsyncTask<Void, String, Boolean> {

	private static final String TAG = "DropBearServer";

	private Context mContext = null;
	private ProgressDialog mProgressDialog = null;

	private ServerStopperCallback<Boolean> mCallback;

	public ServerStopper(Context context, ServerStopperCallback<Boolean> callback) {
		mContext = context;
		mCallback = callback;
		if (mContext != null) {
			mProgressDialog = new ProgressDialog(mContext);
			mProgressDialog.setTitle("Stopping server");
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
		Log.d(TAG, "ServerStopper: " + error);
		//Toast.makeText(mContext, "Error: " + error, Toast.LENGTH_LONG).show();
		return false;
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		Log.i(TAG, "ServerStopper: doInBackground()");

		String pidFile = ServerUtils.getLocalDir(mContext) + "/pid";
		ShellUtils.rm(pidFile);

		String lockFile = ServerUtils.getLocalDir(mContext) + "/lock";
		if (ShellUtils.echoToFile("0", lockFile) == false)
			return falseWithError("echoToFile(0, " + lockFile + ")");

		Log.i(TAG, "ServerStopper: Killing processes");
		if (ShellUtils.killall("dropbear") == false)
			return falseWithError("killall(dropbear)");

		return true;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
		}
		if (mCallback != null) {
			mCallback.onServerStopperComplete(result);
		}
	}
}