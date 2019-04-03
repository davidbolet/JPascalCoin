package com.github.davidbolet.jpascalcoin.crypto.helper;

import java.security.SecureRandom;
import org.spongycastle.crypto.BasicAgreement;
import org.spongycastle.crypto.BufferedBlockCipher;
import org.spongycastle.crypto.CipherParameters;
import org.spongycastle.crypto.KeyEncoder;
import org.spongycastle.crypto.KeyGenerationParameters;
import org.spongycastle.crypto.KeyParser;
import org.spongycastle.crypto.agreement.ECDHBasicAgreement;
import org.spongycastle.crypto.digests.MD5Digest;
import org.spongycastle.crypto.digests.SHA512Digest;
import org.spongycastle.crypto.engines.AESEngine;
import org.spongycastle.crypto.generators.ECKeyPairGenerator;
import org.spongycastle.crypto.generators.EphemeralKeyPairGenerator;
import org.spongycastle.crypto.macs.HMac;
import org.spongycastle.crypto.modes.CBCBlockCipher;
import org.spongycastle.crypto.paddings.BlockCipherPadding;
import org.spongycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.spongycastle.crypto.paddings.ZeroBytePadding;
import org.spongycastle.crypto.params.ECDomainParameters;
import org.spongycastle.crypto.params.ECKeyGenerationParameters;
import org.spongycastle.crypto.params.ECPrivateKeyParameters;
import org.spongycastle.crypto.params.ECPublicKeyParameters;
import org.spongycastle.crypto.params.IESWithCipherParameters;
import org.spongycastle.crypto.params.ParametersWithIV;
import org.spongycastle.crypto.parsers.ECIESPublicKeyParser;
import org.spongycastle.jcajce.provider.asymmetric.util.ECUtil;
import org.spongycastle.jce.ECNamedCurveTable;
import org.spongycastle.jce.spec.ECNamedCurveParameterSpec;
import org.spongycastle.math.ec.ECCurve;

import com.github.davidbolet.jpascalcoin.common.helper.HexConversionsHelper;
import com.github.davidbolet.jpascalcoin.common.model.PascPublicKey;
import com.github.davidbolet.jpascalcoin.crypto.model.EciesPublicKeyEncoder;
import com.github.davidbolet.jpascalcoin.crypto.model.PascPrivateKey;
import com.github.davidbolet.jpascalcoin.crypto.model.PascalCoinIESEngine;
import com.github.davidbolet.jpascalcoin.crypto.model.TPascalCoinECIESKdfBytesGenerator;

/**
 * 
 * @author davidbolet
 *
 */
public class EncryptionUtils {
	
	/**
	 * Performs Pascalcoin's custom ECIES decryption
	 * @param privateKey PascProvateKey
	 * @param payload Payload to decript (full pascalcoin's structure)
	 * @return byte[] with the result (without padding)
	 * @throws Exception if something goes wrong
	 */
	public static byte[] doPascalcoinEciesDecrypt(PascPrivateKey privateKey, String payload) throws Exception {
		
		ECNamedCurveParameterSpec curveParameterSpec = ECNamedCurveTable.getParameterSpec(privateKey.getKeyType().name());
		ECCurve ecCurve = curveParameterSpec.getCurve();
        ECDomainParameters ecDomainParameters = new ECDomainParameters(
        		ecCurve,
        		curveParameterSpec.getG(),
        		curveParameterSpec.getN(),
        		curveParameterSpec.getH());
        ECPrivateKeyParameters ecdhPrivateKeyParams = (ECPrivateKeyParameters) ECUtil.generatePrivateKeyParameter(privateKey.getECPrivateKey());
	    byte[] encryptedMessage=HexConversionsHelper.decodeStr2Hex(payload);
        
		BasicAgreement agree = new ECDHBasicAgreement();
        agree.init(ecdhPrivateKeyParams);
        SHA512Digest shaDigest = new SHA512Digest();

	    TPascalCoinECIESKdfBytesGenerator generator=new TPascalCoinECIESKdfBytesGenerator(shaDigest);
	    org.spongycastle.crypto.Mac digestMacInstance = new HMac(new MD5Digest());
        BlockCipherPadding padding = new ZeroBytePadding();
        BufferedBlockCipher cipher = new PaddedBufferedBlockCipher(
                new CBCBlockCipher(new AESEngine()), padding);

        PascalCoinIESEngine iesCipher= new PascalCoinIESEngine(agree, generator, digestMacInstance, cipher);
		IESWithCipherParameters iesParams=new IESWithCipherParameters(null, null, 32 * 8,32 * 8);
		KeyParser publicKeyParser=new ECIESPublicKeyParser(ecDomainParameters);
		CipherParameters params = new ParametersWithIV(iesParams, new byte[16]);
		iesCipher.init(ecdhPrivateKeyParams, params, publicKeyParser);
        
        return iesCipher.processBlock(encryptedMessage, 0, encryptedMessage.length);
        
	}
	
	/**
	 * Performs Pascalcoin's ECIES custom encryption
	 * @param publicKey PascalCoin public key
	 * @param payload Payload to encrypt
	 * @return byte[] with the encrypted payload with Pascalcoin's structure
	 * @throws Exception if something goes wrong
	 */
	public static byte[] doPascalcoinEciesEncrypt(PascPublicKey publicKey, String payload) throws Exception {
		byte[] toEncrypt=payload.getBytes();
		return doPascalcoinEciesEncrypt(publicKey, toEncrypt);
	}
	
	/**
	 * Performs Pascalcoin's ECIES custom encryption
	 * @param publicKey PascalCoin public key
	 * @param payload Payload to encrypt
	 * @return byte[] with the encrypted payload with Pascalcoin's structure
	 * @throws Exception if something goes wrong
	 */
	public static byte[] doPascalcoinEciesEncrypt(PascPublicKey publicKey, byte[] payload) throws Exception {

		ECNamedCurveParameterSpec curveParameterSpec = ECNamedCurveTable.getParameterSpec(publicKey.getKeyType().name());		
		ECCurve ecCurve = curveParameterSpec.getCurve();
        ECDomainParameters ecDomainParameters = new ECDomainParameters(
        		ecCurve,
        		curveParameterSpec.getG(),
        		curveParameterSpec.getN(),
        		curveParameterSpec.getH());
		
        ECPublicKeyParameters pubKeyParams =(ECPublicKeyParameters) ECUtil.generatePublicKeyParameter(publicKey.getECPublicKey());
		BasicAgreement agree = new ECDHBasicAgreement();
        
        SHA512Digest shaDigest = new SHA512Digest();
	    TPascalCoinECIESKdfBytesGenerator generator=new TPascalCoinECIESKdfBytesGenerator(shaDigest);
	    org.spongycastle.crypto.Mac digestMacInstance = new HMac(new MD5Digest());
        
        BlockCipherPadding padding = new ZeroBytePadding();
        BufferedBlockCipher cipher = new PaddedBufferedBlockCipher(
                new CBCBlockCipher(new AESEngine()), padding);
        
		PascalCoinIESEngine iesCipher= new PascalCoinIESEngine(agree, generator, digestMacInstance, cipher);
		IESWithCipherParameters iesParams=new IESWithCipherParameters(null, null, 32 * 8,32 * 8);
        KeyEncoder keyEncoder=new EciesPublicKeyEncoder();
		CipherParameters params = new ParametersWithIV(iesParams, new byte[16]);
		ECKeyPairGenerator ecKeyGenerator=new ECKeyPairGenerator();
		
		KeyGenerationParameters keyPairParams=new ECKeyGenerationParameters(ecDomainParameters, new SecureRandom());
		ecKeyGenerator.init(keyPairParams);
		EphemeralKeyPairGenerator ephemeralKeyPairGenerator=new EphemeralKeyPairGenerator(ecKeyGenerator, keyEncoder);
		iesCipher.init(pubKeyParams, params, ephemeralKeyPairGenerator);
		
		return iesCipher.processBlock(payload, 0, payload.length);
	}
	
}
