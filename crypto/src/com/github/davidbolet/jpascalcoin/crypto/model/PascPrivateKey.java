package com.github.davidbolet.jpascalcoin.crypto.model;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.ECPrivateKey;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.ECPrivateKeySpec;

import com.github.davidbolet.jpascalcoin.common.exception.UnsupportedKeyTypeException;
import com.github.davidbolet.jpascalcoin.common.helper.CryptoUtils;
import com.github.davidbolet.jpascalcoin.common.helper.HexConversionsHelper;
import com.github.davidbolet.jpascalcoin.common.model.KeyType;
import com.github.davidbolet.jpascalcoin.common.model.PascPublicKey;
import com.github.davidbolet.jpascalcoin.crypto.helper.ECPointUtils;;


/**
 * PrivateKey Object, created for handling private keys on an offline storage
 * 
 * @author davidbolet
 *
 */
public class PascPrivateKey {
	
	public static final String B58_PUBKEY_PREFIX="01";
	
	private final String privateKey;
	private final ECPrivateKey jPrivateKey;
	private final com.github.davidbolet.jpascalcoin.common.model.KeyType keyType;
	private final com.github.davidbolet.jpascalcoin.common.model.PascPublicKey publicKey;

	/**
	 * Private constructor, Private key must be generated or derived from existing (use generate or fromPrivateKey)
	 * @param jPrivateKey ECPrivateKey object
	 * @param privateKey PascalCoin string representation of the private key
	 * @param keyType KeyType of the key
	 */
	private PascPrivateKey(ECPrivateKey jPrivateKey, String privateKey,KeyType keyType ) {
		this.jPrivateKey=jPrivateKey;
		this.privateKey=privateKey;
		this.keyType=keyType;
		this.publicKey=fromPrivateKey(this);
	}
	
	/**
	 * Gets the String object corresponding to this privateKey
	 * @return String
	 */
	public String getPrivateKey() {
		return privateKey;
	}

	/**
	 * Returns public key actually calculated, or null if it's an imported privateKey and we haven't call it with a KeyHelper 
	 * @return PublicKey key actually stored
	 */
	public PascPublicKey getPublicKey() {
		return publicKey;
	}

	/**
	 * Returns keyType of the actual private key
	 * @return KeyType of the privatekey 
	 */
	public KeyType getKeyType() {
		return keyType;
	}
	
	/**
	 * Gets the ECPrivateKey corresponding to this private key
	 * @return ECPrivateKey
	 */
	public ECPrivateKey getECPrivateKey()  {
		return jPrivateKey;	
	}
	
	/**
	 * Signs a text with Private Key
	 * @param text Text to sign
	 * @return SignResult representation of the signature 
	 */
	public OfflineSignResult sign(String text) {
		try {
			return sign(text.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Signs a byte array with Private Key
	 * @param strByte bytearray to sign
	 * @return SignResult structure representing signature data 
	 */
	public OfflineSignResult sign(byte[] strByte) {
		OfflineSignResult result = null;
		try 
		{	Signature dsa = Signature.getInstance("SHA256withECDSA");
			dsa.initSign(getECPrivateKey());
	        dsa.update(strByte);

	        byte[] realSig = dsa.sign();
	        result = new OfflineSignResult(realSig);
	        return result;
		} catch(Exception ex) {
			ex.printStackTrace();
		}
        return null;
	}
	
    /**
     * Generates a new PascPrivateKey object from an string representation of a private key
     * @param privateKey The private Key in string format
     * @param type KeyType Actually only SECP256K1
     * @return PascPrivateKey
     */
	public static PascPrivateKey fromPrivateKey(String privateKey, KeyType type) {
        PascPrivateKey result=null;
        
        if (!KeyType.SECP256K1.equals(type))
        	throw new IllegalArgumentException("Only SECP256K1 keys are supported by the moment");
        try {
        	BigInteger s=new BigInteger(privateKey, 16);
        	ECParameterSpec ecParameters= CryptoUtils.getECParameterSpec("secp256k1");
        	ECPrivateKeySpec keySpec = new ECPrivateKeySpec(s,ecParameters);
        	KeyFactory factory = KeyFactory.getInstance("EC");
            PrivateKey privateKey1=factory.generatePrivate(keySpec);
            String sepvt = adjustTo64(((ECPrivateKey)privateKey1).getS().toString(16)).toUpperCase();
            result = new PascPrivateKey((ECPrivateKey)privateKey1,sepvt,type);
            return result;
        } catch (Exception ex) {
            return null;
        }
    }

      /**
	 * Generates a new PascPrivateKey object
	 * @param type KeyType (by the moment, only SECP256K1 keys are supported)
	 * @return PascPrivateKey
	 * @throws UnsupportedKeyTypeException if given type is other than SECP256K1
	 */
	public static PascPrivateKey generate(KeyType type) throws UnsupportedKeyTypeException {
		if (!type.equals(KeyType.SECP256K1)) {
			throw new UnsupportedKeyTypeException();
		}
		try {
			//Generate an ECDSA Key Pair
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC");
			ECGenParameterSpec ecSpec = new ECGenParameterSpec("secp256k1");
			keyGen.initialize(ecSpec);
			KeyPair kp = keyGen.generateKeyPair();
			PublicKey pub = kp.getPublic();
			PrivateKey pvt = kp.getPrivate();
			
			//The ECDSA Private Key
			String sepvt = adjustTo64(((ECPrivateKey)pvt).getS().toString(16)).toUpperCase();
			PascPrivateKey result = new PascPrivateKey((ECPrivateKey)pvt, sepvt, type);
			assert (result.getPublicKey().getECPublicKey().equals(pub));
			return result;
		}
		catch(NoSuchAlgorithmException | InvalidAlgorithmParameterException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	  /**
     * Derivates Public Key from given privateKey 
     * @param privateKey PascPrivateKey to derivate
     * @return PublicKey
     */
    public static PascPublicKey fromPrivateKey(PascPrivateKey privateKey) {
    	com.github.davidbolet.jpascalcoin.common.model.PascPublicKey result=null;
    	BigInteger s=new BigInteger(privateKey.getPrivateKey(), 16);
    	java.security.spec.ECParameterSpec ecParameters= CryptoUtils.getECParameterSpec("secp256k1");
    	
    	ECPoint generator=ecParameters.getGenerator();
    	ECPoint w=ECPointUtils.scalmult(generator, s);
    	try {   
			String sx = adjustTo64(w.getAffineX().toString(16)).toUpperCase();
			String sy = adjustTo64(w.getAffineY().toString(16)).toUpperCase();
			//We must divide by 2 as charset is Unicode, while Pasc uses AnsiString 
			String bcPub = HexConversionsHelper.int2BigEndianHex(KeyType.SECP256K1.getValue())+HexConversionsHelper.int2BigEndianHex(sx.length()/2) + sx + HexConversionsHelper.int2BigEndianHex(sy.length()/2)+sy;
			result=com.github.davidbolet.jpascalcoin.common.model.PascPublicKey.fromEncodedPubKey( bcPub);
    	} catch (Exception ex) {
    		ex.printStackTrace();
    	}
    	return result;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((privateKey == null) ? 0 : privateKey.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof PascPrivateKey)) {
			return false;
		}
		PascPrivateKey other = (PascPrivateKey) obj;
		if (privateKey == null) {
			if (other.privateKey != null) {
				return false;
			}
		} else if (!privateKey.equals(other.privateKey)) {
			return false;
		}
		return true;
	}

	
}
