/**
 * Represents an interface for admin and students to implement to login.
 * @author Lim Boon Leng
 * @version 1.0
 * @since 2017-04-06
*/
package Interface;

public interface LoginInterface {
	/**
	* Login method
	* Compares username and passwordHash with database data
	* @param username The user's username
	* @param passwordHash The user's passwordHash
	* @return login success failure boolean.
	*/
	public abstract boolean login(String username, String passwordHash);
}