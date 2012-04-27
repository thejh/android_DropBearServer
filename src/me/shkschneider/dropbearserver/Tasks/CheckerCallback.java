/*
 * SirDarius <http://stackoverflow.com/questions/3291490/common-class-for-asynctask-in-android>
 */
package me.shkschneider.dropbearserver.Tasks;

public interface CheckerCallback<T> {

	public void onCheckerComplete(T result);
	
}
