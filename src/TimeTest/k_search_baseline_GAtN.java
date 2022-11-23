package TimeTest;

import Baselines.GroupAlwaysNearest;
import Ciper.BGN;
import FileUtils.ReadFile;
import PathQuery.GarbledCricuit;
import PathQuery.QueryNoNode;
import PathQuery.QueryTokenGen;
import PathQuery.Strategy_SecureNavigation;
import Setup.Setup_init;
import com.carrotsearch.sizeof.RamUsageEstimator;
import myGraph.Point;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author DELL
 * @Date 2022/10/26 14:21
 * @Version 1.0
 */
public class k_search_baseline_GAtN {

    public static void main(String[] args) throws Exception{
        BGN bgn = new BGN();
        int number = 2200;

        String file_path = "E:\\Hermes\\OpenStreetMap\\experiments\\"+String.valueOf(number)+"\\";

        int numberOfnodes = number+1;
        Point start = new Point(1560);
        start.x = 22;
        start.y = 56;

        Point end = new Point(1121);
        end.x = 52;
        end.y = 73;

//        start.setC_X(bgn);
//        start.setC_Y(bgn);
//        end.setC_X(bgn);
//        end.setC_Y(bgn);

        Point[] low = new Point[3];
        low[0] = new Point(1453);
        low[0].x = 10;
        low[0].y = 61;
        low[1] = new Point(1852);
        low[1].x = 20;
        low[1].y = 48;
        low[2] = new Point(336);
        low[2].x = 7;
        low[2].y = 51;


        Point[] mid = new Point[3];
        mid[0] = new Point(1452);
        mid[0].x = 29;
        mid[0].y = 68;
        mid[1] = new Point(1801);
        mid[1].x = 44;
        mid[1].y = 45;
        mid[2] = new Point(1645);
        mid[2].x = 49;
        mid[2].y = 59;


        Point[] high = new Point[3];
        high[0] = new Point(1445);
        high[0].x = 59;
        high[0].y = 91;
        high[1] = new Point(81);
        high[1].x = 65;
        high[1].y = 83;
        high[2] = new Point(1751);
        high[2].x = 70;
        high[2].y = 73;




        start.setC_X(bgn);
        start.setC_Y(bgn);
        end.setC_X(bgn);
        end.setC_Y(bgn);

        Setup_init setup_init = new Setup_init();
        setup_init.init_Key();
        setup_init.init_Function(setup_init.K, numberOfnodes);
        System.out.println("Init ok");
        System.out.println("Floyd ok");
        System.out.println("Encrypt shortestDistanceMatrix: ");
        long ESD_Start = System.currentTimeMillis();

        long ESD_End = System.currentTimeMillis();
        System.out.println("Encrypt shortestDistanceMatrix: "+(ESD_End-ESD_Start));
        System.out.println("Encrypt node: ");
        long Enode_start = System.currentTimeMillis();
        long Enode_end = System.currentTimeMillis();
        System.out.println("Encrypt node: "+(Enode_end-Enode_start));
        System.out.println("Encrypt DX and Arr: ");

        long EDX_start = System.currentTimeMillis();

        long EDX_end = System.currentTimeMillis();
        System.out.println("Encrypt DX and Arr: "+(EDX_end-EDX_start));
        System.out.println("DX ready");
        String[] K_vi = ReadFile.readString_list_t(file_path+"K_vi.txt");
        String[] K_vi_star = ReadFile.readString_list_t(file_path+"K_vi_star.txt");
        String[] randomNumber_vi = ReadFile.readString_list_t(file_path+"randomNumber_vi.txt");
        byte[][][] Arr = ReadFile.readByte_Three(file_path + "Arr.txt");
        byte[][] DX = ReadFile.readByte_martix(file_path+"DX.txt");
        System.out.println("Arr size: "+RamUsageEstimator.sizeOf(Arr));
        System.out.println("DX size: "+RamUsageEstimator.sizeOf(DX));



        int[] order_low = {3,2,2,1,1,1,0,0,0,0};
        int[] order_mid = {0,1,0,2,0,1,3,2,1,0};
        int[] order_high = {0,0,1,0,2,1,0,1,2,3};
        int k = 3;

        long[] Sum_search = new long[order_high.length];
        long[] Sum_recover = new long[order_high.length];
        int[] Sum_recover_len= new int[order_high.length];

        for (int group_time = 0; group_time < order_high.length; group_time++) {
            System.out.println("group_time: "+group_time);

            int low_index = order_low[group_time] > 0 ? order_low[group_time] : -1;
            int mid_index = order_mid[group_time] > 0 ? order_mid[group_time] : -1;
            int high_index = order_high[group_time] > 0 ? order_high[group_time] : -1;

            Point[] h = new Point[k];
            int index = 0;
            if (low_index != -1) {
                for (int i = 0; i < low_index; i++) {
                    h[index] = low[i];
                    index++;
                }
            }

            if (mid_index != -1) {
                for (int i = 0; i < mid_index; i++) {
                    h[index] = mid[i];
                    index++;
                }
            }

            if (high_index != -1) {
                for (int i = 0; i < high_index; i++) {
                    h[index] = high[i];
                    index++;
                }
            }

            for (int i = 0; i < h.length; i++) {
                h[i].setC_X(bgn);
                h[i].setC_Y(bgn);
            }
            System.out.println();


            long Token_start = System.currentTimeMillis();
            int[] q1 = new QueryTokenGen().generateToken_q1(setup_init.PRP_T2, setup_init.T2, start, end, h);
            myUtil.show.show_IntArr(q1);
            byte[][] q2 = new QueryTokenGen().generateToken_q2(setup_init.PRF_F2, start, end, h);
            byte[][] q3 = new QueryTokenGen().generateToken_q3(start, end, h, K_vi_star);
//        byte[][] q3 = new QueryTokenGen().generateToken_q3(start, end, h, dXandArr.K_vi_star);
            long Token_end = System.currentTimeMillis();
            System.out.println("Token ok: " + (Token_end - Token_start));
            System.out.println("Token size: " + (RamUsageEstimator.sizeOf(q1) + RamUsageEstimator.sizeOf(q2) + RamUsageEstimator.sizeOf(q3) + RamUsageEstimator.sizeOf(h)));
            Strategy_SecureNavigation strategy_secureNavigation = new Strategy_SecureNavigation();
//        Point[] points = strategy_secureNavigation.retrieve(bgn,dXandArr.DX, dXandArr.Arr, dXandArr.randomNumber_vi,dXandArr.K_vi_star,q1,q2);
            Point[] points = strategy_secureNavigation.retrieve(bgn, DX, Arr, randomNumber_vi, K_vi_star, q1, q2);

            for (int i = 0; i < points.length; i++) {
                points[i].setC_X(bgn);
                points[i].setC_Y(bgn);
            }

            long SN_time = 0;
//        long SN_time = SN_end-SN_start;
//        System.out.println(SN_time);

            GroupAlwaysNearest groupAlwaysNearest = new GroupAlwaysNearest();
            long[] search_time = new long[10];
            long[] recover_time = new long[10];
            int[] recover_time_len = new int[10];
            for (int oi = 0; oi < 10; oi++) {
                Point[] next = new Point[points.length - 2];
                for (int i = 0; i < next.length; i++) {
//                next[i] = points[i+2];
                    next[i] = h[i];
                }
                List<Point> all = new ArrayList<>();
//            Point re_start = points[0];
                Point re_start = start;
//            Point re_end = points[1];
                Point re_end = end;
                for (Point p : next) {
                    all.add(p);
//                System.out.println(p.id+"("+p.x+","+p.y+")");
                }
                List<Point> res = new ArrayList<>();
//        strategy_secureNavigation.Secure_SN(bgn, start,end,h,res,all);
                long SN_start = System.currentTimeMillis();
                GarbledCricuit.genCircuit();
                groupAlwaysNearest.Secure_SN_GAN(bgn, re_start, re_end, next, res, all);
                System.out.println("Navi over");
                for (Point p : res
                ) {
                    System.out.println(p.id);
                }


//            System.out.println("GC times: "+ GarbledCricuit.time);
//            System.out.println("K sort ");

                List<Integer> Res_sd = new ArrayList<>();
                List<String> Res_sp = new ArrayList<>();

                Point[] sorted = new Point[res.size() + 2];
                sorted[0] = re_start;
                for (int i = 0; i < res.size(); i++) {
                    sorted[i + 1] = res.get(i);
                }
                sorted[res.size() + 1] = re_end;


                QueryNoNode queryNoNode = new QueryNoNode();
                long Recover = 0;

                for (int i = 1; i < sorted.length; i++) {
//                Recover += queryNoNode.GetFromDX(bgn,setup_init.PRF_F1,setup_init.PRP_T2, setup_init.T2, dXandArr.DX, dXandArr.Arr, dXandArr.randomNumber_vi, dXandArr.K_vi_star, sorted[i-1],sorted[i],q1,q2,q3,
                    Recover += queryNoNode.GetFromDX(bgn, setup_init.PRF_F1, setup_init.PRP_T2, setup_init.T2, DX, Arr, randomNumber_vi, K_vi_star, sorted[i - 1], sorted[i], q1, q2, q3,
                            Res_sd, Res_sp);
                }

                int SD = 0;
                for (Integer sd : Res_sd
                ) {
                    SD += sd;
//                System.out.print(sd+",");
                }
                System.out.println();
                System.out.println("SD= " + SD);

                for (String sp : Res_sp
                ) {
                    System.out.print(sp + ", ");
                }
                System.out.println();

                long SN_end = System.currentTimeMillis();
//                SN_time += SN_end - SN_start;
                res.clear();
                search_time[oi] = SN_end - SN_start - Recover;
                recover_time[oi] = Recover;
                recover_time_len[oi] = QueryNoNode.Len_recover;
                System.out.println("Query :" + (SN_end - SN_start - Recover));
                System.out.println("Recover: " + Recover);
                System.out.println("Recover length: " + QueryNoNode.Len_recover);
                QueryNoNode.Len_recover = 0;
            }
            System.out.println("----------");
            myUtil.show.show_longArr(search_time);
            myUtil.show.show_longArr(recover_time);
            long time_1 = Arrays.stream(search_time).sum();
            long time_2= Arrays.stream(recover_time).sum();
            int size_1 = Arrays.stream(recover_time_len).sum();
            System.out.println("Serach: " + time_1);
            System.out.println("Recover: " + time_2);
            Sum_search[group_time] = time_1;
            Sum_recover[group_time] = time_2;
            Sum_recover_len[group_time] = size_1;
//        System.out.println(SN_time);
        }

        System.out.println("-----------------");
        myUtil.show.show_longArr(Sum_search);
        for (int i = 0; i < Sum_search.length; i++) {
            System.out.println(Sum_search[i]);
        }
        System.out.println("-----------------");
        myUtil.show.show_longArr(Sum_recover);
        for (int i = 0; i < Sum_recover.length; i++) {
            System.out.println(Sum_recover[i]);
        }
        System.out.println("-----------------");
        myUtil.show.show_IntArr(Sum_recover_len);
        for (int i = 0; i < Sum_recover_len.length; i++) {
            System.out.println(Sum_recover_len[i]/10);
        }
    }
}
