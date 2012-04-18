/*
 * Johan Nilsson <https://github.com/johannilsson/android-actionbar>
 */
package me.shkschneider.dropbearserver;

import com.markupartist.android.widget.ActionBar.Action;

import android.content.Context;
import android.view.View;

class HomeAction implements Action {

	private Context mContext;
	
	public HomeAction(Context context) {
		mContext = context;
	}
	
    @Override
    public int getDrawable() {
        return R.drawable.ic_launcher;
    }

    @Override
    public void performAction(View view) {
    	((MainActivity) mContext).goToDefaultPage();
    }

}
