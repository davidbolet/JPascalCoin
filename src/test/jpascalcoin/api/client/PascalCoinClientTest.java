package test.jpascalcoin.api.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.github.davidbolet.jpascalcoin.api.client.PascalCoinClient;
import com.github.davidbolet.jpascalcoin.api.client.PascalCoinClientImpl;
import com.github.davidbolet.jpascalcoin.api.constants.PascalCoinConstants;
import com.github.davidbolet.jpascalcoin.api.helpers.HexConversionsHelper;
import com.github.davidbolet.jpascalcoin.api.model.Account;
import com.github.davidbolet.jpascalcoin.api.model.AccountKey;
import com.github.davidbolet.jpascalcoin.api.model.Block;
import com.github.davidbolet.jpascalcoin.api.model.Connection;
import com.github.davidbolet.jpascalcoin.api.model.DecodeOpHashResult;
import com.github.davidbolet.jpascalcoin.api.model.DecryptedPayload;
import com.github.davidbolet.jpascalcoin.api.model.KeyType;
import com.github.davidbolet.jpascalcoin.api.model.MultiOperation;
import com.github.davidbolet.jpascalcoin.api.model.NodeServer;
import com.github.davidbolet.jpascalcoin.api.model.NodeStatus;
import com.github.davidbolet.jpascalcoin.api.model.OpChanger;
import com.github.davidbolet.jpascalcoin.api.model.OpReceiver;
import com.github.davidbolet.jpascalcoin.api.model.OpSender;
import com.github.davidbolet.jpascalcoin.api.model.Operation;
import com.github.davidbolet.jpascalcoin.api.model.PascPrivateKey;
import com.github.davidbolet.jpascalcoin.api.model.PayLoadEncryptionMethod;
import com.github.davidbolet.jpascalcoin.api.model.PublicKey;
import com.github.davidbolet.jpascalcoin.api.model.RawOperation;
import com.github.davidbolet.jpascalcoin.api.model.SignResult;
import com.github.davidbolet.jpascalcoin.exception.UsupportedKeyTypeException;

/**
 * Basic tests that show how client work
 * 
 * @author davidbolet
 *
 */
public class PascalCoinClientTest {
	PascalCoinClient client;
	String encPubKey,b58PubKey, b58PubKeyOtherWallet;
	Integer accountId, account2Id, account3Id, account4Id;
	String password;
	String b58PubKey2, b58BuyKey;
	
	/**
	 * Initialize all vars here
	 */
	@Before
	public void init()
	{
		//String base="localhost";
		client = new PascalCoinClientImpl("10.211.55.10",PascalCoinConstants.DEFAULT_MAINNET_RPC_PORT,1);
		//client = new PascalCoinClientImpl("10.211.55.7",PascalCoinConstants.DEFAULT_MAINNET_RPC_PORT,1);
		
		//b58PubKey ="3GhhbopiJkQFZYUJ2vAYMmBJj2hWybSLJjkHEvqPjpdaDKGG8S5CvCzvYVbs9azzvSEtFDpvvZxftvB5dgGnDunvA64oq9HqfskigY";
		b58PubKey = "3GhhbouPE7mf5rVxu7rm8f2dEczavgmeZXoxU5Z1QtraVQwurYBgmRS2Q5F49VyVn5yDpQV87a6VTTFiKAF6bDbmeDb2MDxLxUT616";
		//b58PubKey="<Your public key (as wallet exports)>";
		//b58PubKey="3Ghhbom4iB3ZAGDPq2C4jE2NrF8n9QTno7jvJPzJLkPmoQs9a58Q82RPQqXaJV5LQBMjdNDQquWbPB5o4QHVLWLwoAwkJ7dQ3A4xFA";
		//encPubKey="CA022000206D6BBAE9FFBC5582D711D472E688C7FF0D459956E58A5C9BBA090075C71626200062730E04C61BCCD33853B4197BF28CF9CF280E37407F526712143B11D9AD3454";
//		b58PubKey2 ="<Another public key>";

		
		b58PubKeyOtherWallet="<Another public key>";
		b58BuyKey="<Another public key used on buyAccount>";
		accountId = 126682; //An account id
		account2Id = 381309; //An account id
		account3Id = 381403; //An account id
		account4Id = 381404; //An account id
		password = "<password>"; //Your wallet password
		//Initially unlock wallet,
		client.unlock(password);
	}
	
	@Test
	public void testAddNode()
	{
		Integer res = client.addNode("139.59.149.12,207.154.196.101");
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
		result = client.findAccounts(search, false,null, null,null,null,  0, 10);
		System.out.println(String.format("Finding accounts containing '%s' on name. Size %d", search,result.size()));
		for(Account account:result)
		{
			System.out.println(String.format("Account %s has name %s and balance %.4f. State is %s", account.getAccount(),account.getName(),account.getBalance(), account.getState()));
		}
//		int type=2;
//		result = client.findAccounts(null, type, null, null);
		result = client.findAccounts(null, null,null, true,null,null,  0, 10);
//		System.out.println(String.format("Finding accounts for with type=%d. Size %d", type, result.size()));
		for(Account account:result)
		{
			System.out.println(String.format("Finding accounts for sale: %s name %s and price %.4f. State is %s", account.getAccount(),account.getName(),account.getPrice()/10000, account.getState()));
		}
		result = client.findAccounts(null, null, null,null,100.0,1000.0,  0, 10);
//		System.out.println(String.format("Finding accounts for with type=%d. Size %d", type, result.size()));
		for(Account account:result)
		{
			System.out.println(String.format("Finding Account %d has balance between : %.4f and %.4f. Account balance %.4f State is %s", account.getAccount(),100.0,1000.0,account.getBalance(), account.getState()));
		}
		result = client.findAccounts(null, null,null, null,10000.0,null,  0, 10);
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
		List<PublicKey> result = client.getWalletPubKeys(0, 100);
		System.out.println(String.format("getWalletPubKeys size %d", result.size()));
		for(PublicKey pk:result)
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
		PublicKey pk = client.getWalletPubKey(encPubKey, null);		
		System.out.println(String.format("PublicKey b58: %s enc: %s keyType: %s, Name: %s, X=%s, Y=%s", pk.getBase58PubKey(), pk.getEncPubKey(), pk.getKeyType(), pk.getName(), pk.getX(), pk.getY()));
		assertTrue(pk!=null); 
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
		List<Operation> operations = client.getBlockOperations(150590, null, null);
		for(Operation op: operations)
		{
			System.out.println(String.format("Operation Hash: %s\nOperation Type: %s(%s),Subtype: %s, Timestamp: %d\nAccount %d Account sender %d Balance: %.4f, Account dest: %d, Amount: %.4f, Block: %d, Fee:%.4f\nErrors %s, OpHash %s,\n Payload %s, Maturation %d, OperationBlock %d, V1Ophash %s\n,Valid %s ", op.getOpHash(), op.getType(),op.getTypeDescriptor(),op.getSubType(), op.getTime(), op.getAccount(),op.getSenderAccount(), op.getBalance(), op.getDestAccount(), op.getAmount(), op.getBlock(), op.getFee(), op.getErrors(),op.getOpHash(), op.getPayLoad(),op.getMaturation(), op.getOperationBlock(), op.getV1Ophash(), op.getValid() ));
		}
		Operation op = client.getBlockOperation(150590, 46);
		System.out.println(String.format("Operation Hash: %s\nOperation Type: %s(%s),Subtype: %s, Timestamp: %d\nAccount %d Account sender %d Balance: %.4f, Account dest: %d, Amount: %.4f, Block: %d, Fee:%.4f\nErrors %s, OpHash %s,\n Payload %s, Maturation %d, OperationBlock %d, V1Ophash %s\n,Valid %s ", op.getOpHash(), op.getType(),op.getTypeDescriptor(),op.getSubType(), op.getTime(), op.getAccount(),op.getSenderAccount(), op.getBalance(), op.getDestAccount(), op.getAmount(), op.getBlock(), op.getFee(), op.getErrors(),op.getOpHash(), op.getPayLoad(),op.getMaturation(), op.getOperationBlock(), op.getV1Ophash(), op.getValid() ));
		assertTrue(op.getOperationBlock().equals(operations.get(46).getOperationBlock()));
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
		Operation op = client.findOperation("394E02007DD105000500000026E34749214D52765404EA230640210149FC43BB");
		System.out.println(String.format("Operation Hash: %s\nOperation Type: %s(%s),Subtype: %s, Timestamp: %d\nAccount %d Account sender %d Balance: %.4f, Account dest: %d, Amount: %.4f, Block: %d, Fee:%.4f\nErrors %s, OpHash %s,\n Payload %s, Maturation %d, OperationBlock %d, V1Ophash %s\n,Valid %s ", op.getOpHash(), op.getType(),op.getTypeDescriptor(),op.getSubType(), op.getTime(), op.getAccount(),op.getSenderAccount(), op.getBalance(), op.getDestAccount(), op.getAmount(), op.getBlock(), op.getFee(), op.getErrors(),op.getOpHash(), op.getPayLoad(),op.getMaturation(), op.getOperationBlock(), op.getV1Ophash(), op.getValid() ));
		assertTrue(op!=null);
		
		Operation op2= client.findOperation("27410300DBD1050011000000FC4973EF2F9DA398B8778CA0B672AC3470F508EE");
		System.out.println(String.format("Operation Hash: %s\nOperation Type: %s(%s),Subtype: %s, Timestamp: %d\nAccount %d Account sender %d Balance: %.4f, Account dest: %d, Amount: %.4f, Block: %d, Fee:%.4f\nErrors %s, OpHash %s,\n Payload %s, Maturation %d, OperationBlock %d, V1Ophash %s\n,Valid %s ", op.getOpHash(), op.getType(),op.getTypeDescriptor(),op.getSubType(), op.getTime(), op.getAccount(),op.getSenderAccount(), op.getBalance(), op.getDestAccount(), op.getAmount(), op.getBlock(), op.getFee(), op.getErrors(),op.getOpHash(), op.getPayLoad(),op.getMaturation(), op.getOperationBlock(), op.getV1Ophash(), op.getValid() ));
		assertTrue(op2!=null);
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
	 * Test changeAccountInfo 
	 */
	@Test
	public void testChangeAccountInfo()
	{
		Operation op = client.changeAccountInfo(126682, 126682, null, b58PubKey2, "bolet",(short) 0, 0.01, "Payload aes".getBytes() , PayLoadEncryptionMethod.AES, "123456");
		System.out.println(String.format("Operation Hash: %s\nOperation Type: %s(%s),Subtype: %s, Timestamp: %d\nAccount %d Account sender %d Balance: %.4f, Account dest: %d, Amount: %.4f, Block: %d, Fee:%.4f\nErrors %s, OpHash %s,\n Payload %s, Maturation %d, OperationBlock %d, V1Ophash %s\n,Valid %s ", op.getOpHash(), op.getType(),op.getTypeDescriptor(),op.getSubType(), op.getTime(), op.getAccount(),op.getSenderAccount(), op.getBalance(), op.getDestAccount(), op.getAmount(), op.getBlock(), op.getFee(), op.getErrors(),op.getOpHash(), op.getPayLoad(),op.getMaturation(), op.getOperationBlock(), op.getV1Ophash(), op.getValid() ));
		assertTrue(op!=null);
	}
	
	/**
	 * Test sendTo 
	 */
	@Test
	public void testSendTo()
	{
		Operation op = client.sendTo(accountId, account3Id, 0.0300, 0.01, "mi polla como una olla".getBytes(), PayLoadEncryptionMethod.NONE, null);
		System.out.println(String.format("Operation Hash: %s\nOperation Type: %s(%s),Subtype: %s, Timestamp: %d\nAccount %d Account sender %d Balance: %.4f, Account dest: %d, Amount: %.4f, Block: %d, Fee:%.4f\nErrors %s, OpHash %s,\n Payload %s, Maturation %d, OperationBlock %d, V1Ophash %s\n,Valid %s ", op.getOpHash(), op.getType(),op.getTypeDescriptor(),op.getSubType(), op.getTime(), op.getAccount(),op.getSenderAccount(), op.getBalance(), op.getDestAccount(), op.getAmount(), op.getBlock(), op.getFee(), op.getErrors(),op.getOpHash(), op.getPayLoad(),op.getMaturation(), op.getOperationBlock(), op.getV1Ophash(), op.getValid() ));
		assertTrue(op!=null);
	}
	
	/**
	 * Test changeKey (moves account to another wallet) 
	 */
	@Test
	public void testChangeKey()
	{
		client.unlock(password);
		Operation op = client.changeKey(account3Id, account3Id, null, b58PubKey2, 0.0, "Testing".getBytes() , PayLoadEncryptionMethod.AES, "123456");
		System.out.println(String.format("Operation Hash: %s\nOperation Type: %s(%s),Subtype: %s, Timestamp: %d\nAccount %d Account sender %d Balance: %.4f, Account dest: %d, Amount: %.4f, Block: %d, Fee:%.4f\nErrors %s, OpHash %s,\n Payload %s, Maturation %d, OperationBlock %d, V1Ophash %s\n,Valid %s ", op.getOpHash(), op.getType(),op.getTypeDescriptor(),op.getSubType(), op.getTime(), op.getAccount(),op.getSenderAccount(), op.getBalance(), op.getDestAccount(), op.getAmount(), op.getBlock(), op.getFee(), op.getErrors(),op.getOpHash(), op.getPayLoad(),op.getMaturation(), op.getOperationBlock(), op.getV1Ophash(), op.getValid() ));
		assertTrue(op!=null);
	}	
	
	
	/**
	 * Test changeKeys (moves some accounts to another wallet) 
	 */
	@Test
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
	 */
	@Test
	public void testListAccountForSale()
	{
		Operation op = client.listAccountForSale(577053, account2Id, 10.0000, account2Id, null, null, 0, 0.01, "Testing listAccountForSale".getBytes() , PayLoadEncryptionMethod.AES, "123456");
		System.out.println(String.format("Operation Hash: %s\nOperation Type: %s(%s),Subtype: %s, Timestamp: %d\nAccount %d Account sender %d Balance: %.4f, Account dest: %d, Amount: %.4f, Block: %d, Fee:%.4f\nErrors %s, OpHash %s,\n Payload %s, Maturation %d, OperationBlock %d, V1Ophash %s\n,Valid %s ", op.getOpHash(), op.getType(),op.getTypeDescriptor(),op.getSubType(), op.getTime(), op.getAccount(),op.getSenderAccount(), op.getBalance(), op.getDestAccount(), op.getAmount(), op.getBlock(), op.getFee(), op.getErrors(),op.getOpHash(), op.getPayLoad(),op.getMaturation(), op.getOperationBlock(), op.getV1Ophash(), op.getValid() ));
		assertTrue(op!=null);
	}
	
	/**
	 * Test delistAccountForSale
	 */
	@Test
	public void testDelistAccountForSale()
	{
		
		Operation op = client.delistAccountForSale(563108, 563108, 0.0, "Testing delistAccountForSale".getBytes() , PayLoadEncryptionMethod.DEST, null);
		System.out.println(String.format("Operation Hash: %s\nOperation Type: %s(%s),Subtype: %s, Timestamp: %d\nAccount %d Account sender %d Balance: %.4f, Account dest: %d, Amount: %.4f, Block: %d, Fee:%.4f\nErrors %s, OpHash %s,\n Payload %s, Maturation %d, OperationBlock %d, V1Ophash %s\n,Valid %s ", op.getOpHash(), op.getType(),op.getTypeDescriptor(),op.getSubType(), op.getTime(), op.getAccount(),op.getSenderAccount(), op.getBalance(), op.getDestAccount(), op.getAmount(), op.getBlock(), op.getFee(), op.getErrors(),op.getOpHash(), op.getPayLoad(),op.getMaturation(), op.getOperationBlock(), op.getV1Ophash(), op.getValid() ));
		assertTrue(op!=null);
	}	
	
	
	/**
	 * Test buyAccount
	 */
	@Test
	public void testBuyAccount()
	{
		Operation op = client.buyAccount(account2Id, 577131, 0.1111, 86926, null, encPubKey, 0.1111, 0.01, null, null, null);
		System.out.println(String.format("Operation Hash: %s\nOperation Type: %s(%s),Subtype: %s, Timestamp: %d\nAccount %d Account sender %d Balance: %.4f, Account dest: %d, Amount: %.4f, Block: %d, Fee:%.4f\nErrors %s, OpHash %s,\n Payload %s, Maturation %d, OperationBlock %d, V1Ophash %s\n,Valid %s ", op.getOpHash(), op.getType(),op.getTypeDescriptor(),op.getSubType(), op.getTime(), op.getAccount(),op.getSenderAccount(), op.getBalance(), op.getDestAccount(), op.getAmount(), op.getBlock(), op.getFee(), op.getErrors(),op.getOpHash(), op.getPayLoad(),op.getMaturation(), op.getOperationBlock(), op.getV1Ophash(), op.getValid() ));
		assertTrue(op!=null);
	}		
	
	/**
	 * Another test for buyaccount
	 */
	@Test
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
	 */
	@Test
	public void testSignMethodsAndOperationsInfo()
	{
		Account account = client.getAccount(accountId);
		RawOperation rop = client.signSendTo(accountId, account4Id, null, b58PubKey, null, b58BuyKey, account.getnOperation(), 0.2, 0.0, "Test signSend".getBytes() , PayLoadEncryptionMethod.AES, "123456", null);
		System.out.println(String.format("Num operations: %d Total amount: %.4f, Total fee: %.4f, Raw Operations: %s",rop.getNumOperations(), rop.getTotalAmount(), rop.getTotalFee(), rop.getRawOperations()));
		assertTrue(rop!=null);
		RawOperation op2= client.signDelistAccountForSale(account2Id, account2Id, account.getnOperation()+1, 0.0, "Test signSend".getBytes() , PayLoadEncryptionMethod.AES, "123456", b58PubKey, null, rop.getRawOperations());
		System.out.println(String.format("Num operations: %d Total amount: %.4f, Total fee: %.4f, Raw Operations: %s",op2.getNumOperations()+1, op2.getTotalAmount(), op2.getTotalFee(), op2.getRawOperations()));
		RawOperation op3= client.signListAccountForSale(account2Id, account2Id, 1000.0000,accountId, b58PubKey, null, 0,  account.getnOperation()+2, 0.0, "Test signListAccountforSale".getBytes() , PayLoadEncryptionMethod.AES, "123456", b58PubKey, null, op2.getRawOperations());
		System.out.println(String.format("Num operations: %d Total amount: %.4f, Total fee: %.4f, Raw Operations: %s",op3.getNumOperations(), op3.getTotalAmount(), op3.getTotalFee(), op3.getRawOperations()));
		RawOperation op4= client.signChangeKey(account3Id, account3Id, null, b58PubKey, null, b58PubKeyOtherWallet,account.getnOperation()+3, 0.0, "Test signChangeKey".getBytes() , PayLoadEncryptionMethod.AES, "123456", op3.getRawOperations());
		System.out.println(String.format("Num operations: %d Total amount: %.4f, Total fee: %.4f, Raw Operations: %s",op4.getNumOperations(), op4.getTotalAmount(), op4.getTotalFee(), op4.getRawOperations()));
		RawOperation op5= client.signBuyAccount(accountId, account2Id, 10.0000, accountId, b58PubKey, null, 0.001, account.getnOperation()+4, 0.0, "Test signBuyAccount".getBytes() , PayLoadEncryptionMethod.AES, "123456", b58PubKey, null, op4.getRawOperations());
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
		String hex2=HexConversionsHelper.byteToHex("Tengo una vaca lechera, no es una vaca cualquiera".getBytes());
		SignResult res= client.signMessage(hex, encPubKey, null);
		assertTrue(res!=null);
		System.out.println(String.format("Digest: %s, Signature: %s, EncPubKey: %s", res.getDigest(),res.getSignature(),res.getEncPubkey()));
		res = client.verifySign(hex2, encPubKey, res.getSignature());
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
		List<Operation> ops = client.getAccountOperations(381309, null, null, 11);
		
		DecryptedPayload dp =  client.payloadDecrypt(ops.get(0).getPayLoad(), new String[] {"123456"});
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
		PublicKey pk = client.decodePubKey(null, b58PubKey);		
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
		client.unlock("L1L0kio10!");
		client.unlock(password);
		PublicKey pk = client.addNewKey(KeyType.SECP256K1, "davidbolet@gmail.com");		
		System.out.println(String.format("PublicKey b58: %s enc: %s keyType: %s, Name: %s, X=%s, Y=%s", pk.getBase58PubKey(), pk.getEncPubKey(), pk.getKeyType(), pk.getName(), pk.getX(), pk.getY()));
		assertTrue(pk!=null); 
	}
	
	
	/**
	 * Tests stopNode and startNode (plus nodestatus)
	 */
	@Test
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
		//String receiverB58PubKey="3GhhbopiJkQFZYUJ2vAYMmBJj2hWybSLJjkHEvqPjpdaDKGG8S5CvCzvYVbs9azzvSEtFDpvvZxftvB5dgGnDunvA64oq9HqfskigY";
				
		Account senderAccount = client.getAccount(381403);
		
//		RawOperation rop = client.signSendTo(381403, 126682, null, senderB8PubKey, null, receiverB58PubKey, senderAccount.getnOperation(), 0.02, 0.0, "Test signSend".getBytes() , PayLoadEncryptionMethod.AES, "123456", null);
//		System.out.println(String.format("Num operations: %d Total amount: %.4f, Total fee: %.4f, Raw Operations: %s",rop.getNumOperations(), rop.getTotalAmount(), rop.getTotalFee(), rop.getRawOperations()));
//		assertTrue(rop!=null);
//		
//		String rawoperations= "0100000001000000DBD1050011000000DAEE0100C8000000000000000000000000000000200053616C7465645F5F961DD005C65FA70C3F3DEE063C411083CF03314A69803E2900000000000020001F72ABDF23C6AD4C41A86A1D35E0FE443DA5BF3B00A097F7C553BA3B875863092000021D8D5F45E6AAC5A8EEF373A347E8686872BD319DF5096A95A4FC6E535F92BE";
		String payload1="This Payload from sender1";
		String payload1Hex=HexConversionsHelper.byteToHex(payload1.getBytes());
		OpSender op1=new OpSender();
		op1.setAccount(381403);
		op1.setAmount(0.2);
		op1.setnOperation(senderAccount.getnOperation()+1);
		op1.setPayLoad(payload1Hex);
		
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
	
	/**
	 * Test key generation. To check the result with the wallet, import the generated private key and check Base58PubKey
	 * @throws UsupportedKeyTypeException
	 */
	@Test
	public void testGeneratePrivateKey() throws UsupportedKeyTypeException {
		//System.out.println(HexConversionsHelper.int2BigEndianHex(32));
		//System.out.println(HexConversionsHelper.int2BigEndianHex(KeyType.SECP256K1.getValue()));
		PascPrivateKey key = PascPrivateKey.generate(KeyType.SECP256K1,"test");
		System.out.println("private key: "+key.getPrivateKey());
		System.out.println("encPubKey: "+key.getPublicKey().getEncPubKey());
		System.out.println("Base58PubKey: "+key.getPublicKey().getBase58PubKey());
	}
	
}
