/*
 * Johan Nilsson <https://github.com/johannilsson/android-actionbar>
 */
package me.shkschneider.dropbearserver;

import com.markupartist.android.widget.ActionBar.Action;

import android.content.Context;
import android.view.View;

class CheckAction implements Action {
	
	private Context mContext;
	
	public CheckAction(Context context) {
		mContext = context;
	}
	
    @Override
    public int getDrawable() {
        return R.drawable.ic_launcher;
    }

    @Override
    public void performAction(View view) {
    	((MainActivity) mContext).check();
    }

}
