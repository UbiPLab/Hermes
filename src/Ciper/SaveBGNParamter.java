package Ciper;

import FileUtils.SaveFile;
import it.unisa.dia.gas.jpbc.Element;

import java.math.BigInteger;

/**
 * @Author UbiP Lab Laptop 02
 * @Date 2022/5/11
 * @Version 1.0
 */
public class SaveBGNParamter {

    public static void Save_n(BigInteger n){
        String address = "n_byte.txt";
        SaveFile.saveByte_list_t(address, n.toByteArray());
    }

    public static void Save_q1(BigInteger q1){
        String address = "q1_byte.txt";
        SaveFile.saveByte_list_t(address, q1.toByteArray());
    }

    public static void Save_q2(BigInteger q2){
        String address = "q2_byte.txt";
        SaveFile.saveByte_list_t(address, q2.toByteArray());
    }


    public static void Save_G(Element g){
        String address = "g_byte.txt";
        SaveFile.saveByte_list_t(address, g.toBytes());
    }

    public static void Save_G1(Element g1){
        String address = "g1_byte.txt";
        SaveFile.saveByte_list_t(address, g1.toBytes());
    }

    public static void Save_U(Element u){
        String address = "u_byte.txt";
        SaveFile.saveByte_list_t(address, u.toBytes());
    }

    public static void Save_H(Element h){
        String address = "h_byte.txt";
        SaveFile.saveByte_list_t(address, h.toBytes());
    }

    public static void Save_H1(Element h1){
        String address = "h1_byte.txt";
        SaveFile.saveByte_list_t(address, h1.toBytes());
    }

}
