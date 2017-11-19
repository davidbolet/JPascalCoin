package com.github.davidbolet.jpascalcoin.api.model;

import com.google.gson.annotations.SerializedName;

public enum PayLoadEncryptionMethod {
	/**
	 * Not encoded. Will be visible for everybody
	 */
	@SerializedName("none")
	NONE("none"),
	
	/**
	 * Using Public key of "target" account. Only "target" will be able to decrypt this payload
	 */
	@SerializedName("dest")
	DEST("dest"),
	
	/**
	 * Using sender Public key. Only "sender" will be able to decrypt this payload
	 */
	@SerializedName("sender")
	SENDER("sender"),
	
	/**
	 * Encrypted data using pwd param
	 */
	@SerializedName("aes")
	AES("aes");
	
	private final String value;
    public String getValue() {
        return value;
    }

    private PayLoadEncryptionMethod(String value) {
        this.value = value;
    }
}
