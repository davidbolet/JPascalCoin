package com.github.davidbolet.jpascalcoin.crypto.model;

import com.github.davidbolet.jpascalcoin.common.model.PayLoadEncryptionMethod;

/**
 * Abstract class that represent protocol operations
 * @author davidbolet
 *
 */
public abstract class PascOperation {
	public static final float DEFAULT_PROTOCOL_VERSION=4.0f;
	
	protected final byte[] payload;
	protected final PayLoadEncryptionMethod payloadEncriptionMethod;
	protected final String password;
	protected String finalHash;
	
	/**
	 * Default constructor. Will be specialized by derived classes
	 * @param payload Payload to include with the operation
	 * @param payloadMethod enum indicating how the payload will be included
	 * @param pwd String Password to encript the payload if encription method is 'password'
	 */
	PascOperation(byte[] payload, PayLoadEncryptionMethod payloadMethod, String pwd) {
		this.payload=payload;
		this.payloadEncriptionMethod=payloadMethod;
		this.password=pwd;
	}

	/**
	 * Generated the byte array to be signed for this operation.
	 * Calls to generateOpDigest with the default protocol version (DEFAULT_PROTOCOL_VERSION)
	 * @return byte[] array with the bytes to be signed
	 */
	public byte[] generateOpDigest() {
		return generateOpDigest(DEFAULT_PROTOCOL_VERSION);
	}
	
	/**
	 * Generated the byte array to be signed for this operation
	 * @param protocolVersion Version of the protocol, to introduce future changes
	 * @return byte[] array with the bytes to be signed
	 */
	public abstract byte[] generateOpDigest(float protocolVersion);
	
	/**
	 * Structure created from the operation fields following PascalCoin 
	 * specification, prior to apply the SHA256 algorithm in order to 
	 * 
	 * @return Hex String representing the value of the bytes
	 */
	public String getFinalHash() {
		return this.finalHash==null?generateOpDigest().toString():this.finalHash;
	}
	
	/**
	 * Utility function to concatenate a byte array
	 * @param a first byte array
	 * @param b seconf byte array
	 * @return byte array with both arrays concatenated
	 */
	protected static byte[] array_concat(final byte[] a, final byte[] b) {
	    final byte[] c = new byte[a.length + b.length];
	    System.arraycopy(a, 0, c, 0, a.length);
	    System.arraycopy(b, 0, c, a.length, b.length);
	    return c;
	}

	/**
	 * Generates the RAWOPERATIONS from the signed text
	 * @param signR String signature.R
	 * @param signS String signature.S
	 * @return String containing the RAWOPERATIONS string to be send
	 */
	public abstract String getRawOperations(String signR, String signS);
}
