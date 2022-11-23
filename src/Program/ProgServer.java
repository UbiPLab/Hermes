// Copyright (C) 2010 by Yan Huang <yhuang@virginia.edu>

package Program;

import java.net.*;
import java.io.*;

import org.apache.commons.io.output.CountingOutputStream;
import org.apache.commons.io.input.CountingInputStream;

import Utils.*;
import OT.*;
import YaoGC.*;

public abstract class ProgServer extends Program {

    final private int serverPort = 23456;             // server port number
    private ServerSocket sock = null;              // original server socket
    private Socket clientSocket = null;              // socket created by accept

    public Sender snder;
    public int otNumOfPairs;
    public int otMsgBitLength = Wire.labelBitLength;

    public void run() throws Exception {
//        create_socket_and_listen();

        super.run();

//        cleanup();
    }

    protected void init() throws Exception {
//        Program.iterCount = ProgCommon.ois.readInt();
//        System.out.println(Program.iterCount);
//        System.out.println("**2:r");
        super.init();
    }

    private void create_socket_and_listen() throws Exception {
        sock = new ServerSocket(serverPort);            // create socket and bind to port
        System.out.println("waiting for client to connect");
        clientSocket = sock.accept();                   // wait for client to connect
        System.out.println("client has connected");

        CountingOutputStream cos = new CountingOutputStream(clientSocket.getOutputStream());
        CountingInputStream cis = new CountingInputStream(clientSocket.getInputStream());

        ProgCommon.oos = new ObjectOutputStream(cos);
        ProgCommon.ois = new ObjectInputStream(cis);

        StopWatch.cos = cos;
        StopWatch.cis = cis;
    }

    private void cleanup() throws Exception {
        ProgCommon.oos.close();                          // close everything
        ProgCommon.ois.close();
        clientSocket.close();
        sock.close();
    }

    public void initializeOT() throws Exception {
        //  ***1
//        otNumOfPairs = ProgCommon.ois.readInt();
//        System.out.println("**3:r");
        snder = new OTExtSender(otNumOfPairs, otMsgBitLength, ProgCommon.ois, ProgCommon.oos);
//        StopWatch.taskTimeStamp("OT preparation");
    }

    protected void createCircuits() throws Exception {
        Circuit.isForGarbling = true;
        Circuit.setIOStream(ProgCommon.ois, ProgCommon.oos);
        for (int i = 0; i < ProgCommon.ccs.length; i++) {
            ProgCommon.ccs[i].build();
        }

//        StopWatch.taskTimeStamp("circuit preparation");
    }
}