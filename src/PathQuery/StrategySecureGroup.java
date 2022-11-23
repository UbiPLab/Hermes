package PathQuery;

import Ciper.BGN;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.PairingPreProcessing;
import myGraph.Point;

import java.util.ArrayList;
import java.util.List;

import static PathQuery.ComputeUtils.*;
import static PathQuery.GarbledCricuit.runGarbledCircuit;

/**
 * @Author UbiP Lab Laptop 02
 * @Date 2022/8/4 16:01
 * @Version 1.0
 */

public class StrategySecureGroup {

    /**
     * 使用GC判断正负，false>0，true<0
     * @param bgn
     * @param Server_r_group
     * @param start
     * @param p
     * @param
     * @return
     */
    public int runCMP_SDSP(BGN bgn,int Server_r_group,Point start, Point p, PairingPreProcessing preProcessing_1, PairingPreProcessing preProcessing_2){
        Element Cilent_dec_NCS_add_r = Compute_ADD_R(bgn,Compute_ENCS_SD_SP(bgn,start,p,preProcessing_1,preProcessing_2),Server_r_group);
//        Element Cilent_dec_NCS_add_r = Compute_ENC_ADD_R(bgn,Compute_ENCS_SDSP(bgn,start,p,end),Server_r_group);
        //  Client dec

        return runGarbledCircuit(Server_r_group, bgn.decrypt_G1(Cilent_dec_NCS_add_r).intValue());
    }

    /**
     * 使用GC判断正负，false>0，true<0
     * @param bgn
     * @param Server_r_group
     * @param
     * @param p
     * @param end
     * @return
     */
    public int runCMP_DSDP(BGN bgn,int Server_r_group,Point end, Point p, PairingPreProcessing preProcessing_1, PairingPreProcessing preProcessing_2){
        Element Cilent_dec_NCS_add_r = Compute_ADD_R(bgn,Compute_ENCS_DS_DP(bgn,end,p,preProcessing_1,preProcessing_2),Server_r_group);
//        Element Cilent_dec_NCS_add_r = Compute_ENC_ADD_R(bgn,Compute_ENCS_DSDP(bgn,start,p,end),Server_r_group);
        //  Client dec
        return runGarbledCircuit(Server_r_group, bgn.decrypt_G1(Cilent_dec_NCS_add_r).intValue());
    }

    /**
     * 分组，Low=-1，Mid=0，High=1
      * @param point 点集合
     */
    public void groupPoint(BGN bgn, int Server_r_group, Point[] point, Point start, Point end){
        /**
         *      low     mid     high
         *  cb1 1       0       0
         *  cb2 0       0       1
         *  flase=0:Cilent>0;true=1:Cilent<=0
         */
        Element ESD_1 = Compute_ESD_1(bgn,start,end);
        Element ESD_2 = Compute_ESD_2(bgn,start,end);
        PairingPreProcessing pairingPreProcessing_1 = bgn.pairing_Same(ESD_1);
        PairingPreProcessing pairingPreProcessing_2 = bgn.pairing_Same(ESD_2);
        Element EDS_1 = Compute_EDS_1(bgn,start,end);
        Element EDS_2 = Compute_EDS_2(bgn,start,end);
        PairingPreProcessing pairingPreProcessing_1_DS = bgn.pairing_Same(EDS_1);
        PairingPreProcessing pairingPreProcessing_2_DS = bgn.pairing_Same(EDS_2);
        for (Point value : point) {
            int cb1 = runCMP_SDSP(bgn, Server_r_group, start, value, pairingPreProcessing_1,pairingPreProcessing_2);
            int cb2 = runCMP_DSDP(bgn, Server_r_group, end, value, pairingPreProcessing_1_DS,pairingPreProcessing_2_DS);
            /*
                   low     mid     high
               cb1 1       0       0
               cb2 0       0       1
               flase=0:Cilent>0;true=1:Cilent<=0
             */
            if (cb1==1 && cb2==0) {
                value.group_id = -1;
            } else if (cb1==0 && cb2==0) {
                value.group_id = 0;
            } else if (cb1==0 && cb2==1) {
                value.group_id = 1;
            }
        }
    }

    public void groupPoint(BGN bgn, int Server_r_group, Point[] point){
        /**
         *      low     mid     high
         *  cb1 1       0       0
         *  cb2 0       0       1
         *  flase=0:Cilent>0;true=1:Cilent<=0
         */
        Element ESD_1 = Compute_ESD_1(bgn,point[0],point[1]);
        Element ESD_2 = Compute_ESD_2(bgn,point[0],point[1]);
        PairingPreProcessing pairingPreProcessing_1 = bgn.pairing_Same(ESD_1);
        PairingPreProcessing pairingPreProcessing_2 = bgn.pairing_Same(ESD_2);

        Element EDS_1 = Compute_EDS_1(bgn,point[0],point[1]);
        Element EDS_2 = Compute_EDS_2(bgn,point[0],point[1]);
        PairingPreProcessing pairingPreProcessing_1_DS = bgn.pairing_Same(EDS_1);
        PairingPreProcessing pairingPreProcessing_2_DS = bgn.pairing_Same(EDS_2);

        for (int i = 2; i < point.length; i++) {
            Point value = point[i];
            int cb1 = runCMP_SDSP(bgn, Server_r_group, point[0], value, pairingPreProcessing_1,pairingPreProcessing_2);
            int cb2 = runCMP_DSDP(bgn, Server_r_group, point[1], value, pairingPreProcessing_1_DS,pairingPreProcessing_2_DS);
            /*
                   low     mid     high
               cb1 1       0       0
               cb2 0       0       1
               flase=0:Cilent>0;true=1:Cilent<=0
             */
            if (cb1==1 && cb2==0) {
                value.group_id = -1;
            } else if (cb1==0 && cb2==0) {
                value.group_id = 0;
            } else if (cb1==0 && cb2==1) {
                value.group_id = 1;
            }
        }

    }

    /**
     * 根据Point.id = tag 分区域
     * @param groups
     * @param tag
     * @return
     */
    public static List<Point> getEachArea_with_sd(Point[] groups,int tag){
        List<Point> list = new ArrayList<>();
        for (int i=2;i<groups.length;i++) {
            if (groups[i].group_id == tag) {
                list.add(groups[i]);
            }
        }
        return list;
    }

    public static List<Point> getEachArea_without_sd(Point[] groups, int tag){
        List<Point> list = new ArrayList<>();
        for (Point group : groups) {
            if (group.group_id == tag) {
                list.add(group);
            }
        }
        return list;
    }
}
