package Baselines;

import Ciper.BGN;
import PathQuery.GarbledCricuit;
import it.unisa.dia.gas.jpbc.Element;
import myGraph.Point;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


/**
 * @Author UbiP Lab Laptop 02
 * @Date 2022/10/5 18:57
 * @Version 1.0
 */
public class AlwaysNearest {

    public void Secure_SN_AN(BGN bgn, Point start, Point end, Point[] points, List<Point> Result, List<Point> ALL) throws Exception {

//        Point[] points = retrieve(bgn, DX, Arr, randomNumber_vi, K_vi_star, q1, q2);
//        Point start = points[0];
//        Point end = points[1];
//        Result.add(start);

        List<Point> pointList = new ArrayList<>();
        for (Point p:points) {
            pointList.add(p);
        }

        int Server_r_SVNH = 10000;
        Point currectStart = start;
        while (!pointList.isEmpty()){
            currectStart = getPoint_AN(bgn,Server_r_SVNH,currectStart,end,pointList);
            Result.add(currectStart);
            pointList.remove(currectStart);
        }


//        System.out.println("-----------------");
    }

    public Point getPoint_AN(BGN bgn, int Server_r_SVNH, Point start, Point end, List<Point> list){

        if (list.size()==0){
            return end;
        }
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
        Element[] choose = {EP1CS_X2,EP1CS_Y2};
        Element E_R =  bgn.encrypt_G1(BigInteger.valueOf(Server_r_SVNH));
        for (int i = 1; i < list.size(); i++) {
            Point p2 = list.get(i);
//            Element Num_P1P2_SD_S_R = Compute_ADD_R(bgn,ComputeUtils.Compute_ENum_P1P2_SD(bgn,ESD_X,ESD_Y,p1,p2),Server_r_SVNH);
//            Element Num_P1P2_SD_S_R = compute_ENum_p1p2_CS(bgn,ESD_X,ESD_Y,p1,p2,E_R);
//            Element Num_P1P2_SD_S_R = compute_ENum_p1p2_CS(bgn,p1,p2,E_R);
//            cb_5 = GarbledCricuit.runGarbledCircuit(Server_r_SVNH,bgn.decrypt_G1(Num_P1P2_SD_S_R).intValue());
            cb_5 = compute_AN_p1p2_CS(bgn,Server_r_SVNH,choose,p2,CurrentStart,E_R);
            if (cb_5==0){
                p1 = p2;
            }
        }

        return p1;
    }

    public int compute_AN_p1p2_CS(BGN bgn,int Server_r_SVNH,Element[] choose,Point p2,Point CurrentStart,Element E_R){

        Element EP2CS_X = bgn.sub(p2.C_X,CurrentStart.C_X);
        Element EP2CS_Y = bgn.sub(p2.C_Y,CurrentStart.C_Y);
        Element EP2CS_X2 = bgn.Mul(EP2CS_X,EP2CS_X);
        Element EP2CS_Y2 = bgn.Mul(EP2CS_Y,EP2CS_Y);

        Element EP1P2CS = bgn.sub_G1(bgn.add_G1(choose[0],choose[1]),bgn.add_G1(EP2CS_X2,EP2CS_Y2));
        Element Add_R = bgn.add_G1(EP1P2CS,E_R);
        int value = GarbledCricuit.runGarbledCircuit(Server_r_SVNH, bgn.decrypt_G1(Add_R).intValue());
        if (value == 0){
            choose[0] = EP2CS_X2;
            choose[1] = EP2CS_Y2;
        }
        return value;
    }


}
