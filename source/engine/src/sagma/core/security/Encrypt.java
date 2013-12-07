package sagma.core.security;

public class Encrypt {
	
	/**
	 * Encrypts a string using a polymorphic encryption.
	 * This is essentially one-way as no decyrption methods are provided.
	 * 
	 * Keys should be more then one letter (very weak encryption)
	 * The longer the key, the better the encryption
	 * 
	 * 
	 * 
	 * @param s
	 * @param key
	 * @return
	 */
	public static String encrypt(String s, String key) {
		String result = "";
		
		int j = 0;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			
			if (j == key.length()) j = 0;
			char k = key.charAt(j);
			j++;
			
			c += k;
			result += c;
		}
		
		return result;
	}
}
