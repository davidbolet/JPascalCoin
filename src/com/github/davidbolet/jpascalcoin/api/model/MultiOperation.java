package com.github.davidbolet.jpascalcoin.api.model;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MultiOperation  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@SerializedName("rawoperations")
	@Expose
	protected String rawOperations;
	
	/**
	 * ARRAY of objects - When is a transaction, this array contains each sender 
	 */
	@SerializedName("senders")
	@Expose
	List<OpSender> senders;
	
	/**
	 * ARRAY of objects - When is a transaction, this array contains each receiver 
	 */
	@SerializedName("receivers")
	@Expose
	List<OpReceiver> receivers;
	
	/**
	 * "changers" : ARRAY of objects - When accounts changed state 
	 */
	@SerializedName("changers")
	@Expose
	List<OpChanger> changers;

	/** 
	 * Amount of coins received by receivers
	 * PASCURRENCY 
	*/
	@SerializedName("amount")
	@Expose
	protected Double amount;
	
	/** 
	 * Fee of this operation, Equal to "total send" - "total received"
	*/
	@SerializedName("fee")
	@Expose
	protected Double fee;
	
	/** 
	 * Integer with info about how many accounts are signed. Does not check if signature is valid 
	 * for a multioperation not included in blockchain
	*/
	@SerializedName("signed_count")
	@Expose
	protected Integer signedCount;
	
	/** 
	 * Integer with info about how many accounts are pending to be signed
	*/
	@SerializedName("not_signed_count")
	@Expose
	protected Integer notSignedCount;
	
	/**
	 * "signed_can_execute" : Boolean. True if everybody signed. Does not check if MultiOperation is well formed 
	 * or can be added to Network because is an offline call
	 */
	@SerializedName("signed_can_execute")
	@Expose
	protected Boolean signedCanExecute;
	
	/**
	 * "senders_count" : Integer. Number of senders
	 */
	@SerializedName("senders_count")
	@Expose
	protected Integer sendersCount;

	/**
	 * "senders_count" : Integer. Number of receivers
	 */
	@SerializedName("receivers_count")
	@Expose
	protected Integer receiversCount;
	
	/**
	 * "senders_count" : Integer. Number of changers
	 */
	@SerializedName("changesinfo_count")
	@Expose
	protected Integer changesCount;

	public String getRawOperations() {
		return rawOperations;
	}

	public void setRawOperations(String rawOperations) {
		this.rawOperations = rawOperations;
	}

	public List<OpSender> getSenders() {
		return senders;
	}

	public void setSenders(List<OpSender> senders) {
		this.senders = senders;
	}

	public List<OpReceiver> getReceivers() {
		return receivers;
	}

	public void setReceivers(List<OpReceiver> receivers) {
		this.receivers = receivers;
	}

	public List<OpChanger> getChangers() {
		return changers;
	}

	public void setChangers(List<OpChanger> changers) {
		this.changers = changers;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getFee() {
		return fee;
	}

	public void setFee(Double fee) {
		this.fee = fee;
	}

	public Integer getSignedCount() {
		return signedCount;
	}

	public void setSignedCount(Integer signedCount) {
		this.signedCount = signedCount;
	}

	public Integer getNotSignedCount() {
		return notSignedCount;
	}

	public void setNotSignedCount(Integer notSignedCount) {
		this.notSignedCount = notSignedCount;
	}

	public Boolean getSignedCanExecute() {
		return signedCanExecute;
	}

	public void setSignedCanExecute(Boolean signedCanExecute) {
		this.signedCanExecute = signedCanExecute;
	}
	
	
	
}
