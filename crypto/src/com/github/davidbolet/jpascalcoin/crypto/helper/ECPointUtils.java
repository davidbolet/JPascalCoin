package com.github.davidbolet.jpascalcoin.crypto.helper;

import java.math.BigInteger;
import java.security.spec.ECPoint;

public class ECPointUtils {

static BigInteger TWO = new BigInteger("2");
static BigInteger p = new BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFFC2F",16);
static BigInteger a = BigInteger.ZERO;


public static ECPoint scalmult(ECPoint P, BigInteger kin){
    //ECPoint R=P; - incorrect
    ECPoint R = ECPoint.POINT_INFINITY,S = P;
    BigInteger k = kin.mod(p);
    int length = k.bitLength();
    //System.out.println("length is" + length);
    byte[] binarray = new byte[length];
    for(int i=0;i<=length-1;i++){
        binarray[i] = k.mod(TWO).byteValue();
        k = k.divide(TWO);
    }
    /*for(int i = length-1;i >= 0;i--){
        System.out.print("" + binarray[i]); 
    }*/

    for(int i = length-1;i >= 0;i--){
        // i should start at length-1 not -2 because the MSB of binarry may not be 1
        R = doublePoint(R);
        if(binarray[i]== 1) 
            R = addPoint(R, S);
    }
return R;
}

public static ECPoint addPoint(ECPoint r, ECPoint s) {

    if (r.equals(s))
        return doublePoint(r);
    else if (r.equals(ECPoint.POINT_INFINITY))
        return s;
    else if (s.equals(ECPoint.POINT_INFINITY))
        return r;
    BigInteger slope = (r.getAffineY().subtract(s.getAffineY())).multiply(r.getAffineX().subtract(s.getAffineX()).modInverse(p)).mod(p);
    BigInteger Xout = (slope.modPow(TWO, p).subtract(r.getAffineX())).subtract(s.getAffineX()).mod(p);
    //BigInteger Yout = r.getAffineY().negate().mod(p); - incorrect
    BigInteger Yout = s.getAffineY().negate().mod(p);
    //Yout = Yout.add(slope.multiply(r.getAffineX().subtract(Xout))).mod(p); - incorrect
    Yout = Yout.add(slope.multiply(s.getAffineX().subtract(Xout))).mod(p);
    ECPoint out = new ECPoint(Xout, Yout);
    return out;
}

public static ECPoint doublePoint(ECPoint r) {
    if (r.equals(ECPoint.POINT_INFINITY)) 
        return r;
    BigInteger slope = (r.getAffineX().pow(2)).multiply(new BigInteger("3"));
    //slope = slope.add(new BigInteger("3")); - incorrect
    slope = slope.add(a);
    slope = slope.multiply((r.getAffineY().multiply(TWO)).modInverse(p));
    BigInteger Xout = slope.pow(2).subtract(r.getAffineX().multiply(TWO)).mod(p);
    BigInteger Yout = (r.getAffineY().negate()).add(slope.multiply(r.getAffineX().subtract(Xout))).mod(p);
    ECPoint out = new ECPoint(Xout, Yout);
    return out;
}

}
