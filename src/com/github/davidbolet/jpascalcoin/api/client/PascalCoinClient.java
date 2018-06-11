package com.github.davidbolet.jpascalcoin.api.client;

import java.util.List;

import com.github.davidbolet.jpascalcoin.api.model.Account;
import com.github.davidbolet.jpascalcoin.api.model.AccountKey;
import com.github.davidbolet.jpascalcoin.api.model.Block;
import com.github.davidbolet.jpascalcoin.api.model.Connection;
import com.github.davidbolet.jpascalcoin.api.model.DecodeOpHashResult;
import com.github.davidbolet.jpascalcoin.api.model.DecryptedPayload;
import com.github.davidbolet.jpascalcoin.api.model.KeyType;
import com.github.davidbolet.jpascalcoin.api.model.MultiOperation;
import com.github.davidbolet.jpascalcoin.api.model.NodeStatus;
import com.github.davidbolet.jpascalcoin.api.model.OpChanger;
import com.github.davidbolet.jpascalcoin.api.model.OpReceiver;
import com.github.davidbolet.jpascalcoin.api.model.OpSender;
import com.github.davidbolet.jpascalcoin.api.model.Operation;
import com.github.davidbolet.jpascalcoin.api.model.PayLoadEncryptionMethod;
import com.github.davidbolet.jpascalcoin.api.model.PublicKey;
import com.github.davidbolet.jpascalcoin.api.model.RawOperation;
import com.github.davidbolet.jpascalcoin.api.model.SignResult;

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
			* @param account: Cardinal containing account number 
			* @return Account the account object */
			Account getAccount(Integer account);
			
			/**
			 * Returns a JSON array with all wallet accounts.
			 * @param encPubKey:  HEXASTRING (optional). If provided, return only accounts of this public key
			 * @param b58PubKey: String (optional). If provided, return only accounts of this public key. Note: If use enc_pubkey and b58_pubkey together and is not the same public key, will return an error
			 * @param start: Integer (optional, default = 0). If provided, will return wallet accounts starting at this position (index starts at position 0)
			 * @param max: Integer (optional, default = 100). If provided, will return max accounts. If not provided, max=100 by default
			 * @return Each JSON array item contains an Account Object */
			List<Account> getWalletAccounts(String encPubKey, String b58PubKey , Integer start, Integer max);

			/**
			 * Get number of available wallet accounts (total or filtered by public key)
			 * @param encPubKey: HEXASTRING (optional). If provided, return only accounts of this public key
			 * @param b58PubKey: String (optional). If provided, return only accounts of this public key. Note: If use enc_pubkey and b58_pubkey together and is not the same public key, will return an error
			 * @return Returns an integer with total */		
			Integer getWalletAccountsCount(String encPubKey, String b58PubKey);

			/** 
			* Returns a JSON Array with all pubkeys of the Wallet (address)
			* @param start: Integer (optional, default = 0). If provided, will return wallet public keys starting at this position (index starts at position 0)
			* @param max: Integer (optional, default = 100). If provided, will return max public keys. If not provided, max=100 by default
			* @return Returns a JSON Array with "Public Key Object" */
			List<PublicKey> getWalletPubKeys(Integer start, Integer max);
			
			/**
			 * Returns a JSON Object with a public key if found in the Wallet
			 * @param encPubKey: HEXASTRING
			 * @param b58PubKey: String
			 * Note: If use enc_pubkey and b58_pubkey together and is not the same public key, will return an error
			 * @return Returns a JSON Object with a "Public Key Object" */
			PublicKey getWalletPubKey(String encPubKey, String b58PubKey);

			/**
			 * Returns coins balance.
			 * 
			 * @param encPubKey: HEXASTRING (optional). If provided, return only this public key balance
			 * @param b58PubKey: String (optional). If provided, return only this public key balance 
			 * If use encPubKey and b58PubKey together and is not the same public key, will throw an error
			 * @return Returns a PASCURRENCY value with maximum 4 Doubles */
			Double getWalletCoins(String encPubKey, String b58PubKey);

			/**
			 * Returns a JSON Object with a block information
			 * @param block: Block number (0..blocks count-1)
			 * @return Returns a JSON Object with a "Block Object" */
			Block getBlock(Integer block);

			/**
			 * Returns a JSON Array with blocks information from "start" to "end" (or "last" n blocks) Blocks are returned in DESCENDING order. 
			 * @see getBlock
			 * @param last: Last n blocks in the blockchain (n&gt;0 and n&lt;=1000)
			 * @param start: From this block
			 * @param end: To this block
			 * Must use last exclusively, or start and end, or error
			 * @return JSON Array with blocks information */
			List<Block> getBlocks(Integer last, Integer start, Integer end );

			/**
			 * Returns an Integer with blockcount of node
			 * @return Total blocks */
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
			 * Return a JSON Array with "Operation Object" items. Operations made over an account Operations are returned in DESCENDING order
			 * @param account: Account number (0..accounts count-1)
			 * @param startblock: Integer - (Optional, default value 0) start searching backwards on a specific block where this account has been affected. Allowed to use deep as a param name too.
			 * @param depth: Integer - (Optional, default value 100) Depth to search on blocks where this account has been affected. Allowed to use deep as a param name too.
			 * @param start: Integer (optional, default = 0). If provided, will start at this position (index starts at position 0). If start is -1, then will include pending operations, otherwise only operations included on the blockchain
			 * @param max: Integer (optional, default = 100). If provided, will return max registers. If not provided, max=100 by default
			 * @return Returns an array holding operations made over account in "Operation Object" format */
			List<Operation> getAccountOperations(Integer account, Integer startblock, Integer depth, Integer start, Integer max);
			
			
			/**
			 * Return a JSON Array with "Operation Object" items with operations pending to be included at the Blockchain.
			 * @return Returns an array holding pending operations in "Operation Object" format */
			List<Operation> getPendings();
			
			
			/**
			 * Return an Integer with item count of operations pending to be included at the Blockchain.
			 * @return Returns an Integer with number of pending operations*/
			Integer getPendingsCount();

			/**
			 * Return a JSON Object in "Operation Object" format.
			 * @param ophash: HEXASTRING - Value ophash received on an operation
			 *  @return Returns "Operation Object" format JSON object */
			Operation findOperation(String ophash);
			
			/**
			 * Search an operation made to an account based on n_operation field 
			 * Return a JSON Object in "Operation Object" format.
			 * @param account Account number
			 * @param nOperation: is an incremental value to protect double spend 
			 *  @return Returns "Operation Object" format JSON object */
			Operation findNOperation(Integer account,Integer nOperation);
			
			/**
			 * Return a JSON Array with "Operation Object" Search an operation made to an account based on n_operation .
			 * @param account Account number
			 * @param nOperationMin Min n_operation to search
			 * @param nOperationMax  Max n_operation to search
			 * @param startBlock  (optional) Block number to start search. 0=Search all, including pending operations
			 * @return Returns an array holding pending operations in "Operation Object" format
			 */
			List<Operation> findNOperations(Integer account, Integer nOperationMin, Integer nOperationMax, Integer startBlock);

			/**
			* Find accounts by name/type and returns them as an array of Account objects
			* @param name: If has value, will return the account that match name
			* @param type: If has value, will return accounts with same type
			* @param start: Starting account number (by default, 0) 
			* @param max: Max of accounts returned in array (by default, 100) 
			* @return list of accounts matching selected criteria
			* */
			List<Account> findAccounts(String name, Integer type, Integer start, Integer max);
			
			/**
			 * Find accounts by name/type/forSale/balanceMin/balanceMax and returns them as an array of Account objects
			 * @param name If has value, will be used to find the account that matches name
			 * @param exact Used in conjunction with name. By default is set to true. If it's true,will return that matches exactly with name, otherwise will return all Account objects 
			 * 				whose name starts with name's parameter value
			 * @param type  If has value, will return accounts with same type
			 * @param listed By default set to false. If it's set to true, will return only accounts for sale
			 * @param minBalance If set, will return accounts whose balance is greater or equal than its value
			 * @param maxBalance If set, will return accounts whose balance is less or equal than its value
			 * @param start Starting account number (by default, 0) 
			 * @param max Max of accounts returned in result's array (by default, 100) 
			 * @return list of accounts matching selected criteria
			 */
			List<Account> findAccounts(String name, Boolean exact, Integer type, Boolean listed, Double minBalance, Double maxBalance,
					Integer start, Integer max);
			
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
			 * @param accountSigner: Account that signs and pays the fee (must have same public key that delisted account, or be the same)
			 * @param newEncPubKey: HEXASTRING - New public key in encoded format
			 * @param newB58PubKey: New public key in Base 58 format (the same that Application Wallet exports)
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
			 * @param newEncPubKey: HEXASTRING - New public key in encoded format
			 * @param newB58PubKey: New public key in Base 58 format (the same that Application Wallet exports)
			 * @param fee: PASCURRENCY - Fee of the operation
			 * @param payload: Payload "item" that will be included in this operation
			 * @param payloadMethod: Encode type of the item payload
			 * @param pwd: Used to encrypt payload with aes as a payload_method. If none equals to empty password
			 * @return If operation is successfull will return a JSON Array with Operation object items for each key If operation cannot be made, a JSON-RPC error message is returned */
			List<Operation> changeKeys(String accounts, String newEncPubKey, String newB58PubKey, Double fee, byte[] payload, PayLoadEncryptionMethod payloadMethod, String pwd);

			/**
			 * Lists an account for sale (public or private)
			 * Only one or none of new_b58_pubkey, new_enc_pubkey should be used. Populating both will result in an error.
			 * @param accountTarget: Account to be listed
			 * @param accountSigner: Account that signs and pays the fee (must have same public key that listed account, or be the same)
			 * @param price: price account can be purchased for
			 * @param sellerAccount: Account that will receive "price" amount on sell
			 * @param newB58PubKey: Base58 encoded public key (for private sale only)
			 * @param newEncPubKey: Hex-encoded public key (for private sale only)
			 * @param lockedUntilBlock: Block number until this account will be locked (a locked account cannot execute operations while locked)
			 * @param fee: PASCURRENCY - Fee of the operation
			 * @param payload: Payload "item" that will be included in this operation
			 * @param payloadMethod: Encode type of the item payload
			 * @param pwd: Used to encrypt payload with aes as a payload_method. If none equals to empty password
			 * @return If operation is successful will return a JSON Object in "Operation Object" format. 
			 * */
			Operation listAccountForSale(Integer accountTarget, Integer accountSigner, Double price, Integer sellerAccount, String newB58PubKey, String newEncPubKey, Integer lockedUntilBlock, Double fee, byte[] payload, PayLoadEncryptionMethod payloadMethod, String pwd);

			/**
			 * Delist an account from sale.
			 * @param accountTarget: Account to be delisted
			 * @param accountSigner: Account that signs and pays the fee (must have same public key that delisted account, or be the same)
			 * @param fee: PASCURRENCY - Fee of the operation
			 * @param payload: Payload "item" that will be included in this operation
			 * @param payloadMethod: Encode type of the item payload
			 * @param pwd: Used to encrypt payload with aes as a payload_method. If none equals to empty password
			 * @return If operation is successfull will return a JSON Object in "Operation Object" format. */
			Operation delistAccountForSale(Integer accountTarget, Integer accountSigner, Double fee, byte[] payload, PayLoadEncryptionMethod payloadMethod, String pwd);

			/**
			 * Buy an account currently listed for sale (public or private)
			 * @param buyerAccount: Account number of buyer who is purchasing the account
			 * @param accountToPurchase: Account number being purchased
			 * @param price: Settlement price of account being purchased
			 * @param sellerAccount: Account of seller, receiving payment
			 * @param newB58PubKey: Post-settlement public key in base58 encoded format.
			 * @param newEncPubKey: Post-settlement public key in hexaDouble encoded format.
			 * @param amount: Amount being transferred from buyer_account to seller_account (the settlement). This is a PASCURRENCY value.
			 * @param fee: Fee of the operation. This is a PASCURRENCY value.
			 * @param payload: Payload "item" that will be included in this operation
			 * @param payloadMethod: Encode type of the item payload
			 * @param pwd: Used to encrypt payload with aes as a payload_method. If none equals to empty password
			 * @return If operation is successfull will return a JSON Object in "Operation Object" format. */
			Operation buyAccount(Integer buyerAccount, Integer accountToPurchase, Double price, Integer sellerAccount, String newB58PubKey, String newEncPubKey, Double amount, Double fee, byte[] payload, PayLoadEncryptionMethod payloadMethod, String pwd);

			/**
			 * Changes an account Public key, or name, or type value (at least 1 on 3)
			 * @param accountTarget: Account being changed
			 * @param accountSigner: Account paying the fee (must have same public key as account_target)
			 * @param newEncPubKey: New account public key encoded in hexaDouble format
			 * @param newB58PubKey: New account public key encoded in base58 format
			 * @param newName: New account name encoded in PascalCoin64 format (null means keep current name)
			 * @param newType: New account type (null means keep current type)
			 * @param fee: PASCURRENCY - Fee of the operation
			 * @param payload: Payload "item" that will be included in this operation
			 * @param payloadMethod: Encode type of the item payload
			 * @param pwd: Used to encrypt payload with aes as a payload_method. If none equals to empty password
			 * Only one or none of new_b58_pubkey, new_enc_pubkey should be used. Populating both will result in an error.
			 * @return If operation is successfull will return a JSON Object in "Operation Object" format. */
			Operation changeAccountInfo(Integer accountTarget, Integer accountSigner, String newEncPubKey, String newB58PubKey, String newName, Short newType, Double fee, byte[] payload, PayLoadEncryptionMethod payloadMethod, String pwd);
			
			/**
			 * Creates and signs a "Send to" operation without checking information and without transfering to the network. It's useful for "cold wallets" that are off-line (not synchronized with the network) and only holds private keys
			 * 	
			 * @param senderAccount: Sender account 
			 * @param targetAccount: Target account 
			 * @param senderEncPubKey: HEXASTRING - Public key of the sender account in encoded format 
			 * @param senderB58PubKey: HEXASTRING - Public key of the sender account in base58 format 
			 * @param targetEncPubKey: HEXASTRING - Public key of the target account in encoded format 
			 * @param targetB58PubKey: HEXASTRING - Public key of the target account in base58 format 
			 * @param lastNOperation: Last value of n_operation obtained with an Account object, for example when called to getaccount 
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
			 * Wallet must be unlocked and private key (searched with provided public key) must be in wallet. No other checks are made (no checks for valid n_operation, valid fee ...) 
			 * Only one of oldEncPubKey, oldB58PubKey needs be provided
			 * Only one of newEncPubKey, newB58PubKey needs be provided
			 * @param account: Account number to change key 
			 * @param accountSigner: Account that signs and pays the fee (must have same public key that delisted account, or be the same) 
			 * @param oldEncPubKey: HEXASTRING - Public key of the account in encoded format 
			 * @param oldB58PubKey: HEXASTRING - Public key of the account in base58 format 
			 * @param newEncPubKey: HEXASTRING - Public key of the new key for the account in encoded format 
			 * @param newB58PubKey: HEXASTRING - Public key of the new key for the account in base58 format 
			 * @param lastNOperation: Last value of n_operation obtained with an Account object, for example when called to getaccount 
			 * @param fee: Fee of the operation 
			 * @param payload: Payload "item" that will be included in this operation 
			 * @param payloadMethod: Encode type of the item payload 
			 * @param pwd: Used to encrypt payload with aes as a payload_method. If none equals to empty password 
			 * @param rawOperations: HEXASTRING (optional) - If we want to add a sign operation with other previous operations, here we must put previous rawoperations result 
			 * @return Returns a Raw Operations Object 
			 * */
			RawOperation signChangeKey(Integer account, Integer accountSigner, String oldEncPubKey, String oldB58PubKey, String newEncPubKey, String newB58PubKey, Integer lastNOperation, Double fee, byte[] payload, PayLoadEncryptionMethod payloadMethod, String pwd, String rawOperations);

			/**
			 * Signs a "List Account For Sale" operation. 
			 * @param accountTarget: Account to be listed 
			 * @param accountSigner: Account that signs and pays the fee (must have same public key that listed account, or be the same) 
			 * @param price: price account can be purchased for 
			 * @param sellerAccount: Account that will receive "price" amount on sell 
			 * @param newB58PubKey: Base58 encoded public key (for private sale only) 
			 * @param newEncPubKey: Hex-encoded public key (for private sale only) 
			 * @param lockedUntilBlock: Block number until this account will be locked (a locked account cannot execute operations while locked) 
			 * @param lastNOperation: Last value of n_operation obtained with an Account object, for example when called to getaccount 
			 * @param fee: PASCURRENCY - Fee of the operation 
			 * @param payload: Payload "item" that will be included in this operation 
			 * @param payloadMethod: Encode type of the item payload 
			 * @param pwd: Used to encrypt payload with aes as a payload_method. If none equals to empty password 
			 * @param signerB58PubKey: The current public key of "account_signer" in base58 encoding  
			 * @param signerEncPubKey: The current public key of "account_signer" in hexaDouble encoding  
			 * @param rawOperations: HEXASTRING (optional) - If we want to add a sign operation with other previous operations, here we must put previous rawoperations result 
			 * Only one or none of new_b58_pubkey, new_enc_pubkey should be used. Populating both will result in an error.
			 * Only one or none of signer_b58_pubkey, signer_b58_pubkey should be used. Populating both will result in an error.
			 * @return Returns a Raw Operations Object */
			RawOperation signListAccountForSale(Integer accountTarget, Integer accountSigner, Double price, Integer sellerAccount, String newB58PubKey, String newEncPubKey, Integer lockedUntilBlock, Integer lastNOperation, Double fee, byte[] payload, PayLoadEncryptionMethod payloadMethod, String pwd, String signerB58PubKey, String signerEncPubKey, String rawOperations);

			/**
			 * Signs a "Delist Account For Sale" operation, suitable for cold wallet usage.
			 * @param accountTarget: Account to be delisted 
			 * @param accountSigner: Account that signs and pays the fee (must have same public key that delisted account, or be the same) 
			 * @param lastNOperation: Last value of n_operation obtained with an Account object, for example when called to getaccount 
			 * @param fee: PASCURRENCY - Fee of the operation 
			 * @param payload: Payload "item" that will be included in this operation 
			 * @param payloadMethod: Encode type of the item payload 
			 * @param pwd: Used to encrypt payload with aes as a payload_method. If none equals to empty password 
			 * @param signerB58PubKey: The current public key of "account_signer" in base58 encoding  
			 * @param signerEncPubKey: The current public key of "account_signer" in hexaDouble encoding  
			 * @param rawOperations: HEXASTRING (optional) - If we want to add a sign operation with other previous operations, here we must put previous rawoperations result 
			 * Only one or none of signer_b58_pubkey, signer_b58_pubkey should be used. Populating both will result in an error.
			 * @return Returns a Raw Operations Object */
			RawOperation signDelistAccountForSale(Integer accountTarget, Integer accountSigner, Integer lastNOperation, Double fee, byte[] payload, PayLoadEncryptionMethod payloadMethod, String pwd, String signerB58PubKey, String signerEncPubKey, String rawOperations);

			/**
			 * Signs a "Buy Account" operation, suitable for cold wallet usage.
			 * @param buyerAccount: Account number of buyer who is purchasing the account 
			 * @param accountToPurchase: Account number being purchased 
			 * @param price: Settlement price of account being purchased 
			 * @param sellerAccount: Account of seller, receiving payment 
			 * @param newB58PubKey: Post-settlement public key in base58 encoded format. 
			 * @param newEncPubKey: Post-settlement public key in hexaDouble encoded format. 
			 * @param amount: Amount being transferred from buyer_account to seller_account (the settlement). This is a PASCURRENCY value. 
			 * @param lastNOperation: Last value of n_operation obtained with an Account object, for example when called to getaccount 
			 * @param fee: Fee of the operation. This is a PASCURRENCY value. 
			 * @param payload: Payload "item" that will be included in this operation 
			 * @param payloadMethod: Encode type of the item payload 
			 * @param pwd: Used to encrypt payload with aes as a payload_method. If none equals to empty password 
			 * @param signerB58PubKey: The current public key of "account_signer" in base58 encoding  
			 * @param signerEncPubKey: The current public key of "account_signer" in hexaDouble encoding  
			 * @param rawOperations: HEXASTRING (optional) - If we want to add a sign operation with other previous operations, here we must put previous rawoperations result 
			 * Only one or none of new_b58_pubkey, new_enc_pubkey should be used. Populating both will result in an error.
			 * Only one or none of signer_b58_pubkey, signer_b58_pubkey should be used. Populating both will result in an error.
			 * @return Returns a Raw Operations Object */
			RawOperation signBuyAccount(Integer buyerAccount, Integer accountToPurchase, Double price, Integer sellerAccount, String newB58PubKey, String newEncPubKey, Double amount, Integer lastNOperation, Double fee, byte[] payload, PayLoadEncryptionMethod payloadMethod, String pwd, String signerB58PubKey, String signerEncPubKey, String rawOperations);
			
			/**
			 * Signs a "Change Account Info" operation, suitable for cold wallet usage.
			 * @param accountTarget" Account being changed 
			 * @param accountSigner: Account paying the fee (must have same public key as account_target) 
			 * @param newEncPubkey: New account public key encoded in hexaDouble format 
			 * @param newB58PubKey: New account public key encoded in base58 format 
			 * @param newName: New account name encoded in PascalCoin64 format (null means keep current name) 
			 * @param newType: New account type (null means keep current type) 
			 * @param lastNOperation: Last value of n_operation obtained with an Account object, for example when called to getaccount 
			 * @param fee: PASCURRENCY - Fee of the operation 
			 * @param payload: Payload "item" that will be included in this operation 
			 * @param payloadMethod: Encode type of the item payload 
			 * @param pwd: Used to encrypt payload with aes as a payload_method. If none equals to empty password 
			 * @param signerB58PubKey: The current public key of "account_signer" in base58 encoding  
			 * @param signerEncPubKey: The current public key of "account_signer" in hexaDouble encoding  
			 * @param rawOperations: HEXASTRING (optional) - If we want to add a sign operation with other previous operations, here we must put previous rawoperations result 
			 * Only one or none of new_b58_pubkey, new_enc_pubkey should be used. Populating both will result in an error.
			 * @return Returns a Raw Operations Object */
			Operation signChangeAccountInfo(Integer accountTarget, Integer accountSigner, String newEncPubkey, String newB58PubKey, String newName, Short newType, Integer lastNOperation, Double fee, byte[] payload, PayLoadEncryptionMethod payloadMethod, String pwd, String signerB58PubKey, String signerEncPubKey, String rawOperations);

			/**
			 * Returns information stored in a rawoperations param (obtained calling signchangekey or signsendto)
			 *
			 * @param rawOperations: HEXASTRING (obtained calling signchangekey or signsendto) 
			 * @return Returns a JSON Array with Operation Object items, one for each operation in rawoperations param. NOTE: Remember that rawoperations are operations that maybe are not correct */
			List<Operation> operationsInfo(String rawOperations);

			/**
			 * Executes operations included in rawopertions param and transfers to the network. Raw operations can include "Send to" oprations or "Change key" operations.
			 * 
			 * @param rawOperations: Executes operations included in rawopertions param and transfers to the network. Raw operations can include "Send to" oprations or "Change key" operations. 
			 * For each Operation Object item, if there is an error, param valid will be false and param errors will show error description.Otherwise, operation is correct and will contain ophash param
			 * @return Returns a JSON Array with Operation Object items, one for each operation in rawoperations param. */
			List<Operation> executeOperations(String rawOperations);

			/**
			 * Returns information of the Node in a JSON Object
			 *  @return JSON Object with information */
			NodeStatus getNodeStatus();

			/**
			 * Encodes a public key based on params information
			 * @param ecNid: key type 
			 * @param x: HEXASTRING with x value of public key 
			 * @param y: HEXASTRING with y value of public key 
			 * @return Returns a HEXASTRING with encoded public key */
			String encodePubKey(KeyType ecNid, String x, String y);

			/**
			 * Decodes an encoded public key
			 * @param encPubKey: HEXASTRING with encoded public key 
			 * @param b58PubKey: String. b58_pubkey is the same value that Application Wallet exports as a public key 
			 * Note: If use enc_pubkey and b58_pubkey together and is not the same public key, will return an error
			 * @return Returns a JSON Object with a "Public Key Object" */
			PublicKey decodePubKey(String encPubKey, String b58PubKey);

			/**
			 * Encrypt a text "payload" using "payload_method"
			 * @param payload: HEXASTRING - Text to encrypt in hexadecimal format 
			 * @param payloadMethod: Payload method. Possible values are 'aes' 'pubkey' or 'none'
			 * @param pwd: Using a Password. Must provide pwd param 
			 * @param encPubKey: Public key in encoded format to use if payloadMethod='pubkey'
			 * @param b58PubKey: Public key in b58 format to use if payloadMethod='pubkey'
			 * @return Returns a HEXASTRING with encrypted payload */
			String payloadEncrypt(String payload, String payloadMethod, String pwd, String encPubKey, String b58PubKey);

			/**
			 * Returns a HEXASTRING with decrypted text (a payload) using private keys in the wallet or a list of Passwords (used in "aes" encryption)
			 *
			 * @param payload: HEXASTRING - Encrypted data 
			 * @param pwds: List of passwords to use 
			 * If using one of private keys is able to decrypt payload then returns value "key" in payload_method and enc_pubkey contains encoded public key in HEXASTRING
			 * If using one of passwords to decrypt payload then returns value "pwd" in payload_method and pwd contains password used
			 * @return Decryped payload */
			DecryptedPayload payloadDecrypt(String payload, String[] pwds);

			/**
			 * Returns all the current connections
			 * @return JSON Array with Connection Objects */
			List<Connection> getConnections();

			/**
			 * Creates a new Private key and stores it on the wallet, returning an enc_pubkey value
			 * @param ecNid: Type of key encryption 
			 * @param name: Name to alias this new private key 
			 * @return PublicKey object for new generated privateKey
			 * */
			PublicKey addNewKey(KeyType ecNid, String name);

			/**
			 * Locks the Wallet if it has a password, otherwise wallet cannot be locked
			 * @return Returns a Boolean indicating if Wallet is locked. If false that means that Wallet has an empty password and cannot be locked */
			Boolean lock();

			/**
			 * Unlocks a locked Wallet using "pwd" param
			 * @param pwd  Wallet password
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
			
			/**
			 * Decodes block/account/n_operation info of a 32 bytes ophash
			 * @param ophash HEXASTRING with an ophash (ophash is 32 bytes, so must be 64 hexa valid chars)
			 * @return DecodeOpHashResult Object. Its fields mean the following:
			 * 
		     * "block" : Integer. Block number. 0=unknown or pending
		     * "account" : Integer. Account number
		     * "n_operation" : Integer. n_operation used by the account. n_operation is an incremental value, cannot be used twice on same account.
		     * "md160hash" : HEXASTRING with MD160 hash
			 */
			DecodeOpHashResult decodeOpHash(String ophash);

			/**
			 * Signs a digest message using a public key
			 * @param digest: HEXASTRING with the message to sign
			 * @param encPubKey: Public key in encoded format
			 * @param b58PubKey: Public key in b58 format 
			 * @return SignResult object: { digest : HEXASTRING with the message to sign-enc_pubkey : HEXASTRING with the public key that used to sign "digest" data- signature : HEXASTRING with signature}
			 */
			SignResult signMessage(String digest, String encPubKey, String b58PubKey);

			/**
			 * Verify if a digest message is signed by a public key
			 * @param digest: HEXASTRING with the message to sign
			 * @param encPubKey: Public key in encoded format
			 * @param signature: Signature generated by signMessage function
			 * @return SignResult object: { digest : HEXASTRING with the message to sign-enc_pubkey : HEXASTRING with the public key that used to sign "digest" data- signature : HEXASTRING with signature}
			 */
			SignResult verifySign(String digest, String encPubKey, String signature);
			
			/**
			 * Calculates valid checksum from the given account
			 * @param account Integer account
			 * @return String the corresponding checksum
			 */
			Integer calculateChecksum(Integer account);

			/**
			 * Adds operations to a multioperation (or creates a new multioperation and adds new operations)
			 * @param rawoperations: Previous operations
			 * @param autoNOperation:  Will fill n_operation (if not provided). Only valid if wallet is ONLINE (no cold wallets)
			 * @param senders: ARRAY of objects that will be Senders of the multioperation 
			 * @param receivers: ARRAY of objects that will be Receivers of the multioperation 
			 * @param changers: ARRAY of objects that will be accounts executing a changing info 
			 * @return MultiOperation Object, if everything was fine
			 */
			MultiOperation multiOperationAddOperation(String rawoperations, Boolean autoNOperation,
					List<OpSender> senders, List<OpReceiver> receivers, List<OpChanger> changers);

			/**
			 * This method will sign a Multioperation found in a "rawoperations", must provide all n_operation info of each signer because can work in cold wallets
			 * @param rawoperations: HEXASTRING with 1 multioperation in Raw format
			 * @param signers: ARRAY of AccountKey objects with info about accounts and public keys to sign 
			 * @return If success will return a "MultiOperation Object"
			 */
			MultiOperation multiOperationSignOffline(String rawoperations, List<AccountKey> signers);
			
			/**
			 * This method will sign a Multioperation found in a "rawoperations" based on current safebox state public keys
			 * @param rawoperations: HEXASTRING with 1 multioperation in Raw format
			 * @return If success will return a "MultiOperation Object"
			 */
			MultiOperation multiOperationSignOnline(String rawoperations);
			
			/**
			 * This method will sign a Multioperation found in a "rawoperations" based on current safebox state public keys
			 * @param rawoperations: HEXASTRING with 1 multioperation in Raw format
			 * @param index: Index of the operation to remove
			 * @return If success will return a "MultiOperation Object"
			 */
			MultiOperation multiOperationDeleteOperation(String rawoperations,Integer index);

			

			
}
