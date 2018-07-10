package com.github.davidbolet.jpascalcoin.api.model;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Arrays;

import com.github.davidbolet.jpascalcoin.api.helpers.Base58;
import com.github.davidbolet.jpascalcoin.api.helpers.HexConversionsHelper;
import com.github.davidbolet.jpascalcoin.exception.UsupportedKeyTypeException;

/**
 * PrivateKey Object, crated for handling private keys on local
 * This object represents a 
 * @author davidbolet
 *
 */
public class PascPrivateKey {
	
	public static final String B58_PUBKEY_PREFIX="01";
	
	private String privateKey;
	private com.github.davidbolet.jpascalcoin.api.model.PublicKey publicKey;
	
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
	
	public PrivateKey getECPrivateKey()  {
		PrivateKey priv = null;
	    byte[] clear = HexConversionsHelper.decodeStr2Hex(getPrivateKey());
	    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(clear);
	    KeyFactory fact;
		try {
			fact = KeyFactory.getInstance("DSA");
		    priv = fact.generatePrivate(keySpec);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			e.printStackTrace();
		}
	    Arrays.fill(clear, (byte) 0);
	    return priv;
	}
	
	/**
	 * Signs a text with Private Key
	 * @param text Text to sign
	 * @return String representation of the signature 
	 */
	public String sign(String text) {
		String result = null;
		try {
		Signature dsa = Signature.getInstance("SHA1withECDSA");

        dsa.initSign(getECPrivateKey());
      
        byte[] strByte = text.getBytes("UTF-8");
        dsa.update(strByte);

        byte[] realSig = dsa.sign();
        result = new BigInteger(1, realSig).toString(16);
        System.out.println("Signature: " + result);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
        return result;
	}

	/**
	 * Generates a new PascPrivateKey object
	 * @param type KeyType (by the moment, only SECP256K1 keys are supported)
	 * @param name Key name
	 * @return PascPrivateKey
	 * @throws UsupportedKeyTypeException if given type is other than SECP256K1
	 */
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
			String sx = HexConversionsHelper.byteToHex(pt.getAffineX().toByteArray()).toUpperCase(); 
			String sy = HexConversionsHelper.byteToHex(pt.getAffineY().toByteArray()).toUpperCase();
			//String sx = adjustTo64(pt.getAffineX().toString(16)).toUpperCase();
			//String sy = adjustTo64(pt.getAffineY().toString(16)).toUpperCase();
			//We must divide by 2 as charset is Unicode, while Pasc uses AnsiString 
			String bcPub = HexConversionsHelper.int2BigEndianHex(KeyType.SECP256K1.getValue())+HexConversionsHelper.int2BigEndianHex(sx.length()/2) + sx + HexConversionsHelper.int2BigEndianHex(sy.length()/2)+sy;
			com.github.davidbolet.jpascalcoin.api.model.PublicKey pk=new com.github.davidbolet.jpascalcoin.api.model.PublicKey();
			pk.setX(sx);
			pk.setY(sy);
			pk.setName(name);
			pk.setEncPubKey(bcPub);
			pk.setCanUse(true); //However, the wallet doesn't have the private key, as we only have it 
			
			//Now we must calculate Base58PubKey
			MessageDigest sha = MessageDigest.getInstance("SHA-256");
			byte[] s1 = sha.digest(HexConversionsHelper.decodeStr2Hex(bcPub));
			String shaTxt=HexConversionsHelper.byteToHex(s1).toUpperCase();
			//System.out.println("  sha: " + shaTxt);
			
			//set AUX = SHA256( ENC_PUBKEY ) set NEW_RAW = '01' + AUX (as hexadecimal) + Copy(AUX, 1, 4) (as hexadecmial)
			String base58PubKeyPre= B58_PUBKEY_PREFIX+bcPub+shaTxt.substring(0, 8);
			//System.out.println("pre "+base58PubKeyPre);
			String base58PubKey = Base58.encode(HexConversionsHelper.decodeStr2Hex(base58PubKeyPre));
			pk.setBase58PubKey(base58PubKey);
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
	

}
