package PathQuery;

import Ciper.BGN;
import myGraph.Point;

import java.util.List;

/**
 * @Author UbiP Lab Laptop 02
 * @Date 2022/8/26 11:26
 * @Version 1.0
 */
public class StrategySecureSplitting {

    public static boolean Split(BGN bgn, int Server_r, List<Point> list_Semi, Point temp_start, Point temp_end,
                             List<Point> list_left_Semi, List<Point> list_right_Semi){
        if (list_Semi.size()==1){
            return true;
        }
        for (Point p:list_Semi) {
            if (p != null){
                int orient = StrategySecureOrient.Orient(bgn, Server_r, p, temp_start, temp_end);
                if (orient==1){ // include left and on
                    list_left_Semi.add(p);
                }else if(orient==0){    //  right
                    list_right_Semi.add(p);
                }
            }
        }
        return false;
    }
}
