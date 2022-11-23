package GraphEncryption;

import LocalParameter.LocalSetting;
import Setup.setup_createGraph;
import org.junit.Test;

import java.util.Arrays;

/**
 * @Author UbiP Lab Laptop 02
 * @Date 2022/8/23 9:10
 * @Version 1.0
 */
public class use_FloydWarshallTest {


    @Test
    public void test(){
        setup_createGraph setup = new setup_createGraph(LocalSetting.dataSetAddress_10,10);
        use_FloydWarshall use_floydWarshall = new use_FloydWarshall(setup.EWD, setup.node_names);
        System.out.println(Arrays.toString(use_floydWarshall.adjTable));
        System.out.println(Arrays.toString(use_floydWarshall.shortestDistanceMatrix));
        System.out.println(use_floydWarshall.shortestPathMatrix.toString());
    }

}