package shulie;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.math.BigInteger;
import java.util.ArrayList;

/**
 * Created by fj on 2018/9/19.
 */

public class main {

    private static String xx;
    private static String yy;
    private static int[][] anum;

    public static void main(String[] args) {
//        System.out.println("=== "+test2(1000));
//        System.out.println("=== "+test(100));

        xx = "asdfghjgoeeee";
        yy = "bsdfghjhoeee";
//        ArrayList<Integer> list = new ArrayList<>();
//
//        test3(xx.length(),yy.length(),list);
//        System.out.println(" == "+list);

        anum = new int[xx.length()][yy.length()];
        int i1 = test4(xx.length(), yy.length());
//        System.out.println(" == "+i1);

        for(int m=0;m<anum.length;m++){//控制行数
            for(int n=0;n<anum[m].length;n++){//一行中有多少个元素（即多少列）
                System.out.print(anum[m][n]+" ");
            }
            System.out.println();
        }


//        int w=11;//背包装入的总重量不能超过该值，使得总价值最大
//        int n=5;//五个物品
//        int[] value={1,6,18,22,28};//对应物品的价值
//        int[] weight={1,2,5,6,7};//对应每个品的重量
//        System.out.println("所得结果："+findMaxValue(w,n,weight,value));
    }

    private static int findMaxValue(int w,int n, int[] weight, int[] value) {
        int[][]max=new int[n+1][w+1];
        for(int i=0;i<=w;i++)//M[n,W]
            max[0][i]=0;
        for(int j=1;j<=n;j++)
            for(int k=1;k<=w;k++)
                if(weight[j-1]>k){//第j个物品对应重量的下标减1，从0开始。
                    max[j][k]=max[j-1][k];//当加入的一个物品重量大于k，这个物品一定不能选
                }else{
                    int a=max[j-1][k];//不选第j个物品
                    int b=value[j-1]+max[j-1][k-weight[j-1]];//可以选第j个物品，选择这个物品
                    max[j][k]=a>b ? a:b;//选择第j个和不选第j个物品，那个大，返回哪个；
                }
        //遍历数组结果，打印出来看看
        for (int[] is : max) {
            for (int i : is) {
                if (i>=10) {
                    System.out.print(i+"  ");
                }else {
                    System.out.print(i+"   ");
                }
            }
            System.out.println();
        }
        return max[n][w];
    }

    private static int test4(int len1 ,int len2) {
        int num = -1;
        int index = -1;
        for (int i = 0; i < len1; i++) {
            for (int j = 0; j < len2; j++) {
                if (xx.charAt(i) == yy.charAt(j)) {
                    if(i==0 || j==0){
                        anum[i][j] = 1;
                    }else {
                        anum[i][j] = anum[i-1][j-1]+1;
                    }
                    if (num < anum[i][j]) {
                        num = anum[i][j];
                        index = i;
                    }
                }else {
                    anum[i][j] = 0;
                }
            }
        }
        System.out.println("  "+xx.substring(index-num+1,index+1));
        return num;
    }

    private static int test3(int xl, int yl, ArrayList<Integer> list) {
        if (xl == 0 || yl == 0) {
            return 0;
        }
        if (xl == 1 && yl == 1 && xx.charAt(1) == yy.charAt(1)) {

        }
        if (xx.charAt(xl-1) ==  yy.charAt(yl-1)) {
            int num = test3(xl - 2, yl - 2, list);
            list.add(num);
            System.out.println(" == "+num);
            return test3(xl-2,yl-2,list);
        }else  {
            return 0;
        }
    }

    /*private static BigInteger test2(int num) {
        if (num <= 2) {
            return BigInteger.ONE;
        }
        BigInteger x = BigInteger.ZERO;
        BigInteger y = BigInteger.ZERO;
        for (int i = 1; i <= num; i++) {
            if (i <= 2) {
                x = BigInteger.ONE;
                y = BigInteger.ONE;
            }else {
                BigInteger xy = y;
                y = x.add(y);
                x = xy;
            }
        }
        return y;
    }*/

    /*public static BigInteger test(int num) {
        if (num <= 2) {
            return BigInteger.ONE;
        }
        BigInteger bigInteger = test(num-1);
        BigInteger bigInteger2 = test(num-2);
        return bigInteger.add(bigInteger2);
    }*/






}
