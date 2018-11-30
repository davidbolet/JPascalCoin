package com.github.davidbolet.jpascalcoin.api.model;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * OpReceiver Object, created on version 3.0
 * This object represents an element of the "receivers" array for Operation/Multioperation objects
 * @author davidbolet
 *
 */
public class OpReceiver implements Serializable {
	
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
	 * Amount of coins transferred to this sender
	 * PASCURRENCY - In positive value, as we are receiving them
	*/
	@SerializedName("amount")
	@Expose
	protected Double amount;
   
  
	/** 
	 * HEXASTRING Operation payload for this receiver
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
		if (!(obj instanceof OpReceiver)) {
			return false;
		}
		OpReceiver other = (OpReceiver) obj;
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
