package me.shkschneider.dropbearserver.Pages;

import me.shkschneider.dropbearserver.MainActivity;
import me.shkschneider.dropbearserver.R;
import me.shkschneider.dropbearserver.SettingsHelper;
import me.shkschneider.dropbearserver.Tasks.DropbearRemover;
import me.shkschneider.dropbearserver.Tasks.DropbearRemoverCallback;
import me.shkschneider.dropbearserver.Utils.RootUtils;
import me.shkschneider.dropbearserver.Utils.Utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;

public class SettingsPage implements OnClickListener, OnCheckedChangeListener, DialogInterface.OnClickListener, DropbearRemoverCallback<Boolean> {

	private static final String TAG = "DropBearServer";

	private Context mContext;
	private LayoutInflater mLayoutInflater;
	private View mView;

	private LinearLayout mGeneral;
	private LinearLayout mGeneralContent;

	// TODO: assume root access
	private CheckBox mStartAtBoot;
	private CheckBox mOnlyIfRunningBefore;
	private CheckBox mKeepScreenOn;
	private CheckBox mOnlyOverWifi;
	private LinearLayout mCompleteRemoval;

	private LinearLayout mDropbear;
	private LinearLayout mDropbearContent;
	private LinearLayout mDropbearContentError;

	private LinearLayout mBanner;
	private TextView mBannerInfos;
	private AlertDialog mBannerAlertDialog;
	private View mBannerView;
	
	private CheckBox mDisallowRootLogins;
	private CheckBox mDisablePasswordLogins;
	private CheckBox mDisablePasswordLoginsForRoot;
	
	private LinearLayout mListeningPort;
	private TextView mListeningPortInfos;
	private AlertDialog mListeningPortAlertDialog;
	private View mListeningPortView;

	private LinearLayout mPublicKeys;
	private LinearLayout mPublicKeysContent;
	private LinearLayout mPublicKeysContentError;

	private LinearLayout mAccounts;
	private LinearLayout mAccountsContent;
	private LinearLayout mAccountsContentError;
	
	private AlertDialog.Builder mAlertDialogBuilder;

	public SettingsPage(Context context) {
		mContext = context;
		mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mView = mLayoutInflater.inflate(R.layout.settings, null);

		// mGeneral mGeneralContent
		mGeneral = (LinearLayout) mView.findViewById(R.id.general);
		mGeneral.setOnClickListener(this);
		mGeneralContent = (LinearLayout) mView.findViewById(R.id.general_content);

		mStartAtBoot = (CheckBox) mView.findViewById(R.id.start_at_boot);
		mStartAtBoot.setOnCheckedChangeListener(null);
		mOnlyIfRunningBefore = (CheckBox) mView.findViewById(R.id.only_if_running_before);
		mOnlyIfRunningBefore.setOnCheckedChangeListener(null);
		mKeepScreenOn = (CheckBox) mView.findViewById(R.id.keep_screen_on);
		mKeepScreenOn.setOnCheckedChangeListener(null);
		mOnlyOverWifi = (CheckBox) mView.findViewById(R.id.only_over_wifi);
		mOnlyOverWifi.setOnCheckedChangeListener(null);
		mCompleteRemoval = (LinearLayout) mView.findViewById(R.id.complete_removal);
		mCompleteRemoval.setOnClickListener(this);

		// mDropbear mDropbearContent
		mDropbear = (LinearLayout) mView.findViewById(R.id.dropbear);
		mDropbear.setOnClickListener(this);
		mDropbearContent = (LinearLayout) mView.findViewById(R.id.dropbear_content);
		mDropbearContentError = (LinearLayout) mView.findViewById(R.id.dropbear_content_error);

		mBanner = (LinearLayout) mView.findViewById(R.id.banner);
		mBanner.setOnClickListener(this);
		mBannerInfos = (TextView) mView.findViewById(R.id.banner_infos);
		mDisallowRootLogins = (CheckBox) mView.findViewById(R.id.disallow_root_logins);
		mDisallowRootLogins.setOnCheckedChangeListener(null);
		mDisablePasswordLogins = (CheckBox) mView.findViewById(R.id.disable_password_logins);
		mDisablePasswordLogins.setOnCheckedChangeListener(null);
		mDisablePasswordLoginsForRoot = (CheckBox) mView.findViewById(R.id.disable_password_logins_for_root);
		mDisablePasswordLoginsForRoot.setOnCheckedChangeListener(null);
		mListeningPort = (LinearLayout) mView.findViewById(R.id.listening_port);
		mListeningPort.setOnClickListener(this);
		mListeningPortInfos = (TextView) mView.findViewById(R.id.listening_port_infos);

		// mAccounts mAccountsContent
		mAccounts = (LinearLayout) mView.findViewById(R.id.accounts);
		mAccounts.setOnClickListener(this);
		mAccountsContent = (LinearLayout) mView.findViewById(R.id.accounts_content);
		mAccountsContentError = (LinearLayout) mView.findViewById(R.id.accounts_content_error);

		// mPublicKeys mPublicKeysContent
		mPublicKeys = (LinearLayout) mView.findViewById(R.id.public_keys);
		mPublicKeys.setOnClickListener(this);
		mPublicKeysContent = (LinearLayout) mView.findViewById(R.id.public_keys_content);
		mPublicKeysContentError = (LinearLayout) mView.findViewById(R.id.public_keys_content_error);

		// mAlertDialogBuilder mBannerAlertDialog mListeningPortAlertDialog
		mAlertDialogBuilder = new AlertDialog.Builder(mContext);
		mAlertDialogBuilder.setCancelable(false);
		mAlertDialogBuilder.setPositiveButton("Okay", this);
		mAlertDialogBuilder.setNegativeButton("Cancel", this);

		mBannerAlertDialog = mAlertDialogBuilder.create();
		mBannerAlertDialog.setTitle("Banner");
		mBannerView = mLayoutInflater.inflate(R.layout.settings_banner, null);
		mBannerAlertDialog.setView(mBannerView);

		mListeningPortAlertDialog = mAlertDialogBuilder.create();
		mListeningPortAlertDialog.setTitle("Listening port");
		mListeningPortView = mLayoutInflater.inflate(R.layout.settings_listening_port, null);
		mListeningPortAlertDialog.setView(mListeningPortView);
	}

	public void update() {
		if (RootUtils.hasRootAccess == true && RootUtils.hasBusybox == true && RootUtils.hasDropbear == true) {
			mDropbear.setClickable(true);
			mDropbearContentError.setVisibility(View.GONE);
			mAccounts.setClickable(true);
			mAccountsContent.setVisibility(View.VISIBLE);
			mAccountsContentError.setVisibility(View.GONE);
			mPublicKeys.setClickable(true);
			mPublicKeysContent.setVisibility(View.VISIBLE);
			mPublicKeysContentError.setVisibility(View.GONE);
		}
		else {
			mDropbear.setClickable(false);
			mDropbearContent.setVisibility(View.GONE);
			mDropbearContentError.setVisibility(View.VISIBLE);
			mAccounts.setClickable(false);
			mAccountsContent.setVisibility(View.GONE);
			mAccountsContentError.setVisibility(View.VISIBLE);
			mPublicKeys.setClickable(false);
			mPublicKeysContent.setVisibility(View.GONE);
			mPublicKeysContentError.setVisibility(View.VISIBLE);
		}

		// mGeneral
		mStartAtBoot.setChecked(SettingsHelper.getInstance(mContext).getStartAtBoot());
		mStartAtBoot.setOnCheckedChangeListener(this);
		mOnlyIfRunningBefore.setChecked(SettingsHelper.getInstance(mContext).getOnlyIfRunningBefore());
		mOnlyIfRunningBefore.setOnCheckedChangeListener(this);
		mKeepScreenOn.setChecked(SettingsHelper.getInstance(mContext).getKeepScreenOn());
		mKeepScreenOn.setOnCheckedChangeListener(this);
		mOnlyOverWifi.setChecked(SettingsHelper.getInstance(mContext).getOnlyOverWifi());
		mOnlyOverWifi.setOnCheckedChangeListener(this);

		// mDropbear
		mBannerInfos.setText(SettingsHelper.getInstance(mContext).getBanner());
		mDisallowRootLogins.setChecked(SettingsHelper.getInstance(mContext).getDisallowRootLogins());
		mDisallowRootLogins.setOnCheckedChangeListener(this);
		mDisablePasswordLogins.setChecked(SettingsHelper.getInstance(mContext).getDisablePasswordLogins());
		mDisablePasswordLogins.setOnCheckedChangeListener(this);
		mDisablePasswordLoginsForRoot.setChecked(SettingsHelper.getInstance(mContext).getDisablePasswordLoginsForRoot());
		mDisablePasswordLoginsForRoot.setOnCheckedChangeListener(this);
		mListeningPortInfos.setText("" + SettingsHelper.getInstance(mContext).getListeningPort());

		// mAccounts
		// TODO: setAccounts

		// mPublicKeys
		// TODO: setPublicKeys
	}
	
	public void hideAllBut(LinearLayout oneLinearLayout) {
		if (mGeneralContent != oneLinearLayout)
			mGeneralContent.setVisibility(View.GONE);
		if (mDropbearContent != oneLinearLayout)
			mDropbearContent.setVisibility(View.GONE);
		oneLinearLayout.setVisibility(oneLinearLayout.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
	}

	public View getView() {
		return mView;
	}

	public void onClick(View v) {
		// mGeneral
		if (v == mGeneral) {
			hideAllBut(mGeneralContent);
		}
		else if (v == mCompleteRemoval) {
			new AlertDialog.Builder(mContext)
			.setTitle("Confirm")
			.setMessage("This will remove dropbear and all its configuration (including public keys).")
			.setCancelable(true)
			.setPositiveButton("Okay", this)
			.setNegativeButton("Cancel", this)
			.show();
		}

		// mDropbear
		else if (v == mDropbear) {
			hideAllBut(mDropbearContent);
		}
		else if (v == mBanner) {
			EditText editText = (EditText) mBannerView.findViewById(R.id.settings_banner);
			editText.setText(SettingsHelper.getInstance(mContext).getBanner());
			mBannerAlertDialog.show();
		}
		else if (v == mListeningPort) {
			EditText editText = (EditText) mListeningPortView.findViewById(R.id.settings_listening_port);
			editText.setText("" + SettingsHelper.getInstance(mContext).getListeningPort());
			mListeningPortAlertDialog.show();
		}

		// mAccounts
		else if (v == mAccounts) {
			hideAllBut(mAccountsContent);
			// TODO: changeAccounts
		}

		// mPublicKeys
		else if (v == mPublicKeys) {
			hideAllBut(mPublicKeysContent);
			// TODO: changePublicKeys
		}
	}

	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// mGeneral
		if (buttonView == mStartAtBoot) {
			SettingsHelper.getInstance(mContext).setStartAtBoot(buttonView.isChecked());
		}
		else if (buttonView == mKeepScreenOn) {
			if (isChecked == true) {
				((MainActivity) mContext).getWindow().addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
			}
			else {
				((MainActivity) mContext).getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
			}
			SettingsHelper.getInstance(mContext).setKeepScreenOn(buttonView.isChecked());
		}
		else if (buttonView == mOnlyOverWifi) {
			SettingsHelper.getInstance(mContext).setOnlyOverWifi(buttonView.isChecked());
		}
		else if (buttonView == mDisallowRootLogins) {
			SettingsHelper.getInstance(mContext).setDisallowRootLogins(buttonView.isChecked());
		}
		else if (buttonView == mDisablePasswordLogins) {
			SettingsHelper.getInstance(mContext).setDisablePasswordLogins(buttonView.isChecked());
		}
		else if (buttonView == mDisablePasswordLoginsForRoot) {
			SettingsHelper.getInstance(mContext).setDisablePasswordLoginsForRoot(buttonView.isChecked());
		}
	}

	public void onClick(DialogInterface dialog, int button) {
		if (button == DialogInterface.BUTTON_POSITIVE) {
			if (dialog == mBannerAlertDialog) {
				EditText editText = (EditText) mBannerView.findViewById(R.id.settings_banner);
				String settings_banner = editText.getText().toString();
				if (settings_banner.length() == 0) {
					settings_banner = "" + SettingsHelper.BANNER_DEFAULT;
				}
				SettingsHelper.getInstance(mContext).setBanner(settings_banner);
				mBannerInfos.setText(settings_banner);
			}
			else if (dialog == mListeningPortAlertDialog) {
				EditText editText = (EditText) mListeningPortView.findViewById(R.id.settings_listening_port);
				String settings_listening_port = editText.getText().toString();
				if (settings_listening_port.length() == 0) {
					settings_listening_port = "" + SettingsHelper.LISTENING_PORT_DEFAULT;
				}
				if (Utils.isNumeric(settings_listening_port) == true) {
					Integer port = Integer.parseInt(settings_listening_port);
					if (port > 0) {
						SettingsHelper.getInstance(mContext).setListeningPort(port);
					}
					else {
						Log.d(TAG, "SettingsPage: onClick(): Wrong TCP port value [listeningPort=" + port + "]");
						Toast.makeText(mContext, "Wrong TCP port value", Toast.LENGTH_SHORT).show();
					}
				}
				else {
					Log.d(TAG, "SettingsPage: onClick(): Wrong type [listeningPort: " + settings_listening_port + "]");
				}
				mListeningPortInfos.setText(settings_listening_port);
			}
			else {
				// mDropbearRemover
				DropbearRemover dropbearRemover = new DropbearRemover(mContext, this);
				dropbearRemover.execute();
			}
		}
	}

	public void onDropbearRemoverComplete(Boolean result) {
		Log.i(TAG, "SettingsPage: onDropbearRemoverComplete(" + result + ")");
		if (result == true) {
			// do not check for dropbear
			RootUtils.hasDropbear = false;
			((MainActivity) mContext).update();
			Toast.makeText(mContext, "Dropbear successfully removed", Toast.LENGTH_SHORT).show();
			mGeneralContent.setVisibility(View.GONE);
			((MainActivity) mContext).goToDefaultPage();
		}
		else {
			Toast.makeText(mContext, "Dropbear could not be removed", Toast.LENGTH_SHORT).show();
		}
	}
}