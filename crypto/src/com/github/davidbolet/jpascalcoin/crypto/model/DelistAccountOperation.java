package com.github.davidbolet.jpascalcoin.crypto.model;

import java.io.Serializable;

import com.github.davidbolet.jpascalcoin.common.helper.HexConversionsHelper;
import com.github.davidbolet.jpascalcoin.crypto.model.PascOperation;
import com.github.davidbolet.jpascalcoin.common.model.PascPublicKey;
import com.github.davidbolet.jpascalcoin.common.model.PayLoadEncryptionMethod;

public class DelistAccountOperation extends PascOperation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	final Integer accountSigner;
	final Integer accountToDelist;
	final Integer nOperation;

	final Double fee;
	
	public DelistAccountOperation(Integer accountToDelist, Integer accountSigner, PascPublicKey signerPublicKey, Integer nOperation, Double fee, byte[] payload, PayLoadEncryptionMethod payloadMethod, String pwd ) {
		super(payload,payloadMethod, pwd,payloadMethod==PayLoadEncryptionMethod.SENDER?signerPublicKey:payloadMethod==PayLoadEncryptionMethod.DEST?signerPublicKey:null);
		this.accountToDelist=accountToDelist;
		this.accountSigner=accountSigner;
		this.nOperation=nOperation;
		this.fee=fee;
		}

	@Override
	public byte[] generateOpDigest(float protocolVersion) {
		StringBuffer digestBuffer = new StringBuffer();
		digestBuffer.append(HexConversionsHelper.int2BigEndianHex4Byte(accountSigner))
		.append(HexConversionsHelper.int2BigEndianHex4Byte(accountToDelist))
		.append(HexConversionsHelper.int2BigEndianHex4Byte(nOperation))
		.append(HexConversionsHelper.int2BigEndianHex4Byte(0))
		.append(HexConversionsHelper.int2BigEndianHex4Byte(0))
		.append(HexConversionsHelper.int2BigEndianHex4Byte(0))
		.append(HexConversionsHelper.int2BigEndianHex8Byte(Math.round(fee*10000)))
		.append(HexConversionsHelper.byteToHex(this.getPayload()).toUpperCase())
		.append(HexConversionsHelper.int2BigEndianHex4Byte(0))
		.append(HexConversionsHelper.int2BigEndianHex4Byte(0))
		.append(HexConversionsHelper.int2BigEndianHex4Byte(0));
		if (protocolVersion>=4.0f)
		{
			digestBuffer.append("05");
		}
		finalHash=digestBuffer.toString();
		return HexConversionsHelper.decodeStr2Hex(finalHash);
	}
	
	@Override
	public String getRawOperations(String signR, String signS) {
		StringBuffer digestBuffer = new StringBuffer();
		//Operations count (1 for this example) stored as a little endian in 4 bytes
		digestBuffer.append(HexConversionsHelper.int2BigEndianHex4Byte(1))
		.append(HexConversionsHelper.int2BigEndianHex4Byte(5)) //Optype
		.append(HexConversionsHelper.int2BigEndianHex4Byte(accountSigner))
		.append(HexConversionsHelper.int2BigEndianHex4Byte(accountToDelist))
		.append(HexConversionsHelper.int2BigEndianHex(5)) //Optype
		.append(HexConversionsHelper.int2BigEndianHex4Byte(nOperation))
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
