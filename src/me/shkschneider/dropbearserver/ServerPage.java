package me.shkschneider.dropbearserver;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ServerPage extends Activity implements OnClickListener
{
	public static final String TAG = "ServerPage";

	public static final int STATUS_ERROR	= 0x00;
	public static final int STATUS_STOPPED	= 0x01;
	public static final int STATUS_STARTING	= 0x02;
	public static final int STATUS_STARTED	= 0x03;
	public static final int STATUS_STOPPING	= 0x04;
	
	private Context mContext;
	private View mView;
	private int mServerStatusCode;

	private TextView mSuperuserStatus;
	private LinearLayout mGetSuperuser;
	private TextView mDropbearStatus;
	private LinearLayout mGetDropbear;
	private TextView mServerStatus;
	private LinearLayout mServerLaunch;
	private TextView mServerLaunchLabel;
	private TextView mInfos;

	public void initView(Context context) {
		mContext = context;
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mView = inflater.inflate(R.layout.server, null);
		
		// mSuperuserStatus mGetSuperuser
		mSuperuserStatus = (TextView) mView.findViewById(R.id.superuser_status);
		mGetSuperuser = (LinearLayout) mView.findViewById(R.id.get_superuser);
		updateSuperuserStatus();

		// mDropbearStatus mGetDropbear
		mDropbearStatus = (TextView) mView.findViewById(R.id.dropbear_status);
		mGetDropbear = (LinearLayout) mView.findViewById(R.id.get_dropbear);
		updateDropbearStatus();
		
		// mLaunch mLaunchLabel mServerStatus mInfos
		mServerLaunch = (LinearLayout) mView.findViewById(R.id.server_launch);
		mServerLaunch.setOnClickListener(this);
		mServerLaunchLabel = (TextView) mView.findViewById(R.id.launch_label);
		mServerStatus = (TextView) mView.findViewById(R.id.server_status);
		mInfos = (TextView) mView.findViewById(R.id.infos);
		//updateServerStatus();
	}
	
	public void update() {
		// Superuser
		updateSuperuserStatus();
		
		// Dropbear
		updateDropbearStatus();
		
		// ServerStatus
		updateServerStatusCode();
		updateServerStatus();
	}
	
	public void updateSuperuserStatus() {
		if (DropBearServerHelper.hasRootAccess == true) {
			mSuperuserStatus.setText(R.string.ok);
			mSuperuserStatus.setTextColor(Color.GREEN);
			mGetSuperuser.setVisibility(View.GONE);
		}
		else {
			mSuperuserStatus.setText(R.string.ko);
			mSuperuserStatus.setTextColor(Color.RED);
			mGetSuperuser.setVisibility(View.VISIBLE);
			mGetSuperuser.setOnClickListener(this);
		}
	}
	
	public void updateDropbearStatus() {
		if (DropBearServerHelper.hasDropbear == true) {
			mDropbearStatus.setText(R.string.ok);
			mDropbearStatus.setTextColor(Color.GREEN);
			mGetDropbear.setVisibility(View.GONE);
		}
		else {
			mDropbearStatus.setText(R.string.ko);
			mDropbearStatus.setTextColor(Color.RED);
			mGetDropbear.setVisibility(View.VISIBLE);
			mGetDropbear.setOnClickListener(this);
			mServerStatusCode = STATUS_ERROR;
		}
	}
	
	public void updateServerStatus() {
		switch (mServerStatusCode) {
		case STATUS_STOPPED:
			mServerStatus.setText("STOPPED");
			mServerStatus.setTextColor(Color.RED);
			//mServerLaunchLabel.setEnabled(true);
			mServerLaunchLabel.setText("START SERVER");
			break;
		case STATUS_STARTING:
			mServerStatus.setText("STARTING");
			mServerStatus.setTextColor(Color.YELLOW);
			//mServerLaunch.setEnabled(false);
			mServerLaunchLabel.setText("STARTING...");
			break;
		case STATUS_STARTED:
			mServerStatus.setText("STARTED");
			mServerStatus.setTextColor(Color.GREEN);
			//mServerLaunchLabel.setEnabled(true);
			mServerLaunchLabel.setText("STOP SERVER");
			break;
		case STATUS_STOPPING:
			mServerStatus.setText("STOPPING");
			mServerStatus.setTextColor(Color.YELLOW);
			//mServerLaunch.setEnabled(false);
			mServerLaunchLabel.setText("STOPPING...");
			break;
		case STATUS_ERROR:
			mServerStatus.setText("ERROR");
			mServerStatus.setTextColor(Color.RED);
			mServerLaunch.setEnabled(false);
			mServerLaunchLabel.setText("ERROR");
			break;
		default:
			break;
		}
	}
	
	public void updateServerStatusCode() {
		if (DropBearServerHelper.hasRootAccess == false) {
			mServerStatusCode = STATUS_ERROR;
			mInfos.setText("Superuser is missing");
		}
		else if (DropBearServerHelper.hasDropbear == false) {
			mServerStatusCode = STATUS_ERROR;
			mInfos.setText("Dropbear is missing");
		}
		else {
			int pid = DropBearServerHelper.getServerPid();
			if (pid < 0) {
				mServerStatusCode = STATUS_ERROR;
			}
			else if (pid == 0) {
				mServerStatusCode = STATUS_STOPPED;
			}
			else {
				mServerStatusCode = STATUS_STARTED;
			}
		}
	}
	
	public View getView() {
		return mView;
	}

	@Override
	public void onClick(View v) {
		if (v == mServerLaunch) {
			switch (mServerStatusCode) {
			case STATUS_STOPPED:
				mServerStatusCode = STATUS_STARTING;
				break;
			case STATUS_STARTING:
				mServerStatusCode = STATUS_STARTED;
				break;
			case STATUS_STARTED:
				mServerStatusCode = STATUS_STOPPING;
				break;
			case STATUS_STOPPING:
				mServerStatusCode = STATUS_STOPPED;
				break;
			case STATUS_ERROR:
				mServerStatusCode = STATUS_ERROR;
				break;
			default:
				break;
			}
			updateServerStatus();
		}
		else if (v == mGetSuperuser) {
			try {
				mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.noshufou.android.su")));
			}
			catch (ActivityNotFoundException e) {
				DropBearServerHelper.marketNotFound(mContext);
				mGetSuperuser.setEnabled(false);
			}
		}
		else if (v == mGetDropbear) {
			// ...
		}
	}
}