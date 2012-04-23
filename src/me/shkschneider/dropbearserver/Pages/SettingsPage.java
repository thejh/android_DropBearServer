package me.shkschneider.dropbearserver.Pages;

import me.shkschneider.dropbearserver.MainActivity;
import me.shkschneider.dropbearserver.R;
import me.shkschneider.dropbearserver.SettingsHelper;
import me.shkschneider.dropbearserver.Tasks.DropbearRemover;
import me.shkschneider.dropbearserver.Tasks.DropbearRemoverCallback;
import me.shkschneider.dropbearserver.Utils.RootUtils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;

public class SettingsPage implements OnClickListener, OnCheckedChangeListener, DialogInterface.OnClickListener, DropbearRemoverCallback<Boolean> {

	private static final String TAG = "SettingsPage";
	
	private Context mContext;
	private View mView;
	private SettingsHelper mSettingsHelper;

	private LinearLayout mGeneral;
	private LinearLayout mGeneralContent;

	private CheckBox mStartAtBoot;
	private CheckBox mKeepScreenOn;
	private CheckBox mOnlyOverWifi;
	private LinearLayout mCompleteRemoval;

	private LinearLayout mDropbear;
	private LinearLayout mDropbearContent;
	private LinearLayout mDropbearContentError;
	
	private CheckBox mDisallowRootLogins;
	private CheckBox mDisablePasswordLogins;
	private CheckBox mDisablePasswordLoginsForRoot;
	private LinearLayout mListeningPort;

	private LinearLayout mPublicKeys;
	private LinearLayout mPublicKeysContent;
	private LinearLayout mPublicKeysContentError;

	private LinearLayout mAccounts;
	private LinearLayout mAccountsContent;
	private LinearLayout mAccountsContentError;

	public SettingsPage(Context context) {
		mContext = context;
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mView = inflater.inflate(R.layout.settings, null);
		mSettingsHelper = new SettingsHelper(mContext);

		// mGeneral mGeneralContent
		mGeneral = (LinearLayout) mView.findViewById(R.id.general);
		mGeneral.setOnClickListener(this);
		mGeneralContent = (LinearLayout) mView.findViewById(R.id.general_content);

		mStartAtBoot = (CheckBox) mView.findViewById(R.id.start_at_boot);
		mStartAtBoot.setOnCheckedChangeListener(this);
		mKeepScreenOn = (CheckBox) mView.findViewById(R.id.keep_screen_on);
		mKeepScreenOn.setOnCheckedChangeListener(this);
		mOnlyOverWifi = (CheckBox) mView.findViewById(R.id.only_over_wifi);
		mOnlyOverWifi.setOnCheckedChangeListener(this);
		mCompleteRemoval = (LinearLayout) mView.findViewById(R.id.complete_removal);
		mCompleteRemoval.setOnClickListener(this);

		// mDropbear mDropbearContent
		mDropbear = (LinearLayout) mView.findViewById(R.id.dropbear);
		mDropbear.setOnClickListener(this);
		mDropbearContent = (LinearLayout) mView.findViewById(R.id.dropbear_content);
		mDropbearContentError = (LinearLayout) mView.findViewById(R.id.dropbear_content_error);

		mDisallowRootLogins = (CheckBox) mView.findViewById(R.id.disallow_root_logins);
		mDisallowRootLogins.setOnCheckedChangeListener(this);
		mDisablePasswordLogins = (CheckBox) mView.findViewById(R.id.disable_password_logins);
		mDisablePasswordLogins.setOnCheckedChangeListener(this);
		mDisablePasswordLoginsForRoot = (CheckBox) mView.findViewById(R.id.disable_password_logins_for_root);
		mDisablePasswordLoginsForRoot.setOnCheckedChangeListener(this);
		mListeningPort = (LinearLayout) mView.findViewById(R.id.listening_port);
		mListeningPort.setOnClickListener(this);
		
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
		mStartAtBoot.setChecked(mSettingsHelper.getStartAtBoot());
		mKeepScreenOn.setChecked(mSettingsHelper.getKeepScreenOn());
		mOnlyOverWifi.setChecked(mSettingsHelper.getOnlyOverWifi());
		
		// mDropbear
		
		// mPublicKeys
	}

	public View getView() {
		return mView;
	}

	public void onClick(View v) {
		// mGeneral
		if (v == mGeneral) {
			mGeneralContent.setVisibility(mGeneralContent.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
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
			mDropbearContent.setVisibility(mDropbearContent.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
		}
		else if (v == mListeningPort) {
			// TODO: alert with number
		}
		// mAccounts
		else if (v == mAccounts) {
			mAccountsContent.setVisibility(mAccountsContent.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
		}
		// mPublicKeys
		else if (v == mPublicKeys) {
			mPublicKeysContent.setVisibility(mPublicKeysContent.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
		}
	}

	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// mGeneral
		if (buttonView == mStartAtBoot) {
			mSettingsHelper.setStartAtBoot(buttonView.isChecked());
		}
		else if (buttonView == mKeepScreenOn) {
			mSettingsHelper.setKeepScreenOn(buttonView.isChecked());
		}
		else if (buttonView == mOnlyOverWifi) {
			mSettingsHelper.setOnlyOverWifi(buttonView.isChecked());
		}
		else if (buttonView == mDisallowRootLogins) {
			mSettingsHelper.setDisallowRootLogins(buttonView.isChecked());
		}
		else if (buttonView == mDisablePasswordLogins) {
			mSettingsHelper.setDisablePasswordLogins(buttonView.isChecked());
		}
		else if (buttonView == mDisablePasswordLoginsForRoot) {
			mSettingsHelper.setDisablePasswordLoginsForRoot(buttonView.isChecked());
		}
	}

	public void onClick(DialogInterface dialog, int button) {
		if (button == DialogInterface.BUTTON_POSITIVE) {
			// mDropbearRemover
			DropbearRemover dropbearRemover = new DropbearRemover(mContext, this);
			dropbearRemover.execute();
		}
	}

	public void onDropbearRemoverComplete(Boolean result) {
		Log.i(TAG, "onDropbearRemoverComplete(" + result + ")");
		if (result == true) {
			// do not check for dropbear
			RootUtils.hasDropbear = false;
			((MainActivity) mContext).update();
			((MainActivity) mContext).goToDefaultPage();
		}
		else {
			// TODO: Toast
		}
	}
}