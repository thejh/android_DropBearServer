/*
 * Thanks to:
 * - SirDarius <http://stackoverflow.com/questions/3291490/common-class-for-asynctask-in-android>
 */
package me.shkschneider.dropbearserver;

public interface ASyncTaskCallback<T> {

	public void DropbearInstallerDelegate(T result);
	public void ServerStarterDelegate(T result);
	public void ServerStopperDelegate(T result);
	
}
