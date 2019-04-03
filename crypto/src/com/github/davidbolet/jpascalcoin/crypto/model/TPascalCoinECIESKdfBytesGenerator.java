package com.github.davidbolet.jpascalcoin.crypto.model;

import org.spongycastle.crypto.DataLengthException;
import org.spongycastle.crypto.DerivationParameters;
import org.spongycastle.crypto.Digest;
import org.spongycastle.crypto.params.ISO18033KDFParameters;
import org.spongycastle.crypto.params.KDFParameters;

public class TPascalCoinECIESKdfBytesGenerator extends org.spongycastle.crypto.generators.BaseKDFBytesGenerator {

    private byte[] shared;
    private byte[] iv;
    
    
    
	public TPascalCoinECIESKdfBytesGenerator(Digest digest) {
		super(0, digest);
	}

	@Override
	public void init(DerivationParameters param) {
		if (param instanceof KDFParameters)
        {
            KDFParameters p = (KDFParameters)param;

            shared = p.getSharedSecret();
            iv = p.getIV();
        }
        else if (param instanceof ISO18033KDFParameters)
        {
            ISO18033KDFParameters p = (ISO18033KDFParameters)param;

            shared = p.getSeed();
            iv = null;
        }
        else
        {
            throw new IllegalArgumentException("KDF parameters required for generator");
        }
	}

	@Override
	public Digest getDigest() {
		// TODO Auto-generated method stub
		return super.getDigest();
	}

	@Override
	public int generateBytes(byte[] out, int outOff, int len) throws DataLengthException, IllegalArgumentException {
		Integer outLen;
		Integer oBytes;
		byte[] temp;
		
		if ((out.length-len)<outOff) throw new DataLengthException();
		
		oBytes=len;
		outLen=this.getDigest().getDigestSize();
		
		if (oBytes>outLen) throw new DataLengthException();
		
		temp=new byte[this.getDigest().getDigestSize()];
		this.getDigest().update(shared, 0, shared.length);
		this.getDigest().doFinal(temp, 0);
		
		System.arraycopy(temp, 0, out, outOff, len);
		this.getDigest().reset();
		
		return oBytes;
	}

}
