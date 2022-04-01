package 运算符;

/**
 * @author shengwenzhu
 * @date 2022/3/31 22:01
 */
public class OperatorTest {

    public static void main(String[] args) {

        /*
         * 左移运算符示例
         * 5的二进制形式：0000_0000 0000_0000 0000_0000 0000_0101
         * 整体左移两位：0000_0000 0000_0000 0000_0000 0001_0100，转换为十进制即为20
         * -5的二进制形式（使用补码表示）：1111_1111 1111_1111 1111_1111 1111_1011
         * 整体左移两位：1111_1111 1111_1111 1111_1111 1110_1100，转换为十进制即为-20
         */
        System.out.println(5 << 2);     // 输出20
        System.out.println(-5 << 2);    // 输出-20

        /*
         * 右移运算符示例
         */
        System.out.println(5 >> 2);     // 输出1
        System.out.println(-5 >> 2);    // 输出-2
        System.out.println(-5 >>> 2);   // 输出1073741822

        /*
         * &&与&区别
         */
        int a = 10;
        int b = 20;
        if (a < 10 && b++ > 10) {
            System.out.println("hello");
        }
        System.out.println(b);  // 输出20
        if (a < 10 & b++ > 10) {
            System.out.println("hello");
        }
        System.out.println(b);  // 输出21

        /*
         * 三目运算符示例
         */
        int i = 0;
        i += i < 10 ? 1 : 0;
        System.out.println(i);  // 输出1

    }
}
