package PathQuery;

import Ciper.BGN;
import myGraph.Point;

import java.util.ArrayList;
import java.util.List;

import static PathQuery.ComputeUtils.Compute_ADD_R;
import static PathQuery.StrategySecureOrient.Orient;

/**
 * @Author UbiP Lab Laptop 02
 * @Date 2022/8/28 11:00
 * @Version 1.0
 */
public class StrategySecurePulling {

        public static Point Pull(BGN bgn, int Server_r, List<Point> list_high,Point start, Point temp_start, Point B1, Point B2, List<Point> Res){

            if (list_high.size()==0){
                return B2;
            }
            Point p1 = list_high.get(0);







            if (B1.isSame(B2)){
                Point p = Res.get(Res.size()-1);
                int orient = Orient(bgn, Server_r, p, start, B2);
                if (orient==1){
                    for (int i = 1; i < list_high.size(); i++) {
                        if (Orient(bgn,Server_r,list_high.get(i),p1,B2) == 1){
                            p1 = list_high.get(i);
                        }
                    }
                }else if (orient == 0){
                    for (int i = 1; i < list_high.size(); i++) {
                        if (Orient(bgn,Server_r,list_high.get(i),p1,B2) == 0){
                            p1 = list_high.get(i);
                        }
                    }
                }
            }else {
                int orient = Orient(bgn, Server_r, temp_start, start, B2);
                if (orient==1){
                    for (int i = 1; i < list_high.size(); i++) {
                        if (Orient(bgn,Server_r,list_high.get(i),p1,B2) == 0){
                            p1 = list_high.get(i);
                        }
                    }
                }else if (orient == 0){
                    for (int i = 1; i < list_high.size(); i++) {
                        if (Orient(bgn,Server_r,list_high.get(i),p1,B2) == 1){
                            p1 = list_high.get(i);
                        }
                    }
                }
            }
            return p1;
        }

    public static Point Pull(BGN bgn, int Server_r,  Point start, Point current_start, Point B2, Point B1, List<Point> list_left_Semi, List<Point> list_right_Semi, int Side){

        Point res = null;

        if (Side == -1){
            //  Side = -1
            if (list_right_Semi.size() != 0) {
                List<Point> On_L2 = new ArrayList<>();
                Point p1 = list_right_Semi.get(0);
                for (Point p_i : list_right_Semi) {
                    int cb_4 = GarbledCricuit.runGarbledCircuit(Server_r, bgn.decrypt_G1(Compute_ADD_R(bgn, ComputeUtils.Compute_ENCS_SD_SP(bgn, start, p_i, B2), Server_r)).intValue());
                    if (cb_4 == 1) {
                        On_L2.add(p_i);
                    }
                }

                if (On_L2.size() != 0) {
                    //  On L2 right most
                    p1 = On_L2.get(0);
                    for (int i = 1; i < On_L2.size(); i++) {
                        Point p2 = On_L2.get(i);
                        int cb_4 = GarbledCricuit.runGarbledCircuit(Server_r, bgn.decrypt_G1(Compute_ADD_R(bgn, ComputeUtils.Compute_ENum_P1P2_OV(bgn, start, B2, p1, p2), Server_r)).intValue());
                        if (cb_4 == 1) {
                            p1 = p2;
                        }
                    }

                } else {
                    for (int i = 1; i < list_right_Semi.size(); i++) {
                        Point p2 = list_right_Semi.get(i);
                        int orient = Orient(bgn, Server_r, p2, p1, B1);
                        if (orient == 1) {
                            //  找最右边的点
                            p1 = p2;
                        }
                    }
                }
                res = p1;
            } else if (list_left_Semi.size() != 0) {
                List<Point> On_B2CS = new ArrayList<>();
                Point p1 = list_left_Semi.get(0);
                for (Point p_i : list_left_Semi) {
                    int cb_4 = GarbledCricuit.runGarbledCircuit(Server_r, bgn.decrypt_G1(Compute_ADD_R(bgn, ComputeUtils.compute_ENCS_DP_OV_B2CS(bgn, current_start, p_i, B2), Server_r)).intValue());
                    if (cb_4 == 1) {
                        On_B2CS.add(p_i);
                    }
                }
                if (On_B2CS.size() != 0) {
                    p1 = On_B2CS.get(0);
                    //  On_B2CS 最远点
                    for (int i = 1; i < On_B2CS.size(); i++) {
                        Point p2 = On_B2CS.get(i);
                        int cb_4 = GarbledCricuit.runGarbledCircuit(Server_r, bgn.decrypt_G1(Compute_ADD_R(bgn, ComputeUtils.Compute_ENum_P1P2_DS(bgn, start, B2, p1, p2), Server_r)).intValue());
                        if (cb_4 == 1) {
                            p1 = p2;
                        }
                    }

                } else {
                    for (int i = 1; i < list_left_Semi.size(); i++) {
                        Point p2 = list_left_Semi.get(i);
                        int orient = Orient(bgn, Server_r, p2, p1, B1);
                        if (orient == 0) {
                            //  找最右边的点
                            p1 = p2;
                        }
                    }
                }
                res = p1;
            }
            return res;
        }

        if (B1.isSame(B2)){
            //  11
            if (Side == 1) {
                if (list_right_Semi.size() != 0) {

                    List<Point> On_L2 = new ArrayList<>();
                    Point p1 = list_right_Semi.get(0);
                    for (Point p_i : list_right_Semi) {
                        int cb_4 = GarbledCricuit.runGarbledCircuit(Server_r, bgn.decrypt_G1(Compute_ADD_R(bgn, ComputeUtils.Compute_ENCS_SD_SP(bgn, start, p_i, B2), Server_r)).intValue());
                        if (cb_4 == 1) {
                            On_L2.add(p_i);
                        }
                    }

                    if (On_L2.size() != 0) {
                        //  On L2 right most
                        p1 = On_L2.get(0);
                        for (int i = 1; i < On_L2.size(); i++) {
                            Point p2 = On_L2.get(i);
                            int cb_4 = GarbledCricuit.runGarbledCircuit(Server_r, bgn.decrypt_G1(Compute_ADD_R(bgn, ComputeUtils.Compute_ENum_P1P2_OV(bgn, start, B2, p1, p2), Server_r)).intValue());
                            if (cb_4 == 1) {
                                p1 = p2;
                            }
                        }

                    } else {
                        for (int i = 1; i < list_right_Semi.size(); i++) {
                            Point p2 = list_right_Semi.get(i);
                            int orient = Orient(bgn, Server_r, p2, p1, B1);
                            if (orient == 1) {
                                //  找最右边的点
                                p1 = p2;
                            }
                        }
                    }
                    res = p1;
                } else if (list_left_Semi.size() != 0) {
                    List<Point> On_B2CS = new ArrayList<>();
                    Point p1 = list_left_Semi.get(0);
                    for (Point p_i : list_left_Semi) {
                        int cb_4 = GarbledCricuit.runGarbledCircuit(Server_r, bgn.decrypt_G1(Compute_ADD_R(bgn, ComputeUtils.compute_ENCS_DP_OV_B2CS(bgn, current_start, p_i, B2), Server_r)).intValue());
                        if (cb_4 == 1) {
                            On_B2CS.add(p_i);
                        }
                    }
                    if (On_B2CS.size() != 0) {
                        p1 = On_B2CS.get(0);
                        //  On_B2CS 最远点
                        for (int i = 1; i < On_B2CS.size(); i++) {
                            Point p2 = On_B2CS.get(i);
                            int cb_4 = GarbledCricuit.runGarbledCircuit(Server_r, bgn.decrypt_G1(Compute_ADD_R(bgn, ComputeUtils.Compute_ENum_P1P2_DS(bgn, start, B2, p1, p2), Server_r)).intValue());
                            if (cb_4 == 1) {
                                p1 = p2;
                            }
                        }

                    } else {
                        for (int i = 1; i < list_left_Semi.size(); i++) {
                            Point p2 = list_left_Semi.get(i);
                            int orient = Orient(bgn, Server_r, p2, p1, B1);
                            if (orient == 0) {
                                //  找最右边的点
                                p1 = p2;
                            }
                        }
                    }
                    res = p1;
                }

//            } else if (Side == 0) {
            } else {
                if (list_left_Semi.size() != 0) {
                    List<Point> On_B2CS = new ArrayList<>();
                    Point p1 = list_left_Semi.get(0);
                    for (Point p_i : list_left_Semi) {
                        int cb_4 = GarbledCricuit.runGarbledCircuit(Server_r, bgn.decrypt_G1(Compute_ADD_R(bgn, ComputeUtils.compute_ENCS_DP_OV_B2CS(bgn, current_start, p_i, B2), Server_r)).intValue());
                        if (cb_4 == 1) {
                            On_B2CS.add(p_i);
                        }
                    }

                    if (On_B2CS.size() != 0) {
                        //  On_B2CS 最远点
                        p1 = On_B2CS.get(0);
                        for (int i = 1; i < On_B2CS.size(); i++) {
                            Point p2 = On_B2CS.get(i);
                            int cb_4 = GarbledCricuit.runGarbledCircuit(Server_r, bgn.decrypt_G1(Compute_ADD_R(bgn, ComputeUtils.Compute_ENum_P1P2_DS(bgn, start, B2, p1, p2), Server_r)).intValue());
                            if (cb_4 == 1) {
                                p1 = p2;
                            }
                        }


                    } else {
                        //  On_L2
                        List<Point> On_L2 = new ArrayList<>();
                        for (Point p_i : list_right_Semi) {
                            int cb_4 = GarbledCricuit.runGarbledCircuit(Server_r, bgn.decrypt_G1(Compute_ADD_R(bgn, ComputeUtils.Compute_ENCS_DS_DP(bgn, start, p_i, B2), Server_r)).intValue());
                            if (cb_4 == 1) {
                                On_L2.add(p_i);
                            }
                        }

                        if (On_L2.size() != 0) {
                            //  left most
                            p1 = On_L2.get(0);
                            for (int i = 1; i < On_L2.size(); i++) {
                                Point p2 = On_L2.get(i);
                                int cb_4 = GarbledCricuit.runGarbledCircuit(Server_r, bgn.decrypt_G1(Compute_ADD_R(bgn, ComputeUtils.Compute_ENum_P1P2_OV(bgn, start, B2, p1, p2), Server_r)).intValue());
                                if (cb_4 == 0) {
                                    p1 = p2;
                                }
                            }


                        }else {
                            //  common
                            for (int i = 1; i < list_left_Semi.size(); i++) {
                                Point p2 = list_left_Semi.get(i);
                                int orient = Orient(bgn, Server_r, p2, p1, B1);
                                if (orient == 1) {
                                    //  找最左边的点
                                    p1 = p2;
                                }
                            }
                        }
                    }
                    res = p1;
                }else if (list_right_Semi.size() != 0) {
                    Point p1 = list_right_Semi.get(0);
                    List<Point> On_SD = new ArrayList<>();
                    for (Point p_i : list_right_Semi) {
                        int cb_4 = GarbledCricuit.runGarbledCircuit(Server_r, bgn.decrypt_G1(Compute_ADD_R(bgn, ComputeUtils.compute_ENCS_DP_VO(bgn, start, p_i, B2), Server_r)).intValue());
                        if (cb_4 == 1) {
                            On_SD.add(p_i);
                        }
                    }

                    if (On_SD.size() != 0) {
                        //  On_SD 最远
                        p1 = On_SD.get(0);
                        for (int i = 1; i < On_SD.size(); i++) {
                            Point p2 = On_SD.get(i);
                            int cb_4 = GarbledCricuit.runGarbledCircuit(Server_r, bgn.decrypt_G1(Compute_ADD_R(bgn, ComputeUtils.Compute_ENum_P1P2_VO(bgn, start, B2, p1, p2), Server_r)).intValue());
                            if (cb_4 == 1) {
                                p1 = p2;
                            }
                        }


                    } else {
                        for (int i = 1; i < list_right_Semi.size(); i++) {
                            Point p2 = list_right_Semi.get(i);
                            int orient = Orient(bgn, Server_r, p2, p1, B1);
                            if (orient == 1) {
                                //  找最左边的点
                                p1 = p2;
                            }
                        }
                    }
                    res = p1;
                }
            }



        }else {
            //  B1 != B2
            if (Side == 1) {
                if (list_right_Semi.size() != 0) {

                    List<Point> On_L2 = new ArrayList<>();
                    Point p1 = list_right_Semi.get(0);
                    for (Point p_i : list_right_Semi) {
                        int cb_4 = GarbledCricuit.runGarbledCircuit(Server_r, bgn.decrypt_G1(Compute_ADD_R(bgn, ComputeUtils.Compute_ENCS_SD_SP(bgn, start, p_i, B2), Server_r)).intValue());
                        if (cb_4 == 1) {
                            On_L2.add(p_i);
                        }
                    }

                    if (On_L2.size() != 0) {
                        //  On L2 right most
                        p1 = On_L2.get(0);
                        for (int i = 1; i < On_L2.size(); i++) {
                            Point p2 = On_L2.get(i);
                            int cb_4 = GarbledCricuit.runGarbledCircuit(Server_r, bgn.decrypt_G1(Compute_ADD_R(bgn, ComputeUtils.Compute_ENum_P1P2_OV(bgn, start, B2, p1, p2), Server_r)).intValue());
                            if (cb_4 == 1) {
                                p1 = p2;
                            }
                        }

                    } else {
                        for (int i = 1; i < list_right_Semi.size(); i++) {
                            Point p2 = list_right_Semi.get(i);
                            int orient = Orient(bgn, Server_r, p2, p1, B1);
                            if (orient == 1) {
                                //  找最右边的点
                                p1 = p2;
                            }
                        }
                    }
                    res = p1;
                } else if (list_left_Semi.size() != 0) {
                    List<Point> On_B2CS = new ArrayList<>();
                    Point p1 = list_left_Semi.get(0);
                    for (Point p_i : list_left_Semi) {
                        int cb_4 = GarbledCricuit.runGarbledCircuit(Server_r, bgn.decrypt_G1(Compute_ADD_R(bgn, ComputeUtils.compute_ENCS_DP_OV_B2CS(bgn, current_start, p_i, B2), Server_r)).intValue());
                        if (cb_4 == 1) {
                            On_B2CS.add(p_i);
                        }
                    }
                    if (On_B2CS.size() != 0) {
                        p1 = On_B2CS.get(0);
                        //  On_B2CS 最远点
                        for (int i = 1; i < On_B2CS.size(); i++) {
                            Point p2 = On_B2CS.get(i);
                            int cb_4 = GarbledCricuit.runGarbledCircuit(Server_r, bgn.decrypt_G1(Compute_ADD_R(bgn, ComputeUtils.Compute_ENum_P1P2_DS(bgn, start, B2, p1, p2), Server_r)).intValue());
                            if (cb_4 == 1) {
                                p1 = p2;
                            }
                        }

                    } else {
                        for (int i = 1; i < list_left_Semi.size(); i++) {
                            Point p2 = list_left_Semi.get(i);
                            int orient = Orient(bgn, Server_r, p2, p1, B1);
                            if (orient == 0) {
                                //  找最右边的点
                                p1 = p2;
                            }
                        }
                    }
                    res = p1;
                }

            } else if (Side == 0) {

                if (list_left_Semi.size() != 0) {
                    List<Point> On_B2CS = new ArrayList<>();
                    Point p1 = list_left_Semi.get(0);
                    for (Point p_i : list_left_Semi) {
                        int cb_4 = GarbledCricuit.runGarbledCircuit(Server_r, bgn.decrypt_G1(Compute_ADD_R(bgn, ComputeUtils.compute_ENCS_DP_OV_B2CS(bgn, current_start, p_i, B2), Server_r)).intValue());
                        if (cb_4 == 1) {
                            On_B2CS.add(p_i);
                        }
                    }

                    if (On_B2CS.size() != 0) {
                        //  On_B2CS 最远点
                        p1 = On_B2CS.get(0);
                        for (int i = 1; i < On_B2CS.size(); i++) {
                            Point p2 = On_B2CS.get(i);
                            int cb_4 = GarbledCricuit.runGarbledCircuit(Server_r, bgn.decrypt_G1(Compute_ADD_R(bgn, ComputeUtils.Compute_ENum_P1P2_DS(bgn, start, B2, p1, p2), Server_r)).intValue());
                            if (cb_4 == 1) {
                                p1 = p2;
                            }
                        }


                    } else {
                        //  On_L2
                        List<Point> On_L2 = new ArrayList<>();
                        for (Point p_i : list_right_Semi) {
                            int cb_4 = GarbledCricuit.runGarbledCircuit(Server_r, bgn.decrypt_G1(Compute_ADD_R(bgn, ComputeUtils.Compute_ENCS_DS_DP(bgn, start, p_i, B2), Server_r)).intValue());
                            if (cb_4 == 1) {
                                On_L2.add(p_i);
                            }
                        }

                        if (On_L2.size() != 0) {
                            //  left most
                            p1 = On_L2.get(0);
                            for (int i = 1; i < On_L2.size(); i++) {
                                Point p2 = On_L2.get(i);
                                int cb_4 = GarbledCricuit.runGarbledCircuit(Server_r, bgn.decrypt_G1(Compute_ADD_R(bgn, ComputeUtils.Compute_ENum_P1P2_OV(bgn, start, B2, p1, p2), Server_r)).intValue());
                                if (cb_4 == 0) {
                                    p1 = p2;
                                }
                            }


                        }else {
                            //  common
                            for (int i = 1; i < list_left_Semi.size(); i++) {
                                Point p2 = list_left_Semi.get(i);
                                int orient = Orient(bgn, Server_r, p2, p1, B1);
                                if (orient == 1) {
                                    //  找最左边的点
                                    p1 = p2;
                                }
                            }
                        }
                    }
                    res = p1;
                }else if (list_right_Semi.size() != 0) {
                    Point p1 = list_right_Semi.get(0);
                    List<Point> On_SD = new ArrayList<>();
                    for (Point p_i : list_right_Semi) {
                        int cb_4 = GarbledCricuit.runGarbledCircuit(Server_r, bgn.decrypt_G1(Compute_ADD_R(bgn, ComputeUtils.compute_ENCS_DP_VO(bgn, start, p_i, B2), Server_r)).intValue());
                        if (cb_4 == 1) {
                            On_SD.add(p_i);
                        }
                    }

                    if (On_SD.size() != 0) {
                        //  On_SD 最远
                        p1 = On_SD.get(0);
                        for (int i = 1; i < On_SD.size(); i++) {
                            Point p2 = On_SD.get(i);
                            int cb_4 = GarbledCricuit.runGarbledCircuit(Server_r, bgn.decrypt_G1(Compute_ADD_R(bgn, ComputeUtils.Compute_ENum_P1P2_VO(bgn, start, B2, p1, p2), Server_r)).intValue());
                            if (cb_4 == 1) {
                                p1 = p2;
                            }
                        }


                    } else {
                        for (int i = 1; i < list_right_Semi.size(); i++) {
                            Point p2 = list_right_Semi.get(i);
                            int orient = Orient(bgn, Server_r, p2, p1, B1);
                            if (orient == 1) {
                                //  找最左边的点
                                p1 = p2;
                            }
                        }
                    }
                    res = p1;
                }


            } else {
                //  Side = -1
                if (list_right_Semi.size() != 0) {

                    List<Point> On_L2 = new ArrayList<>();
                    Point p1 = list_right_Semi.get(0);
                    for (Point p_i : list_right_Semi) {
                        int cb_4 = GarbledCricuit.runGarbledCircuit(Server_r, bgn.decrypt_G1(Compute_ADD_R(bgn, ComputeUtils.Compute_ENCS_SD_SP(bgn, start, p_i, B2), Server_r)).intValue());
                        if (cb_4 == 1) {
                            On_L2.add(p_i);
                        }
                    }

                    if (On_L2.size() != 0) {
                        //  On L2 right most
                        p1 = On_L2.get(0);
                        for (int i = 1; i < On_L2.size(); i++) {
                            Point p2 = On_L2.get(i);
                            int cb_4 = GarbledCricuit.runGarbledCircuit(Server_r, bgn.decrypt_G1(Compute_ADD_R(bgn, ComputeUtils.Compute_ENum_P1P2_OV(bgn, start, B2, p1, p2), Server_r)).intValue());
                            if (cb_4 == 1) {
                                p1 = p2;
                            }
                        }

                    } else {
                        for (int i = 1; i < list_right_Semi.size(); i++) {
                            Point p2 = list_right_Semi.get(i);
                            int orient = Orient(bgn, Server_r, p2, p1, B1);
                            if (orient == 1) {
                                //  找最右边的点
                                p1 = p2;
                            }
                        }
                    }
                    res = p1;
                } else if (list_left_Semi.size() != 0) {
                    List<Point> On_B2CS = new ArrayList<>();
                    Point p1 = list_left_Semi.get(0);
                    for (Point p_i : list_left_Semi) {
                        int cb_4 = GarbledCricuit.runGarbledCircuit(Server_r, bgn.decrypt_G1(Compute_ADD_R(bgn, ComputeUtils.compute_ENCS_DP_OV_B2CS(bgn, current_start, p_i, B2), Server_r)).intValue());
                        if (cb_4 == 1) {
                            On_B2CS.add(p_i);
                        }
                    }
                    if (On_B2CS.size() != 0) {
                        p1 = On_B2CS.get(0);
                        //  On_B2CS 最远点
                        for (int i = 1; i < On_B2CS.size(); i++) {
                            Point p2 = On_B2CS.get(i);
                            int cb_4 = GarbledCricuit.runGarbledCircuit(Server_r, bgn.decrypt_G1(Compute_ADD_R(bgn, ComputeUtils.Compute_ENum_P1P2_DS(bgn, start, B2, p1, p2), Server_r)).intValue());
                            if (cb_4 == 1) {
                                p1 = p2;
                            }
                        }

                    } else {
                        for (int i = 1; i < list_left_Semi.size(); i++) {
                            Point p2 = list_left_Semi.get(i);
                            int orient = Orient(bgn, Server_r, p2, p1, B1);
                            if (orient == 0) {
                                //  找最右边的点
                                p1 = p2;
                            }
                        }
                    }
                    res = p1;
                }
            }
        }

        return res == null? B2:res;
    }
}
