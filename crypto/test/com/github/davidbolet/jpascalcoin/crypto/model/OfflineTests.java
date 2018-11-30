package com.github.davidbolet.jpascalcoin.crypto.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;

import org.junit.Test;

import com.github.davidbolet.jpascalcoin.common.helper.CryptoUtils;
//import com.github.davidbolet.jpascalcoin.api.helpers.BouncyCastleKeyHelper;
import com.github.davidbolet.jpascalcoin.common.helper.HexConversionsHelper;
import com.github.davidbolet.jpascalcoin.common.model.KeyType;
import com.github.davidbolet.jpascalcoin.common.model.PayLoadEncryptionMethod;
import com.github.davidbolet.jpascalcoin.common.model.PascPublicKey;
import com.github.davidbolet.jpascalcoin.crypto.model.PascPrivateKey;
import com.github.davidbolet.jpascalcoin.crypto.model.TransferOperation;
import com.github.davidbolet.jpascalcoin.common.exception.UnsupportedKeyTypeException;

public class OfflineTests {
	@Test
	public void testGenerateDigest() {
		Integer sender=3700;
		Integer receiver=7890;
		Integer nOperation=2;
		Double amount=3.5;
		Double fee = 0.0001;
		byte[] payload="EXAMPLE".getBytes();
		
		TransferOperation transferOperation=new TransferOperation(sender,receiver,nOperation, amount, fee, payload, PayLoadEncryptionMethod.NONE, null);
		
		String result= HexConversionsHelper.byteToHex(transferOperation.generateOpDigest(4.0f)).toUpperCase();
		
		assertEquals("740E000002000000D21E0000B88800000000000001000000000000004558414D504C45000001",result);
		
		/*
		 *  740E0000 <- Sender account 3700 to hexadecimal stored as a little endian in 4 bytes
- 02000000 <- Sender next n_operation value (previous was 1, next will be 2) stored as a little endian in 4 bytes
- D21E0000 <- Target account 7890 to hexadecimal stored as a little endian in 4 bytes
- B888000000000000 <- Amount 3.5 PASC in native value (*10000) = 35000 to hexadecimal stored as a little endian in 8 bytes
- 0100000000000000 <- Fee 0.0001 PASC in native value (*10000) = 1 to hexadecimal stored as a little endian in 8 bytes
- 4558414D504C45 <- Payload "EXAMPLE" value
- 0000 <- CONSTANT VALUE 2 bytes to "null"
- 01 <- OpType 1=Transaction 1 byte. NOTE: We're using new Protocol V4 (currently only available on TESTNET)

Final HASH value to be signed in Protocol V4 is:
- HASH = SHA256( 740E000002000000D21E0000B88800000000000001000000000000004558414D504C45000001 )
- HASH = B8C2057F4BA187B7A29CC810DB56B66C7B9361FA64FD77BADC759DD21FF4ABE7 <- Digest to be signed!
		 */
	}
	
	
	@Test
	public void verifyPascGeneratedSignature() {
		String privateKey="37B799726961D231492823513F5686B3F7C7909DEFF20907D91BF4D24A356624";
		Integer sender=3700;
		Integer receiver=7890;
		Integer nOperation=2;
		Double amount=3.5;
		Double fee = 0.0001;
		byte[] payload="EXAMPLE".getBytes();
		
		TransferOperation transferOperation=new TransferOperation(sender,receiver,nOperation, amount, fee, payload, PayLoadEncryptionMethod.NONE, null);
		byte[] toSign=transferOperation.generateOpDigest(4.0f);
		String text=HexConversionsHelper.byteToHex(toSign);
		System.out.println(text);
		BigInteger R1 = new BigInteger(HexConversionsHelper.decodeStr2Hex("EFD5CBC12F6CC347ED55F26471E046CF59C87E099513F56F4F1DD49BDFA84C0E"));

		String R1str=R1.toString(16).toUpperCase();;
		System.out.println(R1str);
		BigInteger R=new BigInteger(R1str, 16);
		
		System.out.println(R.toString(16));
		System.out.println(R.toString(10));
		
		BigInteger S = new BigInteger("7BCB0D96A93202A9C6F11D90BFDCAB99F513C880C4888FECAC74D9B09618C06E", 16);		
		
		System.out.println(S.toString(16));
		System.out.println(S.toString(10));

		byte[] signature= CryptoUtils.getDerEncodedSignature(R,S);

		PascPrivateKey key = PascPrivateKey.fromPrivateKey(privateKey, KeyType.SECP256K1);
		PascPublicKey publicKey=key.getPublicKey();
		boolean r= publicKey.verify(signature, toSign);
		assertTrue(r);
	}
	
	@Test
	public void testVerify() throws UnsupportedKeyTypeException, UnsupportedEncodingException, NoSuchProviderException, InvalidKeyException, SignatureException, NoSuchAlgorithmException {
		boolean r=false;
		PascPrivateKey key = PascPrivateKey.generate(KeyType.SECP256K1);
		PascPublicKey publicKey= key.getPublicKey();
		String message="This is the message";
		
		OfflineSignResult res=key.sign(message);
		//BigInteger s=new BigInteger(key.getPrivateKey(), 16);
		//java.security.PublicKey pub = bouncyCastleKeyHelper.fromPrivateKey(s);
		
		//r=bouncyCastleKeyHelper.verifySignature(pub, res.getSignature(), message.getBytes());
       // r=r && bouncyCastleKeyHelper.verifySignature(publicKey.getECPublicKey(), res.getSignature(), message.getBytes());;
		r=publicKey.verify(res.getByteSignature(), message.getBytes());
		assertTrue(r);		
	}
	

	@Test
	public void testDerSign() throws UnsupportedKeyTypeException, UnsupportedEncodingException, NoSuchProviderException, InvalidKeyException, SignatureException, NoSuchAlgorithmException {

		PascPrivateKey key = PascPrivateKey.generate(KeyType.SECP256K1);
		String message="This is the message";
		
		OfflineSignResult res=key.sign(message);

		byte[] result=CryptoUtils.getDerEncodedSignature(res.getR(), res.getS());
		assertEquals(HexConversionsHelper.byteToHex(result).toUpperCase(),res.getStringSignature());
	}
	
	
}
