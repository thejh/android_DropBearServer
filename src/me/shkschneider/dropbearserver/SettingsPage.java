package me.shkschneider.dropbearserver;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public class SettingsPage {
	
	private View mView;
	
	public void initView(Context context) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mView = inflater.inflate(R.layout.settings, null);
	}
	
	public View getView() {
		
		return mView;
	}
}