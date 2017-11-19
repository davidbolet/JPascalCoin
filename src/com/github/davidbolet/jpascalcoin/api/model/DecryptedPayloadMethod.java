package com.github.davidbolet.jpascalcoin.api.model;

import com.google.gson.annotations.SerializedName;

public enum DecryptedPayloadMethod {
	@SerializedName("key") //1? key
	KEY("key"), 
	@SerializedName("pwd")
	PWD("pwd") ;
	
	private final String value;
    public String getValue() {
        return value;
    }

    private DecryptedPayloadMethod(String value) {
        this.value = value;
    }
}
