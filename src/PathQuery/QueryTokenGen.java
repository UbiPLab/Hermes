package PathQuery;

import edu.biu.scapi.primitives.prf.PseudorandomFunction;
import edu.biu.scapi.primitives.prf.PseudorandomPermutation;
import myGraph.Point;
import myUtil.DataFormat;
import myUtil.HashFounction;

import javax.crypto.IllegalBlockSizeException;
import java.math.BigInteger;
import java.util.HashMap;

/**
 * @Author UbiP Lab Laptop 02
 * @Date 2022/8/4 10:37
 * @Version 1.0
 */
public class QueryTokenGen {

    /**
     *
     * @param PRP_T2    伪随机置换
     * @param start     起点
     * @param point     途径点序列
     * @return      伪随机置换后的序列
     * @throws IllegalBlockSizeException
     */
    public int[] generateToken_q1(PseudorandomPermutation PRP_T2, HashMap<BigInteger,Integer> T2, Point start, Point end, Point[] point) throws IllegalBlockSizeException {
        DataFormat dataFormat = new DataFormat();
        byte[] intput_index = dataFormat.toByteArr(start.id);
        byte[] out = new byte[intput_index.length];
        PRP_T2.computeBlock(intput_index,0,intput_index.length,out,0,out.length);
        int[] res = new int[point.length + 2];
        res[0] = T2.get(new BigInteger(out));
//        res[0] = dataFormat.toInt(out);

        intput_index = dataFormat.toByteArr(end.id);
        PRP_T2.computeBlock(intput_index,0,intput_index.length,out,0,out.length);
        res[1] = T2.get(new BigInteger(out));
//        res[1] = dataFormat.toInt(out);
        for (int i = 0; i < point.length; i++) {
            intput_index = dataFormat.toByteArr(point[i].id);
            PRP_T2.computeBlock(intput_index,0,intput_index.length,out,0,out.length);
            res[i+2] = T2.get(new BigInteger(out));
//            res[i+2] = dataFormat.toInt(out);
        }
        return res;

    }

    /**
     *
     * @param PRF_F4    伪随机排列
     * @param start     起点
     * @param point     途径点序列
     * @return      伪随机排列后的序列
     * @throws IllegalBlockSizeException
     */
    public byte[][] generateToken_q2(PseudorandomFunction PRF_F4, Point start, Point end, Point[] point) throws IllegalBlockSizeException {
        DataFormat dataFormat = new DataFormat();
        byte[] intput_index = dataFormat.toByteArr(start.id);
        byte[] out = new byte[intput_index.length];
        PRF_F4.computeBlock(intput_index,0,intput_index.length,out,0,out.length);
        byte[][] res = new byte[point.length + 2][];
        res[0] = out.clone();
        intput_index = dataFormat.toByteArr(end.id);
        PRF_F4.computeBlock(intput_index,0,intput_index.length,out,0,out.length);
        res[1] = out.clone();
        for (int i = 0; i < point.length; i++) {
            intput_index = dataFormat.toByteArr(point[i].id);
            PRF_F4.computeBlock(intput_index,0,intput_index.length,out,0,out.length);
            res[i+2] = out.clone();
        }
        return res;
    }

    /**
     *
     * @param end       终点
     * @param point     途径点序列
     * @return      Hash后的数组
     */
    public byte[][] generateToken_q3(Point start, Point end, Point[] point, String[] K_vi_star) throws Exception {
        byte[][] res = new byte[point.length + 2][];
        res[0] = HashFounction.HmacSHA256Encrypt(String.valueOf(start.id), K_vi_star[0]);
        res[1] = HashFounction.HmacSHA256Encrypt(String.valueOf(end.id),K_vi_star[0]);
        for (int i = 0; i < point.length; i++) {
            res[i+2] = HashFounction.HmacSHA256Encrypt(String.valueOf(point[i]),K_vi_star[0]);
        }
        return res;
    }


}
