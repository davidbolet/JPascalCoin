package com.github.davidbolet.jpascalcoin.api.model;

import com.google.gson.annotations.SerializedName;

public enum OperationSubType {
	
	@SerializedName("11")
	TRANSACTION_SENDER(11),
	
	@SerializedName("12")
	TRANSACTION_RECEIVER(12),
	
	@SerializedName("13")
	BUYTRANSACTION_BUYER(13),
	
	@SerializedName("14")
	BUYTRANSACTION_TARGET(14),
	
	@SerializedName("15")
	BUYTRANSACTION_SELLER(15),	
	
	@SerializedName("21")
	CHANGE_KEY(21),

	@SerializedName("31")
	RECOVER(31),
	
	@SerializedName("41")
	LIST_ACCOUNT_FOR_PUBLIC_SALE(41),
	
	@SerializedName("42")
	LIST_ACCOUNT_FOR_PRIVATE_SALE(42),
	
	@SerializedName("51")
	DELIST_ACCOUNT(51),
	
	@SerializedName("61")
	BUYACCOUNT_BUYER(61),
	
	@SerializedName("62")
	BUYACCONT_TARGET(62),
	
	@SerializedName("63")
	BUYACCOUNT_SELLER(63),
	
	@SerializedName("71")
	CHANGE_KEY_SIGNED(71),
	
	@SerializedName("81")
	CHANGE_ACCOUNT_INFO(81);
	
	private final int value;
    public int getValue() {
        return value;
    }

    private OperationSubType(int value) {
        this.value = value;
    }
}
