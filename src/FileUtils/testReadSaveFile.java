package FileUtils;

import Ciper.ReadBGNParamter;
import Ciper.SaveBGNParamter;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pairing.a1.TypeA1CurveGenerator;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * @Author UbiP Lab Laptop 02
 * @Date 2022/5/11
 * @Version 1.0
 */
public class testReadSaveFile {
    public static void main(String[] args) {
//        byte[] old = "jpbcfdgafg".getBytes(StandardCharsets.UTF_8);
//
//        System.out.println(Arrays.toString(old));
//        FileUtils.SaveFile.saveByte_list_t("byte.txt",old);
//
//        byte[] new_b = FileUtils.ReadFile.readByte_list_t("byte.txt");
//        System.out.println(Arrays.toString(new_b));

//        Pairing bp = PairingFactory.getPairing("my.properties");

//        TypeA1CurveGenerator pg = new TypeA1CurveGenerator(2, 512);
//        PairingParameters typeA1Params = pg.generate();
//        System.out.println(typeA1Params.toString());


//        BigInteger n=typeA1Params.getBigInteger("n");
//        BigInteger q1=typeA1Params.getBigInteger("n0");
//        BigInteger q2=typeA1Params.getBigInteger("n1");
//        BigInteger n = ReadBGNParamter.Read_n();
//        BigInteger q1 = ReadBGNParamter.Read_q1();
//        BigInteger q2 = ReadBGNParamter.Read_q2();
//        Pairing pairing = PairingFactory.getPairing(typeA1Params);
        Pairing pairing = PairingFactory.getPairing("my.properties");

        //Generator
        Element g=pairing.getG1().newRandomElement().getImmutable();//g from G

        //  show old paramter
        System.out.println("show old paramter:");
//        System.out.println("n:"+n);
//        System.out.println("q1:"+q1);
//        System.out.println("q2:"+q2);
        System.out.println("g:"+g);

        System.out.println("--------------------------");
        //  save paramter to local file
        byte[] toBytes = g.toBytes();
        System.out.println(toBytes.length);
        System.out.println(Arrays.toString(toBytes));

        SaveFile.saveByte_list_t("a.txt",toBytes);

        byte[] fromBytes = ReadFile.readByte_list_t("a.txt");
        System.out.println(fromBytes.length);
        System.out.println(Arrays.toString(fromBytes));

        Element g_o = pairing.getG1().newElementFromBytes(fromBytes);
//        System.out.println(g_o.setFromBytes(fromBytes));
        Element g_k  = g_o.getImmutable();
        System.out.println("g_o:"+g_o);
        System.out.println("g_k:"+g_k);


    }


}
