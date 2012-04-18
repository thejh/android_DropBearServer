package me.shkschneider.dropbearserver.Tasks;

import me.shkschneider.dropbearserver.R;
import me.shkschneider.dropbearserver.Utils.ShellUtils;
import me.shkschneider.dropbearserver.Utils.Utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class DropbearInstaller extends AsyncTask<Void, String, Boolean>
{
	public static final String TAG = "DropbearInstaller";

	private Context mContext;
	private ProgressDialog mProgressDialog;
	
	private DropbearInstallerCallback<Boolean> mCallback;

	public DropbearInstaller(Context context, DropbearInstallerCallback<Boolean> callback) {
		mContext = context;
        mCallback = callback;
		mProgressDialog = new ProgressDialog(mContext);
		mProgressDialog.setTitle("Installing Dropbear");
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
		mProgressDialog.setTitle("Installing: " + Math.round(f) + "%");
		mProgressDialog.setMessage(progress[2]);
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		int step = 0;
		int steps = 25;
		
		// read-write
		publishProgress("" + step++, "" + steps, "/system read-write");
		if (Utils.remountReadWrite("/system") == false)
			return false;

		// data/dropbear
		publishProgress("" + step++, "" + steps, "/data/dropbear");
		if (ShellUtils.mkdir("/data/dropbear") == false)
			return false;
		publishProgress("" + step++, "" + steps, "/data/dropbear");
		if (ShellUtils.chown("/data/dropbear", "root.root") == false)
			return false;
		publishProgress("" + step++, "" + steps, "/data/dropbear");
		if (ShellUtils.chmod("/data/dropbear", "755") == false)
			return false;
		
		// data/dropbear/rsa
		publishProgress("" + step++, "" + steps, "/data/dropbear/rsa");
		if (ShellUtils.touch("/data/dropbear/rsa") == false)
			return false;
		publishProgress("" + step++, "" + steps, "/data/dropbear/rsa");
		if (ShellUtils.chown("/data/dropbear/rsa", "root.root") == false)
			return false;
		publishProgress("" + step++, "" + steps, "/data/dropbear/rsa");
		if (ShellUtils.chmod("/data/dropbear/rsa", "644") == false)
			return false;
		
		// data/dropbear/dss
		publishProgress("" + step++, "" + steps, "/data/dropbear/dss");
		if (ShellUtils.touch("/data/dropbear/dss") == false)
			return false;
		publishProgress("" + step++, "" + steps, "/data/dropbear/dss");
		if (ShellUtils.chown("/data/dropbear/dss", "root.root") == false)
			return false;
		publishProgress("" + step++, "" + steps, "/data/dropbear/dss");
		if (ShellUtils.chmod("/data/dropbear/dss", "644") == false)
			return false;
		
		// data/dropbear/.ssh
		publishProgress("" + step++, "" + steps, "/data/dropbear/.ssh");
		if (ShellUtils.mkdir("/data/dropbear/.ssh") == false)
			return false;
		publishProgress("" + step++, "" + steps, "/data/dropbear/.ssh");
		if (ShellUtils.chown("/data/dropbear/.ssh", "root.root") == false)
			return false;
		publishProgress("" + step++, "" + steps, "/data/dropbear/.ssh");
		if (ShellUtils.chmod("/data/dropbear/.ssh", "700") == false)
			return false;
		
		// data/dropbear/.ssh/authorized_keys
		publishProgress("" + step++, "" + steps, "/data/dropbear/.ssh/authorized_keys");
		if (ShellUtils.touch("/data/dropbear/.ssh/authorized_keys") == false)
			return false;
		publishProgress("" + step++, "" + steps, "/data/dropbear/.ssh/authorized_keys");
		if (ShellUtils.chown("/data/dropbear/.ssh/authorized_keys", "root.root") == false)
			return false;
		publishProgress("" + step++, "" + steps, "/data/dropbear/.ssh/authorized_keys");
		if (ShellUtils.chmod("/data/dropbear/.ssh/authorized_keys", "root.root") == false)
			return false;

		// system/xbin
		publishProgress("" + step++, "" + steps, "/system/xbin/dropbear");
		if (Utils.copyRawFile(mContext, R.raw.dropbear, mContext.getCacheDir() + "/dropbear") == false)
			return false;
		publishProgress("" + step++, "" + steps, "/system/xbin/dropbear");
		if (ShellUtils.mv(mContext.getCacheDir() + "/dropbear", "/system/xbin/dropbear") == false)
			return false;
		publishProgress("" + step++, "" + steps, "/system/xbin/dropbear");
		if (ShellUtils.chown("/system/xbin/dropbear", "root.root") == false)
			return false;
		publishProgress("" + step++, "" + steps, "/system/xbin/dropbear");
		if (ShellUtils.chmod("/system/xbin/dropbear", "755") == false)
			return false;
		publishProgress("" + step++, "" + steps, "/system/xbin/dropbearkey");
		if (Utils.copyRawFile(mContext, R.raw.dropbearkey, mContext.getCacheDir() + "/dropbearkey") == false)
			return false;
		publishProgress("" + step++, "" + steps, "/system/xbin/dropbearkey");
		if (ShellUtils.mv(mContext.getCacheDir() + "/dropbearkey", "/system/xbin/dropbearkey") == false)
			return false;
		publishProgress("" + step++, "" + steps, "/system/xbin/dropbearkey");
		if (ShellUtils.chown("/system/xbin/dropbearkey", "root.root") == false)
			return false;
		publishProgress("" + step++, "" + steps, "/system/xbin/dropbearkey");
		if (ShellUtils.chmod("/system/xbin/dropbearkey", "755") == false)
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
			mCallback.onDropbearInstallerComplete(result);
		}
	}
}