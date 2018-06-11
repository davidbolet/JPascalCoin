package com.github.davidbolet.jpascalcoin.api.services;

import java.util.List;
import java.util.Map;

import com.github.davidbolet.jpascalcoin.api.model.Account;
import com.github.davidbolet.jpascalcoin.api.model.Block;
import com.github.davidbolet.jpascalcoin.api.model.Connection;
import com.github.davidbolet.jpascalcoin.api.model.DecodeOpHashResult;
import com.github.davidbolet.jpascalcoin.api.model.DecryptedPayload;
import com.github.davidbolet.jpascalcoin.api.model.MultiOperation;
import com.github.davidbolet.jpascalcoin.api.model.NodeStatus;
import com.github.davidbolet.jpascalcoin.api.model.OpResult;
import com.github.davidbolet.jpascalcoin.api.model.Operation;
import com.github.davidbolet.jpascalcoin.api.model.PublicKey;
import com.github.davidbolet.jpascalcoin.api.model.RawOperation;
import com.github.davidbolet.jpascalcoin.api.model.SignResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface PascalCoinService {

    @Headers({"Content-Type: application/json","User-Agent: JPascalCoin"})
	@POST("/")
	Call<OpResult<Integer>> addNode(@Body Map<String, Object> body);

    @Headers({"Content-Type: application/json","User-Agent: JPascalCoin"})
	@POST("/")
	Call<OpResult<Account>> getAccount(@Body Map<String, Object> body);
    
    @Headers({"Content-Type: application/json","User-Agent: JPascalCoin"})
	@POST("/")
	Call<OpResult<List<Account>>> findAccounts(@Body Map<String, Object> body);   
    
    @Headers({"Content-Type: application/json","User-Agent: JPascalCoin"})
	@POST("/")
	Call<OpResult<Integer>> findAccountsCount(@Body Map<String, Object> body);       

    @Headers({"Content-Type: application/json","User-Agent: JPascalCoin"})
	@POST("/")
	Call<OpResult<List<Account>>> getWalletAccounts(@Body Map<String, Object> body);   
    
    @Headers({"Content-Type: application/json","User-Agent: JPascalCoin"})
	@POST("/")
	Call<OpResult<Integer>> getWalletAccountsCount(@Body Map<String, Object> body);
    
    @Headers({"Content-Type: application/json","User-Agent: JPascalCoin"})
	@POST("/")
    Call<OpResult<PublicKey>> getWalletPubKey(@Body Map<String, Object> body);
	
    @Headers({"Content-Type: application/json","User-Agent: JPascalCoin"})
	@POST("/")
    Call<OpResult<List<PublicKey>>> getWalletPubKeys(@Body Map<String, Object> body);
    
    @Headers({"Content-Type: application/json","User-Agent: JPascalCoin"})
	@POST("/")
    Call<OpResult<Double>> getWalletCoins(@Body Map<String, Object> body);
    
    @Headers({"Content-Type: application/json","User-Agent: JPascalCoin"})
	@POST("/")
    Call<OpResult<Block>> getBlock(@Body Map<String, Object> body);
    
    @Headers({"Content-Type: application/json","User-Agent: JPascalCoin"})
	@POST("/")
    Call<OpResult<List<Block>>> getBlocks(@Body Map<String, Object> body);
    
    @Headers({"Content-Type: application/json","User-Agent: JPascalCoin"})
	@POST("/")
    Call<OpResult<Integer>> getBlockCount(@Body Map<String, Object> body);
    
    @Headers({"Content-Type: application/json","User-Agent: JPascalCoin"})
	@POST("/")
    Call<OpResult<Operation>> getBlockOperation(@Body Map<String, Object> body);
    
    @Headers({"Content-Type: application/json","User-Agent: JPascalCoin"})
	@POST("/")
    Call<OpResult<List<Operation>>> getBlockOperations(@Body Map<String, Object> body);
    
    @Headers({"Content-Type: application/json","User-Agent: JPascalCoin"})
	@POST("/")
    Call<OpResult<List<Operation>>> getAccountOperations(@Body Map<String, Object> body);
    
    @Headers({"Content-Type: application/json","User-Agent: JPascalCoin"})
	@POST("/")
    Call<OpResult<List<Operation>>> getPendings(@Body Map<String, Object> body);
    
    @Headers({"Content-Type: application/json","User-Agent: JPascalCoin"})
   	@POST("/")
    Call<OpResult<Integer>> getPendingsCount(@Body Map<String, Object> body);
    
    @Headers({"Content-Type: application/json","User-Agent: JPascalCoin"})
	@POST("/")
    Call<OpResult<Operation>> findOperation(@Body Map<String, Object> body);
    
    @Headers({"Content-Type: application/json","User-Agent: JPascalCoin"})
	@POST("/")
    Call<OpResult<Operation>> changeAccountInfo(@Body Map<String, Object> body);
    
    @Headers({"Content-Type: application/json","User-Agent: JPascalCoin"})
	@POST("/")
    Call<OpResult<Operation>> sendTo(@Body Map<String, Object> body);
    
    @Headers({"Content-Type: application/json","User-Agent: JPascalCoin"})
	@POST("/")
    Call<OpResult<Operation>> changeKey(@Body Map<String, Object> body);
    
    @Headers({"Content-Type: application/json","User-Agent: JPascalCoin"})
	@POST("/")
    Call<OpResult<List<Operation>>> changeKeys(@Body Map<String, Object> body);
    
    @Headers({"Content-Type: application/json","User-Agent: JPascalCoin"})
	@POST("/")
    Call<OpResult<Operation>> listAccountForSale(@Body Map<String, Object> body);
    
    @Headers({"Content-Type: application/json","User-Agent: JPascalCoin"})
	@POST("/")
    Call<OpResult<Operation>> delistAccountForSale(@Body Map<String, Object> body);
    
    @Headers({"Content-Type: application/json","User-Agent: JPascalCoin"})
	@POST("/")
    Call<OpResult<Operation>> buyAccount(@Body Map<String, Object> body);
    
    @Headers({"Content-Type: application/json","User-Agent: JPascalCoin"})
	@POST("/")
    Call<OpResult<Operation>> signChangeAccountInfo(@Body Map<String, Object> body);
    
    @Headers({"Content-Type: application/json","User-Agent: JPascalCoin"})
	@POST("/")
    Call<OpResult<RawOperation>> signSendTo(@Body Map<String, Object> body);
    
    @Headers({"Content-Type: application/json","User-Agent: JPascalCoin"})
	@POST("/")
    Call<OpResult<RawOperation>> signChangeKey(@Body Map<String, Object> body);
    
    @Headers({"Content-Type: application/json","User-Agent: JPascalCoin"})
	@POST("/")
    Call<OpResult<RawOperation>> signListAccountForSale(@Body Map<String, Object> body);
    
    @Headers({"Content-Type: application/json","User-Agent: JPascalCoin"})
	@POST("/")
    Call<OpResult<RawOperation>> signDelistAccountForSale(@Body Map<String, Object> body);
    
    @Headers({"Content-Type: application/json","User-Agent: JPascalCoin"})
	@POST("/")
    Call<OpResult<RawOperation>> signBuyAccount(@Body Map<String, Object> body);
    
    @Headers({"Content-Type: application/json","User-Agent: JPascalCoin"})
	@POST("/")
    Call<OpResult<List<Operation>>> operationsInfo(@Body Map<String, Object> body);
    
    @Headers({"Content-Type: application/json","User-Agent: JPascalCoin"})
	@POST("/")
    Call<OpResult<List<Operation>>> executeOperations(@Body Map<String, Object> body);
    
    @Headers({"Content-Type: application/json","User-Agent: JPascalCoin"})
	@POST("/")
    Call<OpResult<NodeStatus>> getNodeStatus(@Body Map<String, Object> body);

    @Headers({"Content-Type: application/json","User-Agent: JPascalCoin"})
	@POST("/")
    Call<OpResult<String>> encodePubKey(@Body Map<String, Object> body);
    
    @Headers({"Content-Type: application/json","User-Agent: JPascalCoin"})
	@POST("/")
    Call<OpResult<PublicKey>> decodePubKey(@Body Map<String, Object> body);
    
    @Headers({"Content-Type: application/json","User-Agent: JPascalCoin"})
	@POST("/")
    Call<OpResult<String>> payloadEncrypt(@Body Map<String, Object> body);

    @Headers({"Content-Type: application/json","User-Agent: JPascalCoin"})
	@POST("/")
    Call<OpResult<DecryptedPayload>> payloadDecrypt(@Body Map<String, Object> body);
    
    @Headers({"Content-Type: application/json","User-Agent: JPascalCoin"})
 	@POST("/")
     Call<OpResult<List<Connection>>> getConnections(@Body Map<String, Object> body);
    
    @Headers({"Content-Type: application/json","User-Agent: JPascalCoin"})
   	@POST("/")
    Call<OpResult<PublicKey>> addNewKey(@Body Map<String, Object> body);
    
    @Headers({"Content-Type: application/json","User-Agent: JPascalCoin"})
   	@POST("/")
    Call<OpResult<Boolean>> lock(@Body Map<String, Object> body);
       
    @Headers({"Content-Type: application/json","User-Agent: JPascalCoin"})
   	@POST("/")
    Call<OpResult<Boolean>> unLock(@Body Map<String, Object> body);

    @Headers({"Content-Type: application/json","User-Agent: JPascalCoin"})
   	@POST("/")
    Call<OpResult<Boolean>> setWalletPassword(@Body Map<String, Object> body);        
    
    @Headers({"Content-Type: application/json","User-Agent: JPascalCoin"})
	@POST("/")
    Call<OpResult<Boolean>> startNode(@Body Map<String, Object> body);
    
    @Headers({"Content-Type: application/json","User-Agent: JPascalCoin"})
	@POST("/")
    Call<OpResult<Boolean>> stopNode(@Body Map<String, Object> body);
    
    @Headers({"Content-Type: application/json","User-Agent: JPascalCoin"})
	@POST("/")
    Call<OpResult<Operation>> findNOperation(@Body Map<String, Object> body);   
    
    @Headers({"Content-Type: application/json","User-Agent: JPascalCoin"})
	@POST("/")
    Call<OpResult<List<Operation>>> findNOperations(@Body Map<String, Object> body);
    
    @Headers({"Content-Type: application/json","User-Agent: JPascalCoin"})
	@POST("/")
    Call<OpResult<DecodeOpHashResult>> decodeOpHash(@Body Map<String, Object> body);
    
    @Headers({"Content-Type: application/json","User-Agent: JPascalCoin"})
	@POST("/")
    Call<OpResult<SignResult>> signMessage(@Body Map<String, Object> body);
    
    @Headers({"Content-Type: application/json","User-Agent: JPascalCoin"})
	@POST("/")
    Call<OpResult<SignResult>> verifySign(@Body Map<String, Object> body);
    
    @Headers({"Content-Type: application/json","User-Agent: JPascalCoin"})
 	@POST("/")
    Call<OpResult<MultiOperation>> multiOperationAddOperation(@Body Map<String, Object> body);
    
    @Headers({"Content-Type: application/json","User-Agent: JPascalCoin"})
 	@POST("/")
    Call<OpResult<MultiOperation>> multiOperationSignOffline(@Body Map<String, Object> body);
    
    @Headers({"Content-Type: application/json","User-Agent: JPascalCoin"})
 	@POST("/")
    Call<OpResult<MultiOperation>> multiOperationSignOnline(@Body Map<String, Object> body);
 	
    @Headers({"Content-Type: application/json","User-Agent: JPascalCoin"})
 	@POST("/")
    Call<OpResult<MultiOperation>> multiOperationDeleteOperation(@Body Map<String, Object> body);
	
    
    
}
