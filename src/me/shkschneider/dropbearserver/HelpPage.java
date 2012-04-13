package me.shkschneider.dropbearserver;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public class HelpPage implements OnClickListener {

	private Context mContext;
	private View mView;

	private LinearLayout mWhatIsRoot;
	private LinearLayout mWhatisRootContent;
	private LinearLayout mWhatIsDropbear;
	private LinearLayout mWhatisDropbearContent;

	public void initView(Context context) {
		mContext = context;
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mView = inflater.inflate(R.layout.help, null);

		// mWhatIsRoot mWhatisRootContent
		mWhatIsRoot = (LinearLayout) mView.findViewById(R.id.what_is_root);
		mWhatIsRoot.setOnClickListener(this);
		mWhatisRootContent = (LinearLayout) mView.findViewById(R.id.what_is_root_content);

		// mWhatIsDropbear mWhatisDropbearContent
		mWhatIsDropbear = (LinearLayout) mView.findViewById(R.id.what_is_dropbear);
		mWhatIsDropbear.setOnClickListener(this);
		mWhatisDropbearContent = (LinearLayout) mView.findViewById(R.id.what_is_dropbear_content);
	}

	public View getView() {
		return mView;
	}

	@Override
	public void onClick(View v) {
		if (v == mWhatIsRoot) {
			mWhatisRootContent.setVisibility(mWhatisRootContent.getVisibility() == LinearLayout.VISIBLE ? LinearLayout.GONE : LinearLayout.VISIBLE);
		}
		else if (v == mWhatIsDropbear) {
			mWhatisDropbearContent.setVisibility(mWhatisDropbearContent.getVisibility() == LinearLayout.VISIBLE ? LinearLayout.GONE : LinearLayout.VISIBLE);
		}
	}
}