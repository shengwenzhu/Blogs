package com.zsw.leetcode.editor.cn;

public class N479_LargestPalindromeProduct {
    public static void main(String[] args) {
        Solution solution = new N479_LargestPalindromeProduct().new Solution();
        int res = solution.largestPalindrome(2);
        System.out.println(res);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        /**
         * 两个n位整数的乘积要么是2n位，要么是2n-1位
         * 从大到小构造回文数，然后判断该回文数是否可以分解为两个n位整数
         * 这个题解的依据时要寻找的最大回文数一定是2n位（除了位数为1时最大回文数为9特殊以外），所以构造回文数时不需要构造奇数位的回文数
         * 构造回文数只需要构造回文数的左半部分，然后右半部分通过左半部分翻转就可以构造出回文数
         *
         * @param n
         * @return
         */
        public int largestPalindrome(int n) {
            if (n == 1) {
                return 9;
            }
            // n位数最大值
            int max = (int) (Math.pow(10, n) - 1);
            int res = 0;
            // 回文数的左半部分
            int upper = max;
            while (res == 0) {
                // 翻转左半部分到其自身末尾，构造回文数temp
                long temp = upper;
                for (int x = upper; x > 0; x /= 10) {
                    temp = temp * 10 + x % 10;
                }
                // 判断回文数是否可以分解为两个n位整数
                for (int i = max; Math.pow(i, 2) >= temp; i--) {
                    if (temp % i == 0) {
                        res = (int) (temp % 1337);
                        break;
                    }
                }
                upper--;
            }
            return res;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)

}
