package 数组;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * @author shengwenzhu
 * @date 2022/4/1 13:56
 */
public class ArrayTest {

    public static void main(String[] args) {

        arrayToListTest();
    }

    /**
     * 排序性能测试
     */
    public static void sortTest() {
        int n = 1_0000;
        int[] nums = new int[n];
        // 使用随机数填充数组
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            nums[i] = random.nextInt();
        }
        int[] nums2 = Arrays.copyOf(nums, n);
        // 使用sort方法进行排序
        long time = System.currentTimeMillis();
        Arrays.sort(nums);
        long time2 = System.currentTimeMillis();
        System.out.println("sort方法使用时间：" + (time2 - time));
        // 使用parallelSort进行排序
        long time3 = System.currentTimeMillis();
        Arrays.parallelSort(nums2);
        long time4 = System.currentTimeMillis();
        System.out.println("parallelSort方法使用时间：" + (time4 - time3));
    }

    /**
     * 指定一个比较器对数组进行排序
     */
    public static void comparatorTest() {
        // 按照字符串的长度进行排序
        String[] strs = {"stephen", "klay", "draymond", "andre"};
        Arrays.sort(strs, new Comparator<String>() {
            @Override
            public int compare(String str1, String str2) {
                return str1.length() - str2.length();
            }
        });
        System.out.println(Arrays.toString(strs));
    }

    /**
     * 将数组转换为列表测试
     */
    public static void arrayToListTest() {
        String[] strs = {"stephen", "klay", "draymond", "andre"};
        List<String> list = Arrays.asList(strs);
        list.set(1, "thompson");
        System.out.println(Arrays.toString(strs));  // 输出[stephen, thompson, draymond, andre]
    }

}
