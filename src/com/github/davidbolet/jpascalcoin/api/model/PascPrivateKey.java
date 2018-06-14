package com.github.davidbolet.jpascalcoin.api.model;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.ECPoint;

import com.github.davidbolet.jpascalcoin.exception.UsupportedKeyTypeException;

/**
 * PrivateKey Object, crated for handling private keys on local
 * This object represents a 
 * @author davidbolet
 *
 */
public class PascPrivateKey {
	
	String privateKey;
	com.github.davidbolet.jpascalcoin.api.model.PublicKey publicKey;
	
	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public com.github.davidbolet.jpascalcoin.api.model.PublicKey getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(com.github.davidbolet.jpascalcoin.api.model.PublicKey publicKey) {
		this.publicKey = publicKey;
	}

	public static PascPrivateKey generate(KeyType type,String name) throws UsupportedKeyTypeException {
		if (!type.equals(KeyType.SECP256K1)) {
			throw new UsupportedKeyTypeException();
		}
		try {
			PascPrivateKey result = new PascPrivateKey();
			//Generate an ECDSA Key Pair
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC");
			ECGenParameterSpec ecSpec = new ECGenParameterSpec("secp256k1");
			keyGen.initialize(ecSpec);
			KeyPair kp = keyGen.generateKeyPair();
			PublicKey pub = kp.getPublic();
			PrivateKey pvt = kp.getPrivate();
			
			//The ECDSA Private Key
			ECPrivateKey epvt = (ECPrivateKey)pvt;
			String sepvt = adjustTo64(epvt.getS().toString(16)).toUpperCase();
			result.setPrivateKey(sepvt);
			
			ECPublicKey epub = (ECPublicKey)pub;
			ECPoint pt = epub.getW();
			String sx = adjustTo64(pt.getAffineX().toString(16)).toUpperCase();
			String sy = adjustTo64(pt.getAffineY().toString(16)).toUpperCase();
			
			String bcPub = "01CA022000" + sx + "2000"+sy;
			com.github.davidbolet.jpascalcoin.api.model.PublicKey pk=new com.github.davidbolet.jpascalcoin.api.model.PublicKey();
			pk.setX(sx);
			pk.setY(sy);
			pk.setName(name);
			pk.setEncPubKey(bcPub);
			pk.setCanUse(false); //the wallet doesn't have the private key, as we only have it 
			//System.out.println("bcPub: " + bcPub);
			result.setPublicKey(pk);
			return result;
		}
		catch(NoSuchAlgorithmException | InvalidAlgorithmParameterException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	static private String adjustTo64(String s) {
        switch(s.length()) {
        case 62: return "00" + s;
        case 63: return "0" + s;
        case 64: return s;
        default:
            throw new IllegalArgumentException("not a valid key: " + s);
        }
    }
	
	public static String byteToHex(byte[] in) {
	    final StringBuilder builder = new StringBuilder();
	    for(byte b : in) {
	        builder.append(String.format("%02x", b));
	    }
	    return builder.toString();
	}

}
