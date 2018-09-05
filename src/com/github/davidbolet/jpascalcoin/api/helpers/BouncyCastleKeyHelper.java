package com.github.davidbolet.jpascalcoin.api.helpers;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;

import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.math.ec.ECPoint;

import com.github.davidbolet.jpascalcoin.exception.UsupportedKeyTypeException;

public class BouncyCastleKeyHelper {
	
    /**
     * Generates a new PascPrivateKey object from an string representation of a private key
     * @param privateKey The private Key in string format
     * @return PascPrivateKey
     * @throws UsupportedKeyTypeException if given type is other than SECP256K1
     */
	public static PublicKey fromPrivateKey(BigInteger privateKeyD) {
		try {
			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        	KeyFactory keyFactory = KeyFactory.getInstance("ECDSA", "BC");
        	ECParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec("secp256k1");
        	ECPoint Q = ecSpec.getG().multiply(privateKeyD);
        	byte[] publicDerBytes = Q.getEncoded(false);

        	ECPoint point = ecSpec.getCurve().decodePoint(publicDerBytes);
        	ECPublicKeySpec pubSpec = new ECPublicKeySpec(point, ecSpec);
        	return keyFactory.generatePublic(pubSpec);
           
        } catch (Exception ex) {

            return null;
        }
    }
	
	/**
	 * Generates a signature with SHA256withECDSA algorithm
	 * @param plaintext Text to sign
	 * @param key PrivateKey used to sign 
	 * @return byte[] signed bytes
	 * @throws SignatureException
	 * @throws UnsupportedEncodingException
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchProviderException
	 */
	public static byte[] generateSignature(String plaintext, PrivateKey key) throws SignatureException, UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException{
		Signature ecdsaSign = Signature.getInstance("SHA256withECDSA", "BC");
		ecdsaSign.initSign(key);
		ecdsaSign.update(plaintext.getBytes("UTF-8"));
		byte[] signature = ecdsaSign.sign();
		return signature;
	}

}
