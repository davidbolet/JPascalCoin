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
//		.append(HexConversionsHelper.int2BigEndianHex(0))
//		.append(HexConversionsHelper.int2BigEndianHex(0))
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

	/* 
  Stream.Write(FData.account_signer,Sizeof(FData.account_signer));
  Stream.Write(FData.account_target,Sizeof(FData.account_target));
  case FData.operation_type of
    lat_ListForSale : w := CT_Op_ListAccountForSale;
    lat_DelistAccount : w := CT_Op_DelistAccount;
  else raise Exception.Create('ERROR DEV 20170412-1');
  end;
  Stream.Write(w,2);
  Stream.Write(FData.n_operation,Sizeof(FData.n_operation));
  if FData.operation_type=lat_ListForSale then begin
    Stream.Write(FData.account_price,Sizeof(FData.account_price));
    Stream.Write(FData.account_to_pay,Sizeof(FData.account_to_pay));
    
    Stream.Write(FData.public_key.EC_OpenSSL_NID,Sizeof(FData.public_key.EC_OpenSSL_NID));6
    TStreamOp.WriteAnsiString(Stream,FData.public_key.x);
    TStreamOp.WriteAnsiString(Stream,FData.public_key.y);
    
    TStreamOp.WriteAnsiString(Stream,TAccountComp.AccountKey2RawString(FData.new_public_key)); 6
    
    Stream.Write(FData.locked_until_block,Sizeof(FData.locked_until_block));
  end;
  Stream.Write(FData.fee,Sizeof(FData.fee));
  TStreamOp.WriteAnsiString(Stream,FData.payload);
  TStreamOp.WriteAnsiString(Stream,FData.sign.r);
  TStreamOp.WriteAnsiString(Stream,FData.sign.s);
  r:=TStreamOp.SaveStreamToRaw(Stream);
	 * 
	 * DAEE0100 34CF0800 36000000 80969800 00000000 E8030000 640000000000000054657374696E67206C6973744163636F756E74466F7253616C65 00000000000000000000000004
	 * signer   to sell   noper    price             payed    fee						ECNIC
	 * DAEE0100 34CF0800 37000000 8096980000000000 DAEE0100 0100000000000000 54455354 000000000000000000000000 04
	 * 1000 = E803
	 * 577332 = 34CF08
	 * 
	 * DAEE0100 34CF0800 38000000 80969800 00000000 DAEE0100 0100000000000000 54455354  00000000 00000000 040F0400 04
	 * 
	 * 
	 * unction TOpListAccount.SaveOpToStream(Stream: TStream; SaveExtendedData : Boolean): Boolean;
Var w : Word;
    r : TRawBytes;
begin
  Stream.Write(FData.account_signer,Sizeof(FData.account_signer));
  Stream.Write(FData.account_target,Sizeof(FData.account_target));
  case FData.operation_type of
    lat_ListForSale : w := CT_Op_ListAccountForSale;
    lat_DelistAccount : w := CT_Op_DelistAccount;
  else raise Exception.Create('ERROR DEV 20170412-1');
  end;
  Stream.Write(w,2);
  Stream.Write(FData.n_operation,Sizeof(FData.n_operation));
  if FData.operation_type=lat_ListForSale then begin
    Stream.Write(FData.account_price,Sizeof(FData.account_price));
    Stream.Write(FData.account_to_pay,Sizeof(FData.account_to_pay));
    
    Stream.Write(FData.public_key.EC_OpenSSL_NID,Sizeof(FData.public_key.EC_OpenSSL_NID));6
    TStreamOp.WriteAnsiString(Stream,FData.public_key.x);
    TStreamOp.WriteAnsiString(Stream,FData.public_key.y);
    
    TStreamOp.WriteAnsiString(Stream,TAccountComp.AccountKey2RawString(FData.new_public_key)); 6
    
    Stream.Write(FData.locked_until_block,Sizeof(FData.locked_until_block));
  end;
  Stream.Write(FData.fee,Sizeof(FData.fee));
  TStreamOp.WriteAnsiString(Stream,FData.payload);
  TStreamOp.WriteAnsiString(Stream,FData.sign.r);
  TStreamOp.WriteAnsiString(Stream,FData.sign.s);
  r:=TStreamOp.SaveStreamToRaw(Stream);
  TLog.NewLog(lterror,ClassName,Format('TOpListAccount.SaveOpToStream:%s',[TCrypto.ToHexaString(r)]));
  Result := true;
end;           
	 * 
	 * 
	 * 
	 * DAEE0100 34CF0800 0400360000008096980000000000E803000000000000000006000000000000000000000064000000000000001A0054657374696E67206C6973744163636F756E74466F7253616C652000A3F5AB8AB8308677ABF6660E193916CB7E1D8601008A85CB266E271E7A897BD22000F48824FAAB5EEC8601E223803268E7EA13DEEEC96D84488AC06733F90357D143
	 * Example:
NOTE: The private keys, and values shown at this example are tested and works. This is a REAL example with REAL values.
Account 3700-88 wants to send 3.5 PASC to account 7890-83 with a payload data "EXAMPLE" non encrypted (public payload)
Account 3700-88 private key type SECP_256k1 is:
Private key: 37B799726961D231492823513F5686B3F7C7909DEFF20907D91BF4D24A356624
Public key: x:1F9462CA6FA8FB39DC309F2EF3A6EB8AE9B2538D4EB62055A316692CCEA1557F y:42FFDCCF7ACEA4685EEC3C0A9F9B223636CF257902693933FB0D1EC14A6C519B

Using JSON-RPC getaccount we can see information about account 3700-88
{"account":3700,"enc_pubkey":"CA0220001F9462CA6FA8FB39DC309F2EF3A6EB8AE9B2538D4EB62055A316692CCEA1557F200042FFDCCF7ACEA4685EEC3C0A9F9B223636CF257902693933FB0D1EC14A6C519B","balance":80,"n_operation":1,"updated_b":33062,"state":"normal","name":"","type":0}

DIGEST RAW creation: (shown as an hexadecimal RAW)
- 740E0000 <- Sender account 3700 to hexadecimal stored as a little endian in 4 bytes
- 02000000 <- Sender next n_operation value (previous was 1, next will be 2) stored as a little endian in 4 bytes
- D21E0000 <- Target account 7890 to hexadecimal stored as a little endian in 4 bytes
- B888000000000000 <- Amount 3.5 PASC in native value (*10000) = 35000 to hexadecimal stored as a little endian in 8 bytes
- 0100000000000000 <- Fee 0.0001 PASC in native value (*10000) = 1 to hexadecimal stored as a little endian in 8 bytes
- 4558414D504C45 <- Payload "EXAMPLE" value
- 0000 <- CONSTANT VALUE 2 bytes to "null"
- 01 <- OpType 1=Transaction 1 byte. NOTE: We're using new Protocol V4 (currently only available on TESTNET)

Final HASH value to be signed in Protocol V4 is:
				 740E000002000000D21E0000B88800000000000001000000000000004558414D504C45000001
- HASH = SHA256( 740E000002000000D21E0000B88800000000000001000000000000004558414D504C45000001 )
- HASH = B8C2057F4BA187B7A29CC810DB56B66C7B9361FA64FD77BADC759DD21FF4ABE7 <- Digest to be signed!
Note: For previous protocols (v3 and lower), the HASH = 740E000002000000D21E0000B88800000000000001000000000000004558414D504C450000

SIGNATURE of HASH using private key 37B799726961D231492823513F5686B3F7C7909DEFF20907D91BF4D24A356624 <- Note: This is TESTNET Account 3700 private key
K = A235553C44D970D0FC4D0C6C1AF80330BF06E3B4A6C039A7B9E8A2B5D3722D1F <- Not needed, only for testing purposes if training with this code

			 
SIGNATURE.R = EFD5CBC12F6CC347ED55F26471E046CF59C87E099513F56F4F1DD49BDFA84C0E
SIGNATURE.S = 7BCB0D96A93202A9C6F11D90BFDCAB99F513C880C4888FECAC74D9B09618C06E

Creating the RAWOPERATIONS to transfer:
- 01000000 <- Operations count (1 for this example) stored as a little endian in 4 bytes
- For each operation...
  - 01000000 <- Optype (1=Transaction) stored as a 4 bytes in little endian
  If this is a "Transaction" then...
  - 740E0000 <- Sender account 3700 to hexadecimal stored as a little endian in 4 bytes
  - 02000000 <- Sender next n_operation value (previous was 1, next will be 2) stored as a little endian in 4 bytes
  - D21E0000 <- Target account 7890 to hexadecimal stored as a little endian in 4 bytes
  - B888000000000000 <- Amount 3.5 PASC in native value (*10000) = 35000 to hexadecimal stored as a little endian in 8 bytes
  - 0100000000000000 <- Fee 0.0001 PASC in native value (*10000) = 1 to hexadecimal stored as a little endian in 8 bytes
  - 0700 <- Payload length (EXAMPLE = 7 bytes) storead in 2 bytes little endian
  - 4558414D504C45 <- Payload "EXAMPLE" value (7 bytes)
  - 000000000000 <- CONSTANT VALUE 6 bytes to "null"
  - 2000 <- Signature.R length (32 to hexadecimal = 0x20) stored as a 2 bytes little endian
  - EFD5CBC12F6CC347ED55F26471E046CF59C87E099513F56F4F1DD49BDFA84C0E <- SIGNATURE.R
  - 2000 <- Signature.S length (32 to hexadecimal = 0x20) stored as a 2 bytes little endian
  - 7BCB0D96A93202A9C6F11D90BFDCAB99F513C880C4888FECAC74D9B09618C06E <- SIGNATURE.S
  
RAWOPERATIONS = 
0100000001000000740E000002000000D21E0000B888000000000000010000000000000007004558414D504C450000000000002000EFD5CBC12F6CC347ED55F26471E046CF59C87E099513F56F4F1DD49BDFA84C0E20007BCB0D96A93202A9C6F11D90BFDCAB99F513C880C4888FECAC74D9B09618C06E

Execute JSON-RPC call to "executeoperations" and param "rawoperations" the above value we get:
{"result":[{"block":0,"time":0,"opblock":0,"maturation":null,"optype":1,"subtype":11,"account":3700,"signer_account":3700,"n_operation":2,"senders":[{"account":3700,"n_operation":2,"amount":-3.5001,"payload":"4558414D504C45"}],"receivers":[{"account":7890,"amount":3.5,"payload":"4558414D504C45"}],"changers":[],"optxt":"Tx-Out 3,5000 PASC from 3700-88 to 7890-83","fee":-0.0001,"amount":-3.5,"payload":"4558414D504C45","balance":76.4999,"sender_account":3700,"dest_account":7890,"ophash":"00000000740E00000200000054BB7CC4FC784B6877DA7E3D3181B9510CAB2340","old_ophash":""}],"id":100,"jsonrpc":"2.0"}
	 */

}
