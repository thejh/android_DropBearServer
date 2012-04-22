package me.shkschneider.dropbearserver.Tasks;

import me.shkschneider.dropbearserver.Utils.ShellUtils;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class ServerStarter extends AsyncTask<Void, String, Boolean>
{
	public static final String TAG = "StartServer";

	public Context mContext;
	public ProgressDialog mProgressDialog;
	
	private ServerStarterCallback<Boolean> mCallback;

	public ServerStarter(Context context, ServerStarterCallback<Boolean> callback) {
		mContext = context;
        mCallback = callback;
		mProgressDialog = new ProgressDialog(mContext);
		mProgressDialog.setTitle("Starting server");
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
		ShellUtils.commands.add("dropbear -s");
		if (ShellUtils.execute() == false)
			return false;
		
		return true;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		mProgressDialog.dismiss();
		if (mCallback != null) {
			mCallback.onServerStarterComplete(result);
		}
	}
}