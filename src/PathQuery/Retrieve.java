package PathQuery;

import Ciper.BGN;
import myGraph.Point;
import myUtil.DataFormat;
import myUtil.HashFounction;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * @Author UbiP Lab Laptop 02
 * @Date 2022/8/22 8:49
 * @Version 1.0
 */
public class Retrieve {

    /**
     *  从图加密中获取节点的x，y
     * @param bgn
     * @param DX
     * @param Arr
     * @param randomNumber_vi

     * @param K_vi_star

     * @return
     * @throws Exception
     */
    public Point Retrieve_XY(BGN bgn, byte[][] DX, byte[][][] Arr, String[] randomNumber_vi, String[] K_vi_star, int q1, byte[] q2
                               ) throws Exception {
        //  get a
//        byte[] byte_s_input = new DataFormat().toByteArr(q1);
//        byte[] bytes_out = new byte[byte_s_input.length];
//        PRP_T2.computeBlock(byte_s_input,0,byte_s_input.length,bytes_out,0,bytes_out.length);
//        int index = T2.get(new BigInteger(bytes_out));
//        byte[] bytes_a = DX[index];
//        System.out.println("index:"+index);

        byte[] bytes_a = DX[q1];
        //  get add || k
        byte[] bytes_out_F = q2.clone();
        byte[] bytes_add_k = new DataFormat().xor(bytes_a,bytes_out_F);

        //  前几位为 add， 后几位为 k
        //  get add
        int add = new DataFormat().toInt(Arrays.copyOfRange(bytes_add_k,0,4));
        byte[] k_vi = Arrays.copyOfRange(bytes_add_k,4,bytes_add_k.length);
//        byte[] add = new DataFormat().xor(bytes_add_k,K_vi[start.id].getBytes(StandardCharsets.UTF_8));

//        int id = T2.get(new BigInteger(add));
//        int id = new DataFormat().toInt(Arrays.copyOfRange(bytes_add_k,0,byte_s_input.length));
//        System.out.println("id:"+id);
//        Point point = new Point(id);

        //  get Arr[id]
//        byte[][] Arr_id = Arr[id];
        //  get $
        byte[] H2 = HashFounction.HmacSHA256Encrypt(addBytes(k_vi,randomNumber_vi[add].getBytes(StandardCharsets.UTF_8)),K_vi_star[1]);

        //  get byte_x,byte_y
        byte[] bytes_x_y = new DataFormat().xor(Arr[add][0],H2);

        //  split byte_x_y
        byte[] bytes_x = Arrays.copyOfRange(bytes_x_y,0,bytes_x_y.length/2);
        byte[] bytes_y = Arrays.copyOfRange(bytes_x_y,bytes_x_y.length/2,bytes_x_y.length);

        Point res = new Point(add);
        res.C_X = bgn.getG().getField().newElementFromBytes(bytes_x);
        res.C_Y = bgn.getG().getField().newElementFromBytes(bytes_y);
        res.x = bgn.decrypt(res.C_X).intValue();
        res.y = bgn.decrypt(res.C_Y).intValue();
        return res;
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
