package PathQuery;

import Ciper.BGN;
import myGraph.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author UbiP Lab Laptop 02
 * @Date 2022/8/9 9:08
 * @Version 1.0
 */
public class Strategy_Summary {

    /*
    只有 起点 终点
    起点 终点 + 1 上/中/下
    起点 终点 + 2 上/中/下（同一区域）
    起点 终点 + 1 上/中/下 + 1 上/中/下 （不同区域）
    起点 终点 + 2 上/中/下 + 1 上/中/下 （存在同区域）
    起点 终点 + 1 上/中/下  + 1 上/中/下 +  1 上/中/下（不同区域）
    ...
     */
    /*
    下区域：
    空： 起点 - B1
    1： 起点 - low - B1
    2：  起点 - 扫描 - B1
     */
    /*
    中区域：
    空： B1
     */

    public List<Point> OSCH(BGN bgn, Point[] points, Point start, Point end){
        List<Point> route = new ArrayList<>();
        //  先分上下中区域
        int Server_r_group = 1000;
        new StrategySecureGroup().groupPoint(bgn,Server_r_group,points,start,end);

        //  选定桥接点 B1 , B2
        int Server_r_bridge = 1000;
//        Point B1 = new StrategySecureBridging().choosePoint_B1(bgn, Server_r_bridge, points, start, end);
        Point B2 = end;

        //  low

        //  mid

        //  high

        return route;
    }
}
