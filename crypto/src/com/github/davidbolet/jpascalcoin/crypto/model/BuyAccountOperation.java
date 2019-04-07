package com.github.davidbolet.jpascalcoin.crypto.model;

import java.io.Serializable;

import com.github.davidbolet.jpascalcoin.common.helper.HexConversionsHelper;
import com.github.davidbolet.jpascalcoin.crypto.model.PascOperation;
import com.github.davidbolet.jpascalcoin.common.model.PascPublicKey;
import com.github.davidbolet.jpascalcoin.common.model.PayLoadEncryptionMethod;

public class BuyAccountOperation extends PascOperation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	final Integer buyerAccount;
	final Integer accountToPurchase;
	final Integer sellerAccount;
	final Integer nOperation;
	final Double price;
	final Double fee;
	final PascPublicKey accountBuyerPublicKey;
	
	public BuyAccountOperation(Integer buyerAccount,PascPublicKey newB58PubKey, Integer accountToPurchase,Integer sellerAccount, PascPublicKey sellerPubKey, Integer nOperation, Double price, Double fee, byte[] payload, PayLoadEncryptionMethod payloadMethod, String pwd ) {
		
		super(payload,payloadMethod, pwd,payloadMethod==PayLoadEncryptionMethod.SENDER?newB58PubKey:payloadMethod==PayLoadEncryptionMethod.DEST?sellerPubKey:null);
		this.buyerAccount=buyerAccount;
		this.accountToPurchase=accountToPurchase;
		this.sellerAccount=sellerAccount;
		this.nOperation=nOperation;
		this.price=price;
		this.fee=fee;
		this.accountBuyerPublicKey=newB58PubKey;
	}


	@Override
	public byte[] generateOpDigest(float protocolVersion) {
		StringBuffer digestBuffer = new StringBuffer();
		digestBuffer.append(HexConversionsHelper.int2BigEndianHex4Byte(buyerAccount))
		.append(HexConversionsHelper.int2BigEndianHex4Byte(nOperation))
		.append(HexConversionsHelper.int2BigEndianHex4Byte(accountToPurchase))
		.append(HexConversionsHelper.int2BigEndianHex8Byte(Math.round(price*10000)))  //amount == price
		.append(HexConversionsHelper.int2BigEndianHex8Byte(Math.round(fee*10000)))
		.append(HexConversionsHelper.byteToHex(getPayload()).toUpperCase())
		.append(HexConversionsHelper.int2BigEndianHex(0))
		.append(HexConversionsHelper.int2BigEndianHex8Byte(Math.round(price*10000))) 
		.append(HexConversionsHelper.int2BigEndianHex4Byte(sellerAccount));
		if (accountBuyerPublicKey!=null) {
			digestBuffer.append(HexConversionsHelper.int2BigEndianHex(accountBuyerPublicKey.getKeyType().getValue()))
			.append(accountBuyerPublicKey.getX())
			.append(accountBuyerPublicKey.getY());
		}
		else
		{
			digestBuffer.append(HexConversionsHelper.int2BigEndianHex(0));
			digestBuffer.append(HexConversionsHelper.int2BigEndianHex(0));
			digestBuffer.append(HexConversionsHelper.int2BigEndianHex(0));
		}
		if (protocolVersion>=4.0f)
		{
			digestBuffer.append("06");
		}
		finalHash=digestBuffer.toString();
		return HexConversionsHelper.decodeStr2Hex(finalHash);
	}
	
	@Override
	public String getRawOperations(String signR, String signS) {
		StringBuffer digestBuffer = new StringBuffer();
		//Operations count (1 for this example) stored as a little endian in 4 bytes
		digestBuffer.append(HexConversionsHelper.int2BigEndianHex4Byte(1))
		.append(HexConversionsHelper.int2BigEndianHex4Byte(6)) //Optype
		.append(HexConversionsHelper.int2BigEndianHex4Byte(buyerAccount))
		.append(HexConversionsHelper.int2BigEndianHex4Byte(nOperation))
		.append(HexConversionsHelper.int2BigEndianHex4Byte(accountToPurchase))
		.append(HexConversionsHelper.int2BigEndianHex8Byte(Math.round(price*10000)))
		.append(HexConversionsHelper.int2BigEndianHex8Byte(Math.round(fee*10000)))
		.append(HexConversionsHelper.int2BigEndianHex(getPayload().length))
		.append(HexConversionsHelper.byteToHex(getPayload()).toUpperCase())
		.append(HexConversionsHelper.int2BigEndianHex(0))
		.append(HexConversionsHelper.int2BigEndianHex(0))
		.append(HexConversionsHelper.int2BigEndianHex(0))
		.append( "02") //Transaction Buy Account
		.append(HexConversionsHelper.int2BigEndianHex8Byte(Math.round(price*10000)))
		.append(HexConversionsHelper.int2BigEndianHex4Byte(sellerAccount));
		if (accountBuyerPublicKey!=null) {
			digestBuffer.append(accountBuyerPublicKey.getEncPubKey());
		}
		else
		{
			digestBuffer.append(HexConversionsHelper.int2BigEndianHex(0));
			digestBuffer.append(HexConversionsHelper.int2BigEndianHex(0));
			digestBuffer.append(HexConversionsHelper.int2BigEndianHex(0));
		}
		digestBuffer
		.append(HexConversionsHelper.int2BigEndianHex(signR.length()/2))
		.append(signR)
		.append(HexConversionsHelper.int2BigEndianHex(signS.length()/2))
		.append(signS);
		return digestBuffer.toString();
	}

	

}
