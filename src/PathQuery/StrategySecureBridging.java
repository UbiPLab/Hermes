package PathQuery;

import Ciper.BGN;
import myGraph.Point;

import java.util.List;

/**
 * @Author UbiP Lab Laptop 02
 * @Date 2022/8/7 8:37
 * @Version 1.0
 */
public class StrategySecureBridging {

    /**
     * 从中间区域选取桥接点B1
     * @param bgn
     * @param Server_r_bridge
     * @param start
     * @param end
     * @return
     */
    public Point choosePoint_B1(BGN bgn, int Server_r_bridge, List<Point> mid_list, List<Point> high_list, Point start, Point end){

        Point p = null;
        //  中间区域的点个数
        int len = mid_list.size();
        if (len==0){
            p = getB1_from_High(bgn,Server_r_bridge,high_list,start,end);
        }else {
            p = getB1_from_Mid(bgn,Server_r_bridge,start,end,mid_list);
//            p = getB1_from_Mid(bgn,Server_r_bridge,mid_list,start,end);
        }
//        switch (len){
//            case 0:
//                    List<Point> high_list = StrategySecureGroup.getEachArea(groups,1);
//                    p = getB1_from_High(bgn,Server_r_bridge,high_list,start,end);
//                    break;
//            case 1: p = mid_list.get(0);
//                    break;
//            default:
//                    p = getB1_from_Mid(bgn,Server_r_bridge,mid_list,start,end);
//        }
        return p;
    }

    private Point getB1_from_High(BGN bgn, int Server_r_bridge, List<Point> high_list, Point start, Point end) {
        //  00
        if (high_list.size()==0){
            return end;
        }
        //  分边
        StrategySecureOrient.Orient(bgn,Server_r_bridge,high_list,start,end);
        //
        Point p = high_list.get(0);
        for (int i = 1; i < high_list.size(); i++) {
            //  11
            if (high_list.get(i).side != -1 && p.side != high_list.get(i).side){
                return end;
            }
        }
        return p;
    }



    /**
     * mid_list从中选取B1   SVNH
     * @param bgn
     * @param Server_r_bridge
     * @param mid_list
     * @param start
     * @param end
     * @return
     */
//    private Point getB1_from_Mid(BGN bgn, int Server_r_bridge, List<Point> mid_list, Point start, Point end) {
//        Point p = mid_list.get(0);
//        for (int i = 1; i < mid_list.size(); i++) {
//            Point p2 = mid_list.get(i);
//            //  计算SP_1和SP_2在SD上的投影，两两相比，取最小值的点为P
//            //  p2 < p
//            if (runGarbledCircuit(Server_r_bridge,bgn.decrypt_G1(compute_ENum_p1_p2(bgn,start,end,p,p2,Server_r_bridge)).intValue()) == 0){
//                p = p2;
//            }
//        }
//        return p;
//    }

    private Point getB1_from_Mid(BGN bgn, int Server_r_bridge, Point start, Point end, List<Point> mid_list) {

        return new StrategySecureVerticallyNearestHop().getPoint_SVNH(bgn,Server_r_bridge,start,end,mid_list);
    }

    /**
     * 计算ENum_p1_p2
     * @param bgn
     * @param start
     * @param end
     * @param p1
     * @param p2
     * @param Server_r_bridge
     * @return
     */
//    public Element compute_ENum_p1_p2(BGN bgn,Point start,Point end,Point p1,Point p2,int Server_r_bridge){
//        Element C_X_p1_sub_C_X_p2 = bgn.sub(p1.C_X, p2.C_X);
//        Element C_Y_p1_sub_C_Y_p2 = bgn.sub(p1.C_Y,p2.C_Y);
//        Element C_X_d_sub_C_X_s = bgn.sub(end.C_X, start.C_X);
//        Element C_Y_d_sub_C_Y_s = bgn.sub(end.C_Y, start.C_Y);
//        Element R = bgn.encrypt_G1(BigInteger.valueOf(Server_r_bridge));
//        Element mul_1 = bgn.Mul(C_X_p1_sub_C_X_p2,C_X_d_sub_C_X_s);
//        Element mul_2 = bgn.Mul(C_Y_p1_sub_C_Y_p2,C_Y_d_sub_C_Y_s);
//        return bgn.add_G1(bgn.add_G1(mul_1,mul_2),R);
//    }

    /**
     * B2 = D
     * @param end
     * @return
     */
    public Point choosePoint_B2(Point end){
        return end;
    }


}
