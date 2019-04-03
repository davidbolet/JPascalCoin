package com.github.davidbolet.jpascalcoin.crypto.model;

import java.io.Serializable;

import com.github.davidbolet.jpascalcoin.common.helper.HexConversionsHelper;
import com.github.davidbolet.jpascalcoin.common.model.PascPublicKey;
import com.github.davidbolet.jpascalcoin.common.model.PayLoadEncryptionMethod;

public class ChangeAccountOperation extends PascOperation implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	final Integer accountSigner;
	final Integer accountToChange;
	final PascPublicKey signerPublicKey;
	final Integer nOperation;
	final Double fee;
	final Integer newAccountType;
	final String newAccountName;
	final PascPublicKey newPublicKey;
	
	public ChangeAccountOperation(Integer accountToChange,PascPublicKey signerPublicKey, Integer accountSigner,Integer nOperation,Double fee, String newName, PascPublicKey newPublicKey, Integer  newType, byte[] payload, PayLoadEncryptionMethod method, String aesPassword)
	{
		super(payload,method, aesPassword,method==PayLoadEncryptionMethod.SENDER?signerPublicKey:method==PayLoadEncryptionMethod.DEST?newPublicKey:null);
		this.accountSigner=accountSigner;
		this.accountToChange=accountToChange;
		this.signerPublicKey=signerPublicKey;
		this.nOperation=nOperation;
		this.fee=fee;
		this.newAccountName=newName;
		this.newPublicKey=newPublicKey;
		this.newAccountType=newType;
	}
	public Integer getAccountSigner() {
		return accountSigner;
	}

	public Integer getAccountToChange() {
		return accountToChange;
	}

	public Integer getnOperation() {
		return nOperation;
	}

	public Double getFee() {
		return fee;
	}

	public Integer getNewAccountType() {
		return newAccountType;
	}

	public String getNewAccountNameDigest() {
		StringBuffer result=new StringBuffer();
		if (this.newAccountName!=null && !"".equals(newAccountName)) {
			try {
				result.append(HexConversionsHelper.int2BigEndianHex(newAccountName.length()))
					.append(HexConversionsHelper.byteToHex(newAccountName.getBytes("ASCII")).toUpperCase());
			}
			catch(Exception ex) {
				return result.append(HexConversionsHelper.int2BigEndianHex(0)).toString();
			}
		}
		else {
			result.append(HexConversionsHelper.int2BigEndianHex(0));
		}
		return result.toString();
	}

	public String getNewAccountName() {
		return newAccountName;
	}
	
	public String getNewPublicKeyDigest() {
		StringBuffer result=new StringBuffer();
		if (newPublicKey!=null ) {
			result.append(newPublicKey.getEncPubKey());
		}
		else
		{
			result.append(HexConversionsHelper.int2BigEndianHex(0));
			result.append(HexConversionsHelper.int2BigEndianHex(0));
			result.append(HexConversionsHelper.int2BigEndianHex(0));
		}
		return result.toString();
	}
	
	
	@Override
	public byte[] generateOpDigest(float protocolVersion) {
		StringBuffer digestBuffer = new StringBuffer();
		digestBuffer.append(HexConversionsHelper.int2BigEndianHex4Byte(accountSigner))
		.append(HexConversionsHelper.int2BigEndianHex4Byte(accountToChange))
		.append(HexConversionsHelper.int2BigEndianHex4Byte(nOperation))
		.append(HexConversionsHelper.int2BigEndianHex8Byte(Math.round(fee*10000)))
		.append(HexConversionsHelper.int2BigEndianHex(getPayload().length))
		.append(HexConversionsHelper.byteToHex(getPayload()).toUpperCase());
		if (signerPublicKey!=null) {
			digestBuffer.append(signerPublicKey.getEncPubKey());
		}
		else
		{
			digestBuffer.append(HexConversionsHelper.int2BigEndianHex(0));
			digestBuffer.append(HexConversionsHelper.int2BigEndianHex(0));
			digestBuffer.append(HexConversionsHelper.int2BigEndianHex(0));
		}
		digestBuffer.append(HexConversionsHelper.byteToHex(getOpByte()));		
		if (newPublicKey!=null) {
			digestBuffer.append(newPublicKey.getEncPubKey());
		}
		else
		{
			digestBuffer.append(HexConversionsHelper.int2BigEndianHex(0));
			digestBuffer.append(HexConversionsHelper.int2BigEndianHex(0));
			digestBuffer.append(HexConversionsHelper.int2BigEndianHex(0));
		}
		digestBuffer.append(getNewAccountNameDigest());
		if (newAccountType!=null) {
			digestBuffer.append(HexConversionsHelper.int2BigEndianHex(newAccountType));
		}
		else
		{
			digestBuffer.append(HexConversionsHelper.int2BigEndianHex(0));
		}
		if (protocolVersion>=4.0f)
		{
			digestBuffer.append("08");
		}
		finalHash=digestBuffer.toString();
		return HexConversionsHelper.decodeStr2Hex(finalHash);
	}
	
	
	private byte getOpByte() {
		Integer b=0;
		if (newPublicKey!=null && !newPublicKey.equals(signerPublicKey)) {
			b= b|1;
		}
		if (newAccountName!=null && !"".equals(newAccountName)) {
			b=b|2;
		}
		if (newAccountType!=null) {
			b=b|4;
		}
		return b.byteValue();
	}
	

	@Override
	public String getRawOperations(String signR, String signS) {
		StringBuffer digestBuffer = new StringBuffer();
		//Operations count (1 for this example) stored as a little endian in 4 bytes
		digestBuffer.append(HexConversionsHelper.int2BigEndianHex4Byte(1))
		.append(HexConversionsHelper.int2BigEndianHex4Byte(8)) //Optype
		.append(HexConversionsHelper.int2BigEndianHex4Byte(accountSigner))
		.append(HexConversionsHelper.int2BigEndianHex4Byte(accountToChange))
		.append(HexConversionsHelper.int2BigEndianHex4Byte(nOperation))
		.append(HexConversionsHelper.int2BigEndianHex8Byte(Math.round(fee*10000)))
		.append(HexConversionsHelper.int2BigEndianHex(getPayload().length))
		.append(HexConversionsHelper.byteToHex(getPayload()).toUpperCase());
		if (signerPublicKey!=null) {
			digestBuffer.append(signerPublicKey.getEncPubKey());
		}
		else
		{
			digestBuffer.append(HexConversionsHelper.int2BigEndianHex(0));
			digestBuffer.append(HexConversionsHelper.int2BigEndianHex(0));
			digestBuffer.append(HexConversionsHelper.int2BigEndianHex(0));
		}
		digestBuffer.append(HexConversionsHelper.byteToHex(getOpByte()));		
		if (newPublicKey!=null) {
			digestBuffer.append(newPublicKey.getEncPubKey());
		}
		else
		{
			digestBuffer.append(HexConversionsHelper.int2BigEndianHex(0));
			digestBuffer.append(HexConversionsHelper.int2BigEndianHex(0));
			digestBuffer.append(HexConversionsHelper.int2BigEndianHex(0));
		}
		digestBuffer.append(getNewAccountNameDigest());
		if (newAccountType!=null) {
			digestBuffer.append(HexConversionsHelper.int2BigEndianHex(newAccountType));
		}
		else
		{
			digestBuffer.append(HexConversionsHelper.int2BigEndianHex(0));
		}
		digestBuffer.append(HexConversionsHelper.int2BigEndianHex(signR.length()/2))
		.append(signR)
		.append(HexConversionsHelper.int2BigEndianHex(signS.length()/2))
		.append(signS);
		return digestBuffer.toString();
	}
		

}
