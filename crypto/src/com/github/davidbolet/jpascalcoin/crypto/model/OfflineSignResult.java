package com.github.davidbolet.jpascalcoin.crypto.model;

import java.math.BigInteger;

import com.github.davidbolet.jpascalcoin.common.helper.CryptoUtils;
import com.github.davidbolet.jpascalcoin.common.helper.HexConversionsHelper;
import com.github.davidbolet.jpascalcoin.common.model.SignResult;

/**
 * Class for handling offline signatures
 * 
 * @author davidbolet
 *
 */
public class OfflineSignResult extends SignResult {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	final BigInteger R;
	final BigInteger S;
	final byte[] byteSignature;
	
	/**
	 * Constructor
	 * @param signature bytearray with generated signature
	 */
	public OfflineSignResult(byte[] signature) {
		this.R=CryptoUtils.extractR(signature);
		this.S=CryptoUtils.extractS(signature);	
		this.byteSignature=signature;
	}
	
	/**
	 * Returns value of R
	 * @return BigInteger R
	 */
	public BigInteger getR() {
		return R;
	}
	
	/**
	 * Returns value of S
	 * @return BigInteger S
	 */
	public BigInteger getS() {
		return S;
	}
	
	/**
	 * Returns signature in Der format
	 * @return String with the Der representation of the byte array
	 */
	public String getDerSign() {
		return HexConversionsHelper.byteToHex(CryptoUtils.getDerEncodedSignature(R, S)).toUpperCase();
	}
	
	/**
	 * Returns string representation of the initial bytearray
	 * @return Signature
	 */
	public String getStringSignature() {
		return HexConversionsHelper.byteToHex(byteSignature).toUpperCase();
	}
	
	/**
	 * Returns signature in PASC format
	 * @return String with the signature as expected by pascalcoin
	 */
	@Override
	public String getSignature() {
		String signR=getStringR();
		String signS=getStringS();
		StringBuffer digestBuffer = new StringBuffer();
		digestBuffer.append(HexConversionsHelper.int2BigEndianHex(signR.length()/2))
		.append(signR)
		.append(HexConversionsHelper.int2BigEndianHex(signS.length()/2))
		.append(signS);			
		return digestBuffer.toString().toUpperCase();
	}
	
	/**
	 * Returns raw signature created initially
	 * @return byte[] 
	 */
	public byte[] getByteSignature() {
		return byteSignature;
	}
	
	/**
	 * Returns String representation of BigInteger R
	 * @return String R
	 */
    public String getStringR() { 
    	return getByteArrayFromBigInteger(R);
    	//return R.toString(16).toUpperCase(); 
    };
    
	/**
	 * Returns String representation of BigInteger S
	 * @return String S
	 */
    public String getStringS() { 
    	return getByteArrayFromBigInteger(S);
    	// This is equivalent to: return S.toString(16).toUpperCase(); 
    };
    
    private static String getByteArrayFromBigInteger(BigInteger number) {
    	byte[] array = number.toByteArray();
    	if (array[0] == 0) {
    	    byte[] tmp = new byte[array.length - 1];
    	    System.arraycopy(array, 1, tmp, 0, tmp.length);
    	    array = tmp;
    	}
    	return HexConversionsHelper.byteToHex(array).toUpperCase();
    }
	
}
