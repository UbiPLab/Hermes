package TimeTest;

import Ciper.BGN;
import GraphEncryption.use_FloydWarshall;
import LocalParameter.LocalSetting;
import Setup.setup_createGraph;
import myGraph.Point;

/**
 * @Author DELL
 * @Date 2022/11/2 16:03
 * @Version 1.0
 */
public class Distance_test {
    public static void main(String[] args) throws Exception {
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

        Setup.setup_createGraph createGraph = new setup_createGraph(file_path+ LocalSetting.dataSetAddress_map,numberOfnodes);
        System.out.println("Init ok");
        use_FloydWarshall use_floydWarshall = new use_FloydWarshall(createGraph.EWD, createGraph.node_names);
        System.out.println("Floyd ok");
        double[][] shortestDistanceMatrix = use_floydWarshall.shortestDistanceMatrix;

        //  83 - 643 - 813 -339
        System.out.println("83 - 643 : "+shortestDistanceMatrix[83][643]);
        System.out.println("643 - 813 : "+shortestDistanceMatrix[643][813]);
        System.out.println("813 - 339 : "+shortestDistanceMatrix[813][339]);
        System.out.println("83-643-813-339 : "+(shortestDistanceMatrix[83][643]+shortestDistanceMatrix[643][813]+shortestDistanceMatrix[813][339]));

        System.out.println("83 - 813 : "+shortestDistanceMatrix[83][813]);
        System.out.println("813 - 643 : "+shortestDistanceMatrix[813][643]);
        System.out.println("643 - 339 : "+shortestDistanceMatrix[643][339]);
        System.out.println("83-813-643-339 : "+(shortestDistanceMatrix[83][813]+shortestDistanceMatrix[813][643]+shortestDistanceMatrix[643][339]));

    }
}
