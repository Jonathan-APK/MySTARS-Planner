/**
 * Represents an interface for file IO operations to be implemented.
 * @author Lim Boon Leng
 * @version 1.0
 * @since 2017-04-06
*/

package Interface;

import java.util.List;

public interface FileIOInterface {
	/**
	* Reads the data from the database file
	* @return list of objects in the database file.
	*/
	@SuppressWarnings("rawtypes")
	public abstract List readSerializedObject();

	/**
	* Writes data to the database file
	* @param list The list of objects to write
	*/
	@SuppressWarnings("rawtypes")
	public abstract void writeSerializedObject(List list);
}