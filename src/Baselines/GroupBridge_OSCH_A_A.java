package Baselines;

import Ciper.BGN;
import PathQuery.*;
import it.unisa.dia.gas.jpbc.Element;
import myGraph.Point;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author DELL
 * @Date 2022/11/2 19:31
 * @Version 1.0
 */
public class GroupBridge_OSCH_A_A {

    public void Secure_SN_GB_OSCH_A_A(BGN bgn, Point start, Point end, Point[] points, List<Point> Result, List<Point> ALL) throws Exception {


        //  group
        StrategySecureGroup secureGroup = new StrategySecureGroup();
        int Server_r_group =10000;
        secureGroup.groupPoint(bgn,Server_r_group,points,start,end);

        List<Point> list_low = StrategySecureGroup.getEachArea_without_sd(points,-1);
        List<Point> list_mid = StrategySecureGroup.getEachArea_without_sd(points,0);
        List<Point> list_high = StrategySecureGroup.getEachArea_without_sd(points,1);

        StrategySecureBridging secureBridging = new StrategySecureBridging();
        Point B1 = secureBridging.choosePoint_B1(bgn, Server_r_group, list_mid, list_high, start, end);
        Point B2 = secureBridging.choosePoint_B2(end);
//        System.out.println("------------");
//        System.out.println(level++);
//        System.out.println("List_low:");
//        for (Point p:list_low) {
//            System.out.print(p.id+",");
//        }
//        System.out.println();

//        System.out.println("List_mid:");
//        for (Point p:list_mid) {
//            System.out.print(p.id+",");
//        }
//        System.out.println();
//
//        System.out.println("List_high:");
//        for (Point p:list_high) {
//            System.out.print(p.id+",");
//        }
//        System.out.println();
//        System.out.println("------------");
//

        int Server_r = 10000;
        Point currentStart = start;
        if (!list_low.isEmpty()){
            //  分左右
            List<Point> list_left = new ArrayList<>();
            List<Point> list_right = new ArrayList<>();
//            Point p = list_low.get(0);

            //  orient
            StrategySecureOrient.Orient(bgn, Server_r, list_low, start, end);
            for (Point p_i : list_low) {
                if (p_i.side == 1) {
                    list_left.add(p_i);
                } else if (p_i.side == 0) {
                    list_right.add(p_i);
                }
            }
//            System.out.println("List_low_left:");
//            for (Point p:list_left) {
//                System.out.print(p.id+",");
//            }
//            System.out.println();
//
//            System.out.println("List_low_right:");
//            for (Point p:list_right) {
//                System.out.print(p.id+",");
//            }
//            System.out.println();

            //  B1 side
            int orient_B1 = StrategySecureOrient.Orient(bgn, Server_r, B1, start, end);
//            System.out.println("orient_B1:" + orient_B1);
            List<Point> list_right_Semi = new ArrayList<>();
            List<Point> list_left_Semi = new ArrayList<>();

            if (list_low.size() != 0) {
                Point scan = null;
                if (orient_B1 == 1) {
                    if (list_right.size() != 0) {
                        if (StrategySecureSplitting.Split(bgn, Server_r, list_right, currentStart, B1, list_left_Semi, list_right_Semi)) {
                            scan = list_right.get(0);
                        }else {
//                        System.out.println("List_low_right_left:");
//                        for (Point p:list_left_Semi) {
//                            System.out.print(p.id+",");
//                        }
//                        System.out.println();
//
//                        System.out.println("List_low_right_right:");
//                        for (Point p:list_right_Semi) {
//                            System.out.print(p.id+",");
//                        }
//                        System.out.println();

                            scan = StrategySecureScanning.Scan(bgn, Server_r, currentStart, end, B1, list_left_Semi, list_right_Semi, orient_B1);
                        }
//                        System.out.println("scan "+scan.id);

                    } else {
                        if (StrategySecureSplitting.Split(bgn, Server_r, list_left, currentStart, B1, list_left_Semi, list_right_Semi)) {
                            scan = list_left.get(0);
                        } else{
                            scan = StrategySecureScanning.Scan(bgn, Server_r, currentStart, end, B1, list_left_Semi, list_right_Semi, orient_B1);
                        }
//                        System.out.println("scan "+scan.id);
                    }

                } else {
                    //  orient_B1 == 0
                    if (list_left.size() != 0) {

                        if (StrategySecureSplitting.Split(bgn, Server_r, list_left, currentStart, B1, list_left_Semi, list_right_Semi)) {
                            scan = list_left.get(0);
                        }else {
                            scan = StrategySecureScanning.Scan(bgn, Server_r, currentStart, end, B1, list_left_Semi, list_right_Semi, orient_B1);
                        }
//                        System.out.println("scan "+scan.id);
                    } else {
                        if (StrategySecureSplitting.Split(bgn, Server_r, list_right, currentStart, B1, list_left_Semi, list_right_Semi)) {
                            scan = list_right.get(0);
                        }else {
                            scan = StrategySecureScanning.Scan(bgn, Server_r, currentStart, end, B1, list_left_Semi, list_right_Semi, orient_B1);
                        }
//                        System.out.println("scan "+scan.id);
                    }
                }
//                if (!scan.isSame(list_low.get(list_low.size()-1))) {
                if (list_low.size()>1) {
                    Result.add(scan);
//                    System.out.println("Add: "+scan.id);
                    currentStart = scan;
                    //......
                    list_low.remove(scan);
                    ALL.remove(scan);
                    int size = list_low.size();

                    Point[] next = new Point[size];
                    for (int i = 0; i < size; i++) {
                        next[i] = list_low.get(i);
                    }
                    Secure_SN_GB_OSCH_A_A(bgn, currentStart, B1, next, Result,ALL);

                }else {
                    Result.add(scan);
//                    Result.add(B1);
//                    System.out.println("Add: "+scan.id);
//                    System.out.println(scan.id+" the last one in low");
                    list_low.remove(scan);
                    currentStart = scan;
//                    list_mid.remove(B1);
//                    int size = list_mid.size();
//                    Point[] next = new Point[size];
//                    for (int i = 0; i < size; i++) {
//                        next[i] = list_mid.get(i);
//                    }
//                    ALL.remove(scan);
//                    Secure_SN(bgn,scan,B2,next,Result,ALL);
                }
            }
        }

        if (!list_mid.isEmpty()){

            currentStart = getPoint_GAN(bgn,Server_r,B1,end,list_mid);
            Result.add(currentStart);
            list_mid.remove(currentStart);
            if (!list_mid.isEmpty()) {
                Point[] next = new Point[list_mid.size()];
                for (int i = 0; i < list_mid.size(); i++) {
                    next[i] = list_mid.get(i);
                }
                Secure_SN_GB_OSCH_A_A(bgn, currentStart, end, next, Result, ALL);
            }
        }

        if (!list_high.isEmpty()){
            currentStart = getPoint_GAN(bgn,Server_r,currentStart,end,list_high);
            Result.add(currentStart);
            list_high.remove(currentStart);
            if (!list_high.isEmpty()) {
                Point[] next = new Point[list_high.size()];
                for (int i = 0; i < list_high.size(); i++) {
                    next[i] = list_high.get(i);
                }
                Secure_SN_GB_OSCH_A_A(bgn, currentStart, end, next, Result, ALL);
            }
        }


//        System.out.println("-----------------");
    }


    public Point getPoint_GAN(BGN bgn, int Server_r_SVNH, Point start, Point end, List<Point> list){

//        if (list.size()==0){
//            return end;
//        }
        if (list.size()==1){
            return list.get(0);
        }
        Point p1 = list.get(0);
        int cb_5 = 0;
        Point CurrentStart = start;
        Element EP1CS_X = bgn.sub(p1.C_X,CurrentStart.C_X);
        Element EP1CS_Y = bgn.sub(p1.C_Y,CurrentStart.C_Y);
        Element EP1CS_X2 = bgn.Mul(EP1CS_X,EP1CS_X);
        Element EP1CS_Y2 = bgn.Mul(EP1CS_Y,EP1CS_Y);
        Element E_R =  bgn.encrypt_G1(BigInteger.valueOf(Server_r_SVNH));
        for (int i = 1; i < list.size(); i++) {
            Point p2 = list.get(i);
//            Element Num_P1P2_SD_S_R = Compute_ADD_R(bgn,ComputeUtils.Compute_ENum_P1P2_SD(bgn,ESD_X,ESD_Y,p1,p2),Server_r_SVNH);
//            Element Num_P1P2_SD_S_R = compute_ENum_p1p2_CS(bgn,ESD_X,ESD_Y,p1,p2,E_R);
//            Element Num_P1P2_SD_S_R = compute_ENum_p1p2_CS(bgn,p1,p2,E_R);
//            cb_5 = GarbledCricuit.runGarbledCircuit(Server_r_SVNH,bgn.decrypt_G1(Num_P1P2_SD_S_R).intValue());
            cb_5 = compute_AN_p1p2_CS(bgn,Server_r_SVNH,EP1CS_X2,EP1CS_Y2,p2,CurrentStart,E_R);
            if (cb_5==0){
                p1 = p2;
            }
        }

        return p1;
    }

    public int compute_AN_p1p2_CS(BGN bgn, int Server_r_SVNH, Element EP1CS_X2, Element EP1CS_Y2, Point p2, Point CurrentStart, Element E_R){

        Element EP2CS_X = bgn.sub(p2.C_X,CurrentStart.C_X);
        Element EP2CS_Y = bgn.sub(p2.C_Y,CurrentStart.C_Y);
        Element EP2CS_X2 = bgn.Mul(EP2CS_X,EP2CS_X);
        Element EP2CS_Y2 = bgn.Mul(EP2CS_Y,EP2CS_Y);

        Element EP1P2CS = bgn.sub_G1(bgn.sub_G1(EP1CS_X2,EP2CS_X2),bgn.sub_G1(EP1CS_Y2,EP2CS_Y2));
        Element Add_R = bgn.add_G1(EP1P2CS,E_R);
        int value = GarbledCricuit.runGarbledCircuit(Server_r_SVNH, bgn.decrypt_G1(Add_R).intValue());
        if (value == 0){
            EP1CS_X2 = EP2CS_X2;
            EP1CS_Y2 = EP2CS_Y2;
        }
        return value;
    }
}
