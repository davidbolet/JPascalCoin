package com.github.davidbolet.jpascalcoin.api.model;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SignResult implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * HEXASTRING with the message to sign
	 */  
	@SerializedName("digest")
	@Expose
	String digest;
	
	/**
	 * HEXASTRING with the public key that used to sign "digest" data
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
