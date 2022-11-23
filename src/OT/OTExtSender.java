// Copyright (C) 2010 by Yan Huang <yhuang@virginia.edu>

package OT;

import java.math.*;
import java.io.*;
import java.security.SecureRandom;

import Cipher.Cipher;

public class OTExtSender extends Sender {
    public static class SecurityParameter {
	public static final int k1 = 80;    // number of columns in T
	public static final int k2 = 80;
    }

    public static SecureRandom rnd = new SecureRandom();
    public NPOTReceiver rcver;
    public BigInteger s;
    public BigInteger[] keys;

    public OTExtSender(int numOfPairs, int msgBitLength,
		       ObjectInputStream in, ObjectOutputStream out) throws Exception {
	super(numOfPairs, msgBitLength, in, out);
	
	initialize();
    }

    public void execProtocol(BigInteger[][] msgPairs) throws Exception {
//		System.out.println("**9:r");
//	BigInteger[][] cphPairs = (BigInteger[][]) ois.readObject();
	BigInteger[][] cphPairs = Temp.getCphPairs();
	int bytelength;

	BitMatrix Q = new BitMatrix(numOfPairs, SecurityParameter.k1);

	for (int i = 0; i < SecurityParameter.k1; i++) {
	    if (s.testBit(i))
		Q.data[i] = Cipher.decrypt(keys[i], cphPairs[i][1], numOfPairs);
	    else
		Q.data[i] = Cipher.decrypt(keys[i], cphPairs[i][0], numOfPairs);
	}

	BitMatrix tQ = Q.transpose();
	
	BigInteger[][] y = new BigInteger[numOfPairs][2];
	for (int i = 0; i < numOfPairs; i++) {
	    y[i][0] = Cipher.encrypt(i, tQ.data[i],        msgPairs[i][0], msgBitLength);
	    y[i][1] = Cipher.encrypt(i, tQ.data[i].xor(s), msgPairs[i][1], msgBitLength);
	}

	bytelength = (msgBitLength-1)/8 + 1;
//	for (int i = 0; i < numOfPairs; i++) {
//	    Utils.writeBigInteger(y[i][0], bytelength, oos);
//	    Utils.writeBigInteger(y[i][1], bytelength, oos);
//	}
//	oos.flush();
		Temp.setY(y);
//		System.out.println("**10:w");

    }

    public void initialize() throws Exception {
//		System.out.println("**4:w");
    }

	public void initialize2() throws Exception {

		rcver = new NPOTReceiver(SecurityParameter.k1, ois, oos);
		Micro_receiver = rcver;
		s = new BigInteger(SecurityParameter.k1, rnd);

		Micro_receiver.execProtocol(s);
//		rcver.execProtocol(s);
//		keys = rcver.getData();
	}

	public void initialize3() throws Exception {

		keys = Micro_receiver.getData();
//		keys = rcver.getData();
//		keys = Temp.getData();
//		Temp.setKeys(keys);
	}



}