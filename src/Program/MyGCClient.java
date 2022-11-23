package Program;

import OT.Temp;
import Utils.StopWatch;
import YaoGC.State;
import YaoGC.Wire;
import Utils.*;
import java.math.BigInteger;

/**
 * @Author UbiP Lab Laptop 02
 * @Date 2022/8/2 11:03
 * @Version 1.0
 */
public class MyGCClient extends ProgClient{
    public BigInteger cBits;   //  cBits = bv = random
    public BigInteger[] sBitslbs, cBitslbs;

    public State outputState;

    public MyGCClient(BigInteger bv, int length) {   //  bv is random ; length = n = 100
        cBits = bv;
        MyGCCommon.bitVecLen = length;
    }

    protected void init() throws Exception {
//        MyGCCommon.bitVecLen = MyGCCommon.ois.readInt();
//        System.out.println("**1:r");
        MyGCCommon.initCircuits();   //  创建电路

        otNumOfPairs = MyGCCommon.bitVecLen;

        super.init();
    }

    public void execTransfer() throws Exception {
//        System.out.println("**8:r");
        sBitslbs = new BigInteger[MyGCCommon.bitVecLen];

//        for (int i = 0; i < MyGCCommon.bitVecLen; i++) {
//            int bytelength = (Wire.labelBitLength - 1) / 8 + 1;     //  10
//            sBitslbs[i] = Utils.readBigInteger(bytelength, MyGCCommon.ois);
//        }
        sBitslbs = Temp.getTemp_sBitslps();
//        StopWatch.taskTimeStamp("receiving labels for peer's inputs");

        cBitslbs = new BigInteger[MyGCCommon.bitVecLen];
        rcver.execProtocol(cBits);  //  选择
//        rcver.execProtocol(cBits);  //  选择
//        cBitslbs = rcver.getData();
//        StopWatch.taskTimeStamp("receiving labels for self's inputs");
    }

    public void execCircuit() throws Exception {
        cBitslbs = rcver.getData();
        outputState = MyGCCommon.execCircuit(sBitslbs, cBitslbs);
    }


    public void interpretResult() throws Exception {
//        System.out.println("**11:w");
        Temp.setOutLabels(outputState.toLabels());
//        MyGCCommon.oos.writeObject(outputState.toLabels());
//        MyGCCommon.oos.flush();
    }

    public void verify_result() throws Exception {
        Temp.setcBits(cBits);
//        MyGCCommon.oos.writeObject(cBits);
//        MyGCCommon.oos.flush();
    }
}
