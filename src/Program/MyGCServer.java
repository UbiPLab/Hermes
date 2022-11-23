package Program;

import OT.Temp;
import YaoGC.State;
import YaoGC.Wire;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * @Author UbiP Lab Laptop 02
 * @Date 2022/8/2 11:03
 * @Version 1.0
 */
public class MyGCServer extends ProgServer {
    public BigInteger sBits;   // 随机选择的
    public BigInteger res;
    public int tag;

    public State outputState;

    public BigInteger[][] sBitslps, cBitslps;

    public static final SecureRandom rnd = new SecureRandom();

    public MyGCServer(BigInteger bv, int length) {
        sBits = bv;
        MyGCCommon.bitVecLen = length;
    }

    protected void init() throws Exception {
//        MyGCCommon.oos.writeInt(MyGCCommon.bitVecLen);
//        MyGCCommon.oos.flush();
//        System.out.println("**1:w");
        MyGCCommon.initCircuits();

        generateLabelPairs();

        super.init();
    }

    public void generateLabelPairs() {
        sBitslps = new BigInteger[MyGCCommon.bitVecLen][2];
        cBitslps = new BigInteger[MyGCCommon.bitVecLen][2];

        for (int i = 0; i < MyGCCommon.bitVecLen; i++) {
            BigInteger glb0 = new BigInteger(Wire.labelBitLength, rnd);
            BigInteger glb1 = glb0.xor(Wire.R.shiftLeft(1).setBit(0));
            sBitslps[i][0] = glb0;
            sBitslps[i][1] = glb1;

            glb0 = new BigInteger(Wire.labelBitLength, rnd);
            glb1 = glb0.xor(Wire.R.shiftLeft(1).setBit(0));
            cBitslps[i][0] = glb0;
            cBitslps[i][1] = glb1;
        }
    }

    public void execTransfer() throws Exception {
        BigInteger[] temp_sBitslps = new BigInteger[MyGCCommon.bitVecLen];
        for (int i = 0; i < MyGCCommon.bitVecLen; i++) {
            int idx = sBits.testBit(i) ? 1 : 0;
            temp_sBitslps[i] = sBitslps[i][idx];
//            int bytelength = (Wire.labelBitLength - 1) / 8 + 1;
//            Utils.writeBigInteger(sBitslps[i][idx], bytelength, MyGCCommon.oos);
        }
//        MyGCCommon.oos.flush();

        //  ***10
//        System.out.println("**8:w");
        Temp.setTemp_sBitslps(temp_sBitslps);
//        StopWatch.taskTimeStamp("sending labels for selfs inputs");
//        snder.execProtocol(cBitslps);
//        StopWatch.taskTimeStamp("sending labels for peers inputs");

    }

    public void execS() throws Exception {
        snder.execProtocol(cBitslps);
//        StopWatch.taskTimeStamp("sending labels for peers inputs");
    }

    public void execCircuit() throws Exception {
        BigInteger[] sBitslbs = new BigInteger[MyGCCommon.bitVecLen];
        BigInteger[] cBitslbs = new BigInteger[MyGCCommon.bitVecLen];

        for (int i = 0; i < sBitslps.length; i++)
            sBitslbs[i] = sBitslps[i][0];

        for (int i = 0; i < cBitslps.length; i++)
            cBitslbs[i] = cBitslps[i][0];

        outputState = MyGCCommon.execCircuit(sBitslbs, cBitslbs);

//        show_Big(cBitslbs);
//        show_Big(sBitslbs);
    }

    public void interpretResult() throws Exception {
//        BigInteger[] outLabels = (BigInteger[]) MyGCCommon.ois.readObject();
//        System.out.println("**11:r");
        BigInteger[] outLabels = Temp.getOutLabels();

        BigInteger output = BigInteger.ZERO;



        for (int i = 0; i < outLabels.length; i++) {
            if (outputState.wires[i].value != Wire.UNKNOWN_SIG) {
                if (outputState.wires[i].value == 1)
                    output = output.setBit(i);  //  this | (1<<n)
                continue;
            } else if (outLabels[i].equals(outputState.wires[i].invd ?
                    outputState.wires[i].lbl :
                    outputState.wires[i].lbl.xor(Wire.R.shiftLeft(1).setBit(0)))) {
                output = output.setBit(i);
            } else if (!outLabels[i].equals(outputState.wires[i].invd ?
                    outputState.wires[i].lbl.xor(Wire.R.shiftLeft(1).setBit(0)) :
                    outputState.wires[i].lbl)){}
//                throw new Exception("Bad label encountered: i = " + i + "\t" +
//                        outLabels[i] + " != (" +
//                        outputState.wires[i].lbl + ", " +
//                        outputState.wires[i].lbl.xor(Wire.R.shiftLeft(1).setBit(0)) + ")");

        }

//        System.out.println("output (pp): " + output);
//        res = output;
//        StopWatch.taskTimeStamp("output labels received and interpreted");
    }

    public void verify_result() throws Exception {
//        BigInteger cBits = (BigInteger) MyGCCommon.ois.readObject();

        BigInteger cBits = Temp.getcBits();
//        System.out.println("output (verify): " +
//                (cBits.intValue()>sBits.intValue()?cBits:sBits));
        tag = sBits.intValue() < cBits.intValue() ? 0:1;
//        System.out.println(cBits);
//        BigInteger res = sBits.xor(cBits);
//        System.out.println(res);
//        System.out.println("output (verify): " +
//                res.bitCount());
    }
}
