package me.shkschneider.dropbearserver.Pages;

import java.util.List;

import me.shkschneider.dropbearserver.MainActivity;
import me.shkschneider.dropbearserver.R;
import me.shkschneider.dropbearserver.SettingsHelper;
import me.shkschneider.dropbearserver.Explorer.ExplorerActivity;
import me.shkschneider.dropbearserver.Tasks.DropbearRemover;
import me.shkschneider.dropbearserver.Tasks.DropbearRemoverCallback;
import me.shkschneider.dropbearserver.Utils.RootUtils;
import me.shkschneider.dropbearserver.Utils.ServerUtils;
import me.shkschneider.dropbearserver.Utils.Utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.ToggleButton;

public class SettingsPage implements OnClickListener, OnCheckedChangeListener, DialogInterface.OnClickListener, DropbearRemoverCallback<Boolean> {

	private static final String TAG = "DropBearServer";

	public static Boolean goToHome = false;

	private Context mContext;
	private LayoutInflater mLayoutInflater;
	private View mView;

	private LinearLayout mGeneral;
	private LinearLayout mGeneralContent;

	private CheckBox mAssumeRootAccess;
	private CheckBox mNotification;
	private CheckBox mKeepScreenOn;
	private CheckBox mOnlyOverWifi;
	private LinearLayout mCompleteRemoval;
	private AlertDialog mCompleteRemovalAlertDialog;

	private LinearLayout mDropbear;
	private LinearLayout mDropbearContent;
	private LinearLayout mDropbearContentError;

	private LinearLayout mListeningPort;
	private TextView mListeningPortInfos;
	private AlertDialog mListeningPortAlertDialog;
	private View mListeningPortView;
	private CheckBox mDisallowRootLogins;
	private CheckBox mDisablePasswordLogins;
	private CheckBox mDisablePasswordLoginsForRoot;

	private LinearLayout mCredentials;
	private LinearLayout mCredentialsContent;
	private LinearLayout mCredentialsContentError;

	private TextView mCredentialsInfos;
	private AlertDialog mCredentialsAlertDialog;
	private View mCredentialsView;

	private LinearLayout mPublicKeys;
	private LinearLayout mPublicKeysContent;
	private LinearLayout mPublicKeysContentError;

	private AlertDialog mPublicKeysAlertDialog;
	private List<String> mPublicKeysList;
	private String mPublicKey;

	private AlertDialog.Builder mAlertDialogBuilder;

	public SettingsPage(Context context) {
		mContext = context;
		mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mView = mLayoutInflater.inflate(R.layout.settings, null);

		// mGeneral mGeneralContent
		mGeneral = (LinearLayout) mView.findViewById(R.id.general);
		mGeneral.setOnClickListener(this);
		mGeneralContent = (LinearLayout) mView.findViewById(R.id.general_content);

		mAssumeRootAccess = (CheckBox) mView.findViewById(R.id.assume_root_access);
		mAssumeRootAccess.setOnCheckedChangeListener(null);
		mNotification = (CheckBox) mView.findViewById(R.id.notification);
		mNotification.setOnCheckedChangeListener(null);
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

		mListeningPort = (LinearLayout) mView.findViewById(R.id.listening_port);
		mListeningPort.setOnClickListener(this);
		mListeningPortInfos = (TextView) mView.findViewById(R.id.listening_port_infos);
		mDisallowRootLogins = (CheckBox) mView.findViewById(R.id.disallow_root_logins);
		mDisallowRootLogins.setOnCheckedChangeListener(null);
		mDisablePasswordLogins = (CheckBox) mView.findViewById(R.id.disable_password_logins);
		mDisablePasswordLogins.setOnCheckedChangeListener(null);
		mDisablePasswordLoginsForRoot = (CheckBox) mView.findViewById(R.id.disable_password_logins_for_root);
		mDisablePasswordLoginsForRoot.setOnCheckedChangeListener(null);

		// mCredentials mCredentialsContent
		mCredentials = (LinearLayout) mView.findViewById(R.id.credentials);
		mCredentials.setOnClickListener(this);
		mCredentialsContent = (LinearLayout) mView.findViewById(R.id.credentials_content);
		mCredentialsContent.setOnClickListener(this);
		mCredentialsContentError = (LinearLayout) mView.findViewById(R.id.credentials_content_error);

		mCredentialsInfos = (TextView) mView.findViewById(R.id.credentials_infos);

		// mPublicKeys mPublicKeysContent
		mPublicKeys = (LinearLayout) mView.findViewById(R.id.public_keys);
		mPublicKeys.setOnClickListener(this);
		mPublicKeysContent = (LinearLayout) mView.findViewById(R.id.public_keys_content);
		mPublicKeysContentError = (LinearLayout) mView.findViewById(R.id.public_keys_content_error);

		mPublicKeysList = null;

		// mAlertDialogBuilder mCompleteRemovalAlertDialog mBannerAlertDialog mListeningPortAlertDialog
		mAlertDialogBuilder = new AlertDialog.Builder(mContext);
		mAlertDialogBuilder.setCancelable(false);
		mAlertDialogBuilder.setPositiveButton("Okay", this);
		mAlertDialogBuilder.setNegativeButton("Cancel", this);

		mListeningPortAlertDialog = mAlertDialogBuilder.create();
		mListeningPortAlertDialog.setTitle("Listening port");
		mListeningPortView = mLayoutInflater.inflate(R.layout.settings_listening_port, null);
		mListeningPortAlertDialog.setView(mListeningPortView);
		EditText listeningPort = (EditText) mListeningPortView.findViewById(R.id.settings_listening_port);
		listeningPort.setHint("" + SettingsHelper.LISTENING_PORT_DEFAULT);

		mCompleteRemovalAlertDialog = mAlertDialogBuilder.create();
		mCompleteRemovalAlertDialog.setTitle("Confirm");
		mCompleteRemovalAlertDialog.setMessage("This will remove dropbear and all its configuration (including public keys).");

		mCredentialsAlertDialog = mAlertDialogBuilder.create();
		mCredentialsAlertDialog.setTitle("Credentials");
		mCredentialsView = mLayoutInflater.inflate(R.layout.settings_credentials, null);
		mCredentialsAlertDialog.setView(mCredentialsView);
		EditText passwd = (EditText) mCredentialsView.findViewById(R.id.settings_credentials_passwd);
		passwd.setHint(SettingsHelper.CREDENTIALS_PASSWD_DEFAULT);

		mPublicKeysAlertDialog = mAlertDialogBuilder.create();
		mPublicKeysAlertDialog.setTitle("Confirm");
		mPublicKeysAlertDialog.setMessage("You are about to remove this public key.");
		mPublicKey = null;
		
		updateAll();
	}

	public void updateAll() {
		if (RootUtils.hasRootAccess == true && RootUtils.hasBusybox == true && RootUtils.hasDropbear == true) {
			mDropbear.setClickable(true);
			mDropbearContent.setVisibility(View.VISIBLE);
			mDropbearContentError.setVisibility(View.GONE);
			mCredentials.setClickable(true);
			mCredentialsContent.setVisibility(View.VISIBLE);
			mCredentialsContentError.setVisibility(View.GONE);
			mPublicKeys.setClickable(true);
			mPublicKeysContent.setVisibility(View.VISIBLE);
			mPublicKeysContentError.setVisibility(View.GONE);
		}
		else {
			mDropbear.setClickable(false);
			mDropbearContent.setVisibility(View.GONE);
			mDropbearContentError.setVisibility(View.VISIBLE);
			mCredentials.setClickable(false);
			mCredentialsContent.setVisibility(View.GONE);
			mCredentialsContentError.setVisibility(View.VISIBLE);
			mPublicKeys.setClickable(false);
			mPublicKeysContent.setVisibility(View.GONE);
			mPublicKeysContentError.setVisibility(View.VISIBLE);
		}

		// mGeneral
		mAssumeRootAccess.setChecked(SettingsHelper.getInstance(mContext).getAssumeRootAccess());
		mAssumeRootAccess.setOnCheckedChangeListener(this);
		mNotification.setChecked(SettingsHelper.getInstance(mContext).getNotification());
		mNotification.setOnCheckedChangeListener(this);
		mKeepScreenOn.setChecked(SettingsHelper.getInstance(mContext).getKeepScreenOn());
		mKeepScreenOn.setOnCheckedChangeListener(this);
		mOnlyOverWifi.setChecked(SettingsHelper.getInstance(mContext).getOnlyOverWifi());
		mOnlyOverWifi.setOnCheckedChangeListener(this);

		// mDropbear
		mDisallowRootLogins.setChecked(SettingsHelper.getInstance(mContext).getDisallowRootLogins());
		mDisallowRootLogins.setOnCheckedChangeListener(this);
		mDisablePasswordLogins.setChecked(SettingsHelper.getInstance(mContext).getDisablePasswordLogins());
		mDisablePasswordLogins.setOnCheckedChangeListener(this);
		mDisablePasswordLoginsForRoot.setChecked(SettingsHelper.getInstance(mContext).getDisablePasswordLoginsForRoot());
		mDisablePasswordLoginsForRoot.setOnCheckedChangeListener(this);
		updateListeningPort();

		// mCredentials
		updateCredentials();

		// mPublicKeys
		updatePublicKeys();
	}

	public void updateListeningPort() {
		mListeningPortInfos.setText("" + SettingsHelper.getInstance(mContext).getListeningPort());
	}

	public void updateCredentials() {
		String credentials = (SettingsHelper.getInstance(mContext).getCredentialsLogin() == true ? "True" : "False");
		credentials = credentials.concat(" \n" + SettingsHelper.getInstance(mContext).getCredentialsPasswd() + " ");
		mCredentialsInfos.setText(credentials);
	}

	public void updatePublicKeys() {
		if (mPublicKeysList != null) {
			mPublicKeysList.clear();
		}
		mPublicKeysContent.removeAllViews();
		mPublicKeysList = ServerUtils.getPublicKeys(ServerUtils.getLocalDir(mContext) + "/authorized_keys");
		for (String publicKey : mPublicKeysList) {
			if (mPublicKeysList.contains(publicKey) == true) {
				ServerUtils.removePublicKey(publicKey, ServerUtils.getLocalDir(mContext) + "/authorized_keys");
			}
			View view = mLayoutInflater.inflate(R.layout.settings_list, null);
			TextView textView = (TextView) view.findViewById(R.id.settings_name);
			textView.setText(publicKey);
			view.setOnClickListener(this);
			mPublicKeysContent.addView(view);
		}
	}

	private void hideAllBut(LinearLayout oneLinearLayout) {
		if (oneLinearLayout != mGeneralContent) {
			mGeneralContent.setVisibility(View.GONE);
		}
		if (oneLinearLayout != mDropbearContent) {
			mDropbearContent.setVisibility(View.GONE);
		}
		oneLinearLayout.setVisibility(oneLinearLayout.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
	}

	public View getView() {
		return mView;
	}

	public void onClick(View view) {
		// mGeneral
		if (view == mGeneral) {
			hideAllBut(mGeneralContent);
		}
		else if (view == mCompleteRemoval) {
			mCompleteRemovalAlertDialog.show();
		}

		// mDropbear
		else if (view == mDropbear) {
			hideAllBut(mDropbearContent);
		}
		else if (view == mListeningPort) {
			EditText listeningPort = (EditText) mListeningPortView.findViewById(R.id.settings_listening_port);
			listeningPort.setText("" + SettingsHelper.getInstance(mContext).getListeningPort());
			mListeningPortAlertDialog.show();
		}

		// mCredentials
		else if (view == mCredentials || view == mCredentialsContent) {
			ToggleButton login = (ToggleButton) mCredentialsView.findViewById(R.id.settings_credentials_login);
			login.setChecked(SettingsHelper.getInstance(mContext).getCredentialsLogin());
			EditText passwd = (EditText) mCredentialsView.findViewById(R.id.settings_credentials_passwd);
			passwd.setText(SettingsHelper.getInstance(mContext).getCredentialsPasswd());
			mCredentialsAlertDialog.show();
		}

		// mPublicKeys
		else if (view == mPublicKeys) {
			if (Utils.hasStorage(false) == true) {
				MainActivity.needToCheckDropbear = false;
				SettingsPage.goToHome = false;
				// ExplorerActivity
				Intent intent = new Intent(mContext, ExplorerActivity.class);
				((MainActivity) mContext).startActivityForResult(intent, 1);
			}
			else {
				Toast.makeText(mContext, "Error: Could not found any storage", Toast.LENGTH_LONG).show();
			}
		}
		else {
			TextView textView = (TextView) view.findViewById(R.id.settings_name);
			if (textView != null) {
				mPublicKey = textView.getText().toString();
				if (mPublicKey != null) {
					mPublicKeysAlertDialog.show();
				}
			}
		}
	}

	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// mGeneral
		if (buttonView == mAssumeRootAccess) {
			SettingsHelper.getInstance(mContext).setAssumeRootAccess(buttonView.isChecked());
		}
		else if (buttonView == mNotification) {
			SettingsHelper.getInstance(mContext).setNotification(buttonView.isChecked());
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

		// mDropbear
		else if (buttonView == mDisallowRootLogins) {
			SettingsHelper.getInstance(mContext).setDisallowRootLogins(buttonView.isChecked());
			Toast.makeText(mContext, "You need to restart the server for the changes to take effect", Toast.LENGTH_SHORT).show();
		}
		else if (buttonView == mDisablePasswordLogins) {
			SettingsHelper.getInstance(mContext).setDisablePasswordLogins(buttonView.isChecked());
			Toast.makeText(mContext, "You need to restart the server for the changes to take effect", Toast.LENGTH_SHORT).show();
		}
		else if (buttonView == mDisablePasswordLoginsForRoot) {
			SettingsHelper.getInstance(mContext).setDisablePasswordLoginsForRoot(buttonView.isChecked());
			Toast.makeText(mContext, "You need to restart the server for the changes to take effect", Toast.LENGTH_SHORT).show();
		}
	}

	public void onClick(DialogInterface dialog, int button) {
		if (button == DialogInterface.BUTTON_POSITIVE) {
			// mCompleteRemoval
			if (dialog == mCompleteRemovalAlertDialog) {
				// mDropbearRemover
				DropbearRemover dropbearRemover = new DropbearRemover(mContext, this);
				dropbearRemover.execute();
			}

			// mListeningPort
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
						Toast.makeText(mContext, "You need to restart the server for the changes to take effect", Toast.LENGTH_SHORT).show();
					}
					else {
						Log.d(TAG, "SettingsPage: onClick(): Wrong TCP port value [listeningPort=" + port + "]");
						Toast.makeText(mContext, "Error: Wrong TCP port value", Toast.LENGTH_LONG).show();
					}
				}
				else {
					Log.d(TAG, "SettingsPage: onClick(): Wrong type [listeningPort: " + settings_listening_port + "]");
				}
				updateListeningPort();
			}

			// mCredentials
			else if (dialog == mCredentialsAlertDialog) {
				ToggleButton login = (ToggleButton) mCredentialsView.findViewById(R.id.settings_credentials_login);
				Boolean settings_login = login.isChecked();
				EditText passwd = (EditText) mCredentialsView.findViewById(R.id.settings_credentials_passwd);
				String settings_passwd = passwd.getText().toString();
				if (settings_passwd.length() == 0) {
					settings_passwd = SettingsHelper.CREDENTIALS_PASSWD_DEFAULT;
				}
				SettingsHelper.getInstance(mContext).setCredentialsLogin(settings_login);
				SettingsHelper.getInstance(mContext).setCredentialsPasswd(settings_passwd);
				Toast.makeText(mContext, "You need to restart the server for the changes to take effect", Toast.LENGTH_SHORT).show();
				updateCredentials();
			}

			// mPublicKeys
			else {
				if (mPublicKeysList.size() > 0) {
					for (String key : mPublicKeysList) {
						if (key.equals(mPublicKey) == true) {
							ServerUtils.removePublicKey(mPublicKey, ServerUtils.getLocalDir(mContext) + "/authorized_keys");
							Log.d(TAG, "SettingsPage: onClick(): Public key removed: '" + mPublicKey + "'");
							Toast.makeText(mContext, "Public key successfully removed", Toast.LENGTH_SHORT).show();
							updatePublicKeys();
							break ;
						}
					}
				}
			}
		}
	}

	public void onDropbearRemoverComplete(Boolean result) {
		Log.i(TAG, "SettingsPage: onDropbearRemoverComplete(" + result + ")");
		if (result == true) {
			// do not check for dropbear
			RootUtils.hasDropbear = false;
			((MainActivity) mContext).updateSettings();
			((MainActivity) mContext).updateServer();
			MainActivity.dropbearVersion = null;
			Toast.makeText(mContext, "DropBear successfully removed", Toast.LENGTH_SHORT).show();
			mGeneralContent.setVisibility(View.GONE);
			((MainActivity) mContext).goToDefaultPage();
		}
	}
}