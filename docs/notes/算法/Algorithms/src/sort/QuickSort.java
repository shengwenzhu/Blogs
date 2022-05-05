package sort;

import java.util.Arrays;
import java.util.Random;

public class QuickSort
{
    public static void quickSort(Comparable[] a)
    {
        int n = a.length;
        quickSort(a, 0, n-1);
    }
    
    public static void quickSort(Comparable[] a, int lo, int hi)
    {
        // 递归选择终止条件
        if (hi <= lo) return;
        // 基准元素最终位置
        int j = partition(a, lo, hi);
        quickSort(a, lo, j - 1);
        quickSort(a, j + 1, hi);
    }
    
    public static int partition(Comparable[] a, int lo, int hi)
    {
        int i = lo + 1;
        int j = hi;
        // 选择基准元素
        Comparable temp = a[lo];
        while (true)
        {
            // 从左往右遍历寻找大于基准元素的元素
            while (a[i].compareTo(temp) <= 0)
            {
                if (i == hi) break;
                i++;
            }
            // 从右往左遍历寻找小于基准元素的元素
            while (temp.compareTo(a[j]) <= 0)
            {
                if (j == lo) break;
                j--;
            }
            if (i >= j) break;
            exch(a, i, j);
        }
        exch(a, lo, j);
        return j;
    }
    
    public static void exch(Comparable[] a, int i, int j)
    {
        Comparable temp = a[i];
        a[i] = a[j];
        a[j] = temp;
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
            arrays[i] = r.nextInt(1000);
        }
        quickSort(arrays);
        System.out.println(isSorted(arrays));
    }
}
