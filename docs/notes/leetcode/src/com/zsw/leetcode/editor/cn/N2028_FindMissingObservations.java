package com.zsw.leetcode.editor.cn;

public class N2028_FindMissingObservations {
    public static void main(String[] args) {
        Solution solution = new N2028_FindMissingObservations().new Solution();
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int[] missingRolls(int[] rolls, int mean, int n) {
            int m = rolls.length;
            // 计算未遗失数据的总和
            int sum = 0;
            for (int i = 0; i < m; i++) {
                sum += rolls[i];
            }
            // 计算遗失数据的总和
            int sumMissed = mean * (m + n) - sum;
            // 不存在符合要求的答案
            if (sumMissed < n || sumMissed > n * 6) {
                return new int[0];
            }
            // 存在可行答案
            int[] res = new int[n];
            /**
             * 寻找可行方案
             * 我第一次做的方式是每次遍历确定一个数据，如下，比较费时，不建议这样做
             *         for (int i = 0; i < n; i++) {
             *             int meanMissed = sumMissed / (n - i);
             *             res[i] = meanMissed;
             *             sumMissed -= meanMissed;
             *         }
             * leetcode官方题解效率更高：先计算遗失数据的平均值meanMissed，然后进行求余remainder，
             * 那么在缺失的n个数据中，有remainder个数据的值是meanMissed+1，其余数据都是meanMissed
             */
            int meanMissed = sumMissed / n;
            int remainder = sumMissed % n;
            for (int i = 0; i < n; i++) {
                res[i] = meanMissed + (i < remainder ? 1 : 0);
            }
            return res;
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}
