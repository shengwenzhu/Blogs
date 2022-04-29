package com.zsw.leetcode.editor.cn;

import java.util.Random;

public class N398_RandomPickIndex {
    public static void main(String[] args) {
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        int[] nums;
        Random random;

        public Solution(int[] nums) {
            this.nums = nums;
            random = new Random();
        }

        public int pick(int target) {
            int res = -1;
            // 记录第几次遇到target
            int count = 0;
            for (int i = 0; i < nums.length; i++) {
                if(nums[i] == target) {
                    count++;
                    // 第一个target被选中的概率为1，其余target被选中的概率为 1/count
                    if(random.nextInt(count) == 0) {
                        res = i;
                    }
                }
            }
            return res;
        }
    }

/**
 * Your Solution object will be instantiated and called as such:
 * Solution obj = new Solution(nums);
 * int param_1 = obj.pick(target);
 */
//leetcode submit region end(Prohibit modification and deletion)

}
