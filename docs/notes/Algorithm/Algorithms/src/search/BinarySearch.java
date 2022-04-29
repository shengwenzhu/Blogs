package search;

import java.util.Arrays;

public class BinarySearch
{
    public static int rank(int key, int[] a)
    {
        int lo = 0;
        int hi = a.length - 1;
        while (lo <= hi)
        {
            int mid = lo + (hi - lo) / 2;
            if (key < a[mid]) hi = mid - 1;
            else if (key > a[mid]) lo = mid + 1;
            else return mid;
        }
        return -1;
    }
    
    public static void main(String[] args)
    {
        int[] arrays = {123, 456, 789, 147, 258, 369};
        // 排序
        Arrays.sort(arrays);
        // 二分查找
        int index = rank(456, arrays);
        System.out.println(index);
    }
}
