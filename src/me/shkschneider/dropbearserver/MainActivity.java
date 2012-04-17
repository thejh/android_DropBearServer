package me.shkschneider.dropbearserver;

import com.astuetz.viewpagertabs.ViewPagerTabs;
import me.shkschneider.dropbearserver.R;
import me.shkschneider.dropbearserver.Utils.RootUtils;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {

	private static final String TAG = "MainActivity";
	private static final int DEFAULT_PAGE = 1;

	private ViewPager mPager;
	private ViewPagerTabs mPagerTabs;
	private MainAdapter mAdapter;

	private ImageView mLogo;
	private TextView mDropbearVersion;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate()");
		setContentView(R.layout.main);

		// Introduction version
		String appName = getResources().getString(R.string.app_name);
		String appVersion = "0";
		String packageName = getApplication().getPackageName();
		try {
			PackageInfo packageInfo = getPackageManager().getPackageInfo(packageName, PackageManager.GET_META_DATA);
			appVersion = packageInfo.versionName.toString();
		} catch (NameNotFoundException e) {
			Log.d(TAG, "onCreate(): " + e.getMessage());
		}
		Log.i(TAG, appName + " v" + appVersion + " (" + packageName + ")");

		// Header
		mLogo = (ImageView) findViewById(R.id.logo);
		mLogo.setOnClickListener(this);
		mDropbearVersion = (TextView) findViewById(R.id.dropbear_version);
		mDropbearVersion.setText(appVersion);

		// ViewPagerTabs
		mAdapter = new MainAdapter(this);
		mAdapter.initPages();
		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);
		mPagerTabs = (ViewPagerTabs) findViewById(R.id.tabs);
		mPagerTabs.setViewPager(mPager);
		mPager.setCurrentItem(DEFAULT_PAGE);
	}

	@Override
	public void onClick(View v) {
		if (v == mLogo) {
			mPager.setCurrentItem(DEFAULT_PAGE);
		}
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
		RootUtils.checkRootAccess();
		RootUtils.checkBusybox();
		RootUtils.checkDropbear();

		// Pages
		mAdapter.updatePages();
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
}