package me.shkschneider.dropbearserver.Tasks;

import me.shkschneider.dropbearserver.Utils.ServerUtils;
import me.shkschneider.dropbearserver.Utils.ShellUtils;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class ServerStopper extends AsyncTask<Void, String, Boolean> {

	public Context mContext = null;
	public ProgressDialog mProgressDialog = null;

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

	@Override
	protected Boolean doInBackground(Void... params) {
		
		ShellUtils.kill(9, ServerUtils.getServerPidFromPs());
		//ShelUtils.killall("dropbear");
		
		return true;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		mProgressDialog.dismiss();
		if (mCallback != null) {
			mCallback.onServerStopperComplete(result);
		}
	}
}