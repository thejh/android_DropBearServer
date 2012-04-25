package me.shkschneider.dropbearserver.Explorer;

public class ExplorerItem implements Comparable<ExplorerItem> {
	
	private String mName;
	private String mData;
	private String mPath;

	public ExplorerItem(String name, String data, String path) {
		mName = name;
		mData = data;
		mPath = path;
	}
	
	public String getName() {
		return mName;
	}
	
	public String getData() {
		return mData;
	}
	
	public String getPath() {
		return mPath;
	}

	public int compareTo(ExplorerItem o) {
		if (mName != null)
			return mName.toLowerCase().compareTo(o.getName().toLowerCase()); 
		else 
			throw new IllegalArgumentException();
	}
}
