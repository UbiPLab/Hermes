package Setup;

import Ciper.BGN;
import FileUtils.ReadFile;
import edu.biu.scapi.primitives.prf.IteratedPrfVarying;
import edu.biu.scapi.primitives.prf.PseudorandomFunction;
import edu.biu.scapi.primitives.prf.PseudorandomPermutation;
import edu.biu.scapi.tools.Factories.PrfFactory;
import myUtil.DataFormat;

import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * @Author UbiP Lab Laptop 02
 * @Date 2022/5/31 15:33
 * @Version 1.0
 */
public class Setup_init {
    /**
     * List<List<String>> shortestPathMatrix                double[][] shortestDistanceMatrix
     * BGN
     * PRF
     * PRP
     * T
     * H1
     * H2
     * k1,k2,k3,k4
     */


    public BGN bgn;
    public SecretKeySpec[] K;
    public PseudorandomPermutation PRP_T1;
    public PseudorandomPermutation PRP_T2;
    public PseudorandomFunction PRF_F1;
    public PseudorandomFunction PRF_F2;
    public HashMap<BigInteger,Integer> T1;
    public HashMap<BigInteger,Integer> T2;
    public BigInteger[] PK;
    public SecretKeySpec[] SK;
    public BigInteger sk;

    public List<BigInteger> L1;
    public List<BigInteger> L2;

    public void init_Map(){

    }

    public void init_Function(SecretKeySpec[] secretKey, int numberOfnodes) throws Exception {
        //  choose PRP_T

        PRP_T1 = (PseudorandomPermutation) PrfFactory.getInstance().getObject("LubyRackoffPrpFromPrfVarying");
        PRP_T1.setKey(secretKey[0]);


        PRP_T2 = (PseudorandomPermutation) PrfFactory.getInstance().getObject("LubyRackoffPrpFromPrfVarying");
        PRP_T2.setKey(secretKey[1]);

        //  choose PRF_F

        PRF_F1 = new IteratedPrfVarying();
        PRF_F1.setKey(secretKey[2]);

        PRF_F2 = new IteratedPrfVarying();
        PRF_F2.setKey(secretKey[3]);

        T1 = new HashMap<>(numberOfnodes);
        T2 = new HashMap<>(numberOfnodes);

        L1 = new ArrayList<>();
        L2 = new ArrayList<>();

        int[] randomArray = new DataFormat().createArray(0, numberOfnodes - 1, numberOfnodes);
//        int[] randomArray = new DataFormat().randomArray(0, numberOfnodes - 1, numberOfnodes);

        for (int i = 0; i < randomArray.length; i++) {
            byte[] intput = new DataFormat().toByteArr(randomArray[i]);
            byte[] out_1 = new byte[intput.length];
            byte[] out_2 = new byte[intput.length];
            PRP_T1.computeBlock(intput,0,intput.length,out_1,0,out_1.length);
            PRP_T2.computeBlock(intput,0,intput.length,out_2,0,out_2.length);
            T1.put(new BigInteger(out_1),randomArray[i]);
            T2.put(new BigInteger(out_2),randomArray[i]);
            L1.add(new BigInteger(out_1));
            L2.add(new BigInteger(out_2));
        }

//        SaveFile.saveString_list_s("L1.txt",L1);
//        SaveFile.saveString_list_s("L2.txt",L2);




    }

    public void init_Key() throws NoSuchAlgorithmException {
        //  generate secretKey 1-4

        String address_1 = "keyBytes_1.txt";
        String address_2 = "keyBytes_2.txt";
        String address_3 = "keyBytes_3.txt";
        String address_4 = "keyBytes_4.txt";
        KeyGenerator kg = KeyGenerator.getInstance("DES");
//        byte[] keyBytes = kg.generateKey().getEncoded();
//        SaveFile.saveByte_list_t(address_1,keyBytes);
//        SecretKeySpec secretKey1 = new SecretKeySpec(keyBytes, kg.getAlgorithm());
//        System.out.println("key1: " + Base64.getEncoder().encodeToString(secretKey1.getEncoded()));
        byte[] bytes = ReadFile.readByte_list_t(address_1);
        SecretKeySpec secretKey1_0 = new SecretKeySpec(bytes, kg.getAlgorithm());
//        System.out.println("key1: " + Base64.getEncoder().encodeToString(secretKey1_0.getEncoded()));

//        byte[] keyBytes2 = kg.generateKey().getEncoded();
//        SaveFile.saveByte_list_t(address_2,keyBytes2);
//        SecretKeySpec secretKey2= new SecretKeySpec(keyBytes2, kg.getAlgorithm());
//        System.out.println("key2: " + Base64.getEncoder().encodeToString(secretKey2.getEncoded()));
        byte[] bytes_2 = ReadFile.readByte_list_t(address_2);
        SecretKeySpec secretKey2_0 = new SecretKeySpec(bytes_2, kg.getAlgorithm());
//        System.out.println("key2: " + Base64.getEncoder().encodeToString(secretKey2_0.getEncoded()));

//        byte[] keyBytes3 = kg.generateKey().getEncoded();
//        SaveFile.saveByte_list_t(address_3,keyBytes3);
//        SecretKeySpec secretKey3 = new SecretKeySpec(keyBytes3, kg.getAlgorithm());
//        System.out.println("key3: " + Base64.getEncoder().encodeToString(secretKey3.getEncoded()));
        byte[] bytes_3 = ReadFile.readByte_list_t(address_3);
        SecretKeySpec secretKey3_0 = new SecretKeySpec(bytes_3, kg.getAlgorithm());
//        System.out.println("key3: " + Base64.getEncoder().encodeToString(secretKey3_0.getEncoded()));

//        byte[] keyBytes4 = kg.generateKey().getEncoded();
//        SaveFile.saveByte_list_t(address_4,keyBytes4);
//        SecretKeySpec secretKey4 = new SecretKeySpec(keyBytes4, kg.getAlgorithm());
//        System.out.println("key4: " + Base64.getEncoder().encodeToString(secretKey4.getEncoded()));
        byte[] bytes_4 = ReadFile.readByte_list_t(address_4);
        SecretKeySpec secretKey4_0 = new SecretKeySpec(bytes_4, kg.getAlgorithm());
//        System.out.println("key4: " + Base64.getEncoder().encodeToString(secretKey4_0.getEncoded()));

//        K = new SecretKeySpec[] {secretKey1,secretKey2,secretKey3,secretKey4};
        K = new SecretKeySpec[] {secretKey1_0,secretKey2_0,secretKey3_0,secretKey4_0};
//        SecretKeySpec[] secretKey = {secretKey1,secretKey2,secretKey3,secretKey4};

        //  init BGN

        bgn = new BGN();

        //  create PK

        PK = new BigInteger[]{ bgn.getQ2(), bgn.getG1().toBigInteger(),  bgn.getH1().toBigInteger()};
//        System.out.println(Arrays.toString(PK));
        //  create SK

//        SK = new SecretKeySpec[]{ secretKey1,  secretKey2,  secretKey3, secretKey4};
        SK = new SecretKeySpec[]{ secretKey1_0,  secretKey2_0,  secretKey3_0, secretKey4_0};

        sk = bgn.getQ1();
//        System.out.println(Arrays.toString(SK));
//        System.out.println(sk);
    }

    public static void main(String[] args) throws Exception {
        Setup_init setup_init = new Setup_init();
        setup_init.init_Key();
        setup_init.init_Function(setup_init.SK, 20);

        Iterator<BigInteger> iterator = setup_init.T1.keySet().iterator();
        while (iterator.hasNext()) {
            Object key = iterator.next();
            System.out.println("key:" + key + ",vaule:" + setup_init.T1.get(key));
        }
        System.out.println("========");
        Iterator<BigInteger> iterator_2 = setup_init.T2.keySet().iterator();
        while (iterator_2.hasNext()) {
            Object key = iterator_2.next();
            System.out.println("key:" + key + ",vaule:" + setup_init.T2.get(key));
        }

        System.out.println("========");
        for (int i = 0; i < setup_init.L1.size(); i++) {
            System.out.println("key:" + setup_init.L1.get(i) + ",vaule:" + i);
        }
        System.out.println("========");
        for (int i = 0; i < setup_init.L2.size(); i++) {
            System.out.println("key:" + setup_init.L2.get(i) + ",vaule:" + i);
        }
//        SaveFile.saveString_list_s("L1.txt", setup_init.L1);
//
//        List<BigInteger> list_s = ReadFile.readString_list_s("L1.txt");
//        System.out.println("========");
//        for (int i = 0; i < list_s.size(); i++) {
//            System.out.println("key:" + list_s.get(i) + ",vaule:" + i);
//        }
    }
    /*
    key1: TF5MXY+YN10=
key1: TF5MXY+YN10=
key2: UTSkT5cgkZs=
key2: UTSkT5cgkZs=
key3: xPhw4y8a/bY=
key3: xPhw4y8a/bY=
key4: uSCYFa4fSs4=
key4: uSCYFa4fSs4=
     */
}
