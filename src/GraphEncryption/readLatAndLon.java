package GraphEncryption;

import LocalParameter.LocalSetting;
import ReadFileData.ReadFiledata;

/**
 * @Author UbiP Lab Laptop 02
 * @Date 2022/9/23 10:20
 * @Version 1.0
 */
public class readLatAndLon {

    public int[][] LatAndLon(String FileName){
        String[][] oldfile = ReadFiledata.readArray_String(FileName);
//        myUtil.show.show_StringMatrix(oldfile);
        int[][] res = new int[oldfile.length][3];
        for (int i = 0; i < oldfile.length; i++) {
            res[i][0] = Integer.parseInt(oldfile[i][0]);
            res[i][1] = Integer.parseInt(oldfile[i][1]);
            res[i][2] = Integer.parseInt(oldfile[i][2]);
//            System.out.println(res[i][0]+","+res[i][1]+","+res[i][2]);
        }

        return res;
    }

    public static void main(String[] args) {
        int[][] ints = new readLatAndLon().LatAndLon(LocalSetting.dataSetAddress_LatAndLon);
    }
}
