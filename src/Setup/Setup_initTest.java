package Setup;

import org.junit.Test;

import java.security.NoSuchAlgorithmException;

/**
 * @Author UbiP Lab Laptop 02
 * @Date 2022/8/23 8:54
 * @Version 1.0
 */
public class Setup_initTest {

    Setup_init setup_init = new Setup_init();

    @Test
    public void init_Map() {

    }

    @Test
    public void init_Function() throws Exception {
        setup_init.init_Key();
        int numberOfnodes = 10;
        setup_init.init_Function(setup_init.K, numberOfnodes);
    }

    @Test
    public void init_Key() throws NoSuchAlgorithmException {
        setup_init.init_Key();
    }
}