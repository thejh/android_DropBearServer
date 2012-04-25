/*
 * Michael Almyros <http://www.dreamincode.net/forums/topic/190013-creating-simple-file-chooser/>
 */
package me.shkschneider.dropbearserver.Explorer;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import com.markupartist.android.widget.ActionBar;

import me.shkschneider.dropbearserver.R;
import me.shkschneider.dropbearserver.Utils.ServerUtils;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ExplorerActivity extends ListActivity implements DialogInterface.OnClickListener {

	private static final String TAG = "DropBearServer";

	private ActionBar mActionBar;
	private TextView mCurrentPath;
	private String mPublicKey;

	private File mDir;
	private File mSdcard;
	private ExplorerAdapter mExplorerAdapter;
	Stack<File> mDirStack = new Stack<File>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ListView listView = getListView();
		LayoutInflater layoutInflater = getLayoutInflater();
		ViewGroup header = (ViewGroup) layoutInflater.inflate(R.layout.explorer_header, listView, false);
		listView.addHeaderView(header, null, false);

		// Header
		mActionBar = (ActionBar) header.findViewById(R.id.actionbar);
		mActionBar.setTitle(getResources().getString(R.string.app_name));
		mActionBar.setHomeAction(new HomeAction(this));
		mActionBar.addAction(new CancelAction(this));

		mCurrentPath = (TextView) header.findViewById(R.id.current_path);
		mCurrentPath.setText("SDCard: /");

		// Explorer
		mSdcard = Environment.getExternalStorageDirectory();
		mDir = new File(mSdcard.toString());
		fill(mDir);
	}

	private void fill(File path)
	{
		File[] content = path.listFiles();
		List<ExplorerItem> dirs = new ArrayList<ExplorerItem>();
		List<ExplorerItem> files = new ArrayList<ExplorerItem>();

		mCurrentPath.setText("SDCard: " + path.toString().replaceFirst("^" + mSdcard.toString() + "/?", "/"));

		try {
			for (File file : content) {
				if (file.getName().startsWith(".") == false) {
					if (file.isDirectory() == true)
						dirs.add(new ExplorerItem(file.getName(), file.getAbsolutePath(), true));
					else
						files.add(new ExplorerItem(file.getName(), file.getAbsolutePath(), false));
				}
			}
		}
		catch (Exception e) {
			Log.e(TAG, "ExplorerActivity: fill(): " + e.getMessage());
		}

		// After this, files should be after directories
		Collections.sort(dirs);
		Collections.sort(files);
		dirs.addAll(files);

		if (mCurrentPath.getText().equals("SDCard: /") == false)
			dirs.add(0, new ExplorerItem("..", path.getParent(), true));

		mExplorerAdapter = new ExplorerAdapter(ExplorerActivity.this, R.layout.explorer_list, dirs);
		this.setListAdapter(mExplorerAdapter);
	}

	@Override
	protected void onListItemClick(ListView listView, View view, int position, long id) {
		super.onListItemClick(listView, view, position, id);

		/*
		 * I did not understand why, but since I added the header, the position is 1 too big...
		 */
		position -= 1;

		ExplorerItem item = mExplorerAdapter.getItem(position);
		File f = new File(item.getPath());
		if (f.isDirectory() == true) {
			mDirStack.push(mDir);
			mDir = f;
			fill(mDir);
		}
		else if (item.getName().equals("..")) {
			mDir = mDirStack.pop();
			fill(mDir);
		}
		else {
			onFileClick(item);
		}
	}

	private void onFileClick(ExplorerItem item) {
		mPublicKey = null;
		try {
			FileInputStream fis = new FileInputStream(item.getPath());
			DataInputStream dis = new DataInputStream(fis);
			BufferedReader br = new BufferedReader(new InputStreamReader(dis));
			String line = br.readLine();
			dis.close();
			// validates the PublicKey
			if (line != null && line.startsWith("ssh-rsa ") == true)
				mPublicKey = line;
		}
		catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
		if (mPublicKey == null) {
			Toast.makeText(this, "Error: Invalid public key", Toast.LENGTH_SHORT).show();
		}
		else {
			new AlertDialog.Builder(this)
			.setTitle("Confirm")
			.setMessage(mPublicKey)
			.setCancelable(true)
			.setPositiveButton("Okay", this)
			.setNegativeButton("Cancel", this)
			.show();
		}
	}

	@Override
	public void onBackPressed() {
		if (mDirStack.size() > 0) {
			mDir = mDirStack.pop();
			fill(mDir);
		}
	}

	public void onClick(DialogInterface dialog, int button) {
		if (button == DialogInterface.BUTTON_POSITIVE) {
			ServerUtils.addPublicKey(mPublicKey);
			Toast.makeText(this, "Public key successfully added", Toast.LENGTH_SHORT).show();
			finish();
		}
	}

}

