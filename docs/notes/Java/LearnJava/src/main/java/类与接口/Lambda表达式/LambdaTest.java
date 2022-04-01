package 类与接口.Lambda表达式;

import java.util.Arrays;
import java.util.stream.Stream;

public class LambdaTest {

    public static void main(String[] args) {

        LambdaTest test = new LambdaTest();
        test.constructorReferenceTest();
    }

    public void lambdaTest1() {
        /**
         * 使用Lambda表达式创建一个Comparator实例
         */
        String[] strs = {"stephen", "klay", "draymond", "andre"};
        Arrays.sort(strs, (str1, str2) -> str1.length() - str2.length());
        System.out.println(Arrays.toString(strs));
    }

    /**
     * 方法引用示例
     */
    public void methodReferenceTest() {
        String[] strs = {"stephen", "klay", "draymond", "andre"};
        // 按照字符串的字典序进行排序，忽略大小写
        Arrays.sort(strs, String::compareToIgnoreCase);
        System.out.println(Arrays.toString(strs));
    }

    /**
     * 构造器引用示例
     */
    public void constructorReferenceTest() {
        String[] strs = {"stephen", "klay", "draymond", "andre"};
        Stream<Player> stream = Arrays.stream(strs).map(Player::new);
        Player[] players = stream.toArray(Player[]::new);
        System.out.println(Arrays.toString(players));
    }

    class Player {
        String name;

        public Player(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

}
