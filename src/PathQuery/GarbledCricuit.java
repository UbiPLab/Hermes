package PathQuery;

import Program.MyGCCommon;
import Program.ProgCommon;
import Program.Program;
import Program.*;
import YaoGC.Circuit;

import java.math.BigInteger;

/**
 * @Author UbiP Lab Laptop 02
 * @Date 2022/8/10 9:34
 * @Version 1.0
 */
public class GarbledCricuit {

    public static int time = 0;



    public static MyGCServer myGCServer;
    public static MyGCClient myGCClient;

    public static void genCircuit() throws Exception{
        Program.iterCount = 1;
        BigInteger S_bits = BigInteger.valueOf(1000);
        BigInteger C_bits = BigInteger.valueOf(1000);
        int n = 2000;


        //  new GCServer GCClient
//         MyGCServer myGCServer = new MyGCServer(S_bits, n);
          myGCServer = new MyGCServer(S_bits, n);
//         MyGCClient myGCClient = new MyGCClient(C_bits, n);
          myGCClient = new MyGCClient(C_bits, n);


        myGCServer.run();
        myGCClient.run();

        //  ---initCircuits

        MyGCCommon.initCircuits();

        myGCClient.otNumOfPairs = MyGCCommon.bitVecLen;

        //  ---generateLabelPairs

//        myGCServer.generateLabelPairs();

        //  ---createCircuits
        Circuit.isForGarbling = true;
        for (int i = 0; i < ProgCommon.ccs.length; i++) {
            ProgCommon.ccs[i].build();
        }
        //  ***createCircuits
//        Circuit.isForGarbling = false;
//        for (int i = 0; i < ProgCommon.ccs.length; i++) {
//            ProgCommon.ccs[i].build();
//        }
//        StopWatch.taskTimeStamp("circuit preparation");


        //  ---initializeOT
        myGCServer.otNumOfPairs = MyGCCommon.bitVecLen;

        myGCServer.initializeOT();

        myGCClient.initializeOT();

        myGCServer.snder.initialize2();
        myGCClient.rcver.initialize2();

        myGCServer.snder.Micro_receiver.step_2();

        myGCServer.snder.initialize3();


    }

    /**
     * 判断Cilent输入是否大于0,默认flase=0=Cilent>=0,true=1=Cilent<0
     * @param Server_r
     * @param Cilent_dec_add_r
     * @return
     */
    public static int runGarbledCircuit(int Server_r,int Cilent_dec_add_r) {
        /**
         *      Server_r >= Cilent_dec_add_r     1
         *      Server_r < Cilent_dec_add_r     0
         *      Server_r  Cilent_dec_add_r
         *      1000        897         1
         *      1000        1000        1
         *      1000        1897        0
         */

//        Program.iterCount = 1;
//        BigInteger S_bits = BigInteger.valueOf(Server_r);
//        BigInteger C_bits = BigInteger.valueOf(Cilent_dec_add_r);
//        int n = 14;
//
//
//        //  new GCServer GCClient
//        MyGCServer myGCServer = new MyGCServer(S_bits, n);
//        MyGCClient myGCClient = new MyGCClient(C_bits, n);
//
//
//        try {
//            myGCServer.run();
//            myGCClient.run();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//        //  ---initCircuits
//
//        MyGCCommon.initCircuits();
//
//        myGCClient.otNumOfPairs = MyGCCommon.bitVecLen;
//
//        //  ---generateLabelPairs
//
////        myGCServer.generateLabelPairs();
//
//        //  ---createCircuits
//        Circuit.isForGarbling = true;
//        for (int i = 0; i < ProgCommon.ccs.length; i++) {
//            try {
//                ProgCommon.ccs[i].build();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        //  ***createCircuits
////        Circuit.isForGarbling = false;
////        for (int i = 0; i < ProgCommon.ccs.length; i++) {
////            ProgCommon.ccs[i].build();
////        }
////        StopWatch.taskTimeStamp("circuit preparation");
//
//
//        //  ---initializeOT
//        myGCServer.otNumOfPairs = MyGCCommon.bitVecLen;
//
//        try {
//            myGCServer.initializeOT();
//            myGCClient.initializeOT();
//
//            myGCServer.snder.initialize2();
//            myGCClient.rcver.initialize2();
//
//            myGCServer.snder.Micro_receiver.step_2();
//
//            myGCServer.snder.initialize3();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }



////        time++;
        BigInteger S_bits = BigInteger.valueOf(Server_r);
        BigInteger C_bits = BigInteger.valueOf(Cilent_dec_add_r);
        int n = 2000;
        myGCServer.sBits = S_bits;
        myGCClient.cBits = C_bits;



        try {
            //  ---execTransfer

            myGCServer.execTransfer();
            myGCClient.execTransfer();

            myGCServer.execS();
            myGCClient.rcver.exex();
            //  ***execCircuit
            Circuit.isForGarbling = false;

            myGCClient.execCircuit();

            //  ---execCircuit

            myGCServer.execCircuit();

            //  ***interpretResult
            myGCClient.interpretResult();
            //  ---interpretResult
            myGCServer.interpretResult();

            //  ---verify_result
            myGCClient.verify_result();
            myGCServer.verify_result();
        }catch (Exception e){
            e.printStackTrace();
        }






//        show_Big(myGCClient.cBitslbs);
//        show_Big(myGCClient.sBitslbs);

//        show_Big(myGCServer.outputState.toLabels());
//        show_Big(myGCClient.outputState.toLabels());

        //  ---interpretResult





        return myGCServer.tag;
//        return Server_r < Cilent_dec_add_r ? 0:1;
    }
}
