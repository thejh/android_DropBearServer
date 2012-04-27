/*
 * Johan Nilsson <https://github.com/johannilsson/android-actionbar>
 */
package me.shkschneider.dropbearserver.Explorer;

import com.markupartist.android.widget.ActionBar.Action;

import me.shkschneider.dropbearserver.R;
import me.shkschneider.dropbearserver.Pages.SettingsPage;

import android.content.Context;
import android.view.View;

class HomeAction implements Action {

	private Context mContext;
	
	public HomeAction(Context context) {
		mContext = context;
	}
	
    public int getDrawable() {
        return R.drawable.ic_launcher;
    }

    public void performAction(View view) {
    	((ExplorerActivity) mContext).finish();
    	SettingsPage.goToHome = true;
    }

}
