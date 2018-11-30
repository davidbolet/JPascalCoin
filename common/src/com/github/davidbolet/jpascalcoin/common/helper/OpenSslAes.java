package com.github.davidbolet.jpascalcoin.common.helper;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.logging.Logger;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.security.NoSuchAlgorithmException;

/**
* Mimics the OpenSSL AES Cipher options for encrypting and decrypting messages using a shared key (aka password) with symetric ciphers.
*/
public class OpenSslAes {
	private static final Logger logger = Logger.getLogger(OpenSslAes.class.getName());
	/** OpenSSL's magic initial bytes. */
	private static final String SALTED_STR = "Salted__";
	private static final byte[] SALTED_MAGIC = SALTED_STR.getBytes(UTF_8);


	static String encryptAndURLEncode(String password, String clearText) throws Exception {
	    String encrypted = encrypt(password, clearText);
	    return URLEncoder.encode(encrypted, UTF_8.name() );
	}

	/**
	 *
	 * @param password  The password / key to encrypt with.
	 * @param source Data to encrypt
	 * @return  A base64 encoded string containing the encrypted data.
	 * @throws Exception if there's an error
	 */
	public static String encrypt(String password, String source) throws Exception {
		 try {
			 final byte[] pass = password.getBytes(UTF_8);
				
			 final byte[] inBytes = HexConversionsHelper.decodeStr2Hex(source);
			 
			 final MessageDigest md = MessageDigest.getInstance("SHA-256");
			 md.update(pass);
			 md.update(SALTED_MAGIC);
			 final byte[] key =md.digest();
			    
			 final MessageDigest md2 = MessageDigest.getInstance("SHA-256");
			 md2.update(key);
			 md2.update(pass);
			 md2.update(SALTED_MAGIC);
			    
		     final byte[] longIv = md2.digest();
		     final byte[] keyValue = Arrays.copyOfRange(key, 0, 32);
			   
			 final byte[] iv = Arrays.copyOfRange(longIv, 0, 16);
			 final SecretKeySpec keySpec = new SecretKeySpec(keyValue, "AES");
			    
			 final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			 cipher.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv));
			 final byte[] clear = array_concat(SALTED_MAGIC, cipher.doFinal(inBytes));
			 
			 return HexConversionsHelper.byteToHex(clear).toUpperCase();
		    } catch(Exception ex) {
		    	logger.severe(ex.getMessage());
		    	return null;
		    }
	}

	/**
	 * look at http://stackoverflow.com/questions/32508961/java-equivalent-of-an-openssl-aes-cbc-encryption
	 * for what looks like a useful answer. The not-yet-commons-ssl also has an implementation
	 * @param password password to decrypt
	 * @param source The encrypted data
	 * @return String
	 */
	public static String decrypt(String password, String source) {
	    try {
			final byte[] pass = password.getBytes(UTF_8);
	
		    final byte[] inBytes = HexConversionsHelper.decodeStr2Hex(source); //Base64.getDecoder().decode(source.getBytes(UTF_8));
		    
		    final byte[] shouldBeMagic = Arrays.copyOfRange(inBytes, 0, SALTED_MAGIC.length);
		    if (!Arrays.equals(shouldBeMagic, SALTED_MAGIC)) {
		        throw new IllegalArgumentException("Initial bytes from input do not match OpenSSL SALTED_MAGIC salt value.");
		    }
		   
		    final byte[] salt = Arrays.copyOfRange(inBytes, SALTED_MAGIC.length, SALTED_MAGIC.length + 8);
		   
		    final MessageDigest md = MessageDigest.getInstance("SHA-256");
		    md.update(pass);
		    md.update(salt);
		    final byte[] key =md.digest();
		    
		    final MessageDigest md2 = MessageDigest.getInstance("SHA-256");
		    md2.update(key);
		    md2.update(pass);
		    md2.update(salt);
		    
	        final byte[] longIv = md2.digest();
	
		    final byte[] keyValue = Arrays.copyOfRange(key, 0, 32);
		   
		    final byte[] iv = Arrays.copyOfRange(longIv, 0, 16);
		    final SecretKeySpec keySpec = new SecretKeySpec(keyValue, "AES");
		    
		    final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		    cipher.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv));
		    final byte[] clear = cipher.doFinal(inBytes, 16, inBytes.length -16 );
		    return HexConversionsHelper.byteToHex(clear).toUpperCase();
	    } catch(Exception ex) {
	    	logger.severe(ex.getMessage());
	    	return null;
	    }
	}


	private static byte[] array_concat(final byte[] a, final byte[] b) {
	    final byte[] c = new byte[a.length + b.length];
	    System.arraycopy(a, 0, c, 0, a.length);
	    System.arraycopy(b, 0, c, a.length, b.length);
	    return c;
	}

	/**
	 * Decodes an string using given password
	 * @param password password used
	 * @param encString encoded string
	 * @return String decoded
	 * @throws Exception if there's an IO error
	 */
	public static String dec(String password, String encString)
	        throws Exception {

		int ivSize = 16;
        int keySize = 32;
        
        byte[] iv = new byte[ivSize];
        byte[] encryptedIvTextBytes=HexConversionsHelper.decodeStr2Hex(encString);
        
        final byte[] salt = Arrays.copyOfRange(encryptedIvTextBytes, SALTED_MAGIC.length, SALTED_MAGIC.length + 8);
	    final byte[] passAndSalt = array_concat(password.getBytes(UTF_8), salt);
        // Extract IV.

        System.arraycopy(encryptedIvTextBytes, 0, iv, 0, iv.length);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        // Extract encrypted part.
        int encryptedSize = encryptedIvTextBytes.length - ivSize;
        byte[] encryptedBytes = new byte[encryptedSize];
        System.arraycopy(encryptedIvTextBytes, ivSize, encryptedBytes, 0, encryptedSize);

        // Hash key.
        byte[] keyBytes = new byte[keySize];
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(passAndSalt);
        System.arraycopy(md.digest(), 0, keyBytes, 0, keyBytes.length);
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");

        // Decrypt.
        Cipher cipherDecrypt = Cipher.getInstance("AES/CBC/NoPadding");
        cipherDecrypt.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] decrypted = cipherDecrypt.doFinal(encryptedBytes);
        return HexConversionsHelper.byteToHex(decrypted).toUpperCase();
	}


	

}
