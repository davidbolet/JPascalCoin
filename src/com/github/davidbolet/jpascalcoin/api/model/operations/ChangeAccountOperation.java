package com.github.davidbolet.jpascalcoin.api.model.operations;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import com.github.davidbolet.jpascalcoin.api.model.PayLoadEncryptionMethod;

public class ChangeAccountOperation implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Integer accountSigner;
	Integer accountToChange;
	Integer nOperation;
	Double fee;
	byte[] payload;
	Integer newAccountType;
	String newAccountName;
	String newPublicKey;
	
	public ChangeAccountOperation(Integer accountToChange,Integer accountSigner,Integer nOperation,Double fee, String newName, String newKey, Integer  newType, String payload, PayLoadEncryptionMethod method, String aesPassword)
	{
		//this.payload=Payload.
		this.accountSigner=accountSigner;
		this.accountToChange=accountToChange;
		this.nOperation=nOperation;
		this.fee=fee;
		this.newAccountName=newName;
		this.newPublicKey=newKey;
		this.newAccountType=newType;
	}
	public Integer getAccountSigner() {
		return accountSigner;
	}

	public void setAccountSigner(Integer accountSigner) {
		this.accountSigner = accountSigner;
	}

	public Integer getAccountToChange() {
		return accountToChange;
	}

	public void setAccountToChange(Integer accountToChange) {
		this.accountToChange = accountToChange;
	}

	public Integer getnOperation() {
		return nOperation;
	}

	public void setnOperation(Integer nOperation) {
		this.nOperation = nOperation;
	}

	public Double getFee() {
		return fee;
	}

	public void setFee(Double fee) {
		this.fee = fee;
	}

	public byte[] getPayload() {
		return payload;
	}

	public void setPayload(byte[] payload) {
		this.payload = payload;
	}

	public Integer getNewAccountType() {
		return newAccountType;
	}

	public void setNewAccountType(Integer newAccountType) {
		this.newAccountType = newAccountType;
	}

	public String getNewAccountName() {
		return newAccountName;
	}

	public void setNewAccountName(String newAccountName) {
		this.newAccountName = newAccountName;
	}

	public String getNewPublicKey() {
		return newPublicKey;
	}

	public void setNewPublicKey(String newPublicKey) {
		this.newPublicKey = newPublicKey;
	}
	
	public Long getPascLong(Double value) {
		Double res=value*10000;
		return res.longValue();
	}

	public byte[] generateOpDigest() {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		int b=0;
		byte[] result=null;
		ObjectOutput out = null;
		try {
		  out = new ObjectOutputStream(bos);   
		  out.writeObject(this.getAccountSigner().shortValue());
		  out.writeObject(this.getAccountToChange().shortValue());
		  out.writeObject(this.getnOperation().shortValue());
		  out.writeLong(getPascLong(this.getFee()));
		  out.write(getPayload());
		  //out.write() write public key
		  if (newPublicKey!=null && !"".equals(newPublicKey)) {
			  b=b|1;
		  }
		  if (newAccountName!=null && !"".equals(newAccountName)) {
			  b=b|2;
		  }
		  if (newAccountType!=null) {
			  b=b|4;
		  }
		  out.writeByte(b);
		  out.flush();
		  result = bos.toByteArray();
		  bos.close();
		} catch (IOException ex) {
		
		}
		return result;
		/*
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
	     *
	     */        
	}

}
