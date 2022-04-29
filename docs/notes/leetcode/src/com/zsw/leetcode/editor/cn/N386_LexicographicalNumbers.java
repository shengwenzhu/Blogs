package com.zsw.leetcode.editor.cn;

import java.util.ArrayList;
import java.util.List;

public class N386_LexicographicalNumbers {
    public static void main(String[] args) {
        Solution solution = new N386_LexicographicalNumbers().new Solution();
        System.out.println(solution.lexicalOrder(25).toString());
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        /**
         * 对于一个整数num，按照字典序顺序，如果num*10<=n,其下一个整数为num*10
         * 如果num*10>n，需要判断此时num个位数是否为9或者num+1>n，表示当前数位已经遍历结束，将num回退到上一位，循环进行这一步
         *
         * @param n
         * @return
         */
        public List<Integer> lexicalOrder(int n) {
            List<Integer> res = new ArrayList<>();
            int num = 1;
            for (int i = 0; i < n; i++) {
                res.add(num);
                if (num * 10 <= n) {
                    num *= 10;
                } else {
                    while (num % 10 == 9 || num + 1 > n) {
                        num /= 10;
                    }
                    num++;
                }
            }
            return res;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)

}
