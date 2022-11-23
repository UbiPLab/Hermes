package PathQuery;

import Ciper.BGN;
import myGraph.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author UbiP Lab Laptop 02
 * @Date 2022/9/2 9:01
 * @Version 1.0
 */
public class Strategy_SecureNavigation {

    public Point[] retrieve(BGN bgn,byte[][] DX, byte[][][] Arr, String[] randomNumber_vi, String[] K_vi_star,
                                int[] q1, byte[][] q2) throws Exception {
//        Point start = new Retrieve().Retrieve_XY(bgn,DX,Arr,randomNumber_vi,K_vi_star,q1[0],q2[0]);
//        Point end = new Retrieve().Retrieve_XY(bgn,DX,Arr,randomNumber_vi,K_vi_star,q1[1],q2[1]);
        Point[] points = new Point[q1.length];
        for (int i = 0; i < q1.length; i++) {
            points[i] = new Retrieve().Retrieve_XY(bgn,DX,Arr,randomNumber_vi,K_vi_star,q1[i],q2[i]);
        }
//        points[0] = start;
//        points[1] = end;
        return points;
    }

    public void show(Point[] points){
        System.out.print("points: ");
        for (int i = 0; i < points.length; i++) {
            System.out.print(points[i].id+",");
        }
        System.out.print("end:");
    }

    public  Point currentStart;
    public void Secure_SN(BGN bgn,Point start,Point end, Point[] points, List<Point> Result, List<Point> ALL) throws Exception {

//        Point[] points = retrieve(bgn, DX, Arr, randomNumber_vi, K_vi_star, q1, q2);
//        Point start = points[0];
//        Point end = points[1];
//        Result.add(start);
//        System.out.println("-----------------");
//        System.out.println("{{{");
//        System.out.print("start: "+start.id+",");
//        show(points);
//        System.out.println(end.id+".");

        //  group
        StrategySecureGroup secureGroup = new StrategySecureGroup();
        int Server_r_group =10000;
        secureGroup.groupPoint(bgn,Server_r_group,points,start,end);

        List<Point> list_low = StrategySecureGroup.getEachArea_without_sd(points,-1);
        List<Point> list_mid = StrategySecureGroup.getEachArea_without_sd(points,0);
        List<Point> list_high = StrategySecureGroup.getEachArea_without_sd(points,1);

//        System.out.println("List_low:");
//        for (Point p:list_low) {
//            System.out.print(p.id+",");
//        }
//        System.out.println();
//
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
        //  bridge
        int Server_r_bridge =10000;
        Point B1 = new StrategySecureBridging().choosePoint_B1(bgn,Server_r_bridge,list_mid,list_high,start,end);
        Point B2 = new StrategySecureBridging().choosePoint_B2(end);

//        System.out.println("B1: "+B1.id);
//        System.out.println("B2: "+B2.id);
        //  Navi

        //  000
        if(list_low.size()==0 && list_mid.size()==0 && list_high.size()==0){
            Result.add(end);
            return;
        }

        int Server_r_orient =10000;
        int Server_r_split =10000;
        int Server_r_scan =10000;

//        System.out.println("Low:" );
//        Point currentStart;
        currentStart = start;


        if (list_low.size() != 0) {
            //  分左右
            List<Point> list_left = new ArrayList<>();
            List<Point> list_right = new ArrayList<>();
//            Point p = list_low.get(0);

            //  orient
            StrategySecureOrient.Orient(bgn, Server_r_orient, list_low, start, end);
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
            int orient_B1 = StrategySecureOrient.Orient(bgn, Server_r_orient, B1, start, end);
//            System.out.println("orient_B1:" + orient_B1);
            List<Point> list_right_Semi = new ArrayList<>();
            List<Point> list_left_Semi = new ArrayList<>();

            if (list_low.size() != 0) {
                Point scan = null;
                if (orient_B1 == 1) {
                    if (list_right.size() != 0) {
                        if (StrategySecureSplitting.Split(bgn, Server_r_split, list_right, currentStart, B1, list_left_Semi, list_right_Semi)) {
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

                            scan = StrategySecureScanning.Scan(bgn, Server_r_scan, currentStart, end, B1, list_left_Semi, list_right_Semi, orient_B1);
                        }
//                        System.out.println("scan "+scan.id);

                    } else {
                        if (StrategySecureSplitting.Split(bgn, Server_r_split, list_left, currentStart, B1, list_left_Semi, list_right_Semi)) {
                            scan = list_left.get(0);
                        } else{
                            scan = StrategySecureScanning.Scan(bgn, Server_r_scan, currentStart, end, B1, list_left_Semi, list_right_Semi, orient_B1);
                        }
//                        System.out.println("scan "+scan.id);
                    }

                } else {
                    //  orient_B1 == 0
                    if (list_left.size() != 0) {

                        if (StrategySecureSplitting.Split(bgn, Server_r_split, list_left, currentStart, B1, list_left_Semi, list_right_Semi)) {
                            scan = list_left.get(0);
                        }else {
                            scan = StrategySecureScanning.Scan(bgn, Server_r_scan, currentStart, end, B1, list_left_Semi, list_right_Semi, orient_B1);
                        }
//                        System.out.println("scan "+scan.id);
                    } else {
                        if (StrategySecureSplitting.Split(bgn, Server_r_split, list_right, currentStart, B1, list_left_Semi, list_right_Semi)) {
                            scan = list_right.get(0);
                        }else {
                            scan = StrategySecureScanning.Scan(bgn, Server_r_scan, currentStart, end, B1, list_left_Semi, list_right_Semi, orient_B1);
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
                    Secure_SN(bgn, currentStart, B1, next, Result,ALL);

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

        //  mid
//        System.out.println("Mid: ");
        int Server_r_SVNH =10000;
        if (list_mid.size() != 0){
            StrategySecureVerticallyNearestHop hop = new StrategySecureVerticallyNearestHop();

            Point point_svnh = hop.getPoint_SVNH(bgn, Server_r_SVNH, currentStart, B2, list_mid);
            if (!point_svnh.isSame(B2) ) {
//                if (!point_svnh.isSame(list_mid.get(list_mid.size()-1))) {
                if (list_mid.size()>1) {
                    Result.add(point_svnh);
                    currentStart = point_svnh;
                    //.....
//                    System.out.println("Add: "+point_svnh.id);
                    list_mid.remove(point_svnh);
                    ALL.remove(point_svnh);
                    int size = list_mid.size();
                    Point[] next = new Point[size];
                    for (int i = 0; i < size; i++) {
                        next[i] = list_mid.get(i);
                    }
                    Secure_SN(bgn, currentStart, B2, next, Result,ALL);
                }else {
                    Result.add(point_svnh);
//                    Result.add(end);
//                    System.out.println("Add: "+point_svnh.id);
//                    System.out.println(point_svnh.id+" the last one in mid");
                    ALL.remove(point_svnh);
                    list_mid.remove(point_svnh);
                    currentStart = point_svnh;
//                    int size = list_high.size();
//                    Point[] next = new Point[size];
//                    for (int i = 0; i < size; i++) {
//                        next[i] = list_high.get(i);
//                    }
//                    Secure_SN(bgn,point_svnh,B2,next,Result,ALL);
                }
            }
        }

        //  high
//        System.out.println("High: ");
        int Server_r_pull =10000;

        if (list_high.size() != 0){
            List<Point> list_left = new ArrayList<>();
            List<Point> list_right = new ArrayList<>();
//            Point p = list_high.get(0);

            //  orient
//            StrategySecureOrient.Orient(bgn, Server_r_orient, list_high, start, end);
            StrategySecureOrient.Orient(bgn, Server_r_orient, list_high, currentStart, end);
            for (Point p_i : list_high) {
                if (p_i.side == 1) {
                    list_left.add(p_i);
                } else if (p_i.side == 0) {
                    list_right.add(p_i);
                }
            }
            //  B1 side
            int orient_CS = StrategySecureOrient.Orient(bgn, Server_r_orient, currentStart, start, end);
//            System.out.println("orient_CS: "+orient_CS);
            List<Point> list_right_Semi = new ArrayList<>();
            List<Point> list_left_Semi = new ArrayList<>();

            if (list_high.size() != 0) {
                Point pull = null;
                if (orient_CS == 1) {
                    if (list_left.size() != 0) {
                        if (StrategySecureSplitting.Split(bgn, Server_r_split, list_left, currentStart, B2, list_left_Semi, list_right_Semi)) {
                            pull = list_left.get(0);
                        }else {
                            pull = StrategySecurePulling.Pull(bgn, Server_r_pull, start, currentStart, B2, B1, list_left_Semi, list_right_Semi, orient_CS);
                        }
                    } else {
                        if (StrategySecureSplitting.Split(bgn, Server_r_split, list_right, currentStart, B2, list_left_Semi, list_right_Semi)) {
                            pull = list_right.get(0);
                        }else {
                            pull = StrategySecurePulling.Pull(bgn, Server_r_pull, start, currentStart, B2, B1, list_left_Semi, list_right_Semi, orient_CS);
                        }
                    }
                } else {
                    if (list_right.size() != 0) {
                        if (StrategySecureSplitting.Split(bgn, Server_r_split, list_right, currentStart, B2, list_left_Semi, list_right_Semi)) {
                            pull = list_right.get(0);
                        }else {
                            pull = StrategySecurePulling.Pull(bgn, Server_r_pull, start, currentStart, B2, B1, list_left_Semi, list_right_Semi, orient_CS);
                        }
                    } else {
                        if (StrategySecureSplitting.Split(bgn, Server_r_split, list_left, currentStart, B2, list_left_Semi, list_right_Semi)) {
                            pull = list_left.get(0);
                        }else {
                            pull = StrategySecurePulling.Pull(bgn, Server_r_pull, start, currentStart, B2, B1, list_left_Semi, list_right_Semi, orient_CS);
                        }
                    }
                }
//                if (!pull.isSame(list_high.get(list_high.size()-1))) {
                if (list_high.size()>1) {
                    Result.add(pull);
                    currentStart = pull;
                    //......
//                    System.out.println("Add: "+pull.id);
                    list_high.remove(pull);
                    ALL.remove(pull);
                    int size = list_high.size();

                    Point[] next = new Point[size];
                    for (int i = 0; i < size; i++) {
                        next[i] = list_high.get(i);
                    }
                    Secure_SN(bgn, currentStart, B2, next, Result,ALL);
                }else {
                    Result.add(pull);
//                    System.out.println("Add: "+pull.id);
//                    System.out.println(pull.id+" the last one in high");
                    list_high.remove(pull);
                }
            }

        }else {
//            if (!currentStart.isSame(end)){
//                Result.add(end);
//            }
        }
//        System.out.println("}}}");
//        System.out.println("--------------");
    }

    public void SecureNavigation_SN(BGN bgn,byte[][] DX, byte[][][] Arr, String[] randomNumber_vi, String[] K_vi_star,
                                    int[] q1, byte[][] q2, List<Point> Result) throws Exception {

//        List<Point> Result = new ArrayList<>();
        //  Retrieve all the point

        Point start = new Retrieve().Retrieve_XY(bgn,DX,Arr,randomNumber_vi,K_vi_star,q1[0],q2[0]);
        Point end = new Retrieve().Retrieve_XY(bgn,DX,Arr,randomNumber_vi,K_vi_star,q1[1],q2[1]);
        Point[] points = new Point[q1.length-2];
        for (int i = 2; i < q1.length; i++) {
            points[i-2] = new Retrieve().Retrieve_XY(bgn,DX,Arr,randomNumber_vi,K_vi_star,q1[i],q2[i]);
        }
        Result.add(start);

        //  group
        StrategySecureGroup secureGroup = new StrategySecureGroup();
        int Server_r_group =1000;
        secureGroup.groupPoint(bgn,Server_r_group,points);

        List<Point> list_low = StrategySecureGroup.getEachArea_without_sd(points,-1);
        List<Point> list_mid = StrategySecureGroup.getEachArea_without_sd(points,0);
        List<Point> list_high = StrategySecureGroup.getEachArea_without_sd(points,1);

        //  bridge
        int Server_r_bridge =1000;
        Point B1 = new StrategySecureBridging().choosePoint_B1(bgn,Server_r_bridge,list_mid,list_high,start,end);
        Point B2 = new StrategySecureBridging().choosePoint_B2(end);

        System.out.println("B1: "+B1.id);
        //  Navi

        //  000
        if(list_low.size()==0 && list_mid.size()==0 && list_high.size()==0){
            Result.add(end);
        }

        int Server_r_orient =1000;
        int Server_r_split =1000;
        int Server_r_scan =1000;

        System.out.println("Low:" );
        Point currentStart = start;
        if (list_low.size() != 0) {
            List<Point> list_left = new ArrayList<>();
            List<Point> list_right = new ArrayList<>();
//            Point p = list_low.get(0);

            //  orient
            StrategySecureOrient.Orient(bgn, Server_r_orient, list_low, start, end);
            for (Point p_i : list_low) {
                if (p_i.side == 1) {
                    list_left.add(p_i);
                } else if (p_i.side == 0) {
                    list_right.add(p_i);
                }
            }
            //  B1 side
            int orient_B1 = StrategySecureOrient.Orient(bgn, Server_r_orient, B1, start, end);
            List<Point> list_right_Semi = new ArrayList<>();
            List<Point> list_left_Semi = new ArrayList<>();

            while (list_low.size() != 0) {
                Point scan = null;
                if (orient_B1 == 1) {
                    if (list_right.size() != 0) {
                        StrategySecureSplitting.Split(bgn, Server_r_split, list_right, currentStart, B1, list_left_Semi, list_right_Semi);

                        scan = StrategySecureScanning.Scan(bgn, Server_r_scan, currentStart, end, B1, list_left_Semi, list_right_Semi, orient_B1);

                    } else {
                        StrategySecureSplitting.Split(bgn, Server_r_split, list_left, currentStart, B1, list_left_Semi, list_right_Semi);

                        scan = StrategySecureScanning.Scan(bgn, Server_r_scan, currentStart, end, B1, list_left_Semi, list_right_Semi, orient_B1);

                    }

                } else {
                    //  orient_B1 == 0
                    if (list_left.size() != 0) {
                        StrategySecureSplitting.Split(bgn, Server_r_split, list_left, currentStart, B1, list_left_Semi, list_right_Semi);

                        scan = StrategySecureScanning.Scan(bgn, Server_r_scan, currentStart, end, B1, list_left_Semi, list_right_Semi, orient_B1);

                    } else {
                        StrategySecureSplitting.Split(bgn, Server_r_split, list_right, currentStart, B1, list_left_Semi, list_right_Semi);

                        scan = StrategySecureScanning.Scan(bgn, Server_r_scan, currentStart, end, B1, list_left_Semi, list_right_Semi, orient_B1);

                    }
                }
                Result.add(scan);
                System.out.println(scan.id);
                currentStart = scan;
                //......
                list_low.remove(scan);
            }
        }

        //  mid
        System.out.println("Mid: ");
        int Server_r_SVNH =1000;
        if (list_mid.size() != 0){
            StrategySecureVerticallyNearestHop hop = new StrategySecureVerticallyNearestHop();
            while (list_mid.size() !=0) {
                Point point_svnh = hop.getPoint_SVNH(bgn, Server_r_SVNH, currentStart, B2, list_mid);
                Result.add(point_svnh);
                currentStart = point_svnh;
                //.....
                System.out.println(point_svnh.id);
                list_mid.remove(point_svnh);
            }
        }

        //  high
        System.out.println("High: ");
        int Server_r_pull =1000;

        if (list_high.size() != 0){
            List<Point> list_left = new ArrayList<>();
            List<Point> list_right = new ArrayList<>();
//            Point p = list_high.get(0);

            //  orient
            StrategySecureOrient.Orient(bgn, Server_r_orient, list_high, start, end);
            for (Point p_i : list_high) {
                if (p_i.side == 1) {
                    list_left.add(p_i);
                } else if (p_i.side == 0) {
                    list_right.add(p_i);
                }
            }
            //  B1 side
            int orient_CS = StrategySecureOrient.Orient(bgn, Server_r_orient, currentStart, start, end);
            List<Point> list_right_Semi = new ArrayList<>();
            List<Point> list_left_Semi = new ArrayList<>();

            while (list_low.size() != 0) {
                Point pull = null;
                if (orient_CS == 1) {
                    if (list_right.size() != 0) {
                        StrategySecureSplitting.Split(bgn, Server_r_split, list_right, currentStart, B2, list_left_Semi, list_right_Semi);

                        pull = StrategySecurePulling.Pull(bgn, Server_r_pull, start, currentStart, B2, B1,list_left_Semi, list_right_Semi, orient_CS);

                    } else {
                        StrategySecureSplitting.Split(bgn, Server_r_split, list_left, currentStart, B2, list_left_Semi, list_right_Semi);

                        pull = StrategySecurePulling.Pull(bgn, Server_r_pull, start, currentStart, B2, B1,list_left_Semi, list_right_Semi, orient_CS);

                    }

                } else {
                    //  orient_B1 == 0
                    if (list_left.size() != 0) {
                        StrategySecureSplitting.Split(bgn, Server_r_split, list_left, currentStart, B1, list_left_Semi, list_right_Semi);

                        pull = StrategySecurePulling.Pull(bgn, Server_r_pull, start, currentStart, B2, B1,list_left_Semi, list_right_Semi, orient_CS);

                    } else {
                        StrategySecureSplitting.Split(bgn, Server_r_split, list_right, currentStart, B1, list_left_Semi, list_right_Semi);

                        pull = StrategySecurePulling.Pull(bgn, Server_r_pull, start, currentStart, B2, B1,list_left_Semi, list_right_Semi, orient_CS);

                    }
                }

                Result.add(pull);
                currentStart = pull;
                //......
                System.out.println(pull.id);
                list_high.remove(pull);
            }

        }else {
            if (!currentStart.isSame(end)){
                Result.add(end);
            }
        }

    }
}
