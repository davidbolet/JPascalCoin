package com.github.davidbolet.jpascalcoin.common.helper;

import java.math.BigInteger;
import java.security.AlgorithmParameters;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.ECParameterSpec;
import java.util.Arrays;

/**
 * 
 * @author davidbolet
 * Based in some utility functions extracted from
 * https://stackoverflow.com/questions/48783809/ecdsa-sign-with-bouncycastle-and-verify-with-crypto
 * 
 */
public class CryptoUtils {

	/**
	 * Initializes java security with SpongyCastleProvider
	 */
	public static void initSecurity() {
		Security.addProvider(new org.spongycastle.jce.provider.BouncyCastleProvider());
	}
	
	/**
	 * Extracts BigInteger r from a generated signature
	 * @param signature in der format
	 * @return BigInteger with the value of R
	 */
	public static BigInteger extractR(byte[] signature) {
		    int startR = (signature[1] & 0x80) != 0 ? 3 : 2;
		    int lengthR = signature[startR + 1];
		    return new BigInteger(Arrays.copyOfRange(signature, startR + 2, startR + 2 + lengthR));
	}

	/**
	 * Extracts BigInteger s from a generated signature
	 * @param signature in der format
	 * @return BigInteger with the value of S
	 */
	public static BigInteger extractS(byte[] signature) {
	    int startR = (signature[1] & 0x80) != 0 ? 3 : 2;
	    int lengthR = signature[startR + 1];
	    int startS = startR + 2 + lengthR;
	    int lengthS = signature[startS + 1];
	    return new BigInteger(Arrays.copyOfRange(signature, startS + 2, startS + 2 + lengthS));
	}

	/**
	 * Returns a der-encoded signature
	 * @param r BigInteger r
	 * @param s BigInteger s
	 * @return byte array with the corresponding representation
	 */
	public static byte[] getDerEncodedSignature(BigInteger r, BigInteger s) {
	    return derSign(r.toByteArray(), s.toByteArray());
	}
	
	/**
	 * Returns a der-encoded signature
	 * @param rb byte array corresponding to biginteger r
	 * @param sb byte array orresponding to biginteger s
	 * @return bytearray with the corresponding representation
	 */
	public static byte[] derSign(byte[] rb, byte[] sb) {
	    int off = (2 + 2) + rb.length;
	    int tot = off + (2 - 2) + sb.length;
	    byte[] der = new byte[tot + 2];
	    der[0] = 0x30;
	    der[1] = (byte) (tot & 0xff);
	    der[2 + 0] = 0x02;
	    der[2 + 1] = (byte) (rb.length & 0xff);
	    System.arraycopy(rb, 0, der, 2 + 2, rb.length);
	    der[off + 0] = 0x02;
	    der[off + 1] = (byte) (sb.length & 0xff);
	    System.arraycopy(sb, 0, der, off + 2, sb.length);
	    return der;
	}
		

	/**
	 * Gets the corresponding Elliptique curve
	 * @param curve Name of the curve
	 * @return ECParameterSpec
	 */
	public static ECParameterSpec getECParameterSpec(String curve)  {
		try {
			AlgorithmParameters parameters = null;
			try {
				parameters =AlgorithmParameters.getInstance("EC", "SC"); 
			} catch (NoSuchProviderException ex) {
				initSecurity();
				parameters =AlgorithmParameters.getInstance("EC", "SC"); 
			}
			if (parameters==null) {
				parameters = AlgorithmParameters.getInstance("EC", "BC"); 
				if (parameters==null)
					parameters =AlgorithmParameters.getInstance("EC", "SunEC");
			}
	        parameters.init(new ECGenParameterSpec(curve));
	        ECParameterSpec ecParameters = parameters.getParameterSpec(ECParameterSpec.class);
	        return ecParameters;
		}
		catch(Exception ex) {
			return null;
		}
	}

	
	/**
	 * Returns the SHA 256 digest of the given String
	 * @param input String to process
	 * @return String Hex-encoded result of the SHA256 result
	 */
	public static String getSHA256(String input) {
		try {
			MessageDigest sha = MessageDigest.getInstance("SHA-256");
			byte[] s1 = sha.digest(input.getBytes());
			return HexConversionsHelper.byteToHex(s1);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
