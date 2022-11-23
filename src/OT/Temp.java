package OT;

import java.math.BigInteger;

/**
 * @Author UbiP Lab Laptop 02
 * @Date 2022/10/17 8:30
 * @Version 1.0
 */
public class Temp {

    public static BigInteger[] pk0;
    public static BigInteger[][] cphPairs;

    public static BigInteger[][] msg;

    public static BigInteger[] temp_sBitslps;

    public static BigInteger[][] y;

    public static BigInteger[] outLabels;

    public static BigInteger[] keys;

    public static BigInteger choices;

    public static int numOfChoices;

    public static BigInteger[] data;

    public static BigInteger[][] gtt = null;

    public static BigInteger cBits;

    public static BigInteger getcBits() {
        return cBits;
    }

    public static void setcBits(BigInteger cBits) {
        Temp.cBits = cBits;
    }

    public static BigInteger[][] getGtt() {
        return gtt;
    }

    public static void setGtt(BigInteger[][] gtt) {
        Temp.gtt = gtt;
    }

    public static BigInteger[] getData() {
        return data;
    }

    public static void setData(BigInteger[] data) {
        Temp.data = data;
    }

    public static int getNumOfChoices() {
        return numOfChoices;
    }

    public static void setNumOfChoices(int numOfChoices) {
        Temp.numOfChoices = numOfChoices;
    }

    public static BigInteger getChoices() {
        return choices;
    }

    public static void setChoices(BigInteger choices) {
        Temp.choices = choices;
    }

    public static BigInteger[] getKeys() {
        return keys;
    }

    public static void setKeys(BigInteger[] keys) {
        Temp.keys = keys;
    }

    public static BigInteger[] getOutLabels() {
        return outLabels;
    }

    public static void setOutLabels(BigInteger[] outLabels) {
        Temp.outLabels = outLabels;
    }

    public static BigInteger[][] getY() {
        return y;
    }

    public static void setY(BigInteger[][] y) {
        Temp.y = y;
    }

    public static BigInteger[] getTemp_sBitslps() {
        return temp_sBitslps;
    }

    public static void setTemp_sBitslps(BigInteger[] temp_sBitslps) {
        Temp.temp_sBitslps = temp_sBitslps;
    }

    public static BigInteger[][] getMsg() {
        return msg;
    }

    public static void setMsg(BigInteger[][] msg) {
        Temp.msg = msg;
    }

    public static BigInteger[] getPk0() {
        return pk0;
    }

    public static void setPk0(BigInteger[] pk0) {
        Temp.pk0 = pk0;
    }

    public static BigInteger[][] getCphPairs() {
        return cphPairs;
    }

    public static void setCphPairs(BigInteger[][] cphPairs) {
        Temp.cphPairs = cphPairs;
    }

    public static BigInteger p, q, g, C, r;
    public static BigInteger Cr, gr;

    public static BigInteger getCr() {
        return Cr;
    }

    public static void setCr(BigInteger cr) {
        Cr = cr;
    }

    public static BigInteger getGr() {
        return gr;
    }

    public static void setGr(BigInteger gr) {
        Temp.gr = gr;
    }

    public static BigInteger getP() {
        return p;
    }

    public static void setP(BigInteger p) {
        Temp.p = p;
    }

    public static BigInteger getQ() {
        return q;
    }

    public static void setQ(BigInteger q) {
        Temp.q = q;
    }

    public static BigInteger getG() {
        return g;
    }

    public static void setG(BigInteger g) {
        Temp.g = g;
    }

    public static BigInteger getC() {
        return C;
    }

    public static void setC(BigInteger c) {
        C = c;
    }

    public static BigInteger getR() {
        return r;
    }

    public static void setR(BigInteger r) {
        Temp.r = r;
    }


}
