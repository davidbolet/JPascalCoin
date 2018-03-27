package com.github.davidbolet.jpascalcoin.api.model;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DecodeOpHashResult implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@SerializedName("block")
    @Expose
	protected Integer block;
	
	@SerializedName("account")
    @Expose
	protected Integer account;
	
	@SerializedName("n_operation")
    @Expose
	protected Integer nOperation;
	
	@SerializedName("md160hash")
    @Expose
	protected String md160Hash;

	public Integer getBlock() {
		return block;
	}

	public void setBlock(Integer block) {
		this.block = block;
	}

	public Integer getAccount() {
		return account;
	}

	public void setAccount(Integer account) {
		this.account = account;
	}

	public Integer getnOperation() {
		return nOperation;
	}

	public void setnOperation(Integer nOperation) {
		this.nOperation = nOperation;
	}

	public String getMd160Hash() {
		return md160Hash;
	}

	public void setMd160Hash(String md160Hash) {
		this.md160Hash = md160Hash;
	}
	
	

}
