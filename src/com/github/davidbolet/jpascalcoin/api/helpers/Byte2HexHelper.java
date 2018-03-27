package com.github.davidbolet.jpascalcoin.api.helpers;

public class Byte2HexHelper {
	


public static String byteToHex(byte[] in) {
    final StringBuilder builder = new StringBuilder();
    for(byte b : in) {
        builder.append(String.format("%02x", b));
    }
    return builder.toString();
}



}
