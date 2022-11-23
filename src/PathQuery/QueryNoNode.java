package PathQuery;

import Ciper.BGN;
import edu.biu.scapi.primitives.prf.PseudorandomFunction;
import edu.biu.scapi.primitives.prf.PseudorandomPermutation;
import it.unisa.dia.gas.jpbc.Element;
import myGraph.Point;
import myUtil.DataFormat;
import myUtil.HashFounction;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @Author UbiP Lab Laptop 02
 * @Date 2022/10/11 10:18
 * @Version 1.0
 */
public class QueryNoNode {

    public static int Len_recover = 0;

    public long GetFromDX(BGN bgn, PseudorandomFunction PRF_F3, PseudorandomPermutation PRP_T2, HashMap<BigInteger,Integer> T2, byte[][] DX, byte[][][] Arr, String[] random_vi, String[] K_vi_star, Point start, Point end, int[] q1, byte[][] q2, byte[][] q3,
                          List<Integer> Res_sd, List<String> Res_sp) throws Exception {

        //  int[] q1
        //  byte[][] q2 q3

        int index_start = start.id;
        int index_end = end.id;

        byte[] bytes_input = new DataFormat().toByteArr(start.id);
        byte[] bytes_out = new byte[bytes_input.length];
        PRP_T2.computeBlock(bytes_input,0,bytes_input.length,bytes_out,0,bytes_out.length);

//        PRP_T2.invertBlock(bytes_input,bytes_input.length,bytes_out,bytes_out.length,bytes_out.length);
        int start_id = T2.get(new BigInteger(bytes_out));
//        System.out.println("start_id : "+start_id);
        bytes_input = new DataFormat().toByteArr(end.id);
        PRP_T2.computeBlock(bytes_input,0,bytes_input.length,bytes_out,0,bytes_out.length);
//        PRP_T2.invertBlock(bytes_input,bytes_input.length,bytes_out,bytes_out.length,bytes_out.length);
        int end_id = T2.get(new BigInteger(bytes_out));
//        System.out.println("end_id : "+end_id);
        for (int i = 0; i < q1.length; i++) {
            if (start_id == q1[i]){
                index_start = i;
            }
            if (end_id == q1[i]){
                index_end = i;
            }
        }
//        System.out.println("index: "+index_start+","+index_end);



//        byte[] one = DX[T2.get(new BigInteger(String.valueOf(q1[index_start])))];
        byte[] one = DX[q1[index_start]];
        byte[] add_kv = new DataFormat().xor(one,q2[index_start]);

        byte[] add = Arrays.copyOfRange(add_kv,0,4);
        byte[] k_vi = Arrays.copyOfRange(add_kv,4,add_kv.length);

        int value_add  =new DataFormat().toInt(add);
        int value_j = q1[index_end];
//        int value_j = new DataFormat().toInt(q2[index_end]);

//        byte[] Arr_0 = Arr[value_add][0];
//        byte[] H2 = HashFounction.HmacSHA256Encrypt(addBytes(k_vi,random_vi[value_add].getBytes(StandardCharsets.UTF_8)),K_vi_star[1]);
//        //  get byte_x,byte_y
//        byte[] bytes_x_y = new DataFormat().xor(Arr_0,H2);
//
//        //  split byte_x_y
//        byte[] bytes_x = Arrays.copyOfRange(bytes_x_y,0,bytes_x_y.length/2);
//        byte[] bytes_y = Arrays.copyOfRange(bytes_x_y,bytes_x_y.length/2,bytes_x_y.length);
//
//        Element C_X = bgn.getG().getField().newElementFromBytes(bytes_x);
//        int x = bgn.decrypt(C_X).intValue();
//        System.out.println("start X: "+start.x+" , dec X: "+x);
//        System.out.println("end : "+end.id+","+value_j);

        byte[] Arr_start_end = Arr[value_add][value_j];
//        byte[] byte_vj = new byte[bytes_input.length];
//        PRP_T2.invertBlock(q2[index_end],q2[index_end].length,byte_vj,byte_vj.length,byte_vj.length );
//        int vj = new DataFormat().toInt(byte_vj);
        int vj = q1[index_end];

//        byte[] H2_byte = (K_vi[i] + randomNumber_vi[j]).getBytes(StandardCharsets.UTF_8);
        byte[] H2_byte = HashFounction.HmacSHA256Encrypt(addBytes(k_vi,random_vi[vj].getBytes(StandardCharsets.UTF_8)),K_vi_star[1]);
        byte[] befo = new DataFormat().xor(Arr_start_end,H2_byte);



        byte[] Esd = Arrays.copyOfRange(befo,befo.length-260,befo.length);
        byte[] sp_F3 = Arrays.copyOfRange(befo,q3[index_end].length,befo.length-Esd.length);
        byte[] bytes_h = HashFounction.HmacSHA256Encrypt(random_vi[vj], K_vi_star[2]);
        byte[] bytes_F3_out = new byte[bytes_h.length];
        PRF_F3.computeBlock(bytes_h,0,bytes_h.length,bytes_F3_out,0,bytes_F3_out.length);
        byte[] sp = new DataFormat().xor(sp_F3,bytes_F3_out);

        System.out.println("Recover length: "+ (Esd.length+sp.length));
        Len_recover += (Esd.length+sp.length);
        //  Recover
        long dec_start = System.currentTimeMillis();
        Element A=bgn.getG().getField().newElementFromBytes(Esd);
        int sd = bgn.decrypt(A).intValue();
        Res_sd.add(sd);
        String path = start_id + "-" + new String(sp)+"-"+String.valueOf(end_id);
        Res_sp.add(path);
        long dec_end = System.currentTimeMillis();
        return dec_end-dec_start;

    }

    public long GetFromDX_FP(BGN bgn, PseudorandomFunction PRF_F3, PseudorandomPermutation PRP_T2, HashMap<BigInteger,Integer> T2, byte[][] DX, byte[][][] Arr, String[] random_vi, String[] K_vi_star, Point start, Point end, int[] q1, byte[][] q2, byte[][] q3,
                          List<Integer> Res_sd, List<String> Res_sp,List<Element> cipertext) throws Exception {

        //  int[] q1
        //  byte[][] q2 q3

        int index_start = start.id;
        int index_end = end.id;

        byte[] bytes_input = new DataFormat().toByteArr(start.id);
        byte[] bytes_out = new byte[bytes_input.length];
        PRP_T2.computeBlock(bytes_input,0,bytes_input.length,bytes_out,0,bytes_out.length);

//        PRP_T2.invertBlock(bytes_input,bytes_input.length,bytes_out,bytes_out.length,bytes_out.length);
        int start_id = T2.get(new BigInteger(bytes_out));
//        System.out.println("start_id : "+start_id);
        bytes_input = new DataFormat().toByteArr(end.id);
        PRP_T2.computeBlock(bytes_input,0,bytes_input.length,bytes_out,0,bytes_out.length);
//        PRP_T2.invertBlock(bytes_input,bytes_input.length,bytes_out,bytes_out.length,bytes_out.length);
        int end_id = T2.get(new BigInteger(bytes_out));
//        System.out.println("end_id : "+end_id);
        for (int i = 0; i < q1.length; i++) {
            if (start_id == q1[i]){
                index_start = i;
            }
            if (end_id == q1[i]){
                index_end = i;
            }
        }
//        System.out.println("index: "+index_start+","+index_end);



//        byte[] one = DX[T2.get(new BigInteger(String.valueOf(q1[index_start])))];
        byte[] one = DX[q1[index_start]];
        byte[] add_kv = new DataFormat().xor(one,q2[index_start]);

        byte[] add = Arrays.copyOfRange(add_kv,0,4);
        byte[] k_vi = Arrays.copyOfRange(add_kv,4,add_kv.length);

        int value_add  =new DataFormat().toInt(add);
        int value_j = q1[index_end];
//        int value_j = new DataFormat().toInt(q2[index_end]);

//        byte[] Arr_0 = Arr[value_add][0];
//        byte[] H2 = HashFounction.HmacSHA256Encrypt(addBytes(k_vi,random_vi[value_add].getBytes(StandardCharsets.UTF_8)),K_vi_star[1]);
//        //  get byte_x,byte_y
//        byte[] bytes_x_y = new DataFormat().xor(Arr_0,H2);
//
//        //  split byte_x_y
//        byte[] bytes_x = Arrays.copyOfRange(bytes_x_y,0,bytes_x_y.length/2);
//        byte[] bytes_y = Arrays.copyOfRange(bytes_x_y,bytes_x_y.length/2,bytes_x_y.length);
//
//        Element C_X = bgn.getG().getField().newElementFromBytes(bytes_x);
//        int x = bgn.decrypt(C_X).intValue();
//        System.out.println("start X: "+start.x+" , dec X: "+x);
//        System.out.println("end : "+end.id+","+value_j);

        byte[] Arr_start_end = Arr[value_add][value_j];
//        byte[] byte_vj = new byte[bytes_input.length];
//        PRP_T2.invertBlock(q2[index_end],q2[index_end].length,byte_vj,byte_vj.length,byte_vj.length );
//        int vj = new DataFormat().toInt(byte_vj);
        int vj = q1[index_end];

//        byte[] H2_byte = (K_vi[i] + randomNumber_vi[j]).getBytes(StandardCharsets.UTF_8);
        byte[] H2_byte = HashFounction.HmacSHA256Encrypt(addBytes(k_vi,random_vi[vj].getBytes(StandardCharsets.UTF_8)),K_vi_star[1]);
        byte[] befo = new DataFormat().xor(Arr_start_end,H2_byte);



        byte[] Esd = Arrays.copyOfRange(befo,befo.length-260,befo.length);
        byte[] sp_F3 = Arrays.copyOfRange(befo,q3[index_end].length,befo.length-Esd.length);
        byte[] bytes_h = HashFounction.HmacSHA256Encrypt(random_vi[vj], K_vi_star[2]);
        byte[] bytes_F3_out = new byte[bytes_h.length];
        PRF_F3.computeBlock(bytes_h,0,bytes_h.length,bytes_F3_out,0,bytes_F3_out.length);
        byte[] sp = new DataFormat().xor(sp_F3,bytes_F3_out);

//        System.out.println("Recover length: "+ (Esd.length+sp.length));
        Len_recover += (Esd.length+sp.length);
        //  Recover
        long dec_start = System.currentTimeMillis();
        Element A=bgn.getG().getField().newElementFromBytes(Esd);
        cipertext.add(A.duplicate());

//        int sd = bgn.decrypt(A).intValue();
        Res_sd.add(1);
        String path = start_id + "-" + new String(sp)+"-"+String.valueOf(end_id);
        Res_sp.add(path);
        long dec_end = System.currentTimeMillis();
        return dec_end-dec_start;

    }

    private byte[] addBytes(byte[] before, byte[] after) {
        byte[] res = new byte[before.length + after.length];
//        System.arraycopy(B,2,A,4,3);
        //源数组为B（将数组B部分元素拷贝至A数组），拷贝起始点下标为2，即从45开始拷贝；A数组拷贝开始点为下标为4，即从5开始更改数组内容，拷贝长度为3
        System.arraycopy(before,0,res,0,before.length);
        System.arraycopy(after,0,res,before.length,after.length);
        return res;
    }
}
