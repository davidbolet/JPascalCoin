package com.github.davidbolet.jpascalcoin.crypto.model;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.davidbolet.jpascalcoin.api.client.PascalCoinClient;
import com.github.davidbolet.jpascalcoin.api.client.PascalCoinClientImpl;
import com.github.davidbolet.jpascalcoin.api.constants.PascalCoinConstants;
import com.github.davidbolet.jpascalcoin.common.exception.UnsupportedKeyTypeException;
import com.github.davidbolet.jpascalcoin.common.helper.HexConversionsHelper;
import com.github.davidbolet.jpascalcoin.common.helper.OpenSslAes;
import com.github.davidbolet.jpascalcoin.common.model.KeyType;
import com.github.davidbolet.jpascalcoin.common.model.PascPublicKey;

public class PascPrivateKeyShould {

static Properties props = new Properties();
	
	PascalCoinClient client;
	String password;
	
	String privateKey1;
	String publicKey1;
	String privateKey2;
	String privateKey2Encoded;

	@BeforeClass
	public static void loadCommons() throws IOException {
		String path="test/resources/jPascalcoin-crypto-test.properties";
		
		InputStream is = ClassLoader.getSystemResourceAsStream(path);
		if (is==null) throw new Error("Could not find jPascalcoin-test.properties file");
		props.load(is);
	}
	
	
	/**
	 * Initialize all vars here (or will be loaded from test/resources/JPascalcoin-test.properties)
	 */
	@Before
	public void init() 
	{
		client = new PascalCoinClientImpl(props.getProperty("jPascalcoin.client.ip", "10.211.55.10"),
			Short.parseShort(props.getProperty("jPascalcoin.client.port", PascalCoinConstants.DEFAULT_MAINNET_RPC_PORT.toString())),
			getIntProperty(props, "jPascalcoin.client.logLevel", 1));

		password=props.getProperty("jPascalcoin.wallet.password");

		privateKey1=props.getProperty("jPascalcoin.test.privateKey.mainNetwork1");
		publicKey1=props.getProperty("jPascalcoin.test.publicKey.mainNetwork1");
		
		privateKey2=props.getProperty("jPascalcoin.test.privateKey.mainNetwork2");
		privateKey2Encoded=props.getProperty("jPascalcoin.test.privateKey.mainNetwork2.encoded");
		//Initially unlock wallet,
		client.unlock(password);
	}
	
	
	/**
	 * Test key generation. To check the result with the wallet, import the generated private key and check Base58PubKey
	 * @throws UnsupportedKeyTypeException if given key is different from SECP256K1
	 */
	@Test
	public void generate_private_and_public_keys() throws UnsupportedKeyTypeException  {

		PascPrivateKey key = PascPrivateKey.generate(KeyType.SECP256K1);
		System.out.println("private key: "+key.getPrivateKey());
		System.out.println("encPubKey: "+key.getPublicKey().getEncPubKey());
		System.out.println("Base58PubKey: "+key.getPublicKey().getBase58PubKey());
		
		PascPublicKey pk2=client.decodePubKey(null, key.getPublicKey().getBase58PubKey());	
		System.out.println("encPubKey: "+pk2.getEncPubKey());
		System.out.println("Base58PubKey: "+pk2.getBase58PubKey());
		
		assertEquals(pk2.getEncPubKey(),key.getPublicKey().getEncPubKey());
		assertEquals(pk2.getBase58PubKey(),key.getPublicKey().getBase58PubKey());
	}
	
	
	/**
	 * Test import existing private key in RAW format
	 * @throws UnsupportedKeyTypeException if given key is different from SECP256K1
	 */
	@Test
	public void import_raw_private_key() throws Exception {
		String pk1=privateKey1;
		PascPrivateKey key1 = PascPrivateKey.fromPrivateKey(pk1, KeyType.SECP256K1);
		System.out.println("private key: "+key1.getPrivateKey());
		System.out.println("encPubKey: "+key1.getPublicKey().getEncPubKey());
		System.out.println("Base58PubKey: "+key1.getPublicKey().getBase58PubKey());
		
		String pub1=publicKey1;
		PascPublicKey public1=client.decodePubKey(null, pub1);
		assertEquals(key1.getPublicKey().getEncPubKey(),public1.getEncPubKey());
		assertEquals(key1.getPublicKey().getBase58PubKey(),public1.getBase58PubKey());
	}
	
	/**
	 * Test import existing private key in encrypted format
	 * @throws UnsupportedKeyTypeException if given key is different from SECP256K1
	 */
	@Test
	public void import_encrypted_private_key() throws Exception {
		

		//This is a Private Key previously imported on the wallet
		String privateKey=privateKey2;
		//This is the wallet's export from previous private key with password "12345678" 
		String pk=privateKey2Encoded;		
		
		//Encryption TEST
		String encrypt=OpenSslAes.encrypt("12345678", privateKey);
		//assertEquals(encrypt,pk);
		//Decryption TEST
		String decrypt=OpenSslAes.decrypt("12345678", pk);
		String decrypt2=OpenSslAes.decrypt("12345678", encrypt);
		
		assertEquals(privateKey, decrypt.substring(8));
		assertEquals(privateKey, decrypt2);
		KeyType type=KeyType.fromValue(HexConversionsHelper.hexBigEndian2Int(decrypt.substring(0,4)));
		//Private Key import TEST
		PascPrivateKey key = PascPrivateKey.fromPrivateKey(privateKey, type);
		System.out.println("private key: "+key.getPrivateKey());
		System.out.println("encPubKey: "+key.getPublicKey().getEncPubKey());
		System.out.println("Base58PubKey: "+key.getPublicKey().getBase58PubKey());
		
		//Noew Test against wallet's results
		PascPublicKey pk2=client.decodePubKey(null, key.getPublicKey().getBase58PubKey());	
		System.out.println("encPubKey: "+pk2.getEncPubKey());
		System.out.println("Base58PubKey: "+pk2.getBase58PubKey());
		//Must fit
		assertEquals(pk2.getEncPubKey(),key.getPublicKey().getEncPubKey());
		assertEquals(pk2.getBase58PubKey(),key.getPublicKey().getBase58PubKey());
	}
	
	
	private int getIntProperty(Properties props, String key, int defaultValue) {
		int res=defaultValue;
		try {
			res=Integer.parseInt(props.getProperty(key));
		} catch(NumberFormatException ex) {
			
		} 
		return res;
	}
}
