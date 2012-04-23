/*
 * Andreas Stutz <https://github.com/astuetz/android-viewpagertabs-example>
 */
package me.shkschneider.dropbearserver;

import com.astuetz.viewpagertabs.ViewPagerTabProvider;

import me.shkschneider.dropbearserver.Pages.AboutPage;
import me.shkschneider.dropbearserver.Pages.HelpPage;
import me.shkschneider.dropbearserver.Pages.ServerPage;
import me.shkschneider.dropbearserver.Pages.SettingsPage;
import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

public class MainAdapter extends PagerAdapter implements ViewPagerTabProvider {

	private static final String TAG = "MainAdapter";
	
	public static final int DEFAULT_PAGE = 1;
	
	private static final int SETTINGS_INDEX = 0;
	private static final int SERVER_INDEX = 1;
	private static final int HELP_INDEX = 2;
	private static final int ABOUT_INDEX = 3;

	private Context mContext;
	private SettingsPage mSettingsPage;
	private ServerPage mServerPage;
	private HelpPage mHelpPage;
	private AboutPage mAboutPage;

	private String[] mTitles = {
			"SETTINGS",
			"SERVER",
			"HELP",
			"ABOUT"
	};

	public MainAdapter(Context context) {
		Log.d(TAG, "MainAdapter()");
		
		mContext = context;
		mSettingsPage = new SettingsPage(mContext);
		mServerPage = new ServerPage(mContext);
		mHelpPage = new HelpPage(mContext);
		mAboutPage = new AboutPage(mContext);
	}

	public void update() {
		mSettingsPage.update();
		mServerPage.update();
	}

	@Override
	public Object instantiateItem(View container, int position) {
		View v = null;
		switch (position) {
		case SETTINGS_INDEX:
			//mSettingsPage.update();
			v = mSettingsPage.getView();
			break;
		case SERVER_INDEX:
			//mServerPage.update();
			v = mServerPage.getView();
			break;
		case HELP_INDEX:
			//mHelpPage.update();
			v = mHelpPage.getView();
			break;
		case ABOUT_INDEX:
			//mAboutPage.update();
			v = mAboutPage.getView();
			break;
		}
		((ViewPager) container).addView(v, 0);
		return v;
	}

	@Override
	public int getCount() {
		return mTitles.length;
	}

	@Override
	public void destroyItem(View container, int position, Object view) {
		((ViewPager) container).removeView((View) view);
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == ((View) object);
	}

	@Override
	public void finishUpdate(View container) {
	}

	@Override
	public void restoreState(Parcelable state, ClassLoader loader) {
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void startUpdate(View container) {
	}

	public String getTitle(int position) {
		final int len = mTitles.length;
		return (position >= 0 && position < len ? mTitles[position] : "");
	}
}