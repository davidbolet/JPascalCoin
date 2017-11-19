package com.github.davidbolet.jpascalcoin.api.model;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Block implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Block number
	 */  
	@SerializedName("block")
	@Expose
	protected Integer block;

	/**
	 * Last block that updated this account. If equal to blockchain blocks count it means that it has pending operations to be included to the blockchain
	 */  
	@SerializedName("enc_pubkey")
	@Expose
	protected String encPubKey;

	/**
	 * Reward of first account's block
	 */  
	@SerializedName("reward")
	@Expose
	protected Double reward;

	/**
	 * Fee obtained by operations (PASCURRENCY)
	 */  
	@SerializedName("fee")
	@Expose
	protected Double fee;

	/**
	 * Pascal Coin protocol used
	 */  
	@SerializedName("ver")
	@Expose
	protected Integer version;

	/**
	 * Pascal Coin protocol available by the miner
	 */  
	@SerializedName("ver_a")
	@Expose
	protected Integer availableVersion;

	/**
	 * Unix timestamp
	 */  
	@SerializedName("timestamp")
	@Expose
	protected Long timestamp;

	/**
	 * Target used
	 */  
	@SerializedName("target")
	@Expose
	protected Integer compactTarget;

	/**
	 * Nonce used
	 */  
	@SerializedName("nonce")
	@Expose
	protected Long nonce;

	/**
	 * Miner's payload
	 */  
	@SerializedName("payload")
	@Expose
	protected String payload;

	/**
	 * SafeBox Hash
	 */  
	@SerializedName("sbh")
	@Expose
	protected String safeBoxHash;

	/**
	 * Operations hash
	 */  
	@SerializedName("oph")
	@Expose
	protected String operationsHash;

	/**
	 * Proof of work
	 */  
	@SerializedName("pow")
	@Expose
	protected String proofOfWork;

	/**
	 * Number of operations included in this block
	 */  
	@SerializedName("operations")
	@Expose
	protected Integer operationCount;

	/**
	 * Estimated network hashrate calculated by previous 50 blocks average
	 */  
	@SerializedName("hashratekhs")
	@Expose
	protected Long last50HashRateKhs;

	/**
	 * Number of blocks in the blockchain higher than this
	 */  
	@SerializedName("maturation")
	@Expose
	protected Integer maturation;

	public Integer getBlock() {
		return block;
	}

	public void setBlock(Integer block) {
		this.block = block;
	}

	public String getEncPubKey() {
		return encPubKey;
	}

	public void setEncPubKey(String encPubKey) {
		this.encPubKey = encPubKey;
	}

	public Double getReward() {
		return reward;
	}

	public void setReward(Double reward) {
		this.reward = reward;
	}

	public Double getFee() {
		return fee;
	}

	public void setFee(Double fee) {
		this.fee = fee;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Integer getAvailableVersion() {
		return availableVersion;
	}

	public void setAvailableVersion(Integer availableVersion) {
		this.availableVersion = availableVersion;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public Integer getCompactTarget() {
		return compactTarget;
	}

	public void setCompactTarget(Integer compactTarget) {
		this.compactTarget = compactTarget;
	}

	public Long getNonce() {
		return nonce;
	}

	public void setNonce(Long nonce) {
		this.nonce = nonce;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	public String getSafeBoxHash() {
		return safeBoxHash;
	}

	public void setSafeBoxHash(String safeBoxHash) {
		this.safeBoxHash = safeBoxHash;
	}

	public String getOperationsHash() {
		return operationsHash;
	}

	public void setOperationsHash(String operationsHash) {
		this.operationsHash = operationsHash;
	}

	public String getProofOfWork() {
		return proofOfWork;
	}

	public void setProofOfWork(String proofOfWork) {
		this.proofOfWork = proofOfWork;
	}

	public Integer getOperationCount() {
		return operationCount;
	}

	public void setOperationCount(Integer operationCount) {
		this.operationCount = operationCount;
	}

	public Long getLast50HashRateKhs() {
		return last50HashRateKhs;
	}

	public void setLast50HashRateKhs(Long last50HashRateKhs) {
		this.last50HashRateKhs = last50HashRateKhs;
	}

	public Integer getMaturation() {
		return maturation;
	}

	public void setMaturation(Integer maturation) {
		this.maturation = maturation;
	}
	
}
