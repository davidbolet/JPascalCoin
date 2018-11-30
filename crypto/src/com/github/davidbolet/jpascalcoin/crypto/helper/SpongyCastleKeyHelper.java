package com.github.davidbolet.jpascalcoin.crypto.helper;
//package com.github.davidbolet.jpascalcoin.api.helpers;
//
//import java.io.UnsupportedEncodingException;
//import java.math.BigInteger;
//import java.security.InvalidKeyException;
//import java.security.KeyFactory;
//import java.security.NoSuchAlgorithmException;
//import java.security.NoSuchProviderException;
//import java.security.PrivateKey;
//import java.security.PublicKey;
//import java.security.Security;
//import java.security.Signature;
//import java.security.SignatureException;
//
//import org.spongycastle.jce.ECNamedCurveTable;
//import org.spongycastle.jce.spec.ECParameterSpec;
//import org.spongycastle.jce.spec.ECPublicKeySpec;
//import org.spongycastle.math.ec.ECPoint;
//
//public class SpongyCastleKeyHelper implements KeyHelper {
//	
//    /**
//     * Generates a new PascPrivateKey object from an string representation of a private key
//     * @param privateKeyD The private Key in string format
//     * @return PascPrivateKey
//     */
//	@Override
//	public PublicKey fromPrivateKey(BigInteger privateKeyD) {
//		try {
//			Security.addProvider(new org.spongycastle.jce.provider.BouncyCastleProvider());
//        	KeyFactory keyFactory = KeyFactory.getInstance("ECDSA", "SC");
//        	ECParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec("secp256k1");
//        	ECPoint Q = ecSpec.getG().multiply(privateKeyD);
//        	byte[] publicDerBytes = Q.getEncoded(false);
//
//        	ECPoint point = ecSpec.getCurve().decodePoint(publicDerBytes);
//        	ECPublicKeySpec pubSpec = new ECPublicKeySpec(point, ecSpec);
//        	return keyFactory.generatePublic(pubSpec);
//           
//        } catch (Exception ex) {
//
//            return null;
//        }
//    }
//	
//	/**
//	 * Generates a signature with SHA256withECDSA algorithm
//	 * @param digest Text to sign
//	 * @param key PrivateKey used to sign 
//	 * @return byte[] signed bytes
//	 * @throws SignatureException if invalid signature
//	 * @throws UnsupportedEncodingException if invalid encoding
//	 * @throws InvalidKeyException if invalid key
//	 * @throws NoSuchAlgorithmException if given key is different from SECP256K1
//	 * @throws NoSuchProviderException if provider invalid
//	 */
//	@Override
//	public byte[] generateSignature(byte[] digest, PrivateKey key) throws SignatureException, UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException{
//		Security.addProvider(new org.spongycastle.jce.provider.BouncyCastleProvider());
//		Signature ecdsaSign = Signature.getInstance("SHA256withECDSA", "SC");
//		ecdsaSign.initSign(key);
//		ecdsaSign.update(digest);
//		byte[] signature = ecdsaSign.sign();
//		return signature;
//	}
//
//	@Override
//	public boolean verifySignature(PublicKey key, byte[] signedBytes, byte[] original)
//			throws SignatureException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException {
//		Security.addProvider(new org.spongycastle.jce.provider.BouncyCastleProvider());
//		Signature signature = Signature.getInstance("SHA256withECDSA", "SC");
//		signature.initVerify(key);
//		signature.update(original);
//		return signature.verify(signedBytes);
//	}
//
//	@Override
//	public byte[] testSignature(byte[] toSign, String key) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//}
