package com.github.davidbolet.jpascalcoin.api.helpers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

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
	 
	 
}
