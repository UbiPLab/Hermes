package YaoGC;

/**
 * @Author UbiP Lab Laptop 02
 * @Date 2022/8/2 10:04
 * @Version 1.0
 */
public class MyGC_3_1 extends CompositeCircuit{



    private final static int  XORA = 0;
    private final static int  XORB = 1;
    private final static int  XORC = 3;
    private final static int  AND = 2;


    public MyGC_3_1(int a, int b, int c) {
        super(a+b+c, 1, 4, "MyGC_" + 3 + "_" + 1);
    }

    protected void createSubCircuits() throws Exception {
        subCircuits[XORA] = new XOR_2_1();
        subCircuits[XORB] = new XOR_2_1();
        subCircuits[XORC] = new XOR_2_1();
        subCircuits[AND] = AND_2_1.newInstance();
        super.createSubCircuits();
    }

    protected void connectWires() {

        //  a
        inputWires[0].connectTo(subCircuits[XORC].inputWires,0);
        inputWires[1].connectTo(subCircuits[XORA].inputWires,0);

        //  b
        inputWires[2].connectTo(subCircuits[XORB].inputWires,0);

        //  c
        inputWires[3].connectTo(subCircuits[XORA].inputWires,1);
        inputWires[4].connectTo(subCircuits[XORB].inputWires,1);

        subCircuits[XORA].outputWires[0].connectTo(subCircuits[AND].inputWires,0);
        subCircuits[XORB].outputWires[0].connectTo(subCircuits[AND].inputWires,1);

        subCircuits[AND].outputWires[0].connectTo(subCircuits[XORC].inputWires,1);

    }

    protected void defineOutputWires() {
        System.arraycopy(subCircuits[XORC].outputWires, 0, outputWires, 0, outDegree);
    }


}
