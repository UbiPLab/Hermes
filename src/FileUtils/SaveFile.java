package FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Base64;
import java.util.List;

/**
 * @Author UbiP Lab Laptop 02
 * @Date 2022/5/11:36
 * @Version 1.0
 */
public class SaveFile {

    public static void saveByte_list_t(String address,byte[] array){
        //1.创建字符输出流
        FileWriter writeFile = null;
        try {
            //2.数据想写入的路径及文件
            File file = new File(address);
            //3.如果该文件不存在，就创建
            if(!file.exists()) {
                file.createNewFile();
            }
            //4.给字节输出流赋予实例
            writeFile = new FileWriter(file);
            //5.通过循环将数组写入txt文件中
            for(int i = 0; i < array.length; i++) {
                //6.数据尾部加入" "
                writeFile.write(array[i] + "\t");
            }
            //9.把writeFile里的数据全部刷新一次，全部写入文件中
            writeFile.flush();
        } catch (Exception e) {//10.异常捕获
            e.printStackTrace();
        } finally {
            try {
                //11.如果writeFile不为空，就将其关闭
                if(writeFile != null)
                    writeFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void saveString_list_t(String address,String[] array){
        //1.创建字符输出流
        FileWriter writeFile = null;
        try {
            //2.数据想写入的路径及文件
            File file = new File(address);
            //3.如果该文件不存在，就创建
            if(!file.exists()) {
                file.createNewFile();
            }
            //4.给字节输出流赋予实例
            writeFile = new FileWriter(file);
            //5.通过循环将数组写入txt文件中
            for(int i = 0; i < array.length; i++) {
                //6.数据尾部加入" "
                writeFile.write(array[i] + "\t");
            }
            //9.把writeFile里的数据全部刷新一次，全部写入文件中
            writeFile.flush();
        } catch (Exception e) {//10.异常捕获
            e.printStackTrace();
        } finally {
            try {
                //11.如果writeFile不为空，就将其关闭
                if(writeFile != null)
                    writeFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void saveString_list_s(String address,List<BigInteger> array){
        //1.创建字符输出流
        FileWriter writeFile = null;
        try {
            //2.数据想写入的路径及文件
            File file = new File(address);
            //3.如果该文件不存在，就创建
            if(!file.exists()) {
                file.createNewFile();
            }
            //4.给字节输出流赋予实例
            writeFile = new FileWriter(file);
            //5.通过循环将数组写入txt文件中
            for(int i = 0; i < array.size(); i++) {
                //6.数据尾部加入" "
                writeFile.write(array.get(i) + "\t");
            }
            //9.把writeFile里的数据全部刷新一次，全部写入文件中
            writeFile.flush();
        } catch (Exception e) {//10.异常捕获
            e.printStackTrace();
        } finally {
            try {
                //11.如果writeFile不为空，就将其关闭
                if(writeFile != null)
                    writeFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void saveString_t(String address,String[][] array){
        //1.创建字符输出流
        FileWriter writeFile = null;
        try {
            //2.数据想写入的路径及文件
            File file = new File(address);
            //3.如果该文件不存在，就创建
            if(!file.exists()) {
                file.createNewFile();
            }
            //4.给字节输出流赋予实例
            writeFile = new FileWriter(file);
            //5.通过循环将数组写入txt文件中
            for(int i = 0; i < array.length; i++) {
                //6.数据尾部加入" "
                for(int j = 0; j < array[0].length; j++) {
                    writeFile.write(array[i][j] + "\t");
                }
                //8.加上换行符
                writeFile.write("\r\n");
            }
            //9.把writeFile里的数据全部刷新一次，全部写入文件中
            writeFile.flush();
        } catch (Exception e) {//10.异常捕获
            e.printStackTrace();
        } finally {
            try {
                //11.如果writeFile不为空，就将其关闭
                if(writeFile != null)
                    writeFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void saveByte_Three(String address,byte[][][] array){
        //1.创建字符输出流
        FileWriter writeFile = null;
        try {
            //2.数据想写入的路径及文件
            File file = new File(address);
            //3.如果该文件不存在，就创建
            if(!file.exists()) {
                file.createNewFile();
            }
            //4.给字节输出流赋予实例
            writeFile = new FileWriter(file);
            //5.通过循环将数组写入txt文件中
            for(int i = 0; i < array.length; i++) {
                //6.数据尾部加入" "
                for(int j = 0; j < array[i].length; j++) {
                    writeFile.write(Base64.getEncoder().encodeToString(array[i][j]) + "\t");
                }
                //8.加上换行符
                writeFile.write("\r\n");
            }
            //9.把writeFile里的数据全部刷新一次，全部写入文件中
            writeFile.flush();
        } catch (Exception e) {//10.异常捕获
            e.printStackTrace();
        } finally {
            try {
                //11.如果writeFile不为空，就将其关闭
                if(writeFile != null)
                    writeFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void saveDouble_matrix(String address,double[][] array){
        //1.创建字符输出流
        FileWriter writeFile = null;
        try {
            //2.数据想写入的路径及文件
            File file = new File(address);
            //3.如果该文件不存在，就创建
            if(!file.exists()) {
                file.createNewFile();
            }
            //4.给字节输出流赋予实例
            writeFile = new FileWriter(file);
            //5.通过循环将数组写入txt文件中
            for(int i = 0; i < array.length; i++) {
                //6.数据尾部加入" "
                for(int j = 0; j < array[0].length; j++) {
                    writeFile.write(String.valueOf(array[i][j]) + "\t");
                }
                //8.加上换行符
                writeFile.write("\r\n");
            }
            //9.把writeFile里的数据全部刷新一次，全部写入文件中
            writeFile.flush();
        } catch (Exception e) {//10.异常捕获
            e.printStackTrace();
        } finally {
            try {
                //11.如果writeFile不为空，就将其关闭
                if(writeFile != null)
                    writeFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void saveByte_matrix(String address,byte[][] array){
        //1.创建字符输出流
        FileWriter writeFile = null;
        try {
            //2.数据想写入的路径及文件
            File file = new File(address);
            //3.如果该文件不存在，就创建
            if(!file.exists()) {
                file.createNewFile();
            }
            //4.给字节输出流赋予实例
            writeFile = new FileWriter(file);
            //5.通过循环将数组写入txt文件中
            for(int i = 0; i < array.length; i++) {
                //6.数据尾部加入" "
                for(int j = 0; j < array[0].length; j++) {
                    writeFile.write(String.valueOf(array[i][j]) + "\t");
                }
                //8.加上换行符
                writeFile.write("\r\n");
            }
            //9.把writeFile里的数据全部刷新一次，全部写入文件中
            writeFile.flush();
        } catch (Exception e) {//10.异常捕获
            e.printStackTrace();
        } finally {
            try {
                //11.如果writeFile不为空，就将其关闭
                if(writeFile != null)
                    writeFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void saveString_ListList(String address, List<List<String>> array){
        //1.创建字符输出流
        FileWriter writeFile = null;
        try {
            //2.数据想写入的路径及文件
            File file = new File(address);
            //3.如果该文件不存在，就创建
            if(!file.exists()) {
                file.createNewFile();
            }
            //4.给字节输出流赋予实例
            writeFile = new FileWriter(file);
            //5.通过循环将数组写入txt文件中
            for(int i = 0; i < array.size(); i++) {
                //6.数据尾部加入" "
                for(int j = 0; j < array.get(i).size(); j++) {
                    if (array.get(i).get(j).equals("")){
                        writeFile.write("M" + "\t");
                    }else {
                        writeFile.write(array.get(i).get(j) + "\t");
                    }

                }
                //8.加上换行符
                writeFile.write("\r\n");
            }
            //9.把writeFile里的数据全部刷新一次，全部写入文件中
            writeFile.flush();
        } catch (Exception e) {//10.异常捕获
            e.printStackTrace();
        } finally {
            try {
                //11.如果writeFile不为空，就将其关闭
                if(writeFile != null)
                    writeFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
