package me.shkschneider.dropbearserver.Tasks;

import me.shkschneider.dropbearserver.Utils.RootUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class Checker extends AsyncTask<Void, String, Boolean>
{
	public static final String TAG = "Checker";

	private Context mContext;
	private ProgressDialog mProgressDialog;
	
	private CheckerCallback<Boolean> mCallback;

	public Checker(Context context, CheckerCallback<Boolean> callback) {
		mContext = context;
        mCallback = callback;
		mProgressDialog = new ProgressDialog(mContext);
		mProgressDialog.setTitle("Checking dependencies");
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
		mProgressDialog.setTitle("Checking: " + Math.round(f) + "%");
		mProgressDialog.setMessage(progress[2]);
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		int step = 0;
		int steps = 3;
		
		// root
		publishProgress("" + step++, "" + steps, "Root access");
		RootUtils.checkRootAccess();
		// busybox
		publishProgress("" + step++, "" + steps, "Busybox");
		RootUtils.checkBusybox();
		// dropbear
		publishProgress("" + step++, "" + steps, "Dropbear");
		RootUtils.checkDropbear();
		
		return (RootUtils.hasRootAccess && RootUtils.hasBusybox && RootUtils.hasDropbear);
	}

	@Override
	protected void onPostExecute(Boolean result) {
		mProgressDialog.dismiss();
		Toast.makeText(mContext, TAG + ": onPostExecute(" + result + ")", Toast.LENGTH_SHORT).show();
		if (mCallback != null) {
			mCallback.onCheckerComplete(result);
		}
	}
}