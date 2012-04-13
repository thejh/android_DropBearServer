package me.shkschneider.dropbearserver;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class ServerPage {

	private Context mContext;
	private View mView;

	private TextView mRootStatus;
	private TextView mServerStatus;
	private TextView mInfos;

	public void initView(Context context) {
		mContext = context;
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mView = inflater.inflate(R.layout.server, null);

		mRootStatus = (TextView) mView.findViewById(R.id.root_status);
		mServerStatus = (TextView) mView.findViewById(R.id.server_status);
		mInfos = (TextView) mView.findViewById(R.id.infos);
	}

	public View getView() {
		// mRootStatus
		if (RootAccess.rootAvailable == true) {
			mRootStatus.setText(R.string.ok);
			mRootStatus.setTextColor(Color.GREEN);
		}
		else {
			mRootStatus.setText(R.string.ko);
			mRootStatus.setTextColor(Color.RED);
		}

		// mServerStatus
		mServerStatus.setText(R.string.stopped);
		mServerStatus.setTextColor(Color.RED);

		// mInfos
		mInfos.setText("-");

		return mView;
	}
}