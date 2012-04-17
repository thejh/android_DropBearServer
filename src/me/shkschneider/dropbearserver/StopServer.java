package me.shkschneider.dropbearserver;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class StopServer extends AsyncTask<Void, String, Boolean>
{
	public static final String TAG = "StopServer";

	public Context mContext;
	public ProgressDialog mProgressDialog;

	public void init(Context context) {
		mContext = context;
		mProgressDialog = new ProgressDialog(mContext);
		mProgressDialog.setTitle("Starting server");
		mProgressDialog.setMessage("Please wait...");
		mProgressDialog.setCancelable(false);
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		mProgressDialog.show();
		Toast.makeText(mContext, TAG + ": onPreExecute()", Toast.LENGTH_LONG).show();
	}

	@Override
	protected void onProgressUpdate(String... progress) {
		super.onProgressUpdate(progress);
		mProgressDialog.setTitle(progress[0]);
		mProgressDialog.setMessage(progress[1]);
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
		mProgressDialog.dismiss();
		if (result == true) {
			Toast.makeText(mContext, TAG + ": onPostExecute(true)", Toast.LENGTH_LONG).show();
		}
		else {
			Toast.makeText(mContext, TAG + ": onPostExecute(false)", Toast.LENGTH_LONG).show();
		}
	}
}