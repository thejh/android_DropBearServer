package me.shkschneider.dropbearserver;

import me.shkschneider.dropbearserver.Utils.ShellUtils;
import me.shkschneider.dropbearserver.Utils.Utils;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

public class DropbearInstaller extends AsyncTask<Void, String, Boolean>
{
	public static final String TAG = "DropbearInstaller";

	private Context mContext;
	private ProgressDialog mProgressDialog;

	public void init(Context context) {
		mContext = context;
		mProgressDialog = new ProgressDialog(mContext);
		mProgressDialog.setTitle("Installing Dropbear");
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
		int step = 1;
		int steps = 10;
		
		// read-write
		publishProgress(step++ + "/" + steps, "/system read-write");
		Utils.remountReadWrite("/system");

		// data/dropbear
		publishProgress(step++ + "/" + steps, "/data/dropbear");
		if (ShellUtils.mkdir("/data/dropbear", "root.root", "755") == false)
			return false;
		publishProgress(step++ + "/" + steps, "/data/dropbear/rsa");
		if (ShellUtils.touch("/data/dropbear/rsa", "root.root", "644") == false)
			return false;
		publishProgress(step++ + "/" + steps, "/data/dropbear/dss");
		if (ShellUtils.touch("/data/dropbear/dss", "root.root", "644") == false)
			return false;
		publishProgress(step++ + "/" + steps, "/data/dropbear/pid");
		if (ShellUtils.touch("/data/dropbear/pid", "root.root", "600") == false)
			return false;
		publishProgress(step++ + "/" + steps, "/data/dropbear/.ssh");
		if (ShellUtils.mkdir("/data/dropbear/.ssh", "root.root", "700") == false)
			return false;
		publishProgress(step++ + "/" + steps, "/data/dropbear/.ssh/authorized_keys");
		if (ShellUtils.touch("/data/dropbear/.ssh/authorized_keys", "root.root", "600") == false)
			return false;

		// system/xbin
		publishProgress(step++ + "/" + steps, "/system/xbin/dropbear");
		if (Utils.copyRawFile(mContext, R.raw.dropbear, Environment.getExternalStorageDirectory() + "/dropbear") == false)
			return false;
		if (ShellUtils.mv("/sdcard/dropbear", "/system/xbin/dropbear", "root.root", "755") == false)
			return false;
		publishProgress(step++ + "/" + steps, "/system/xbin/dropbearkey");
		if (Utils.copyRawFile(mContext, R.raw.dropbearkey, Environment.getExternalStorageDirectory() + "/dropbearkey") == false)
			return false;
		if (ShellUtils.mv("/sdcard/dropbearkey", "/system/xbin/dropbearkey", "root.root", "755") == false)
			return false;

		// read-only
		publishProgress(step++ + "/" + steps, "/system read-only");
		Utils.remountReadOnly("/system");
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