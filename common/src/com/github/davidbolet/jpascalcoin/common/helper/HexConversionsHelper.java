package com.github.davidbolet.jpascalcoin.common.helper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;

public class HexConversionsHelper {
	
	public static String byteToHex(byte[] in) {
	    final StringBuilder builder = new StringBuilder();
	    for(byte b : in) {
	        builder.append(String.format("%02x", b));
	    }
	    return builder.toString();
	}
	
	public static String byteToHex(byte b) {
	    final StringBuilder builder = new StringBuilder();
	    builder.append(String.format("%02x", b));
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
	
	public static String int2BigEndianHex4Byte(int num) {
		String res = "";
		String littleEndian=String.format("%08x", num);
		String[] hex=littleEndian.split("(?<=\\G.{2})");
		for (int i=hex.length-1;i>=0;i--) {
			res+=hex[i];
		}
		return res.toUpperCase();
	}
	
	public static String int2BigEndianHex8Byte(long num) {
		String res = "";
		String littleEndian=String.format("%016x", num);
		String[] hex=littleEndian.split("(?<=\\G.{2})");
		for (int i=hex.length-1;i>=0;i--) {
			res+=hex[i];
		}
		return res.toUpperCase();
	}
	
	public static String bigInt2HexByte(BigInteger bigInt) {
		String res = "";
		String littleEndian=bigInt.toString(16);
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
	

}
