package com.zsw.leetcode.editor.cn;

public class N509_FibonacciNumber {
    public static void main(String[] args) {
        Solution solution = new N509_FibonacciNumber().new Solution();
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int fib(int n) {
            if (n < 2) {
                return n;
            }
            int num1 = 0, num2 = 1, res = 0;
            for (int i = 2; i <= n; i++) {
                res = num1 + num2;
                num1 = num2;
                num2 = res;
            }
            return res;
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}
