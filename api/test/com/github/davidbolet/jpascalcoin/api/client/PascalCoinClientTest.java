package com.github.davidbolet.jpascalcoin.api.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.github.davidbolet.jpascalcoin.api.constants.PascalCoinConstants;
import com.github.davidbolet.jpascalcoin.api.model.Account;
import com.github.davidbolet.jpascalcoin.api.model.AccountKey;
import com.github.davidbolet.jpascalcoin.api.model.Block;
import com.github.davidbolet.jpascalcoin.api.model.Connection;
import com.github.davidbolet.jpascalcoin.api.model.DecodeOpHashResult;
import com.github.davidbolet.jpascalcoin.api.model.DecryptedPayload;
import com.github.davidbolet.jpascalcoin.common.model.KeyType;
import com.github.davidbolet.jpascalcoin.api.model.MultiOperation;
import com.github.davidbolet.jpascalcoin.api.model.NodeServer;
import com.github.davidbolet.jpascalcoin.api.model.NodeStatus;
import com.github.davidbolet.jpascalcoin.api.model.OpChanger;
import com.github.davidbolet.jpascalcoin.api.model.OpReceiver;
import com.github.davidbolet.jpascalcoin.api.model.OpSender;
import com.github.davidbolet.jpascalcoin.api.model.Operation;
import com.github.davidbolet.jpascalcoin.api.model.RawOperation;

import com.github.davidbolet.jpascalcoin.common.helper.OpenSslAes;
import com.github.davidbolet.jpascalcoin.common.helper.HexConversionsHelper;
import com.github.davidbolet.jpascalcoin.common.model.PayLoadEncryptionMethod;
import com.github.davidbolet.jpascalcoin.common.model.PascPublicKey;
import com.github.davidbolet.jpascalcoin.common.model.SignResult;


/**
 * Basic tests that show how client work
 * 
 * @author davidbolet
 *
 */
public class PascalCoinClientTest {
	
	static Properties props = new Properties();
	PascalCoinClient client;
	String encPubKey,b58PubKey, b58PubKeyOtherWallet;
	Integer accountId, account2Id, account3Id, account4Id;
	String password;
	String b58PubKey2, b58BuyKey;

	
	@BeforeClass
	public static void loadCommons() throws IOException {
		String path="test/resources/jPascalcoin-test.properties";
		
		PascalCoinClientTest.class.getClassLoader();
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

		b58PubKeyOtherWallet="<Another public key>";
		b58BuyKey="<Another public key used on buyAccount>";
		
		//Create the client object setting values from properties file
		client = new PascalCoinClientImpl(props.getProperty("jPascalcoin.client.ip", "10.211.55.10"),
							Short.parseShort(props.getProperty("jPascalcoin.client.port", PascalCoinConstants.DEFAULT_MAINNET_RPC_PORT.toString())),
							getIntProperty(props, "jPascalcoin.client.logLevel", 1));
		
		password=props.getProperty("jPascalcoin.wallet.password");
		
		b58PubKey=props.getProperty("jPascalcoin.client.publicKey.b58");
		encPubKey=props.getProperty("jPascalcoin.client.publicKey.encoded");
				
		accountId = getIntProperty(props, "jPascalcoin.test.accountId", 126682); //An account id
		account2Id = getIntProperty(props, "jPascalcoin.test.account2Id",381309); //An account id
		account3Id = getIntProperty(props, "jPascalcoin.test.account3Id",381403); //An account id
		account4Id = getIntProperty(props, "jPascalcoin.test.account4Id",381404); //An account id
		
		//Initially unlock wallet,
		client.unlock(password);
	}
	
	@Test
	public void testAddNode()
	{
		Integer res = client.addNode(props.getProperty("jPascalcoin.test.addNode.nodeList"));
		System.out.println("Result:"+res);
		assertTrue(res>0);
	}
	
	/**
	 * Tests getAccount function and related entity Account
	 */
	@Test
	public void testGetAccount()
	{
		Account account = client.getAccount(accountId);
		System.out.println(String.format("Account %s has name %s and balance %.4f. UpdatedB: %d, Type: %d, State: %s, PrivateSale: %s, Operations %d\n NewEncPubKey: %s, LockedUntilBlock %d,\n EncPubKey %s", account.getAccount(),account.getName(),account.getBalance(), account.getUpdatedB(), account.getType(), account.getState().getValue(), account.getPrivateSale(), account.getnOperation(), account.getNewEncPubkey(), account.getLockedUntilBlock(), account.getEncPubkey()));
		assertEquals(account.getAccount(), accountId); 
		account = client.getAccount(account2Id);
		System.out.println(String.format("Account %s has name %s and balance %.4f. UpdatedB: %d, Type: %d, State: %s, PrivateSale: %s, Operations %d\n NewEncPubKey: %s, LockedUntilBlock %d,\n EncPubKey %s Seller account: %d Price: %.4f", account.getAccount(),account.getName(),account.getBalance(), account.getUpdatedB(), account.getType(), account.getState().getValue(), account.getPrivateSale(), account.getnOperation(), account.getNewEncPubkey(), account.getLockedUntilBlock(), account.getEncPubkey(), account.getSellerAccount(), account.getPrice()));
		assertTrue(account!=null);
	}
	
	/**
	 * Tests getAccountsCount function
	 */
	@Test
	public void testGetAccountsCount()
	{	
		Integer result = client.getWalletAccountsCount(null, b58PubKey2);
		System.out.println(String.format("GetAccountsCount %d", result));
		assertTrue(result>0); 
		Integer result2 = client.getWalletAccountsCount(null, null);
		System.out.println(String.format("GetAccountsCount %d", result2));
		assertTrue(result2>0); 
	}
	
	/**
	 * Tests listAccounts function 
	 */
	@Test
	public void testListAccounts()
	{	
		List<Account> result = client.getWalletAccounts(null, null, null, null);
		System.out.println(String.format("GetAccounts size %d", result.size()));
		for(Account account:result)
		{
			System.out.println(String.format("Account %s has name %s and balance %.4f", account.getAccount(),account.getName(),account.getBalance()));
		}
		assertTrue(result.size()>0); 
	}

	/**
	 * Tests findAccounts function with modifications introduced on v3.0.2
	 */
	@Test
	public void testFindAccounts()
	{	
		String search="david";
		List<Account> result = client.findAccounts(search, null, 0, 10);		
		System.out.println(String.format("Finding account with name '%s' (Exact match). Size %d", search,result.size()));
		for(Account account:result)
		{
			System.out.println(String.format("Account %s has name %s and balance %.4f. State is %s", account.getAccount(),account.getName(),account.getBalance(), account.getState()));
		}
		assertTrue(result!=null);
		result = client.findAccounts(search, false,null, null,null,null,null,null,  0, 10);
		System.out.println(String.format("Finding accounts containing '%s' on name. Size %d", search,result.size()));
		for(Account account:result)
		{
			System.out.println(String.format("Account %s has name %s and balance %.4f. State is %s", account.getAccount(),account.getName(),account.getBalance(), account.getState()));
		}
//		int type=2;
//		result = client.findAccounts(null, type, null, null);
		result = client.findAccounts(null, null,null, true,null,null,null,null,  0, 10);
//		System.out.println(String.format("Finding accounts for with type=%d. Size %d", type, result.size()));
		for(Account account:result)
		{
			System.out.println(String.format("Finding accounts for sale: %s name %s and price %.4f. State is %s", account.getAccount(),account.getName(),account.getPrice(), account.getState()));
		}
		result = client.findAccounts(null, null, null,null,null,null,100.0,1000.0,  0, 10);
//		System.out.println(String.format("Finding accounts for with type=%d. Size %d", type, result.size()));
		for(Account account:result)
		{
			System.out.println(String.format("Finding Account %d has balance between : %.4f and %.4f. Account balance %.4f State is %s", account.getAccount(),100.0,1000.0,account.getBalance(), account.getState()));
		}
		result = client.findAccounts(null, null,null, null,null,null,10000.0,null,  0, 10);
		for(Account account:result)
		{
			System.out.println(String.format("Finding Account %d balance greater than : %.4f. Account balance %.4f State is %s", account.getAccount(),10000.0,account.getBalance(), account.getState()));
		}
		result = client.findAccounts(null, null,null, null,null,b58PubKey,null,null,  0, 10);
		for(Account account:result)
		{
			System.out.println(String.format("Finding Account %d balance greater than : %.4f. Account balance %.4f State is %s", account.getAccount(),10000.0,account.getBalance(), account.getState()));
		}
	}
	
	/**
	 * Tests getWalletsPubKeys function and related entity PublicKey
	 */
	@Test
	public void testGetWalletPubKeys()
	{	
		List<PascPublicKey> result = client.getWalletPubKeys(0, -1);
		System.out.println(String.format("getWalletPubKeys size %d", result.size()));
		for(PascPublicKey pk:result)
		{
			System.out.println(String.format("PublicKey b58: %s enc: %s keyType: %s, Name: %s, X=%s, Y=%s", pk.getBase58PubKey(), pk.getEncPubKey(), pk.getKeyType(), pk.getName(), pk.getX(), pk.getY()));
		}
		assertTrue(result.size()>0); 
	}
	
	/**
	 * Tests getWalletPubKey
	 */
	@Test
	public void testGetWalletPubKey()
	{	
		PascPublicKey pk = client.getWalletPubKey(encPubKey, null);		
		System.out.println(String.format("PublicKey b58: %s enc: %s keyType: %s, Name: %s, X=%s, Y=%s", pk.getBase58PubKey(), pk.getEncPubKey(), pk.getKeyType(), pk.getName(), pk.getX(), pk.getY()));
		assertTrue(pk!=null); 
		PascPublicKey pk2 = client.getWalletPubKey(null,"3GhhbonyDRgQbVa8feSo11X7waLeSn41gG6eKoo3D6ueM8Jj2h7TFM2qCrG4HYdPdTAZE5qx84hHt68oCmh1h4xS92TScH3rZVqsaz");
		assertTrue(pk2!=null); 
	}

	/**
	 * Tests getWalletCoins
	 */
	@Test
	public void testGetWalletCoins()
	{	
		Double coins = client.getWalletCoins(null, null);
		
		System.out.println(String.format("Wallet coins %.4f", coins));
		
		assertTrue(coins!=null); 
	}
	
	/**
	 * Tests getBlockCount, getBlock and getBlocks
	 */
	@Test
	public void testBlocks()
	{
		Integer blockCount = client.getBlockCount();
		System.out.println(String.format("Block count: %d", blockCount));
		assertTrue(blockCount>0);
		Block last = client.getBlock(blockCount-1);
		System.out.println(String.format("AvailableVersion %d, Block %d, CompaqTarget %d, EncPubKey %s\n Fee %.4f, Last50HashRateKhs %d, Maturation %d,Nonce %d,OperationCount %d, OperationsHash %s,\nPayload %s\n, ProofOfWork %s,\nReward: %.4f, SafeBoxHash %s, Timestamp: %d, Version :%d", last.getAvailableVersion(),last.getBlock(),last.getCompactTarget(),last.getEncPubKey(),last.getFee(),last.getLast50HashRateKhs(), last.getMaturation(), last.getNonce(), last.getOperationCount(),last.getOperationsHash(),last.getPayload(),last.getProofOfWork(),last.getReward(), last.getSafeBoxHash(), last.getTimestamp(),last.getVersion()));
		//System.out.println(String.format("Retrieving blocks from %d to %d", blockCount-10, blockCount-2));// blockCount-10, blockCount-2));
		//List<Block> blocks = client.getBlocks(null, blockCount-10, blockCount-2);
		System.out.println(String.format("Retrieving last 3 blocks"));
		List<Block> blocks = client.getBlocks(3, null,null);
		assertTrue(blocks!=null && blocks.size()>0 && blocks.get(0).getBlock().equals(last.getBlock()));
	}
	
	/**
	 * Tests getBlockOperation and getBlockOperations, and Operation entity
	 */
	@Test
	public void testBlockOperations()
	{
		Integer blockCount = client.getBlockCount();
		Block last = client.getBlock(blockCount-1);
		List<Operation> operations = client.getBlockOperations(last.getBlock(), 0, last.getOperationCount());
		if (last.getOperationCount()==0) return;
		for(Operation op: operations)
		{
			System.out.println(String.format("Operation Hash: %s\nOperation Type: %s(%s),Subtype: %s, Timestamp: %d\nAccount %d Account sender %d Balance: %.4f, Account dest: %d, Amount: %.4f, Block: %d, Fee:%.4f\nErrors %s, OpHash %s,\n Payload %s, Maturation %d, OperationBlock %d, V1Ophash %s\n,Valid %s ", op.getOpHash(), op.getType(),op.getTypeDescriptor(),op.getSubType(), op.getTime(), op.getAccount(),op.getSenderAccount(), op.getBalance(), op.getDestAccount(), op.getAmount(), op.getBlock(), op.getFee(), op.getErrors(),op.getOpHash(), op.getPayLoad(),op.getMaturation(), op.getOperationBlock(), op.getV1Ophash(), op.getValid() ));
		}
		Operation op = client.getBlockOperation(last.getBlock(),last.getOperationCount()-1);
		System.out.println(String.format("Operation Hash: %s\nOperation Type: %s(%s),Subtype: %s, Timestamp: %d\nAccount %d Account sender %d Balance: %.4f, Account dest: %d, Amount: %.4f, Block: %d, Fee:%.4f\nErrors %s, OpHash %s,\n Payload %s, Maturation %d, OperationBlock %d, V1Ophash %s\n,Valid %s ", op.getOpHash(), op.getType(),op.getTypeDescriptor(),op.getSubType(), op.getTime(), op.getAccount(),op.getSenderAccount(), op.getBalance(), op.getDestAccount(), op.getAmount(), op.getBlock(), op.getFee(), op.getErrors(),op.getOpHash(), op.getPayLoad(),op.getMaturation(), op.getOperationBlock(), op.getV1Ophash(), op.getValid() ));
		assertTrue(op.getOperationBlock().equals(operations.get(last.getOperationCount()-1).getOperationBlock()));
	}
	
	/**
	 * Tests getAccountOperation and Operation entity
	 */
	@Test
	public void testAccountOperations()
	{
		List<Operation> operations = client.getAccountOperations(account2Id, null, null, null);
		for(Operation op: operations)
		{
			System.out.println(String.format("Operation Hash: %s\nOperation Type: %s(%s),Subtype: %s, Timestamp: %d\nAccount %d Account sender %d Balance: %.4f, Account dest: %d, Amount: %.4f, Block: %d, Fee:%.4f\nErrors %s, OpHash %s,\n Payload %s, Maturation %d, OperationBlock %d, V1Ophash %s\n,Valid %s ", op.getOpHash(), op.getType(),op.getTypeDescriptor(),op.getSubType(), op.getTime(), op.getAccount(),op.getSenderAccount(), op.getBalance(), op.getDestAccount(), op.getAmount(), op.getBlock(), op.getFee(), op.getErrors(),op.getOpHash(), op.getPayLoad(),op.getMaturation(), op.getOperationBlock(), op.getV1Ophash(), op.getValid() ));
		}
		assertTrue(operations!=null);
	}	
	
	/**
	 * Tests getPendings() and Operation entity
	 */
	@Test
	public void testPendings()
	{
		Integer count= client.getPendingsCount();
		System.out.print(String.format("Number of pending operations: %d\n",count));
		List<Operation> operations = client.getPendings();
		for(Operation op: operations)
		{
			System.out.println(String.format("Operation V2 fields: OpHash: %s\nOperation Type: %s(%s),Subtype: %s, Timestamp: %d\nAccount %d Account sender %d Balance: %.4f, Account dest: %d, Amount: %.4f, Block: %d, Fee:%.4f\nErrors %s, OpHash %s,\n Payload %s, Maturation %d, OperationBlock %d, V1Ophash %s\n,Valid %s ", op.getOpHash(), op.getType(),op.getTypeDescriptor(),op.getSubType(), op.getTime(), op.getAccount(),op.getSenderAccount(), op.getBalance(), op.getDestAccount(), op.getAmount(), op.getBlock(), op.getFee(), op.getErrors(),op.getOpHash(), op.getPayLoad(),op.getMaturation(), op.getOperationBlock(), op.getV1Ophash(), op.getValid() ));
		}
		assertTrue(operations!=null);
	}	

	
	/**
	 * Tests findNOperation
	 */
	@Test
	public void testFindNOperation()
	{
		Operation op = client.findNOperation(account2Id,4);
		System.out.println(String.format("Operation Hash: %s\nOperation Type: %s(%s),Subtype: %s, Timestamp: %d\nAccount %d Account sender %d Balance: %.4f, Account dest: %d, Amount: %.4f, Block: %d, Fee:%.4f\nErrors %s, OpHash %s,\n Payload %s, Maturation %d, OperationBlock %d, V1Ophash %s\n,Valid %s ", op.getOpHash(), op.getType(),op.getTypeDescriptor(),op.getSubType(), op.getTime(), op.getAccount(),op.getSenderAccount(), op.getBalance(), op.getDestAccount(), op.getAmount(), op.getBlock(), op.getFee(), op.getErrors(),op.getOpHash(), op.getPayLoad(),op.getMaturation(), op.getOperationBlock(), op.getV1Ophash(), op.getValid() ));
		assertTrue(op!=null);
	}
	
	/**
	 * Tests findNOperations
	 */
	@Test
	public void testFindNOperations()
	{
		List<Operation> ops = client.findNOperations(account2Id, 4, 10, 0);
		for(Operation op:ops)
		{
			System.out.println(String.format("Operation Hash: %s\nOperation Type: %s(%s),Subtype: %s, Timestamp: %d\nAccount %d Account sender %d Balance: %.4f, Account dest: %d, Amount: %.4f, Block: %d, Fee:%.4f\nErrors %s, OpHash %s,\n Payload %s, Maturation %d, OperationBlock %d, V1Ophash %s\n,Valid %s ", op.getOpHash(), op.getType(),op.getTypeDescriptor(),op.getSubType(), op.getTime(), op.getAccount(),op.getSenderAccount(), op.getBalance(), op.getDestAccount(), op.getAmount(), op.getBlock(), op.getFee(), op.getErrors(),op.getOpHash(), op.getPayLoad(),op.getMaturation(), op.getOperationBlock(), op.getV1Ophash(), op.getValid() ));
		}
		assertTrue(ops!=null);
	}
	
	/**
	 * Tests findOperation
	 */
	@Test
	public void testFindOperation()
	{
		Integer blockCount = client.getBlockCount();
		Block last = client.getBlock(blockCount-1);
		if (last.getOperationCount()==0) return;
		Operation op = client.getBlockOperation(last.getBlock(),last.getOperationCount()-1);
		
		Operation op2 = client.findOperation(op.getOpHash());
		System.out.println(String.format("Operation Hash: %s\nOperation Type: %s(%s),Subtype: %s, Timestamp: %d\nAccount %d Account sender %d Balance: %.4f, Account dest: %d, Amount: %.4f, Block: %d, Fee:%.4f\nErrors %s, OpHash %s,\n Payload %s, Maturation %d, OperationBlock %d, V1Ophash %s\n,Valid %s ", op.getOpHash(), op.getType(),op.getTypeDescriptor(),op.getSubType(), op.getTime(), op.getAccount(),op.getSenderAccount(), op.getBalance(), op.getDestAccount(), op.getAmount(), op.getBlock(), op.getFee(), op.getErrors(),op.getOpHash(), op.getPayLoad(),op.getMaturation(), op.getOperationBlock(), op.getV1Ophash(), op.getValid() ));
		assertTrue(op.equals(op2));
	}
	
	/**
	 * Tests findOperation
	 */
	@Test
	public void testDecodeOpHash()
	{
		DecodeOpHashResult res = client.decodeOpHash("314B02007DD1050003000000D3567A59AF2E1531DC384892764DB7C51CB7CAC9");
		System.out.println(String.format("Block: %s\nAccount: %s,NOperation: %s, md160hash: %s\n", res.getBlock(),res.getAccount(),res.getnOperation(),res.getMd160Hash()));
		assertTrue(res!=null);
	}
	
	
	/**
	 * Test changeAccountInfo - name
	 * Configure manually and remove the Ignore statement if you want to try it
	 */
	@Test
	@Ignore
	public void testChangeAccountInfo()
	{
		Operation op = client.changeAccountInfo(601640, 126682, null, null, "test.david",null, 0.0001, "TEST".getBytes() , PayLoadEncryptionMethod.NONE, null); //"Payload aes".getBytes() , PayLoadEncryptionMethod.AES, "123456"
		System.out.println(String.format("Operation Hash: %s\nOperation Type: %s(%s),Subtype: %s, Timestamp: %d\nAccount %d Account sender %d Balance: %.4f, Account dest: %d, Amount: %.4f, Block: %d, Fee:%.4f\nErrors %s, OpHash %s,\n Payload %s, Maturation %d, OperationBlock %d, V1Ophash %s\n,Valid %s ", op.getOpHash(), op.getType(),op.getTypeDescriptor(),op.getSubType(), op.getTime(), op.getAccount(),op.getSenderAccount(), op.getBalance(), op.getDestAccount(), op.getAmount(), op.getBlock(), op.getFee(), op.getErrors(),op.getOpHash(), op.getPayLoad(),op.getMaturation(), op.getOperationBlock(), op.getV1Ophash(), op.getValid() ));
		assertTrue(op!=null);
	}
	
	
	/**
	 * Test changeAccountInfo -public key
	 * Configure manually and remove the Ignore statement if you want to try it
	 */
	@Test
	@Ignore
	public void testChangeAccountInfoPublicKey()
	{
		Operation op = client.changeAccountInfo(601640, 126682, null, "3GhhbonyDRgQbVa8feSo11X7waLeSn41gG6eKoo3D6ueM8Jj2h7TFM2qCrG4HYdPdTAZE5qx84hHt68oCmh1h4xS92TScH3rZVqsaz",null,null, 0.0001, "TEST".getBytes() , PayLoadEncryptionMethod.NONE, null); //"Payload aes".getBytes() , PayLoadEncryptionMethod.AES, "123456"
		System.out.println(String.format("Operation Hash: %s\nOperation Type: %s(%s),Subtype: %s, Timestamp: %d\nAccount %d Account sender %d Balance: %.4f, Account dest: %d, Amount: %.4f, Block: %d, Fee:%.4f\nErrors %s, OpHash %s,\n Payload %s, Maturation %d, OperationBlock %d, V1Ophash %s\n,Valid %s ", op.getOpHash(), op.getType(),op.getTypeDescriptor(),op.getSubType(), op.getTime(), op.getAccount(),op.getSenderAccount(), op.getBalance(), op.getDestAccount(), op.getAmount(), op.getBlock(), op.getFee(), op.getErrors(),op.getOpHash(), op.getPayLoad(),op.getMaturation(), op.getOperationBlock(), op.getV1Ophash(), op.getValid() ));
		assertTrue(op!=null);
	}
	
	/**
	 * Test sendTo (move funds)
	 * Configure manually and remove the Ignore statement if you want to try it
	 */
	@Test
	@Ignore
	public void testSendTo()
	{
		Operation op = client.sendTo(accountId, account3Id, 0.0300, 0.0001, "mi polla como una olla".getBytes(), PayLoadEncryptionMethod.NONE, null);
		System.out.println(String.format("Operation Hash: %s\nOperation Type: %s(%s),Subtype: %s, Timestamp: %d\nAccount %d Account sender %d Balance: %.4f, Account dest: %d, Amount: %.4f, Block: %d, Fee:%.4f\nErrors %s, OpHash %s,\n Payload %s, Maturation %d, OperationBlock %d, V1Ophash %s\n,Valid %s ", op.getOpHash(), op.getType(),op.getTypeDescriptor(),op.getSubType(), op.getTime(), op.getAccount(),op.getSenderAccount(), op.getBalance(), op.getDestAccount(), op.getAmount(), op.getBlock(), op.getFee(), op.getErrors(),op.getOpHash(), op.getPayLoad(),op.getMaturation(), op.getOperationBlock(), op.getV1Ophash(), op.getValid() ));
		assertTrue(op!=null);
	}
	
	/**
	 * Test changeKey (moves account to another wallet) 
	 * Configure manually and remove the Ignore statement if you want to try it
	 */
	@Test
	@Ignore
	public void testChangeKey()
	{
		client.unlock(password);
		Operation op = client.changeKey(account3Id, account3Id, null, b58PubKey2, 0.0000, "Testing".getBytes() , PayLoadEncryptionMethod.AES, "123456");
		System.out.println(String.format("Operation Hash: %s\nOperation Type: %s(%s),Subtype: %s, Timestamp: %d\nAccount %d Account sender %d Balance: %.4f, Account dest: %d, Amount: %.4f, Block: %d, Fee:%.4f\nErrors %s, OpHash %s,\n Payload %s, Maturation %d, OperationBlock %d, V1Ophash %s\n,Valid %s ", op.getOpHash(), op.getType(),op.getTypeDescriptor(),op.getSubType(), op.getTime(), op.getAccount(),op.getSenderAccount(), op.getBalance(), op.getDestAccount(), op.getAmount(), op.getBlock(), op.getFee(), op.getErrors(),op.getOpHash(), op.getPayLoad(),op.getMaturation(), op.getOperationBlock(), op.getV1Ophash(), op.getValid() ));
		assertTrue(op!=null);
	}	
	
	
	/**
	 * Test changeKeys (moves some accounts to another wallet) 
	 * Configure manually and remove the Ignore statement if you want to try it
	 */
	@Test
	@Ignore
	public void testChangeKeys()
	{
		client.unlock(password);
		List<Operation> ops = client.changeKeys(""+accountId, null, b58PubKey2, 0.0, "Testing".getBytes() , PayLoadEncryptionMethod.AES, "123456");
		for(Operation op:ops)
			System.out.println(String.format("Operation Hash: %s\nOperation Type: %s(%s),Subtype: %s, Timestamp: %d\nAccount %d Account sender %d Balance: %.4f, Account dest: %d, Amount: %.4f, Block: %d, Fee:%.4f\nErrors %s, OpHash %s,\n Payload %s, Maturation %d, OperationBlock %d, V1Ophash %s\n,Valid %s ", op.getOpHash(), op.getType(),op.getTypeDescriptor(),op.getSubType(), op.getTime(), op.getAccount(),op.getSenderAccount(), op.getBalance(), op.getDestAccount(), op.getAmount(), op.getBlock(), op.getFee(), op.getErrors(),op.getOpHash(), op.getPayLoad(),op.getMaturation(), op.getOperationBlock(), op.getV1Ophash(), op.getValid() ));
		assertTrue(ops!=null);
	}	
	
	
	/**
	 * Test listAccountForSale
	 * Configure manually and remove the Ignore statement if you want to try it
	 */
	@Test
	@Ignore
	public void testListAccountForSale()
	{
		Operation op = client.listAccountForSale(606270, 126682, 1000.0000, 126682, null, null, 0, 0.0001, "TEST".getBytes() , PayLoadEncryptionMethod.NONE, "123456");
		System.out.println(String.format("Operation Hash: %s\nOperation Type: %s(%s),Subtype: %s, Timestamp: %d\nAccount %d Account sender %d Balance: %.4f, Account dest: %d, Amount: %.4f, Block: %d, Fee:%.4f\nErrors %s, OpHash %s,\n Payload %s, Maturation %d, OperationBlock %d, V1Ophash %s\n,Valid %s ", op.getOpHash(), op.getType(),op.getTypeDescriptor(),op.getSubType(), op.getTime(), op.getAccount(),op.getSenderAccount(), op.getBalance(), op.getDestAccount(), op.getAmount(), op.getBlock(), op.getFee(), op.getErrors(),op.getOpHash(), op.getPayLoad(),op.getMaturation(), op.getOperationBlock(), op.getV1Ophash(), op.getValid() ));
		assertTrue(op!=null);
	}
	
	/**
	 * Test delistAccountForSale
	 * Configure manually and remove the Ignore statement if you want to try it
	 */
	@Test
	@Ignore
	public void testDelistAccountForSale()
	{
		
		Operation op = client.delistAccountForSale(606282, 126682, 0.0001, "Testing delistAccountForSale".getBytes() , PayLoadEncryptionMethod.NONE, null);
		System.out.println(String.format("Operation Hash: %s\nOperation Type: %s(%s),Subtype: %s, Timestamp: %d\nAccount %d Account sender %d Balance: %.4f, Account dest: %d, Amount: %.4f, Block: %d, Fee:%.4f\nErrors %s, OpHash %s,\n Payload %s, Maturation %d, OperationBlock %d, V1Ophash %s\n,Valid %s ", op.getOpHash(), op.getType(),op.getTypeDescriptor(),op.getSubType(), op.getTime(), op.getAccount(),op.getSenderAccount(), op.getBalance(), op.getDestAccount(), op.getAmount(), op.getBlock(), op.getFee(), op.getErrors(),op.getOpHash(), op.getPayLoad(),op.getMaturation(), op.getOperationBlock(), op.getV1Ophash(), op.getValid() ));
		assertTrue(op!=null);
	}	
	
	
	/**
	 * Test buyAccount
	 * Configure manually and remove the Ignore statement if you want to try it
	 */
	@Test
	@Ignore
	public void testBuyAccount()
	{
		Account a=client.getAccount(126682);
		Operation op = client.buyAccount(126682, 490922, 0.1, 214322, null,a.getEncPubkey(), 0.1, 0.0001, null, null, null);
		System.out.println(String.format("Operation Hash: %s\nOperation Type: %s(%s),Subtype: %s, Timestamp: %d\nAccount %d Account sender %d Balance: %.4f, Account dest: %d, Amount: %.4f, Block: %d, Fee:%.4f\nErrors %s, OpHash %s,\n Payload %s, Maturation %d, OperationBlock %d, V1Ophash %s\n,Valid %s ", op.getOpHash(), op.getType(),op.getTypeDescriptor(),op.getSubType(), op.getTime(), op.getAccount(),op.getSenderAccount(), op.getBalance(), op.getDestAccount(), op.getAmount(), op.getBlock(), op.getFee(), op.getErrors(),op.getOpHash(), op.getPayLoad(),op.getMaturation(), op.getOperationBlock(), op.getV1Ophash(), op.getValid() ));
		assertTrue(op!=null);
	}		
	
	/**
	 * Another test for buyaccount.
	 * Configure manually and remove the Ignore statement if you want to try it
	 */
	@Test
	@Ignore
	public void testBuyAccount2() {
		Account account = client.getAccount(accountId);
		RawOperation buyOperation= client.signBuyAccount(account2Id, 577299, 0.1111, 86926, b58PubKey, null, 0.1111, account.getnOperation()+1, 0.01, "Test signBuyAccount".getBytes() , PayLoadEncryptionMethod.AES, "123456", b58PubKey, null, "");
		System.out.println(String.format("Num operations: %d Total amount: %.4f, Total fee: %.4f, Raw Operations: %s",buyOperation.getNumOperations(), buyOperation.getTotalAmount(), buyOperation.getTotalFee(), buyOperation.getRawOperations()));

		
		List<Operation> operations2 = client.executeOperations(buyOperation.getRawOperations());
		Operation op=operations2.get(0);
		System.out.println(String.format("Operation Hash: %s\nOperation Type: %s(%s),Subtype: %s, Timestamp: %d\nAccount %d Account sender %d Balance: %.4f, Account dest: %d, Amount: %.4f, Block: %d, Fee:%.4f\nErrors %s, OpHash %s,\n Payload %s, Maturation %d, OperationBlock %d, V1Ophash %s\n,Valid %s ", op.getOpHash(), op.getType(),op.getTypeDescriptor(),op.getSubType(), op.getTime(), op.getAccount(),op.getSenderAccount(), op.getBalance(), op.getDestAccount(), op.getAmount(), op.getBlock(), op.getFee(), op.getErrors(),op.getOpHash(), op.getPayLoad(),op.getMaturation(), op.getOperationBlock(), op.getV1Ophash(), op.getValid() ));

	}
	
	
	/**
	 * Test signSendTo, signDelistAccountForSale, operationsInfo and executeOperations functions
	 * Configure manually and remove the Ignore statement if you want to try it
	 */
	@Test
	@Ignore
	public void testSignMethodsAndOperationsInfo()
	{
		Account account = client.getAccount(accountId);

		RawOperation op2= client.signDelistAccountForSale(account2Id, account2Id, account.getnOperation()+1, 0.0, "Test signSend".getBytes() , PayLoadEncryptionMethod.AES, "123456", b58PubKey, null, null);
		System.out.println(String.format("Num operations: %d Total amount: %.4f, Total fee: %.4f, Raw Operations: %s",op2.getNumOperations()+1, op2.getTotalAmount(), op2.getTotalFee(), op2.getRawOperations()));
		RawOperation op3= client.signListAccountForSale(account2Id, account2Id, 1000.0000,accountId, b58PubKey, null, 0,  account.getnOperation()+2, 0.0, "Test signListAccountforSale".getBytes() , PayLoadEncryptionMethod.AES, "123456", b58PubKey, null, op2.getRawOperations());
		System.out.println(String.format("Num operations: %d Total amount: %.4f, Total fee: %.4f, Raw Operations: %s",op3.getNumOperations(), op3.getTotalAmount(), op3.getTotalFee(), op3.getRawOperations()));
		RawOperation op5= client.signBuyAccount(accountId, account2Id, 10.0000, accountId, b58PubKey, null, 0.001, account.getnOperation()+4, 0.0, "Test signBuyAccount".getBytes() , PayLoadEncryptionMethod.AES, "123456", b58PubKey, null, op3.getRawOperations());
		System.out.println(String.format("Num operations: %d Total amount: %.4f, Total fee: %.4f, Raw Operations: %s",op5.getNumOperations(), op5.getTotalAmount(), op5.getTotalFee(), op5.getRawOperations()));

		List<Operation> operations = client.operationsInfo(op5.getRawOperations());
		assertEquals(operations.size(),5);
		for(Operation op: operations)
		{
			System.out.println(String.format("Operation Hash: %s\nOperation Type: %s(%s),Subtype: %s, Timestamp: %d\nAccount %d Account sender %d Balance: %.4f, Account dest: %d, Amount: %.4f, Block: %d, Fee:%.4f\nErrors %s, OpHash %s,\n Payload %s, Maturation %d, OperationBlock %d, V1Ophash %s\n,Valid %s ", op.getOpHash(), op.getType(),op.getTypeDescriptor(),op.getSubType(), op.getTime(), op.getAccount(),op.getSenderAccount(), op.getBalance(), op.getDestAccount(), op.getAmount(), op.getBlock(), op.getFee(), op.getErrors(),op.getOpHash(), op.getPayLoad(),op.getMaturation(), op.getOperationBlock(), op.getV1Ophash(), op.getValid() ));
		}
		List<Operation> operations2 = client.executeOperations(op2.getRawOperations());
		assertEquals(operations.size(),operations2.size());
	}
	
	/**
	 * Tests signMessage and verifySign
	 */
	@Test
	public void testSignAndVerifySign()
	{
		String toSign="This is the text that will be signed";
		String hex=HexConversionsHelper.byteToHex(toSign.getBytes());
 		SignResult res= client.signMessage(hex, encPubKey, null);
		assertTrue(res!=null);
		System.out.println(String.format("Digest: %s, Signature: %s, EncPubKey: %s", res.getDigest(),res.getSignature(),res.getEncPubkey()));
		res = client.verifySign(hex, encPubKey, res.getSignature());
		System.out.println(String.format("Digest: %s, Signature: %s, EncPubKey: %s", res.getDigest(),res.getSignature(),res.getEncPubkey()));
	}
	
	/**
	 * Tests getNodeStatus, and related entities NodeStatus, NodeServer and NetStats
	 */
	@Test
	public void testNodeStatus()
	{	
		NodeStatus status = client.getNodeStatus();
		System.out.println(String.format("Node status: Blocks: %d Locked %s, NetProtocol version %d, net protocol available version: %d, SSL version %s, status descriptor %s, version %s, readydescriptor %s", status.getBlocks(), status.getLocked(), status.getNetProtocol().getVersion(), status.getNetProtocol().getAvailableVersion(),status.getOpenssl() ,status.getStatusDescriptor(), status.getVersion(), status.getReadyDescriptor()));
		for (NodeServer server: status.getNodeServers())
		{
			System.out.println(String.format("Node server: IP %s, port %d, attempts: %d, last connect: %s", server.getIP(), server.getPort(), server.getAttempts(), server.getLastConnect()));
		}
		System.out.println(String.format("Net stats: Active: %d Bytes received %d, Bytes send %d, clients %d, servers %d, serversT %d, total %d, total clients %d, total servers %d", status.getNetStats().getActive(), status.getNetStats().getBytesReceived(), status.getNetStats().getBytesSent(), status.getNetStats().getClients(), status.getNetStats().getServers(), status.getNetStats().getServersT(),status.getNetStats().getTotal(),status.getNetStats().getTotalClients(),status.getNetStats().getTotalServers()));
		
		assert(status!=null); 
	}
	
	/**
	 * Tests payloadDencrypt
	 */
	@Test
	public void testPayloadDecrypt()
	{	
		String crypted = client.payloadEncrypt("Test signBuyAccount", "aes", "123456", null,null);	
		System.out.println(crypted);
		
		DecryptedPayload dp =  client.payloadDecrypt(crypted, new String[] {"123456"});
		System.out.println(String.format("Password: %s,\noriginal payload %s,\nunencrypted payload %s\nEncoded Pub key %s\nResult %s\nPayload HEX %s", dp.getDecryptPassword(), dp.getOriginalPayload(),dp.getUnencryptedPayload(), dp.getEncodedPubKey(), dp.getResult(), dp.getUnencryptedPayloadHex() ));
		assertTrue(dp!=null); 
	}	
	
	
	/**
	 * Tests payloadEncrypt, and payloadDencrypt
	 */
	@Test
	public void testPayloadEncryptDecrypt()
	{	
		//1-Test AES
		String crypted1 = client.payloadEncrypt("this is a test", PayLoadEncryptionMethod.AES.getValue(), "123456", null, null);		
		System.out.println(String.format("Encrypted text: %s", crypted1));
		DecryptedPayload dp =  client.payloadDecrypt(crypted1, new String[] {"123456"});
		System.out.println(String.format("Password: %s,\noriginal payload %s,\nunencrypted payload %s\nEncoded Pub key %s\nResult %s\nPayload HEX %s", dp.getDecryptPassword(), dp.getOriginalPayload(),dp.getUnencryptedPayload(), dp.getEncodedPubKey(), dp.getResult(), dp.getUnencryptedPayloadHex() ));
		assertTrue(crypted1!=null);
		assertEquals(dp.getUnencryptedPayloadHex(), OpenSslAes.decrypt("123456", crypted1));
		//2-TEST pubkey
		String crypted2 = client.payloadEncrypt("this is a test", "pubkey", null, null, b58PubKey);
		System.out.println(String.format("Encrypted text: %s", crypted2));
		DecryptedPayload dp2 =  client.payloadDecrypt(crypted2, null);
		System.out.println(String.format("Original payload %s,\nunencrypted payload %s\nEncoded Pub key %s\nResult %s\nPayload HEX %s",  dp2.getOriginalPayload(),dp2.getUnencryptedPayload(), dp2.getEncodedPubKey(), dp2.getResult(), dp2.getUnencryptedPayloadHex() ));

	}	
	
	/**
	 * Tests encodePubKey, and decodePubKey
	 */
	@Test
	public void testDecodeEncodePubKey()
	{	
		PascPublicKey pk = client.decodePubKey(null, b58PubKey);		
		System.out.println(String.format("PublicKey b58: %s \nenc: %s \nkeyType: %s, Name: %s, X=%s, Y=%s", pk.getBase58PubKey(), pk.getEncPubKey(), pk.getKeyType(), pk.getName(), pk.getX(), pk.getY()));
		assertTrue(pk!=null); 
		String encodedKey = client.encodePubKey(KeyType.SECP256K1, pk.getX(), pk.getY());
		System.out.println(String.format("encoded key:"+encodedKey));
		assertEquals(encodedKey,pk.getEncPubKey());
	}
	
	
	/**
	 * Tests getConnections method and related entity Connection
	 */
	@Test
	public void testGetConnections()
	{	
		List<Connection> result = client.getConnections();
		System.out.println(String.format("getConnections size %d", result.size()));
		for(Connection c:result)
		{
			System.out.println(String.format("Connection: Version app %s, Bytes received: %d, Bytes send: %d, Connection duration %d, IP:%s:%s, Server? %s, Remote version: %d, Remote available version: %d, Time diff: %d", c.getAppVersion(), c.getBytesReceived(), c.getBytesSent(), c.getConnectedDurationSec(), c.getIP(), c.getPort(), c.getIsServer(), c.getRemoteVersion(), c.getRemoveAvailableVersion(), c.getTimeDiff() ));
		}
		assertTrue(result.size()>0); 
	}
		
	/**
	 * Tests addNewKey, adds a new private key to the wallet
	 */
	@Test
	public void testAddNewKey()
	{	
		client.unlock(password);
		PascPublicKey pk = client.addNewKey(KeyType.SECP256K1, "Oh no, another unuseful key. Please delete me!");		
		System.out.println(String.format("PublicKey b58: %s enc: %s keyType: %s, Name: %s, X=%s, Y=%s", pk.getBase58PubKey(), pk.getEncPubKey(), pk.getKeyType(), pk.getName(), pk.getX(), pk.getY()));
		assertTrue(pk!=null); 
	}
	
	
	/**
	 * Tests stopNode and startNode (plus nodestatus)
	 * If you want to try, prease remove the Ignore annotation
	 */
	@Test
	@Ignore
	public void testStopStart()
	{	
		NodeStatus status = client.getNodeStatus();
		System.out.println(String.format("Node status: %s, descriptor %s", status.getStatusDescriptor(), status.getStatusDescriptor()));
		Boolean result = client.stopNode();
		assertTrue(result!=null); 
		NodeStatus status1 = client.getNodeStatus();
		System.out.println(String.format("Node status: %s, descriptor %s", status1.getStatusDescriptor(), status1.getStatusDescriptor()));
		result = client.startNode();
		NodeStatus status2 = client.getNodeStatus();
		System.out.println(String.format("Node status: %s, descriptor %s", status2.getStatusDescriptor(), status2.getStatusDescriptor()));
		assertTrue(result!=null); 
	}
	
	@Test
	public void multiOpAddTest() {
		
		String senderB8PubKey="3GhhbouPE7mf5rVxu7rm8f2dEczavgmeZXoxU5Z1QtraVQwurYBgmRS2Q5F49VyVn5yDpQV87a6VTTFiKAF6bDbmeDb2MDxLxUT616";
				
		Account senderAccount = client.getAccount(381403);

		OpSender op1=new OpSender();
		op1.setAccount(381403);
		op1.setAmount(0.2);
		op1.setnOperation(senderAccount.getnOperation()+1);
		//op1.setPayLoad(payload1Hex);
		
		String payload2="This Payload to receiver";
		String payload2Hex=HexConversionsHelper.byteToHex(payload2.getBytes());
		OpReceiver op2=new OpReceiver();
		op2.setAccount(126682);
		op2.setAmount(0.2);
		op2.setPayLoad(payload2Hex);
		
		OpChanger op3=new OpChanger();
		op3.setFee(0.0);
		op3.setAccount(381404);
		op3.setNewType(1);
		op3.setNewName("les.acacies");
		
		List<OpSender> senders = new ArrayList<OpSender>();
		senders.add(op1);
		List<OpReceiver> receivers = new ArrayList<OpReceiver>();
		receivers.add(op2);
		List<OpChanger> changers = new ArrayList<OpChanger>();

		MultiOperation res = client.multiOperationAddOperation(null, true, senders, receivers, changers);
		
		assertTrue(res!=null);
		System.out.println(String.format("Not signed: %d, Signed: %d, Fee: %.4f PASC, Amount:  %.4f PASC, Can Execute:%b", res.getNotSignedCount(),res.getSignedCount(), res.getFee(), res.getAmount(), res.getSignedCanExecute()));
		
		senders.clear();
		receivers.clear();
		changers.add(op3);
		
		MultiOperation res2 = client.multiOperationAddOperation(res.getRawOperations(), true, senders, receivers, changers);
		assertTrue(res2!=null);
		System.out.println(String.format("Not signed: %d, Signed: %d, Fee: %.4f PASC, Amount:  %.4f PASC, Can Execute:%b", res2.getNotSignedCount(),res2.getSignedCount(), res2.getFee(), res2.getAmount(), res2.getSignedCanExecute()));
		
		List<AccountKey> signers = new ArrayList<>();
		AccountKey key=new AccountKey();
		key.setAccount(381403);
		key.setBase58PubKey(senderB8PubKey);
		signers.add(key);
		
		MultiOperation res3 = client.multiOperationSignOffline(res2.getRawOperations(), signers);
		assertTrue(res3!=null);
		System.out.println(String.format("Not signed: %d, Signed: %d, Fee: %.4f PASC, Amount:  %.4f PASC, Can Execute:%b", res3.getNotSignedCount(),res3.getSignedCount(), res3.getFee(), res3.getAmount(), res3.getSignedCanExecute()));
	
		MultiOperation res4 = client.multiOperationSignOnline(res3.getRawOperations());
		assertTrue(res4!=null);
		System.out.println(String.format("Not signed: %d, Signed: %d, Fee: %.4f PASC, Amount:  %.4f PASC, Can Execute:%b", res4.getNotSignedCount(),res4.getSignedCount(), res4.getFee(), res4.getAmount(), res4.getSignedCanExecute()));

		MultiOperation res5= client.multiOperationDeleteOperation(res4.getRawOperations(), 0);
		assertTrue(res5!=null);
		System.out.println(String.format("Not signed: %d, Signed: %d, Fee: %.4f PASC, Amount:  %.4f PASC, Can Execute:%b", res5.getNotSignedCount(),res5.getSignedCount(), res5.getFee(), res5.getAmount(), res5.getSignedCanExecute()));

//		Uncomment if you want to execute, if so - comment deleteoperation	
//		List<Operation> operations=client.executeOperations(res4.getRawOperations());
//		for(Operation op: operations)
//		{
//			System.out.println(String.format("Operation Hash: %s\nOperation Type: %s(%s),Subtype: %s, Timestamp: %d\nAccount %d Account sender %d Balance: %.4f, Account dest: %d, Amount: %.4f, Block: %d, Fee:%.4f\nErrors %s, OpHash %s,\n Payload %s, Maturation %d, OperationBlock %d, V1Ophash %s\n,Valid %s ", op.getOpHash(), op.getType(),op.getTypeDescriptor(),op.getSubType(), op.getTime(), op.getAccount(),op.getSenderAccount(), op.getBalance(), op.getDestAccount(), op.getAmount(), op.getBlock(), op.getFee(), op.getErrors(),op.getOpHash(), op.getPayLoad(),op.getMaturation(), op.getOperationBlock(), op.getV1Ophash(), op.getValid() ));
//		}
	}
	
	/**
	 * Tests lock, unlock and setWalletPassword functions
	 * If you want to execute the test, remove ignore it and use your passwords ;)
	 */
	@Test
	@Ignore
	public void testLockPasswordUnlock()
	{	
		//Lock wallet
		Boolean locked = client.lock();
		System.out.println(String.format("Wallet locked? %s", locked));
		assertEquals(locked, Boolean.TRUE);
		//Can't change password as wallet should be locked
		Boolean result = client.setWalletPassword("ThisIsANewPassword");
		System.out.println(String.format("Password changed? %s", result!=null));
		assertEquals(result, null); 
		//Unlock wallet
		locked = !client.unlock(password);
		System.out.println(String.format("Wallet locked? %s", locked));
		assertEquals(locked, Boolean.FALSE); 
		result = client.setWalletPassword("ThisWillBeTheNewPassword");
		System.out.println(String.format("Password changed? %s", result));
		assertEquals(result, Boolean.TRUE);
	}
	
	@Test
	@Ignore
	public void testPublicKey() {
		
		PascPublicKey pub=client.decodePubKey(encPubKey, null);
		PascPublicKey pub2=PascPublicKey.fromEncodedPubKey(encPubKey);
		
		assertEquals(pub.getX(),pub2.getX());
		assertEquals(pub.getY(),pub2.getY());
		assertEquals(pub.getBase58PubKey(),pub2.getBase58PubKey());
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
