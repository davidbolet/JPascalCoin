
package org.jpascalcoin.api.model;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Account implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SerializedName("account")
    @Expose
    private Integer account;
    
    @SerializedName("type")
    @Expose
    private Integer type;
    
    @SerializedName("name")
    @Expose
    private String name;
    
    @SerializedName("state")
    @Expose
    private AccountState state;  
    
    @SerializedName("private_sale")
    @Expose
    private Boolean privateSale;     
    
    @SerializedName("new_enc_pubkey")
    @Expose
    private String newEncPubkey;
    
    @SerializedName("locked_until_block")
    @Expose
    private Integer lockedUntilBlock;    
    
    @SerializedName("enc_pubkey")
    @Expose
    private String encPubkey;    
    
    @SerializedName("balance")
    @Expose
    private Double balance;
    
    @SerializedName("n_operation")
    @Expose
    private Integer nOperation;
    
    @SerializedName("updated_b")
    @Expose
    private Integer updatedB;

    /**
     * Account number
     * @return Integer 
     */
	public Integer getAccount() {
		return account;
	}

	public void setAccount(Integer account) {
		this.account = account;
	}

	/**
	 * Account Type
	 * @return Integer
	 */
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	/**
	 * Account name in PascalCoin64 Encoding - abcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-+{}[]_:"|&lt;>,.?/~
	 * First char cannot start with number
	 * Must empty/null or 3..64 characters in length
	 * @return String
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Account State
	 * @return AccountState
	 */
	public AccountState getState() {
		return state;
	}

	public void setState(AccountState state) {
		this.state = state;
	}

	/**
	 * For Listed accounts, this indicates whether it's private or public sale
	 * @return Boolean
	 */
	public Boolean getPrivateSale() {
		return privateSale;
	}

	public void setPrivateSale(Boolean privateSale) {
		this.privateSale = privateSale;
	}

	/**
	 * For Listed accounts for PrivateSale, this indicates the buyers public key
	 * @return String
	 */
	public String getNewEncPubkey() {
		return newEncPubkey;
	}

	public void setNewEncPubkey(String newEncPubkey) {
		this.newEncPubkey = newEncPubkey;
	}

	/**
	 * 
	 * @return Integer
	 */
	public Integer getLockedUntilBlock() {
		return lockedUntilBlock;
	}

	public void setLockedUntilBlock(Integer lockedUntilBlock) {
		this.lockedUntilBlock = lockedUntilBlock;
	}

	/**
	 * Encoded public key value (hexastring)
	 * @return @String
	 */
	public String getEncPubkey() {
		return encPubkey;
	}

	public void setEncPubkey(String encPubkey) {
		this.encPubkey = encPubkey;
	}

	/**
	 * Account balance (PASCURRENCY)
	 * @return
	 */
	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	/**
	 * Operations made by this account (Note: When an account receives a transaction, n_operation is not changed)
	 * @return Integer
	 */
	public Integer getnOperation() {
		return nOperation;
	}

	public void setnOperation(Integer nOperation) {
		this.nOperation = nOperation;
	}

	/**
	 * Last block that updated this account. If equal to blockchain blocks count it means that it has pending 
	 * operations to be included to the blockchain
	 * @return Integer
	 */
	public Integer getUpdatedB() {
		return updatedB;
	}

	public void setUpdatedB(Integer updatedB) {
		this.updatedB = updatedB;
	}
    


}
