package Setup;

import LocalParameter.LocalSetting;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * @Author UbiP Lab Laptop 02
 * @Date 2022/8/23 8:48
 * @Version 1.0
 */
public class setup_createGraphTest {



    @Test
    public void test(){
        setup_createGraph setp = new setup_createGraph(LocalSetting.dataSetAddress_10,10);
        System.out.println(Arrays.toString(setp.node_list));
        System.out.println(setp.EWD.toString());
        System.out.println(Arrays.toString(setp.node_names));
        System.out.println(setp.edges);
    }

}