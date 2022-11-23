package myGraph;

import Ciper.BGN;
import it.unisa.dia.gas.jpbc.Element;

import java.math.BigInteger;

/**
 * @Author UbiP Lab Laptop 02
 * @Date 2022/8/8 9:31
 * @Version 1.0
 */
public class Point {
    public String name;
    public int id;
    public int x;
    public int y;
    public int group_id;    //  -1 : low ; 0 : mid ; 1 : high
//    public byte[] C_X;
//    public byte[] C_Y;
    public Element C_X;
    public Element C_Y;
    public int side;    //  1 left ; 0 right ; 重合点 -1
//    public boolean side;    //  true left ; false right
    public boolean isSort;

    public Point() {
    }

    public Point(String name) {
        this.name = name;
    }

    public Point(int id) {
        this.id = id;
    }

    public void setC_X(BGN bgn){
        // Enc_pk(x)
        C_X = bgn.encrypt(BigInteger.valueOf(x));
    }

    public void setC_Y(BGN bgn){
        // Enc_pk(y)
        C_Y = bgn.encrypt(BigInteger.valueOf(y));
    }

    @Override
    public String toString() {
        return "Point{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", x=" + x +
                ", y=" + y +
                ", group_id=" + group_id +
                ", C_X=" + C_X +
                ", C_Y=" + C_Y +
                ", side=" + side +
                '}';
    }

    public boolean isSame(Point a){
        return  (a.x == x) && (a.y == y);
    }
}
