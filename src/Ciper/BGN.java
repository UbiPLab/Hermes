package Ciper;

import it.unisa.dia.gas.jpbc.*;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pairing.a1.TypeA1CurveGenerator;

import java.math.BigInteger;

/**
 * @Author UbiP Lab Laptop 02
 * @Date 2022/5/11 8:27
 * @Version 1.0
 */
public class BGN {


    private Element r;
    private TypeA1CurveGenerator pg;
    private PairingParameters typeA1Params;
    private BigInteger n;
    private BigInteger q1;
    private BigInteger q2;
    private Pairing pairing;
    private Field Z;
    private Element g;
    private Element u;
    private Element h;
    private Element g1;
    private Element h1;
    private ElementPowPreProcessing g_Pre;

    public Element getR() {
        return r;
    }

    public ElementPowPreProcessing getG_Pre() {
        return g_Pre;
    }

    public ElementPowPreProcessing getU_Pre() {
        return u_Pre;
    }

    public ElementPowPreProcessing getH_Pre() {
        return h_Pre;
    }

    public ElementPowPreProcessing getG1_Pre() {
        return g1_Pre;
    }

    public ElementPowPreProcessing getH1_Pre() {
        return h1_Pre;
    }

    private ElementPowPreProcessing u_Pre;
    private ElementPowPreProcessing h_Pre;
    private ElementPowPreProcessing g1_Pre;
    private ElementPowPreProcessing h1_Pre;
    private final static BigInteger MAXNUMBER = BigInteger.valueOf(40000);


    public BGN(){
        //  loading exist parameters
        pairing = PairingFactory.getPairing("my.properties");
        n = ReadBGNParamter.Read_n();
        //  sk
        q1 = ReadBGNParamter.Read_q1();
        q2 = ReadBGNParamter.Read_q2();
        Z = pairing.getZr();
        //Generator
        g = pairing.getG1().newElementFromBytes(ReadBGNParamter.Read_G()).getImmutable();//g from G
        u = pairing.getG1().newElementFromBytes(ReadBGNParamter.Read_U()).getImmutable();//u from G

//        Element G = pairing.getG1().newRandomElement();
//        G.setFromBytes(ReadBGNParamter.Read_G());
//        Element U = pairing.getG1().newRandomElement();
//        U.setFromBytes(ReadBGNParamter.Read_U());

//        Element H = U.getImmutable().pow(q2).getImmutable();
//        System.out.println("H:"+H);
//        Element G1 = pairing.pairing(G.getImmutable(),G.getImmutable()).getImmutable();
//        System.out.println("G1"+G1);

        h = u.pow(q2).getImmutable();//The method to generate h using u and q2
        g1 = pairing.pairing(g,g).getImmutable();
        h1 = pairing.pairing(g,h).getImmutable();
        //  Pre
        g_Pre = g.getElementPowPreProcessing();
        u_Pre = u.getElementPowPreProcessing();
        h_Pre = h.getElementPowPreProcessing();
        g1_Pre = g1.getElementPowPreProcessing();
        h1_Pre = h1.getElementPowPreProcessing();

        r=Z.newRandomElement().getImmutable();
    }

    public BigInteger getN() {
        return n;
    }

    public BigInteger getQ1() {
        return q1;
    }

    public BigInteger getQ2() {
        return q2;
    }

    public Pairing getPairing() {
        return pairing;
    }

    public Element getG() {
        return g;
    }

    public Element getU() {
        return u;
    }

    public Element getH() {
        return h;
    }

    public Element getG1() {
        return g1;
    }

    public Element getH1() {
        return h1;
    }

    public Field getZ() {
        return Z;
    }

    /**
     * random generate curve for bgn
     * @param numPrimes
     * @param bits
     */
    public BGN(int numPrimes,int bits){
        pg = new TypeA1CurveGenerator(numPrimes,bits);
        typeA1Params = pg.generate();
        pairing = PairingFactory.getPairing(typeA1Params);
        n=typeA1Params.getBigInteger("n");
        //  sk
        q1=typeA1Params.getBigInteger("n0");
        q2=typeA1Params.getBigInteger("n1");

        Z=pairing.getZr();
        //Generator
        g=pairing.getG1().newRandomElement().getImmutable();//g from G
        u=pairing.getG1().newRandomElement().getImmutable();//u from G
        h=u.pow(q2).getImmutable();//The method to generate h using u and q2
        g1=pairing.pairing(g,g).getImmutable();
        h1=pairing.pairing(g,h).getImmutable();


    }

    public  Element encrypt_2(BigInteger m){
//        Element r=Z.newRandomElement().getImmutable();
        return g.pow(m).mul(h.powZn(r)).getImmutable();
    }

    public  Element encrypt(BigInteger m){
//        Element r=Z.newRandomElement().getImmutable();
        return g_Pre.pow(m).mul(h_Pre.powZn(r)).getImmutable();
    }

    public  Element encrypt_G1_2(BigInteger m){
//        Element r=Z.newRandomElement().getImmutable();
        return g1.pow(m).mul(h1.powZn(r)).getImmutable();
    }

    public  Element encrypt_G1(BigInteger m){
//        Element r=Z.newRandomElement().getImmutable();
        return g1_Pre.pow(m).mul(h1_Pre.powZn(r)).getImmutable();
    }

//    public Element sub(Element C1, Element C2){
//        return C1.div(C2).mul(h.powZn(Z.newRandomElement())).getImmutable();
//    }
    public Element sub_2(Element C1, Element C2){
        return C1.div(C2).mul(h.powZn(r)).getImmutable();
    }
    public Element sub(Element C1, Element C2){
        return C1.div(C2).mul(h_Pre.powZn(r)).getImmutable();
    }

//    public Element sub_G1(Element C1, Element C2){
//        return C1.div(C2).mul(h1.powZn(Z.newRandomElement())).getImmutable();
//    }
    public Element sub_G1_2(Element C1, Element C2){
        return C1.div(C2).mul(h1.powZn(r)).getImmutable();
    }

    public Element sub_G1(Element C1, Element C2){
        return C1.div(C2).mul(h1_Pre.powZn(r)).getImmutable();
    }

//    public Element Mul(Element C1, Element C2){
//        return pairing.pairing(C1,C2).mul(h1.powZn(Z.newRandomElement())).getImmutable();
//    }
    public Element Mul_2(Element C1, Element C2){
        return pairing.pairing(C1,C2).mul(h1.powZn(r)).getImmutable();
    }

    public Element Mul(Element C1, Element C2){
        return pairing.pairing(C1,C2).mul(h1_Pre.powZn(r)).getImmutable();
    }



    // G1 的加法

    public Element add_G1_2(Element C1,Element C2){
        return C1.mul(C2).mul(h1.powZn(Z.newRandomElement())).getImmutable();
    }

    public Element add_G1(Element C1,Element C2){
        return C1.mul(C2).mul(h1_Pre.powZn(Z.newRandomElement())).getImmutable();
    }

    public Element add_2(Element C1, Element C2){
        return C1.mul(C2).mul(h.powZn(Z.newRandomElement())).getImmutable();
    }
    public Element add(Element C1, Element C2){
        return C1.mul(C2).mul(h_Pre.powZn(Z.newRandomElement())).getImmutable();
    }

    public BigInteger decrypt_2(Element C){
        Element Cq1=C.pow(q1).getImmutable();
        Element gq1=g.pow(q1).getImmutable();
        return Burp_decrypt(gq1,Cq1,MAXNUMBER);
    }

    public BigInteger decrypt(Element C){
        Element Cq1=C.pow(q1).getImmutable();
        Element gq1=g_Pre.pow(q1).getImmutable();
        return Burp_decrypt(gq1,Cq1,MAXNUMBER);
    }

    public BigInteger decrypt_G1_2(Element C){
        Element Cq1=C.pow(q1).getImmutable();
        Element gq1=g1.pow(q1).getImmutable();
        return Burp_decrypt(gq1,Cq1,MAXNUMBER);
    }

    public BigInteger decrypt_G1(Element C){
        Element Cq1=C.pow(q1).getImmutable();
        Element gq1=g1_Pre.pow(q1).getImmutable();
        return Burp_decrypt(gq1,Cq1,MAXNUMBER);
    }

    public PairingPreProcessing pairing_Same(Element Same){
        return pairing.getPairingPreProcessingFromElement(Same);
    }

    public Element Pre_pairing(Element C,PairingPreProcessing pre){
        return pre.pairing(C).mul(h1_Pre.powZn(r));
    }

    public static BigInteger Burp_decrypt(Element basepoint,Element y,BigInteger N){
        Element temp=basepoint.sub(basepoint).duplicate();
//        for(int i=0;BigInteger.valueOf(i).compareTo(N)==-1;i++){// i<N
//            if(basepoint.pow(BigInteger.valueOf(i)).isEqual(y)){
//                //System.out.println("Found");
//                return BigInteger.valueOf(i);
//            }
//        }
        for(int i = 0; BigInteger.valueOf(i).compareTo(N) < 0; i++){// i<N
            if(temp.isEqual(y)){
                //System.out.println("Found");
                return BigInteger.valueOf(i);
            }
            else {
                temp.mul(basepoint);
                //System.out.println(temp);
            }
        }
        //System.out.println("Not Found");
        return null;
    }

    public void showParamter(){
        System.out.println(toString());
    }

    @Override
    public String toString() {
        return "BGN{" +
                "n=" + n +
                ", q1=" + q1 +
                ", q2=" + q2 +
                ", g=" + g +
                ", u=" + u +
                ", h=" + h +
                ", g1=" + g1 +
                ", h1=" + h1 +
                '}';
    }
}
