package com.github.davidbolet.jpascalcoin.crypto.model;

import java.io.Serializable;

import com.github.davidbolet.jpascalcoin.common.helper.HexConversionsHelper;
import com.github.davidbolet.jpascalcoin.crypto.model.PascOperation;
import com.github.davidbolet.jpascalcoin.common.model.PayLoadEncryptionMethod;

public class TransferOperation extends PascOperation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	final Integer accountSender;
	final Integer accountTarget;
	final Integer nOperation;
	final Double amount;
	final Double fee;
	
	public TransferOperation(Integer accountSender,Integer accountTarget,Integer nOperation, Double amount, Double fee, byte[] payload, PayLoadEncryptionMethod payloadMethod, String pwd ) {
		super(payload,payloadMethod, pwd );
		this.accountSender=accountSender;
		this.accountTarget=accountTarget;
		this.nOperation=nOperation;
		this.amount=amount;
		this.fee=fee;
	}

	@Override
	public byte[] generateOpDigest(float protocolVersion) {
		StringBuffer digestBuffer = new StringBuffer();
		digestBuffer.append(HexConversionsHelper.int2BigEndianHex4Byte(accountSender))
		.append(HexConversionsHelper.int2BigEndianHex4Byte(nOperation))
		.append(HexConversionsHelper.int2BigEndianHex4Byte(accountTarget))
		.append(HexConversionsHelper.int2BigEndianHex8Byte(Math.round(amount*10000)))
		.append(HexConversionsHelper.int2BigEndianHex8Byte(Math.round(fee*10000)))
		.append(HexConversionsHelper.byteToHex(payload).toUpperCase())
		.append(HexConversionsHelper.int2BigEndianHex(0));
		if (protocolVersion>=4.0f)
		{
			digestBuffer.append("01");
		}
		finalHash=digestBuffer.toString();
		return HexConversionsHelper.decodeStr2Hex(finalHash);
	}
	
	@Override
	public String getRawOperations(String signR, String signS) {
		StringBuffer digestBuffer = new StringBuffer();
		//Operations count (1 for this example) stored as a little endian in 4 bytes
		digestBuffer.append(HexConversionsHelper.int2BigEndianHex4Byte(1))
		.append("01000000") //Optype
		.append(HexConversionsHelper.int2BigEndianHex4Byte(accountSender))
		.append(HexConversionsHelper.int2BigEndianHex4Byte(nOperation))
		.append(HexConversionsHelper.int2BigEndianHex4Byte(accountTarget))
		.append(HexConversionsHelper.int2BigEndianHex8Byte(Math.round(amount*10000)))
		.append(HexConversionsHelper.int2BigEndianHex8Byte(Math.round(fee*10000)))
		.append(HexConversionsHelper.int2BigEndianHex(payload.length))
		.append(HexConversionsHelper.byteToHex(payload).toUpperCase())
		.append("000000000000")
		.append(HexConversionsHelper.int2BigEndianHex(signR.length()/2))
		.append(signR)
		.append(HexConversionsHelper.int2BigEndianHex(signS.length()/2))
		.append(signS);
		return digestBuffer.toString();
	}

	/*
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
