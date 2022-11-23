// Copyright (C) 2010 by Yan Huang <yhuang@virginia.edu>

package OT;

import java.math.*;
import java.io.*;

public abstract class Receiver {
    protected BigInteger choices;
    protected int numOfChoices;
    protected ObjectInputStream ois;
    protected ObjectOutputStream oos;

    public BigInteger[] data = null;

    public Sender Micro_sender;

    public Receiver(int numOfChoices, ObjectInputStream in, ObjectOutputStream out) {
        this.numOfChoices = numOfChoices;
        ois = in;
        oos = out;
    }

    public void execProtocol(BigInteger choices) throws Exception {
        this.choices = choices;
    }

    public BigInteger[] getData() {
        return data;
    }

    public abstract void initialize2() throws Exception;

    public abstract void step_2() throws Exception;

    public abstract void exex();
}
