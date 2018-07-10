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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((base58PubKey == null) ? 0 : base58PubKey.hashCode());
		result = prime * result + ((encPubKey == null) ? 0 : encPubKey.hashCode());
		result = prime * result + ((keyType == null) ? 0 : keyType.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((x == null) ? 0 : x.hashCode());
		result = prime * result + ((y == null) ? 0 : y.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof PublicKey)) {
			return false;
		}
		PublicKey other = (PublicKey) obj;
		if (base58PubKey == null) {
			if (other.base58PubKey != null) {
				return false;
			}
		} else if (!base58PubKey.equals(other.base58PubKey)) {
			return false;
		}
		if (encPubKey == null) {
			if (other.encPubKey != null) {
				return false;
			}
		} else if (!encPubKey.equals(other.encPubKey)) {
			return false;
		}
		if (keyType != other.keyType) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (x == null) {
			if (other.x != null) {
				return false;
			}
		} else if (!x.equals(other.x)) {
			return false;
		}
		if (y == null) {
			if (other.y != null) {
				return false;
			}
		} else if (!y.equals(other.y)) {
			return false;
		}
		return true;
	}
}
