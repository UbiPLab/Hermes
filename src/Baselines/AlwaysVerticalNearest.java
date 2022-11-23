package Baselines;

import Ciper.BGN;
import PathQuery.ComputeUtils;
import it.unisa.dia.gas.jpbc.Element;
import myGraph.Point;
import PathQuery.GarbledCricuit;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import PathQuery.ComputeUtils.*;


/**
 * @Author UbiP Lab Laptop 02
 * @Date 2022/10/5 19:31
 * @Version 1.0
 */
public class AlwaysVerticalNearest {
    public void Secure_SN_AVN(BGN bgn, Point start, Point end, Point[] points, List<Point> Result, List<Point> ALL) throws Exception {

//        Point[] points = retrieve(bgn, DX, Arr, randomNumber_vi, K_vi_star, q1, q2);
//        Point start = points[0];
//        Point end = points[1];
        Result.add(start);

        List<Point> pointList = new ArrayList<>();
        for (Point p:points) {
            pointList.add(p);
        }

        int Server_r_SVNH = 1000;
        Point currectStart = start;
        while (!pointList.isEmpty()){
//            currectStart = getPoint_SVNH(bgn,Server_r_SVNH,start,end,pointList);
            currectStart = getPoint_SVNH(bgn,Server_r_SVNH,currectStart,end,pointList);
            Result.add(currectStart);
            pointList.remove(currectStart);
        }

        System.out.println("-----------------");
    }

    public Point getPoint_SVNH(BGN bgn, int Server_r_SVNH, Point start, Point end, List<Point> list){

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

        return p1;
    }

    public static Element compute_ENum_p1p2_SD(BGN bgn, Element ESD_X, Element ESD_Y, Point p1, Point p2, Element R){
//        Element C_X_p1_sub_C_X_p2 = bgn.sub(p1.C_X, p2.C_X);
        Element C_X_p1_sub_C_X_p2 = ComputeUtils.Compute_EP2P1_1(bgn,p1,p2);
//        Element C_Y_p1_sub_C_Y_p2 = bgn.sub(p1.C_Y,p2.C_Y);
        Element C_Y_p1_sub_C_Y_p2 = ComputeUtils.Compute_EP2P1_2(bgn,p1,p2);

        Element mul_1 = bgn.Mul(C_X_p1_sub_C_X_p2,ESD_X);
        Element mul_2 = bgn.Mul(C_Y_p1_sub_C_Y_p2,ESD_Y);
        return bgn.add_G1(bgn.add_G1(mul_1,mul_2),R);
    }
}
