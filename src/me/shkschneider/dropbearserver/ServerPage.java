package me.shkschneider.dropbearserver;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ServerPage implements OnClickListener {

	private Context mContext;
	private View mView;
	private Boolean mServerStarted;

	private TextView mRootStatus;
	private TextView mDropbearStatus;
	private TextView mServerStatus;
	private TextView mInfos;
	private LinearLayout mLaunch;
	private TextView mLaunchLabel;

	public void initView(Context context) {
		mContext = context;
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mView = inflater.inflate(R.layout.server, null);
		mServerStarted = false;
		
		// mRootStatus
		mRootStatus = (TextView) mView.findViewById(R.id.root_status);
		if (RootAccess.suAvailable == true) {
			mRootStatus.setText(R.string.ok);
			mRootStatus.setTextColor(Color.GREEN);
		}
		else {
			mRootStatus.setText(R.string.ko);
			mRootStatus.setTextColor(Color.RED);
		}

		// mDropbearStatus
		mDropbearStatus = (TextView) mView.findViewById(R.id.dropbear_status);
		mDropbearStatus.setText(R.string.ko);
		mDropbearStatus.setTextColor(Color.RED);

		// mServerStatus
		mServerStatus = (TextView) mView.findViewById(R.id.server_status);
		mServerStatus.setText(R.string.stopped);
		mServerStatus.setTextColor(Color.YELLOW);

		// mInfos
		mInfos = (TextView) mView.findViewById(R.id.infos);
		mInfos.setText("-");

		// mLaunch mLaunchLabel
		mLaunch = (LinearLayout) mView.findViewById(R.id.launch);
		mLaunch.setOnClickListener(this);
		mLaunchLabel = (TextView) mView.findViewById(R.id.launch_label);
	}

	public View getView() {
		return mView;
	}

	@Override
	public void onClick(View v) {
		if (v == mLaunch) {
			if (mServerStarted == false) {
				mServerStatus.setText(R.string.started);
				mServerStatus.setTextColor(Color.GREEN);
				mLaunchLabel.setText(R.string.stop);
				mServerStarted = true;
			}
			else {
				mServerStatus.setText(R.string.stopped);
				mServerStatus.setTextColor(Color.YELLOW);
				mLaunchLabel.setText(R.string.start);
				mServerStarted = false;
			}
		}
	}
}