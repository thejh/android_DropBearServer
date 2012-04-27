package me.shkschneider.dropbearserver.Pages;

import me.shkschneider.dropbearserver.Utils.ServerUtils;
import me.shkschneider.dropbearserver.Utils.Utils;
import me.shkschneider.dropbearserver.MainActivity;
import me.shkschneider.dropbearserver.R;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AboutPage implements OnClickListener {
	
	private Context mContext;
	private View mView;
	
	private TextView mAppVersion;
	private LinearLayout mRateThisApp;
	private LinearLayout mDonate;
	private LinearLayout mVisitMyWebsite;
	
	public AboutPage(Context context) {
		mContext = context;
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mView = inflater.inflate(R.layout.about, null);

		// mAppVersion
		mAppVersion = (TextView) mView.findViewById(R.id.about_app_version);
		mAppVersion.setText("Version " + MainActivity.getAppVersion());
		
		// mRateThisApp
		mRateThisApp = (LinearLayout) mView.findViewById(R.id.go_rate);
		mRateThisApp.setOnClickListener(this);
		
		// mDonate
		mDonate = (LinearLayout) mView.findViewById(R.id.go_donate);
		mDonate.setOnClickListener(this);
		
		// mVisiteMyWebsite
		mVisitMyWebsite = (LinearLayout) mView.findViewById(R.id.go_website);
		mVisitMyWebsite.setOnClickListener(this);
	}
	
	public View getView() {
		String version = ServerUtils.getDropbearVersion();
		if (version != null) {
			mAppVersion.setText("Version " + MainActivity.getAppVersion() + " / " + ServerUtils.getDropbearVersion());
		}
		return mView;
	}

	public void onClick(View view) {
		if (view == mRateThisApp) {
			try {
				mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + mContext.getApplicationInfo().packageName)));
			}
			catch (ActivityNotFoundException e) {
				Utils.marketNotFound(mContext);
				mRateThisApp.setEnabled(false);
			}
		}
		else if (view == mDonate) {
			mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=FP4JTHPKJPKS6&lc=GB&item_name=shkschneider&currency_code=USD&bn=PP%2dDonationsBF%3abtn_donate_SM%2egif%3aNonHosted")));
		}
		else if (view == mVisitMyWebsite) {
			mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mContext.getResources().getString(R.string.app_website))));
		}
	}
}