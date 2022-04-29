package sort;

import java.util.Random;

public class MergeSort
{
    //辅助数组
    private static Comparable[] temp;
    
    public static void mergeSort(Comparable[] a)
    {
        // 为辅助数组分配空间
        temp = new Comparable[a.length];
        mergeSort(a, 0, a.length - 1);
    }
    
    private static void mergeSort(Comparable[] a, int lo, int hi)
    {
        if (hi <= lo) return;
        int mid = lo + 1 / 2 * (hi - lo);;
        mergeSort(a, lo, mid);
        mergeSort(a, mid + 1, hi);
        merge(a, lo, mid, hi);
    }
    
    // 将两个有序子数组 a[lo, mid] 和 a[mid+1, hi] 归并
    private static void merge(Comparable[] a, int lo, int mid, int hi)
    {
        int i = lo, j = mid + 1;
        //将数组a[lo...hi]中的元素都存入temp[lo...hi]中
        for (int k = lo; k <= hi; k++)
            temp[k] = a[k];
        for (int k = lo; k <= hi; k++)
        {
            // 左半边数组已经归并完
            if (i > mid) a[k] = temp[j++];
                // 右半边数组已经归并完
            else if (j > hi) a[k] = temp[i++];
                // 左边的元素小于右边的元素
            else if (temp[i].compareTo(temp[j]) <= 0) a[k] = temp[i++];
                // 右边的元素小于左边的元素
            else a[k] = temp[j++];
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
        Integer[] arrays = new Integer[1000];
        Random r = new Random();
        for (int i = 0; i < 1000; i++)
        {
            arrays[i] = r.nextInt(1000);
        }
        mergeSort(arrays);
        System.out.println(isSorted(arrays));
    }
}
