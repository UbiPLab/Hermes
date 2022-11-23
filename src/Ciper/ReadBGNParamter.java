package Ciper;

import FileUtils.ReadFile;
import FileUtils.SaveFile;
import it.unisa.dia.gas.jpbc.Element;

import java.math.BigInteger;

/**
 * @Author UbiP Lab Laptop 02
 * @Date 2022/5/12 9:10
 * @Version 1.0
 */
public class ReadBGNParamter {

    public static BigInteger Read_n(){
        String address = "n_byte.txt";
        return new BigInteger(ReadFile.readByte_list_t(address));
    }

    public static BigInteger Read_q1(){
        String address = "q1_byte.txt";
        return new BigInteger(ReadFile.readByte_list_t(address));
    }

    public static BigInteger Read_q2(){
        String address = "q2_byte.txt";
        return new BigInteger(ReadFile.readByte_list_t(address));
    }

    public static Element Read_G(Element g){
        String address = "g_byte.txt";
        g.setFromBytes(ReadFile.readByte_list_t(address));
        return g;
    }

    public static byte[] Read_G(){
        String address = "g_byte.txt";
        return ReadFile.readByte_list_t(address);
    }

    public static Element Read_G1(Element g1){
        String address = "g1_byte.txt";
        g1.setFromBytes(ReadFile.readByte_list_t(address));
        return g1;
    }

    public static byte[] Read_G1(){
        String address = "g1_byte.txt";
        return ReadFile.readByte_list_t(address);
    }

    public static Element Read_U(Element u){
        String address = "u_byte.txt";
        u.setFromBytes(ReadFile.readByte_list_t(address));
        return u;
    }

    public static byte[] Read_U(){
        String address = "u_byte.txt";
        return ReadFile.readByte_list_t(address);
    }

    public static Element Read_H(Element h){
        String address = "h_byte.txt";
        h.setFromBytes(ReadFile.readByte_list_t(address));
        return h;
    }

    public static byte[] Read_H(){
        String address = "h_byte.txt";
        return ReadFile.readByte_list_t(address);
    }

    public static Element Read_H1(Element h1){
        String address = "h1_byte.txt";
        h1.setFromBytes(ReadFile.readByte_list_t(address));
        return h1;
    }

    public static byte[] Read_H1(){
        String address = "h1_byte.txt";
        return ReadFile.readByte_list_t(address);
    }
}
