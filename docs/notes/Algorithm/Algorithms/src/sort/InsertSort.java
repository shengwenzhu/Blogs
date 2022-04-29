package sort;

import java.util.Arrays;
import java.util.Random;

public class InsertSort
{
    public static void insertSort(Comparable[] a)
    {
        int n = a.length;
        for (int i = 1; i < n; i++)
        {
            Comparable temp = a[i];
            int j;
            for (j = i - 1; j >= 0 && temp.compareTo(a[j]) < 0; j--)
            {
                a[j + 1] = a[j];
            }
            a[j + 1] = temp;
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
        Integer[] arrays = new Integer[100];
        Random r = new Random();
        for (int i = 0; i < 100; i++)
        {
            arrays[i] = r.nextInt(1000);
        }
        insertSort(arrays);
        System.out.println(isSorted(arrays));
    }
}
