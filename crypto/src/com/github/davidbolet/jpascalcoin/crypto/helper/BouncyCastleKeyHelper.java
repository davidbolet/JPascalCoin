package com.github.davidbolet.jpascalcoin.crypto.helper;
//package com.github.davidbolet.jpascalcoin.api.helpers;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.UnsupportedEncodingException;
//import java.math.BigInteger;
//import java.security.InvalidKeyException;
//import java.security.KeyFactory;
//import java.security.NoSuchAlgorithmException;
//import java.security.NoSuchProviderException;
//import java.security.PrivateKey;
//import java.security.PublicKey;
//import java.security.SecureRandom;
//import java.security.Security;
//import java.security.Signature;
//import java.security.SignatureException;
//
//import org.bouncycastle.asn1.ASN1Integer;
//import org.bouncycastle.asn1.DERSequenceGenerator;
//import org.bouncycastle.asn1.sec.SECNamedCurves;
//import org.bouncycastle.asn1.x9.X9ECParameters;
//import org.bouncycastle.crypto.digests.SHA256Digest;
//import org.bouncycastle.crypto.params.ECDomainParameters;
//import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
//import org.bouncycastle.crypto.signers.ECDSASigner;
//import org.bouncycastle.crypto.signers.HMacDSAKCalculator;
//import org.bouncycastle.jce.ECNamedCurveTable;
//import org.bouncycastle.jce.spec.ECParameterSpec;
//import org.bouncycastle.jce.spec.ECPublicKeySpec;
//import org.bouncycastle.math.ec.ECPoint;
//
//public class BouncyCastleKeyHelper implements KeyHelper {
//	
//    static final X9ECParameters curve = SECNamedCurves.getByName("secp256k1");
//    static final ECDomainParameters domain = new ECDomainParameters(curve.getCurve(), curve.getG(), curve.getN(), curve.getH());
//    static final SecureRandom secureRandom = new SecureRandom();
//    static final BigInteger HALF_CURVE_ORDER = curve.getN().shiftRight(1);
//	
//	
//    /**
//     * Generates a new PascPrivateKey object from an string representation of a private key
//     * @param privateKeyD The private Key in string format
//     * @return PascPrivateKey
//     */
//    @Override
//	public PublicKey fromPrivateKey(BigInteger privateKeyD) {
//		try {
//			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
//        	KeyFactory keyFactory = KeyFactory.getInstance("ECDSA", "BC");
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
//    @Override
//	public byte[] generateSignature(byte[] digest, PrivateKey key) throws SignatureException, UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException{
//		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
//		Signature ecdsaSign = Signature.getInstance("SHA256withECDSA", "BC");
//		ecdsaSign.initSign(key);
//		ecdsaSign.update(digest);
//		byte[] signature = ecdsaSign.sign();
//		return signature;
//	}
//	
//    @Override
//	public boolean verifySignature(PublicKey key, byte[] signedBytes, byte[] original) throws SignatureException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException {
//		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
//		Signature signature = Signature.getInstance("SHA256withECDSA", "BC");
//		signature.initVerify(key);
//		signature.update(original);
//		return signature.verify(signedBytes);
//	}
//	
//	
//	public byte[] testSignature(byte[] toSign, String key) {
//		ECDSASigner signer = new ECDSASigner(new HMacDSAKCalculator(new SHA256Digest()));
//		ECDomainParameters domain = new ECDomainParameters(SECNamedCurves.getByName("secp256k1").getCurve(), curve.getG(), curve.getN(), curve.getH());
//	    signer.init(true, new ECPrivateKeyParameters(new BigInteger(key,16), domain));
//	    BigInteger[] signature = signer.generateSignature(toSign);
//	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
//	    try {
//	        DERSequenceGenerator seq = new DERSequenceGenerator(baos);
//	        System.out.println(signature[0].toString(16).toUpperCase());
//	        seq.addObject(new ASN1Integer(signature[0]));
//	        seq.addObject(new ASN1Integer(signature[1]));
//	        //seq.addObject(new ASN1Integer(toCanonicalS(signature[1])));
//	        System.out.println(signature[1].toString(16).toUpperCase());
//	        System.out.println("canonical S:"+toCanonicalS(signature[1]).toString(16).toUpperCase());
//	        seq.close();
//	        return baos.toByteArray();
//	    } catch (IOException e) {
//	        return new byte[0];
//	    }
//	}
//	
//	
//	private BigInteger toCanonicalS(BigInteger s) {
//        if (s.compareTo(HALF_CURVE_ORDER) <= 0) {
//            return s;
//        } else {
//            return curve.getN().subtract(s);
//        }
//    }
//	
//	
//	
//
//}
