package com.github.davidbolet.jpascalcoin.crypto.helper;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;

public interface KeyHelper {
	
	PublicKey fromPrivateKey(BigInteger privateKeyD);
	byte[] generateSignature(byte[] digest, PrivateKey key) throws SignatureException, UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException;
	boolean verifySignature(PublicKey key, byte[] signedBytes, byte[] originalBytes)
			throws SignatureException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException;
	public byte[] testSignature(byte[] toSign, String key);

}
