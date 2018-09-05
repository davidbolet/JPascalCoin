package com.github.davidbolet.jpascalcoin.api.helpers;

import java.util.List;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.github.davidbolet.jpascalcoin.api.model.Operation;
import com.github.davidbolet.jpascalcoin.api.model.internal.TPCOperation;

public class HexConversionsHelper {
	
	public static String byteToHex(byte[] in) {
	    final StringBuilder builder = new StringBuilder();
	    for(byte b : in) {
	        builder.append(String.format("%02x", b));
	    }
	    return builder.toString();
	}
	
	public static String int2BigEndianHex(int num) {
		String res = "";
		String littleEndian=String.format("%04x", num);
		String[] hex=littleEndian.split("(?<=\\G.{2})");
		for (int i=hex.length-1;i>=0;i--) {
			res+=hex[i];
		}
		return res.toUpperCase();
	}
	
	public static int hexBigEndian2Int(String hexValue)
	{
		int result=0;
		String[] hex=hexValue.split("(?<=\\G.{2})");
		int exp=0;
		for (int i=0; i<hex.length;i++) {
			exp=(int)Math.pow(16,i*2);
			result+=Integer.parseInt(hex[i], 16)*exp;
		}
		return result;
	}

	public static byte[] decodeStr2Hex(String data)
	{
		ByteArrayOutputStream	bOut = new ByteArrayOutputStream();
		 HexEncoder encoder= new HexEncoder();
	     try {
		     encoder.decode(data, bOut);
		 }
		 catch (IOException e)
		 {
			 throw new RuntimeException("exception decoding Hex string: " + e);
		 }
		 return bOut.toByteArray();
	}
	
	
	public List<Operation> fromRawOperations(String rawOperation) {
		
		List<Operation> result=new ArrayList<>();
		
		//"01000000090000000300010014D10C00D007000000000000070000000000000000000100DAEE0100D007000000000000180054686973205061796C6F616420746F2072656365697665720000"
		//"senders":[{"account":839956,"n_operation":7,"amount":-0.2,"payload":""}],"receivers":[{"account":126682,"amount":0.2,"payload":"54686973205061796C6F616420746F207265636569766572"}],"changers":[],"amount":0.2,"fee":0,"senders_count":1,"receivers_count":1,"changesinfo_count":0,"signed_count":0,"not_signed_count":1,"signed_can_execute":false}
		
		return null;
		
	}
	 
	 
}
