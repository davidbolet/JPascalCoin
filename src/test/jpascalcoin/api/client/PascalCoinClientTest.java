package test.jpascalcoin.api.client;

import static org.junit.Assert.*;

import java.util.Base64;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.github.davidbolet.jpascalcoin.api.client.PascalCoinClient;
import com.github.davidbolet.jpascalcoin.api.client.PascalCoinClientImpl;
import com.github.davidbolet.jpascalcoin.api.constants.PascalCoinConstants;
import com.github.davidbolet.jpascalcoin.api.model.Account;
import com.github.davidbolet.jpascalcoin.api.model.Block;
import com.github.davidbolet.jpascalcoin.api.model.Connection;
import com.github.davidbolet.jpascalcoin.api.model.DecodeOpHashResult;
import com.github.davidbolet.jpascalcoin.api.model.DecryptedPayload;
import com.github.davidbolet.jpascalcoin.api.model.KeyType;
import com.github.davidbolet.jpascalcoin.api.model.NodeServer;
import com.github.davidbolet.jpascalcoin.api.model.NodeStatus;
import com.github.davidbolet.jpascalcoin.api.model.Operation;
import com.github.davidbolet.jpascalcoin.api.model.PayLoadEncryptionMethod;
import com.github.davidbolet.jpascalcoin.api.model.PublicKey;
import com.github.davidbolet.jpascalcoin.api.model.RawOperation;

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
		String base="localhost";
		client = new PascalCoinClientImpl("10.211.55.7",PascalCoinConstants.DEFAULT_MAINNET_RPC_PORT);
		b58PubKey ="3GhhbopiJkQFZYUJ2vAYMmBJj2hWybSLJjkHEvqPjpdaDKGG8S5CvCzvYVbs9azzvSEtFDpvvZxftvB5dgGnDunvA64oq9HqfskigY";
		b58PubKey2 = "3GhhbouPE7mf5rVxu7rm8f2dEczavgmeZXoxU5Z1QtraVQwurYBgmRS2Q5F49VyVn5yDpQV87a6VTTFiKAF6bDbmeDb2MDxLxUT616";
//		b58PubKey="<Your public key (as wallet exports)>";
//		encPubKey="<Your public key (encoded format)>";
//		b58PubKey2 ="<Another public key>";

		
		b58PubKeyOtherWallet="<Another public key>";
		b58BuyKey="<Another public key used on buyAccount>";
		accountId = 126682; //An account id
		account2Id = 381309; //An account id
		account3Id = 381403; //An account id
		account4Id = 381404; //An account id
		password = "L1L0kio10"; //Your wallet password
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
		Integer result = client.getWalletAccountsCount(null, b58PubKey, null, null);
		System.out.println(String.format("GetAccountsCount %d", result));
		assertTrue(result>0); 
	}
	
	/**
	 * Tests listAccounts function
	 */
	@Test
	public void testListAccounts()
	{	
		List<Account> result = client.getWalletAccounts(encPubKey, null, null, null);
		System.out.println(String.format("GetAccounts size %d", result.size()));
		for(Account account:result)
		{
			System.out.println(String.format("Account %s has name %s and balance %.4f", account.getAccount(),account.getName(),account.getBalance()));
		}
		assertTrue(result.size()>0); 
	}

	/**
	 * Tests findAccounts function
	 */
	@Test
	public void testFindAccounts()
	{	
		List<Account> result = client.findAccounts("california", null, 0, 0, 100);
		System.out.println(String.format("Finding accounts containing 'california' on name. Size %d", result.size()));
		for(Account account:result)
		{
			System.out.println(String.format("Account %s has name %s and balance %.4f. State is %s", account.getAccount(),account.getName(),account.getBalance(), account.getState()));
		}
		assertTrue(result!=null);
//		Integer res = client.findAccountsCount(null, 0, 2);
//		System.out.println("findAccountsCount:"+res);
		result = client.findAccounts("", 0, 2, null, null);
		System.out.println(String.format("Finding accounts for public sale. Size %d", result.size()));
		for(Account account:result)
		{
			System.out.println(String.format("For Sale: %s name %s and balance %.4f. State is %s", account.getAccount(),account.getName(),account.getBalance(), account.getState()));
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
		Double coins = client.getWalletCoins(null, b58PubKey);
		
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
			System.out.println(String.format("Operation Hash: %s\nOperation Type: %s(%s),Subtype: %s, Timestamp: %d\nAccount %d Account sender %d Balance: %.4f, Account dest: %d, Amount: %.4f, Block: %d, Fee:%.4f\nErrors %s, OpHash %s,\n Payload %s, Maturation %d, OperationBlock %d, V1Ophash %s\n,Valid %s ", op.getOpHash(), op.getType(),op.getTypeDescriptor(),op.getSubType(), op.getTime(), op.getAccount(),op.getSenderAccount(), op.getBalance(), op.getDestAccount(), op.getAmount(), op.getBlock(), op.getFee(), op.getErrors(),op.getOpHash(), Base64.getEncoder().encodeToString(op.getPayLoad()),op.getMaturation(), op.getOperationBlock(), op.getV1Ophash(), op.getValid() ));
		}
		Operation op = client.getBlockOperation(150590, 46);
		System.out.println(String.format("Operation Hash: %s\nOperation Type: %s(%s),Subtype: %s, Timestamp: %d\nAccount %d Account sender %d Balance: %.4f, Account dest: %d, Amount: %.4f, Block: %d, Fee:%.4f\nErrors %s, OpHash %s,\n Payload %s, Maturation %d, OperationBlock %d, V1Ophash %s\n,Valid %s ", op.getOpHash(), op.getType(),op.getTypeDescriptor(),op.getSubType(), op.getTime(), op.getAccount(),op.getSenderAccount(), op.getBalance(), op.getDestAccount(), op.getAmount(), op.getBlock(), op.getFee(), op.getErrors(),op.getOpHash(), Base64.getEncoder().encodeToString(op.getPayLoad()),op.getMaturation(), op.getOperationBlock(), op.getV1Ophash(), op.getValid() ));
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
			System.out.println(String.format("Operation Hash: %s\nOperation Type: %s(%s),Subtype: %s, Timestamp: %d\nAccount %d Account sender %d Balance: %.4f, Account dest: %d, Amount: %.4f, Block: %d, Fee:%.4f\nErrors %s, OpHash %s,\n Payload %s, Maturation %d, OperationBlock %d, V1Ophash %s\n,Valid %s ", op.getOpHash(), op.getType(),op.getTypeDescriptor(),op.getSubType(), op.getTime(), op.getAccount(),op.getSenderAccount(), op.getBalance(), op.getDestAccount(), op.getAmount(), op.getBlock(), op.getFee(), op.getErrors(),op.getOpHash(), Base64.getEncoder().encodeToString(op.getPayLoad()),op.getMaturation(), op.getOperationBlock(), op.getV1Ophash(), op.getValid() ));
		}
		assertTrue(operations!=null);
	}	
	
	/**
	 * Tests getPendings() and Operation entity
	 */
	@Test
	public void testPendings()
	{
		List<Operation> operations = client.getPendings();
		for(Operation op: operations)
		{
			System.out.println(String.format("Operation Hash: %s\nOperation Type: %s(%s),Subtype: %s, Timestamp: %d\nAccount %d Account sender %d Balance: %.4f, Account dest: %d, Amount: %.4f, Block: %d, Fee:%.4f\nErrors %s, OpHash %s,\n Payload %s, Maturation %d, OperationBlock %d, V1Ophash %s\n,Valid %s ", op.getOpHash(), op.getType(),op.getTypeDescriptor(),op.getSubType(), op.getTime(), op.getAccount(),op.getSenderAccount(), op.getBalance(), op.getDestAccount(), op.getAmount(), op.getBlock(), op.getFee(), op.getErrors(),op.getOpHash(), Base64.getEncoder().encodeToString(op.getPayLoad()),op.getMaturation(), op.getOperationBlock(), op.getV1Ophash(), op.getValid() ));
		}
		assertTrue(operations!=null);
	}	
	
	/**
	 * Tests findOperation
	 */
	@Test
	public void testFindNOperation()
	{
		Operation op = client.findNOperation(account2Id,4);
		System.out.println(String.format("Operation Hash: %s\nOperation Type: %s(%s),Subtype: %s, Timestamp: %d\nAccount %d Account sender %d Balance: %.4f, Account dest: %d, Amount: %.4f, Block: %d, Fee:%.4f\nErrors %s, OpHash %s,\n Payload %s, Maturation %d, OperationBlock %d, V1Ophash %s\n,Valid %s ", op.getOpHash(), op.getType(),op.getTypeDescriptor(),op.getSubType(), op.getTime(), op.getAccount(),op.getSenderAccount(), op.getBalance(), op.getDestAccount(), op.getAmount(), op.getBlock(), op.getFee(), op.getErrors(),op.getOpHash(), Base64.getEncoder().encodeToString(op.getPayLoad()),op.getMaturation(), op.getOperationBlock(), op.getV1Ophash(), op.getValid() ));
		assertTrue(op!=null);
	}
	
	/**
	 * Tests findOperation
	 */
	@Test
	public void testFindNOperations()
	{
		List<Operation> ops = client.findNOperations(account2Id, 4, 10, 0);
		for(Operation op:ops)
		{
			System.out.println(String.format("Operation Hash: %s\nOperation Type: %s(%s),Subtype: %s, Timestamp: %d\nAccount %d Account sender %d Balance: %.4f, Account dest: %d, Amount: %.4f, Block: %d, Fee:%.4f\nErrors %s, OpHash %s,\n Payload %s, Maturation %d, OperationBlock %d, V1Ophash %s\n,Valid %s ", op.getOpHash(), op.getType(),op.getTypeDescriptor(),op.getSubType(), op.getTime(), op.getAccount(),op.getSenderAccount(), op.getBalance(), op.getDestAccount(), op.getAmount(), op.getBlock(), op.getFee(), op.getErrors(),op.getOpHash(), Base64.getEncoder().encodeToString(op.getPayLoad()),op.getMaturation(), op.getOperationBlock(), op.getV1Ophash(), op.getValid() ));
		}
		assertTrue(ops!=null);
	}
	
	/**
	 * Tests findOperation
	 */
	@Test
	public void testFindOperation()
	{
		Operation op = client.findOperation("314B02007DD1050003000000D3567A59AF2E1531DC384892764DB7C51CB7CAC9");
		System.out.println(String.format("Operation Hash: %s\nOperation Type: %s(%s),Subtype: %s, Timestamp: %d\nAccount %d Account sender %d Balance: %.4f, Account dest: %d, Amount: %.4f, Block: %d, Fee:%.4f\nErrors %s, OpHash %s,\n Payload %s, Maturation %d, OperationBlock %d, V1Ophash %s\n,Valid %s ", op.getOpHash(), op.getType(),op.getTypeDescriptor(),op.getSubType(), op.getTime(), op.getAccount(),op.getSenderAccount(), op.getBalance(), op.getDestAccount(), op.getAmount(), op.getBlock(), op.getFee(), op.getErrors(),op.getOpHash(), Base64.getEncoder().encodeToString(op.getPayLoad()),op.getMaturation(), op.getOperationBlock(), op.getV1Ophash(), op.getValid() ));
		assertTrue(op!=null);
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
		client.unlock("L1L0kio10!");
		Operation op = client.changeAccountInfo(126682, 126682, null, b58PubKey2, "david_y", Short.parseShort("0"), 0.01, "Payload".getBytes() , PayLoadEncryptionMethod.DEST, null);
		System.out.println(String.format("Operation Hash: %s\nOperation Type: %s(%s),Subtype: %s, Timestamp: %d\nAccount %d Account sender %d Balance: %.4f, Account dest: %d, Amount: %.4f, Block: %d, Fee:%.4f\nErrors %s, OpHash %s,\n Payload %s, Maturation %d, OperationBlock %d, V1Ophash %s\n,Valid %s ", op.getOpHash(), op.getType(),op.getTypeDescriptor(),op.getSubType(), op.getTime(), op.getAccount(),op.getSenderAccount(), op.getBalance(), op.getDestAccount(), op.getAmount(), op.getBlock(), op.getFee(), op.getErrors(),op.getOpHash(), Base64.getEncoder().encodeToString(op.getPayLoad()),op.getMaturation(), op.getOperationBlock(), op.getV1Ophash(), op.getValid() ));
		assertTrue(op!=null);
	}
	
	/**
	 * Test sendTo 
	 */
	@Test
	public void testSendTo()
	{
		Operation op = client.sendTo(accountId, account3Id, 0.0300, 0.01, "mi polla como una olla".getBytes(), PayLoadEncryptionMethod.NONE, null);
		System.out.println(String.format("Operation Hash: %s\nOperation Type: %s(%s),Subtype: %s, Timestamp: %d\nAccount %d Account sender %d Balance: %.4f, Account dest: %d, Amount: %.4f, Block: %d, Fee:%.4f\nErrors %s, OpHash %s,\n Payload %s, Maturation %d, OperationBlock %d, V1Ophash %s\n,Valid %s ", op.getOpHash(), op.getType(),op.getTypeDescriptor(),op.getSubType(), op.getTime(), op.getAccount(),op.getSenderAccount(), op.getBalance(), op.getDestAccount(), op.getAmount(), op.getBlock(), op.getFee(), op.getErrors(),op.getOpHash(), Base64.getEncoder().encodeToString(op.getPayLoad()),op.getMaturation(), op.getOperationBlock(), op.getV1Ophash(), op.getValid() ));
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
		System.out.println(String.format("Operation Hash: %s\nOperation Type: %s(%s),Subtype: %s, Timestamp: %d\nAccount %d Account sender %d Balance: %.4f, Account dest: %d, Amount: %.4f, Block: %d, Fee:%.4f\nErrors %s, OpHash %s,\n Payload %s, Maturation %d, OperationBlock %d, V1Ophash %s\n,Valid %s ", op.getOpHash(), op.getType(),op.getTypeDescriptor(),op.getSubType(), op.getTime(), op.getAccount(),op.getSenderAccount(), op.getBalance(), op.getDestAccount(), op.getAmount(), op.getBlock(), op.getFee(), op.getErrors(),op.getOpHash(), Base64.getEncoder().encodeToString(op.getPayLoad()),op.getMaturation(), op.getOperationBlock(), op.getV1Ophash(), op.getValid() ));
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
			System.out.println(String.format("Operation Hash: %s\nOperation Type: %s(%s),Subtype: %s, Timestamp: %d\nAccount %d Account sender %d Balance: %.4f, Account dest: %d, Amount: %.4f, Block: %d, Fee:%.4f\nErrors %s, OpHash %s,\n Payload %s, Maturation %d, OperationBlock %d, V1Ophash %s\n,Valid %s ", op.getOpHash(), op.getType(),op.getTypeDescriptor(),op.getSubType(), op.getTime(), op.getAccount(),op.getSenderAccount(), op.getBalance(), op.getDestAccount(), op.getAmount(), op.getBlock(), op.getFee(), op.getErrors(),op.getOpHash(), Base64.getEncoder().encodeToString(op.getPayLoad()),op.getMaturation(), op.getOperationBlock(), op.getV1Ophash(), op.getValid() ));
		assertTrue(ops!=null);
	}	
	
	
	/**
	 * Test listAccountForSale
	 */
	@Test
	public void testListAccountForSale()
	{
		Operation op = client.listAccountForSale(account2Id, account2Id, 1000.0000, accountId, null, null, 151125, 0.0, "Testing listAccountForSale".getBytes() , PayLoadEncryptionMethod.AES, "123456");
		System.out.println(String.format("Operation Hash: %s\nOperation Type: %s(%s),Subtype: %s, Timestamp: %d\nAccount %d Account sender %d Balance: %.4f, Account dest: %d, Amount: %.4f, Block: %d, Fee:%.4f\nErrors %s, OpHash %s,\n Payload %s, Maturation %d, OperationBlock %d, V1Ophash %s\n,Valid %s ", op.getOpHash(), op.getType(),op.getTypeDescriptor(),op.getSubType(), op.getTime(), op.getAccount(),op.getSenderAccount(), op.getBalance(), op.getDestAccount(), op.getAmount(), op.getBlock(), op.getFee(), op.getErrors(),op.getOpHash(), Base64.getEncoder().encodeToString(op.getPayLoad()),op.getMaturation(), op.getOperationBlock(), op.getV1Ophash(), op.getValid() ));
		assertTrue(op!=null);
	}
	
	/**
	 * Test delistAccountForSale
	 */
	@Test
	public void testDelistAccountForSale()
	{
		Operation op = client.delistAccountForSale(account2Id, account2Id, 0.0, "Testing delistAccountForSale".getBytes() , PayLoadEncryptionMethod.AES, "123456");
		System.out.println(String.format("Operation Hash: %s\nOperation Type: %s(%s),Subtype: %s, Timestamp: %d\nAccount %d Account sender %d Balance: %.4f, Account dest: %d, Amount: %.4f, Block: %d, Fee:%.4f\nErrors %s, OpHash %s,\n Payload %s, Maturation %d, OperationBlock %d, V1Ophash %s\n,Valid %s ", op.getOpHash(), op.getType(),op.getTypeDescriptor(),op.getSubType(), op.getTime(), op.getAccount(),op.getSenderAccount(), op.getBalance(), op.getDestAccount(), op.getAmount(), op.getBlock(), op.getFee(), op.getErrors(),op.getOpHash(), Base64.getEncoder().encodeToString(op.getPayLoad()),op.getMaturation(), op.getOperationBlock(), op.getV1Ophash(), op.getValid() ));
		assertTrue(op!=null);
	}	
	
	
	/**
	 * Test buyAccount
	 */
	@Test
	public void testBuyAccount()
	{
		Operation op = client.buyAccount(accountId, account4Id, 0.7699, 330025, b58BuyKey, null, 0.7699, 0.0, "Test buy account".getBytes() , PayLoadEncryptionMethod.AES, "123456");
		System.out.println(String.format("Operation Hash: %s\nOperation Type: %s(%s),Subtype: %s, Timestamp: %d\nAccount %d Account sender %d Balance: %.4f, Account dest: %d, Amount: %.4f, Block: %d, Fee:%.4f\nErrors %s, OpHash %s,\n Payload %s, Maturation %d, OperationBlock %d, V1Ophash %s\n,Valid %s ", op.getOpHash(), op.getType(),op.getTypeDescriptor(),op.getSubType(), op.getTime(), op.getAccount(),op.getSenderAccount(), op.getBalance(), op.getDestAccount(), op.getAmount(), op.getBlock(), op.getFee(), op.getErrors(),op.getOpHash(), Base64.getEncoder().encodeToString(op.getPayLoad()),op.getMaturation(), op.getOperationBlock(), op.getV1Ophash(), op.getValid() ));
		assertTrue(op!=null);
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
			System.out.println(String.format("Operation Hash: %s\nOperation Type: %s(%s),Subtype: %s, Timestamp: %d\nAccount %d Account sender %d Balance: %.4f, Account dest: %d, Amount: %.4f, Block: %d, Fee:%.4f\nErrors %s, OpHash %s,\n Payload %s, Maturation %d, OperationBlock %d, V1Ophash %s\n,Valid %s ", op.getOpHash(), op.getType(),op.getTypeDescriptor(),op.getSubType(), op.getTime(), op.getAccount(),op.getSenderAccount(), op.getBalance(), op.getDestAccount(), op.getAmount(), op.getBlock(), op.getFee(), op.getErrors(),op.getOpHash(), Base64.getEncoder().encodeToString(op.getPayLoad()),op.getMaturation(), op.getOperationBlock(), op.getV1Ophash(), op.getValid() ));
		}
		List<Operation> operations2 = client.executeOperations(op2.getRawOperations());
		assertEquals(operations.size(),operations2.size());
	}
	
	/**
	 * Tests getNodeStatus, and related entities NodeStatus, NodeServer and NetStats
	 */
	@Test
	public void testNodeStatus()
	{	
		NodeStatus status = client.getNodeStatus();
		System.out.println(String.format("Node status: Blocks: %d Locked %s, NetProtocol version %d, net protocol available version: %d, status descriptor %s, version %s, readydescriptor %s", status.getBlocks(), status.getLocked(), status.getNetProtocol().getVersion(), status.getNetProtocol().getAvailableVersion(), status.getStatusDescriptor(), status.getVersion(), status.getReadyDescriptor()));
		for (NodeServer server: status.getNodeServers())
		{
			System.out.println(String.format("Node server: IP %s, port %d, attempts: %d, last connect: %s", server.getIP(), server.getPort(), server.getAttempts(), server.getLastConnect()));
		}
		System.out.println(String.format("Net stats: Active: %d Bytes received %d, Bytes send %d, clients %d, servers %d, serversT %d, total %d, total clients %d, total servers %d", status.getNetStats().getActive(), status.getNetStats().getBytesReceived(), status.getNetStats().getBytesSent(), status.getNetStats().getClients(), status.getNetStats().getServers(), status.getNetStats().getServersT(),status.getNetStats().getTotal(),status.getNetStats().getTotalClients(),status.getNetStats().getTotalServers()));
		
		assert(status!=null); 
	}
	
	/**
	 * Tests payloadEncrypt, and payloadDencrypt
	 */
	@Test
	public void testPayloadEncryptDecrypt()
	{	String toDecrypt="21100E00100002F4BAC5467E8581A61E385F09A3312288FF4B946C7B0AC1897D7CC2E514E40549C6EFDAD7377064411ED8605BFA6BF9CBA6037AFFC05A10CFB64D02843E381606";
		String crypted = client.payloadEncrypt("this is a test", PayLoadEncryptionMethod.AES, encPubKey);		
		System.out.println(String.format("Encrypted text: %s", crypted));
		DecryptedPayload dp =  client.payloadDecrypt(toDecrypt, new String[] {b58PubKey,encPubKey});
		System.out.println(String.format("Password: %s,\noriginal payload %s,\nunencrypted payload %s\nEncoded Pub key %s\nResult %s\nPayload HEX %s", dp.getDecryptPassword(), dp.getOriginalPayload(),dp.getUnencryptedPayload(), dp.getEncodedPubKey(), dp.getResult(), dp.getUnencryptedPayloadHex() ));
		assertTrue(crypted!=null); 
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
	
}
