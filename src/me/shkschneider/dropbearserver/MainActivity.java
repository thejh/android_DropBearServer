package me.shkschneider.dropbearserver;

import com.astuetz.viewpagertabs.ViewPagerTabs;
import me.shkschneider.dropbearserver.R;
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
		
		// Header
		mLogo = (ImageView) findViewById(R.id.logo);
		mLogo.setOnClickListener(this);
		mDropbearVersion = (TextView) findViewById(R.id.dropbear_version);
		try {
			PackageInfo pInfo = getApplicationContext().getPackageManager().getPackageInfo(getResources().getString(R.string.app_package), PackageManager.GET_META_DATA);
			mDropbearVersion.setText("" + pInfo.versionName);
		} catch (NameNotFoundException e) {
			mDropbearVersion.setText("" + 0);
		}

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
		
		// Root dependencies
		DropBearServerHelper.checkRootAccess();
		DropBearServerHelper.checkDropbear();
		
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