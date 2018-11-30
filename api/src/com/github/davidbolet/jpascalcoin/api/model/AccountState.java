package com.github.davidbolet.jpascalcoin.api.model;

import com.google.gson.annotations.SerializedName;

public enum AccountState { 
	
	@SerializedName("normal")
	NORMAL("normal"), 
	@SerializedName("listed")
	LISTED("listed") ;
	
	private final String value;
    public String getValue() {
        return value;
    }

    private AccountState(String value) {
        this.value = value;
    }
}

