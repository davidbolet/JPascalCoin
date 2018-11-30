package com.github.davidbolet.jpascalcoin.api.model;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RawOperation implements Serializable { 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Count how many operations has rawoperations param
	 */
	@SerializedName("operations")
	@Expose
	protected Integer numOperations;

	/**
	 * Total amount
	 */
	@SerializedName("amount")
	@Expose
	protected Double totalAmount;

	/**
	 * Total fee
	 */
	@SerializedName("fee")
	@Expose
	protected Double totalFee;

	/**
	 * This is the operations in raw format.
	 */
	@SerializedName("rawoperations")
	@Expose
	protected String rawOperations;

	public Integer getNumOperations() {
		return numOperations;
	}

	public void setNumOperations(Integer numOperations) {
		this.numOperations = numOperations;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Double getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(Double totalFee) {
		this.totalFee = totalFee;
	}

	public String getRawOperations() {
		return rawOperations;
	}

	public void setRawOperations(String rawOperations) {
		this.rawOperations = rawOperations;
	}
}
