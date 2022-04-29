package 数据类型;

import java.math.BigDecimal;
import java.nio.charset.Charset;

/**
 * @author shengwenzhu
 * @date 2022/3/31 14:27
 */
public class PrimaryType {
    public static void main(String[] args) {

    }

    public static String getType(Object a) {
        return a.getClass().toString();
    }

    /**
     * 长整型测试
     */
    public static void longTest() {
        // 将数值 2^31 赋值给long类型变量
        // 下行代码报错
        // long num = 2147483648;
        // 正确做法
        long num = 2147483648L;
        System.out.println(num);
    }

    /**
     * 浮点数测试
     */
    public static void doubleTest() {
        double num = 1.0 / 0;
        System.out.println(num);    // Infinity
        // Double类提供了方法判断double类型变量是否为无穷大
        System.out.println(Double.isInfinite(num));
        double num2 = -1.0 / 0;
        System.out.println(num);    // Infinity

        double num3 = 0.0 / 0;
        System.out.println(num3);   // NaN
        System.out.println(Double.isNaN(num3)); // true

        // 浮点数计算引发误差
        System.out.println(2 - 1.1);      // 0.8999999999999999

        BigDecimal decimal = new BigDecimal(2);
        // 使用BigDecimal类中参数类型为double的构造器存在一定的不可预知性
        // 如下所示，并没有创建一个值为1.1的BigDecimal实例，所以不推荐使用该构造器
        BigDecimal decimal2 = new BigDecimal(1.1);
        System.out.println(decimal2.toString());    // 1.100000000000000088817841970012523233890533447265625
        BigDecimal res = decimal.subtract(decimal2);
        System.out.println(res.toString());     // 0.899999999999999911182158029987476766109466552734375

        // 推荐使用参数类型为字符串的构造器
        BigDecimal decimal3 = new BigDecimal("2");
        // 如下创建了一个值为1.1的BigDecimal实例
        BigDecimal decimal4 = new BigDecimal("1.1");
        System.out.println(decimal4.toString());    // 1.1
        BigDecimal res2 = decimal3.subtract(decimal4);
        System.out.println(res2);     // 0.9
    }

    /**
     * 字符型值测试
     */
    public static void charTest() {
        char sex = '男';
        // '男'的Unicode值表示形式
        char sex2 = '\u7537';

        System.out.println(Charset.defaultCharset());
    }

    /**
     * 数值类型转换测试
     */
    public static void typeConversion() {
        int n = 1_2345_6789;
        float f = n;
        System.out.println(f);  // 1.23456792E8

        int num = 300;
        // 300的二进制形式：1_0010_1100
        // 转换为Byte类型时会将原来的数进行截断，即得到0010_1100
        byte num2 = (byte)num;
        System.out.println(num2); // 44
    }
}
