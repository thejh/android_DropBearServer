package me.shkschneider.dropbearserver.Tasks;

import me.shkschneider.dropbearserver.Utils.ShellUtils;
import me.shkschneider.dropbearserver.Utils.Utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class DropbearRemover extends AsyncTask<Void, String, Boolean>
{
	public static final String TAG = "DropbearInstaller";

	private Context mContext;
	private ProgressDialog mProgressDialog;
	
	private DropbearRemoverCallback<Boolean> mCallback;

	public DropbearRemover(Context context, DropbearRemoverCallback<Boolean> callback) {
		mContext = context;
        mCallback = callback;
		mProgressDialog = new ProgressDialog(mContext);
		mProgressDialog.setTitle("Removing Dropbear");
		mProgressDialog.setMessage("Please wait...");
		mProgressDialog.setCancelable(false);
    }
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		mProgressDialog.show();
	}

	@Override
	protected void onProgressUpdate(String... progress) {
		super.onProgressUpdate(progress);
		Float f = (Float.parseFloat(progress[0] + ".0") / Float.parseFloat(progress[1] + ".0") * 100);
		mProgressDialog.setTitle("Removing: " + Math.round(f) + "%");
		mProgressDialog.setMessage(progress[2]);
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		int step = 0;
		int steps = 5;
		
		// read-write
		publishProgress("" + step++, "" + steps, "/system read-write");
		if (Utils.remountReadWrite("/system") == false)
			return false;

		// data/dropbear
		publishProgress("" + step++, "" + steps, "/data/dropbear");
		if (ShellUtils.rmRecursive("/data/dropbear") == false)
			return false;;

		// system/xbin
		publishProgress("" + step++, "" + steps, "/system/xbin/dropbear");
		if (ShellUtils.rm("/system/xbin/dropbear") == false)
			return false;
		publishProgress("" + step++, "" + steps, "/system/xbin/dropbearkey");
		if (ShellUtils.rm("/system/xbin/dropbearkey") == false)
			return false;

		// read-only
		publishProgress("" + step++, "" + steps, "/system read-only");
		if (Utils.remountReadOnly("/system") == false)
			return false;
		return true;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		mProgressDialog.dismiss();
		if (result == true) {
			Toast.makeText(mContext, TAG + ": onPostExecute(true)", Toast.LENGTH_SHORT).show();
		}
		else {
			Toast.makeText(mContext, TAG + ": onPostExecute(false)", Toast.LENGTH_SHORT).show();
		}
		if (mCallback != null) {
			mCallback.onDropbearRemoverComplete(result);
		}
	}
}