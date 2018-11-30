package com.github.davidbolet.jpascalcoin.common.model;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SignResult implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/* Fields for communicating with the daemon*/
	/**
	 * HEXASTRING with the message to sign
	 */  
	@SerializedName("digest")
	@Expose
	String digest;
	
	/**
	 * HEXASTRING with the public key that must be used to verify "digest" data, or to sign 
	 */  
	@SerializedName("enc_pubkey")
	@Expose
	String encPubkey;
	
	/**
	 * HEXASTRING with signature
	 */  
	@SerializedName("signature")
	@Expose
	String signature;
	

	public String getDigest() {
		return digest;
	}

	public void setDigest(String digest) {
		this.digest = digest;
	}

	public String getEncPubkey() {
		return encPubkey;
	}

	public void setEncPubkey(String encPubkey) {
		this.encPubkey = encPubkey;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}
	
}
