package Program;

import Utils.StopWatch;
import YaoGC.*;


import java.math.BigInteger;

/**
 * @Author UbiP Lab Laptop 02
 * @Date 2022/8/2 11:03
 * @Version 1.0
 */
public class MyGCCommon extends ProgCommon{
    public static int bitVecLen;   //  bitVecLen = length = n = 100

    static int bitLength(int x) {
        return BigInteger.valueOf(x).bitLength();
    }

    public static void initCircuits() {
        ccs = new Circuit[1];
//        ccs[0] = new GT_2L_1(bitVecLen);
//        ccs[0] = new myCMP_2L_L(bitVecLen);
        ccs[0] = new MAX_2L_L(bitVecLen);
//        ccs[0] = new MIN_2L_L(bitVecLen);
//        ccs[0] = new MyGC_3_1(2,1,2);
        //  k is bitVecLen bit-length eg. 8,4
    }

    public static State execCircuit(BigInteger[] slbs, BigInteger[] clbs) throws Exception {
        BigInteger[] lbs = new BigInteger[2 * bitVecLen];
        System.arraycopy(slbs, 0, lbs, 0, bitVecLen);
        System.arraycopy(clbs, 0, lbs, bitVecLen, bitVecLen);
        State in = State.fromLabels(lbs);

        State out = ccs[0].startExecuting(in);

//        StopWatch.taskTimeStamp("circuit garbling");

        return out;
    }
}
