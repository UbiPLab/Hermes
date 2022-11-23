import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pairing.a1.TypeA1CurveGenerator;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @Author UbiP Lab Laptop 02
 * @Date 2022/5/10 14:35
 * @Version 1.0
 */
public class testADD {
    public static void main(String[] args) {
        TypeA1CurveGenerator pg = new TypeA1CurveGenerator(2, 512);
        PairingParameters typeA1Params = pg.generate();

        BigInteger n=typeA1Params.getBigInteger("n");
        //System.out.println(typeA1Params.toString(" "));

        //  sk
        BigInteger q1=typeA1Params.getBigInteger("n0");
        System.out.println(q1);
        //  11094172990343546844408059313654616460913055282974684314289897368554439628835737298744594533356089719949780427808364967324113177065780301991947670675408713
        BigInteger q2=typeA1Params.getBigInteger("n1");
        System.out.println(q2);
        //  7649628956153460181599203165222278005829907924076040410271151821525986810189150891004593755518779391823042629102401180169006316144513517884807254390662419
        //System.out.println(q1);
        //System.out.println(q1.multiply(q2).compareTo(n));
        Pairing pairing = PairingFactory.getPairing(typeA1Params);
        Field Z=pairing.getZr();
        //Generator
        Element g=pairing.getG1().newRandomElement();//g from G
        System.out.println(g);
        byte[] g_byte = g.toBytes();
        System.out.println(g_byte);
        //  17778904370188577775519965241385956654718937532172481289616296665645333547887490640220589277731664700206333970581431998911241408822619774879944720460196011065962591996387097662747129649174940101171500787036859996634212231453932182843401517690746652686662307377365726341989503736389346085919895710021277283496207,1873022944817481561609421010033398274315757624749171283495791686751540734306011024410810069494643331389500330322032307236724111818952071934024200014028660134451328629341331534688563793583564342851444953213003747630528378918910153380168707428529050693982902229503364904985609363211569517029040848944236772578417,0
        g.setFromBytes(g_byte);
        System.out.println(g);



        //        Element new_g = g.set(new BigInteger("17778904370188577775519965241385956654718937532172481289616296665645333547887490640220589277731664700206333970581431998911241408822619774879944720460196011065962591996387097662747129649174940101171500787036859996634212231453932182843401517690746652686662307377365726341989503736389346085919895710021277283496207,1873022944817481561609421010033398274315757624749171283495791686751540734306011024410810069494643331389500330322032307236724111818952071934024200014028660134451328629341331534688563793583564342851444953213003747630528378918910153380168707428529050693982902229503364904985609363211569517029040848944236772578417,0"));



        Element u=pairing.getG1().newRandomElement().getImmutable();//u from G
        //Element h= ElementUtils.getGenerator(pairing,g,typeA1Params,0,2);//genereta h of G_q1
        Element h=u.pow(q2).getImmutable();//The method to generate h using u and q2

        Element g1=pairing.pairing(g,g).getImmutable();

        System.out.println(g1);


        //  {x=42599557970078703668746969521032690019186713559443666979961012647892513712375559681917617500888616943494123066557228051770512359308558542487262673514400510913607295170530736564988850027721514675660725417691608656151356597280807680442767004206743680065569431818311839343079970727683031405679533930795321062031430,y=93919416047191013408704785559803599993458617631470042062004559584216143981933523654525366719225043014083197694026652359917402911176524873300454858449998706306239746793996343443670904187060319871557394505112893048867790124333744115191424122530114537066744323454508851727040548165848929424993569865112161718369685}
        Element h1=pairing.pairing(g,h).getImmutable();


        /*
         * Encrypt
         * */
        BigInteger m1=BigInteger.valueOf(172);
        BigInteger m2=BigInteger.valueOf(172);
        BigInteger m=BigInteger.valueOf(12345);
        System.out.println("m="+m);
        System.out.println("m1="+m1);
        System.out.println("m2="+m2);
        Element r=Z.newRandomElement().getImmutable();
        Element C=g.pow(m).mul(h.powZn(r)).getImmutable();
        Element r1=Z.newRandomElement().getImmutable();
        Element C1=g.pow(m1).mul(h.powZn(r1)).getImmutable();
        Element r2=Z.newRandomElement().getImmutable();
        Element C2=g.pow(m2).mul(h.powZn(r2)).getImmutable();
        /*
         * Sub
         * */
        Element C_sub=C1.div(C2).mul(h.powZn(Z.newRandomElement())).getImmutable();//m1-m2, C_sub in G
        /*
         * Mul
         * */
        Element C_mul=pairing.pairing(C1,C2).mul(h1.powZn(Z.newRandomElement())).getImmutable();//m1*m2, C_mul in G1
        /*
         * Mul2
         * */
        Element C_mul2=C1.mul(C2).mul(h.powZn(Z.newRandomElement())).getImmutable();//m1+m2, C_mul2 in G
        /*
         * Decrypt
         * */
        System.out.println("Decrypt:");
        Element Cq1=C.pow(q1).getImmutable();
        Element gq1=g.pow(q1).getImmutable();
        /*Pollard Decrypt*/
        //decrypt C
        //BigInteger x=Pollard_decrypt(gq1,Cq1,BigInteger.ZERO,BigInteger.valueOf(200).subtract(BigInteger.ONE),BigInteger.valueOf(1000));
        BigInteger x=Burp_decrypt(gq1,Cq1,BigInteger.valueOf(40000));
        System.out.println("m="+x);

        //decrypt C_sub
        Element C_sub_q1=C_sub.pow(q1).getImmutable();
        //x=Pollard_decrypt(gq1,C_sub_q1,BigInteger.ZERO,BigInteger.valueOf(200).subtract(BigInteger.ONE),BigInteger.valueOf(100));
        x=Burp_decrypt(gq1,C_sub_q1,BigInteger.valueOf(40000));
        System.out.println("m1-m2="+x);

        //decrypt C_mul
        Element C_mul_q1=C_mul.pow(q1).getImmutable();
        Element g1q1=g1.pow(q1).getImmutable();
        //x=Pollard_decrypt(g1q1,C_mul_q1,BigInteger.ZERO,BigInteger.valueOf(1000).subtract(BigInteger.ONE),BigInteger.valueOf(1000));
        x=Burp_decrypt(g1q1,C_mul_q1,BigInteger.valueOf(40000));
        System.out.println("m1*m2="+x);

        //decrypt C_mul2
        Element C_mul2_q1=C_mul2.pow(q1).getImmutable();
        //x=Pollard_decrypt(gq1,C_mul2_q1,BigInteger.ZERO,BigInteger.valueOf(1000).subtract(BigInteger.ONE),BigInteger.valueOf(100));
        x=Burp_decrypt(gq1,C_mul2_q1,BigInteger.valueOf(40000));
        System.out.println("m1+m2="+x);

        //Test Sim
//        Sim(g,g1,h,h1,q1,Z,pairing);
    }

    public static BigInteger Pollard_decrypt(Element basepoint,Element y,BigInteger a,BigInteger b,BigInteger N) throws NoSuchAlgorithmException {
        /**
         * See https://en.wikipedia.org/wiki/Pollard%27s_kangaroo_algorithm and
         * https://crypto.stackexchange.com/questions/47351/pollards-kangaroo-attack-on-elliptic-curve-groups
         * https://ece.uwaterloo.ca/~p24gill/Projects/Cryptography/Pollard's_Rho_and_Lambda/Project.pdf
         *
         * @param basepoint the generator g.
         * @param n order of the group(n=p1*q1).
         * @param y target y
         * @param a left searching margin
         * @param b right searching margin
         * @param N max search times
         */
        /*Setup*/
        int HashMod=1000;
        MessageDigest digest = MessageDigest.getInstance("SHA-256");//Hash function H
        BigInteger x_tame=BigInteger.ZERO;
        Element y_tame=basepoint.pow(b).getImmutable();

        while(N.compareTo(BigInteger.ZERO)==1){//N>0
            BigInteger yT=new BigInteger(digest.digest(y_tame.toBytes())).abs().mod(BigInteger.valueOf(HashMod));
            x_tame=x_tame.add(yT);
            y_tame=y_tame.mul(basepoint.pow(yT)).getImmutable();
            N=N.subtract(BigInteger.ONE);
        }
        //assert y_tame == (b + x_tame) * basepoint
        /*Search*/
        BigInteger x_wild=BigInteger.ZERO;
        Element y_wild=y;

        BigInteger upper_limit=b.subtract(a).add(x_tame);
        while(x_wild.compareTo(upper_limit)==-1){//x_wild<upper_limit
            BigInteger hy=new BigInteger(digest.digest(y_wild.toBytes())).abs().mod(BigInteger.valueOf(HashMod));
            x_wild=x_wild.add(hy);
            y_wild=y_wild.mul(basepoint.pow(hy)).getImmutable();
            if(y_wild.isEqual(y_tame)){
                System.out.println("Found");
                return b.add(x_tame).subtract(x_wild);
            }
        }
        System.out.println("Not Found");
        return null;
    }

    public static BigInteger Burp_decrypt(Element basepoint,Element y,BigInteger N){
        Element temp=basepoint.sub(basepoint).duplicate();
//        for(int i=0;BigInteger.valueOf(i).compareTo(N)==-1;i++){// i<N
//            if(basepoint.pow(BigInteger.valueOf(i)).isEqual(y)){
//                //System.out.println("Found");
//                return BigInteger.valueOf(i);
//            }
//        }
        for(int i=0;BigInteger.valueOf(i).compareTo(N)==-1;i++){// i<N
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

    public static void Sim(Element g,Element g1,Element h,Element h1,BigInteger q1,Field Z,Pairing pairing){
        BigInteger xpi=BigInteger.valueOf(6);
        BigInteger xd=BigInteger.valueOf(5);
        BigInteger xs=BigInteger.valueOf(4);

        BigInteger ypi=BigInteger.valueOf(3);
        BigInteger yd=BigInteger.valueOf(2);
        BigInteger ys=BigInteger.valueOf(1);

        long start_enc=System.currentTimeMillis();
        Element e_xpi=g.pow(xpi).mul(h.powZn(Z.newRandomElement()));
        Element e_xd=g.pow(xd).mul(h.powZn(Z.newRandomElement()));
        Element e_xs=g.pow(xs).mul(h.powZn(Z.newRandomElement()));

        Element e_ypi=g.pow(ypi).mul(h.powZn(Z.newRandomElement()));
        Element e_yd=g.pow(yd).mul(h.powZn(Z.newRandomElement()));
        Element e_ys=g.pow(ys).mul(h.powZn(Z.newRandomElement()));
        long end_enc=System.currentTimeMillis();
        System.out.println(end_enc-start_enc);
        long start_div=System.currentTimeMillis();
        Element ESPi1=e_xpi.div(e_xs).mul(h.powZn(Z.newRandomElement()));
        Element ESPi2=e_ypi.div(e_ys).mul(h.powZn(Z.newRandomElement()));
        Element ESD1=e_xd.div(e_xs).mul(h.powZn(Z.newRandomElement()));
        Element ESD2=e_yd.div(e_ys).mul(h.powZn(Z.newRandomElement()));
        long end_div=System.currentTimeMillis();
        System.out.println(end_div-start_div);

        long start_pairing=System.currentTimeMillis();
        Element e1=pairing.pairing(ESPi1,ESD1).mul(h1.powZn(Z.newRandomElement()));
        Element e2=pairing.pairing(ESPi2,ESD2).mul(h1.powZn(Z.newRandomElement()));
        long end_pairing=System.currentTimeMillis();
        System.out.println(end_pairing-start_pairing);

        long start_mul=System.currentTimeMillis();
        Element ciphertext=e1.mul(e2).mul(h1.powZn(Z.newRandomElement())).getImmutable();
        Element e_r=g1.pow(BigInteger.valueOf(1000)).mul(h1.powZn(Z.newRandomElement()));
        ciphertext=ciphertext.mul(e_r);
        long end_mul=System.currentTimeMillis();
        System.out.println(end_mul-start_mul);

        long start_dec=System.currentTimeMillis();
        BigInteger x=Burp_decrypt(g1.pow(q1),ciphertext.pow(q1),BigInteger.valueOf(10000));
        long end_dec=System.currentTimeMillis();
        System.out.println(end_dec-start_dec);
        System.out.println("x="+x);
    }
}
