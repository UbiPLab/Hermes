package TimeTest;

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
public class k_search {

    public static void main(String[] args) throws Exception{
        BGN bgn = new BGN();
        String file_path = "E:\\Hermes\\OpenStreetMap\\experiments\\1000\\";
        int number = 1000;
        int numberOfnodes = number+1;
        Point start = new Point(83);
        Point end = new Point(339);
        Point[] h = new Point[3];
//        for (int i = 0; i < h.length; i++) {
//            h[i] = new Point(i+1);
//        }

        int[][] low = new int[4][2];
        //  568
        low[0][0] = 17;
        low[0][1] = 52;
        //  987
        low[1][0] = 29;
        low[1][1] = 44;
        //  417
        low[2][0] = 9;
        low[2][1] = 30;
        //  644
        low[3][0] = 11;
        low[3][1] = 55;

        int[][] mid = new int[4][2];
        //  643
        mid[0][0] = 32;
        mid[0][1] = 63;
        //  813
        mid[1][0] = 43;
        mid[1][1] = 56;
        //  291
        mid[2][0] = 59;
        mid[2][1] = 48;
        //  464
        mid[3][0] = 45;
        mid[3][1] = 60;

        int[][] high = new int[4][2];
        //  51
        high[0][0] = 55;
        high[0][1] = 82;
        //  635
        high[1][0] = 58;
        high[1][1] = 95;
        //  59
        high[2][0] = 76;
        high[2][1] = 81;
        //  440
        high[3][0] = 79;
        high[3][1] = 69;


        start.x = 29;
        start.y = 54;

        h[0] = new Point(635);
        h[0].x = high[1][0];
        h[0].y = high[1][1];

        h[1] = new Point(59);
        h[1].x = high[2][0];
        h[1].y = high[2][1];

        h[2] = new Point(51);
        h[2].x = high[0][0];
        h[2].y = high[0][1];

//        h[3] = new Point(813);
//        h[3].x = mid[1][0];
//        h[3].y = mid[1][1];
//
//        h[4] = new Point(643);
//        h[4].x = mid[0][0];
//        h[4].y = mid[0][1];
//
//        h[5] = new Point(291);
//        h[5].x = mid[2][0];
//        h[5].y = mid[2][1];
//
//        h[6] = new Point(440);
//        h[6].x = high[3][0];
//        h[6].y = high[3][1];
//
//        h[7] = new Point(635);
//        h[7].x = high[1][0];
//        h[7].y = high[1][1];
//
//        h[8] = new Point(51);
//        h[8].x = high[0][0];
//        h[8].y = high[0][1];

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

//        EdgeWeightedDigraph edgeWeightedDigraph = new EdgeWeightedDigraph(numberOfnodes, ReadFiledata.readArray_String(LocalSetting.dataSetAddress_11));
//        System.out.println("Points ready");
//        Setup.setup_createGraph createGraph = new setup_createGraph(LocalSetting.dataSetAddress_map,numberOfnodes);
//        System.out.println("Graph ready");
//        double[][] adjTable = createGraph.EWD.getAdjTable();
//        myUtil.show.doubleMatrix(adjTable);
        System.out.println();
//
        Setup_init setup_init = new Setup_init();
        setup_init.init_Key();
//
        setup_init.init_Function(setup_init.K, numberOfnodes);
        System.out.println("Init ok");
//        use_FloydWarshall use_floydWarshall = new use_FloydWarshall(createGraph.EWD, createGraph.node_names);
//        double[][] shortestDistanceMatrix = use_floydWarshall.shortestDistanceMatrix;
//        myUtil.show.doubleMatrix(shortestDistanceMatrix);
        System.out.println("Floyd ok");

        System.out.println("Encrypt shortestDistanceMatrix: ");

        long ESD_Start = System.currentTimeMillis();

//        Element[][] Enc_shortestDistanceMatrix = new Element[numberOfnodes][numberOfnodes];
//        for (int i = 0; i < shortestDistanceMatrix.length; i++) {
//            for (int j = 0; j < shortestDistanceMatrix[i].length; j++) {
//                Enc_shortestDistanceMatrix[i][j] = bgn.encrypt(BigInteger.valueOf((long) shortestDistanceMatrix[i][j]));
//            }
//        }
        long ESD_End = System.currentTimeMillis();
        System.out.println("Encrypt shortestDistanceMatrix: "+(ESD_End-ESD_Start));

        System.out.println("Encrypt node: ");
//        int[][] latAndLon = new readLatAndLon().LatAndLon(LocalSetting.dataSetAddress_LatAndLon_wa);
        long Enode_start = System.currentTimeMillis();
//        Element[][] Enc_node = new Element[numberOfnodes][2];
//        for (int i = 0; i < numberOfnodes; i++) {
//            Enc_node[i][0] = bgn.encrypt(BigInteger.valueOf(latAndLon[i][1]));
//            Enc_node[i][1] = bgn.encrypt(BigInteger.valueOf(latAndLon[i][2]));
//        }
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
//        byte[][][] Arr_reload = ReadFile.readByte_Three("Arr.txt");
//        System.out.println("Arr:");
//        myUtil.show.show_ByteArr(dXandArr.Arr[0][0]);
//        System.out.println("Arr_reload:");
//        myUtil.show.show_ByteArr(Arr_reload[0][0]);

//        SaveFile.saveByte_matrix(file_path+"DX.txt", dXandArr.DX);
        byte[][] DX = ReadFile.readByte_martix(file_path+"DX.txt");
//        byte[][] DX_reload = ReadFile.readByte_martix("DX.txt");
//        System.out.println("DX:");
//        myUtil.show.show_ByteArr(dXandArr.DX[0]);
//        System.out.println("DX_reload:");
//        myUtil.show.show_ByteArr(DX_reload[0]);

        System.out.println("Arr size: "+RamUsageEstimator.sizeOf(Arr));
        System.out.println("DX size: "+RamUsageEstimator.sizeOf(DX));

        long Token_start = System.currentTimeMillis();

        int[] q1 = new QueryTokenGen().generateToken_q1(setup_init.PRP_T2, setup_init.T2, start,end, h);
        myUtil.show.show_IntArr(q1);
        byte[][] q2 = new QueryTokenGen().generateToken_q2(setup_init.PRF_F2, start, end, h);
        byte[][] q3 = new QueryTokenGen().generateToken_q3(start, end, h, K_vi_star);
//        byte[][] q3 = new QueryTokenGen().generateToken_q3(start, end, h, dXandArr.K_vi_star);
        long Token_end = System.currentTimeMillis();
        System.out.println("Token ok: "+(Token_end-Token_start));
        System.out.println("Token size: "+(RamUsageEstimator.sizeOf(q1)+RamUsageEstimator.sizeOf(q2)+RamUsageEstimator.sizeOf(q3)+RamUsageEstimator.sizeOf(h)));

        Strategy_SecureNavigation strategy_secureNavigation = new Strategy_SecureNavigation();



//        Point[] points = strategy_secureNavigation.retrieve(bgn,dXandArr.DX, dXandArr.Arr, dXandArr.randomNumber_vi,dXandArr.K_vi_star,q1,q2);
        Point[] points = strategy_secureNavigation.retrieve(bgn,DX, Arr, randomNumber_vi,K_vi_star,q1,q2);

        for (int i = 0; i < points.length; i++) {
            points[i].setC_X(bgn);
            points[i].setC_Y(bgn);
        }

        long SN_time = 0;
//        long SN_time = SN_end-SN_start;
//        System.out.println(SN_time);

        long[] search_time = new long[10];
        long[] recover_time = new long[10];
        for (int oi = 0; oi < 10; oi++) {

            Point[] next = new Point[points.length-2];
            for (int i = 0; i < next.length; i++) {
//                next[i] = points[i+2];
                next[i] = h[i];
            }
            List<Point> all = new ArrayList<>();
//            Point re_start = points[0];
        Point re_start = start;
//            Point re_end = points[1];
        Point re_end = end;
//        all.add(re_start);
//        all.add(re_end);
//        for (Point p:h){
            for (Point p:next){
                all.add(p);
                System.out.println(p.id+"("+p.x+","+p.y+")");
            }

            List<Point> res = new ArrayList<>();
//        strategy_secureNavigation.Secure_SN(bgn, start,end,h,res,all);
            long SN_start = System.currentTimeMillis();
            GarbledCricuit.genCircuit();
            strategy_secureNavigation.Secure_SN(bgn, re_start,re_end,next,res,all);
            System.out.println("Navi over");
            for (Point p:res
            ) {
                System.out.println(p.id);
            }
            long SN_end = System.currentTimeMillis();
            SN_time += SN_end-SN_start;

            System.out.println("GC times: "+ GarbledCricuit.time);
            System.out.println("K sort ");

            List<Integer> Res_sd = new ArrayList<>();
            List<String> Res_sp = new ArrayList<>();

            Point[] sorted = new Point[res.size()+2];
            sorted[0] = re_start;
            for (int i = 0; i < res.size(); i++) {
                sorted[i+1] = res.get(i);
            }
            sorted[res.size()+1]=re_end;


            QueryNoNode queryNoNode = new QueryNoNode();
            long Recover = 0;
            for (int i = 1; i < sorted.length; i++) {
//                Recover += queryNoNode.GetFromDX(bgn,setup_init.PRF_F1,setup_init.PRP_T2, setup_init.T2, dXandArr.DX, dXandArr.Arr, dXandArr.randomNumber_vi, dXandArr.K_vi_star, sorted[i-1],sorted[i],q1,q2,q3,
                Recover += queryNoNode.GetFromDX(bgn,setup_init.PRF_F1,setup_init.PRP_T2, setup_init.T2, DX, Arr, randomNumber_vi, K_vi_star, sorted[i-1],sorted[i],q1,q2,q3,
                        Res_sd,Res_sp);
            }

            int SD = 0;
            for (Integer sd:Res_sd
            ) {
                SD += sd;
//                System.out.print(sd+",");
            }
            System.out.println();
            System.out.println("SD= "+SD);

            for (String sp:Res_sp
            ) {
                System.out.print(sp+", ");
            }
            System.out.println();

            res.clear();
            search_time[oi] = SN_end-SN_start-Recover;
            recover_time[oi] = Recover;
            System.out.println("Query :"+(SN_end-SN_start-Recover));
            System.out.println("Recover: "+Recover);
        }
        System.out.println("----------");
        myUtil.show.show_longArr(search_time);
        myUtil.show.show_longArr(recover_time);
        System.out.println("Serach: "+ Arrays.stream(search_time).sum());
        System.out.println("Recover: "+ Arrays.stream(recover_time).sum());

//        System.out.println(SN_time);
    }
}
