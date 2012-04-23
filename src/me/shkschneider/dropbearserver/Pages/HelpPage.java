package me.shkschneider.dropbearserver.Pages;

import me.shkschneider.dropbearserver.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public class HelpPage implements OnClickListener {

	private Context mContext;
	private View mView;

	private LinearLayout mWhatIsRoot;
	private LinearLayout mWhatIsRootContent;
	private LinearLayout mWhatIsDropbear;
	private LinearLayout mWhatIsDropbearContent;

	public HelpPage(Context context) {
		mContext = context;
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mView = inflater.inflate(R.layout.help, null);

		// mWhatIsRoot mWhatIsRootContent
		mWhatIsRoot = (LinearLayout) mView.findViewById(R.id.what_is_root);
		mWhatIsRoot.setOnClickListener(this);
		mWhatIsRootContent = (LinearLayout) mView.findViewById(R.id.what_is_root_content);

		// mWhatIsDropbear mWhatIsDropbearContent
		mWhatIsDropbear = (LinearLayout) mView.findViewById(R.id.what_is_dropbear);
		mWhatIsDropbear.setOnClickListener(this);
		mWhatIsDropbearContent = (LinearLayout) mView.findViewById(R.id.what_is_dropbear_content);
	}

	public View getView() {
		return mView;
	}

	public void onClick(View v) {
		if (v == mWhatIsRoot) {
			mWhatIsRootContent.setVisibility(mWhatIsRootContent.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
		}
		else if (v == mWhatIsDropbear) {
			mWhatIsDropbearContent.setVisibility(mWhatIsDropbearContent.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
		}
	}
}