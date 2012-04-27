/*
 * Johan Nilsson <https://github.com/johannilsson/android-actionbar>
 */
package me.shkschneider.dropbearserver.Explorer;

import com.markupartist.android.widget.ActionBar.Action;

import android.content.Context;
import android.view.View;

class CancelAction implements Action {

	private Context mContext;
	
	public CancelAction(Context context) {
		mContext = context;
	}
	
    public int getDrawable() {
        return android.R.drawable.ic_menu_close_clear_cancel;
    }

    public void performAction(View view) {
    	((ExplorerActivity) mContext).finish();
    }

}
