// Copyright (C) 2010 by Yan Huang <yhuang@virginia.edu>

package OT;

import java.math.*;
import java.util.*;
import java.io.*;
import java.security.SecureRandom;

import Cipher.Cipher;
import Program.MyGCClient;
import Utils.StopWatch;
import YaoGC.Wire;

public class NPOTReceiver extends Receiver {
    private static SecureRandom rnd = new SecureRandom();

	public int msgBitLength;
    public BigInteger p, q, g, C;
	public BigInteger gr;

	public BigInteger[] gk, C_over_gk;
	public BigInteger[][] pk;

	public BigInteger[] keys;

    public NPOTReceiver(int numOfChoices, 
			    ObjectInputStream in, ObjectOutputStream out) throws Exception {
	super(numOfChoices, in, out);
//		StopWatch.pointTimeStamp("NPOTReceiver");
	initialize();

    }

    public void execProtocol(BigInteger choices) throws Exception {
	super.execProtocol(choices);

	step1();

//	step2();

    }

	@Override
	public void initialize2() throws Exception {

	}

	private void initialize() throws Exception {
//	C  = (BigInteger) ois.readObject();
//	p  = (BigInteger) ois.readObject();
//	q  = (BigInteger) ois.readObject();
//	g  = (BigInteger) ois.readObject();
//	gr = (BigInteger) ois.readObject();
//	msgBitLength = ois.readInt();
//	***5
//		System.out.println("**5:r");
		C = Temp.getC();
		p = Temp.getP();
		q = Temp.getQ();
		g = Temp.getG();
		gr = Temp.getGr();
		msgBitLength = Wire.labelBitLength;

		gk = new BigInteger[numOfChoices];
		C_over_gk = new BigInteger[numOfChoices];
		keys = new BigInteger[numOfChoices];
		for (int i = 0; i < numOfChoices; i++) {
			BigInteger k = (new BigInteger(q.bitLength(), rnd)).mod(q);
			gk[i] = g.modPow(k, p);
			C_over_gk[i] = C.multiply(gk[i].modInverse(p)).mod(p);
			keys[i] = gr.modPow(k, p);
		}

//	Temp.setKeys(keys);
    }

    private void step1() throws Exception {
	pk = new BigInteger[numOfChoices][2];
	BigInteger[] pk0 = new BigInteger[numOfChoices];
	for (int i = 0; i < numOfChoices; i++) {
	    int sigma = choices.testBit(i) ? 1 : 0;
	    pk[i][sigma] = gk[i];
	    pk[i][1-sigma] = C_over_gk[i];

	    pk0[i] = pk[i][0];
	}
	Temp.setPk0(pk0);
//		System.out.println("**6:w");
//	oos.writeObject(pk0);
//	oos.flush();
		//	***6


    }

    public void step_2() throws Exception {
//	BigInteger[][] msg = (BigInteger[][]) ois.readObject();
		//	***9
//		System.out.println("**7:r");
	BigInteger[][] msg = Temp.getMsg();
	data = new BigInteger[numOfChoices];
	for (int i = 0; i < numOfChoices; i++) {
	    int sigma = choices.testBit(i) ? 1 : 0;
	    data[i] = Cipher.decrypt(keys[i], msg[i][sigma], msgBitLength);
	}

    }

	@Override
	public void exex() {

	}
}
