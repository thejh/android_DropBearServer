/*
 * Michael Almyros <http://www.dreamincode.net/forums/topic/190013-creating-simple-file-chooser/>
 */
package me.shkschneider.dropbearserver.Explorer;

public class ExplorerItem implements Comparable<ExplorerItem> {
	
	private String mName;
	private String mPath;
	private Boolean mIsDirectory;

	public ExplorerItem(String name, String path, Boolean isDirectory) {
		mName = name;
		mPath = path;
		mIsDirectory = isDirectory;
	}
	
	public String getName() {
		return mName;
	}
	
	public String getPath() {
		return mPath;
	}
	
	public Boolean isDirectory() {
		return mIsDirectory;
	}

	public int compareTo(ExplorerItem item) {
		if (mName != null)
			return mName.toLowerCase().compareTo(item.getName().toLowerCase()); 
		else 
			throw new IllegalArgumentException();
	}
}
