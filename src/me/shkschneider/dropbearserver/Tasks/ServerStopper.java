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
	private Integer mServerPid;

	private ServerStopperCallback<Boolean> mCallback;

	public ServerStopper(Context context, ServerStopperCallback<Boolean> callback, Integer serverPid) {
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
		mServerPid = serverPid;
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
		if (ShellUtils.echoToFile("0", pidFile) == false)
			return falseWithError("echoToFile(0, " + ServerUtils.getLocalDir(mContext) + "/pid" + ")");

		Log.i(TAG, "ServerStopper: Killing process #" + mServerPid);
		if (ShellUtils.kill(9, mServerPid) == false && ShellUtils.killall("dropbear") == false)
			return falseWithError("kill(9, " + mServerPid + ") killall(dropbear)");
		
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