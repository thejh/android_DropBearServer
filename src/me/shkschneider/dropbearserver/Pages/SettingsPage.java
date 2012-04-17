package me.shkschneider.dropbearserver.Pages;

import me.shkschneider.dropbearserver.R;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public class SettingsPage extends Activity implements OnClickListener {

	private Context mContext;
	private View mView;

	private LinearLayout mGeneral;
	private LinearLayout mGeneralContent;
	private LinearLayout mDropbear;
	private LinearLayout mDropbearContent;

	public void initView(Context context) {
		mContext = context;
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mView = inflater.inflate(R.layout.settings, null);

		// mGeneral mGeneralContent
		mGeneral = (LinearLayout) mView.findViewById(R.id.general);
		mGeneral.setOnClickListener(this);
		mGeneralContent = (LinearLayout) mView.findViewById(R.id.general_content);

		// mDropbear mDropbearContent
		mDropbear = (LinearLayout) mView.findViewById(R.id.dropbear);
		mDropbear.setOnClickListener(this);
		mDropbearContent = (LinearLayout) mView.findViewById(R.id.dropbear_content);
	}
	
	public void update() {
		// ...
	}

	public View getView() {
		return mView;
	}

	@Override
	public void onClick(View v) {
		if (v == mGeneral) {
			mGeneralContent.setVisibility(mGeneralContent.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
		}
		else if (v == mDropbear) {
			mDropbearContent.setVisibility(mDropbearContent.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
		}
	}
}