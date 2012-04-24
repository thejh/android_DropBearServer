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
	private LinearLayout mHowToUseDropbearServer;
	private LinearLayout mHowToUseDropbearServerContent;
	private LinearLayout mHowToGeneratePublicKey;
	private LinearLayout mHowToGeneratePublicKeyContent;
	private LinearLayout mErrorPermissionDenied;
	private LinearLayout mErrorPermissionDeniedContent;
	private LinearLayout mErrorHostIdentificationChanged;
	private LinearLayout mErrorHostIdentificationChangedContent;

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
		
		// mHowToUseDropbearServer mHowToUseDropbearServerContent
		mHowToUseDropbearServer = (LinearLayout) mView.findViewById(R.id.how_to_use_dropbear_server);
		mHowToUseDropbearServer.setOnClickListener(this);
		mHowToUseDropbearServerContent = (LinearLayout) mView.findViewById(R.id.how_to_use_dropbear_server_content);
		
		// mHowToGeneratePublicKey mHowToGeneratePublicKeyContent
		mHowToGeneratePublicKey = (LinearLayout) mView.findViewById(R.id.how_to_generate_publickey);
		mHowToGeneratePublicKey.setOnClickListener(this);
		mHowToGeneratePublicKeyContent = (LinearLayout) mView.findViewById(R.id.how_to_generate_publickey_content);
		
		// mErrorPermissionDenied mErrorPermissionDeniedContent
		mErrorPermissionDenied = (LinearLayout) mView.findViewById(R.id.error_permission_denied);
		mErrorPermissionDenied.setOnClickListener(this);
		mErrorPermissionDeniedContent = (LinearLayout) mView.findViewById(R.id.error_permission_denied_content);
		
		// mErrorHostIdentificationChanged mErrorHostIdentificationChangedContent
		mErrorHostIdentificationChanged = (LinearLayout) mView.findViewById(R.id.error_host_identification_changed);
		mErrorHostIdentificationChanged.setOnClickListener(this);
		mErrorHostIdentificationChangedContent = (LinearLayout) mView.findViewById(R.id.error_host_identification_changed_content);
	}
	
	public void hideAllBut(LinearLayout oneLinearLayout) {
		if (mWhatIsRootContent != oneLinearLayout)
			mWhatIsRootContent.setVisibility(View.GONE);
		if (mWhatIsDropbearContent != oneLinearLayout)
			mWhatIsDropbearContent.setVisibility(View.GONE);
		if (mHowToUseDropbearServerContent != oneLinearLayout)
			mHowToUseDropbearServerContent.setVisibility(View.GONE);
		if (mHowToGeneratePublicKeyContent != oneLinearLayout)
			mHowToGeneratePublicKeyContent.setVisibility(View.GONE);
		if (mErrorPermissionDeniedContent != oneLinearLayout)
			mErrorPermissionDeniedContent.setVisibility(View.GONE);
		if (mErrorHostIdentificationChangedContent != oneLinearLayout)
			mErrorHostIdentificationChangedContent.setVisibility(View.GONE);
		oneLinearLayout.setVisibility(oneLinearLayout.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
	}

	public View getView() {
		return mView;
	}

	public void onClick(View v) {
		if (v == mWhatIsRoot) {
			hideAllBut(mWhatIsRootContent);
		}
		else if (v == mWhatIsDropbear) {
			hideAllBut(mWhatIsDropbearContent);
		}
		else if (v == mHowToUseDropbearServer) {
			hideAllBut(mHowToUseDropbearServerContent);
		}
		else if (v == mHowToGeneratePublicKey) {
			hideAllBut(mHowToGeneratePublicKeyContent);
		}
		else if (v == mErrorPermissionDenied) {
			hideAllBut(mErrorPermissionDeniedContent);
		}
		else if (v == mErrorHostIdentificationChanged) {
			hideAllBut(mErrorHostIdentificationChangedContent);
		}
	}
}