package com.github.davidbolet.jpascalcoin.crypto.model;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.security.Security;
import java.util.Properties;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.davidbolet.jpascalcoin.api.client.PascalCoinClient;
import com.github.davidbolet.jpascalcoin.api.client.PascalCoinClientImpl;
import com.github.davidbolet.jpascalcoin.api.constants.PascalCoinConstants;
import com.github.davidbolet.jpascalcoin.common.helper.HexConversionsHelper;
import com.github.davidbolet.jpascalcoin.common.model.KeyType;
import com.github.davidbolet.jpascalcoin.common.model.PascPublicKey;
import com.github.davidbolet.jpascalcoin.crypto.helper.EncryptionUtils;

public class EncryptionUtilsTest {
	static Properties props = new Properties();
	
	PascalCoinClient client;
	String password;
	String pwd;
	
	String privateKey1;
	String privateKeyEnc;

	@BeforeClass
	public static void loadCommons() throws IOException {
		String path="test/resources/jPascalcoin-crypto-test.properties";
		
		InputStream is = ClassLoader.getSystemResourceAsStream(path);
		if (is==null) throw new Error("Could not find test/resources/jPascalcoin-crypto-test.properties file");
		props.load(is);
	}
	
	private int getIntProperty(Properties props, String key, int defaultValue) {
		int res=defaultValue;
		try {
			res=Integer.parseInt(props.getProperty(key));
		} catch(NumberFormatException ex) {
			
		} 
		return res;
	}
	
	/**
	 * Initialize all vars here (or will be loaded from test/resources/JPascalcoin-test.properties)
	 */
	@Before
	public void init() //"10.211.55.10"
	{
		//client = new PascalCoinClientImpl(props.getProperty("jPascalcoin.client.ip", "10.211.55.10"),
		client = new PascalCoinClientImpl("10.211.55.10",
			Short.parseShort(props.getProperty("jPascalcoin.client.port", PascalCoinConstants.DEFAULT_MAINNET_RPC_PORT.toString())),
			getIntProperty(props, "jPascalcoin.client.logLevel", 1));

		password=props.getProperty("jPascalcoin.wallet.password");

		privateKey1 = props.getProperty("jPascalcoin.test.privateKey.mainNetwork1");
		privateKeyEnc= props.getProperty("jPascalcoin.test.privateKey.mainNetwork1.encoded");
		pwd=props.getProperty("jPascalcoin.test.privateKey.password");
		//Initially unlock wallet for the test
		client.unlock(password);
		Security.addProvider(new org.spongycastle.jce.provider.BouncyCastleProvider());
	}
	
	
	String text="TEST";
	String textSimpleASCII="This is a text, it includes some numbers, like 1234567890, and symbols, like $\"!'-=+*";
	String textWithAccents="me llamo Pedrín y no soy un malandrín, esto es una gran historia, pero no la vamos a contar hoy, quizá mañana";
	String textRussian="сейчас будем знать, можно ли по-русский писать или нет. Не знаю какая длинность байтов этого текста. Я Хочу чтобы он будет очень длинный, а что после того, как вернуться, будеть ещё понятьно";

	
	@Test
	public void checkEciesDecryption() throws Exception {
		PascPrivateKey pk=PascPrivateKey.generate(KeyType.SECP256K1);
		String b58PubKey=pk.getPublicKey().getBase58PubKey();
		
		PascPublicKey publicKey=client.decodePubKey(null, b58PubKey);
		PascPublicKey key=PascPublicKey.fromB58PubKey(b58PubKey);
		assertEquals(publicKey.getEncPubKey().toUpperCase(),key.getEncPubKey().toUpperCase());
		System.out.println(textWithAccents.length());
		
		String enc1=client.payloadEncrypt(text, "pubkey", null, null, b58PubKey);
		String enc2=client.payloadEncrypt(textSimpleASCII, "pubkey", null, null, b58PubKey);
		String enc3=client.payloadEncrypt(textWithAccents, "pubkey", null, null, b58PubKey);
		String enc4=client.payloadEncrypt(textRussian, "pubkey", null, null, b58PubKey);
		
		String result1=new String(EncryptionUtils.doPascalcoinEciesDecrypt(pk,enc1));
		assertEquals(result1,text);

		String result2=new String(EncryptionUtils.doPascalcoinEciesDecrypt(pk,enc2));
		assertEquals(result2,textSimpleASCII);
		
//		String result33=client.payloadDecrypt(enc3, null).getUnencryptedPayload();
//		assertEquals(result33,textWithAccents);
		String result3=new String(EncryptionUtils.doPascalcoinEciesDecrypt(pk,enc3));
		assertEquals(result3,textWithAccents);
		
		String result4=new String(EncryptionUtils.doPascalcoinEciesDecrypt(pk,enc4));
		assertEquals(result4,textRussian);
	}

	@Test
	public void checkEciesEncryption() throws Exception {
		PascPrivateKey pk=PascPrivateKey.fromPrivateKey(privateKey1, KeyType.SECP256K1);
		
		String b58PubKey=pk.getPublicKey().getBase58PubKey();
		
		PascPublicKey publicKey=client.decodePubKey(null, b58PubKey);
		String enc1=HexConversionsHelper.byteToHex(EncryptionUtils.doPascalcoinEciesEncrypt(publicKey,text)).toUpperCase();
		String enc2=HexConversionsHelper.byteToHex(EncryptionUtils.doPascalcoinEciesEncrypt(publicKey,textSimpleASCII)).toUpperCase();
		String enc3=HexConversionsHelper.byteToHex(EncryptionUtils.doPascalcoinEciesEncrypt(publicKey,textWithAccents)).toUpperCase();
		String enc4=HexConversionsHelper.byteToHex(EncryptionUtils.doPascalcoinEciesEncrypt(publicKey,textRussian)).toUpperCase();
		
		//First test our own decryption
		String resultLocal1=new String(EncryptionUtils.doPascalcoinEciesDecrypt(pk,enc1));
		assertEquals(text,resultLocal1);

		String resultLocal2=new String(EncryptionUtils.doPascalcoinEciesDecrypt(pk,enc2));
		assertEquals(textSimpleASCII,resultLocal2);
		
		String resultLocal3=new String(EncryptionUtils.doPascalcoinEciesDecrypt(pk,enc3));
		assertEquals(textWithAccents,resultLocal3);
		
		String resultLocal4=new String(EncryptionUtils.doPascalcoinEciesDecrypt(pk,enc4));
		assertEquals(textRussian,resultLocal4);
		
		//Second, test against node (private key must exist)
		String resultDecode=client.payloadDecrypt(enc1, null).getUnencryptedPayload();
		assertEquals(text,resultDecode);
		
		String resultDecode2=client.payloadDecrypt(enc2, null).getUnencryptedPayload();
		assertEquals(textSimpleASCII,resultDecode2);
		
		String resultDecode3=client.payloadDecrypt(enc3, null).getUnencryptedPayload();
		assertEquals(textWithAccents,resultDecode3);
		
		String resultDecode4=client.payloadDecrypt(enc4, null).getUnencryptedPayload();
		assertEquals(textRussian,resultDecode4);
		
	}
	

}
