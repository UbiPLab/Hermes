package FileUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * @Author UbiP Lab Laptop 02
 * @Date 2022/5/11
 * @Version 1.0
 */
public class ReadFile {

    public static byte[] readByte_list_t(String address){
        //读入文件
        //1.声明一个字符输入流
        FileReader reader = null;
        //2.声明一个字符输入缓冲流
        BufferedReader readerBuf = null;
        //3.声明一个二维数组
        byte[] array = null;
        try {
            //4.指定reader的读取路径
            reader = new FileReader(address);
            //5.通过BufferedReader包装字符输入流
            readerBuf = new BufferedReader(reader);
            //6.创建一个集合，用来存放读取的文件的数据
            List<String> strList = new ArrayList<>();
            //7.用来存放一行的数据
            String lineStr;
            String[] str_list = null;
            //8.逐行读取txt文件中的内容
            while((lineStr = readerBuf.readLine()) != null) {
                //9.把读取的行添加到list中
                str_list = lineStr.split("\t");
            }
            array = new byte[str_list.length];
            for (int i = 0; i < str_list.length; i++) {
                array[i] = Byte.parseByte(str_list[i]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //17.关闭字符输入流
            try {
                if(reader != null)
                    reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //18.关闭字符输入缓冲流
            try {
                if(readerBuf != null)
                    readerBuf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //19.返回稀疏数组
        return array;
    }

    public static String[] readString_list_t(String address){
        //读入文件
        //1.声明一个字符输入流
        FileReader reader = null;
        //2.声明一个字符输入缓冲流
        BufferedReader readerBuf = null;
        //3.声明一个二维数组
        String[] array = null;
        try {
            //4.指定reader的读取路径
            reader = new FileReader(address);
            //5.通过BufferedReader包装字符输入流
            readerBuf = new BufferedReader(reader);
            //6.创建一个集合，用来存放读取的文件的数据
            List<String> strList = new ArrayList<>();
            //7.用来存放一行的数据
            String lineStr;
            //8.逐行读取txt文件中的内容
            while((lineStr = readerBuf.readLine()) != null) {
                //9.把读取的行添加到list中
                array = lineStr.split("\t");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //17.关闭字符输入流
            try {
                if(reader != null)
                    reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //18.关闭字符输入缓冲流
            try {
                if(readerBuf != null)
                    readerBuf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //19.返回稀疏数组
        return array;
    }

    public static List<BigInteger> readString_list_s(String address){
        //读入文件
        //1.声明一个字符输入流
        FileReader reader = null;
        //2.声明一个字符输入缓冲流
        BufferedReader readerBuf = null;
        //3.声明一个二维数组
        List<BigInteger> array = new ArrayList<>();
        try {
            //4.指定reader的读取路径
            reader = new FileReader(address);
            //5.通过BufferedReader包装字符输入流
            readerBuf = new BufferedReader(reader);
            //6.创建一个集合，用来存放读取的文件的数据
            List<String> strList = new ArrayList<>();
            //7.用来存放一行的数据
            String lineStr;
            //8.逐行读取txt文件中的内容
            String[] array_temp = null;
            while((lineStr = readerBuf.readLine()) != null) {
                //9.把读取的行添加到list中
                array_temp = lineStr.split("\t");
            }
            for (int i = 0; i < array_temp.length; i++) {
                array.add(new BigInteger(array_temp[i]));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //17.关闭字符输入流
            try {
                if(reader != null)
                    reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //18.关闭字符输入缓冲流
            try {
                if(readerBuf != null)
                    readerBuf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //19.返回稀疏数组
        return array;
    }

    public static byte[][] readByte_martix(String address){
        //读入文件
        //1.声明一个字符输入流
        FileReader reader = null;
        //2.声明一个字符输入缓冲流
        BufferedReader readerBuf = null;
        //3.声明一个二维数组
        byte[][] array = null;
        try {
            //4.指定reader的读取路径
            reader = new FileReader(address);
            //5.通过BufferedReader包装字符输入流
            readerBuf = new BufferedReader(reader);
            //6.创建一个集合，用来存放读取的文件的数据
            List<String> strList = new ArrayList<>();
            //7.用来存放一行的数据
            String lineStr;
            //8.逐行读取txt文件中的内容
            while((lineStr = readerBuf.readLine()) != null) {
                //9.把读取的行添加到list中
                strList.add(lineStr);
            }
            //10.获取文件有多少行
            int lineNum = strList.size();
            //11.获取数组有多少列
            String s =  strList.get(0);
            int columnNum = s.split("\t").length;
            //12.根据文件行数创建对应的数组
            array = new byte[strList.size()][columnNum];
            //13.记录输出当前行
            int count = 0;
            //14.循环遍历集合，将集合中的数据放入数组中
            for(String str : strList) {
                //15.将读取的str按照" "分割，用字符串数组来接收
                String[] strs = str.split("\t");
                for(int i = 0; i < columnNum; i++) {
                    array[count][i] = Byte.parseByte(strs[i]);;
                }
                //16.将行数 + 1
                count++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //17.关闭字符输入流
            try {
                if(reader != null)
                    reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //18.关闭字符输入缓冲流
            try {
                if(readerBuf != null)
                    readerBuf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //19.返回稀疏数组
        return array;
    }

    public static byte[][][] readByte_Three(String address){
        //读入文件
        //1.声明一个字符输入流
        FileReader reader = null;
        //2.声明一个字符输入缓冲流
        BufferedReader readerBuf = null;
        //3.声明一个二维数组

        byte[][][] array = null;
        try {
            //4.指定reader的读取路径
            reader = new FileReader(address);
            //5.通过BufferedReader包装字符输入流
            readerBuf = new BufferedReader(reader);
            //6.创建一个集合，用来存放读取的文件的数据
            List<String> strList = new ArrayList<>();
            //7.用来存放一行的数据
            String lineStr;
            //8.逐行读取txt文件中的内容
            while((lineStr = readerBuf.readLine()) != null) {
                //9.把读取的行添加到list中
                strList.add(lineStr);
            }
            //10.获取文件有多少行
            int lineNum = strList.size();
            //11.获取数组有多少列
            String s =  strList.get(0);
            int columnNum = s.split("\t").length;
            //12.根据文件行数创建对应的数组
            array = new byte[strList.size()][columnNum][];
            //13.记录输出当前行
            int count = 0;
            //14.循环遍历集合，将集合中的数据放入数组中
            for(String str : strList) {
                //15.将读取的str按照" "分割，用字符串数组来接收
                String[] strs = str.split("\t");
                for(int i = 0; i < columnNum; i++) {
//                    array[count][i] = strs[i];;
                    array[count][i] = Base64.getDecoder().decode(strs[i]);
                }
                //16.将行数 + 1
                count++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //17.关闭字符输入流
            try {
                if(reader != null)
                    reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //18.关闭字符输入缓冲流
            try {
                if(readerBuf != null)
                    readerBuf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //19.返回稀疏数组
        return array;
    }


    public static String[][] readString_t(String address){
        //读入文件
        //1.声明一个字符输入流
        FileReader reader = null;
        //2.声明一个字符输入缓冲流
        BufferedReader readerBuf = null;
        //3.声明一个二维数组
        String[][] array = null;
        try {
            //4.指定reader的读取路径
            reader = new FileReader(address);
            //5.通过BufferedReader包装字符输入流
            readerBuf = new BufferedReader(reader);
            //6.创建一个集合，用来存放读取的文件的数据
            List<String> strList = new ArrayList<>();
            //7.用来存放一行的数据
            String lineStr;
            //8.逐行读取txt文件中的内容
            while((lineStr = readerBuf.readLine()) != null) {
                //9.把读取的行添加到list中
                strList.add(lineStr);
            }
            //10.获取文件有多少行
            int lineNum = strList.size();
            //11.获取数组有多少列
            String s =  strList.get(0);
            int columnNum = s.split("\t").length;
            //12.根据文件行数创建对应的数组
            array = new String[strList.size()][columnNum];
            //13.记录输出当前行
            int count = 0;
            //14.循环遍历集合，将集合中的数据放入数组中
            for(String str : strList) {
                //15.将读取的str按照" "分割，用字符串数组来接收
                String[] strs = str.split("\t");
                for(int i = 0; i < columnNum; i++) {
                    array[count][i] = strs[i];;
                }
                //16.将行数 + 1
                count++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //17.关闭字符输入流
            try {
                if(reader != null)
                    reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //18.关闭字符输入缓冲流
            try {
                if(readerBuf != null)
                    readerBuf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //19.返回稀疏数组
        return array;
    }



    public static double[][] readDoule_matrix(String address){
        //读入文件
        //1.声明一个字符输入流
        FileReader reader = null;
        //2.声明一个字符输入缓冲流
        BufferedReader readerBuf = null;
        //3.声明一个二维数组
        double[][] array = null;
        try {
            //4.指定reader的读取路径
            reader = new FileReader(address);
            //5.通过BufferedReader包装字符输入流
            readerBuf = new BufferedReader(reader);
            //6.创建一个集合，用来存放读取的文件的数据
            List<String> strList = new ArrayList<>();
            //7.用来存放一行的数据
            String lineStr;
            //8.逐行读取txt文件中的内容
            while((lineStr = readerBuf.readLine()) != null) {
                //9.把读取的行添加到list中
                strList.add(lineStr);
            }
            //10.获取文件有多少行
            int lineNum = strList.size();
            //11.获取数组有多少列
            String s =  strList.get(0);
            int columnNum = s.split("\t").length;
            //12.根据文件行数创建对应的数组
            array = new double[strList.size()][columnNum];
            //13.记录输出当前行
            int count = 0;
            //14.循环遍历集合，将集合中的数据放入数组中
            for(String str : strList) {
                //15.将读取的str按照" "分割，用字符串数组来接收
                String[] strs = str.split("\t");
                for(int i = 0; i < columnNum; i++) {
                    array[count][i] = Double.parseDouble(strs[i]);;
                }
                //16.将行数 + 1
                count++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //17.关闭字符输入流
            try {
                if(reader != null)
                    reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //18.关闭字符输入缓冲流
            try {
                if(readerBuf != null)
                    readerBuf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //19.返回稀疏数组
        return array;
    }

    public static List<List<String>> readString_ListList(String address){
        //读入文件
        //1.声明一个字符输入流
        FileReader reader = null;
        //2.声明一个字符输入缓冲流
        BufferedReader readerBuf = null;
        //3.声明一个二维数组
        List<List<String>> array = new ArrayList<>();
//        String[][] array = null;
        try {
            //4.指定reader的读取路径
            reader = new FileReader(address);
            //5.通过BufferedReader包装字符输入流
            readerBuf = new BufferedReader(reader);
            //6.创建一个集合，用来存放读取的文件的数据
            List<String> strList = new ArrayList<>();
            //7.用来存放一行的数据
            String lineStr;
            //8.逐行读取txt文件中的内容
            while((lineStr = readerBuf.readLine()) != null) {
                //9.把读取的行添加到list中
                strList.add(lineStr);
            }
            //10.获取文件有多少行
            int lineNum = strList.size();
            //11.获取数组有多少列
            String s =  strList.get(0);
            int columnNum = s.split("\t").length;
            //12.根据文件行数创建对应的数组
//            array = new String[strList.size()][columnNum];
            //13.记录输出当前行
            int count = 0;
            //14.循环遍历集合，将集合中的数据放入数组中
            for(String str : strList) {
                //15.将读取的str按照" "分割，用字符串数组来接收
                String[] strs = str.split("\t");
                List<String> line = new ArrayList<>();
                for(int i = 0; i < columnNum; i++) {
                    line.add(strs[i]);
                }
                //16.将行数 + 1
                count++;
                array.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //17.关闭字符输入流
            try {
                if(reader != null)
                    reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //18.关闭字符输入缓冲流
            try {
                if(readerBuf != null)
                    readerBuf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //19.返回稀疏数组
        return array;
    }

}
