/*
 * Thanks to:
 * - SirDarius <http://stackoverflow.com/questions/3291490/common-class-for-asynctask-in-android>
 */
package me.shkschneider.dropbearserver;

public interface ASyncTaskCallback<T> {

	public void onDropbearInstallerComplete(T result);
	public void onStartServerComplete(T result);
	public void onStopServerComplete(T result);
	
}
