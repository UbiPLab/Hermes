package TimeTest;

import Ciper.BGN;
import GraphEncryption.gen_DXandArr;
import GraphEncryption.readLatAndLon;
import GraphEncryption.use_FloydWarshall;
import LocalParameter.LocalSetting;
import PathQuery.GarbledCricuit;
import PathQuery.QueryNoNode;
import PathQuery.QueryTokenGen;
import PathQuery.Strategy_SecureNavigation;
import Setup.Setup_init;
import Setup.setup_createGraph;
import it.unisa.dia.gas.jpbc.Element;
import myGraph.Point;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author UbiP Lab Laptop 02
 * @Date 2022/10/11 10:07
 * @Version 1.0
 */
public class Correct {
    public static void main(String[] args) throws Exception {
        BGN bgn = new BGN();
        String file_path = "E:\\Hermes\\OpenStreetMap\\experiments\\1800\\";
        int numberOfnodes = 1801;
        Point start = new Point(84);
        Point end = new Point(11);
        Point[] h = new Point[3];
//        for (int i = 0; i < h.length; i++) {
//            h[i] = new Point(i+1);
//        }

        start.x = 16;
        start.y = 65;

        h[0] = new Point(268);
        h[0].x = 29;
        h[0].y = 58;

        h[1] = new Point(326);
        h[1].x = 27;
        h[1].y = 61;

        h[2] = new Point(405);
        h[2].x = 24;
        h[2].y = 64;

        end.x = 30;
        end.y = 54;

        start.setC_X(bgn);
        start.setC_Y(bgn);
        end.setC_X(bgn);
        end.setC_Y(bgn);
        for (int i = 0; i < h.length; i++) {
            h[i].setC_X(bgn);
            h[i].setC_Y(bgn);
        }

//        EdgeWeightedDigraph edgeWeightedDigraph = new EdgeWeightedDigraph(numberOfnodes, ReadFiledata.readArray_String(LocalSetting.dataSetAddress_11));
        System.out.println("Points ready");
        Setup.setup_createGraph createGraph = new setup_createGraph(file_path+LocalSetting.dataSetAddress_map,numberOfnodes);
        System.out.println("Graph ready");
//        double[][] adjTable = createGraph.EWD.getAdjTable();
//        myUtil.show.doubleMatrix(adjTable);
        System.out.println();
//
        Setup_init setup_init = new Setup_init();
        setup_init.init_Key();
//
        setup_init.init_Function(setup_init.K, numberOfnodes);
        System.out.println("Init ok");
        long floyd_start = System.currentTimeMillis();
        use_FloydWarshall use_floydWarshall = new use_FloydWarshall(createGraph.EWD, createGraph.node_names);
        long floyd_end = System.currentTimeMillis();
        double[][] shortestDistanceMatrix = use_floydWarshall.shortestDistanceMatrix;
//        myUtil.show.doubleMatrix(shortestDistanceMatrix);
        System.out.println("Floyd ok");
        System.out.println("Floyd time: "+(floyd_end-floyd_start));

        System.out.println("Encrypt shortestDistanceMatrix: ");

        long ESD_Start = System.currentTimeMillis();

        Element[][] Enc_shortestDistanceMatrix = new Element[numberOfnodes][numberOfnodes];
        for (int i = 0; i < shortestDistanceMatrix.length; i++) {
            for (int j = i; j < shortestDistanceMatrix[i].length; j++) {
                Enc_shortestDistanceMatrix[i][j] = bgn.encrypt(BigInteger.valueOf((long) shortestDistanceMatrix[i][j]));
            }
        }
        long ESD_End = System.currentTimeMillis();
        System.out.println("Encrypt shortestDistanceMatrix: "+(ESD_End-ESD_Start));
        for (int i = 0; i < shortestDistanceMatrix.length; i++) {
            for (int j = 0; j < shortestDistanceMatrix[i].length; j++) {
//                Enc_shortestDistanceMatrix[i][j] = bgn.encrypt(BigInteger.valueOf((long) shortestDistanceMatrix[i][j]));
                if (j<i){
                    Enc_shortestDistanceMatrix[i][j] = Enc_shortestDistanceMatrix[j][i];
                }
            }
        }

        System.out.println("Encrypt node: ");
        int[][] latAndLon = new readLatAndLon().LatAndLon(file_path+LocalSetting.dataSetAddress_LatAndLon_wa);
        long Enode_start = System.currentTimeMillis();
        Element[][] Enc_node = new Element[numberOfnodes][2];
        for (int i = 0; i < numberOfnodes; i++) {
            Enc_node[i][0] = bgn.encrypt(BigInteger.valueOf(latAndLon[i][1]));
            Enc_node[i][1] = bgn.encrypt(BigInteger.valueOf(latAndLon[i][2]));
        }
        long Enode_end = System.currentTimeMillis();
        System.out.println("Encrypt node: "+(Enode_end-Enode_start));


        System.out.println("Encrypt DX and Arr: ");

        long EDX_start = System.currentTimeMillis();
        gen_DXandArr dXandArr = new gen_DXandArr(numberOfnodes,bgn,Enc_node, use_floydWarshall.shortestPathMatrix,Enc_shortestDistanceMatrix, setup_init.PRF_F1,setup_init.PRF_F2,
                setup_init.PRP_T1, setup_init.PRP_T2, setup_init.T1, setup_init.T2);
        long EDX_end = System.currentTimeMillis();
        System.out.println("Encrypt DX and Arr: "+(EDX_end-EDX_start));
        System.out.println("DX ready");

//        SaveFile.saveString_list_t(file_path+"K_vi.txt", dXandArr.K_vi);
//        SaveFile.saveString_list_t(file_path+"K_vi_star.txt", dXandArr.K_vi_star);
//        SaveFile.saveString_list_t(file_path+"randomNumber_vi.txt", dXandArr.randomNumber_vi);
//
//
//        SaveFile.saveByte_Three(file_path+"Arr.txt", dXandArr.Arr);
//        byte[][][] Arr_reload = ReadFile.readByte_Three("Arr.txt");
//        System.out.println("Arr:");
//        myUtil.show.show_ByteArr(dXandArr.Arr[0][0]);
//        System.out.println("Arr_reload:");
//        myUtil.show.show_ByteArr(Arr_reload[0][0]);

//        SaveFile.saveByte_matrix(file_path+"DX.txt", dXandArr.DX);
//        byte[][] DX_reload = ReadFile.readByte_martix("DX.txt");
//        System.out.println("DX:");
//        myUtil.show.show_ByteArr(dXandArr.DX[0]);
//        System.out.println("DX_reload:");
//        myUtil.show.show_ByteArr(DX_reload[0]);

        int[] q1 = new QueryTokenGen().generateToken_q1(setup_init.PRP_T2, setup_init.T2, start,end, h);
        System.out.println("q1 len:"+q1.length);
        myUtil.show.show_IntArr(q1);
        byte[][] q2 = new QueryTokenGen().generateToken_q2(setup_init.PRF_F2, start, end, h);
        byte[][] q3 = new QueryTokenGen().generateToken_q3(start, end, h, dXandArr.K_vi_star);
        System.out.println("Token ok");
        Strategy_SecureNavigation strategy_secureNavigation = new Strategy_SecureNavigation();

        List<Point> res = new ArrayList<>();

        Point[] points = strategy_secureNavigation.retrieve(bgn,dXandArr.DX, dXandArr.Arr, dXandArr.randomNumber_vi,dXandArr.K_vi_star,q1,q2);
        for (int i = 0; i < points.length; i++) {
            points[i].setC_X(bgn);
            points[i].setC_Y(bgn);
        }

        long SN_time = 0;
//        long SN_time = SN_end-SN_start;

//        System.out.println(SN_time);


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
                System.out.println(p.id+"("+p.x+","+p.y+")");
            }


//        strategy_secureNavigation.Secure_SN(bgn, start,end,h,res,all);
            long SN_start = System.currentTimeMillis();
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
                Recover += queryNoNode.GetFromDX(bgn,setup_init.PRF_F1,setup_init.PRP_T2, setup_init.T2, dXandArr.DX, dXandArr.Arr, dXandArr.randomNumber_vi, dXandArr.K_vi_star, sorted[i-1],sorted[i],q1,q2,q3,
                        Res_sd,Res_sp);
            }

            int SD = 0;
            for (Integer sd:Res_sd
                 ) {
                SD += sd;
                System.out.print(sd+",");
            }
            System.out.println();
            System.out.println("SD= "+SD);

            for (String sp:Res_sp
                 ) {
                System.out.print(sp+", ");
            }
            System.out.println();

            res.clear();
            System.out.println("Query :"+(SN_end-SN_start-Recover));
            System.out.println("Recover: "+Recover);
        }


        System.out.println(SN_time);



    }
}
