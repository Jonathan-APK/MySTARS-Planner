/**
	Represents a control class for the hashing of the password.
	
	@author Lim Boon Leng
	@version 1.0
	@since 2017-04-06
*/

package Control;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHashController {
	/**
	* Hashes the input string using SHA-256 algorithm
	* @param stringToEncrypt The string to encrypt (the password)
	* @return the hashed string
	*/
	public static String hash(String stringToEncrypt) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(stringToEncrypt.getBytes());

			byte byteData[] = md.digest();

			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
			}

			stringToEncrypt = sb.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return stringToEncrypt;
	}
}