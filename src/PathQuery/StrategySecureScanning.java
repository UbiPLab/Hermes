package PathQuery;

import Ciper.BGN;
import myGraph.Point;

import java.util.ArrayList;
import java.util.List;

import static PathQuery.ComputeUtils.Compute_ADD_R;
import static PathQuery.StrategySecureOrient.Orient;

/**
 * @Author UbiP Lab Laptop 02
 * @Date 2022/8/28 8:12
 * @Version 1.0
 */
public class StrategySecureScanning {

    public static Point Scan(BGN bgn, int Server_r, List<Point> list_point, Point B1){
        if (list_point.size()==0){
            return B1;
        }

        Point p = list_point.get(0);

        if (p.group_id == -1){
            if (B1.side == 1){
                //  Low 顺时针
                for (int i = 1; i < list_point.size(); i++) {
                    Point p2 = list_point.get(i);
                    int orient = Orient(bgn, Server_r, p2, p, B1);
                    if (orient==0){
                        //  找最右边的点
                        p = p2;
                    }
                }
            }else if (B1.side == 0){
                //  Low 逆时针
                for (int i = 1; i < list_point.size(); i++) {
                    Point p2 = list_point.get(i);
                    int orient = Orient(bgn, Server_r, p2, p, B1);
                    if (orient==1){
                        //  找最左边的点
                        p = p2;
                    }
                }
            }else if(B1.side == -1){
                //  B1 与 D 重合
                //  Low 顺时针
                for (int i = 1; i < list_point.size(); i++) {
                    Point p2 = list_point.get(i);
                    int orient = Orient(bgn, Server_r, p2, p, B1);
                    if (orient==0){
                        //  找最右边的点
                        p = p2;
                    }
                }
            }
        }

        return p;
    }

    public static Point Scan(BGN bgn, int Server_r,  Point start, Point end, Point B1, List<Point> list_left_Semi, List<Point> list_right_Semi, int Side){

        Point res = null;


        if (Side == 1){
            //  B1 left
            if (list_right_Semi.size() != 0){
                List<Point> On_L1 = new ArrayList<>();
                //  low_semi_right_right
                Point p1 = list_right_Semi.get(0);
                for (Point p_i:list_right_Semi) {
                    int cb_4 = GarbledCricuit.runGarbledCircuit(Server_r, bgn.decrypt_G1(Compute_ADD_R(bgn, ComputeUtils.Compute_ENCS_DS_SP(bgn, start, p_i, end), Server_r)).intValue());
                    if (cb_4 == 1){
                        On_L1.add(p_i);
                    }
                }

                if (On_L1.size() !=0 ){
                    //  on L1
                    p1 = On_L1.get(0);
                    for (int i = 1; i < On_L1.size(); i++) {
                        Point p2 = On_L1.get(i);
                        int cb_4 = GarbledCricuit.runGarbledCircuit(Server_r, bgn.decrypt_G1(Compute_ADD_R(bgn,ComputeUtils.Compute_ENum_P1P2_OV(bgn,start,end,p1,p2),Server_r)).intValue());
                        if (cb_4 == 0){
                            p1 = p2;
                        }
                    }


                }else {
                    //  common
                    for (int i = 1; i < list_right_Semi.size(); i++) {
                        Point p2 = list_right_Semi.get(i);
                        int orient = Orient(bgn, Server_r, p2, p1, B1);
                        if (orient==0){
                            //  找最右边的点
                            p1 = p2;
                        }
                    }
                }
                res = p1;
            }else if(list_left_Semi.size() !=0){
                List<Point> On_B1S = new ArrayList<>();
                //  low_semi_right_left
                Point p1 = list_left_Semi.get(0);
                for (Point p_i:list_left_Semi) {
                    //  Encs_B1Sov_SP
//                    int cb_4 = GarbledCricuit.runGarbledCircuit(Server_r, bgn.decrypt_G1(Compute_ADD_R(bgn, ComputeUtils.Compute_ENCS_DS_SP(bgn, start, p_i, B1), Server_r)).intValue());
                    int cb_4 = GarbledCricuit.runGarbledCircuit(Server_r, bgn.decrypt_G1(Compute_ADD_R(bgn, ComputeUtils.compute_ENCS_SP_OV_B1S(bgn, start, p_i, B1), Server_r)).intValue());
                    if (cb_4 == 1){
                        On_B1S.add(p_i);
                    }
                }

                if (On_B1S.size() !=0 ){
                    //  on B1S
                    p1 = On_B1S.get(0);
                    for (int i = 1; i < On_B1S.size(); i++) {
                        Point p2 = On_B1S.get(i);
                        int cb_4 = GarbledCricuit.runGarbledCircuit(Server_r, bgn.decrypt_G1(Compute_ADD_R(bgn,ComputeUtils.Compute_ENum_P1P2_DS(bgn,start,B1,p1,p2),Server_r)).intValue());
                        if (cb_4 == 0){
                            p1 = p2;
                        }
                    }


                }else {
                    //  common
                    for (int i = 1; i < list_left_Semi.size(); i++) {
                        Point p2 = list_left_Semi.get(i);
                        int orient = Orient(bgn, Server_r, p2, p1, B1);
                        if (orient==0){
                            //  找最右边的点
                            p1 = p2;
                        }
                    }
                }
                res = p1;
            }

        }else if (Side == 0){

            //  B1 right
            if(list_left_Semi.size() !=0){
                //  low_semi_left_left
                List<Point> On_B1S = new ArrayList<>();
                Point p1 = list_left_Semi.get(0);
                for (Point p_i:list_left_Semi) {
                    int cb_4 = GarbledCricuit.runGarbledCircuit(Server_r, bgn.decrypt_G1(Compute_ADD_R(bgn, ComputeUtils.compute_ENCS_SP_OV_B1S(bgn, start, p_i, B1), Server_r)).intValue());
                    if (cb_4 == 1){
                        On_B1S.add(p_i);
                    }
                }

                if (On_B1S.size() !=0 ){
                    //  on B1S
                    p1 = On_B1S.get(0);
                    for (int i = 1; i < On_B1S.size(); i++) {
                        Point p2 = On_B1S.get(i);
                        int cb_4 = GarbledCricuit.runGarbledCircuit(Server_r, bgn.decrypt_G1(Compute_ADD_R(bgn,ComputeUtils.Compute_ENum_P1P2_DS(bgn,start,B1,p1,p2),Server_r)).intValue());
                        if (cb_4 == 0){
                            p1 = p2;
                        }
                    }


                }else {
                    List<Point> On_L1 = new ArrayList<>();

                    for (Point p_i:list_left_Semi) {
                        int cb_4 = GarbledCricuit.runGarbledCircuit(Server_r, bgn.decrypt_G1(Compute_ADD_R(bgn, ComputeUtils.Compute_ENCS_DS_SP(bgn, start, p_i, end), Server_r)).intValue());
                        if (cb_4 == 1){
                            On_L1.add(p_i);
                        }
                    }

                    if (On_L1.size() !=0 ){
                        //  on L1
                        p1 = On_L1.get(0);
                        for (int i = 1; i < On_L1.size(); i++) {
                            Point p2 = On_L1.get(i);
                            int cb_4 = GarbledCricuit.runGarbledCircuit(Server_r, bgn.decrypt_G1(Compute_ADD_R(bgn,ComputeUtils.Compute_ENum_P1P2_VO(bgn,start,end,p1,p2),Server_r)).intValue());
                            if (cb_4 == 0){
                                p1 = p2;
                            }
                        }


                    }else {
                        //  common
                        for (int i = 1; i < list_left_Semi.size(); i++) {
                            Point p2 = list_left_Semi.get(i);
                            int orient = Orient(bgn, Server_r, p1, p2, B1);
                            if (orient==0){
                                //  找最左边的点
                                p1 = p2;
                            }
                        }
                    }

                }
                res = p1;
            }else if (list_right_Semi.size() != 0){
                //  low_semi_left_right
                List<Point> On_DS = new ArrayList<>();
                Point p1 = list_right_Semi.get(0);
                for (Point p_i:list_right_Semi) {
                    int cb_4 = GarbledCricuit.runGarbledCircuit(Server_r, bgn.decrypt_G1(Compute_ADD_R(bgn, ComputeUtils.compute_ENCS_SP_VO(bgn, start, p_i, end), Server_r)).intValue());
                    if (cb_4 == 1){
                        On_DS.add(p_i);
                    }
                }

                if (On_DS.size() !=0 ){
                    //  on L1 shortest
                    p1 = On_DS.get(0);
                    for (int i = 1; i < On_DS.size(); i++) {
                        Point p2 = On_DS.get(i);
                        int cb_4 = GarbledCricuit.runGarbledCircuit(Server_r, bgn.decrypt_G1(Compute_ADD_R(bgn,ComputeUtils.Compute_ENum_P1P2_DS(bgn,start,end,p1,p2),Server_r)).intValue());
                        if (cb_4 == 0){
                            p1 = p2;
                        }
                    }


                }else {
                    //  common
                    for (int i = 1; i < list_right_Semi.size(); i++) {
                        Point p2 = list_right_Semi.get(i);
                        int orient = Orient(bgn, Server_r, p2, p1, B1);
                        if (orient==1){
                            //  找最左边的点
                            p1 = p2;
                        }
                    }
                }
                res = p1;
            }


        }else {
            //  B1 = B2 00,11
            if (list_right_Semi.size() != 0){
                List<Point> On_L1 = new ArrayList<>();
                //  low_semi_right_right
                Point p1 = list_right_Semi.get(0);
                for (Point p_i:list_right_Semi) {
                    int cb_4 = GarbledCricuit.runGarbledCircuit(Server_r, bgn.decrypt_G1(Compute_ADD_R(bgn, ComputeUtils.Compute_ENCS_DS_SP(bgn, start, p_i, end), Server_r)).intValue());
                    if (cb_4 == 1){
                        On_L1.add(p_i);
                    }
                }

                if (On_L1.size() !=0 ){
                    //  on L1
                    p1 = On_L1.get(0);
                    for (int i = 1; i < On_L1.size(); i++) {
                        Point p2 = On_L1.get(i);
                        int cb_4 = GarbledCricuit.runGarbledCircuit(Server_r, bgn.decrypt_G1(Compute_ADD_R(bgn,ComputeUtils.Compute_ENum_P1P2_OV(bgn,start,end,p1,p2),Server_r)).intValue());
                        if (cb_4 == 0){
                            p1 = p2;
                        }
                    }


                }else {
                    //  common
                    for (int i = 1; i < list_right_Semi.size(); i++) {
                        Point p2 = list_right_Semi.get(i);
                        int orient = Orient(bgn, Server_r, p2, p1, B1);
                        if (orient==0){
                            //  找最右边的点
                            p1 = p2;
                        }
                    }
                }
                res = p1;
            }else if(list_left_Semi.size() !=0){
                List<Point> On_B1S = new ArrayList<>();
                //  low_semi_right_left
                Point p1 = list_left_Semi.get(0);
                for (Point p_i:list_left_Semi) {
                    //  Encs_B1Sov_SP
//                    int cb_4 = GarbledCricuit.runGarbledCircuit(Server_r, bgn.decrypt_G1(Compute_ADD_R(bgn, ComputeUtils.Compute_ENCS_DS_SP(bgn, start, p_i, B1), Server_r)).intValue());
                    int cb_4 = GarbledCricuit.runGarbledCircuit(Server_r, bgn.decrypt_G1(Compute_ADD_R(bgn, ComputeUtils.compute_ENCS_SP_OV_B1S(bgn, start, p_i, B1), Server_r)).intValue());
                    if (cb_4 == 1){
                        On_B1S.add(p_i);
                    }
                }

                if (On_B1S.size() !=0 ){
                    //  on B1S
                    p1 = On_B1S.get(0);
                    for (int i = 1; i < On_B1S.size(); i++) {
                        Point p2 = On_B1S.get(i);
                        int cb_4 = GarbledCricuit.runGarbledCircuit(Server_r, bgn.decrypt_G1(Compute_ADD_R(bgn,ComputeUtils.Compute_ENum_P1P2_DS(bgn,start,B1,p1,p2),Server_r)).intValue());
                        if (cb_4 == 0){
                            p1 = p2;
                        }
                    }


                }else {
                    //  common
                    for (int i = 1; i < list_left_Semi.size(); i++) {
                        Point p2 = list_left_Semi.get(i);
                        int orient = Orient(bgn, Server_r, p2, p1, B1);
                        if (orient==0){
                            //  找最右边的点
                            p1 = p2;
                        }
                    }
                }
                res = p1;
            }
        }

        return res;
    }
}
