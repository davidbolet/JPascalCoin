package com.github.davidbolet.jpascalcoin.api.model;

import com.google.gson.annotations.SerializedName;

public enum OperationType {
	
	@SerializedName("0")
	BLOCKCHAINREWARD(0),
	
	@SerializedName("1")
	TRANSACTION(1),
	
	@SerializedName("2")
	CHANGEKEY(2),
	
	/**
	 * Recover founds (lost keys)
	 */
	@SerializedName("3")
	RECOVERFUNDS(3);
	
	private final int value;
    public int getValue() {
        return value;
    }

    private OperationType(int value) {
        this.value = value;
    }
}
