package com.github.davidbolet.jpascalcoin.api.model;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PublicKey implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	* Human readable name stored at the Wallet for this key
	*/
	@SerializedName("name")
    @Expose
	protected String name;

	/**
	* If false then Wallet doesn't have Private key for this public key, so, Wallet cannot execute operations with this key
	*/
	@SerializedName("can_use")
	@Expose
	protected Boolean canUse;

	/**
	* Encoded value of this public key.This HEXASTRING has no checksum, so, if using it always must be sure that value is correct
	*/
	@SerializedName("enc_pubkey")
	@Expose
	protected String encPubKey;

	/**
	* Encoded value of this public key in Base 58 format, also contains a checksum.This is the same value that Application Wallet exports as a public key
	*/
	@SerializedName("b58_pubkey")
	@Expose
	protected String base58PubKey;

	/**
	*Indicates which EC type is used (EC_NID)
	*/
	@SerializedName("ec_nid")
	@Expose
	protected KeyType keyType;

	/**
	* HEXASTRING with x value of public key
	*/
	@SerializedName("x")
	@Expose
	protected String x;

	/**
	* HEXASTRING with y value of public key
	*/
	@SerializedName("y")
	@Expose	
	protected String y;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getCanUse() {
		return canUse;
	}

	public void setCanUse(Boolean canUse) {
		this.canUse = canUse;
	}

	public String getEncPubKey() {
		return encPubKey;
	}

	public void setEncPubKey(String encPubKey) {
		this.encPubKey = encPubKey;
	}

	public String getBase58PubKey() {
		return base58PubKey;
	}

	public void setBase58PubKey(String base58PubKey) {
		this.base58PubKey = base58PubKey;
	}

	public KeyType getKeyType() {
		return keyType;
	}

	public void setKeyType(KeyType keyType) {
		this.keyType = keyType;
	}

	public String getX() {
		return x;
	}

	public void setX(String x) {
		this.x = x;
	}

	public String getY() {
		return y;
	}

	public void setY(String y) {
		this.y = y;
	}
}
