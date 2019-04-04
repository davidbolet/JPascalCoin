# JPascalCoin

JPascalCoin is a Java library for PascalCoin. Currently supports JSON-RPC API, key generation/encryption and operation offline signing.
It's intended to be used both on Android or Java (java 1.7 is requiered).
Uses both google gson library for json deserialization and retrofit for rest calls. 
For logging uses java.util.logging, as it works by default on both Android and pure Java.
Junit tests are included.
Last version (4.0.2)
 * Version 4 is a major revision. Not only includes all rpc changes introduced by protocol v4, but also all basic cryptographic functions have been added, including key generation, offline signing/verifying, encryption/decryption and operation generation.

Version (3.0.1)
This version is aligned with wallet version 3.0.1, plus changes on findaccounts introduced by this author ;)
* Updated entity Operation as specified on [Pascalcoin repo](https://github.com/PascalCoin/PascalCoin)
* Created new entities "MultiOperation", "OpSender", "OpReceiver" and "OpChanger"
* Added "startblock" as an optional parameter to getaccountoperations 
* Now when there is an error, a RPCApiException is thrown, instead of a raw RuntimeException
* Added following params to findaccounts:

    new boolean parameter 'listed' (by default false): If true, filters returning only accounts for sale

    new boolean parameter 'exact' (by default true): If false, includes all accounts whose name starts by the value of 'name' parameter. If true returns exact match (as in the actual version)

    new double parameter 'min_balance' (by default -1). If >0, filters accounts with balance greater or equal that its value

    new double parameter 'max_balance' (by default -1). If >0, filters accounts with balance less or equal than its value


Changes on version 2.1.9
* Added new optional parameter logLevel on constructor PascalCoinClientImpl(String server, Short port, Integer log). 0 -> No log (default value) 1-Log json requests 2-More verbose log 
* Fixed bug with payloadEncrypt function, added correct parameters
* Added some missing error-handling in several methods
* Changed signature of field 'payload' on Operation class, now it's a string to avoid some issues with Base64 encoding/decoding

Changes on version 2.1.8
* Fixed bug with buyaccount and signbuyaccount functions
* Added message trace options to server calls
* removed ignored params start and max on getAccountsCount funcion

Version version 1.0.3 was renamed to 2.1.6, in order to keep alignment with PascalCoin Wallet. Previous version was 1.0.2,
which works with wallet 2.1.3. Warning: version 2.1.6 fails with wallet 2.1.3 as new functions have been added. 
However, version 1.0.2 still works fine with wallet 2.1.6

## Usage

Project has been uploaded to Maven central Repository
## Maven    
```
<dependency>   
	<groupId>com.github.davidbolet.jpascalcoin</groupId>    
	<artifactId>jPascalcoin</artifactId>      
	<version>1.0.2</version>  
</dependency>  
```

## Gradle
```
implementation 'com.github.davidbolet.jpascalcoin:jPascalcoin:1.0.2'   
```

Check junit test for full examples
You will need to include your account numbers and public keys (default key exported by wallet id b58PubKey)    


## Example:  Listing Wallet Accounts

```java
PascalCoinClient client =  new PascalCoinClientImpl("127.0.0.1",PascalCoinConstants.DEFAULT_MAINNET_RPC_PORT);
//use either <Encoded Public Key>, <B58 Public Key>
List<Account> accounts = client.getWalletAccounts("<Encoded Public Key>", "<B58 Public Key>", 0, 100);
```
## Full Example show Account's balance:
```java 
import java.io.IOException;
import com.github.davidbolet.jpascalcoin.api.client.*;
import com.github.davidbolet.jpascalcoin.api.model.Account;

public final class Example1
{
    public static void main(final String... args)
        throws IOException
    {
        PascalCoinClient client = new PascalCoinClientImpl();
        Account account = client.getAccount(0);
        System.out.println(String.format("Account's balance: %.4f PASC"+account.getBalance()));
    }
}
```

## Example: Sending funds

Unlocking wallet and sending funds

```java 
PascalCoinClient client =  new PascalCoinClientImpl("127.0.0.1",PascalCoinConstants.DEFAULT_MAINNET_RPC_PORT);
client.unlock("wallet_password");
Operation op = client.sendTo(accountSender, accountTarget, amount, fee, payload.getBytes(), PayLoadEncryptionMethod.AES, "password_to_encrypt_payload"); 
```

# Credits
Donations apreciated :)

PascalCoin -> Account 381309-50 (you can test sendTo function, if you want :)))

BitCoin -> 
![18zU5odGJqefdwzNmnj9teSJRZS5auEPRg](./bitcoin.png?raw=true "18zU5odGJqefdwzNmnj9teSJRZS5auEPRg")


David Bolet <davidbolet@gmail.com>
