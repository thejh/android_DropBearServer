package me.shkschneider.dropbearserver.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.stericson.RootTools.RootTools;

public abstract class Utils
{
	public static String TAG = "Utils";

	public static void marketNotFound(Context context)
	{
		Toast.makeText(context, "Market not found!", Toast.LENGTH_SHORT).show();
	}
	
	public static final Boolean copyRawFile(Context context, int rawId, String path) {
		try {
			InputStream in = context.getResources().openRawResource(rawId);
			OutputStream out = new FileOutputStream(new File(path));
			byte[] buffer = new byte[1024];
			int read;
			while ((read = in.read(buffer)) != -1) {
				out.write(buffer, 0, read);
			}
			in.close();
			out.flush();
			out.close();
		} catch(Exception e) {
			Log.e(TAG, "copyRawFile(): " + e.getMessage());
			return false;
		}
		return true;
	}
	
	public static final Boolean remountReadWrite(String path) {
		return RootTools.remount(path, "RW");
	}
	
	public static final Boolean remountReadOnly(String path) {
		return RootTools.remount(path, "RO");
	}
}
