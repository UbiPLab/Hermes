package PathQuery;

import Ciper.BGN;
import myGraph.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author UbiP Lab Laptop 02
 * @Date 2022/9/2 9:01
 * @Version 1.0
 */
public class Strategy_SecureNavigation_JUC {

    public int k;
    public AtomicInteger aim;
    public AtomicInteger index;
    public int last;
    public Point[] origin;

    public ExecutorService executorService = Executors.newFixedThreadPool(18);
    public Resource resource = new Resource();


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


    //    public Point currentStart;
    public void Secure_SN(BGN bgn,Point start,Point end, Point[] points, List<Point> Result) throws Exception {

        //  group
        StrategySecureGroup secureGroup = new StrategySecureGroup();
        int Server_r_group =10000;
        secureGroup.groupPoint(bgn,Server_r_group,points,start,end);
        List<Point> list_low = StrategySecureGroup.getEachArea_without_sd(points,-1);
        List<Point> list_mid = StrategySecureGroup.getEachArea_without_sd(points,0);
        List<Point> list_high = StrategySecureGroup.getEachArea_without_sd(points,1);
        //  bridge
        int Server_r_bridge =10000;
        Point B1 = new StrategySecureBridging().choosePoint_B1(bgn,Server_r_bridge,list_mid,list_high,start,end);
        Point B2 = new StrategySecureBridging().choosePoint_B2(end);
        //  Navi
        //  000
        if(list_low.size()==0 && list_mid.size()==0 && list_high.size()==0){
            Result.add(end);
            return;
        }
        Point currentStart;
        currentStart = start;

        //  JUC condition 1 : low is empty single thread

        if (list_low.size() ==0 ){
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
//                        System.out.println("Add: "+point_svnh.id);
//                        int io = aim.decrementAndGet();
//                        System.out.println(io);
//                        Point op = point_svnh;
//                        //arr[index.getAndIncrement()] = op;
                        //arr[index.getAndIncrement()] = op;
//                        point_svnh.isSort=true;
                        list_mid.remove(point_svnh);

                        int size = list_mid.size();
                        Point[] next = new Point[size];
                        for (int i = 0; i < size; i++) {
                            next[i] = list_mid.get(i);
                        }
                        try {
                            new Strategy_SecureNavigation_JUC().Secure_SN(bgn,currentStart,end,next,Result);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else {
                        Result.add(point_svnh);
//                        System.out.println("Add: "+point_svnh.id);
//                        int io = aim.decrementAndGet();
//                        System.out.println(io);
//                        Point op = point_svnh;
                        //arr[index.getAndIncrement()] = op;
                        list_mid.remove(point_svnh);
//                        point_svnh.isSort=true;
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
                StrategySecureOrient.Orient(bgn, Server_r_pull, list_high, currentStart, end);
                for (Point p_i : list_high) {
                    if (p_i.side == 1) {
                        list_left.add(p_i);
                    } else if (p_i.side == 0) {
                        list_right.add(p_i);
                    }
                }
                //  B1 side
                int orient_CS = StrategySecureOrient.Orient(bgn, Server_r_pull, currentStart, start, end);
//            System.out.println("orient_CS: "+orient_CS);
                List<Point> list_right_Semi = new ArrayList<>();
                List<Point> list_left_Semi = new ArrayList<>();

                if (list_high.size() != 0) {
                    Point pull = null;
                    if (orient_CS == 1) {
                        if (list_left.size() != 0) {
                            if (StrategySecureSplitting.Split(bgn, Server_r_pull, list_left, currentStart, B2, list_left_Semi, list_right_Semi)) {
                                pull = list_left.get(0);
                            }else {
                                pull = StrategySecurePulling.Pull(bgn, Server_r_pull, start, currentStart, B2, B1, list_left_Semi, list_right_Semi, orient_CS);
                            }
                        } else {
                            if (StrategySecureSplitting.Split(bgn, Server_r_pull, list_right, currentStart, B2, list_left_Semi, list_right_Semi)) {
                                pull = list_right.get(0);
                            }else {
                                pull = StrategySecurePulling.Pull(bgn, Server_r_pull, start, currentStart, B2, B1, list_left_Semi, list_right_Semi, orient_CS);
                            }
                        }
                    } else {
                        if (list_right.size() != 0) {
                            if (StrategySecureSplitting.Split(bgn, Server_r_pull, list_right, currentStart, B2, list_left_Semi, list_right_Semi)) {
                                pull = list_right.get(0);
                            }else {
                                pull = StrategySecurePulling.Pull(bgn, Server_r_pull, start, currentStart, B2, B1, list_left_Semi, list_right_Semi, orient_CS);
                            }
                        } else {
                            if (StrategySecureSplitting.Split(bgn, Server_r_pull, list_left, currentStart, B2, list_left_Semi, list_right_Semi)) {
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
                        System.out.println("Add: "+pull.id);
                        int io = aim.decrementAndGet();
                        System.out.println(io);
                        Point op = pull;
                        //arr[index.getAndIncrement()] = op;
//                        pull.isSort=true;
                        list_high.remove(pull);

                        int size = list_high.size();
                        Point[] next = new Point[size];
                        for (int i = 0; i < size; i++) {
                            next[i] = list_high.get(i);
                        }
                        try {
                            new Strategy_SecureNavigation_JUC().Secure_SN(bgn,currentStart,end,next,Result);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else {
                        Result.add(pull);
                        System.out.println("Add: "+pull.id);
                        int io = aim.decrementAndGet();
                        System.out.println(io);
                        Point op = pull;
                        //arr[index.getAndIncrement()] = op;
//                    System.out.println(pull.id+" the last one in high");
                        pull.isSort = true;
                        list_high.remove(pull);
                    }
                }

            }
        }

        //  JUC condition 2 : low is not empty and mid/high is empty single thread

        if (list_mid.size()==0 && list_high.size()==0){
            if (list_low.size() != 0) {
                //  分左右
                List<Point> list_left = new ArrayList<>();
                List<Point> list_right = new ArrayList<>();
//            Point p = list_low.get(0);

                int Server_r_orient = 10000;
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
                        System.out.println("Add: "+scan.id);
                        int io = aim.decrementAndGet();
                        System.out.println(io);
                        Point op = scan;
                        //arr[index.getAndIncrement()] = op;
                        currentStart = scan;
                        //......

                        scan.isSort=true;
                        list_low.remove(scan);

                        int size = list_low.size();
                        Point[] next = new Point[size];
                        for (int i = 0; i < size; i++) {
                            next[i] = list_low.get(i);
                        }
                        try {
                            new Strategy_SecureNavigation_JUC().Secure_SN(bgn,currentStart,end,next,Result);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Result.add(scan);
                        System.out.println("Add: "+scan.id);
                        int io = aim.decrementAndGet();
                        System.out.println(io);
                        Point op = scan;
                        //arr[index.getAndIncrement()] = op;
                        scan.isSort=true;
                        list_low.remove(scan);
                        currentStart = scan;

                    }
                }
            }
        }

        //  JUC condition 3 : low is not empty and mid/high is not empty JUC


        if (list_low.size() !=0 && (list_mid.size()!=0 || list_high.size()!=0)) {
            //  sort : low-mid-high
//            Point[] sort = new Point[points.length];
//            int len = list_low.size();
//            for (int i = 0; i < len; i++) {
//                sort[i] = list_low.get(i);
//            }
//            for (int i = 0; i < list_mid.size(); i++) {
//                sort[i + len] = list_mid.get(i);
//            }
//            len += list_mid.size();
//            for (int i = 0; i < list_high.size(); i++) {
//                sort[i + len] = list_high.get(i);
//            }
            current(bgn, start, end, B1, Result, list_low, list_mid, list_high);
//            Point[] current = current(bgn, start, end, B1, Result, list_low, list_mid, list_high);

//            int len = list_low.size();
//            if (len >0) {
//                Point[] next_low = new Point[len];
//                for (int i = 0; i < len; i++) {
//                    next_low[i] = list_low.get(i);
//                }
////                new Strategy_SecureNavigation_JUC().Secure_SN(bgn, current[0], B1, next_low, Result);
//            }
//
//            len = list_mid.size() + list_high.size();
//            if (len>0) {
//                Point[] next_midhigh = new Point[len];
//                len = list_mid.size();
//                for (int i = 0; i < len; i++) {
//                    next_midhigh[i] = list_mid.get(i);
//                }
//                for (int i = 0; i < list_high.size(); i++) {
//                    next_midhigh[i + len] = list_high.get(i);
//                }
//                new Strategy_SecureNavigation_JUC().Secure_SN(bgn, current[1], end, next_midhigh, Result);
//        }
            //  判断是否所有点已经处理
//            boolean tag_end = false;
//            while (!tag_end) {
//                for (Point p : points) {
//                   if (!p.isSort) tag_end=false;
//                }
//                tag_end = true;
//            }
//            Result.addAll(resource.list);
//            Result.addAll(resource.res);
//            for (int i = 0; i < myArr.myArr.length; i++) {
//                Result.add(myArr.myArr[i]);
//            }

            //  拼接结果
            for (int i = 0; i < last; i++) {
                if (resource.list.contains(origin[i])){
                    Result.add(origin[i]);
                }
            }
            for (int i = 0; i < last; i++) {
                if (resource.res.contains(origin[i])){
                    Result.add(origin[i]);
                }
            }

            for (Point p:resource.list) {
                if (!Result.contains(p)){
                    Result.add(p);
                }
            }

            for (Point p:resource.res) {
                if (!Result.contains(p)){
                    Result.add(p);
                }
            }

        }

    }



    class Resource{
        public boolean[] finish = new boolean[2];
        public AtomicInteger result = new AtomicInteger(0);
        public CopyOnWriteArrayList<Point> list = new CopyOnWriteArrayList<>();
        public CopyOnWriteArrayList<Point> res = new CopyOnWriteArrayList<>();
        public Point[] arr = new Point[k];
        public Point[] next___ = new Point[2];
        public Resource(){

        }

        public void Low(List<Point> list_low,BGN bgn,Point start,Point end,Point B1){

            Point currentStart = start;
            if (list_low.size() != 0) {
                //  分左右
                List<Point> list_left = new ArrayList<>();
                List<Point> list_right = new ArrayList<>();
//            Point p = list_low.get(0);

                int Server_r_orient = 10000;
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
                        list.add(scan);
                    System.out.println("Add: "+scan.id);
                        int io = aim.decrementAndGet();
                        System.out.println(io);
                        Point op = scan;
                        //arr[index.getAndIncrement()] = op;
                        currentStart = scan;
//                        next___[0] = scan;
//                        System.out.println("Next: "+next___[0].id);
                        //......
                        scan.isSort=true;
                        list_low.remove(scan);

//                        return currentStart;
                        int size = list_low.size();
                        Point[] next = new Point[size];
                        for (int i = 0; i < size; i++) {
                            next[i] = list_low.get(i);
                        }

                        Split(bgn,currentStart,B1,next,list);
//                        return currentStart;
//                        try {
////                            ArrayList<Point> Result = new ArrayList<>();
////                            Result.addAll(list);
//                            new Strategy_SecureNavigation_JUC().Secure_SN(bgn,currentStart,end,next,list);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        Low(list_low,bgn,currentStart,end,B1);
                    } else {
                        list.add(scan);
                        System.out.println("Add: "+scan.id);
                        if (aim!=null) {
                            int io = aim.decrementAndGet();
                            System.out.println(io);
                        }
                        Point op = scan;
                        //arr[index.getAndIncrement()] = op;
                        next___[0] = scan;
                        scan.isSort=true;
                        list_low.remove(scan);
                        currentStart = scan;
//                        finish[0] = true;
//                        return currentStart;
                    }
                }
            }
//            return currentStart;
        }
        public void MidHigh(List<Point> list_mid,List<Point> list_high,BGN bgn,Point start,Point end){
            result.set(1);
            Point currentStart = start;
            int Server_r_bridge =10000;
            Point B1 = new StrategySecureBridging().choosePoint_B1(bgn,Server_r_bridge,list_mid,list_high,start,end);
            Point B2 = new StrategySecureBridging().choosePoint_B2(end);
            int Server_r_SVNH =10000;
            if (list_mid.size() != 0){
                StrategySecureVerticallyNearestHop hop = new StrategySecureVerticallyNearestHop();

                Point point_svnh = hop.getPoint_SVNH(bgn, Server_r_SVNH, currentStart, B2, list_mid);
                if (!point_svnh.isSame(B2) ) {
//                if (!point_svnh.isSame(list_mid.get(list_mid.size()-1))) {
                    if (list_mid.size()>1) {
                        res.add(point_svnh);
                        currentStart = point_svnh;
                        //.....
                    System.out.println("Add: "+point_svnh.id);
                        int io = aim.decrementAndGet();
                        System.out.println(io);
                        Point op = point_svnh;
                        //arr[index.getAndIncrement()] = op;
                        next___[1] = point_svnh;
                        point_svnh.isSort=true;
                        list_mid.remove(point_svnh);
//                        return currentStart;
                        int size = list_mid.size();
                        Point[] next = new Point[size];
//                        Point[] next = new Point[size + list_high.size()];
                        for (int i = 0; i < size; i++) {
                            next[i] = list_mid.get(i);
                        }
//                        for (int i = 0; i < list_high.size(); i++) {
//                            next[i + size] = list_high.get(i);
//                        }
//                new Strategy_SecureNavigation_JUC().Secure_SN(bgn, current[1], end, next_midhigh, Result);
                        Split(bgn,currentStart,end,next,res);
//                        return currentStart;
//                        try {
//                            new Strategy_SecureNavigation_JUC().Secure_SN(bgn,currentStart,end,next,list);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        MidHigh(list_mid,list_high,bgn,currentStart,end);
                    }else {
                        res.add(point_svnh);
                        System.out.println("Add: "+point_svnh.id);
                        int io = aim.decrementAndGet();
                        System.out.println(io);
                        Point op = point_svnh;
                        //arr[index.getAndIncrement()] = op;
                        next___[1] = point_svnh;
                        point_svnh.isSort=true;
                        list_mid.remove(point_svnh);
                        currentStart = point_svnh;
//                        return currentStart;
                    }
                }
//                return currentStart;
            }

            int Server_r_pull =10000;

            if (list_high.size() != 0){
                List<Point> list_left = new ArrayList<>();
                List<Point> list_right = new ArrayList<>();
//            Point p = list_high.get(0);

                //  orient
//            StrategySecureOrient.Orient(bgn, Server_r_orient, list_high, start, end);
                StrategySecureOrient.Orient(bgn, Server_r_pull, list_high, currentStart, end);
                for (Point p_i : list_high) {
                    if (p_i.side == 1) {
                        list_left.add(p_i);
                    } else if (p_i.side == 0) {
                        list_right.add(p_i);
                    }
                }
                //  B1 side
                int orient_CS = StrategySecureOrient.Orient(bgn, Server_r_pull, currentStart, start, end);
//            System.out.println("orient_CS: "+orient_CS);
                List<Point> list_right_Semi = new ArrayList<>();
                List<Point> list_left_Semi = new ArrayList<>();

                if (list_high.size() != 0) {
                    Point pull = null;
                    if (orient_CS == 1) {
                        if (list_left.size() != 0) {
                            if (StrategySecureSplitting.Split(bgn, Server_r_pull, list_left, currentStart, B2, list_left_Semi, list_right_Semi)) {
                                pull = list_left.get(0);
                            }else {
                                pull = StrategySecurePulling.Pull(bgn, Server_r_pull, start, currentStart, B2, B1, list_left_Semi, list_right_Semi, orient_CS);
                            }
                        } else {
                            if (StrategySecureSplitting.Split(bgn, Server_r_pull, list_right, currentStart, B2, list_left_Semi, list_right_Semi)) {
                                pull = list_right.get(0);
                            }else {
                                pull = StrategySecurePulling.Pull(bgn, Server_r_pull, start, currentStart, B2, B1, list_left_Semi, list_right_Semi, orient_CS);
                            }
                        }
                    } else {
                        if (list_right.size() != 0) {
                            if (StrategySecureSplitting.Split(bgn, Server_r_pull, list_right, currentStart, B2, list_left_Semi, list_right_Semi)) {
                                pull = list_right.get(0);
                            }else {
                                pull = StrategySecurePulling.Pull(bgn, Server_r_pull, start, currentStart, B2, B1, list_left_Semi, list_right_Semi, orient_CS);
                            }
                        } else {
                            if (StrategySecureSplitting.Split(bgn, Server_r_pull, list_left, currentStart, B2, list_left_Semi, list_right_Semi)) {
                                pull = list_left.get(0);
                            }else {
                                pull = StrategySecurePulling.Pull(bgn, Server_r_pull, start, currentStart, B2, B1, list_left_Semi, list_right_Semi, orient_CS);
                            }
                        }
                    }
//                if (!pull.isSame(list_high.get(list_high.size()-1))) {
                    if (list_high.size()>1) {
                        res.add(pull);
                        currentStart = pull;
                        //......
                    System.out.println("Add: "+pull.id);
                        int io = aim.decrementAndGet();
                        System.out.println(io);
                        Point op = pull;
                        //arr[index.getAndIncrement()] = op;
                        next___[1] = pull;
                        pull.isSort=true;
                        list_high.remove(pull);
//                        return currentStart;
                        int size = list_high.size();
                        Point[] next = new Point[size];
                        for (int i = 0; i < size; i++) {
                            next[i] = list_high.get(i);
                        }
                        Split(bgn,currentStart,end,next,res);
//                        return currentStart;
//                        try {
//                            new Strategy_SecureNavigation_JUC().Secure_SN(bgn,currentStart,end,next,list);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        MidHigh(list_mid,list_high,bgn,currentStart,end);
                    }else {
                        res.add(pull);
                    System.out.println("Add: "+pull.id);
                    if (aim!=null) {
                        int io = aim.decrementAndGet();
                        System.out.println(io);
                    }
                        Point op = pull;
                        //arr[index.getAndIncrement()] = op;
//                        next___[1] = pull;
//                    System.out.println(pull.id+" the last one in high");
                        pull.isSort = true;
                        list_high.remove(pull);
//                        finish[1] = true;
//                        return currentStart;
                    }
                }

            }
            result.set(0);

//            return null;
        }

        public void Split(BGN bgn,Point start,Point end, Point[] points, List<Point> Result){

            //  group
            StrategySecureGroup secureGroup = new StrategySecureGroup();
            int Server_r_group =10000;
            secureGroup.groupPoint(bgn,Server_r_group,points,start,end);
            List<Point> list_low = StrategySecureGroup.getEachArea_without_sd(points,-1);
            List<Point> list_mid = StrategySecureGroup.getEachArea_without_sd(points,0);
            List<Point> list_high = StrategySecureGroup.getEachArea_without_sd(points,1);
            //  bridge
            int Server_r_bridge =10000;
            Point B1 = new StrategySecureBridging().choosePoint_B1(bgn,Server_r_bridge,list_mid,list_high,start,end);
            Point B2 = new StrategySecureBridging().choosePoint_B2(end);
            //  Navi
            //  000
            if(list_low.size()==0 && list_mid.size()==0 && list_high.size()==0){
                Result.add(end);
                return;
            }
            Point currentStart;
            currentStart = start;

            //  JUC condition 1 : low is empty single thread

            if (list_low.size() ==0 ){
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
                    System.out.println("Add: "+point_svnh.id);
                            int io = aim.decrementAndGet();
                            System.out.println(io);
                            Point op = point_svnh;
                            //arr[index.getAndIncrement()] = op;
                            point_svnh.isSort=true;
                            list_mid.remove(point_svnh);

                            int size = list_mid.size();
                            Point[] next = new Point[size];
                            for (int i = 0; i < size; i++) {
                                next[i] = list_mid.get(i);
                            }
                            try {
                                Split(bgn,currentStart,end,next,Result);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }else {
                            Result.add(point_svnh);
                            System.out.println("Add: "+point_svnh.id);
                            int io = aim.decrementAndGet();
                            System.out.println(io);
                            Point op = point_svnh;
                            //arr[index.getAndIncrement()] = op;
                            point_svnh.isSort=true;
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
                    StrategySecureOrient.Orient(bgn, Server_r_pull, list_high, currentStart, end);
                    for (Point p_i : list_high) {
                        if (p_i.side == 1) {
                            list_left.add(p_i);
                        } else if (p_i.side == 0) {
                            list_right.add(p_i);
                        }
                    }
                    //  B1 side
                    int orient_CS = StrategySecureOrient.Orient(bgn, Server_r_pull, currentStart, start, end);
//            System.out.println("orient_CS: "+orient_CS);
                    List<Point> list_right_Semi = new ArrayList<>();
                    List<Point> list_left_Semi = new ArrayList<>();

                    if (list_high.size() != 0) {
                        Point pull = null;
                        if (orient_CS == 1) {
                            if (list_left.size() != 0) {
                                if (StrategySecureSplitting.Split(bgn, Server_r_pull, list_left, currentStart, B2, list_left_Semi, list_right_Semi)) {
                                    pull = list_left.get(0);
                                }else {
                                    pull = StrategySecurePulling.Pull(bgn, Server_r_pull, start, currentStart, B2, B1, list_left_Semi, list_right_Semi, orient_CS);
                                }
                            } else {
                                if (StrategySecureSplitting.Split(bgn, Server_r_pull, list_right, currentStart, B2, list_left_Semi, list_right_Semi)) {
                                    pull = list_right.get(0);
                                }else {
                                    pull = StrategySecurePulling.Pull(bgn, Server_r_pull, start, currentStart, B2, B1, list_left_Semi, list_right_Semi, orient_CS);
                                }
                            }
                        } else {
                            if (list_right.size() != 0) {
                                if (StrategySecureSplitting.Split(bgn, Server_r_pull, list_right, currentStart, B2, list_left_Semi, list_right_Semi)) {
                                    pull = list_right.get(0);
                                }else {
                                    pull = StrategySecurePulling.Pull(bgn, Server_r_pull, start, currentStart, B2, B1, list_left_Semi, list_right_Semi, orient_CS);
                                }
                            } else {
                                if (StrategySecureSplitting.Split(bgn, Server_r_pull, list_left, currentStart, B2, list_left_Semi, list_right_Semi)) {
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
                    System.out.println("Add: "+pull.id);
                            int io = aim.decrementAndGet();
                            System.out.println(io);
                            Point op = pull;
                            //arr[index.getAndIncrement()] = op;
                            pull.isSort=true;
                            list_high.remove(pull);

                            int size = list_high.size();
                            Point[] next = new Point[size];
                            for (int i = 0; i < size; i++) {
                                next[i] = list_high.get(i);
                            }
                            try {
                                Split(bgn,currentStart,end,next,Result);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }else {
                            Result.add(pull);
                    System.out.println("Add: "+pull.id);
                            int io = aim.decrementAndGet();
                            System.out.println(io);
                            Point op = pull;
                            //arr[index.getAndIncrement()] = op;
//                    System.out.println(pull.id+" the last one in high");
                            pull.isSort=true;
                            list_high.remove(pull);
                        }
                    }
                }
            }

            //  JUC condition 2 : low is not empty and mid/high is empty single thread

            if (list_mid.size()==0 && list_high.size()==0){
                if (list_low.size() != 0) {
                    //  分左右
                    List<Point> list_left = new ArrayList<>();
                    List<Point> list_right = new ArrayList<>();
//            Point p = list_low.get(0);

                    int Server_r_orient = 10000;
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
                    System.out.println("Add: "+scan.id);
                            int io = aim.decrementAndGet();
                            System.out.println(io);
                            Point op = scan;
                            //arr[index.getAndIncrement()] = op;
                            currentStart = scan;
                            //......
                            scan.isSort=true;
                            list_low.remove(scan);
                            int size = list_low.size();
                            Point[] next = new Point[size];
                            for (int i = 0; i < size; i++) {
                                next[i] = list_low.get(i);
                            }
                            try {
                                Split(bgn,currentStart,end,next,Result);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Result.add(scan);
                            System.out.println("Add: Split Low _ "+scan.id);
                            int io = aim.decrementAndGet();
                            System.out.println(io);
                            Point op = scan;
                            //arr[index.getAndIncrement()] = op;
                            scan.isSort=true;
                            list_low.remove(scan);
                            currentStart = scan;
                        }
                    }
                }
            }

            //  JUC condition 3 : low is not empty and mid/high is not empty JUC
            if (list_low.size() !=0 && (list_mid.size()!=0 || list_high.size()!=0)) {
                //  sort : low-mid-high
//            Point[] sort = new Point[points.length];
//            int len = list_low.size();
//            for (int i = 0; i < len; i++) {
//                sort[i] = list_low.get(i);
//            }
//            for (int i = 0; i < list_mid.size(); i++) {
//                sort[i + len] = list_mid.get(i);
//            }
//            len += list_mid.size();
//            for (int i = 0; i < list_high.size(); i++) {
//                sort[i + len] = list_high.get(i);
//            }
                current(bgn, start, end, B1, Result, list_low, list_mid, list_high);
            }
        }
    }




    public void current(BGN bgn, Point start,Point end,Point B1,List<Point> Result,List<Point> list_low,List<Point> list_mid,List<Point> list_high){
//        int N = 8;

//        ExecutorService executorService = Executors.newFixedThreadPool(N);
//        Resource resource = new Resource();
        boolean tag = true;
        // 没用处理完成一直循环
//        Point[] next_Start = new Point[2];
        boolean[] finish = new boolean[2];
        System.out.println("Start---");
        while (tag){
            tag = true;
                executorService.execute(() -> {
//                next_Start[0] = resource.Low(list_low, bgn, start, end, B1);
                    finish[0] = false;
                    resource.Low(list_low, bgn, start, end, B1);
                    finish[0] = true;
                    System.out.println("Low ok");

                });

                executorService.execute(() -> {
//                next_Start[1] = resource.MidHigh(list_mid, list_high, bgn, B1, end);
                    finish[1] = false;
                    resource.MidHigh(list_mid, list_high, bgn, B1, end);
                    finish[1] = true;
                    System.out.println("High ok");
                    finish[0] = true;
                    System.out.println(finish[0]+","+finish[1]);
                });

//            while (resource.result.get() != 0) ;
//            System.out.println(tag);
            tag = false;
        }
//        System.out.println("finish");
        boolean end_tag;
        while (finish[0] == false || finish[1] == false){
//            int i = aim.get();
//            if (i>0) {
                System.out.print(finish[0] + "," + finish[1] + ",");
//            }
        };
//        while (resource.finish[1] == false);
        System.out.println("finish----");
//        System.out.println(resource.next___[0].id);
//        System.out.println(resource.next___[1].id);
//        Result.add(resource.next___[0]);
//        Result.add(resource.next___[1]);
//        System.out.println();

//        executorService.shutdown();
//        return resource.next___.clone();

//        return null;
    }

    private Point[] convListToArr(List<Point> list) {
        Point[] re = new Point[list.size()];
        for (int i = 0; i < re.length; i++) {
            re[i] = list.get(i);
        }
        return re;
    }

    public Point Method_Low(List<Point> list_low,BGN bgn,Point start,Point end,Point B1,List<Point> Result){
        Point currentStart = start;
        if (list_low.size() != 0) {
            //  分左右
            List<Point> list_left = new ArrayList<>();
            List<Point> list_right = new ArrayList<>();
//            Point p = list_low.get(0);

            int Server_r_orient = 10000;
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
                    System.out.println("Add: "+scan.id);
                    System.out.println(aim.decrementAndGet());
                    k--;
                    currentStart = scan;
                    //......
                    scan.isSort=true;
                    list_low.remove(scan);

                    int size = list_low.size();
                    Point[] next = new Point[size];
                    for (int i = 0; i < size; i++) {
                        next[i] = list_low.get(i);
                    }
                    try {
//                            ArrayList<Point> Result = new ArrayList<>();
//                            Result.addAll(list);
                        new Strategy_SecureNavigation_JUC().Secure_SN(bgn,currentStart,end,next,Result);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
//                        Low(list_low,bgn,currentStart,end,B1);
                } else {
                    Result.add(scan);
                    list_low.remove(scan);
                    currentStart = scan;
                }
            }
        }
        return currentStart;
    }

    public Point Method_MidHigh(List<Point> list_mid,List<Point> list_high,BGN bgn,Point start,Point end,List<Point> Result) {
        Point currentStart = start;
        int Server_r_bridge = 10000;
        Point B1 = new StrategySecureBridging().choosePoint_B1(bgn, Server_r_bridge, list_mid, list_high, start, end);
        Point B2 = new StrategySecureBridging().choosePoint_B2(end);
        int Server_r_SVNH = 10000;
        if (list_mid.size() != 0) {
            StrategySecureVerticallyNearestHop hop = new StrategySecureVerticallyNearestHop();

            Point point_svnh = hop.getPoint_SVNH(bgn, Server_r_SVNH, currentStart, B2, list_mid);
            if (!point_svnh.isSame(B2)) {
//                if (!point_svnh.isSame(list_mid.get(list_mid.size()-1))) {
                if (list_mid.size() > 1) {
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
                            new Strategy_SecureNavigation_JUC().Secure_SN(bgn,currentStart,end,next,Result);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
//                    MidHigh(list_mid, list_high, bgn, currentStart, end);
                } else {
                    Result.add(point_svnh);
                    list_mid.remove(point_svnh);
                    currentStart = point_svnh;

                }
            }
            return currentStart;
        }

        int Server_r_pull = 10000;

        if (list_high.size() != 0) {
            List<Point> list_left = new ArrayList<>();
            List<Point> list_right = new ArrayList<>();
//            Point p = list_high.get(0);

            //  orient
//            StrategySecureOrient.Orient(bgn, Server_r_orient, list_high, start, end);
            StrategySecureOrient.Orient(bgn, Server_r_pull, list_high, currentStart, end);
            for (Point p_i : list_high) {
                if (p_i.side == 1) {
                    list_left.add(p_i);
                } else if (p_i.side == 0) {
                    list_right.add(p_i);
                }
            }
            //  B1 side
            int orient_CS = StrategySecureOrient.Orient(bgn, Server_r_pull, currentStart, start, end);
//            System.out.println("orient_CS: "+orient_CS);
            List<Point> list_right_Semi = new ArrayList<>();
            List<Point> list_left_Semi = new ArrayList<>();

            if (list_high.size() != 0) {
                Point pull = null;
                if (orient_CS == 1) {
                    if (list_left.size() != 0) {
                        if (StrategySecureSplitting.Split(bgn, Server_r_pull, list_left, currentStart, B2, list_left_Semi, list_right_Semi)) {
                            pull = list_left.get(0);
                        } else {
                            pull = StrategySecurePulling.Pull(bgn, Server_r_pull, start, currentStart, B2, B1, list_left_Semi, list_right_Semi, orient_CS);
                        }
                    } else {
                        if (StrategySecureSplitting.Split(bgn, Server_r_pull, list_right, currentStart, B2, list_left_Semi, list_right_Semi)) {
                            pull = list_right.get(0);
                        } else {
                            pull = StrategySecurePulling.Pull(bgn, Server_r_pull, start, currentStart, B2, B1, list_left_Semi, list_right_Semi, orient_CS);
                        }
                    }
                } else {
                    if (list_right.size() != 0) {
                        if (StrategySecureSplitting.Split(bgn, Server_r_pull, list_right, currentStart, B2, list_left_Semi, list_right_Semi)) {
                            pull = list_right.get(0);
                        } else {
                            pull = StrategySecurePulling.Pull(bgn, Server_r_pull, start, currentStart, B2, B1, list_left_Semi, list_right_Semi, orient_CS);
                        }
                    } else {
                        if (StrategySecureSplitting.Split(bgn, Server_r_pull, list_left, currentStart, B2, list_left_Semi, list_right_Semi)) {
                            pull = list_left.get(0);
                        } else {
                            pull = StrategySecurePulling.Pull(bgn, Server_r_pull, start, currentStart, B2, B1, list_left_Semi, list_right_Semi, orient_CS);
                        }
                    }
                }
//                if (!pull.isSame(list_high.get(list_high.size()-1))) {
                if (list_high.size() > 1) {
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
                            new Strategy_SecureNavigation_JUC().Secure_SN(bgn,currentStart,end,next,Result);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
//                    MidHigh(list_mid, list_high, bgn, currentStart, end);
                } else {
                    Result.add(pull);
//                    System.out.println("Add: "+pull.id);
//                    System.out.println(pull.id+" the last one in high");
                    list_high.remove(pull);
                }
            }
            return currentStart;
        }
        return end;
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
