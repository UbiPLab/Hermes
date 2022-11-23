package TimeTest;

import Baselines.FullPermutation;
import Ciper.BGN;
import FileUtils.ReadFile;
import PathQuery.GarbledCricuit;
import PathQuery.QueryNoNode;
import PathQuery.QueryTokenGen;
import PathQuery.Strategy_SecureNavigation;
import Setup.Setup_init;
import com.carrotsearch.sizeof.RamUsageEstimator;
import it.unisa.dia.gas.jpbc.Element;
import myGraph.Point;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author DELL
 * @Date 2022/10/26 14:21
 * @Version 1.0
 */
public class k_search_baseline_FP {

    public static void main(String[] args) throws Exception{
        BGN bgn = new BGN();
        String file_path = "E:\\Hermes\\OpenStreetMap\\experiments\\1000\\";
        int number = 1000;
        int numberOfnodes = number+1;
        Point start = new Point(83);
        Point end = new Point(339);
        Point[] h = new Point[7];
//        for (int i = 0; i < h.length; i++) {
//            h[i] = new Point(i+1);
//        }

        start.x = 29;
        start.y = 54;

        h[0] = new Point(568);
        h[0].x = 17;
        h[0].y = 52;

        h[1] = new Point(643);
        h[1].x = 32;
        h[1].y = 63;

        h[2] = new Point(51);
        h[2].x = 55;
        h[2].y = 82;

        h[3] = new Point(987);
        h[3].x = 29;
        h[3].y = 44;

        h[4] = new Point(813);
        h[4].x = 43;
        h[4].y = 56;

        h[5] = new Point(635);
        h[5].x = 58;
        h[5].y = 95;

        h[6] = new Point(417);
        h[6].x = 9;
        h[6].y = 30;

        end.x = 54;
        end.y = 71;

        start.setC_X(bgn);
        start.setC_Y(bgn);
        end.setC_X(bgn);
        end.setC_Y(bgn);
        for (int i = 0; i < h.length; i++) {
            h[i].setC_X(bgn);
            h[i].setC_Y(bgn);
        }

        System.out.println();
//
        Setup_init setup_init = new Setup_init();
        setup_init.init_Key();
//
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
//        gen_DXandArr dXandArr = new gen_DXandArr(numberOfnodes,bgn,Enc_node, use_floydWarshall.shortestPathMatrix,Enc_shortestDistanceMatrix, setup_init.PRF_F1,setup_init.PRF_F2,
//                setup_init.PRP_T1, setup_init.PRP_T2, setup_init.T1, setup_init.T2);
        long EDX_end = System.currentTimeMillis();
        System.out.println("Encrypt DX and Arr: "+(EDX_end-EDX_start));
        System.out.println("DX ready");

//        SaveFile.saveString_list_t(file_path+"K_vi.txt", dXandArr.K_vi);
        String[] K_vi = ReadFile.readString_list_t(file_path+"K_vi.txt");
//        SaveFile.saveString_list_t(file_path+"K_vi_star.txt", dXandArr.K_vi_star);
        String[] K_vi_star = ReadFile.readString_list_t(file_path+"K_vi_star.txt");
//        SaveFile.saveString_list_t(file_path+"randomNumber_vi.txt", dXandArr.randomNumber_vi);
        String[] randomNumber_vi = ReadFile.readString_list_t(file_path+"randomNumber_vi.txt");
//
//
//        SaveFile.saveByte_Three(file_path+"Arr.txt", dXandArr.Arr);
        byte[][][] Arr = ReadFile.readByte_Three(file_path + "Arr.txt");

//        SaveFile.saveByte_matrix(file_path+"DX.txt", dXandArr.DX);
        byte[][] DX = ReadFile.readByte_martix(file_path+"DX.txt");

        System.out.println("Arr size: "+RamUsageEstimator.sizeOf(Arr));
        System.out.println("DX size: "+RamUsageEstimator.sizeOf(DX));

        long Token_start = System.currentTimeMillis();

        int[] q1 = new QueryTokenGen().generateToken_q1(setup_init.PRP_T2, setup_init.T2, start,end, h);
        System.out.println("q1 len:"+q1.length);
        myUtil.show.show_IntArr(q1);
        byte[][] q2 = new QueryTokenGen().generateToken_q2(setup_init.PRF_F2, start, end, h);
        byte[][] q3 = new QueryTokenGen().generateToken_q3(start, end, h, K_vi_star);
//        byte[][] q3 = new QueryTokenGen().generateToken_q3(start, end, h, dXandArr.K_vi_star);
        long Token_end = System.currentTimeMillis();
        System.out.println("Token ok: "+(Token_end-Token_start));
        System.out.println("Token size: "+(RamUsageEstimator.sizeOf(q1)+RamUsageEstimator.sizeOf(q2)+RamUsageEstimator.sizeOf(q3)));


        Strategy_SecureNavigation strategy_secureNavigation = new Strategy_SecureNavigation();

        List<Point> res = new ArrayList<>();

//        Point[] points = strategy_secureNavigation.retrieve(bgn,dXandArr.DX, dXandArr.Arr, dXandArr.randomNumber_vi,dXandArr.K_vi_star,q1,q2);
        Point[] points = strategy_secureNavigation.retrieve(bgn,DX, Arr, randomNumber_vi,K_vi_star,q1,q2);
        for (int i = 0; i < points.length; i++) {
            points[i].setC_X(bgn);
            points[i].setC_Y(bgn);
        }

        long SN_time = 0;
        long all_time_start = System.currentTimeMillis();
//        long SN_time = SN_end-SN_start;
//        System.out.println(SN_time);
//        AlwaysNearest alwaysNearest = new AlwaysNearest();
//        AlwaysVerticalNearest alwaysVerticalNearest = new AlwaysVerticalNearest();
//        GroupAlwaysNearest groupAlwaysNearest = new GroupAlwaysNearest();
//        GroupAlwaysVerticalNearest alwaysVerticalNearest = new GroupAlwaysVerticalNearest();
        FullPermutation fullPermutation = new FullPermutation();
        for (int oi = 0; oi < 1; oi++) {
            Point[] next = new Point[points.length-2];
            for (int i = 0; i < next.length; i++) {
                next[i] = points[i+2];
            }
            List<Point> all = new ArrayList<>();
            Point re_start = points[0];
//        Point re_start = points[0];
            Point re_end = points[1];
//        Point re_end = points[1];
//        all.add(re_start);
//        all.add(re_end);
//        for (Point p:h){
            for (Point p:next){
                all.add(p);
//                System.out.println(p.id+"("+p.x+","+p.y+")");
            }


//        strategy_secureNavigation.Secure_SN(bgn, start,end,h,res,all);

            GarbledCricuit.genCircuit();
//            alwaysNearest.Secure_SN_AN(bgn,re_start,re_end,next,res,all);
//            alwaysVerticalNearest.Secure_SN_AVN(bgn,re_start,re_end,next,res,all);

            Object[][] objects = fullPermutation.Secure_SN_k(bgn, re_start, re_end, next, res, all);
            int group_k = objects.length;

            long[] Sum_search = new long[group_k];
            long[] Sum_recover = new long[group_k];

            Element[] compare = new Element[group_k];
            for (int ok = 0; ok < group_k; ok++) {
                long SN_start = System.currentTimeMillis();
                for (int j = 0; j < objects[ok].length; j++) {
                    res.add(next[(int) objects[ok][j]]);
                }

//                System.out.println("Navi over");
//                for (Point p:res
//                ) {
//                    System.out.println(p.id);
//                }


//                System.out.println("GC times: "+ GarbledCricuit.time);
//                System.out.println("K sort ");

                List<Integer> Res_sd = new ArrayList<>();
                List<String> Res_sp = new ArrayList<>();

                Point[] sorted = new Point[res.size()+2];
                sorted[0] = re_start;
                for (int i = 0; i < res.size(); i++) {
                    sorted[i+1] = res.get(i);
                }
                sorted[res.size()+1]=re_end;

                List<Element> each_path = new ArrayList<>();
                QueryNoNode queryNoNode = new QueryNoNode();
                long Recover = 0;
                for (int i = 1; i < sorted.length; i++) {
//                Recover += queryNoNode.GetFromDX(bgn,setup_init.PRF_F1,setup_init.PRP_T2, setup_init.T2, dXandArr.DX, dXandArr.Arr, dXandArr.randomNumber_vi, dXandArr.K_vi_star, sorted[i-1],sorted[i],q1,q2,q3,
                    Recover += queryNoNode.GetFromDX_FP(bgn,setup_init.PRF_F1,setup_init.PRP_T2, setup_init.T2, DX, Arr, randomNumber_vi, K_vi_star, sorted[i-1],sorted[i],q1,q2,q3,
                            Res_sd,Res_sp,each_path);
                }

                if (each_path.size()==0){
                    compare[ok] = bgn.encrypt(BigInteger.valueOf(1));
                }else if (each_path.size()==1){
                    compare[ok] = each_path.get(0);
                }else {
                    Element first = each_path.get(0);
//                    System.out.println(bgn.decrypt(first).intValue());
                    for (int i = 1; i < each_path.size(); i++) {
                        first = bgn.add(first,each_path.get(i));
                    }
//                    System.out.println(bgn.decrypt(first).intValue());
                    compare[ok] = first;
                }

                int SD = 0;
//                for (Integer sd:Res_sd
//                ) {
//                    SD += sd;
//                    System.out.print(sd+",");
//                }
//                System.out.println();
//                System.out.println("SD= "+SD);

//                for (String sp:Res_sp
//                ) {
//                    System.out.print(sp+", ");
//                }
//                System.out.println();

                res.clear();

                long SN_end = System.currentTimeMillis();
//                SN_time += SN_end-SN_start;
                Sum_search[ok] = SN_end-SN_start-Recover;
                Sum_recover[ok] = Recover;
//                System.out.println("Query :"+(SN_end-SN_start-Recover));
//                System.out.println("Recover: "+Recover);
            }

            //  GC compare
//            for (int i = 0; i < compare.length; i++) {
//                System.out.println(bgn.decrypt(compare[i]).intValue());
//            }

            Element el_first = compare[0].duplicate();

            for (int i = 1; i < compare.length; i++) {
//                System.out.println("el_first: "+bgn.decrypt(el_first.duplicate()).intValue());
//                System.out.println("compare[i]: "+bgn.decrypt(compare[i].duplicate()).intValue());
//                Element sub = bgn.sub(el_first.duplicate(), compare[i].duplicate());
//                int value = bgn.decrypt_G1(sub).intValue();
//                System.out.println(value);
//                int tag = GarbledCricuit.runGarbledCircuit(10000,value );
                int tag = GarbledCricuit.runGarbledCircuit(bgn.decrypt(el_first.duplicate()).intValue(),bgn.decrypt(compare[i].duplicate()).intValue() );

                if (tag==1){
//                    System.out.println("<");
                    el_first = compare[i].duplicate();
                }
            }


            System.out.println("Sum_search: "+ Arrays.stream(Sum_search).sum());
            System.out.println("Sum_recover: "+ Arrays.stream(Sum_recover).sum());
        }
        long all_time_end = System.currentTimeMillis();

        System.out.println(all_time_end-all_time_start);



//        System.out.println(SN_time);
    }
}
