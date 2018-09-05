package com.github.davidbolet.jpascalcoin.api.model.internal;

import java.math.BigInteger;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPrivateKeySpec;
import java.util.Arrays;

import com.github.davidbolet.jpascalcoin.api.helpers.BouncyCastleKeyHelper;
import com.github.davidbolet.jpascalcoin.api.helpers.HexConversionsHelper;
import com.github.davidbolet.jpascalcoin.api.model.KeyType;
import com.github.davidbolet.jpascalcoin.exception.UsupportedKeyTypeException;


/**
 * PrivateKey Object, created for handling private keys on an offline storage
 * @author davidbolet
 *
 */
public class PascPrivateKey {
	
	public static final String B58_PUBKEY_PREFIX="01";
	
	private String privateKey;
	private ECPrivateKey jPrivateKey;
	private com.github.davidbolet.jpascalcoin.api.model.PublicKey publicKey;
	
	public String getPrivateKey() {
		return privateKey;
	}

	public com.github.davidbolet.jpascalcoin.api.model.PublicKey getPublicKey() {
		return publicKey;
	}

	private void setPrivateKey(String privateKey) {
	    this.privateKey=privateKey;
    }

	private void setPublicKey(com.github.davidbolet.jpascalcoin.api.model.PublicKey publicKey) {
		this.publicKey = publicKey;
	}
	
	public ECPrivateKey getECPrivateKey()  {
		if (this.jPrivateKey==null) {
			try {
				BigInteger s=new BigInteger(privateKey, 16);
	        	ECParameterSpec ecParameters= getECParameterSpec("secp256k1");
	        	ECPrivateKeySpec keySpec = new ECPrivateKeySpec(s,ecParameters);
	        	KeyFactory factory = KeyFactory.getInstance("EC");
	            PrivateKey privateKey1=factory.generatePrivate(keySpec);
	            this.jPrivateKey = (ECPrivateKey)privateKey1;
			}
			catch(Exception ex) {
				
			}
		}
		return jPrivateKey;	
	}
	
	/**
	 * Signs a text with Private Key
	 * @param text Text to sign
	 * @return String representation of the signature 
	 */
	public String sign(String text) {
		String result = null;
		try 
		{
			/*304502202793978DF73C005E469492B84C2AA05F8E59C12DC659D7CA3ACEF5A5FAFC689F022100A9B26EE869A28A7447DD8D82EE64CFB46E1CA9FD1C85A9973F232E6E19EB8792
			01000000090000000300010014D10C00D0070000000000000700000000002000035DDBFB7CAC548650FF911D19B0464B6822EBE092A25AA6DB1F926E653963332000307E704E6D
			085F9CD8351DCF5045D7C380915360A37B6808E9C4FF755DDEFC650100DAEE0100D007000000000000180054686973205061796C6F616420746F2072656365697665720000 */
			//Signature dsa = Signature.getInstance("SHA256withECDSA");
			Signature dsa = Signature.getInstance("SHA1withECDSA");
			dsa.initSign(getECPrivateKey());
	        byte[] strByte = text.getBytes("UTF-8");
	        dsa.update(strByte);
		    /*
		     * Now that all the data to be signed has been read in, generate a
		     * signature for it
		     */
	        byte[] realSig = dsa.sign();
	        //result = new BigInteger(1, realSig).toString(16).toUpperCase();
	        BigInteger r=extractR(realSig);
	        BigInteger s = extractS(realSig);
	        System.out.println(r.toString(16).toUpperCase());
	        System.out.println(s.toString(16).toUpperCase());
	        result=HexConversionsHelper.byteToHex(realSig).toUpperCase();
	        System.out.println(HexConversionsHelper.byteToHex(derSign(r,s)).toUpperCase());
		    System.out.println("Signature: " + result);	
		} catch(Exception ex) {
			ex.printStackTrace();
		}
        return result;
	}

	//we can either use the Java standard signature ANS1 format output, or just take the R and S parameters from it, and pass those to Go
	//https://stackoverflow.com/questions/48783809/ecdsa-sign-with-bouncycastle-and-verify-with-crypto
	public static BigInteger extractR(byte[] signature) throws Exception {
	    int startR = (signature[1] & 0x80) != 0 ? 3 : 2;
	    int lengthR = signature[startR + 1];
	    return new BigInteger(Arrays.copyOfRange(signature, startR + 2, startR + 2 + lengthR));
	}

	public static BigInteger extractS(byte[] signature) throws Exception {
	    int startR = (signature[1] & 0x80) != 0 ? 3 : 2;
	    int lengthR = signature[startR + 1];
	    int startS = startR + 2 + lengthR;
	    int lengthS = signature[startS + 1];
	    return new BigInteger(Arrays.copyOfRange(signature, startS + 2, startS + 2 + lengthS));
	}

	public static byte[] derSign(BigInteger r, BigInteger s) throws Exception {
	    byte[] rb = r.toByteArray();
	    byte[] sb = s.toByteArray();
	    int off = (2 + 2) + rb.length;
	    int tot = off + (2 - 2) + sb.length;
	    byte[] der = new byte[tot + 2];
	    der[0] = 0x30;
	    der[1] = (byte) (tot & 0xff);
	    der[2 + 0] = 0x02;
	    der[2 + 1] = (byte) (rb.length & 0xff);
	    System.arraycopy(rb, 0, der, 2 + 2, rb.length);
	    der[off + 0] = 0x02;
	    der[off + 1] = (byte) (sb.length & 0xff);
	    System.arraycopy(sb, 0, der, off + 2, sb.length);
	    return der;
	}
	
	/**
	 * Signs a text with Private Key
	 * @param text Text to sign
	 * @return String representation of the signature 
	 */
	public String signBC(String text) {
		String result = null;
		try 
		{
			byte[] realSig = BouncyCastleKeyHelper.generateSignature(text, this.getECPrivateKey());
			result=HexConversionsHelper.byteToHex(realSig).toUpperCase();
			//result = new BigInteger(1, realSig).toString(16).toUpperCase();
		    System.out.println("Signature: " + result);	
		} catch(Exception ex) {
			ex.printStackTrace();
		}
        return result;
	}
	
	
	private static ECParameterSpec getECParameterSpec(String curve) {
		try {
			AlgorithmParameters parameters = AlgorithmParameters.getInstance("EC", "SunEC");
	        parameters.init(new ECGenParameterSpec(curve));
	        ECParameterSpec ecParameters = parameters.getParameterSpec(ECParameterSpec.class);
	        return ecParameters;
		}
		catch(Exception ex) {
			return null;
		}
	}
	
    /**
     * Generates a new PascPrivateKey object from an string representation of a private key
     * @param privateKey The private Key in string format
     * @return PascPrivateKey
     * @throws UsupportedKeyTypeException if given type is other than SECP256K1
     */
	public static PascPrivateKey fromPrivateKey(String privateKey, KeyType type) {
        PascPrivateKey result=new PascPrivateKey();
        if (!KeyType.SECP256K1.equals(type))
        	throw new IllegalArgumentException("Only SECP256K1 keys are supported by the moment");
        try {
        	BigInteger s=new BigInteger(privateKey, 16);
        	ECParameterSpec ecParameters= getECParameterSpec("secp256k1");
        	ECPrivateKeySpec keySpec = new ECPrivateKeySpec(s,ecParameters);
        	KeyFactory factory = KeyFactory.getInstance("EC");
            PrivateKey privateKey1=factory.generatePrivate(keySpec);
            result.jPrivateKey = (ECPrivateKey)privateKey1;
           
            String sepvt = adjustTo64(result.jPrivateKey.getS().toString(16)).toUpperCase();
            result.setPrivateKey(sepvt);
           
            PublicKey pub = BouncyCastleKeyHelper.fromPrivateKey(s);
            result.setPublicKey(com.github.davidbolet.jpascalcoin.api.model.PublicKey.fromECPublicKey((ECPublicKey)pub));
            return result;
        } catch (Exception ex) {

            return null;
        }
    }

      /**
	 * Generates a new PascPrivateKey object
	 * @param type KeyType (by the moment, only SECP256K1 keys are supported)
	 * @return PascPrivateKey
	 * @throws UsupportedKeyTypeException if given type is other than SECP256K1
	 */
	public static PascPrivateKey generate(KeyType type) throws UsupportedKeyTypeException {
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
			result.jPrivateKey = (ECPrivateKey)pvt;
			String sepvt = adjustTo64(result.jPrivateKey.getS().toString(16)).toUpperCase();
			result.setPrivateKey(sepvt);
			com.github.davidbolet.jpascalcoin.api.model.PublicKey pk=com.github.davidbolet.jpascalcoin.api.model.PublicKey.fromECPublicKey((ECPublicKey)pub);
//			ECPublicKey epub = (ECPublicKey)pub;
//			ECPoint pt = epub.getW();
////			String sx1 = HexConversionsHelper.byteToHex(pt.getAffineX().toByteArray()).toUpperCase(); 
////			String sy1 = HexConversionsHelper.byteToHex(pt.getAffineY().toByteArray()).toUpperCase();
//			String sx = adjustTo64(pt.getAffineX().toString(16)).toUpperCase();
//			String sy = adjustTo64(pt.getAffineY().toString(16)).toUpperCase();
//			//We must divide by 2 as charset is Unicode, while Pasc uses AnsiString 
//			String bcPub = HexConversionsHelper.int2BigEndianHex(KeyType.SECP256K1.getValue())+HexConversionsHelper.int2BigEndianHex(sx.length()/2) + sx + HexConversionsHelper.int2BigEndianHex(sy.length()/2)+sy;
//			com.github.davidbolet.jpascalcoin.api.model.PublicKey pk=com.github.davidbolet.jpascalcoin.api.model.PublicKey.fromEncodedPubKey(bcPub);
			
//			pk.setX(sx);
//			pk.setY(sy);
//			//pk.setName(name);
//			pk.setEncPubKey(bcPub);
//			pk.setCanUse(true); //However, the wallet doesn't have the private key, as we only have it 
//			
//			//Now we must calculate Base58PubKey
//			MessageDigest sha = MessageDigest.getInstance("SHA-256");
//			byte[] s1 = sha.digest(HexConversionsHelper.decodeStr2Hex(bcPub));
//			String shaTxt=HexConversionsHelper.byteToHex(s1).toUpperCase();
//			//System.out.println("  sha: " + shaTxt);
//			
//			//set AUX = SHA256( ENC_PUBKEY ) set NEW_RAW = '01' + AUX (as hexadecimal) + Copy(AUX, 1, 4) (as hexadecmial)
//			String base58PubKeyPre= B58_PUBKEY_PREFIX+bcPub+shaTxt.substring(0, 8);
//			//System.out.println("pre "+base58PubKeyPre);
//			String base58PubKey = Base58.encode(HexConversionsHelper.decodeStr2Hex(base58PubKeyPre));
//			pk.setBase58PubKey(base58PubKey);
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
