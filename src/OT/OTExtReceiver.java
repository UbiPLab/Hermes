// Copyright (C) 2010 by Yan Huang <yhuang@virginia.edu>

package OT;

import java.util.*;
import java.math.*;
import java.io.*;
import java.security.SecureRandom;

import Cipher.Cipher;
import Utils.*;
import YaoGC.Wire;

public class OTExtReceiver extends Receiver {
    private static SecureRandom rnd = new SecureRandom();

    public int k1;
    public int k2;
    public int msgBitLength;

    public NPOTSender snder;
    public BitMatrix T;
    public BigInteger[][] keyPairs;

    public OTExtReceiver(int numOfChoices, 
			 ObjectInputStream in, ObjectOutputStream out) throws Exception {
	super(numOfChoices, in, out);

	initialize();
    }

    public void execProtocol(BigInteger choices) throws Exception {
	super.execProtocol(choices);

	BigInteger[][] msgPairs = new BigInteger[k1][2];
	BigInteger[][] cphPairs = new BigInteger[k1][2];

	for (int i = 0; i < k1; i++) {
	    msgPairs[i][0] = T.data[i];
	    msgPairs[i][1] = T.data[i].xor(choices);

	    cphPairs[i][0] = Cipher.encrypt(keyPairs[i][0], msgPairs[i][0], numOfChoices);
	    cphPairs[i][1] = Cipher.encrypt(keyPairs[i][1], msgPairs[i][1], numOfChoices);
	}

	Temp.setCphPairs(cphPairs);
//		System.out.println("**9:w");

//	oos.writeObject(cphPairs);
//	oos.flush();
    }

    public void exex(){
		int bytelength;

		BitMatrix tT = T.transpose();
//		System.out.println("**10:r");
		BigInteger[][] y = new BigInteger[numOfChoices][2];
		bytelength = (msgBitLength-1)/8 + 1;
//		for (int i = 0; i < numOfChoices; i++) {
//			y[i][0] = Utils.readBigInteger(bytelength, ois);
//			y[i][1] = Utils.readBigInteger(bytelength, ois);
//		}
		y = Temp.getY();

		numOfChoices = y.length;
		data = new BigInteger[numOfChoices];
		for (int i = 0; i < numOfChoices; i++) {
			int sigma = choices.testBit(i) ? 1 : 0;
			data[i] = Cipher.decrypt(i, tT.data[i], y[i][sigma], msgBitLength);
		}
	}

    public void initialize() throws Exception {
//	k1 = ois.readInt();
//	k2 = ois.readInt();
//	msgBitLength = ois.readInt();
		//	***3
//		System.out.println("**4:r");
	k1 = 80;
	k2 = 80;
	msgBitLength = Wire.labelBitLength;

	snder = new NPOTSender(k1, k2, ois, oos);
	Micro_sender = snder;

    }

	public void initialize2() throws Exception {
		//	***7

		T = new BitMatrix(numOfChoices, k1);
		T.initialize(rnd);

		keyPairs = new BigInteger[k1][2];
		for (int i = 0; i < k1; i++) {
			keyPairs[i][0] = new BigInteger(k2, rnd);
			keyPairs[i][1] = new BigInteger(k2, rnd);
		}

		Micro_sender.execProtocol(keyPairs);
//		snder.execProtocol(keyPairs);
	}

	@Override
	public void step_2() throws Exception {
//		snder.execProtocol(keyPairs);
	}
}