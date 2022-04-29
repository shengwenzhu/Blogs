package sort;

import java.util.Random;

public class ShellSort
{
    public static void shellSort(Comparable[] a)
    {
        int N = a.length;
        // 首先选择一个增量序列
        // 计算递增序列的增量有两种方式，1.实时计算递增序列；2.将递增序列存储到一个数组中；
        // 本次实现采用Sedgewick增量序列：1，5，19，41，109...
        int[] h = {1, 5, 19, 41, 109, 209, 505, 929, 2161, 3905, 8929};
        //确定第一次排序的增量h
        int i = 0;
        while (h[i] < N / 2) i++;
        //将数组变为h有序
        while (i >= 0)
        {
            //temp存储每一次排序的增量
            int temp = h[i];
            for (int j = temp; j < N; j++)
            {
                Comparable t = a[j];
                int k;
                for(k = j-temp; k>=0&&t.compareTo(a[k])<0;k-=temp)
                {
                    a[k+temp] = a[k];
                }
                a[k+temp]= t;
            }
            i--;
        }
    }
    
    //测试数组元素是否有序
    public static boolean isSorted(Comparable[] a)
    {
        for (int i = 1; i < a.length; i++)
            if (a[i].compareTo(a[i - 1]) < 0) return false;
        return true;
    }
    
    public static void main(String[] args)
    {
        //测试排序结果
        Integer[] arrays = new Integer[1000];
        Random r = new Random();
        for (int i = 0; i < 1000; i++)
        {
            arrays[i] = r.nextInt(10000);
        }
        shellSort(arrays);
        System.out.println(isSorted(arrays));
        
    }
}
