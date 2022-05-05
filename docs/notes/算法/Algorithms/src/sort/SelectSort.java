package sort;

import java.util.Arrays;
import java.util.Random;

public class SelectSort
{
    public static void selectSort(Comparable[] a)
    {
        int N = a.length;
        for (int i = 0; i < N; i++)
        {
            int min = i;
            for (int j = i + 1; j < N; j++)
            {
                if (a[j].compareTo(a[min]) < 0) min = j;
            }
            exch(a, i, min);
        }
    }
    
    //交换元素位置
    private static void exch(Comparable[] a, int i, int j)
    {
        Comparable t = a[i];
        a[i] = a[j];
        a[j] = t;
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
        Integer[] arrays = new Integer[100];
        Random r = new Random();
        for (int i = 0; i < 100; i++)
        {
            arrays[i] = r.nextInt(1000);
        }
        selectSort(arrays);
        System.out.println(isSorted(arrays));
    }
}
