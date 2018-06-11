package com.github.davidbolet.jpascalcoin.api.model;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Operation Object, modified on version 3.0
 * @author davidbolet
 *
 */
public class Operation implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
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
	 * n_operation param, added in version 2.1.6
	 * n_operation is an incremental value to protect double spend
	 */
	@SerializedName("n_operation")
	@Expose
	protected Integer nOperation;

	/** 
	 * Operation hash used to find this operation in the blockchain. (HEXASTRING).
	*/
	@SerializedName("ophash")
	@Expose
	protected String opHash;

	/** 
	 * If operation is invalid, value=false  (optional)
	*/
	@SerializedName("valid")
	@Expose
	protected Boolean valid;

	/** 
	 * If operation is invalid, an error description (optional)
	*/
	@SerializedName("errors")
	@Expose
	protected String errors;

	/** 
	 * Block number (only when valid) 
	*/
	@SerializedName("block")
	@Expose
	protected Integer block;

	/** 
	 * Block timestamp (only when valid) 
	*/
	@SerializedName("time")
	@Expose
	protected Long time;

	/** 
	 * Operation index inside a block(0..operations-1). Note: If opblock = -1 means that is a blockchain reward (only when valid)
	*/
	@SerializedName("opblock")
	@Expose
	protected Integer operationBlock ;

	/** 
	 * Return null when operation is not included on a blockchain yet, 0 means that is included in highest block and so on... (New on Build 1.4.3)
	*/
	@SerializedName("maturation")
	@Expose
	protected Integer maturation;

	/** 
	 * Operation type.
	*/
	@SerializedName("optype")
	@Expose
	protected OperationType type;


	/** 
	 * Operation sub-type.
	*/
	@SerializedName("subtype")
	@Expose
	protected OperationSubType subType;

	/** 
	 * Account affected by this operation.Note: A transaction has 2 affected accounts.
	*/
	@SerializedName("account")
	@Expose
	protected int account;

	/** 
	 * Account affected by this operation.Note: A transaction has 2 affected accounts.
	*/
	@SerializedName("optxt")
	@Expose
	protected String typeDescriptor;

	/** 
	 * Amount of coins transferred from sender_account to dest_account (Only apply when optype = 1) (PASCURRENCY)
	*/
	@SerializedName("amount")
	@Expose
	protected Double amount;

	/** 
	 * Fee of this operation (PASCURRENCY)
	*/
	@SerializedName("fee")
	@Expose
	protected Double fee;

	/** 
	 * Balance of account after this block is introduced in the Blockchain (PASCURRENCY).
	 * balance is a calculation based on current safebox account balance and previous operations, it's only returned on pending operations and account operations
	 */
	@SerializedName("balance")
	@Expose
	protected Double balance;

	/** 
	 * Sender account in a single transaction (optype = 1)
	*/
	@SerializedName("sender_account")
	@Expose
	protected Integer senderAccount;

	/** 
	 * Destination account in a transaction (optype = 1)
	*/
	@SerializedName("dest_account")
	@Expose
	protected Integer destAccount;

	/** 
	 * Account that signed operation, and pays network fee.
	*/
	@SerializedName("signer_account")
	@Expose
	protected Integer signerAccount;

	/** 
	 * Encoded protected key used in either a change key operation (optype = 2) or a list account for sale (private sale) operation. (HEXASTRING).
	 * See decodepubkey method to deconstruct datatype.
	 */
	@SerializedName("enc_pubkey")
	@Expose
	protected String encPubKey;

	/** 
	 * Operation payload in hex format
	*/
	@SerializedName("payload")
	@Expose
	protected String payLoad;

	/** 
	 * Operation hash calculated using V1 algorithm. Only provided for operations before V2 activation. (HEXASTRING).
	*/
	@SerializedName("old_ophash")
	@Expose
	protected String v1Ophash;

	public Integer getNOperation() {
		return this.nOperation;
	}
	
	public void setNOperation(Integer nOperation) {
		this.nOperation=nOperation;
	}
	
	public String getOpHash() {
		return opHash;
	}

	public void setOpHash(String opHash) {
		this.opHash = opHash;
	}

	public Boolean getValid() {
		return valid;
	}

	public void setValid(Boolean valid) {
		this.valid = valid;
	}

	public String getErrors() {
		return errors;
	}

	public void setErrors(String errors) {
		this.errors = errors;
	}

	public Integer getBlock() {
		return block;
	}

	public void setBlock(Integer block) {
		this.block = block;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public Integer getOperationBlock() {
		return operationBlock;
	}

	public void setOperationBlock(Integer operationBlock) {
		this.operationBlock = operationBlock;
	}

	public Integer getMaturation() {
		return maturation;
	}

	public void setMaturation(Integer maturation) {
		this.maturation = maturation;
	}

	public OperationType getType() {
		return type;
	}

	public void setType(OperationType type) {
		this.type = type;
	}

	public OperationSubType getSubType() {
		return subType;
	}

	public void setSubType(OperationSubType subType) {
		this.subType = subType;
	}

	public int getAccount() {
		return account;
	}

	public void setAccount(int account) {
		this.account = account;
	}

	public String getTypeDescriptor() {
		return typeDescriptor;
	}

	public void setTypeDescriptor(String typeDescriptor) {
		this.typeDescriptor = typeDescriptor;
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

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public Integer getSenderAccount() {
		return senderAccount;
	}

	public void setSenderAccount(Integer senderAccount) {
		this.senderAccount = senderAccount;
	}

	public Integer getDestAccount() {
		return destAccount;
	}

	public void setDestAccount(Integer destAccount) {
		this.destAccount = destAccount;
	}

	public Integer getSignerAccount() {
		return signerAccount;
	}

	public void setSignerAccount(Integer signerAccount) {
		this.signerAccount = signerAccount;
	}

	public String getEncPubKey() {
		return encPubKey;
	}

	public void setEncPubKey(String encPubKey) {
		this.encPubKey = encPubKey;
	}

	public String getPayLoad() {
		return payLoad;
	}

	public void setPayLoad(String payLoad) {
		this.payLoad = payLoad;
	}

	public String getV1Ophash() {
		return v1Ophash;
	}

	public void setV1Ophash(String v1Ophash) {
		this.v1Ophash = v1Ophash;
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

	public Integer getnOperation() {
		return nOperation;
	}

	public void setnOperation(Integer nOperation) {
		this.nOperation = nOperation;
	}
	
}
