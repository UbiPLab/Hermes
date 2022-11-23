package PathQuery;

import Ciper.BGN;
import myGraph.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

/**
 * @Author DELL
 * @Date 2022/11/9 8:35
 * @Version 1.0
 */
public class PointThread implements Callable<List<Point>> {

    public List<Point> Result;
    public CountDownLatch count;
    public List<Point> list_low;
    public List<Point> list_mid;
    public List<Point> list_high;
    public boolean tag;
    public int Server_r_orient;
    public Point start;
    public Point end;
    public Point B1;
    public Point B2;
    public Point currentStart;
    public BGN bgn;

    public PointThread(List<Point> Result,CountDownLatch count,List<Point> list_low,List<Point> list_mid,List<Point> list_high,Point start,Point end,Point currentStart,Point B1,Point B2,boolean tag){
        this.count = count;
        this.Result = Result;
        this.list_low = list_low;
        this.list_mid = list_mid;
        this.list_high = list_high;
        this.start = start;
        this.end = end;
        this.currentStart = currentStart;
        this.B1 = B1;
        this.B2 = B2;
        this.tag = tag;
    }

    @Override
    public List<Point> call() throws Exception {
        if (!tag){
            doLow();
        }else {
            doMidHigh();
        }
        try {
            return Result;
        }finally {
            count.countDown();
        }
    }

    private void doMidHigh() {
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
                    int size = list_mid.size();
                    Point[] next = new Point[size];
                    for (int i = 0; i < size; i++) {
                        next[i] = list_mid.get(i);
                    }
                    try {
                        new Strategy_SecureNavigation_JUC_K3().Secure_SN(bgn,currentStart,end,next,Result);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else {
                    Result.add(point_svnh);
                    list_mid.remove(point_svnh);
                    currentStart = point_svnh;
                }
            }
        }

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
                        if (StrategySecureSplitting.Split(bgn, Server_r_orient, list_left, currentStart, B2, list_left_Semi, list_right_Semi)) {
                            pull = list_left.get(0);
                        }else {
                            pull = StrategySecurePulling.Pull(bgn, Server_r_pull, start, currentStart, B2, B1, list_left_Semi, list_right_Semi, orient_CS);
                        }
                    } else {
                        if (StrategySecureSplitting.Split(bgn, Server_r_orient, list_right, currentStart, B2, list_left_Semi, list_right_Semi)) {
                            pull = list_right.get(0);
                        }else {
                            pull = StrategySecurePulling.Pull(bgn, Server_r_pull, start, currentStart, B2, B1, list_left_Semi, list_right_Semi, orient_CS);
                        }
                    }
                } else {
                    if (list_right.size() != 0) {
                        if (StrategySecureSplitting.Split(bgn, Server_r_orient, list_right, currentStart, B2, list_left_Semi, list_right_Semi)) {
                            pull = list_right.get(0);
                        }else {
                            pull = StrategySecurePulling.Pull(bgn, Server_r_pull, start, currentStart, B2, B1, list_left_Semi, list_right_Semi, orient_CS);
                        }
                    } else {
                        if (StrategySecureSplitting.Split(bgn, Server_r_orient, list_left, currentStart, B2, list_left_Semi, list_right_Semi)) {
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

                    int size = list_high.size();
                    Point[] next = new Point[size];
                    for (int i = 0; i < size; i++) {
                        next[i] = list_high.get(i);
                    }
                    try {
                        new Strategy_SecureNavigation_JUC_K3().Secure_SN(bgn,currentStart,end,next,Result);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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

    }

    private void doLow() {
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
            //  B1 side
            int orient_B1 = StrategySecureOrient.Orient(bgn, Server_r_orient, B1, start, end);
//            System.out.println("orient_B1:" + orient_B1);
            List<Point> list_right_Semi = new ArrayList<>();
            List<Point> list_left_Semi = new ArrayList<>();

            if (list_low.size() != 0) {
                Point scan = null;
                if (orient_B1 == 1) {
                    if (list_right.size() != 0) {
                        if (StrategySecureSplitting.Split(bgn, Server_r_orient, list_right, currentStart, B1, list_left_Semi, list_right_Semi)) {
                            scan = list_right.get(0);
                        } else {

                            scan = StrategySecureScanning.Scan(bgn, Server_r_orient, currentStart, end, B1, list_left_Semi, list_right_Semi, orient_B1);
                        }
                    } else {
                        if (StrategySecureSplitting.Split(bgn, Server_r_orient, list_left, currentStart, B1, list_left_Semi, list_right_Semi)) {
                            scan = list_left.get(0);
                        } else {
                            scan = StrategySecureScanning.Scan(bgn, Server_r_orient, currentStart, end, B1, list_left_Semi, list_right_Semi, orient_B1);
                        }
                    }

                } else {
                    //  orient_B1 == 0
                    if (list_left.size() != 0) {

                        if (StrategySecureSplitting.Split(bgn, Server_r_orient, list_left, currentStart, B1, list_left_Semi, list_right_Semi)) {
                            scan = list_left.get(0);
                        } else {
                            scan = StrategySecureScanning.Scan(bgn, Server_r_orient, currentStart, end, B1, list_left_Semi, list_right_Semi, orient_B1);
                        }
//                        System.out.println("scan "+scan.id);
                    } else {
                        if (StrategySecureSplitting.Split(bgn, Server_r_orient, list_right, currentStart, B1, list_left_Semi, list_right_Semi)) {
                            scan = list_right.get(0);
                        } else {
                            scan = StrategySecureScanning.Scan(bgn, Server_r_orient, currentStart, end, B1, list_left_Semi, list_right_Semi, orient_B1);
                        }
//                        System.out.println("scan "+scan.id);
                    }
                }
//                if (!scan.isSame(list_low.get(list_low.size()-1))) {
                if (list_low.size() > 1) {
                    Result.add(scan);
//                    System.out.println("Add: "+scan.id);
                    currentStart = scan;
                    //......
                    list_low.remove(scan);
                    int size = list_low.size();
                    Point[] next = new Point[size];
                    for (int i = 0; i < size; i++) {
                        next[i] = list_low.get(i);
                    }
                    try {
                        new Strategy_SecureNavigation_JUC_K3().Secure_SN(bgn,currentStart,end,next,Result);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Result.add(scan);
                    list_low.remove(scan);
                    currentStart = scan;
                }
            }
        }
    }
}
