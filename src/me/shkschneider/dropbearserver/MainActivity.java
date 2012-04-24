package me.shkschneider.dropbearserver;

import com.astuetz.viewpagertabs.ViewPagerTabs;
import com.markupartist.android.widget.ActionBar;

import me.shkschneider.dropbearserver.R;
import me.shkschneider.dropbearserver.Tasks.Checker;
import me.shkschneider.dropbearserver.Tasks.CheckerCallback;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;

public class MainActivity extends Activity implements CheckerCallback<Boolean> {

	private static final String TAG = "MainActivity";

	private static Boolean needToCheckDependencies = true;
	
	private ViewPager mPager;
	private ViewPagerTabs mPagerTabs;
	private MainAdapter mAdapter;

	private ActionBar mActionBar;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate()");
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		// Introduction
		String appName = getResources().getString(R.string.app_name);
		String appVersion = "0";
		String packageName = getApplication().getPackageName();
		try {
			PackageInfo packageInfo = getPackageManager().getPackageInfo(packageName, PackageManager.GET_META_DATA);
			appVersion = packageInfo.versionName.toString();
		}
		catch (Exception e) {
			Log.e(TAG, "onCreate(): " + e.getMessage());
		}
		Log.i(TAG, appName + " v" + appVersion + " (" + packageName + ") Android " + Build.VERSION.RELEASE + " (API-" + Build.VERSION.SDK + ")");
		
		// Header
		mActionBar = (ActionBar) findViewById(R.id.actionbar);
		mActionBar.setTitle(getResources().getString(R.string.app_name));
		mActionBar.setHomeAction(new HomeAction(this));
		mActionBar.addAction(new CheckAction(this));

		// ViewPagerTabs
		mAdapter = new MainAdapter(this);
		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);
		mPagerTabs = (ViewPagerTabs) findViewById(R.id.tabs);
		mPagerTabs.setViewPager(mPager);
		goToDefaultPage();
	}
	
	@Override
	public void onResume() {
		Log.d(TAG, "onResume()");
		
		super.onResume();
		if (needToCheckDependencies == true) {
			// Root dependencies
			check();
			needToCheckDependencies = false;
		}
	}

	public static Intent createIntent(Context context) {
		Intent i = new Intent(context, MainActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		return i;
	}

	public void goToDefaultPage() {
		mPager.setCurrentItem(MainAdapter.DEFAULT_PAGE);
	}
	
	public void check() {
		Log.d(TAG, "check()");
		// Checker
		Checker checker = new Checker(this, this);
		checker.execute();
	}

	public void onCheckerComplete(Boolean result) {
		update();
	}
	
	public void update() {
		Log.d(TAG, "update()");
		mAdapter.update();
	}
}