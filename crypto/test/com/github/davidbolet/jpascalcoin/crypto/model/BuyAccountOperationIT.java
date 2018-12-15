package com.github.davidbolet.jpascalcoin.crypto.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.davidbolet.jpascalcoin.api.client.PascalCoinClient;
import com.github.davidbolet.jpascalcoin.api.client.PascalCoinClientImpl;
import com.github.davidbolet.jpascalcoin.api.constants.PascalCoinConstants;
import com.github.davidbolet.jpascalcoin.api.model.Account;
import com.github.davidbolet.jpascalcoin.api.model.Operation;
import com.github.davidbolet.jpascalcoin.common.helper.HexConversionsHelper;
import com.github.davidbolet.jpascalcoin.common.helper.OpenSslAes;
import com.github.davidbolet.jpascalcoin.common.model.KeyType;
import com.github.davidbolet.jpascalcoin.common.model.PascPublicKey;
import com.github.davidbolet.jpascalcoin.common.model.PayLoadEncryptionMethod;

public class BuyAccountOperationIT {
	
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
		pwd=props.getProperty("jPascalcoin.test.privateKey.password");
		privateKeyEnc=props.getProperty("jPascalcoin.test.privateKey.mainNetwork3.encoded");
		
		//Initially unlock wallet,
		client.unlock(password);
	}
	
	@Test
	public void create_and_execute_buy_account() {
		String privateKey=OpenSslAes.decrypt(pwd, privateKeyEnc);
		PascPrivateKey key = PascPrivateKey.fromPrivateKey(privateKey.substring(8), KeyType.fromValue(HexConversionsHelper.hexBigEndian2Int(privateKey.substring(0,4))));
		PascPublicKey publicKey=client.decodePubKey(key.getPublicKey().getEncPubKey(), null);
		assertEquals(key.getPublicKey().getBase58PubKey(),publicKey.getBase58PubKey());
		List<Account> result = client.getWalletAccounts(null, key.getPublicKey().getBase58PubKey(), null, null);
		for(Account account:result)
		{
			System.out.println(String.format("Account %s has name %s and balance %.4f", account.getAccount(),account.getName(),account.getBalance()));
		}
		assertTrue(result.size()>0); 
		
		Account account = result.get(7);
		System.out.println(String.format("Account %s has name %s and balance %.4f", account.getAccount(),account.getName(),account.getBalance()));
		Account payer=client.getAccount(126682);
		
		BuyAccountOperation operation = new BuyAccountOperation( payer.getAccount(),publicKey,490921, 214322,null, payer.getnOperation()+1, 0.1,0.0001, "TEST".getBytes(), PayLoadEncryptionMethod.NONE, null);
		
		byte[] opDigest=operation.generateOpDigest(4.0f);
		OfflineSignResult res=key.sign(opDigest);
		System.out.println("Digest to sign:"+operation.getFinalHash()); 
		
		String rawOps=operation.getRawOperations(res.getStringR().toUpperCase(), res.getStringS().toUpperCase());
		System.out.println("R:"+res.getStringR());
		System.out.println("S:"+res.getStringS());
		System.out.println(rawOps);
		
		assertTrue( key.getPublicKey().verify(res.getByteSignature(),opDigest));
		
		List<Operation> ops=client.operationsInfo(rawOps);
		assertTrue(ops!=null && ops.size()>0); 
		List<Operation> ops2=client.executeOperations(rawOps);
		assertTrue(ops2!=null && ops2.size()>0 && ops2.get(0).getValid()==null); 
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
