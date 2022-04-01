package com.zsw.leetcode.editor.cn;

public class N693_BinaryNumberWithAlternatingBits {
    public static void main(String[] args) {
        Solution solution = new N693_BinaryNumberWithAlternatingBits().new Solution();
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public boolean hasAlternatingBits(int n) {
            // 获取二级制最后一位的值
            int temp = n % 2;
            n = n / 2;
            while (n > 0) {
                int remainder = n % 2;
                if (remainder == temp) {
                    return false;
                }
                temp = remainder;
                n = n / 2;
            }
            return true;
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}
