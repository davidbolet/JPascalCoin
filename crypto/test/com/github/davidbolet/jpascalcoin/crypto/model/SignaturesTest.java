//package com.github.davidbolet.jpascalcoin.crypto.model;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertTrue;
//
//import java.math.BigInteger;
//import java.security.NoSuchAlgorithmException;
//import java.util.List;
//
//import org.junit.Before;
//import org.junit.Test;
//
//import com.github.davidbolet.jpascalcoin.common.helper.HexConversionsHelper;
//import com.github.davidbolet.jpascalcoin.common.model.KeyType;
//import com.github.davidbolet.jpascalcoin.common.model.PayLoadEncryptionMethod;
//import com.github.davidbolet.jpascalcoin.common.model.PublicKey;
//import com.github.davidbolet.jpascalcoin.crypto.model.PascPrivateKey;
//import com.github.davidbolet.jpascalcoin.crypto.helper.OpenSslAes;
//
//import com.github.davidbolet.jpascalcoin.api.client.PascalCoinClient;
//import com.github.davidbolet.jpascalcoin.api.client.PascalCoinClientImpl;
//import com.github.davidbolet.jpascalcoin.api.constants.PascalCoinConstants;
//import com.github.davidbolet.jpascalcoin.api.model.Account;
//import com.github.davidbolet.jpascalcoin.api.model.Operation;
//import com.github.davidbolet.jpascalcoin.api.model.SignResult;
//import com.github.davidbolet.jpascalcoin.api.model.operations.TransferOperation;
//
///**
// * Advanced tests for signatures and Private/Public keys
// * 
// * @author davidbolet
// *
// */
//public class SignaturesTest {
//	PascalCoinClient client;
//	String encPubKey,b58PubKey, b58PubKeyOtherWallet;
//	Integer accountId, account2Id, account3Id, account4Id;
//	String password;
//	String b58PubKey2, b58BuyKey;
//	
//	/**
//	 * Initialize all vars here
//	 */
//	@Before
//	public void init()
//	{
//		//String base="localhost";
//		client = new PascalCoinClientImpl("10.211.55.10",PascalCoinConstants.DEFAULT_MAINNET_RPC_PORT,1);
//		//client = new PascalCoinClientImpl("10.211.55.7",PascalCoinConstants.DEFAULT_MAINNET_RPC_PORT,1);
//		
//		//b58PubKey ="3GhhbopiJkQFZYUJ2vAYMmBJj2hWybSLJjkHEvqPjpdaDKGG8S5CvCzvYVbs9azzvSEtFDpvvZxftvB5dgGnDunvA64oq9HqfskigY";
//		b58PubKey = "3GhhbouPE7mf5rVxu7rm8f2dEczavgmeZXoxU5Z1QtraVQwurYBgmRS2Q5F49VyVn5yDpQV87a6VTTFiKAF6bDbmeDb2MDxLxUT616";
//		//b58PubKey="<Your public key (as wallet exports)>";
//		//b58PubKey="3Ghhbom4iB3ZAGDPq2C4jE2NrF8n9QTno7jvJPzJLkPmoQs9a58Q82RPQqXaJV5LQBMjdNDQquWbPB5o4QHVLWLwoAwkJ7dQ3A4xFA";
//		//encPubKey="CA022000206D6BBAE9FFBC5582D711D472E688C7FF0D459956E58A5C9BBA090075C71626200062730E04C61BCCD33853B4197BF28CF9CF280E37407F526712143B11D9AD3454";
////		b58PubKey2 ="<Another public key>";
//
//		b58PubKeyOtherWallet="<Another public key>";
//		b58BuyKey="<Another public key used on buyAccount>";
//		accountId = 126682; //An account id
//		account2Id = 381309; //An account id
//		account3Id = 381403; //An account id
//		account4Id = 381404; //An account id
//		password = "L1L0kio10!"; //Your wallet password
//		//Initially unlock wallet,
//		client.unlock(password);
//	}
//	

//	
//	

//	
//	
//	@Test
//	public void testSignature() throws Exception {
//		String privateKey="37B799726961D231492823513F5686B3F7C7909DEFF20907D91BF4D24A356624";
//		Integer sender=3700;
//		Integer receiver=7890;
//		Integer nOperation=2;
//		Double amount=3.5;
//		Double fee = 0.0001;
//		byte[] payload="EXAMPLE".getBytes();
//		
//		TransferOperation transferOperation=new TransferOperation(sender,receiver,nOperation, amount, fee, payload, PayLoadEncryptionMethod.NONE, null);
//		byte[] toSign=transferOperation.generateOpDigest(4.0f);
//		
//		assertEquals("740E000002000000D21E0000B88800000000000001000000000000004558414D504C45000001",transferOperation.getFinalHash());
//		
//		PascPrivateKey key = PascPrivateKey.fromPrivateKey(privateKey, KeyType.SECP256K1);
//		PublicKey publicKey=key.getPublicKey();
//		
//		assertEquals("1F9462CA6FA8FB39DC309F2EF3A6EB8AE9B2538D4EB62055A316692CCEA1557F",publicKey.getX());
//		assertEquals("42FFDCCF7ACEA4685EEC3C0A9F9B223636CF257902693933FB0D1EC14A6C519B",publicKey.getY());
//		
//		PublicKey pk2=PublicKey.fromECPublicKey(publicKey.getECPublicKey());
//		assertEquals("42FFDCCF7ACEA4685EEC3C0A9F9B223636CF257902693933FB0D1EC14A6C519B", pk2.getY());
//		assertEquals(publicKey.getBase58PubKey(),pk2.getBase58PubKey());
//		
//		PascPrivateKey.SignResult signed = key.sign(toSign);
//		System.out.println(signed);
//		PascPrivateKey.SignResult signature3=key.sign(toSign);
//		System.out.println(signature3.getStringSignature());
//
//		String rawOperations=transferOperation.getRawOperations(signed.getStringR(), signed.getStringS());
//		System.out.println(rawOperations);
//		SignResult res= client.signMessage(HexConversionsHelper.byteToHex(toSign), pk2.getEncPubKey(), null);
//		System.out.println(res.getSignature());
//		System.out.println(signed.getPascSignature());
//		client.verifySign(HexConversionsHelper.byteToHex(toSign), pk2.getEncPubKey(), signed.getPascSignature());
//	}
//	
//	@Test
//	public void testSignAndVerifySign() throws NoSuchAlgorithmException
//	{
//		String privateKeyEnc="53616C7465645F5FAB4777157A524C4D7CB71BD05C5A56B6F151ECADA6F3D95AC90FF174BE71666CF40F7EAB6995E75FFEBA1C69D76398BD0EFCE8F61AAB1F46";
//		String pwd ="L1L0kio10";
//		String privateKey=OpenSslAes.decrypt(pwd, privateKeyEnc);
//		PascPrivateKey key = PascPrivateKey.fromPrivateKey(privateKey.substring(8), KeyType.fromValue(HexConversionsHelper.hexBigEndian2Int(privateKey.substring(0,4))));
//		PublicKey publicKey=client.decodePubKey(key.getPublicKey().getEncPubKey(), null); //bouncyCastleKeyHelper
//		assertEquals(key.getPublicKey().getBase58PubKey(),publicKey.getBase58PubKey());
//		
//		String toSign="This is the text that will be signed";
//		
//		String hex=HexConversionsHelper.getSHA256(toSign);
//		
// 		SignResult res= client.signMessage(hex, publicKey.getEncPubKey(), null);
// 		assertTrue(res!=null);
// 		
// 		PascPrivateKey.SignResult signed = key.sign(toSign.getBytes()); // .sign(toSign.getBytes(), bouncyCastleKeyHelper);
// 		
//		System.out.println("Pascal signature:"+res.getSignature());
//		System.out.println("Java signature:"+signed.getStringSignature());
//		System.out.println("Verifying Java signature:");
//		
//		System.out.println(signed.getR().toString(16).toUpperCase());
//		System.out.println(signed.getS().toString(16).toUpperCase());
//		System.out.println(publicKey.verify(signed.getSignature(), toSign.getBytes()));
//		PascPrivateKey.SignResult jSignature=new PascPrivateKey.SignResult(PascPrivateKey.getDerEncodedSignature(signed.getR(), signed.getS()));
//		System.out.println(publicKey.verify(jSignature.getSignature(), toSign.getBytes()));
//		
//		System.out.println("Verifying Pascal signature:");
//		res = client.verifySign(hex, publicKey.getEncPubKey(), res.getSignature());
//		System.out.println(String.format("Digest: %s, Signature: %s, EncPubKey: %s", res.getDigest(),res.getSignature(),res.getEncPubkey()));
//		
//		String cR="B02E85DB1363771354DB67263915E049E22A713EA7B932AF11FA833D623C5A35";
//		String cS="96198EB4A99EB727D0DD31FCBBB0F38218A899675E765ABE7793E03787FCA7A9";
//		BigInteger R=new BigInteger(cR,16);
//		BigInteger S=new BigInteger(cS,16);
//		
//		String cDerSignature="3046022100b02e85db1363771354db67263915e049e22a713ea7b932af11fa833d623c5a3502210096198eb4a99eb727d0dd31fcbbb0f38218a899675e765abe7793e03787fca7a9";
//		
//		assertEquals(HexConversionsHelper.byteToHex(PascPrivateKey.getDerEncodedSignature(R, S)),cDerSignature);
//		
//		System.out.println("Verifying C signature in java:");
//		PascPrivateKey.SignResult cSignature=new PascPrivateKey.SignResult(HexConversionsHelper.decodeStr2Hex(cDerSignature));
//		System.out.println("R and S from signature in java:");
//		System.out.println(cSignature.getStringR());
//		System.out.println(cSignature.getStringS());
//		System.out.println(publicKey.verify(cSignature.getSignature(), toSign.getBytes()));
//		
//		System.out.println("Verifying C signature in pascal:");
//		res = client.verifySign(hex, publicKey.getEncPubKey(), cSignature.getPascSignature());
//		System.out.println(String.format("Digest: %s, Signature: %s, EncPubKey: %s", res.getDigest(),res.getSignature(),res.getEncPubkey()));
//		System.out.println("Verifying Java signature:");
//		res = client.verifySign(hex, publicKey.getEncPubKey(), signed.getPascSignature());
//		System.out.println(String.format("Digest: %s, Signature: %s, EncPubKey: %s", res.getDigest(),res.getSignature(),res.getEncPubkey()));
//		
//	}
//		
//	@Test
//	public void testSignatureMiniTransfer() {
//		String privateKeyEnc="53616C7465645F5FAB4777157A524C4D7CB71BD05C5A56B6F151ECADA6F3D95AC90FF174BE71666CF40F7EAB6995E75FFEBA1C69D76398BD0EFCE8F61AAB1F46";
//		String pwd ="L1L0kio10";
//		String privateKey=OpenSslAes.decrypt(pwd, privateKeyEnc);
//		PascPrivateKey key = PascPrivateKey.fromPrivateKey(privateKey.substring(8), KeyType.fromValue(HexConversionsHelper.hexBigEndian2Int(privateKey.substring(0,4))));
//		PublicKey publicKey=client.decodePubKey(key.getPublicKey().getEncPubKey(), null);
//		assertEquals(key.getPublicKey().getBase58PubKey(),publicKey.getBase58PubKey());
//		List<Account> result = client.getWalletAccounts(null, key.getPublicKey().getBase58PubKey(), null, null);
//		for(Account account:result)
//		{
//			System.out.println(String.format("Account %s has name %s and balance %.4f", account.getAccount(),account.getName(),account.getBalance()));
//		}
//		assertTrue(result.size()>0); 
//		
//		Account account = result.get(0);
//		
//		TransferOperation operation = new TransferOperation(account.getAccount(),3532,account.getnOperation()+1, 0.0001, 0.0001, "TEST".getBytes(), PayLoadEncryptionMethod.NONE, null);
//		byte[] opDigest=operation.generateOpDigest(4.0f);
//		PascPrivateKey.SignResult res=key.sign(opDigest);
//
//		String rawOps=operation.getRawOperations(res.getStringR(), res.getStringS());
//		System.out.println("R:"+res.getStringR());
//		System.out.println("S:"+res.getStringS());
//		System.out.println(rawOps);
//		assertTrue( key.getPublicKey().verify(res.getSignature(),opDigest));
//		List<Operation> ops=client.operationsInfo(rawOps);
//		assertTrue(ops!=null && ops.size()>0); 
//		//List<Operation> ops2=client.executeOperations(rawOps);
//		//assertTrue(ops2!=null && ops2.size()>0 && ops2.get(0).getValid()==null); 
//	}
//	
//}
