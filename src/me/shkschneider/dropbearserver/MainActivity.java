package me.shkschneider.dropbearserver;

import com.astuetz.viewpagertabs.ViewPagerTabs;
import com.markupartist.android.widget.ActionBar;

import me.shkschneider.dropbearserver.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

public class MainActivity extends Activity {

	private static final String TAG = "MainActivity";
	private static final int DEFAULT_PAGE = 1;

	private ViewPager mPager;
	private ViewPagerTabs mPagerTabs;
	private MainAdapter mAdapter;

	private ActionBar mActionBar;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate()");
		setContentView(R.layout.main);

		// Introduction
		String appName = getResources().getString(R.string.app_name);
		String appVersion = "0";
		String packageName = getApplication().getPackageName();
		try {
			PackageInfo packageInfo = getPackageManager().getPackageInfo(packageName, PackageManager.GET_META_DATA);
			appVersion = packageInfo.versionName.toString();
		} catch (NameNotFoundException e) {
			Log.d(TAG, "onCreate(): " + e.getMessage());
		}
		Log.i(TAG, appName + " v" + appVersion + " (" + packageName + ") Android " + Build.VERSION.RELEASE + " (API-" + Build.VERSION.SDK + ")");

		// Header
		mActionBar = (ActionBar) findViewById(R.id.actionbar);
		mActionBar.setTitle("DropBear Server");
		mActionBar.setHomeAction(new HomeAction(this));
		mActionBar.addAction(new CheckAction(this));
	    mActionBar.setProgressBarVisibility(View.VISIBLE);

		// ViewPagerTabs
		mAdapter = new MainAdapter(this);
		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);
		mPagerTabs = (ViewPagerTabs) findViewById(R.id.tabs);
		mPagerTabs.setViewPager(mPager);
		goToDefaultPage();
	}

	public static Intent createIntent(Context context) {
		Intent i = new Intent(context, MainActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		return i;
	}

	@Override
	public void onStart() {
		super.onStart();
		Log.d(TAG, "onStart()");
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.d(TAG, "onResume()");

		// Root dependencies
		check();
		
		// Pages
		mAdapter.update();
	}

	@Override
	public void onStop() {
		super.onStop();
		Log.d(TAG, "onStop()");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy()");
	}

	public void goToDefaultPage() {
		mPager.setCurrentItem(DEFAULT_PAGE);
	}
	
	public void setActionBarProgressBarVisibility(int visibility) {
		mActionBar.setProgressBarVisibility(visibility);
	}
	
	public void check() {
		mAdapter.check();
	}
}