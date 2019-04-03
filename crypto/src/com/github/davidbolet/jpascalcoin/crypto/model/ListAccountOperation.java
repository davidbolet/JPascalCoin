package com.github.davidbolet.jpascalcoin.crypto.model;

import java.io.Serializable;

import com.github.davidbolet.jpascalcoin.common.helper.HexConversionsHelper;
import com.github.davidbolet.jpascalcoin.crypto.model.PascOperation;
import com.github.davidbolet.jpascalcoin.common.model.PascPublicKey;
import com.github.davidbolet.jpascalcoin.common.model.PayLoadEncryptionMethod;

public class ListAccountOperation extends PascOperation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	final Integer accountSigner;
	final Integer accountToSell;
	final Integer accountPayed;
	final Integer nOperation;
	final Integer lockedUntilBlock;
	final Double price;
	final Double fee;
	final PascPublicKey signerPublicKey;
	final PascPublicKey accountBuyerPublicKey;
	
	public ListAccountOperation(Integer accountSigner,PascPublicKey signerPublicKey, Integer accountToSell,Integer accountPayed, PascPublicKey accountBuyerPublicKey, Integer nOperation, Double price, Double fee, Integer lockedUntilBlock, byte[] payload, PayLoadEncryptionMethod payloadMethod, String pwd ) {
		super(payload,payloadMethod, pwd,payloadMethod==PayLoadEncryptionMethod.SENDER?signerPublicKey:payloadMethod==PayLoadEncryptionMethod.DEST?accountBuyerPublicKey:null);
		this.accountSigner=accountSigner;
		this.accountToSell=accountToSell;
		this.accountPayed=accountPayed;
		this.nOperation=nOperation;
		this.price=price;
		this.fee=fee;
		this.lockedUntilBlock=lockedUntilBlock==null?0:lockedUntilBlock;
		this.accountBuyerPublicKey=accountBuyerPublicKey;
		this.signerPublicKey=signerPublicKey;
	}


	@Override
	public byte[] generateOpDigest(float protocolVersion) {
		StringBuffer digestBuffer = new StringBuffer();
		digestBuffer.append(HexConversionsHelper.int2BigEndianHex4Byte(accountSigner))
		.append(HexConversionsHelper.int2BigEndianHex4Byte(accountToSell))
		.append(HexConversionsHelper.int2BigEndianHex4Byte(nOperation))
		.append(HexConversionsHelper.int2BigEndianHex8Byte(Math.round(price*10000)))
		.append(HexConversionsHelper.int2BigEndianHex4Byte(accountPayed))
		.append(HexConversionsHelper.int2BigEndianHex8Byte(Math.round(fee*10000)))
		.append(HexConversionsHelper.byteToHex(getPayload()).toUpperCase())
		.append(HexConversionsHelper.int2BigEndianHex(0));
		if (accountBuyerPublicKey!=null) {
			digestBuffer.append(accountBuyerPublicKey.getEncPubKey());
		}
		else
		{
			digestBuffer.append(HexConversionsHelper.int2BigEndianHex(0));
			digestBuffer.append(HexConversionsHelper.int2BigEndianHex(0));
			digestBuffer.append(HexConversionsHelper.int2BigEndianHex(0));
		}
		digestBuffer.append(HexConversionsHelper.int2BigEndianHex4Byte(lockedUntilBlock));
		if (protocolVersion>=4.0f)
		{
			digestBuffer.append("04");
		}
		finalHash=digestBuffer.toString();
		return HexConversionsHelper.decodeStr2Hex(finalHash);
	}
	
	
	@Override
	public String getRawOperations(String signR, String signS) {
		StringBuffer digestBuffer = new StringBuffer();
		//Operations count (1 for this example) stored as a little endian in 4 bytes
		digestBuffer.append(HexConversionsHelper.int2BigEndianHex4Byte(1))
		.append(HexConversionsHelper.int2BigEndianHex4Byte(4)) //Optype
		.append(HexConversionsHelper.int2BigEndianHex4Byte(accountSigner))
		.append(HexConversionsHelper.int2BigEndianHex4Byte(accountToSell))
		.append(HexConversionsHelper.int2BigEndianHex(4)) //Optype
		.append(HexConversionsHelper.int2BigEndianHex4Byte(nOperation))
		.append(HexConversionsHelper.int2BigEndianHex8Byte(Math.round(price*10000)))
		.append(HexConversionsHelper.int2BigEndianHex4Byte(accountPayed))
		.append(HexConversionsHelper.int2BigEndianHex(0));
		if (accountBuyerPublicKey!=null) {
			digestBuffer.append(accountBuyerPublicKey.getEncPubKey());
		}
		else
		{
			digestBuffer.append(HexConversionsHelper.int2BigEndianHex(0));
			digestBuffer.append(HexConversionsHelper.int2BigEndianHex(0));
			digestBuffer.append(HexConversionsHelper.int2BigEndianHex(0));
		}
		digestBuffer.append(HexConversionsHelper.int2BigEndianHex4Byte(lockedUntilBlock))		
		.append(HexConversionsHelper.int2BigEndianHex8Byte(Math.round(fee*10000)))
		.append(HexConversionsHelper.int2BigEndianHex(getPayload().length))
		.append(HexConversionsHelper.byteToHex(getPayload()).toUpperCase())
		.append(HexConversionsHelper.int2BigEndianHex(signR.length()/2))
		.append(signR)
		.append(HexConversionsHelper.int2BigEndianHex(signS.length()/2))
		.append(signS);
		return digestBuffer.toString();
	}

}
