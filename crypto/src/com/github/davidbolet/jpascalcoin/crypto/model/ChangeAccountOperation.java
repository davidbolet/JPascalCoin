package com.github.davidbolet.jpascalcoin.crypto.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
		if (newPublicKey!=null && !"".equals(newPublicKey)) {
			//digestBuffer.write(HexConversionsHelper.decodeStr2Hex(HexConversionsHelper.int2BigEndianHex(2)));
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
	
	//@Override
	public byte[] generateOpDigestOld(float protocolVersion) {
		ByteArrayOutputStream digestBuffer = new ByteArrayOutputStream();
		try {
			digestBuffer.write(HexConversionsHelper.decodeStr2Hex(HexConversionsHelper.int2BigEndianHex4Byte(accountSigner)));
			digestBuffer.write(HexConversionsHelper.decodeStr2Hex(HexConversionsHelper.int2BigEndianHex4Byte(accountToChange)));
			digestBuffer.write(HexConversionsHelper.decodeStr2Hex(HexConversionsHelper.int2BigEndianHex4Byte(nOperation)));
			digestBuffer.write(HexConversionsHelper.decodeStr2Hex(HexConversionsHelper.int2BigEndianHex8Byte(Math.round(fee*10000))));
			//digestBuffer.write(HexConversionsHelper.decodeStr2Hex(HexConversionsHelper.int2BigEndianHex(0))); //payload.length
			digestBuffer.write(getPayload());
			//digestBuffer.write(HexConversionsHelper.decodeStr2Hex(HexConversionsHelper.int2BigEndianHex(2)));
			digestBuffer.write(HexConversionsHelper.decodeStr2Hex(signerPublicKey.getEncPubKey()));
			digestBuffer.write(getOpByte());
			if (newPublicKey!=null && !"".equals(newPublicKey)) {
				//digestBuffer.write(HexConversionsHelper.decodeStr2Hex(HexConversionsHelper.int2BigEndianHex(2)));
				digestBuffer.write(HexConversionsHelper.decodeStr2Hex(newPublicKey.getEncPubKey()));
			}
			else
			{
				digestBuffer.write(HexConversionsHelper.decodeStr2Hex(HexConversionsHelper.int2BigEndianHex(0)));
				digestBuffer.write(HexConversionsHelper.decodeStr2Hex(HexConversionsHelper.int2BigEndianHex4Byte(0)));
			}
			digestBuffer.write(HexConversionsHelper.decodeStr2Hex(getNewAccountNameDigest()));
			if (newAccountType!=null) {
				digestBuffer.write(HexConversionsHelper.decodeStr2Hex(HexConversionsHelper.int2BigEndianHex(newAccountType)));
			}
			if (protocolVersion>=4.0f)
			{
				digestBuffer.write(HexConversionsHelper.decodeStr2Hex(HexConversionsHelper.int2BigEndianHex(8)));
			}
		}
		catch(IOException ex) {
			
		}
		finalHash=HexConversionsHelper.byteToHex(digestBuffer.toByteArray());
		return digestBuffer.toByteArray();
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
		if (newPublicKey!=null && !"".equals(newPublicKey)) {
			digestBuffer.append(signerPublicKey.getEncPubKey());
		}
		else
		{
			digestBuffer.append(HexConversionsHelper.int2BigEndianHex(0));
			digestBuffer.append(HexConversionsHelper.int2BigEndianHex(0));
			digestBuffer.append(HexConversionsHelper.int2BigEndianHex(0));
		}
		digestBuffer.append(HexConversionsHelper.byteToHex(getOpByte()));		
		if (newPublicKey!=null && !"".equals(newPublicKey)) {
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
		//System.out.println(finalHash);
		return HexConversionsHelper.decodeStr2Hex(finalHash);
	}
	
	/*      DAEE0100 282E0900 31000000 0100000000000000 0400 54455354 000000000000 04 000000000000 0000 0000 08
	 *      DAEE0100 282E0900 05000000 0100000000000000 0400 54455354 000000000000 04 000000000000 0000 0000 08
	 * 
	 *      DAEE0100 282E0900 32000000 0100000000000000 0400 54455354 000000000000 06 000000000000 0A00 746573742E6461766964 0000 08
	 *      DAEE0100 282E0900 05000000 0100000000000000 0400 54455354 000000000000 06 000000000000 0500 64617669642e74657374 0000 08
	 *      
	 *      DAEE0100 282E0900 33000000 0100000000000000 0400 54455354 000000000000 02 000000000000 0A00 746573742E6461766964 0000 08
	 *      DAEE0100 282E0900 05000000 0100000000000000 0400 54455354 000000000000 02 000000000000 0A00 64617669642E74657374 08
	 * 		DAEE0100282E09000500000001000000000000000400544553540000000400000000000008
	 * 		00000000DAEE01003100000080CA3BC34E4F59E32864DE5902E79AC6DE4DC9D2
	
			DAEE0100 282E0900 34000000 0100000000000000 0400 54455354 000000000000 01 CA0220004BC8A8701B1CC25AC33575F168E01434F0A2CA07D5EC041EA3B54CF6EC2DDF4E20006D5B782E8F18BFC7E08DDDAB16F793792C1B728D2EBC9950752A17E27B429B5B 0000 0000 08
			DAEE0100 1C2D0900 04000000 0100000000000000 0400 54455354 CA02200073727AF9C68184749B063FE4D558F643FC58601FA5980C1F77C6846EFD4FEA922000AE18F37219D7BB654EB50C8048F62BFFDEB47A03F33AAF5AF876C507F0B387A101CA0220004BC8A8701B1CC25AC33575F168E01434F0A2CA07D5EC041EA3B54CF6EC2DDF4E20006D5B782E8F18BFC7E08DDDAB16F793792C1B728D2EBC9950752A17E27B429B5B0000000008
	 */
	
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
		//.append("0100") //Optype
		.append(HexConversionsHelper.int2BigEndianHex4Byte(accountSigner))
		.append(HexConversionsHelper.int2BigEndianHex4Byte(accountToChange))
		.append(HexConversionsHelper.int2BigEndianHex4Byte(nOperation))
		.append(HexConversionsHelper.int2BigEndianHex8Byte(Math.round(fee*10000)))
		.append(HexConversionsHelper.int2BigEndianHex(getPayload().length))
		.append(HexConversionsHelper.byteToHex(getPayload()).toUpperCase());
		if (newPublicKey!=null && !"".equals(newPublicKey)) {
			digestBuffer.append(signerPublicKey.getEncPubKey());
		}
		else
		{
			digestBuffer.append(HexConversionsHelper.int2BigEndianHex(0));
			digestBuffer.append(HexConversionsHelper.int2BigEndianHex(0));
			digestBuffer.append(HexConversionsHelper.int2BigEndianHex(0));
		}
		digestBuffer.append(HexConversionsHelper.byteToHex(getOpByte()));		
		if (newPublicKey!=null && !"".equals(newPublicKey)) {
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
		/* DAEE0100282E09000500000001000000000000000400544553540000000400000000000008
		 * 1F9D0C0002000000010000000000000000000000000000004600CA0220002F52600B961DA8D0A139A31E78C1113571C6B82C925CA50006D427F04B5201C72000A3E068959E488A489D3B701B15DDEA263F4460A30EB0EACA6D0D85BD0521738820009D31A0F2AFB23C9076CAAFF1CC1FD420F2EFCC73BE8DF417DADD3F4944F5042B200034081C895CE87D3383FE118683DE8558E8FF346DA63E468FA858DFFB834A3397
		 * DAEE0100282E0900320000000100000000000000040054455354000000000000060000000000000A00746573742E6461766964000008
		 * 0100000008DAEE0100282E0900320000000100000000000000040054455354000000000000060000000000000A00746573742E646176696400002000639A1498D672999A76290C35BF76ADE8F4713C537D2E409FCEFA932AF0F82CA520006F147D07A85D4ACDDCC23D758F513953988D94F58373D906AB606B82C5C29F27
		 * 
		 * Stream.Write(FData.account_signer,Sizeof(FData.account_signer));
  Stream.Write(FData.account_target,Sizeof(FData.account_target));
  Stream.Write(FData.n_operation,Sizeof(FData.n_operation));
  Stream.Write(FData.fee,Sizeof(FData.fee));
  TStreamOp.WriteAnsiString(Stream,FData.payload);
  TStreamOp.WriteAccountKey(Stream,FData.public_key);
  b := 0;
  if (public_key in FData.changes_type) then b:=b OR $01;
  if (account_name in FData.changes_type) then b:=b OR $02;
  if (account_type in FData.changes_type) then b:=b OR $04;
  Stream.Write(b,Sizeof(b));
  TStreamOp.WriteAccountKey(Stream,FData.new_accountkey);
  TStreamOp.WriteAnsiString(Stream,FData.new_name);
  Stream.Write(FData.new_type,Sizeof(FData.new_type));
  TStreamOp.WriteAnsiString(Stream,FData.sign.r);
  TStreamOp.WriteAnsiString(Stream,FData.sign.s);          
		 * 
		 *  changes_type : TOpChangeAccountInfoTypes; // bits mask. $0001 = New account key , $0002 = New name , $0004 = New type
		    new_accountkey: TAccountKey;  // If (changes_mask and $0001)=$0001 then change account key
		    new_name: TRawBytes;          // If (changes_mask and $0002)=$0002 then change name
		    new_type: Word;               // If (changes_mask and $0004)=$0004 then change type                 
		 * 
		 * 
		 *  TOpDataData = Record
    account_signer,              // The account paying fees (if any) and signing operation
    account_sender,              // The account sender. Public key must be EQUAL to account_signer public key
    account_target: Cardinal;    // The destination account. Will recive DATA and amount (if any)
    n_operation : Cardinal;      // Signer n_operation
    dataType : Word;             // 2 byte data type
    dataSequence : Word;         // 2 byte data sequence
    amount: UInt64;              // Allow amount=0
    fee: UInt64;                 // Allow fee=0
    payload: TRawBytes;          // Standard arbitrary data with length<=256
    sign: TECDSA_SIG;
  End;                
		 * 
		 * Constructor CreateChangeAccountInfo(current_protocol : word;
      account_signer, n_operation, account_target: Cardinal; key:TECPrivateKey;
      change_key : Boolean; const new_account_key : TAccountKey;
      change_name: Boolean; const new_name : TRawBytes;
      change_type: Boolean; const new_type : Word;
      fee: UInt64; payload: TRawBytes);         
		 * 
		 * 
		Stream.Write(op.account_signer,Sizeof(op.account_signer));
	    Stream.Write(op.account_target,Sizeof(op.account_target));
	    Stream.Write(op.n_operation,Sizeof(op.n_operation));
	    Stream.Write(op.fee,Sizeof(op.fee));
	    TStreamOp.WriteAnsiString(Stream,op.payload);
	    TStreamOp.WriteAccountKey(Stream,op.public_key);
	    b := 0;
	    if (public_key in op.changes_type) then b:=b OR $01;
	    if (account_name in op.changes_type) then b:=b OR $02;
	    if (account_type in op.changes_type) then b:=b OR $04;
	    Stream.Write(b,Sizeof(b));
	    TStreamOp.WriteAccountKey(Stream,op.new_accountkey);
	    TStreamOp.WriteAnsiString(Stream,op.new_name);
	    Stream.Write(op.new_type,Sizeof(op.new_type));
	    Stream.Position := 0;
	    setlength(Result,Stream.Size);
	    Stream.ReadBuffer(Result[1],Stream.Size);
	      TECDSA_Public = record
     EC_OpenSSL_NID : Word;
     x: TRawBytes;
     y: TRawBytes;
  end;   
	     
	     */        

}
