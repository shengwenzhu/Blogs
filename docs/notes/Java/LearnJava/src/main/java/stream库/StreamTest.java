package stream库;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author shengwenzhu
 * @date 2022/4/14 10:47
 */
public class StreamTest {

    public static void main(String[] args) {
        test();
    }


    /**
     * 中间操作和末端操作测试
     */
    public static void test() {
        int n = 100;
        int[] nums = new int[n];
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            nums[i] = random.nextInt(100);
        }
        int sum = Arrays.stream(nums).filter(x -> x > 60).map(x -> x + 1).sum();
        System.out.println(sum);
    }

}
