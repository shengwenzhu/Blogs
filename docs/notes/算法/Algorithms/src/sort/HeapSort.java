package sort;

import java.util.Random;

public class HeapSort
{
    public static void heapSort(Comparable[] a)
    {
        int n = a.length;
        // 构建初始大顶堆
        buildMaxHeap(a, n);
        
        for (int i = n - 1; i > 0; i--)
        {
            swap(a, 0, i);
            n--;
            heapify(a, 0, n);
        }
    }
    
    private static void buildMaxHeap(Comparable[] a, int len)
    {
        // 从最后一个非叶子节点开始进行调整
        for (int i = (int) len / 2 - 1; i >= 0; i--)
        {
            heapify(a, i, len);
        }
    }
    // 用于调整一个非叶子结点与其左右子结点的位置
    private static void heapify(Comparable[] a, int i, int len)
    {
        // 左子结点
        int left = 2 * i + 1;
        // 右子结点
        int right = 2 * i + 2;
        // 记录当前结点和其左右子结点中最大的结点对应的索引
        int largest = i;
        if (left < len && a[largest].compareTo(a[left]) < 0)
        {
            largest = left;
        }
        
        if (right < len && a[largest].compareTo(a[right]) < 0)
        {
            largest = right;
        }
        
        if (largest != i)
        {
            // 如果当前结点小于其左右子结点，需要交换位置
            swap(a, i, largest);
            // 交换位置后还需要对交换了位置的子结点进行调整
            heapify(a, largest, len);
        }
    }
    
    private static void swap(Comparable[] a, int i, int j)
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
        Integer[] arrays = new Integer[100];
        Random r = new Random();
        for (int i = 0; i < 100; i++)
        {
            arrays[i] = r.nextInt(1000);
        }
        heapSort(arrays);
        System.out.println(isSorted(arrays));
    }
}
