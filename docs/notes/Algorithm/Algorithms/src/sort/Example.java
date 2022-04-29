package sort;

import java.util.Random;

//排序算法类的模板
public class Example
{
    public static void sort(Comparable[] a)
    {
        //排序算法实现主体
    }
    
    //对元素进行比较，第一个元素小于第二个元素就输出true，否则输出false
    private static boolean less(Comparable v, Comparable w)
    {
        return v.compareTo(w) < 0;
    }
    
    //交换元素位置
    private static void exch(Comparable[] a, int i, int j)
    {
        Comparable t = a[i];
        a[i] = a[j];
        a[j] = t;
    }
    
    //在单行中打印数组
    public static void show(Comparable[] a)
    {
        for (int i = 0; i < a.length; i++)
            System.out.print(a[i] + " ");
        System.out.println();
    }
    
    //测试数组元素是否有序
    public static boolean isSorted(Comparable[] a)
    {
        for (int i = 1; i < a.length; i++)
            if (less(a[i], a[i - 1])) return false;
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
        long time = System.currentTimeMillis();
        sort(arrays);
        System.out.println(System.currentTimeMillis() - time);
        System.out.println(isSorted(arrays));
    }
}
