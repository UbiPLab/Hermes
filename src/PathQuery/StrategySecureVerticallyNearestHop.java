package PathQuery;

import Ciper.BGN;
import it.unisa.dia.gas.jpbc.Element;
import myGraph.Point;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author UbiP Lab Laptop 02
 * @Date 2022/9/2 10:36
 * @Version 1.0
 */
public class StrategySecureVerticallyNearestHop {

    public Point getPoint_SVNH(BGN bgn, int Server_r_SVNH, Point start, Point end, List<Point> list){
        //  待商榷 ?
        if (list.size()==0){
            return end;
        }
        if (list.size()==1){
            return list.get(0);
        }
        Point p1 = list.get(0);
        int cb_5 = 0;
        Element ESD_X = bgn.sub(end.C_X, start.C_X);
        Element ESD_Y = bgn.sub(end.C_Y, start.C_Y);
//        Element EDS_X = bgn.sub(start.C_X, end.C_X);
        Element E_R =  bgn.encrypt_G1(BigInteger.valueOf(Server_r_SVNH));
        for (int i = 1; i < list.size(); i++) {
            Point p2 = list.get(i);
//            Element Num_P1P2_SD_S_R = Compute_ADD_R(bgn,ComputeUtils.Compute_ENum_P1P2_SD(bgn,ESD_X,ESD_Y,p1,p2),Server_r_SVNH);
            Element Num_P1P2_SD_S_R = compute_ENum_p1p2_SD(bgn,ESD_X,ESD_Y,p1,p2,E_R);
            cb_5 = GarbledCricuit.runGarbledCircuit(Server_r_SVNH,bgn.decrypt_G1(Num_P1P2_SD_S_R).intValue());
            if (cb_5==0){
                p1 = p2;
            }
        }

        //  best choice point on the same line
        List<Point> mid_left = new ArrayList<>();
        List<Point> mid_right = new ArrayList<>();
//        List<Point> On_L1 = new ArrayList<>();
//        On_L1.add(p1);
        int orient_p1 = StrategySecureOrient.Orient(bgn, Server_r_SVNH, p1, start, end);
        if (orient_p1==1){
            mid_left.add(p1);
        }else {
            mid_right.add(p1);
        }
        for (int i = 0; i < list.size(); i++) {
            Point p2 = list.get(i);
            if (p1.isSame(p2)){
                continue;
            }
            //  compute ENCS_SD_SP
//            Element NCS_SD_P1P2 = Compute_ADD_R(bgn,Compute_ENCS_SD_P1P2(bgn,ESD_X,ESD_Y,p1,p2),Server_r_SVNH);
            Element NCS_SD_P1P2 = compute_NCS_SD_p1p2(bgn,ESD_X,ESD_Y,p1,p2,E_R);
            int cb_1 = GarbledCricuit.runGarbledCircuit(Server_r_SVNH,bgn.decrypt_G1(NCS_SD_P1P2).intValue());
            if (cb_1 == 1) {
//                On_L1.add(p2);
                int orient = StrategySecureOrient.Orient(bgn, Server_r_SVNH, p2, start, end);
                if (orient == 1) {
                    mid_left.add(p2);
                } else {
                    mid_right.add(p2);
                }
            }
        }

        //  right most
        if (mid_right.size() != 0){
            p1 = mid_right.get(0);
            for (int i = 1; i < mid_right.size(); i++) {
                Point p2 = mid_right.get(i);
                Element Num_P1P2_OV_S_R = compute_ENum_P1P2_OV_S_R(bgn,start,end,p1,p2,E_R);
                int cb_4 = GarbledCricuit.runGarbledCircuit(Server_r_SVNH,bgn.decrypt_G1(Num_P1P2_OV_S_R).intValue());
                if (cb_4 == 0){
                    p1 = p2;
                }
            }
        }else {
        //  left most
            p1 = mid_left.get(0);
            for (int i = 1; i < mid_left.size(); i++) {
                Point p2 = mid_left.get(i);
                Element Num_P1P2_VO_S_R = compute_ENum_P1P2_nonOV_S_R(bgn,start,end,p1,p2,E_R);
                int cb_4 = GarbledCricuit.runGarbledCircuit(Server_r_SVNH,bgn.decrypt_G1(Num_P1P2_VO_S_R).intValue());
                if (cb_4 == 0){
                    p1 = p2;
                }
            }

        }

//        if (mid_right.size() != 0){
//            for (Point p_semi:mid_right) {
////                Element NCS_SD_SP = Compute_ADD_R(bgn,Compute_ENCS_SD_SP(bgn,ESD_X,ESD_Y,start,p_semi),Server_r_SVNH);
//                Element NCS_SD_SP = compute_NCS_SD_p1p2(bgn,ESD_X,ESD_Y,start,p_semi,E_R);
//                int cb_4 = GarbledCricuit.runGarbledCircuit(Server_r_SVNH,bgn.decrypt_G1(NCS_SD_SP).intValue());
//                if (cb_4 == 1){
//                    On_L1.add(p_semi);
//                }
//            }
//            if (On_L1.size()!=0){
//                p1 = On_L1.get(0);
//                for (int i = 1; i < On_L1.size(); i++) {
//                    Point p2 = On_L1.get(i);
//                    Element Num_P1P2_OV_S_R = compute_ENum_P1P2_OV_S_R(bgn,start,end,p1,p2,E_R);
//                    int cb_4 = GarbledCricuit.runGarbledCircuit(Server_r_SVNH,bgn.decrypt_G1(Num_P1P2_OV_S_R).intValue());
//                    if (cb_4 == 1){
//                        p1 = p2;
//                    }
//                }
//            }
//
//        }else {
//            for (Point p_semi:mid_left) {
//                Element NCS_SD_SP = compute_NCS_SD_p1p2(bgn,ESD_X,ESD_Y,start,p_semi,E_R);
//                int cb_4 = GarbledCricuit.runGarbledCircuit(Server_r_SVNH,bgn.decrypt_G1(NCS_SD_SP).intValue());
//                if (cb_4 == 1){
//                    On_L1.add(p_semi);
//                }
//            }
//            if (On_L1.size()!=0){
//                p1 = On_L1.get(0);
//                for (int i = 1; i < On_L1.size(); i++) {
//                    Point p2 = On_L1.get(i);
//                    Element Num_P1P2_OV_S_R = compute_ENum_P1P2_nonOV_S_R(bgn,start,end,p1,p2,E_R);
//                    int cb_4 = GarbledCricuit.runGarbledCircuit(Server_r_SVNH,bgn.decrypt_G1(Num_P1P2_OV_S_R).intValue());
//                    if (cb_4 == 1){
//                        p1 = p2;
//                    }
//                }
//            }
//        }

        return p1;
    }

    public Element compute_ENum_p1p2_SD(BGN bgn, Element ESD_X, Element ESD_Y, Point p1, Point p2, Element R){
//        Element C_X_p1_sub_C_X_p2 = bgn.sub(p1.C_X, p2.C_X);
        Element C_X_p1_sub_C_X_p2 = ComputeUtils.Compute_EP2P1_1(bgn,p1,p2);
//        Element C_Y_p1_sub_C_Y_p2 = bgn.sub(p1.C_Y,p2.C_Y);
        Element C_Y_p1_sub_C_Y_p2 = ComputeUtils.Compute_EP2P1_2(bgn,p1,p2);

        Element mul_1 = bgn.Mul(C_X_p1_sub_C_X_p2,ESD_X);
        Element mul_2 = bgn.Mul(C_Y_p1_sub_C_Y_p2,ESD_Y);
        return bgn.add_G1(bgn.add_G1(mul_1,mul_2),R);
    }

    public Element compute_NCS_SD_p1p2(BGN bgn, Element ESD_X, Element ESD_Y, Point p1, Point p2, Element R){
        Element C_X_p2_sub_C_X_p1 = ComputeUtils.Compute_EP1P2_1(bgn,p1,p2);
        Element C_Y_p2_sub_C_Y_p1 = ComputeUtils.Compute_EP1P2_2(bgn,p1,p2);

        Element mul_1 = bgn.Mul(C_X_p2_sub_C_X_p1,ESD_X);
        Element mul_2 = bgn.Mul(C_Y_p2_sub_C_Y_p1,ESD_Y);
        return bgn.add_G1(bgn.add_G1(mul_1,mul_2),R);
    }

    public Element compute_ENum_P1P2_OV_S_R(BGN bgn,Point start, Point end, Point p1,Point p2,Element R){
        Element EOV_X = ComputeUtils.compute_EOV_2(bgn,start,end);
        Element EOV_Y = ComputeUtils.compute_EOV_1(bgn,start,end);

        Element EP2P1_1 = ComputeUtils.Compute_EP1P2_1(bgn,p1,p2);
        Element EP2P1_2 = ComputeUtils.Compute_EP1P2_2(bgn,p1,p2);

        Element mul_1 = bgn.Mul(EP2P1_1,EOV_Y);
        Element mul_2 = bgn.Mul(EP2P1_2,EOV_X);
        return bgn.add_G1(bgn.add_G1(mul_1,mul_2),R);
    }

    public Element compute_ENum_P1P2_nonOV_S_R(BGN bgn,Point start, Point end, Point p1,Point p2,Element R){
        Element EVO_2 = ComputeUtils.compute_EVO_2(bgn,start,end);
        Element EVO_1 = ComputeUtils.compute_EVO_1(bgn,start,end);

        Element EP2P1_1 = ComputeUtils.Compute_EP1P2_1(bgn,p1,p2);
        Element EP2P1_2 = ComputeUtils.Compute_EP1P2_2(bgn,p1,p2);

        Element mul_1 = bgn.Mul(EP2P1_1,EVO_1);
        Element mul_2 = bgn.Mul(EP2P1_2,EVO_2);
        return bgn.add_G1(bgn.add_G1(mul_1,mul_2),R);
    }

}
