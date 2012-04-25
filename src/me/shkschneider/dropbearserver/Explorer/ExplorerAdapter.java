package me.shkschneider.dropbearserver.Explorer;

import me.shkschneider.dropbearserver.R;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ExplorerAdapter extends ArrayAdapter<ExplorerItem> {

	private Context mContext;
	private Integer mId;
	private List<ExplorerItem> mItems;

	public ExplorerAdapter(Context context, Integer id, List<ExplorerItem> items) {
		super(context, id, items);
		mContext = context;
		mId = id;
		mItems = items;
	}

	public ExplorerItem getItem(Integer i) {
		return mItems.get(i);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = vi.inflate(mId, null);
		}
		final ExplorerItem o = mItems.get(position);
		if (o != null) {
			TextView title = (TextView) view.findViewById(R.id.title);
			TextView subtitle = (TextView) view.findViewById(R.id.subtitle);
			if (title != null)
				title.setText(o.getName());
			if (subtitle != null)
				subtitle.setText(o.getData());

		}
		return view;
	}

}


