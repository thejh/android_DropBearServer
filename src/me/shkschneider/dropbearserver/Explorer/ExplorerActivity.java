/*
 * Michael Almyros <http://www.dreamincode.net/forums/topic/190013-creating-simple-file-chooser/>
 */
package me.shkschneider.dropbearserver.Explorer;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import com.markupartist.android.widget.ActionBar;

import me.shkschneider.dropbearserver.R;
import me.shkschneider.dropbearserver.Utils.ShellUtils;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ExplorerActivity extends ListActivity {

	private static final String TAG = "DropBearServer";

	private ActionBar mActionBar;
	private TextView mCurrentPath;
	
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

		ShellUtils.touch("/sdcard/id_rsa.pub");
		ShellUtils.mkdirRecursive("/sdcard/test/dir");
		ShellUtils.touch("/sdcard/test/test.pub");
		ShellUtils.touch("/sdcard/test/dir/dir.pub");
		
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
		 * TODO: understand and explain
		 * This is done because somehow the ".." path or the header takes one place
		 */
		position = position - 1;

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
		Toast.makeText(this, item.getPath(), Toast.LENGTH_SHORT).show();
	}   

	@Override
	public void onBackPressed() {
		if (mDirStack.size() > 0) {
			mDir = mDirStack.pop();
			fill(mDir);
		}
	}
	
	public void dismiss() {
		this.finish();
	}

}

