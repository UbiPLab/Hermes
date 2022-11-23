package PathQuery;

import Ciper.BGN;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.PairingPreProcessing;
import myGraph.Point;

import java.math.BigInteger;

/**
 * @Author UbiP Lab Laptop 02
 * @Date 2022/9/4 8:53
 * @Version 1.0
 */
public class ComputeUtils {


    public static Element Compute_ESD_1(BGN bgn, Point start, Point end){
        return bgn.sub(end.C_X,start.C_X);
    }


    public static Element Compute_ESD_2(BGN bgn, Point start, Point end){
        return bgn.sub(end.C_Y,start.C_Y);
    }


    public static Element Compute_EDS_1(BGN bgn, Point start, Point end){
        return bgn.sub(start.C_X,end.C_X);
    }


    public static Element Compute_EDS_2(BGN bgn, Point start, Point end){
        return bgn.sub(start.C_Y,end.C_Y);
    }


    public static Element Compute_ESP_1(BGN bgn, Point start, Point P){
        return bgn.sub(P.C_X,start.C_X);
    }


    public static Element Compute_ESP_2(BGN bgn, Point start, Point P){
        return bgn.sub(P.C_Y,start.C_Y);
    }


    public static Element Compute_EDP_1(BGN bgn, Point end, Point P){
        return bgn.sub(P.C_X,end.C_X);
    }


    public static Element Compute_EDP_2(BGN bgn, Point end, Point P){
        return bgn.sub(P.C_Y,end.C_Y);
    }

    public static Element Compute_EP1P2_1(BGN bgn, Point p1, Point p2){
        return bgn.sub(p2.C_X,p1.C_X);
    }

    public static Element Compute_EP1P2_2(BGN bgn, Point p1, Point p2){
        return bgn.sub(p2.C_Y,p1.C_Y);
    }

    public static Element Compute_EP2P1_1(BGN bgn, Point p1, Point p2){
        return bgn.sub(p1.C_X,p2.C_X);
    }

    public static Element Compute_EP2P1_2(BGN bgn, Point p1, Point p2){
        return bgn.sub(p1.C_Y,p2.C_Y);
    }

    public static Element compute_EOV_1(BGN bgn, Point start, Point end){
        return bgn.sub(end.C_Y, start.C_Y);
    }

    public static Element compute_EOV_2(BGN bgn, Point start, Point end){
        return bgn.sub(start.C_X, end.C_X);
    }

    public static Element compute_EVO_1(BGN bgn, Point start, Point end){
        return bgn.sub(start.C_Y, end.C_Y);
    }

    public static Element compute_EVO_2(BGN bgn, Point start, Point end){
        return bgn.sub(end.C_X, start.C_X);
    }

    /**
     * left = 1;right =-1
     * @param bgn
     * @param start
     * @param p
     * @param end
     * @return
     */
    public static Element compute_ENCS_SP_OV(BGN bgn, Point start, Point p, Point end){
        Element mul_x = bgn.Mul(compute_EOV_1(bgn,start,end),Compute_ESP_1(bgn,start,p));
        Element mul_y = bgn.Mul(compute_EOV_2(bgn,start,end),Compute_ESP_2(bgn,start,p));
        return bgn.add_G1(mul_x,mul_y);
    }

    public static Element compute_ENCS_SP_OV(BGN bgn, Point start, Point p, PairingPreProcessing pairingPreProcessing_1,PairingPreProcessing pairingPreProcessing_2){
        Element mul_x = bgn.Pre_pairing(Compute_ESP_1(bgn,start,p),pairingPreProcessing_1);
        Element mul_y = bgn.Pre_pairing(Compute_ESP_2(bgn,start,p),pairingPreProcessing_2);
        return bgn.add_G1(mul_x,mul_y);
    }

    public static Element compute_ENCS_SP_OV_B1S(BGN bgn, Point start, Point p, Point B1){
        Element mul_x = bgn.Mul(compute_EVO_1(bgn,start,B1),Compute_ESP_1(bgn,start,p));
        Element mul_y = bgn.Mul(compute_EVO_2(bgn,start,B1),Compute_ESP_2(bgn,start,p));
        return bgn.add_G1(mul_x,mul_y);
    }

    public static Element compute_ENCS_DP_OV_B2CS(BGN bgn, Point CurrentStart, Point p, Point B2){
        Element mul_x = bgn.Mul(compute_EVO_1(bgn,CurrentStart,B2),Compute_ESP_1(bgn,B2,p));
        Element mul_y = bgn.Mul(compute_EVO_2(bgn,CurrentStart,B2),Compute_ESP_2(bgn,B2,p));
        return bgn.add_G1(mul_x,mul_y);

    }

    public static Element compute_ENCS_SP_VO(BGN bgn, Point start, Point p, Point end){
        Element mul_x = bgn.Mul(compute_EVO_1(bgn,start,end),Compute_ESP_1(bgn,start,p));
        Element mul_y = bgn.Mul(compute_EVO_2(bgn,start,end),Compute_ESP_2(bgn,start,p));
        return bgn.add_G1(mul_x,mul_y);

    }

    public static Element compute_ENCS_DP_VO(BGN bgn, Point start, Point p, Point end){
        Element mul_x = bgn.Mul(compute_EVO_1(bgn,start,end),Compute_ESP_1(bgn,end,p));
        Element mul_y = bgn.Mul(compute_EVO_2(bgn,start,end),Compute_ESP_2(bgn,end,p));
        return bgn.add_G1(mul_x,mul_y);

    }



    public static Element Compute_ENCS_SD_SP(BGN bgn,Point start, Point p, Point end){
        //  G1
        Element mul_x = bgn.Mul(Compute_ESP_1(bgn,start,p), Compute_ESD_1(bgn,start,end));
        Element mul_y = bgn.Mul(Compute_ESP_2(bgn,start,p), Compute_ESD_2(bgn,start,end));
        return bgn.add_G1(mul_x,mul_y);
    }

    public static Element Compute_ENCS_SD_SP(BGN bgn, Point start, Point p, PairingPreProcessing pairingPreProcessing_1, PairingPreProcessing pairingPreProcessing_2){
        //  G1
        Element mul_x = bgn.Pre_pairing(Compute_ESP_1(bgn,start,p),pairingPreProcessing_1);

        Element mul_y = bgn.Pre_pairing(Compute_ESP_2(bgn,start,p), pairingPreProcessing_2);
        return bgn.add_G1(mul_x,mul_y);
    }



    public static Element Compute_ENCS_SD_P1P2(BGN bgn,Point start, Point end, Point P1, Point P2){
        //  G1
        Element mul_x = bgn.Mul(Compute_EP1P2_1(bgn,P1,P2), Compute_ESD_1(bgn,start,end));
        Element mul_y = bgn.Mul(Compute_EP1P2_2(bgn,P1,P2), Compute_ESD_2(bgn,start,end));
        return bgn.add_G1(mul_x,mul_y);
    }

    public static Element Compute_ENCS_SD_P2P1(BGN bgn,Point start, Point end, Point P1, Point P2){
        //  G1
        Element mul_x = bgn.Mul(Compute_EP2P1_1(bgn,P1,P2), Compute_ESD_1(bgn,start,end));
        Element mul_y = bgn.Mul(Compute_EP2P1_2(bgn,P1,P2), Compute_ESD_2(bgn,start,end));
        return bgn.add_G1(mul_x,mul_y);
    }

    public static Element Compute_ENCS_DS_DP(BGN bgn,Point start, Point p, Point end){
        //  G1
        Element mul_x = bgn.Mul(Compute_EDP_1(bgn,end,p), Compute_EDS_1(bgn,start,end));
        Element mul_y = bgn.Mul(Compute_EDP_2(bgn,end,p), Compute_EDS_2(bgn,start,end));
        return bgn.add_G1(mul_x,mul_y);
    }

    public static Element Compute_ENCS_DS_DP(BGN bgn, Point end, Point p, PairingPreProcessing preProcessing_1, PairingPreProcessing preProcessing_2){
        //  G1
        Element mul_x = bgn.Pre_pairing(Compute_EDP_1(bgn,end,p), preProcessing_1);
        Element mul_y = bgn.Pre_pairing(Compute_EDP_2(bgn,end,p), preProcessing_2);
        return bgn.add_G1(mul_x,mul_y);
    }

    public static Element Compute_ENCS_DS_SP(BGN bgn,Point start, Point p, Point end){
        //  G1
        Element mul_x = bgn.Mul(Compute_ESP_1(bgn,start,p), Compute_EDS_1(bgn,start,end));
        Element mul_y = bgn.Mul(Compute_ESP_2(bgn,start,p), Compute_EDS_2(bgn,start,end));
        return bgn.add_G1(mul_x,mul_y);
    }




    public static Element Compute_ENum_P1P2_SD(BGN bgn, Point start, Point end, Point p1, Point p2){
        Element mul_1 = bgn.Mul(Compute_EP2P1_1(bgn,p1,p2),Compute_ESD_1(bgn,start,end));
        Element mul_2 = bgn.Mul(Compute_EP2P1_2(bgn,p1,p2),Compute_ESD_2(bgn,start,end));
        return bgn.add_G1(mul_1,mul_2);
    }

    public static Element Compute_ENum_P1P2_DS(BGN bgn, Point start, Point end, Point p1, Point p2){
        Element mul_1 = bgn.Mul(Compute_EP2P1_1(bgn,p1,p2),Compute_EDS_1(bgn,start,end));
        Element mul_2 = bgn.Mul(Compute_EP2P1_2(bgn,p1,p2),Compute_EDS_2(bgn,start,end));
        return bgn.add_G1(mul_1,mul_2);
    }



    public static Element Compute_ENum_P1P2_OV(BGN bgn, Point start, Point end, Point p1, Point p2){
        Element mul_1 = bgn.Mul(Compute_EP2P1_1(bgn,p1,p2),compute_EOV_1(bgn,start,end));
        Element mul_2 = bgn.Mul(Compute_EP2P1_2(bgn,p1,p2),compute_EOV_2(bgn,start,end));
        return bgn.add_G1(mul_1,mul_2);
    }



    public static Element Compute_ENum_P1P2_VO(BGN bgn, Point start, Point end, Point p1, Point p2){
        Element mul_1 = bgn.Mul(Compute_EP2P1_1(bgn,p1,p2),compute_EVO_1(bgn,start,end));
        Element mul_2 = bgn.Mul(Compute_EP2P1_2(bgn,p1,p2),compute_EVO_2(bgn,start,end));
        return bgn.add_G1(mul_1,mul_2);
    }

    /**
     * add randomNumber r
     * @param bgn
     * @param Ciper
     * @param Server_r
     * @return
     */
    public static Element Compute_ADD_R(BGN bgn,Element Ciper, int Server_r){
        Element Ciper_r = bgn.encrypt_G1(BigInteger.valueOf(Server_r));
        return bgn.add_G1(Ciper,Ciper_r);
    }

    public static Element Compute_ADD_R(BGN bgn,Element Ciper, Element Server_r){
        return bgn.add_G1(Ciper,Server_r);
    }
}
