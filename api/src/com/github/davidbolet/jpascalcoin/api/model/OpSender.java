package com.github.davidbolet.jpascalcoin.api.model;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * OpSender Object, created on version 3.0
 * This object represents an element of the "senders" array for Operation/Multioperation objects
 * @author davidbolet
 *
 */
public class OpSender implements Serializable {
	
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
	 * Amount of coins transferred from this sender
	 * PASCURRENCY - In negative value, due it's outgoing from "account"
	*/
	@SerializedName("amount")
	@Expose
	protected Double amount;
   
  
	/** 
	 * HEXASTRING Operation payload introduced by this sender
	*/
	@SerializedName("payload")
	@Expose
	protected String payLoad;


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


	public Double getAmount() {
		return amount;
	}


	public void setAmount(Double amount) {
		this.amount = amount;
	}


	public String getPayLoad() {
		return payLoad;
	}


	public void setPayLoad(String payLoad) {
		this.payLoad = payLoad;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((account == null) ? 0 : account.hashCode());
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
		if (!(obj instanceof OpSender)) {
			return false;
		}
		OpSender other = (OpSender) obj;
		if (account == null) {
			if (other.account != null) {
				return false;
			}
		} else if (!account.equals(other.account)) {
			return false;
		}
		return true;
	}

}
