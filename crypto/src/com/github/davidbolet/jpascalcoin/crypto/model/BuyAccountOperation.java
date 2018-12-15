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
	/*
	 * DAEE010041000000AA7D0700E8030000000000000100000000000000544553540000E80300000000000032450300CA0273727AF9C68184749B063FE4D558F643FC58601FA5980C1F77C6846EFD4FEA92AE18F37219D7BB654EB50C8048F62BFFDEB47A03F33AAF5AF876C507F0B387A106 <- bo
	 * DAEE010042000000A97D0700E8030000000000000100000000000000544553540000E80300000000000032450300CA02200073727AF9C68184749B063FE4D558F643FC58601FA5980C1F77C6846EFD4FEA922000AE18F37219D7BB654EB50C8048F62BFFDEB47A03F33AAF5AF876C507F0B387A106 <-meu
	 * 
	ms.Write(FData.sender,Sizeof(FData.sender));
    ms.Write(FData.n_operation,Sizeof(FData.n_operation));
    ms.Write(FData.target,Sizeof(FData.target));
    ms.Write(FData.amount,Sizeof(FData.amount));
    ms.Write(FData.fee,Sizeof(FData.fee));
    if length(FData.payload)>0 then
      ms.WriteBuffer(FData.payload[1],length(FData.payload));
    ms.Write(FData.public_key.EC_OpenSSL_NID,Sizeof(FData.public_key.EC_OpenSSL_NID));
    if length(FData.public_key.x)>0 then
      ms.WriteBuffer(FData.public_key.x[1],length(FData.public_key.x));
    if length(FData.public_key.y)>0 then
      ms.WriteBuffer(FData.public_key.y[1],length(FData.public_key.y));
    if FData.opTransactionStyle=buy_account then begin
      ms.Write(FData.AccountPrice,Sizeof(FData.AccountPrice));
      ms.Write(FData.SellerAccount,Sizeof(FData.SellerAccount));
      ms.Write(FData.new_accountkey.EC_OpenSSL_NID,Sizeof(FData.new_accountkey.EC_OpenSSL_NID));
      if length(FData.new_accountkey.x)>0 then
        ms.WriteBuffer(FData.new_accountkey.x[1],length(FData.new_accountkey.x));
      if length(FData.new_accountkey.y)>0 then
        ms.WriteBuffer(FData.new_accountkey.y[1],length(FData.new_accountkey.y));
    end;
    if (current_protocol<=CT_PROTOCOL_3) then begin
      ms.Position := 0;
      SetLength(Result,ms.Size);
      ms.ReadBuffer(Result[1],ms.Size);
    end else begin
      b := OpType;
      ms.Write(b,1);
      r:=TStreamOp.SaveStreamToRaw(ms);
      TLog.NewLog(lterror,ClassName,Format('TOpTransaction.GetDigestToSign:%s',[TCrypto.ToHexaString(r)]));
      Result := TCrypto.DoSha256(ms.Memory,ms.Size);
    end;
  finally
    ms.Free;         
	 */
	
	
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

	/*
	 * 
	 * TOpTransaction.SaveOpToStream:DAEE010041000000AA7D0700E8030000000000000100000000000000000000000000000002E80300000000000032450300CA02200073727AF9C68184749B063FE4D558F643FC58601FA5980C1F77C6846EFD4FEA922000AE18F37219D7BB654EB50C8048F62BFFDEB47A03F33AAF5AF876C507F0B387A1200046CFD1DC77D9ECCB8CE34FB7ACAFB039FF2D32739BF13B27136A70031211644020002AFF83E8A19D5A7459F3B9D60E63AB956D92136784E71C0631C77E2E347D215B
06-12-2018 23:50:33.003 TID:7F8980EAF700 [Info] <TThreadDiscoverConnection> Starting discovery of connection 113.117.125.70:4004

	 *  Stream.Write(FData.sender,Sizeof(FData.sender));
  Stream.Write(FData.n_operation,Sizeof(FData.n_operation));
  Stream.Write(FData.target,Sizeof(FData.target));
  Stream.Write(FData.amount,Sizeof(FData.amount));
  Stream.Write(FData.fee,Sizeof(FData.fee));
  TStreamOp.WriteAnsiString(Stream,FData.payload);
  Stream.Write(FData.public_key.EC_OpenSSL_NID,Sizeof(FData.public_key.EC_OpenSSL_NID));
  TStreamOp.WriteAnsiString(Stream,FData.public_key.x);
  TStreamOp.WriteAnsiString(Stream,FData.public_key.y);
  if ((SaveExtendedData) Or (Self is TOpBuyAccount)) then begin
    case FData.opTransactionStyle of
      transaction : b:=0;
      transaction_with_auto_buy_account : b:=1;
      buy_account : b:=2;
    else raise Exception.Create('ERROR DEV 20170424-1');
    end;
    Stream.Write(b,1);
    if (FData.opTransactionStyle in [transaction_with_auto_buy_account,buy_account]) then begin
      Stream.Write(FData.AccountPrice,SizeOf(FData.AccountPrice));
      Stream.Write(FData.SellerAccount,SizeOf(FData.SellerAccount));
      Stream.Write(FData.new_accountkey.EC_OpenSSL_NID,Sizeof(FData.new_accountkey.EC_OpenSSL_NID));
      TStreamOp.WriteAnsiString(Stream,FData.new_accountkey.x);
      TStreamOp.WriteAnsiString(Stream,FData.new_accountkey.y);
    end;
  end;
  TStreamOp.WriteAnsiString(Stream,FData.sign.r);
  TStreamOp.WriteAnsiString(Stream,FData.sign.s);
  r:=TStreamOp.SaveStreamToRaw(Stream);
  TLog.NewLog(lterror,ClassName,Format('TOpTransactio
	 *  
	 *  
 	 */

}
