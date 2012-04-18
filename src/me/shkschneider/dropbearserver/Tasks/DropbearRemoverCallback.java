/*
 * SirDarius <http://stackoverflow.com/questions/3291490/common-class-for-asynctask-in-android>
 */
package me.shkschneider.dropbearserver.Tasks;

public interface DropbearRemoverCallback<T> {

	public void onDropbearRemoverComplete(T result);

}
