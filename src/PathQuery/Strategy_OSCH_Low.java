package PathQuery;

import Ciper.BGN;
import it.unisa.dia.gas.jpbc.Element;
import myGraph.Point;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static PathQuery.StrategySecureOrient.Orient;

/**
 * @Author UbiP Lab Laptop 02
 * @Date 2022/8/8 10:16
 * @Version 1.0
 */
public class Strategy_OSCH_Low {

    public List<Point> sum_route_low(BGN bgn, List<Point> low_list, Point start, Point B1, int Server_r_low){
        List<Point> result = new ArrayList<>();
        result.add(start);
        int len = 0;    //  代表结果集长度，用于作分边的起点
        while (low_list.size()>0){
            Point p = check_number_low_round(bgn,low_list,result.get(len),B1,Server_r_low);
            //  加入结果集
            result.add(p);
            len++;
            //  从当前查询集合中清除
            low_list.remove(p);
        }
        return result;
    }

    public Point check_number_low_round(BGN bgn, List<Point> low_list, Point start, Point B1, int Server_r_low){
        //
        Point p = null;
        //  下面区域的点个数
        int len = low_list.size();

        if (len == 0){
            //  代表没有点
            p = B1;
        }
        if (len==1){
            //  下面仅有一个点
            p = low_list.get(0);
        }else if (len>1){
            //  有两个点以上 分边
            Orient(bgn,Server_r_low,low_list,start,B1);
//            Orient(bgn,Server_r_low,low_list,start,B1);
            //  Splitting
            //  Scanning
            p = Scan(bgn,Server_r_low,low_list,B1);
        }
        return p;
    }


//    /**
//     * 分边
//     * @param bgn
//     * @param Server_r_low
//     * @param low_list
//     * @param temp_start
//     * @param temp_end
//     */
//    public void Orient(BGN bgn, int Server_r_low, List<Point> low_list,Point temp_start,Point temp_end){
//        //  len >= 2
//        for (Point point : low_list) {
//            point.side = GarbledCricuit.runGarbledCircuit(Server_r_low, bgn.decrypt_G1(compute_ENCS_SP_OV(bgn, Server_r_low, temp_start, point, temp_end)).intValue());
//        }
//    }


    public Point Scan(BGN bgn, int Server_r_low, List<Point> low_list,Point temp_end){
        Point p = low_list.get(0);
        if (temp_end.side == 1){
            for (int i = 1; i < low_list.size(); i++) {
                if (GarbledCricuit.runGarbledCircuit (Server_r_low,bgn.decrypt_G1(ComputeUtils.Compute_ADD_R(bgn,compute_ENCS_SP_OV(bgn,Server_r_low,p,
                        low_list.get(i),temp_end),Server_r_low)).intValue())==0){
                    p = low_list.get(i);
                }
            }

        }else if (temp_end.side == 0){
            for (int i = 1; i < low_list.size(); i++) {
                if (GarbledCricuit.runGarbledCircuit (Server_r_low,bgn.decrypt_G1(ComputeUtils.Compute_ADD_R(bgn,compute_ENCS_SP_OV(bgn,Server_r_low,p,
                        low_list.get(i),temp_end),Server_r_low)).intValue())==1){
                    p = low_list.get(i);
                }
            }
        }
//        //  清除
//        low_list.remove(p);
        return p;
    }


    /**
     * C_X_s_sub_C_X_d
     * @param bgn
     * @param start
     * @param end
     * @return
     */
    public Element compute_EOV_X(BGN bgn, Point start, Point end){
        return bgn.sub(start.C_X, end.C_X);
    }

    /**
     * C_Y_s_sub_C_Y_d
     * @param bgn
     * @param start
     * @param end
     * @return
     */
    public Element compute_EOV_Y(BGN bgn, Point start, Point end){
        return bgn.sub(end.C_Y, start.C_Y);
    }

    /**
     * left = 1;right =-1
     * @param bgn
     * @param Server_r_low
     * @param start
     * @param p
     * @param end
     * @return
     */
    public Element compute_ENCS_SP_OV(BGN bgn,int Server_r_low,Point start,Point p,Point end){
        Element C_X_s_sub_C_X_d = compute_EOV_X(bgn,start,end);
        Element C_Y_d_sub_C_Y_s = compute_EOV_Y(bgn,start,end);
        Element C_X_p_sub_C_X_s = ComputeUtils.Compute_ESP_1(bgn,start,p);
        Element C_Y_p_sub_C_Y_s = ComputeUtils.Compute_ESP_2(bgn,start,p);
        Element R = bgn.encrypt_G1(BigInteger.valueOf(Server_r_low));
        Element Mul_and_Add_G = bgn.add_G1(bgn.Mul(C_Y_d_sub_C_Y_s,C_X_p_sub_C_X_s),bgn.Mul(C_X_s_sub_C_X_d,C_Y_p_sub_C_Y_s));
        return bgn.add_G1(Mul_and_Add_G,R);
    }



}
