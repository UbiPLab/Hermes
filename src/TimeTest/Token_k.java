package TimeTest;

import Ciper.BGN;
import FileUtils.ReadFile;
import PathQuery.QueryTokenGen;
import Setup.Setup_init;
import com.carrotsearch.sizeof.RamUsageEstimator;
import myGraph.Point;

/**
 * @Author DELL
 * @Date 2022/10/31 19:13
 * @Version 1.0
 */
public class Token_k {
    public static void main(String[] args) throws Exception{
        BGN bgn = new BGN();
        int number = 1000;

        String file_path = "E:\\Hermes\\OpenStreetMap\\experiments\\"+String.valueOf(number)+"\\";

        int numberOfnodes = number+1;
        Point start = new Point(83);
        start.x = 29;
        start.y = 54;

        Point end = new Point(339);
        end.x = 54;
        end.y = 71;

        start.setC_X(bgn);
        start.setC_Y(bgn);
        end.setC_X(bgn);
        end.setC_Y(bgn);

        Point[] low = new Point[9];
        low[0] = new Point(568);
        low[0].x = 17;
        low[0].y = 52;
        low[1] = new Point(987);
        low[1].x = 29;
        low[1].y = 44;
        low[2] = new Point(417);
        low[2].x = 9;
        low[2].y = 30;
        low[3] = new Point(644);
        low[3].x = 11;
        low[3].y = 55;
        low[4] = new Point(59);
        low[4].x = 76;
        low[4].y = 81;
        low[5] = new Point(813);
        low[5].x = 43;
        low[5].y = 56;
        low[6] = new Point(291);
        low[6].x = 59;
        low[6].y = 48;
        low[7] = new Point(464);
        low[7].x = 45;
        low[7].y = 60;
        low[8] = new Point(51);
        low[8].x = 55;
        low[8].y = 82;
//        high[1] = new Point(635);
//        high[1].x = 58;
//        high[1].y = 95;
//        high[2] = new Point(59);
//        high[2].x = 76;
//        high[2].y = 81;
//        high[3] = new Point(440);
//        high[3].x = 79;
//        high[3].y = 69;

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
//        byte[][][] Arr = ReadFile.readByte_Three(file_path + "Arr.txt");
//        byte[][] DX = ReadFile.readByte_martix(file_path+"DX.txt");
//        System.out.println("Arr size: "+ RamUsageEstimator.sizeOf(Arr));
//        System.out.println("DX size: "+RamUsageEstimator.sizeOf(DX));





        long[] Sum_search = new long[9];
        long[] Sum_recover = new long[9];
        int[] Sum_recover_len= new int[9];

        int k = 5;
        for (int group_time = 0; group_time < 1; group_time++) {
            System.out.println("group_time: "+group_time);

            Point[] h = new Point[k];
            System.arraycopy(low, 0, h, 0, h.length);

            for (int i = 0; i < h.length; i++) {
                h[i].setC_X(bgn);
                h[i].setC_Y(bgn);
            }
            long Token_start = System.nanoTime();
            int[] q1 = new QueryTokenGen().generateToken_q1(setup_init.PRP_T2, setup_init.T2, start, end, h);
            myUtil.show.show_IntArr(q1);
            byte[][] q2 = new QueryTokenGen().generateToken_q2(setup_init.PRF_F2, start, end, h);
            byte[][] q3 = new QueryTokenGen().generateToken_q3(start, end, h, K_vi_star);
//        byte[][] q3 = new QueryTokenGen().generateToken_q3(start, end, h, dXandArr.K_vi_star);
            long Token_end = System.nanoTime();
            Sum_search[group_time] = (Token_end - Token_start);
            System.out.println("Token ok: " + (Token_end - Token_start));
            Sum_recover[group_time] = (RamUsageEstimator.sizeOf(q1) + RamUsageEstimator.sizeOf(q2) + RamUsageEstimator.sizeOf(q3) + RamUsageEstimator.sizeOf(h));
            System.out.println("Token size: " + Sum_recover[group_time]);
            System.out.println();
        }

        System.out.println("-----------------");
        myUtil.show.show_longArr(Sum_search);
        myUtil.show.show_longArr(Sum_recover);
        myUtil.show.show_IntArr(Sum_recover_len);

    }
}
