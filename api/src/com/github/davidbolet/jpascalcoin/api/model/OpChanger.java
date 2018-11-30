package com.github.davidbolet.jpascalcoin.api.model;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * OpChanger Object, created on version 3.0
 * This object represents an element of the "changers" array for Operation/Multioperation objects
 * @author davidbolet
 *
 */
public class OpChanger implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Sending account
	 */
	@SerializedName("account")
	@Expose
	protected Integer account;
	
	/**
	 * n_operation param, the same as aadded in version 2.1.6, but for multioperation
	 * n_operation is an incremental value to protect double spend
	 */
	@SerializedName("n_operation")
	@Expose
	protected Integer nOperation;
	
	/**
	 * new_enc_pubkey, if public key is changed or when is listed for a private sale
	 */
	@SerializedName("new_enc_pubkey")
	@Expose
	protected String newEncPubkey;
	
	/**
	 * new_name, if name is changed
	 */
	@SerializedName("new_name")
	@Expose
	protected String newName;
	
	/**
	 * new type, if type is changed
	 */
	@SerializedName("new_type")
	@Expose
	protected Integer newType;
	
	/**
	 * If is listed for sale (public or private) will show seller account
	 */
	@SerializedName("seller_account")
	@Expose
	protected Integer sellerAccount;
	
	/**
	 * If is listed for sale (public or private) will show account price
	 */
	@SerializedName("account_price")
	@Expose
	protected Double accountPrice;
	
	/**
	 * If is listed for for private sale will show block locked
	 */
	@SerializedName("locked_until_block")
	@Expose
	protected Integer lockedUntilBlock;
	
	/** 
	 * Fee of this operation, in negative value, due it's outgoing from "account" (PASCURRENCY)
	*/
	@SerializedName("fee")
	@Expose
	protected Double fee;

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

	public String getNewEncPubkey() {
		return newEncPubkey;
	}

	public void setNewEncPubkey(String newEncPubkey) {
		this.newEncPubkey = newEncPubkey;
	}

	public String getNewName() {
		return newName;
	}

	public void setNewName(String newName) {
		this.newName = newName;
	}

	public Integer getNewType() {
		return newType;
	}

	public void setNewType(Integer newType) {
		this.newType = newType;
	}

	public Integer getSellerAccount() {
		return sellerAccount;
	}

	public void setSellerAccount(Integer sellerAccount) {
		this.sellerAccount = sellerAccount;
	}

	public Double getAccountPrice() {
		return accountPrice;
	}

	public void setAccountPrice(Double accountPrice) {
		this.accountPrice = accountPrice;
	}

	public Integer getLockedUntilBlock() {
		return lockedUntilBlock;
	}

	public void setLockedUntilBlock(Integer lockedUntilBlock) {
		this.lockedUntilBlock = lockedUntilBlock;
	}

	public Double getFee() {
		return fee;
	}

	public void setFee(Double fee) {
		this.fee = fee;
	}
	
	
}
