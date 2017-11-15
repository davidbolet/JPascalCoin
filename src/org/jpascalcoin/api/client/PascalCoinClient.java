package org.jpascalcoin.api.client;

import java.util.List;

import org.jpascalcoin.api.model.Account;
import org.jpascalcoin.api.model.Block;
import org.jpascalcoin.api.model.Connection;
import org.jpascalcoin.api.model.DecryptedPayload;
import org.jpascalcoin.api.model.KeyType;
import org.jpascalcoin.api.model.NodeStatus;
import org.jpascalcoin.api.model.Operation;
import org.jpascalcoin.api.model.PayLoadEncryptionMethod;
import org.jpascalcoin.api.model.PublicKey;
import org.jpascalcoin.api.model.RawOperation;

/**
 * Java wrapper for API methods offered by pascalcoin wallet
 * @author davidbolet
 * November, 2017
 */
public interface PascalCoinClient {
	
			/** Adds a node to connect 																			
			*  @param nodes String containing 1 or multiple IP:port separated by ';'
			*  @return Returns an integer with nodes added */
			Integer addNode(String nodes);

			/**
			* Returns a JSON Object with account information including pending operations not included in blockchain yet, but affecting this account.		
			* To know if there are pending operations, must look at updated_b param.It tells last block that modified this account.
			* If this number is equal to blockchain blocks then this account is affected by pending operations (send/receive or change key)
			* @param account: Cardinal containing account number */
			Account getAccount(Integer account);

			/**
			* Find accounts by name/type and returns them as an array of Account objects
			* @param name: If has value, will return the account that match name
			* @param type: If has value, will return accounts with same type
			* @param status: If has value, will filter account with status as follows: 0 = all accounts, 1 = accounts for public or private sale only, 2 = accounts for private sale only, 3 = accounts for public sale only 
			* @param start: Start account (by default, 0) 
			* @param max: Max of accounts returned in array (by default, 100) 
			/*  @return */
			List<Account> findAccounts(String name, Integer type , Integer status , Integer start, Integer max);

//			Integer findAccountsCount(String name, Integer type , Integer status);

			
			/**
			/* Returns a JSON array with all wallet accounts.
			/* @param enc_pubkey:  HEXASTRING (optional). If provided, return only accounts of this public key
			/* @param b58_pubkey: String (optional). If provided, return only accounts of this public key. Note: If use enc_pubkey and b58_pubkey together and is not the same public key, will return an error
			/* @param start: Integer (optional, default = 0). If provided, will return wallet accounts starting at this position (index starts at position 0)
			/* @param max: Integer (optional, default = 100). If provided, will return max accounts. If not provided, max=100 by default
			/*  @returnEach JSON array item contains an Account Object */
			List<Account> getWalletAccounts(String encPubKey, String b58PubKey , Integer start, Integer max);

			/**
			/* Get number of available wallet accounts (total or filtered by public key)
			/* @param enc_pubkey: HEXASTRING (optional). If provided, return only accounts of this public key
			/* @param b58_pubkey: String (optional). If provided, return only accounts of this public key. Note: If use enc_pubkey and b58_pubkey together and is not the same public key, will return an error
			/* @param start: Integer (optional, default = 0). If provided, will return wallet accounts starting at this position (index starts at position 0)
			/* @param max: Integer (optional, default = 100). If provided, will return max accounts. If not provided, max=100 by default
			/*  @returnReturns an integer with total */		
			Integer getWalletAccountsCount(String encPubKey, String b58PubKey, Integer start , Integer max );

			/**
			/* Returns a JSON Object with a public key if found in the Wallet
			/* @param enc_pubkey: HEXASTRING
			/* @param b58_pubkey: String
			/* <remarks>Note: If use enc_pubkey and b58_pubkey together and is not the same public key, will return an error</remarks>
			/*  @returnReturns a JSON Object with a "Public Key Object" */
			PublicKey getWalletPubKey(String encPubKey, String b58PubKey);

			/** 
			* Returns a JSON Array with all pubkeys of the Wallet (address)
			* @param start: Integer (optional, default = 0). If provided, will return wallet public keys starting at this position (index starts at position 0)
			* @param max: Integer (optional, default = 100). If provided, will return max public keys. If not provided, max=100 by default
			* @return Returns a JSON Array with "Public Key Object" */
			List<PublicKey> getWalletPubKeys(Integer start, Integer max);

			/**
			 * Returns coins balance.
			 * 
			 * @param enc_pubkey: HEXASTRING (optional). If provided, return only this public key balance
			 * @param b58_pubkey: String (optional). If provided, return only this public key balance 
			 * If use enc_pubkey and b58_pubkey together and is not the same public key, will throw an error
			 * @return Returns a PASCURRENCY value with maximum 4 Doubles */
			Double getWalletCoins(String encPubKey, String b58PubKey);

			/**
			 * Returns a JSON Object with a block information
			 * @param block: Block number (0..blocks count-1)
			 * @return Returns a JSON Object with a "Block Object" */
			Block getBlock(Integer block);

			/**
			 * Returns a JSON Array with blocks information from "start" to "end" (or "last" n blocks) Blocks are returned in DESCENDING order. 
			 * @see GetBlock
			 * @param last: Last n blocks in the blockchain (n>0 and n&lt;=1000)
			 * @param start: From this block
			 * @param end: To this block
			 * Must use last exclusively, or start and end, or error
			 * @return JSON Array with blocks information */
			List<Block> getBlocks(Integer last, Integer start, Integer end );

			/**
			/* Returns an Integer with blockcount of node
			/*  @return Total blocks */
			Integer getBlockCount();

			/**
			 * Returns a JSON Object with an operation inside a block
			 * @param block: Block number
			 * @param opblock: Operation (0..operations-1) of this block
			 * @return JSON Object with a "Operation Object" */
			Operation getBlockOperation(Integer block, Integer opblock);

			/**
			 * Returns a JSON Array with all operations of specified block Operations are returned in DESCENDING order
			 * @param block: Block number
			 * @param start: Integer (optional, default = 0). If provided, will start at this position (index starts at position 0)
			 * @param max: Integer (optional, default = 100). If provided, will return max registers. If not provided, max=100 by default
			 * @return Returns a JSON Array with "Operation Object" items */
			List<Operation> getBlockOperations(Integer block,Integer start, Integer max);

			/**
			 * Return a JSON Array with "Operation Object" items. Operations made over an account Operations are returned in DESCENDING order
			 * @param account: Account number (0..accounts count-1)
			 * @param depth: Integer - (Optional, default value 100) Depth to search on blocks where this account has been affected. Allowed to use deep as a param name too.
			 * @param start: Integer (optional, default = 0). If provided, will start at this position (index starts at position 0). If start is -1, then will include pending operations, otherwise only operations included on the blockchain
			 * @param max: Integer (optional, default = 100). If provided, will return max registers. If not provided, max=100 by default
			 * @return Returns an array holding operations made over account in "Operation Object" format */
			List<Operation> getAccountOperations(Integer account, Integer depth, Integer start, Integer max);

			/**
			 * Return a JSON Array with "Operation Object" items with operations pending to be included at the Blockchain.
			 * @return Returns an array holding pending operations in "Operation Object" format */
			List<Operation> getPendings();

			/**
			 * Return a JSON Object in "Operation Object" format.
			 * @param ophash: HEXASTRING - Value ophash received on an operation
			 *  @return Returns "Operation Object" format JSON object */
			Operation findOperation(String ophash);

			/**
			 * Changes an account Public key, or name, or type value (at least 1 on 3)
			 * @param account_target: Account being changed
			 * @param account_signer: Account paying the fee (must have same public key as account_target)
			 * @param new_enc_pubkey: New account public key encoded in hexaDouble format
			 * @param new_b58_pubkey: New account public key encoded in base58 format
			 * @param new_name: New account name encoded in PascalCoin64 format (null means keep current name)
			 * @param new_type: New account type (null means keep current type)
			 * @param fee: PASCURRENCY - Fee of the operation
			 * @param payload: Payload "item" that will be included in this operation
			 * @param payloadMethod: Encode type of the item payload
			 * @param pwd: Used to encrypt payload with aes as a payload_method. If none equals to empty password
			 * Only one or none of new_b58_pubkey, new_enc_pubkey should be used. Populating both will result in an error.
			 * @return If operation is successfull will return a JSON Object in "Operation Object" format. */
			Operation changeAccountInfo(Integer accountTarget, Integer accountSigner, String newEncPubKey, String newB58PubKey, String newName, Short new_type, Double fee, byte[] payload, PayLoadEncryptionMethod payloadMethod, String pwd);

			/**
			 * Executes a transaction operation from "sender" to "target"
			 * @param sender: Sender account
			 * @param target: Destination account
			 * @param amount: Coins to be transferred
			 * @param fee: Fee of the operation
			 * @param payload: Payload "item" that will be included in this operation
			 * @param payloadMethod: Payload "item" that will be included in this operation
			 * @param pwd: Used to encrypt payload with aes as a payload_method. If none equals to empty password
			 * @return If transaction is successfull will return a JSON Object in "Operation Object" format. Otherwise, will return a JSON-RPC error code with description */
			Operation sendTo(Integer sender, Integer target, Double amount, Double fee, byte[] payload, PayLoadEncryptionMethod payloadMethod, String pwd);

			/**
			 * Executes a change key operation, changing "account" public key for a new one.
			 * Note that new one public key can be another Wallet public key, or none.When none, it's like a transaction, tranferring account owner to an external owner
			 * @param account: Account number to change key
			 * @param account_signer: Account that signs and pays the fee (must have same public key that delisted account, or be the same)
			 * @param new_enc_pubkey: HEXASTRING - New public key in encoded format
			 * @param new_b58_pubkey: New public key in Base 58 format (the same that Application Wallet exports)
			 * @param fee: PASCURRENCY - Fee of the operation
			 * @param payload: Payload "item" that will be included in this operation
			 * @param payloadMethod: Encode type of the item payload
			 * @param pwd: Used to encrypt payload with aes as a payload_method. If none equals to empty password
			 * Only one or none of new_b58_pubkey, new_enc_pubkey should be used. Populating both will result in an error.
			 * @return If operation is successfull will return a JSON Object in "Operation Object" format. */
			Operation changeKey(Integer account, Integer accountSigner, String newEncPubKey, String newB58PubKey, Double fee, byte[] payload, PayLoadEncryptionMethod payloadMethod, String pwd);

			/**
			 * Executes a change key operation, changing "account" public key for a new one, in multiple accounts.
			 * Works like changekey
			 * @param accounts: List of accounts separated by a comma
			 * @param new_enc_pubkey: HEXASTRING - New public key in encoded format
			 * @param new_b58_pubkey: New public key in Base 58 format (the same that Application Wallet exports)
			 * @param fee: PASCURRENCY - Fee of the operation
			 * @param payload: Payload "item" that will be included in this operation
			 * @param payloadMethod: Encode type of the item payload
			 * @param pwd: Used to encrypt payload with aes as a payload_method. If none equals to empty password
			 * @return If operation is successfull will return a JSON Array with Operation object items for each key If operation cannot be made, a JSON-RPC error message is returned */
			List<Operation> changeKeys(String accounts, String newEncPubKey, String newB58PubKey, Double fee, byte[] payload, PayLoadEncryptionMethod payloadMethod, String pwd);

			/**
			 * Lists an account for sale (public or private)
			 * @param account_target: Account to be listed
			 * @param account_signer: Account that signs and pays the fee (must have same public key that listed account, or be the same)
			 * @param price: price account can be purchased for
			 * @param seller_account: Account that will receive "price" amount on sell
			 * @param new_b58_pubkey: Base58 encoded public key (for private sale only)
			 * @param new_enc_pubkey: Hex-encoded public key (for private sale only)
			 * @param locked_until_block: Block number until this account will be locked (a locked account cannot execute operations while locked)
			 * @param fee: PASCURRENCY - Fee of the operation
			 * @param payload: Payload "item" that will be included in this operation
			 * @param payloadMethod: Encode type of the item payload
			 * @param pwd: Used to encrypt payload with aes as a payload_method. If none equals to empty password
			 * Only one or none of new_b58_pubkey, new_enc_pubkey should be used. Populating both will result in an error.
			 * @return If operation is successfull will return a JSON Object in "Operation Object" format. */
			Operation listAccountForSale(Integer accountTarget, Integer accountSigner, Double price, Integer sellerAccount, String newB58PubKey, String newEncPubKey, Integer lockedUntilBlock, Double fee, byte[] payload, PayLoadEncryptionMethod payloadMethod, String pwd);

			/**
			/*  Delist an account from sale.
			/* @param account_target: Account to be delisted
			/* @param account_signer: Account that signs and pays the fee (must have same public key that delisted account, or be the same)
			/* @param fee: PASCURRENCY - Fee of the operation
			/* @param payload: Payload "item" that will be included in this operation
			/* @param payloadMethod: Encode type of the item payload
			/* @param pwd: Used to encrypt payload with aes as a payload_method. If none equals to empty password
			/* @return If operation is successfull will return a JSON Object in "Operation Object" format. */
			Operation delistAccountForSale(Integer accountTarget, Integer accountSigner, Double fee, byte[] payload, PayLoadEncryptionMethod payloadMethod, String pwd);

			/**
			 * Buy an account currently listed for sale (public or private)
			 * @param buyer_account: Account number of buyer who is purchasing the account
			 * @param account_to_purchase: Account number being purchased
			 * @param price: Settlement price of account being purchased
			 * @param seller_account: Account of seller, receiving payment
			 * @param new_b58_pubkey: Post-settlement public key in base58 encoded format.
			 * @param new_enc_pubkey: Post-settlement public key in hexaDouble encoded format.
			 * @param amount: Amount being transferred from buyer_account to seller_account (the settlement). This is a PASCURRENCY value.
			 * @param fee: Fee of the operation. This is a PASCURRENCY value.
			 * @param payload: Payload "item" that will be included in this operation
			 * @param payloadMethod: Encode type of the item payload
			 * @param pwd: Used to encrypt payload with aes as a payload_method. If none equals to empty password
			 * @return If operation is successfull will return a JSON Object in "Operation Object" format. */
			Operation buyAccount(Integer buyerAccount, Integer accountToPurchase, Double price, Integer sellerAccount, String newB58PubKey, String newEncPubKey, Double amount, Double fee, byte[] payload, PayLoadEncryptionMethod payloadMethod, String pwd);

			/**
			 * Signs a "Change Account Info" operation, suitable for cold wallet usage.
			 * @param account_target" Account being changed 
			 * @param account_signer: Account paying the fee (must have same public key as account_target) 
			 * @param new_enc_pubkey: New account public key encoded in hexaDouble format 
			 * @param new_b58_pubkey: New account public key encoded in base58 format 
			 * @param new_name: New account name encoded in PascalCoin64 format (null means keep current name) 
			 * @param new_type: New account type (null means keep current type) 
			 * @param last_n_operation: Last value of n_operation obtained with an Account object, for example when called to getaccount 
			 * @param fee: PASCURRENCY - Fee of the operation 
			 * @param payload: Payload "item" that will be included in this operation 
			 * @param payloadMethod: Encode type of the item payload 
			 * @param pwd: Used to encrypt payload with aes as a payload_method. If none equals to empty password 
			 * @param signer_b58_pubkey: The current public key of "account_signer" in base58 encoding  
			 * @param signer_enc_pubkey: The current public key of "account_signer" in hexaDouble encoding  
			 * @param rawoperations: HEXASTRING (optional) - If we want to add a sign operation with other previous operations, here we must put previous rawoperations result 
			 * Only one or none of new_b58_pubkey, new_enc_pubkey should be used. Populating both will result in an error.
			 * @return Returns a Raw Operations Object */
			Operation signChangeAccountInfo(Integer accountTarget, Integer accountSigner, String newEncPubkey, String newB58PubKey, String newName, Short newType, Integer lastNOperation, Double fee, byte[] payload, PayLoadEncryptionMethod payloadMethod, String pwd, String signerB58PubKey, String signerEncPubKey, String rawOperations);

			/**
			 * Creates and signs a "Send to" operation without checking information and without transfering to the network. It's useful for "cold wallets" that are off-line (not synchronized with the network) and only holds private keys
			 * 	
			 * @param sender: Sender account 
			 * @param target: Target account 
			 * @param sender_enc_pubkey: HEXASTRING - Public key of the sender account in encoded format 
			 * @param sender_b58_pubkey: HEXASTRING - Public key of the sender account in base58 format 
			 * @param target_enc_pubkey: HEXASTRING - Public key of the target account in encoded format 
			 * @param target_b58_pubkey: HEXASTRING - Public key of the target account in base58 format 
			 * @param last_n_operation: Last value of n_operation obtained with an Account object, for example when called to getaccount 
			 * @param amount: Coins to be transferred 
			 * @param fee: Fee of the operation 
			 * @param payload: Payload "item" that will be included in this operation 
			 * @param payloadMethod: Encode type of the item payload 
			 * @param pwd: Used to encrypt payload with aes as a payload_method. If none equals to empty password 
			 * @param rawoperations: HEXASTRING (optional) - If we want to add a sign operation with other previous operations, here we must put previous rawoperations result 
			 * Wallet must be unlocked and sender private key (searched with provided public key) must be in wallet. No other checks are made (no checks for valid target, valid n_operation, valid amount or fee ...)
			 * Only one of sender_enc_pubkey, sender_b58_pubkey needs be provided
			 * Only one of target_enc_pubkey, target_b58_pubkey needs be provided
			 * @return Returns a Raw Operations Object */
			RawOperation signSendTo(Integer senderAccount, Integer targetAccount, String senderEncPubKey, String senderB58PubKey, String targetEncPubKey, String targetB58PubKey, Integer lastNOperation, Double amount, Double fee, byte[] payload, PayLoadEncryptionMethod payloadMethod, String pwd, String rawoperations);

			/**
			 * Creates and signs a "Change key" operation without checking information and without transfering to the network. It's useful for "cold wallets" that are off-line (not synchronized with the network) and only holds private keys
			 * @param account: Account number to change key 
			 * @param account_signer: Account that signs and pays the fee (must have same public key that delisted account, or be the same) 
			 * @param old_enc_pubkey: HEXASTRING - Public key of the account in encoded format 
			 * @param old_b58_pubkey: HEXASTRING - Public key of the account in base58 format 
			 * @param new_enc_pubkey: HEXASTRING - Public key of the new key for the account in encoded format 
			 * @param new_b58_pubkey: HEXASTRING - Public key of the new key for the account in base58 format 
			 * @param last_n_operation: Last value of n_operation obtained with an Account object, for example when called to getaccount 
			 * @param fee: Fee of the operation 
			 * @param payload: Payload "item" that will be included in this operation 
			 * @param payloadMethod: Encode type of the item payload 
			 * @param pwd: Used to encrypt payload with aes as a payload_method. If none equals to empty password 
			 * @param rawoperations: HEXASTRING (optional) - If we want to add a sign operation with other previous operations, here we must put previous rawoperations result 
			 * Wallet must be unlocked and private key (searched with provided public key) must be in wallet. No other checks are made (no checks for valid n_operation, valid fee ...) 
			 * Only one of old_enc_pubkey, old_b58_pubkey needs be provided
			 * Only one of new_enc_pubkey, new_b58_pubkey needs be provided
			 * @return Returns a Raw Operations Object */
			RawOperation signChangeKey(Integer account, Integer accountSigner, String oldEncPubKey, String oldB58PubKey, String newEncPubKey, String newB58PubKey, Integer lastNOperation, Double fee, byte[] payload, PayLoadEncryptionMethod payloadMethod, String pwd, String rawOperations);

			/**
			 * Signs a "List Account For Sale" operation. 
			 * @param account_target: Account to be listed 
			 * @param account_signer: Account that signs and pays the fee (must have same public key that listed account, or be the same) 
			 * @param price: price account can be purchased for 
			 * @param seller_account: Account that will receive "price" amount on sell 
			 * @param new_b58_pubkey: Base58 encoded public key (for private sale only) 
			 * @param new_enc_pubkey: Hex-encoded public key (for private sale only) 
			 * @param locked_until_block: Block number until this account will be locked (a locked account cannot execute operations while locked) 
			 * @param last_n_operation: Last value of n_operation obtained with an Account object, for example when called to getaccount 
			 * @param fee: PASCURRENCY - Fee of the operation 
			 * @param payload: Payload "item" that will be included in this operation 
			 * @param payloadMethod: Encode type of the item payload 
			 * @param pwd: Used to encrypt payload with aes as a payload_method. If none equals to empty password 
			 * @param signer_b58_pubkey: The current public key of "account_signer" in base58 encoding  
			 * @param signer_enc_pubkey: The current public key of "account_signer" in hexaDouble encoding  
			 * @param rawoperations: HEXASTRING (optional) - If we want to add a sign operation with other previous operations, here we must put previous rawoperations result 
			 * Only one or none of new_b58_pubkey, new_enc_pubkey should be used. Populating both will result in an error.
			 * Only one or none of signer_b58_pubkey, signer_b58_pubkey should be used. Populating both will result in an error.
			 * @return Returns a Raw Operations Object */
			RawOperation signListAccountForSale(Integer accountTarget, Integer accountSigner, Double price, Integer sellerAccount, String newB58PubKey, String newEncPubKey, Integer lockedUntilBlock, Integer lastNOperation, Double fee, byte[] payload, PayLoadEncryptionMethod payloadMethod, String pwd, String signerB58PubKey, String signerEncPubKey, String rawOperations);

			/**
			 * Signs a "Delist Account For Sale" operation, suitable for cold wallet usage.
			 * @param account_target: Account to be delisted 
			 * @param account_signer: Account that signs and pays the fee (must have same public key that delisted account, or be the same) 
			 * @param last_n_operation: Last value of n_operation obtained with an Account object, for example when called to getaccount 
			 * @param fee: PASCURRENCY - Fee of the operation 
			 * @param payload: Payload "item" that will be included in this operation 
			 * @param payloadMethod: Encode type of the item payload 
			 * @param pwd: Used to encrypt payload with aes as a payload_method. If none equals to empty password 
			 * @param signer_b58_pubkey: The current public key of "account_signer" in base58 encoding  
			 * @param signer_enc_pubkey: The current public key of "account_signer" in hexaDouble encoding  
			 * @param rawoperations: HEXASTRING (optional) - If we want to add a sign operation with other previous operations, here we must put previous rawoperations result 
			 * Only one or none of signer_b58_pubkey, signer_b58_pubkey should be used. Populating both will result in an error.
			 * @return Returns a Raw Operations Object */
			RawOperation signDelistAccountForSale(Integer accountTarget, Integer accountSigner, Integer lastNOperation, Double fee, byte[] payload, PayLoadEncryptionMethod payloadMethod, String pwd, String signerB58PubKey, String signerEncPubKey, String rawOperations);

			/**
			 * Signs a "Buy Account" operation, suitable for cold wallet usage.
			 * @param buyer_account: Account number of buyer who is purchasing the account 
			 * @param account_to_purchase: Account number being purchased 
			 * @param price: Settlement price of account being purchased 
			 * @param seller_account: Account of seller, receiving payment 
			 * @param new_b58_pubkey: Post-settlement public key in base58 encoded format. 
			 * @param new_enc_pubkey: Post-settlement public key in hexaDouble encoded format. 
			 * @param amount: Amount being transferred from buyer_account to seller_account (the settlement). This is a PASCURRENCY value. 
			 * @param last_n_operation: Last value of n_operation obtained with an Account object, for example when called to getaccount 
			 * @param fee: Fee of the operation. This is a PASCURRENCY value. 
			 * @param payload: Payload "item" that will be included in this operation 
			 * @param payloadMethod: Encode type of the item payload 
			 * @param pwd: Used to encrypt payload with aes as a payload_method. If none equals to empty password 
			 * @param signer_b58_pubkey: The current public key of "account_signer" in base58 encoding  
			 * @param signer_enc_pubkey: The current public key of "account_signer" in hexaDouble encoding  
			 * @param rawoperations: HEXASTRING (optional) - If we want to add a sign operation with other previous operations, here we must put previous rawoperations result 
			 * Only one or none of new_b58_pubkey, new_enc_pubkey should be used. Populating both will result in an error.
			 * Only one or none of signer_b58_pubkey, signer_b58_pubkey should be used. Populating both will result in an error.
			 * @return Returns a Raw Operations Object */
			RawOperation signBuyAccount(Integer buyerAccount, Integer accountToPurchase, Double price, Integer sellerAccount, String newB58PubKey, String newEncPubKey, Double amount, Integer lastNOperation, Double fee, byte[] payload, PayLoadEncryptionMethod payloadMethod, String pwd, String signerB58PubKey, String signerEncPubKey, String rawOperations);

			/**
			 * Returns information stored in a rawoperations param (obtained calling signchangekey or signsendto)
			 *
			 * @param rawoperations: HEXASTRING (obtained calling signchangekey or signsendto) 
			 * @return Returns a JSON Array with Operation Object items, one for each operation in rawoperations param. NOTE: Remember that rawoperations are operations that maybe are not correct */
			List<Operation> operationsInfo(String rawOperations);

			/**
			 * Executes operations included in rawopertions param and transfers to the network. Raw operations can include "Send to" oprations or "Change key" operations.
			 * 
			 * @param rawoperations: Executes operations included in rawopertions param and transfers to the network. Raw operations can include "Send to" oprations or "Change key" operations. 
			 * For each Operation Object item, if there is an error, param valid will be false and param errors will show error description.Otherwise, operation is correct and will contain ophash param
			 * @return Returns a JSON Array with Operation Object items, one for each operation in rawoperations param. */
			List<Operation> executeOperations(String rawOperations);

			/**
			 * Returns information of the Node in a JSON Object
			 *  @return JSON Object with information */
			NodeStatus getNodeStatus();

			/**
			 * Encodes a public key based on params information
			 * @param ec_nid: key type 
			 * @param x: HEXASTRING with x value of public key 
			 * @param y: HEXASTRING with y value of public key 
			 * @return Returns a HEXASTRING with encoded public key */
			String encodePubKey(KeyType ecNid, String x, String y);

			/**
			 * Decodes an encoded public key
			 * @param enc_pubkey: HEXASTRING with encoded public key 
			 * @param b58_pubkey: String. b58_pubkey is the same value that Application Wallet exports as a public key 
			 * Note: If use enc_pubkey and b58_pubkey together and is not the same public key, will return an error
			 * @return Returns a JSON Object with a "Public Key Object" */
			PublicKey decodePubKey(String encPubKey, String b58PubKey);

			/**
			 * Encrypt a text "payload" using "payload_method"
			 * @param payload: HEXASTRING - Text to encrypt in hexadecimal format 
			 * @param payload_method: Payload method 
			 * @param pwd: Using a Password. Must provide pwd param 
			 * @return Returns a HEXASTRING with encrypted payload */
			String payloadEncrypt(String payload, PayLoadEncryptionMethod payloadMethod, String pwd);

			/**
			 * Returns a HEXASTRING with decrypted text (a payload) using private keys in the wallet or a list of Passwords (used in "aes" encryption)
			 *
			 * @param payload: HEXASTRING - Encrypted data 
			 * @param pwds: List of passwords to use 
			 * <remarks>If using one of private keys is able to decrypt payload then returns value "key" in payload_method and enc_pubkey contains encoded public key in HEXASTRING</remarks>
			 * <remarks>If using one of passwords to decrypt payload then returns value "pwd" in payload_method and pwd contains password used</remarks>
			 *  @returnDecryped payload */
			DecryptedPayload payloadDecrypt(String payload, String[] pwds);

			/**
			 * Returns all the current connections
			 *  @returnJSON Array with Connection Objects */
			List<Connection> getConnections();

			/**
			 * Creates a new Private key and stores it on the wallet, returning an enc_pubkey value
			 * @param ec_nid: Type of key encryption 
			 * @param name: Name to alias this new private key 
			 * @return */
			PublicKey addNewKey(KeyType ecNid, String name);

			/**
			 * Locks the Wallet if it has a password, otherwise wallet cannot be locked
			 * @return Returns a Boolean indicating if Wallet is locked. If false that means that Wallet has an empty password and cannot be locked */
			Boolean lock();

			/**
			 * Unlocks a locked Wallet using "pwd" param
			 * @param pwd: 
			 * @return Returns a Boolean indicating if Wallet is unlocked after using pwd password */
			Boolean unlock(String pwd);

			/**
			 * Changes the password of the Wallet. (Must be previously unlocked) 
			 * Note: If pwd param is empty String, then wallet will be not protected by password
			 * @param pwd: New password
			 * @return Returns a Boolean if Wallet password changed with new pwd password */
			Boolean setWalletPassword(String pwd);

			/**
			 * Stops the node and the server. Closes all connections
			 *  @return Boolean true */
			Boolean stopNode();

			/**
			 * Starts the node and the server. Starts connection process
			 *  @return Boolean "true" */
			Boolean startNode();
}
