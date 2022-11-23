package Baselines;

import Ciper.BGN;
import myGraph.Point;
import myUtil.Pseudo;

import java.util.*;

/**
 * @Author UbiP Lab Laptop 02
 * @Date 2022/10/5 18:49
 * @Version 1.0
 */
public class FullPermutation {

    public Object[][] Secure_SN_k(BGN bgn, Point start, Point end, Point[] points, List<Point> Result, List<Point> ALL) throws Exception {

//        Point[] points = retrieve(bgn, DX, Arr, randomNumber_vi, K_vi_star, q1, q2);
//        Point start = points[0];
//        Point end = points[1];
//        Result.add(start);
        int[] nums= new int[points.length];
        for (int i = 0; i < nums.length; i++) {
            nums[i] = i;
        }
        List<List<Integer>> permute = permute(nums);
        Object[][] order = new Object[permute.size()][];
        for (int i = 0; i < order.length; i++) {
            order[i] = permute.get(i).toArray();
        }
        return order;
//        System.out.println("-----------------");
    }

    public static List<List<Integer>> permute(int[] nums) {
        //定义一个存储数组
        List<List<Integer>> res=new ArrayList<>();
        //如果长度为0即全排列为空
        if(nums.length==0) return res;
        Deque<Integer> path=new ArrayDeque<>();
        //定义状态，即判断当前数字是否被排列过 false-未使用，true-已使用
        Boolean[] use=new Boolean[nums.length];
        for(int i=0;i<nums.length;i++){
            use[i]=false;
        }
        dfs(res,use,path,0,nums,nums.length);
        return res;
    }

    private static void dfs(List<List<Integer>> res, Boolean[] use, Deque<Integer> path, int depth, int[] nums, int length) {
        //当深度与长度相等时，即已排列完当前情况，将其放入res中
        if(depth==length){
            res.add(new ArrayList<>(path));
            return;
        }
        for(int i=0;i<nums.length;i++){
            if(use[i]){
                continue;
            }
            path.addLast(nums[i]);
            use[i]=true;
            dfs(res, use, path, depth+1, nums, length);
            //回溯到之前的情况
            use[i]=false;
            path.removeLast();
        }
    }

    public static void main(String[] args) {
        int[] nums= Pseudo.randomArray(0, 2,3);
        List<List<Integer>> permute = permute(nums);

        Object[][] order = new Object[permute.size()][];
        for (int i = 0; i < order.length; i++) {
            order[i] = permute.get(i).toArray();
        }

        for (List<Integer> list : permute) {
            myUtil.show.show_arr(list.toArray());
        }


    }
}
