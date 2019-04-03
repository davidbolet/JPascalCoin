package com.github.davidbolet.jpascalcoin.crypto.model;

import org.spongycastle.crypto.KeyEncoder;
import org.spongycastle.crypto.params.AsymmetricKeyParameter;
import org.spongycastle.crypto.params.ECPublicKeyParameters;

/**
 * 
 * @author davidbolet
 *
 */
public class EciesPublicKeyEncoder implements KeyEncoder {
    @Override
    public byte[] getEncoded(AsymmetricKeyParameter asymmetricKeyParameter) {
        return ((ECPublicKeyParameters) asymmetricKeyParameter).getQ().getEncoded(true);
    }
}
