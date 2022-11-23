package PathQuery;

import Ciper.BGN;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.PairingPreProcessing;
import myGraph.Point;

import java.util.List;

import static PathQuery.ComputeUtils.*;

/**
 * @Author UbiP Lab Laptop 02
 * @Date 2022/8/26 11:04
 * @Version 1.0
 */
public class StrategySecureOrient {

    /**
     * C_X_s_sub_C_X_d
     * @param bgn
     * @param start
     * @param end
     * @return
     */
//    public static Element compute_EOV_X(BGN bgn, Point start, Point end){
//        return bgn.sub(start.C_X, end.C_X);
//    }

    /**
     * C_Y_s_sub_C_Y_d
     * @param bgn
     * @param start
     * @param end
     * @return
     */
//    public static Element compute_EOV_Y(BGN bgn, Point start, Point end){
//        return bgn.sub(end.C_Y, start.C_Y);
//    }

    /**
     * left = 1;right =-1

     */
//    public static Element compute_ENCS_SP_OV(BGN bgn, int Server_r_low, Point start, Point p, Point end){
//        Element C_X_s_sub_C_X_d = compute_EOV_X(bgn,start,end);
//        Element C_Y_d_sub_C_Y_s = compute_EOV_Y(bgn,start,end);
//        Element C_X_p_sub_C_X_s = StrategySecureGroup.Compute_ESP_X(bgn,start,p);
//        Element C_Y_p_sub_C_Y_s = StrategySecureGroup.Compute_ESP_Y(bgn,start,p);
//        Element R = bgn.encrypt_G1(BigInteger.valueOf(Server_r_low));
//        Element Mul_and_Add_G = bgn.add_G1(bgn.Mul(C_Y_d_sub_C_Y_s,C_X_p_sub_C_X_s),bgn.Mul(C_X_s_sub_C_X_d,C_Y_p_sub_C_Y_s));
//        return bgn.add_G1(Mul_and_Add_G,R);
//    }

    public static void Orient(BGN bgn, int Server_r, List<Point> list, Point temp_start, Point temp_end){
        Element EOV_1 = compute_EOV_1(bgn,temp_start,temp_end);
        Element EOV_2 = compute_EOV_2(bgn,temp_start,temp_end);
        PairingPreProcessing pairingPreProcessing_1 = bgn.pairing_Same(EOV_1);
        PairingPreProcessing pairingPreProcessing_2 = bgn.pairing_Same(EOV_2);

        for (Point point : list) {
            if (point.isSame(temp_start) || point.isSame(temp_end)){
                point.side = -1;
            }else {
//               point.side = (GarbledCricuit.runGarbledCircuit(Server_r, bgn.decrypt_G1(compute_ENCS_SP_OV(bgn, Server_r, temp_start, point, temp_end)).intValue()));
               point.side = (GarbledCricuit.runGarbledCircuit(Server_r, bgn.decrypt_G1(ComputeUtils.Compute_ADD_R(bgn,ComputeUtils.compute_ENCS_SP_OV(bgn,temp_start,point,pairingPreProcessing_1,pairingPreProcessing_2),Server_r)).intValue()));
            }

        }
    }

    public static int Orient(BGN bgn, int Server_r, Point point, Point temp_start, Point temp_end){
        Element EOV_1 = compute_EOV_1(bgn,temp_start,temp_end);
        Element EOV_2 = compute_EOV_2(bgn,temp_start,temp_end);
        PairingPreProcessing pairingPreProcessing_1 = bgn.pairing_Same(EOV_1);
        PairingPreProcessing pairingPreProcessing_2 = bgn.pairing_Same(EOV_2);
            if (point.isSame(temp_start) || point.isSame(temp_end)){
                return  -1;
            }
            return  GarbledCricuit.runGarbledCircuit(Server_r, bgn.decrypt_G1(ComputeUtils.Compute_ADD_R(bgn,ComputeUtils.compute_ENCS_SP_OV(bgn,temp_start,point,pairingPreProcessing_1,pairingPreProcessing_2),Server_r)).intValue());
//            return  GarbledCricuit.runGarbledCircuit(Server_r, bgn.decrypt_G1(ComputeUtils.Compute_ADD_R(bgn,ComputeUtils.compute_ENCS_SP_OV(bgn,temp_start,point,temp_end),Server_r)).intValue());
//            return  GarbledCricuit.runGarbledCircuit(Server_r, bgn.decrypt_G1(compute_ENCS_SP_OV(bgn, Server_r, temp_start, point, temp_end)).intValue());
    }
}
