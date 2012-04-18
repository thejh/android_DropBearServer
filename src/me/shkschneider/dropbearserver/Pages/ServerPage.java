package me.shkschneider.dropbearserver.Pages;

import me.shkschneider.dropbearserver.R;
import me.shkschneider.dropbearserver.Tasks.DropbearInstaller;
import me.shkschneider.dropbearserver.Tasks.DropbearInstallerCallback;
import me.shkschneider.dropbearserver.Tasks.ServerStarter;
import me.shkschneider.dropbearserver.Tasks.ServerStarterCallback;
import me.shkschneider.dropbearserver.Tasks.ServerStopper;
import me.shkschneider.dropbearserver.Tasks.ServerStopperCallback;
import me.shkschneider.dropbearserver.Utils.RootUtils;
import me.shkschneider.dropbearserver.Utils.ServerUtils;
import me.shkschneider.dropbearserver.Utils.Utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ServerPage implements OnClickListener, DropbearInstallerCallback<Boolean>, ServerStarterCallback<Boolean>, ServerStopperCallback<Boolean>
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

	private TextView mRootStatus;
	private LinearLayout mGetSuperuser;
	private LinearLayout mGetBusybox;
	private TextView mDropbearStatus;
	private LinearLayout mGetDropbear;
	private TextView mServerStatus;
	private LinearLayout mServerLaunch;
	private TextView mServerLaunchLabel;

	public ServerPage(Context context) {
		mContext = context;
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mView = inflater.inflate(R.layout.server, null);
		mServerStatusCode = STATUS_ERROR;

		// mSuperuserStatus mGetSuperuser
		mRootStatus = (TextView) mView.findViewById(R.id.superuser_status);
		mGetSuperuser = (LinearLayout) mView.findViewById(R.id.get_superuser);
		mGetSuperuser.setOnClickListener(this);
		mGetBusybox = (LinearLayout) mView.findViewById(R.id.get_busybox);
		mGetBusybox.setOnClickListener(this);

		// mDropbearStatus mGetDropbear
		mDropbearStatus = (TextView) mView.findViewById(R.id.dropbear_status);
		mGetDropbear = (LinearLayout) mView.findViewById(R.id.get_dropbear);
		mGetDropbear.setOnClickListener(this);

		// mServerStatus mServerLaunch mServerLaunchLabel
		mServerStatus = (TextView) mView.findViewById(R.id.server_status);
		mServerLaunch = (LinearLayout) mView.findViewById(R.id.server_launch);
		mServerLaunch.setOnClickListener(this);
		mServerLaunchLabel = (TextView) mView.findViewById(R.id.launch_label);
	}

	public void update() {
		updateRootStatus();
		updateDropbearStatus();
		updateServerStatusCode();
		updateServerStatus();
	}

	public void updateRootStatus() {
		if (RootUtils.hasRootAccess == true) {
			if (RootUtils.hasBusybox == true) {
				mRootStatus.setText(R.string.ok);
				mRootStatus.setTextColor(Color.GREEN);
				mGetBusybox.setVisibility(View.GONE);
			}
			else {
				mRootStatus.setText(R.string.ko);
				mRootStatus.setTextColor(Color.RED);
				mGetBusybox.setVisibility(View.VISIBLE);
			}
			mGetSuperuser.setVisibility(View.GONE);
		}
		else {
			mRootStatus.setText(R.string.ko);
			mRootStatus.setTextColor(Color.RED);
			mGetSuperuser.setVisibility(View.VISIBLE);
		}
	}

	public void updateDropbearStatus() {
		if (RootUtils.hasRootAccess == true && RootUtils.hasDropbear == true) {
			mDropbearStatus.setText(R.string.ok);
			mDropbearStatus.setTextColor(Color.GREEN);
			mGetDropbear.setVisibility(View.GONE);
		}
		else {
			mDropbearStatus.setText(R.string.ko);
			mDropbearStatus.setTextColor(Color.RED);
			mGetDropbear.setVisibility(View.VISIBLE);
			mServerStatusCode = STATUS_ERROR;
		}
	}

	public void updateServerStatusCode() {
		if (RootUtils.hasRootAccess == false) {
			mServerStatusCode = STATUS_ERROR;
		}
		else if (RootUtils.hasDropbear == false) {
			mServerStatusCode = STATUS_ERROR;
		}
		else {
			int pid = ServerUtils.getServerPid();
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

	public void updateServerStatus() {
		switch (mServerStatusCode) {
		case STATUS_STOPPED:
			mServerStatus.setText("STOPPED");
			mServerStatus.setTextColor(Color.RED);
			mServerLaunch.setVisibility(View.VISIBLE);
			mServerLaunchLabel.setText("START SERVER");
			break;
		case STATUS_STARTING:
			mServerStatus.setText("STARTING");
			mServerStatus.setTextColor(Color.YELLOW);
			mServerLaunch.setVisibility(View.VISIBLE);
			mServerLaunchLabel.setText("STARTING...");
			break;
		case STATUS_STARTED:
			mServerStatus.setText("STARTED");
			mServerStatus.setTextColor(Color.GREEN);
			mServerLaunch.setVisibility(View.VISIBLE);
			mServerLaunchLabel.setText("STOP SERVER");
			break;
		case STATUS_STOPPING:
			mServerStatus.setText("STOPPING");
			mServerStatus.setTextColor(Color.YELLOW);
			mServerLaunch.setVisibility(View.VISIBLE);
			mServerLaunchLabel.setText("STOPPING...");
			break;
		case STATUS_ERROR:
			mServerStatus.setText("ERROR");
			mServerStatus.setTextColor(Color.RED);
			mServerLaunch.setVisibility(View.GONE);
			mServerLaunchLabel.setText("ERROR");
			break;
		default:
			break;
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
				updateServerStatus();
				// StartServer
				ServerStarter serverStarter = new ServerStarter(mContext, this);
				serverStarter.execute();
				break;
			case STATUS_STARTING:
				mServerStatusCode = STATUS_STARTED;
				break;
			case STATUS_STARTED:
				mServerStatusCode = STATUS_STOPPING;
				updateServerStatus();
				// StopServer
				ServerStopper serverStopper = new ServerStopper(mContext, this);
				serverStopper.execute();
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
		}
		else if (v == mGetSuperuser) {
			try {
				mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.noshufou.android.su")));
			}
			catch (ActivityNotFoundException e) {
				Utils.marketNotFound(mContext);
			}
			RootUtils.checkRootAccess();
			RootUtils.checkBusybox();
			updateRootStatus();
		}
		else if (v == mGetDropbear) {
			// DropbearInstaller
			DropbearInstaller dropbearInstaller = new DropbearInstaller(mContext, this);
			dropbearInstaller.execute();
		}
	}

	@Override
	public void onDropbearInstallerComplete(Boolean result) {
		Log.i(TAG, "onDropbearInstallerComplete(" + result + ")");
		if (result == true) {
			RootUtils.checkDropbear();
			updateDropbearStatus();
			updateServerStatusCode();
			updateServerStatus();
		}
	}

	@Override
	public void onServerStarterComplete(Boolean result) {
		Log.i(TAG, "onStartServerComplete(" + result + ")");
		if (result == true) {
			mServerStatusCode = STATUS_STARTED;
		}
		else {
			mServerStatusCode = STATUS_ERROR;
		}
		updateServerStatus();
	}

	@Override
	public void onServerStopperComplete(Boolean result) {
		Log.i(TAG, "onStopServerComplete(" + result + ")");
		if (result == true) {
			mServerStatusCode = STATUS_STOPPED;
		}
		else {
			mServerStatusCode = STATUS_ERROR;
		}
		updateServerStatus();
	}
}