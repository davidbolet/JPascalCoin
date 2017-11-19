package com.github.davidbolet.jpascalcoin.api.model;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DecryptedPayload implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Decryption result
	 */
	 @SerializedName("result")
	@Expose
	protected Boolean result;

	/**
	 * HEXASTRING - Same value than param payload sent
	 */
	@SerializedName("enc_payload")
	@Expose
	protected String originalPayload;

	/**
	 * Unencoded value in readable format (no HEXASTRING)
	 */
	@SerializedName("unenc_payload")
	@Expose
	protected String unencryptedPayload;
                                           
	/**
	 * HEXASTRING - Unencoded value in hexaString
	 */
	@SerializedName("unenc_hexpayload")
	@Expose
	protected String unencryptedPayloadHex;

	/**
	 * String - "key" or "pwd"
	 */
	@SerializedName("payload_method")
	@Expose
	protected DecryptedPayloadMethod payloadMethod;

	/**
	 * HEXASTRING - Encoded public key used to decrypt when method = "key"
	 */
	@SerializedName("enc_pubkey")
	@Expose
	protected String encodedPubKey;

	/**
	 *  String value used to decrypt when method = "pwd"
	 */
	@SerializedName("pwd")
	@Expose
	protected String decryptPassword;

	public Boolean getResult() {
		return result;
	}

	public void setResult(Boolean result) {
		this.result = result;
	}

	public String getOriginalPayload() {
		return originalPayload;
	}

	public void setOriginalPayload(String originalPayload) {
		this.originalPayload = originalPayload;
	}

	public String getUnencryptedPayload() {
		return unencryptedPayload;
	}

	public void setUnencryptedPayload(String unencryptedPayload) {
		this.unencryptedPayload = unencryptedPayload;
	}

	public String getUnencryptedPayloadHex() {
		return unencryptedPayloadHex;
	}

	public void setUnencryptedPayloadHex(String unencryptedPayloadHex) {
		this.unencryptedPayloadHex = unencryptedPayloadHex;
	}

	public DecryptedPayloadMethod getPayloadMethod() {
		return payloadMethod;
	}

	public void setPayloadMethod(DecryptedPayloadMethod payloadMethod) {
		this.payloadMethod = payloadMethod;
	}

	public String getEncodedPubKey() {
		return encodedPubKey;
	}

	public void setEncodedPubKey(String encodedPubKey) {
		this.encodedPubKey = encodedPubKey;
	}

	public String getDecryptPassword() {
		return decryptPassword;
	}

	public void setDecryptPassword(String decryptPassword) {
		this.decryptPassword = decryptPassword;
	}
}