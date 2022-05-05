package com.zsw.leetcode.editor.cn;

import java.util.Arrays;

public class N908_SmallestRangeI {
    public static void main(String[] args) {
        Solution solution = new N908_SmallestRangeI().new Solution();
        int[] nums = {9, 9, 2, 8, 7};
        int k = 4;
        System.out.println(solution.smallestRangeI(nums, k));
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int smallestRangeI(int[] nums, int k) {
            int min = Arrays.stream(nums).min().getAsInt();
            int max = Arrays.stream(nums).max().getAsInt();
            int res = max - min <= 2 * k ? 0 : max - min - 2 * k;
            return res;
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}
