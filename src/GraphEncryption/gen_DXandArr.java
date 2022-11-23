package GraphEncryption;




import Ciper.BGN;
import edu.biu.scapi.primitives.prf.PseudorandomFunction;
import edu.biu.scapi.primitives.prf.PseudorandomPermutation;
import it.unisa.dia.gas.jpbc.Element;
import myUtil.DataFormat;
import myUtil.HashFounction;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

/**
 * @Author UbiP Lab Laptop 02
 * @Date 2022/3/1 15:39
 * @Version 1.0
 */
public class gen_DXandArr {
    public byte[][] DX;
    public byte[][][] Arr;
//    public String[] Arr_r;
//    public Pseudo pseudo;
//    public pi pi;
    public String[] K_vi;
//    public String[] r_vi;
    public String[] randomNumber_vi;
    public String[] K_vi_star;
    //
//    public PseudorandomFunction PRF_F1;
//    public PseudorandomFunction PRF_F2;
//    public PseudorandomPermutation PRP_T1;
//    public PseudorandomPermutation PRP_T2;
//
//    public HashMap<byte[],Integer> T1;
//    public HashMap<byte[],Integer> T2;


    public gen_DXandArr(int numberOfnodes, BGN bgn,Element[][] LatAndLon, List<List<String>> shortestPathMatrix, Element[][] shortestDistanceMatrix,
                        PseudorandomFunction PRF_F3, PseudorandomFunction PRF_F4, PseudorandomPermutation PRP_T1, PseudorandomPermutation PRP_T2,
                        HashMap<BigInteger,Integer> T1, HashMap<BigInteger,Integer> T2) throws Exception {
        DX = new byte[numberOfnodes][];
        Arr = new byte[numberOfnodes ][numberOfnodes][];
//        Arr_r = new String[numberOfnodes * (numberOfnodes)];
        //  每个节点的密钥
        K_vi = HashFounction.CreateSecretKey(numberOfnodes);
//        r_vi = HashFounction.CreateSecretKey(numberOfnodes);
        //  每个节点的随机数randomNumber

        randomNumber_vi = HashFounction.CreateSecretKey(numberOfnodes);
        K_vi_star = HashFounction.CreateSecretKey(3);   //  use for H(0,1) h(2)

        //  保证随机置换PRP后的数值在[0,n-1]
//        T1 = new HashMap<>();
//        T2 = new HashMap<>();
        //  map index with prp_T
//        int[] randomArray = new DataFormat().randomArray(0, numberOfnodes - 1, numberOfnodes);
//
//        for (int i = 0; i < randomArray.length; i++) {
//            byte[] intput = new DataFormat().toByteArr(randomArray[i]);
//            byte[] out = new byte[intput.length];
//            PRP_T1.computeBlock(intput,intput.length,out,out.length);
//            PRP_T2.computeBlock(intput,intput.length,out,out.length);
//            T1.put(out,randomArray[i]);
//            T2.put(out,randomArray[i]);
//        }
        
        for (int i = 0; i < numberOfnodes; i++) {
            //  add_vi || k_vi @ F4(vi)

            byte[] bytes_vi_input = new DataFormat().toByteArr(i);
//            byte[] add_out = new byte[bytes_vi_input.length];
//            PRP_T1.computeBlock(bytes_vi_input,0,bytes_vi_input.length,add_out,0,add_out.length);

//            byte[] bytes_add =  (i + K_vi[i]).getBytes(StandardCharsets.UTF_8);
            byte[] bytes_add = addBytes(bytes_vi_input, (K_vi[i]).getBytes(StandardCharsets.UTF_8));

//            byte[] bytes_vi_input = new DataFormat().toByteArr(i);
            byte[] bytes_F4_out = new byte[bytes_vi_input.length];
            PRF_F4.computeBlock(bytes_vi_input,0,bytes_vi_input.length,bytes_F4_out,0,bytes_F4_out.length);

            byte[] xor_DX = new DataFormat().xor(bytes_add, bytes_F4_out);


//            byte[] intput_index = new DataFormat().toByteArr(i);
            byte[] out = new byte[bytes_vi_input.length];
            PRP_T2.computeBlock(bytes_vi_input,0,bytes_vi_input.length,out,0,out.length);

            DX[T2.get(new BigInteger(out))] = xor_DX;

            //  first :    { Enc_pk(x)   || Enc_pk(y)  }  @ H2(k_vi || r_i)  , r_i

//            int x = LatAndLon[i][1];
//            int y = LatAndLon[i][2];
            //  1. Enc_pk(x),Enc_pk(y)
//            byte[] encrypt_x = bgn.encrypt(BigInteger.valueOf(x)).toBytes();
//            byte[] encrypt_y = bgn.encrypt(BigInteger.valueOf(y)).toBytes();
//            byte[] bytes_x_y = addBytes(bgn.encrypt(BigInteger.valueOf(x)).toBytes(), bgn.encrypt(BigInteger.valueOf(y)).toBytes());
            byte[] bytes_x_y = addBytes(LatAndLon[i][0].toBytes(), LatAndLon[i][1].toBytes());

            //  2. H2(k_vi || r_i)
            byte[] bytes_H2_input = (K_vi[i] + randomNumber_vi[i]).getBytes(StandardCharsets.UTF_8);
            byte[] bytes_H2 = HashFounction.HmacSHA256Encrypt(bytes_H2_input,K_vi_star[1]);
            //  3. xor
            byte[] xor = new DataFormat().xor(bytes_x_y, bytes_H2);

            Arr[i][0] = xor;
//            Arr[T1.get(add_out)][0] = xor;
            for (int j = 1; j < numberOfnodes; j++) {

                //  dataFormat : { H1(vj) || sp_ij  @ F3 ( h(r_j) ）|| Esd_ij  } @ H2(k1||r_j)   ,   r_j
                //  H1(vj)
                byte[] bytes_H1 = HashFounction.HmacSHA256Encrypt(String.valueOf(j), K_vi_star[0]);
                //  sp_ij
                byte[] bytes_sp = shortestPathMatrix.get(i).get(j).getBytes(StandardCharsets.UTF_8);
                //  H1(vj) || sp_ij
//                byte[] bytes_A = addBytes(bytes_H1, bytes_sp);

                //  F3 ( h(r_j) ）
                byte[] bytes_h = HashFounction.HmacSHA256Encrypt(randomNumber_vi[j], K_vi_star[2]);
                byte[] bytes_F3_out = new byte[bytes_h.length];
                PRF_F3.computeBlock(bytes_h,0,bytes_h.length,bytes_F3_out,0,bytes_F3_out.length);
                byte[] xor_AB = new DataFormat().xor(bytes_sp, bytes_F3_out);
                //  Esd_ij
//                byte[] bytes_Esd = bgn.encrypt(BigInteger.valueOf((long) shortestDistanceMatrix[i][j])).toBytes();
                byte[] bytes_Esd = null;
                bytes_Esd =  shortestDistanceMatrix[i][j].toBytes();
//                if (i<j){
//                    bytes_Esd =  shortestDistanceMatrix[i][j].toBytes();
//                }else {
//                    bytes_Esd = shortestDistanceMatrix[j][i].toBytes();
//                }


                //  F3 ( h(r_j) ）|| Esd_ij
//                byte[] bytes_B = addBytes(bytes_F3_out, bytes_Esd);

                //  { H1(vj) || sp_ij  @ F3 ( h(r_j) ）|| Esd_ij  }
//                byte[] xor_AB = new DataFormat().xor(bytes_A, bytes_B);
                byte[] AB = addBytes(bytes_H1,xor_AB);
                byte[] ABC = addBytes(AB,bytes_Esd);
                //   H2(k1||r_j)
                byte[] bytes_H2_input_T = (K_vi[i] + randomNumber_vi[j]).getBytes(StandardCharsets.UTF_8);
                byte[] bytes_H2_T = HashFounction.HmacSHA256Encrypt(bytes_H2_input_T,K_vi_star[1]);
                //   xor
                byte[] xor_ABC = new DataFormat().xor(ABC, bytes_H2_T);

                byte[] intput_index_Arr = new DataFormat().toByteArr(j);
                byte[] out_Arr = new byte[intput_index_Arr.length];
                PRP_T1.computeBlock(intput_index_Arr,0,intput_index_Arr.length,out_Arr,0,out_Arr.length);

                Arr[i][T1.get(new BigInteger(out_Arr))] = xor_ABC;

            }
        }
    }

    /**
     * 拼接字节数组
     * @param before 左边数组
     * @param after  右边数组
     * @return  集合数组
     */
    private byte[] addBytes(byte[] before, byte[] after) {
        byte[] res = new byte[before.length + after.length];
//        System.arraycopy(B,2,A,4,3);
        //源数组为B（将数组B部分元素拷贝至A数组），拷贝起始点下标为2，即从45开始拷贝；A数组拷贝开始点为下标为4，即从5开始更改数组内容，拷贝长度为3
        System.arraycopy(before,0,res,0,before.length);
        System.arraycopy(after,0,res,before.length,after.length);
        return res;
    }

    /**
     * from shortest path get mid node
     * @param path  shortest path
     * @return 返回途径点
     */
    public int[] getMidPath(String path){
        String[] str = path.split("--");
        int[] result = new int[str.length];
        for (int i = 0; i < str.length; i++) {
            if ("".equals(str[i])){
                continue;
            }
            result[i] = Integer.parseInt(str[i]);
        }
        return result;
    }
}
