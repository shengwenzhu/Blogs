package com.zsw.leetcode.editor.cn;

public class N868_BinaryGap {
    public static void main(String[] args) {
        Solution solution = new N868_BinaryGap().new Solution();
        System.out.println(solution.binaryGap(5));
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int binaryGap(int n) {
            int maxGap = 0;
            // 记录二进制中上一个1的索引
            int index = -1;
            for (int i = 0; n != 0; i++) {
                int temp = n % 2;
                if (temp == 1) {
                    if (index != -1) {
                        maxGap = Math.max(i - index, maxGap);
                    }
                    index = i;
                }
                n /= 2;
            }
            return maxGap;
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}
