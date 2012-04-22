package me.shkschneider.dropbearserver.Tasks;

import me.shkschneider.dropbearserver.Utils.ShellUtils;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class ServerStopper extends AsyncTask<Void, String, Boolean>
{
	public static final String TAG = "StopServer";

	public Context mContext;
	public ProgressDialog mProgressDialog;
	
	private ServerStopperCallback<Boolean> mCallback;

	public ServerStopper(Context context, ServerStopperCallback<Boolean> callback) {
		mContext = context;
        mCallback = callback;
		mProgressDialog = new ProgressDialog(mContext);
		mProgressDialog.setTitle("Stopping server");
		mProgressDialog.setMessage("Please wait...");
		mProgressDialog.setCancelable(false);
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		mProgressDialog.show();
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		// dropbear
		ShellUtils.commands.add("killall dropbear");
		if (ShellUtils.execute() == false)
			return false;
		
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