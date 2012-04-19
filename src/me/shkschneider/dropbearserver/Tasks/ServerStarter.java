package me.shkschneider.dropbearserver.Tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class ServerStarter extends AsyncTask<Void, String, Boolean>
{
	public static final String TAG = "StartServer";

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
	protected void onProgressUpdate(String... progress) {
		super.onProgressUpdate(progress);
		if (mProgressDialog != null) {
			mProgressDialog.setTitle(progress[0]);
			mProgressDialog.setMessage(progress[1]);
		}
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			Log.w(TAG, "doInBackground(): " + e.getMessage());
		}
		return true;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
		}
		if (mCallback != null) {
			mCallback.onServerStarterComplete(result);
		}
	}
}