// Copyright (C) 2010 by Yan Huang <yhuang@virginia.edu>

package Program;

import java.net.*;

import Utils.*;
import OT.*;
import YaoGC.*;



public abstract class ProgClient extends Program {

    public static String serverIPname = "localhost";             // server IP name
    private final int serverPort = 23456;                   // server port number
    private Socket sock = null;                    // Socket object for communicating

    public int otNumOfPairs;     //  长度
    public Receiver rcver;

    public void run() throws Exception {
//        create_socket_and_connect();

        super.run();

//        cleanup();
    }

    protected void init() throws Exception {
//        System.out.println(Program.iterCount);
//        ProgCommon.oos.writeInt(Program.iterCount);
//        ProgCommon.oos.flush();
//        System.out.println("**2:w");
        super.init();
    }

    private void create_socket_and_connect() throws Exception {
        sock = new java.net.Socket(serverIPname, serverPort);          // create socket and connect
        ProgCommon.oos = new java.io.ObjectOutputStream(sock.getOutputStream());
        ProgCommon.ois = new java.io.ObjectInputStream(sock.getInputStream());

    }

    private void cleanup() throws Exception {
        ProgCommon.oos.close();                                                   // close everything
        ProgCommon.ois.close();
        sock.close();
    }

    protected void createCircuits() throws Exception {
        Circuit.isForGarbling = false;
        Circuit.setIOStream(ProgCommon.ois, ProgCommon.oos);
        for (int i = 0; i < ProgCommon.ccs.length; i++) {
            ProgCommon.ccs[i].build();
        }

//        StopWatch.taskTimeStamp("circuit preparation");
    }

    public void initializeOT() throws Exception {
//        ProgCommon.oos.writeInt(otNumOfPairs);
//        ProgCommon.oos.flush();
//  ***0
//        System.out.println("**3:w");
        rcver = new OTExtReceiver(otNumOfPairs, ProgCommon.ois, ProgCommon.oos);
//        StopWatch.taskTimeStamp("OT preparation");
    }
}