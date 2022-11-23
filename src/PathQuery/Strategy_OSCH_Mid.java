package PathQuery;

import Ciper.BGN;
import it.unisa.dia.gas.jpbc.Element;
import myGraph.Point;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author UbiP Lab Laptop 02
 * @Date 2022/8/8 16:11
 * @Version 1.0
 */
public class Strategy_OSCH_Mid {

    public List<Point> sum_route_mid(BGN bgn, List<Point> mid_list, Point B1, Point B2, int Server_r_low){
        List<Point> result = new ArrayList<>();
        result.add(B1);
        int len = 0;    //  代表结果集长度，用于作分边的起点
        mid_list.remove(B1);
        while (mid_list.size()>0){
            Point p = check_number_mid(bgn,mid_list,result.get(len),B2,Server_r_low);
            //  加入结果集
            result.add(p);
            len++;
            //  从当前查询集合中清除
            mid_list.remove(p);
        }
        return result;
    }


    public Point check_number_mid(BGN bgn, List<Point> mid_list, Point B1, Point B2, int Server_r_mid){
        //
        Point p = null;    //  代表没有点
        
        int len = mid_list.size();

        if (len == 0){
            p = B2;
        }

        if (len==1){
            //  下面仅有一个点
            p = mid_list.get(0);
        }else if (len>1){
            p = mid_list.get(0);
            for (int i = 1; i < len; i++) {
                Point p2 = mid_list.get(i);
                //  p2 < p
                if ( GarbledCricuit.runGarbledCircuit(Server_r_mid,bgn.decrypt_G1(ComputeUtils.Compute_ADD_R(bgn,ComputeUtils.Compute_ENum_P1P2_SD(bgn,B1,B2,p,p2),Server_r_mid)).intValue())==0){
                    p = p2;
                }
            }
        }
        mid_list.remove(p);
        return p;
    }


//    /**
//     * 分边
//     *
//     * @param bgn
//     * @param Server_r_mid
//     * @param mid_list
//     * @param temp_start
//     * @param temp_end
//     */
//    public void Orient(BGN bgn, int Server_r_mid, List<Point> mid_list, Point temp_start, Point temp_end) {
//        //  len >= 2
//        for (Point point : mid_list) {
//            point.side = GarbledCricuit.runGarbledCircuit(Server_r_mid, bgn.decrypt_G1(compute_ENCS_SP_OV(bgn, Server_r_mid, temp_start, point, temp_end)).intValue());
//        }
//    }

    /**
     * C_X_s_sub_C_X_d
     *
     * @param bgn
     * @param start
     * @param end
     * @return
     */
    public Element compute_EOV_X(BGN bgn, Point start, Point end) {
        return bgn.sub(start.C_X, end.C_X);
    }

    /**
     * C_Y_s_sub_C_Y_d
     *
     * @param bgn
     * @param start
     * @param end
     * @return
     */
    public Element compute_EOV_Y(BGN bgn, Point start, Point end) {
        return bgn.sub(end.C_Y, start.C_Y);
    }

    /**
     * left = 1;right =-1
     *
     * @param bgn
     * @param Server_r_high
     * @param start
     * @param p
     * @param end
     * @return
     */
    public Element compute_ENCS_SP_OV(BGN bgn, int Server_r_high, Point start, Point p, Point end) {

        Element C_X_s_sub_C_X_d = compute_EOV_X(bgn, start, end);

        Element C_Y_d_sub_C_Y_s = compute_EOV_Y(bgn, start, end);

        Element C_X_p_sub_C_X_s = ComputeUtils.Compute_ESP_1(bgn, start, p);

        Element C_Y_p_sub_C_Y_s = ComputeUtils.Compute_ESP_2(bgn, start, p);

        Element R = bgn.encrypt_G1(BigInteger.valueOf(Server_r_high));

        Element Mul_and_Add_G = bgn.add_G1(bgn.Mul(C_Y_d_sub_C_Y_s, C_X_p_sub_C_X_s), bgn.Mul(C_X_s_sub_C_X_d, C_Y_p_sub_C_Y_s));

        return bgn.add_G1(Mul_and_Add_G, R);
    }
}
